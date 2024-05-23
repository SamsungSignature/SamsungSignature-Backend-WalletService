package samsung.signature.walletservice.card.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import samsung.signature.walletservice.card.domain.CardCompanyType;

@Builder
public record CardSaveRequest(
	@JsonProperty("card_num")
	String cardNum,
	@JsonProperty("card_expiration_date")
	String cardExpirationDate,
	@JsonProperty("card_cvc")
	String cardCvc,
	@JsonProperty("card_name")
	String cardName,
	@JsonProperty("card_company")
	CardCompanyType cardCompany,
	@JsonProperty("card_img")
	String cardImg
) {
}
