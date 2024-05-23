package samsung.signature.walletservice.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import samsung.signature.walletservice.payment.domain.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
