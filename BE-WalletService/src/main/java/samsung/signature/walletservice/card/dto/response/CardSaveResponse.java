package samsung.signature.walletservice.card.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record CardSaveResponse(
	@JsonProperty("card_id") Long cardId
) {
}
