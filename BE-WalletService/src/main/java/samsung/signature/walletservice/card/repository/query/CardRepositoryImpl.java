package samsung.signature.walletservice.card.repository.query;

import static samsung.signature.walletservice.card.domain.QCard.*;
import static samsung.signature.walletservice.card.domain.QCardToken.*;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.util.Strings;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import samsung.signature.walletservice.card.domain.Card;
import samsung.signature.walletservice.card.domain.CardCompanyType;
import samsung.signature.walletservice.card.dto.response.CardInfo;
import samsung.signature.walletservice.card.dto.response.CardInfoToken;
import samsung.signature.walletservice.card.dto.response.QCardInfo;
import samsung.signature.walletservice.card.dto.response.QCardInfoToken;

@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<CardInfo> findAllById(List<Long> cardId) {
		return queryFactory.select(new QCardInfo(
				card.id,
				card.cardName,
				card.cardCompany.stringValue(),
				card.cardImg))
			.from(card)
			.where(card.id.in(cardId))
			.fetch();
	}

	@Override
	public Optional<Card> findSignatureCardByMemberId(Long memberId) {
		return Optional.ofNullable(
			queryFactory.selectFrom(card)
				.where(card.member.id.eq(memberId)
					.and(card.cardCompany.eq(CardCompanyType.SIGNATURE))
				)
				.fetchOne()
		);
	}

	@Override
	public Optional<CardInfoToken> findCardInfoTokenByCardId(
		final Long memberId,
		final Long cardId
	) {
		return Optional.ofNullable(
			queryFactory.select(new QCardInfoToken(
					card.id,
					card.cardName,
					cardToken1.cardToken)
				)
				.from(card)
				.join(cardToken1)
				.on(card.id.eq(cardToken1.cardId))
				.where(card.id.eq(cardId)
					.and(card.member.id.eq(memberId))
				)
				.fetchOne()
		);
	}

	@Override
	public List<CardInfo> findAllByMember(Long memberId) {
		return queryFactory.select(new QCardInfo(
				card.id,
				card.cardName,
				card.cardCompany.stringValue(),
				card.cardImg))
			.from(card)
			.where(card.member.id.eq(memberId))
			.orderBy(card.createdAt.desc())
			.fetch();
	}

	@Override
	public List<CardInfo> findAllByMemberAndPayable(Long memberId) {
		return queryFactory.select(new QCardInfo(
				card.id,
				card.cardName,
				card.cardCompany.stringValue(),
				card.cardImg))
			.from(card)
			.where(card.member.id.eq(memberId).and(card.cardCompany.ne(CardCompanyType.SIGNATURE)))
			.orderBy(card.createdAt.desc())
			.fetch();
	}

	@Override
	public boolean existSignatureCardByMemberId(Long memberId) {
		Integer fetchOne = queryFactory
			.selectOne()
			.from(card)
			.where(card.member.id.eq(memberId)
				.and(card.cardCompany.eq(CardCompanyType.SIGNATURE))
			)
			.fetchFirst();
		return fetchOne != null;
	}

	@Override
	public Optional<Card> findCardByCardToken(final String cardToken) {
		return Optional.ofNullable(
			queryFactory.selectFrom(card)
				.join(cardToken1)
				.on(card.id.eq(cardToken1.cardId))
				.where(cardToken1.cardToken.eq(cardToken))
				.fetchOne()
		);
	}

	@Override
	public void deleteSignatureToken(Long memberId) {
		queryFactory.update(cardToken1)
			.set(cardToken1.cardToken, Strings.EMPTY)
			.where(cardToken1.cardId.eq(
					queryFactory.select(card.id)
						.from(card)
						.where(card.member.id.eq(memberId)
							.and(card.cardCompany.eq(CardCompanyType.SIGNATURE))
						)
				)
			)
			.execute();
	}
}
