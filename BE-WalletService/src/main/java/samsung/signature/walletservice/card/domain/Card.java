package samsung.signature.walletservice.card.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import samsung.signature.walletservice.card.dto.request.CardSaveRequest;
import samsung.signature.walletservice.global.domain.BaseTime;
import samsung.signature.walletservice.member.domain.Member;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "cards_tbl")
public class Card extends BaseTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "card_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Column(name = "card_img")
	private String cardImg;

	@Column(name = "card_name")
	private String cardName;

	@Column(name = "card_company")
	@Enumerated(EnumType.STRING)
	private CardCompanyType cardCompany;

	public static Card of(CardSaveRequest cardSaveRequest, Member member){
		return Card.builder()
			.cardName(cardSaveRequest.cardName())
			.member(member)
			.cardImg(cardSaveRequest.cardImg())
			.cardCompany(cardSaveRequest.cardCompany())
			.build();
	}
}
