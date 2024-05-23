package samsung.signature.walletservice.card.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import samsung.signature.common.exception.ErrorCode;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public enum CardErrorCode implements ErrorCode {
	NOT_EXIST_CARD(404, "CARD_001", "존재하지 않는 카드입니다."),
	EXIST_SIGNATURE_CARD(400, "CARD_002", "SIGNATURE 카드가 이미 존재합니다"),
	NOT_EXIST_CARD_TOKEN(404, "CARD_003", "카드토큰이 존재하지 않습니다"),
	;

	private final int statusCode;
	private final String errorCode;
	private final String message;

}
