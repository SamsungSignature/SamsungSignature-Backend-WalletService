package samsung.signature.walletservice.card.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record ExistsSignatureCardResponse(
	@JsonProperty("is_exists_signature_card") boolean isExists
) {
}
