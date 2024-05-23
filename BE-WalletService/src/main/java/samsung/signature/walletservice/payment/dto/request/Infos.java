package samsung.signature.walletservice.payment.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record Infos(
	@JsonProperty("card_id") Long cardId,
	@JsonProperty("payment_info") Info paymentInfo
) {
}
