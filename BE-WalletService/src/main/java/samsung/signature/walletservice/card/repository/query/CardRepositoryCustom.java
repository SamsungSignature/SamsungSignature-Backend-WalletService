package samsung.signature.walletservice.card.repository.query;

import java.util.List;
import java.util.Optional;

import samsung.signature.walletservice.card.domain.Card;
import samsung.signature.walletservice.card.dto.response.CardInfo;
import samsung.signature.walletservice.card.dto.response.CardInfoToken;

public interface CardRepositoryCustom {
	List<CardInfo> findAllById(List<Long> cardId);

	Optional<Card> findSignatureCardByMemberId(Long memberId);

	Optional<CardInfoToken> findCardInfoTokenByCardId(final Long memberId, final Long cardId);

	List<CardInfo> findAllByMember(Long memberId);

	List<CardInfo> findAllByMemberAndPayable(Long memberId);

	boolean existSignatureCardByMemberId(Long memberId);

	Optional<Card> findCardByCardToken(final String cardToken);

	void deleteSignatureToken(final Long memberId);
}
