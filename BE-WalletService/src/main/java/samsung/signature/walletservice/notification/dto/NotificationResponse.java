package samsung.signature.walletservice.notification.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record NotificationResponse (
	@JsonProperty("price")
	Integer price,
	@JsonProperty("market_name")
	String marketName,
	@JsonProperty("message")
	String message
) {
}