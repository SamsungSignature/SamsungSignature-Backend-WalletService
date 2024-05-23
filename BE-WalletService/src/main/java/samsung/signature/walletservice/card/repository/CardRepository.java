package samsung.signature.walletservice.card.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import samsung.signature.walletservice.card.domain.Card;
import samsung.signature.walletservice.card.domain.CardCompanyType;
import samsung.signature.walletservice.card.repository.query.CardRepositoryCustom;

@Repository
public interface CardRepository extends JpaRepository<Card, Long>, CardRepositoryCustom {
	boolean existsByMemberIdAndCardCompany(Long memberId, CardCompanyType type);

	Optional<Card> findCardByCardCompanyAndMember_Id(CardCompanyType type, Long memberId);
}
