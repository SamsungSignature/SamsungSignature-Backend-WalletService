package samsung.signature.walletservice.card.repository.query;

import java.util.Optional;

import samsung.signature.walletservice.card.domain.CardToken;

public interface CardTokenRepositoryCustom {
	Optional<CardToken> findSignatureCardTokenByMemberId(Long memberId);
	Optional<CardToken> findSignatureCardTokenByCardId(Long cardId);

}
