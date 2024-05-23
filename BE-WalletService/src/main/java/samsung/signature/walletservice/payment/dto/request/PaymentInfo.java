package samsung.signature.walletservice.payment.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record PaymentInfo(
	@JsonProperty("price")
	Integer price,
	@JsonProperty("card_company")
	String cardCompany,
	@JsonProperty("card_token")
	String cardToken
) {
}

