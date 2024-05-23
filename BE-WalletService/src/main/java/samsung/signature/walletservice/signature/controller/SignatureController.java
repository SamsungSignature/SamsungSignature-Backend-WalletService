package samsung.signature.walletservice.signature.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import samsung.signature.walletservice.card.dto.response.CardInfo;
import samsung.signature.walletservice.card.dto.response.CardInfoToken;
import samsung.signature.walletservice.card.dto.response.ExistsSignatureCardResponse;
import samsung.signature.walletservice.card.service.CardService;

@RequiredArgsConstructor
@RequestMapping("/wallet-service")
@RestController
public class SignatureController {
	private final CardService cardService;

	@PostMapping("/v1/card-infos")
	public Map<Long, CardInfo> getCardInfo(
		@RequestBody final List<Long> cardIds
	) {
		return cardService.cardInfoGet(cardIds);
	}

	@GetMapping("/v1/cards/{card_id}")
	public CardInfoToken getCardInfoToken(
		@RequestHeader(name = "Member-Id", required = true) Long memberId,
		@PathVariable(name = "card_id") Long cardId
	) {
		return cardService.cardInfoTokenGet(memberId, cardId);
	}

	@GetMapping("/v1/signature")
	public ExistsSignatureCardResponse isExistsSignatureCard(
		@RequestHeader(name = "Member-Id", required = true) Long memberId) {
		return ExistsSignatureCardResponse.builder().isExists(cardService.getExistsSignatureCard(memberId)).build();
	}

	@PutMapping("/v1/signature-tokens")
	public void saveSignatureToken(
		@RequestHeader(name = "Member-Id", required = true) Long memberId,
		@RequestBody String encodedToken
	) {
		cardService.saveSignatureToken(memberId, encodedToken);
	}

	@DeleteMapping("/v1/signature-tokens/{member_id}")
	void deleteSignatureToken(@PathVariable("member_id") final Long memberId) {
		cardService.deleteSignatureToken(memberId);
	}
}
