package samsung.signature.walletservice.payment.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import samsung.signature.common.response.ExceptionCode;
import samsung.signature.common.response.ResponseEntityExceptionFactory;

@RestControllerAdvice
public class PaymentExceptionHandler {

	@ExceptionHandler({PaymentException.class})
	public ResponseEntity<ExceptionCode> handlePaymentException(PaymentException e) {
		return ResponseEntityExceptionFactory.exception(e);
	}
}
