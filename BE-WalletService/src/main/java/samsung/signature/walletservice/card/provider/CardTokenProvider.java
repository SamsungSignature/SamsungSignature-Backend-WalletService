package samsung.signature.walletservice.card.provider;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import samsung.signature.walletservice.card.dto.request.CardSaveRequest;

@Component
public class CardTokenProvider {
	private final Key key;

	public CardTokenProvider(@Value("${jwt.secret-key}") String key) {
		byte[] keyBytes = Decoders.BASE64.decode(key);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	public String createToken(
		Long memberId,
		CardSaveRequest cardSaveRequest
	) {
		Claims claims = Jwts.claims().setSubject(String.valueOf(memberId));
		// TODO: 카드 정보 claims에서 제외
		claims.put("cardNum", cardSaveRequest.cardNum());
		claims.put("cardCvc", cardSaveRequest.cardCvc());
		claims.put("cardExpirationDate", cardSaveRequest.cardExpirationDate());

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(new Date())
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}
}
