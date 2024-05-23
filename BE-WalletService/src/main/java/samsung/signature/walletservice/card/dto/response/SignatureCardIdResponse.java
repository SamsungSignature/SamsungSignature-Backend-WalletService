package samsung.signature.walletservice.card.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import samsung.signature.walletservice.card.domain.Card;

@Builder
public record SignatureCardIdResponse(
	@JsonProperty("signature_card_id") Long signatureCardId
) {

	public static SignatureCardIdResponse of(
		final Card signatureCard
	) {
		return SignatureCardIdResponse.builder().signatureCardId(signatureCard.getId()).build();
	}
}
