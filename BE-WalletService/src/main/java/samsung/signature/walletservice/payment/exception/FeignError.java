package samsung.signature.walletservice.payment.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FeignError(
	@JsonProperty("message")
	String message,
	@JsonProperty("code")
	String code
) {
}
