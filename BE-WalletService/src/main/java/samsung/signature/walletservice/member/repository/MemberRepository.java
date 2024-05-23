package samsung.signature.walletservice.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import samsung.signature.walletservice.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
