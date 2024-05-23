package samsung.signature.walletservice.card.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import samsung.signature.common.response.MessageBody;
import samsung.signature.common.response.ResponseEntityFactory;
import samsung.signature.walletservice.card.dto.request.CardSaveRequest;
import samsung.signature.walletservice.card.dto.response.CardInfoListResponse;
import samsung.signature.walletservice.card.dto.response.CardSaveResponse;
import samsung.signature.walletservice.card.dto.response.SignatureCardIdResponse;
import samsung.signature.walletservice.card.service.CardService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/wallet-service")
@RestController
public class CardController {
	private final CardService cardService;

	@PostMapping("/v1/cards")
	public ResponseEntity<MessageBody<CardSaveResponse>> saveCard(
		@RequestHeader(name = "Member-Id", required = true) long memberId,
		@RequestBody CardSaveRequest cardSaveRequest
	) {
		return ResponseEntityFactory.created("wallet 카드 등록 완료", cardService.cardSave(memberId, cardSaveRequest));
	}

	@DeleteMapping("/v1/cards/{card_id}")
	public ResponseEntity<MessageBody<Void>> deleteCard(
		@RequestHeader(name = "Member-Id", required = true) long memberId,
		@PathVariable(name = "card_id") Long cardId
	) {
		cardService.cardDelete(memberId, cardId);
		return ResponseEntityFactory.ok("wallet 카드 삭제 완료");
	}

	@GetMapping("/v1/cards")
	public ResponseEntity<MessageBody<CardInfoListResponse>> getCardList(
		@RequestHeader(name = "Member-Id", required = true) Long memberId) {
		return ResponseEntityFactory.ok("wallet 카드(신용카드+signature) 목록 조회 성공", cardService.getCardList(memberId));
	}

	@GetMapping("/v1/payable-cards")
	public ResponseEntity<MessageBody<CardInfoListResponse>> getPayableCardList(
		@RequestHeader(name = "Member-Id", required = true) Long memberId,
		@RequestParam(name = "signature_detail_id", required = false, defaultValue = "-1") Long signatureDetailId) {
		return ResponseEntityFactory.ok("wallet 신용 카드 목록 조회 성공",
			cardService.getPayableCardList(memberId, signatureDetailId));
	}

	@GetMapping("/v1/signature-card")
	public ResponseEntity<MessageBody<SignatureCardIdResponse>> getSignatureCardId(
		@RequestHeader(name = "Member-Id", required = true) Long memberId
	) {
		return ResponseEntityFactory.ok("시그니처 카드 id 조회 성공", cardService.getSignatureCardId(memberId));
	}

}
