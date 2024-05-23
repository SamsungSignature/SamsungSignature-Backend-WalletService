package samsung.signature.walletservice.payment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import samsung.signature.common.response.MessageBody;
import samsung.signature.common.response.ResponseEntityFactory;
import samsung.signature.walletservice.payment.dto.request.Infos;
import samsung.signature.walletservice.payment.service.PaymentService;

@RequiredArgsConstructor
@RequestMapping("/wallet-service")
@RestController
public class VanController {
	private final PaymentService paymentService;

	@PostMapping("/v1/payments")
	public ResponseEntity<MessageBody<Void>> pay(@RequestBody Infos infos) {
		paymentService.pay(infos);
		return ResponseEntityFactory.status(HttpStatus.ACCEPTED, "결제가 성공적으로 진행되었습니다.");

	}
}
