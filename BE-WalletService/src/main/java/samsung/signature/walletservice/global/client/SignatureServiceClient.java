package samsung.signature.walletservice.global.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import samsung.signature.walletservice.payment.dto.request.SignaturePaymentInfo;

@FeignClient(name = "signature-service")
public interface SignatureServiceClient {

	@PostMapping("/signature-service/v1/payments")
	String useSignatureCard(
		@RequestBody SignaturePaymentInfo signaturePaymentInfo);
}
