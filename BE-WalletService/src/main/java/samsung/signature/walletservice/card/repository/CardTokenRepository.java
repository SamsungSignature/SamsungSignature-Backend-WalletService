package samsung.signature.walletservice.card.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import samsung.signature.walletservice.card.domain.CardToken;
import samsung.signature.walletservice.card.repository.query.CardTokenRepositoryCustom;

@Repository
public interface CardTokenRepository extends JpaRepository<CardToken, Long>, CardTokenRepositoryCustom {
	void deleteByCardId(Long cardId);

}
