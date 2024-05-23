package samsung.signature.walletservice.card.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import samsung.signature.walletservice.global.domain.BaseTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "card_tokens_tbl")
public class CardToken extends BaseTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "card_token_id")
	private Long id;

	@Column(name = "card_id")
	private Long cardId;

	@Column(name = "card_token")
	private String cardToken;

	@Builder
	public CardToken(Long cardId, String cardToken) {
		this.cardId = cardId;
		this.cardToken = cardToken;
	}

	public static CardToken of(Card card, String cardToken) {
		return CardToken.builder()
			.cardId(card.getId())
			.cardToken(cardToken)
			.build();
	}

	public CardToken insertToken(final String encodedToken) {
		this.cardToken = encodedToken;
		return this;
	}
}
