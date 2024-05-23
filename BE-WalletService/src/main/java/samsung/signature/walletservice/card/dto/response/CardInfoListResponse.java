package samsung.signature.walletservice.card.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record CardInfoListResponse(
	@JsonProperty("cards") List<CardInfo> list
) {
}
