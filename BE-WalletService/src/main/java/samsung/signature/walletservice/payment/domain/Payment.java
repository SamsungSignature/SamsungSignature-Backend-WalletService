package samsung.signature.walletservice.payment.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import samsung.signature.walletservice.card.domain.Card;
import samsung.signature.walletservice.global.domain.BaseTime;
import samsung.signature.walletservice.payment.dto.request.Infos;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "payments_tbl")
public class Payment extends BaseTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "card_id")
	private Card card;

	@NotBlank
	@Column(name = "market_name")
	private String marketName;

	@Column(name = "total_amount")
	private int totalAmount;

	@Builder
	public Payment(Card card, String marketName, int totalAmount) {
		this.card = card;
		this.marketName = marketName;
		this.totalAmount = totalAmount;
	}

	public static Payment of(final Infos infos, final Card giverCard) {
		return Payment.builder()
			.card(giverCard)
			.marketName(infos.paymentInfo().marketName())
			.totalAmount(infos.paymentInfo().price())
			.build();
	}
}
