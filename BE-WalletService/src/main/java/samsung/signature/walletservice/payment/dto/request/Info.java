package samsung.signature.walletservice.payment.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record Info(
	@JsonProperty("price") int price,
	@JsonProperty("market_name") String marketName
) {
}
