package samsung.signature.walletservice.card.util;

import org.springframework.dao.EmptyResultDataAccessException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import samsung.signature.common.exception.SignatureException;
import samsung.signature.walletservice.card.domain.Card;
import samsung.signature.walletservice.card.domain.CardToken;
import samsung.signature.walletservice.card.dto.response.CardInfoToken;
import samsung.signature.walletservice.card.exception.CardErrorCode;
import samsung.signature.walletservice.card.repository.CardRepository;
import samsung.signature.walletservice.card.repository.CardTokenRepository;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CardServiceUtil {
	public static Card getCard(
		final CardRepository cardRepository,
		final Long cardId
	) {
		return cardRepository.findById(cardId)
			.orElseThrow(() -> new SignatureException(CardErrorCode.NOT_EXIST_CARD));
	}

	public static void deleteCard(
		CardRepository cardRepository,
		CardTokenRepository cardTokenRepository,
		Long cardId
	) {
		try {
			cardRepository.deleteById(cardId);
			cardTokenRepository.deleteByCardId(cardId);
		} catch (EmptyResultDataAccessException e) {
			throw new SignatureException(CardErrorCode.NOT_EXIST_CARD);
		}
	}

	public static CardInfoToken getCardInfoToken(
		final CardRepository cardRepository,
		final Long memberId,
		final Long cardId
	) {
		return cardRepository.findCardInfoTokenByCardId(memberId, cardId)
			.orElseThrow(() -> new SignatureException(CardErrorCode.NOT_EXIST_CARD));
	}

	public static CardToken getSignatureCardToken(
		final CardTokenRepository cardTokenRepository,
		final Long memberId
	){
		return cardTokenRepository.findSignatureCardTokenByMemberId(memberId)
			.orElseThrow(() -> new SignatureException(CardErrorCode.NOT_EXIST_CARD));
	}
}
