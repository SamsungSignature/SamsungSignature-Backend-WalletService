package samsung.signature.walletservice.payment.service;

import static samsung.signature.walletservice.card.domain.CardCompanyType.*;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import samsung.signature.common.exception.SignatureException;
import samsung.signature.walletservice.card.domain.Card;
import samsung.signature.walletservice.card.exception.CardErrorCode;
import samsung.signature.walletservice.card.repository.CardRepository;
import samsung.signature.walletservice.card.repository.CardTokenRepository;
import samsung.signature.walletservice.global.client.SignatureServiceClient;
import samsung.signature.walletservice.global.util.KafkaProducer;
import samsung.signature.walletservice.notification.dto.NotificationResponse;
import samsung.signature.walletservice.notification.service.NotificationService;
import samsung.signature.walletservice.payment.domain.Payment;
import samsung.signature.walletservice.payment.dto.request.Infos;
import samsung.signature.walletservice.payment.dto.request.PaymentInfo;
import samsung.signature.walletservice.payment.dto.request.SignaturePaymentInfo;
import samsung.signature.walletservice.payment.exception.FeignError;
import samsung.signature.walletservice.payment.exception.PaymentErrorCode;
import samsung.signature.walletservice.payment.exception.PaymentException;
import samsung.signature.walletservice.payment.repository.PaymentRepository;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PaymentService {
	private final PaymentRepository paymentRepository;
	private final CardRepository cardRepository;
	private final CardTokenRepository cardTokenRepository;
	private final SignatureServiceClient signatureServiceClient;
	private final NotificationService notificationService;
	private final KafkaProducer kafkaProducer;

	@Transactional
	public void pay(final Infos infos) {
		try {
			Card card = cardRepository.getReferenceById(infos.cardId());
			String cardTokenFromTrustZone = getCardTokenFromTrustZone(card);

			// 시그니처 카드 결제 로직
			if (isSignatureCard(card)) {
				PaymentInfo paymentInfo = buildPaymentInfo(infos, card, cardTokenFromTrustZone);
				processSignaturePayment(infos, paymentInfo);
				notificationService.publish(card.getMember().getId(), NotificationResponse.builder()
					.price(infos.paymentInfo().price())
					.marketName(infos.paymentInfo().marketName())
					.build()
				);
				return;
			}
			// 일반 카드 결제 로직
			savePayment(infos, card);
			notificationService.publish(card.getMember().getId(), NotificationResponse.builder()
					.price(infos.paymentInfo().price())
					.marketName(infos.paymentInfo().marketName())
					.build()
				);
		} catch (FeignException e) {
			handleFeignException(e);
		} catch (Exception e) {
			throw new PaymentException(PaymentErrorCode.NOT_ACCEPTABLE);
		}
	}

	private String getCardTokenFromTrustZone(Card card) {
		return cardTokenRepository.findSignatureCardTokenByCardId(card.getId())
			.orElseThrow(() -> new PaymentException(CardErrorCode.NOT_EXIST_CARD_TOKEN))
			.getCardToken();
	}

	private boolean isSignatureCard(Card card) {
		return card.getCardCompany().equals(SIGNATURE);
	}

	private PaymentInfo buildPaymentInfo(Infos infos, Card card, String cardTokenFromTrustZone) {
		return PaymentInfo.builder()
			.price(infos.paymentInfo().price())
			.cardCompany(card.getCardCompany().toString())
			.cardToken(cardTokenFromTrustZone)
			.build();
	}

	private void processSignaturePayment(Infos infos, PaymentInfo paymentInfo) throws SignatureException {
		String cardToken = signatureServiceClient.useSignatureCard(SignaturePaymentInfo.from(paymentInfo));
		Card giverCard = findCardByToken(cardToken);
		savePayment(infos, giverCard);
	}

	private Card findCardByToken(String cardToken) throws SignatureException {
		return cardRepository.findCardByCardToken(cardToken)
			.orElseThrow(() -> new PaymentException(CardErrorCode.NOT_EXIST_CARD));
	}

	private void savePayment(Infos infos, Card giverCard) {
		paymentRepository.save(Payment.of(infos, giverCard));
	}

	private void handleFeignException(FeignException e) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			FeignError error = objectMapper.readValue(e.contentUTF8(), FeignError.class);
			throw new PaymentException(PaymentErrorCode.NOT_ACCEPTABLE.modifyErrorMessage(error.message()));
		} catch (IOException ioException) {
			throw new PaymentException(PaymentErrorCode.NOT_ACCEPTABLE);
		}
	}
}
