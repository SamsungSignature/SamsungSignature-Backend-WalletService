package samsung.signature.walletservice.payment.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import samsung.signature.common.exception.ErrorCode;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum PaymentErrorCode implements ErrorCode {
	NOT_ACCEPTABLE(406, "PAYMENT_002", "결제가 실패하였습니다."),;

	private final int statusCode;
	private final String errorCode;
	private String message;

	public PaymentErrorCode modifyErrorMessage(String message) {
		this.message = message;
		return this;
	}
}
