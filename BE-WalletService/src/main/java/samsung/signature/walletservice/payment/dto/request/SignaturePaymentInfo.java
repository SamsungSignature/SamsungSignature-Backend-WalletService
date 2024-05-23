package samsung.signature.walletservice.payment.dto.request;

import lombok.Builder;

@Builder
public record SignaturePaymentInfo(
	Integer amount,
	String cardToken
) {
	public static SignaturePaymentInfo from(final PaymentInfo paymentInfo) {
		return SignaturePaymentInfo.builder()
			.amount(paymentInfo.price())
			.cardToken(paymentInfo.cardToken())
			.build();
	}
}
