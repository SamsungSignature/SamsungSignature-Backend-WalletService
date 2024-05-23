package samsung.signature.walletservice.payment.exception;

import samsung.signature.common.exception.ErrorCode;
import samsung.signature.common.exception.SignatureException;

public class PaymentException extends SignatureException {
	public PaymentException(ErrorCode errorCode) {
		super(errorCode);
	}
}

