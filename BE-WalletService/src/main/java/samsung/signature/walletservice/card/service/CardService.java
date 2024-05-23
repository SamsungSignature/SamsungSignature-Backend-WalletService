package samsung.signature.walletservice.card.service;

import static samsung.signature.walletservice.card.domain.CardCompanyType.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import samsung.signature.common.exception.ServerErrorCode;
import samsung.signature.common.exception.SignatureException;
import samsung.signature.walletservice.card.domain.Card;
import samsung.signature.walletservice.card.domain.CardCompanyType;
import samsung.signature.walletservice.card.domain.CardToken;
import samsung.signature.walletservice.card.dto.request.CardSaveRequest;
import samsung.signature.walletservice.card.dto.response.CardInfo;
import samsung.signature.walletservice.card.dto.response.CardInfoListResponse;
import samsung.signature.walletservice.card.dto.response.CardInfoToken;
import samsung.signature.walletservice.card.dto.response.CardSaveResponse;
import samsung.signature.walletservice.card.dto.response.SignatureCardIdResponse;
import samsung.signature.walletservice.card.exception.CardErrorCode;
import samsung.signature.walletservice.card.provider.CardTokenProvider;
import samsung.signature.walletservice.card.repository.CardRepository;
import samsung.signature.walletservice.card.repository.CardTokenRepository;
import samsung.signature.walletservice.card.util.CardServiceUtil;
import samsung.signature.walletservice.global.client.SignatureServiceClient;
import samsung.signature.walletservice.global.dto.kafka.PickCardEvent;
import samsung.signature.walletservice.global.util.KafkaProducer;
import samsung.signature.walletservice.member.domain.Member;
import samsung.signature.walletservice.member.repository.MemberRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CardService {
	private final MemberRepository memberRepository;
	private final CardRepository cardRepository;
	private final CardTokenRepository cardTokenRepository;
	private final SignatureServiceClient signatureServiceClient;
	private final CardTokenProvider cardTokenProvider;
	private final KafkaProducer kafkaProducer;
	private final ObjectMapper objectMapper;

	@Transactional
	public CardSaveResponse cardSave(Long memberId, CardSaveRequest cardSaveRequest) {

		// signature card 이미 있으면 에러
		if (isSignature(cardSaveRequest) && cardRepository.existSignatureCardByMemberId(memberId)) {
			throw new SignatureException(CardErrorCode.EXIST_SIGNATURE_CARD);
		}

		Member loginUser = memberRepository.getReferenceById(memberId);
		Card card = Card.of(cardSaveRequest, loginUser);
		Card saveCard = cardRepository.save(card);

		String cardToken = null;
		if (!isSignature(cardSaveRequest)) {
			cardToken = cardTokenProvider.createToken(memberId, cardSaveRequest);
		}
		CardToken token = CardToken.of(card, cardToken);
		cardTokenRepository.save(token);

		return CardSaveResponse.builder().cardId(saveCard.getId()).build();
	}

	private boolean isSignature(CardSaveRequest cardSaveRequest) {
		return cardSaveRequest.cardCompany().equals(SIGNATURE);
	}

	@Transactional
	public void cardDelete(long memberId, Long cardId) {
		// TODO: 삭제하려고하는 카드가 사용자의 것인지 확인 로직 필요
		CardServiceUtil.deleteCard(cardRepository, cardTokenRepository, cardId);
	}

	public Map<Long, CardInfo> cardInfoGet(List<Long> cardId) {
		List<CardInfo> cards = cardRepository.findAllById(cardId);
		return cards.stream()
			.collect(
				Collectors.toMap(
					CardInfo::getId,
					cardInfo -> cardInfo,
					(existing, replacement) -> existing)
			);
	}

	public CardInfoToken cardInfoTokenGet(final Long memberId, final Long cardId) {
		return CardServiceUtil.getCardInfoToken(cardRepository, memberId, cardId);
	}

	public CardInfoListResponse getCardList(Long memberId) {
		return CardInfoListResponse.builder().list(cardRepository.findAllByMember(memberId)).build();
	}

	public CardInfoListResponse getPayableCardList(Long memberId, Long signatureDetailId) {
		try {
			if (signatureDetailId != -1) {
				kafkaProducer.sendMessage("wallet-signature-pickcard-topic",
					objectMapper.writeValueAsString(PickCardEvent.builder()
						.signatureDetailId(signatureDetailId)
						.build())
				);
			}
		} catch (JsonProcessingException e) {
			throw new SignatureException(ServerErrorCode.INTERNAL_SERVER_ERROR);
		}
		List<CardInfo> cards = cardRepository.findAllByMemberAndPayable(memberId);
		return CardInfoListResponse.builder().list(cards).build();
	}

	public Boolean getExistsSignatureCard(Long memberId) {
		return cardRepository.existsByMemberIdAndCardCompany(memberId, CardCompanyType.SIGNATURE);
	}

	@Transactional
	public void saveSignatureToken(final Long memberId, final String encodedToken) {
		CardToken cardToken = CardServiceUtil.getSignatureCardToken(
			cardTokenRepository,
			memberId
		);
		cardTokenRepository.save(cardToken.insertToken(encodedToken));
	}

	public SignatureCardIdResponse getSignatureCardId(Long memberId) {
		return SignatureCardIdResponse.of(
			cardRepository.findCardByCardCompanyAndMember_Id(SIGNATURE, memberId)
				.orElseThrow(() -> new SignatureException(CardErrorCode.NOT_EXIST_CARD)));
	}

	@Transactional
	public void deleteSignatureToken(final Long memberId) {
		cardRepository.deleteSignatureToken(memberId);
	}
}
