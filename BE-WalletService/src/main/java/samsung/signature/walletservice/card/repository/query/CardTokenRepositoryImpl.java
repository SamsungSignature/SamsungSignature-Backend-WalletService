package samsung.signature.walletservice.card.repository.query;

import static samsung.signature.walletservice.card.domain.QCard.*;
import static samsung.signature.walletservice.card.domain.QCardToken.*;

import java.util.Optional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import samsung.signature.walletservice.card.domain.CardCompanyType;
import samsung.signature.walletservice.card.domain.CardToken;

@RequiredArgsConstructor
public class CardTokenRepositoryImpl implements CardTokenRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public Optional<CardToken> findSignatureCardTokenByMemberId(final Long memberId) {
		return Optional.ofNullable(
			queryFactory
				.selectFrom(cardToken1)
				.join(card)
				.on(card.id.eq(cardToken1.cardId))
				.where(card.member.id.eq(memberId)
					.and(card.cardCompany.eq(CardCompanyType.SIGNATURE)))
				.fetchOne()
		);
	}
@Override
	public Optional<CardToken> findSignatureCardTokenByCardId(final Long cardId) {
		return Optional.ofNullable(
			queryFactory
				.selectFrom(cardToken1)
				.join(card)
				.on(card.id.eq(cardToken1.cardId))
				.where(card.id.eq(cardId))
				.fetchOne()
		);
	}
}
