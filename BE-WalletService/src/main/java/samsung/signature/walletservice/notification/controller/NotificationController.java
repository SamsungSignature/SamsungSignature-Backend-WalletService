package samsung.signature.walletservice.notification.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import samsung.signature.walletservice.notification.service.NotificationService;

@RequiredArgsConstructor
@RequestMapping("/wallet-service")
@RestController
public class NotificationController {
	private final NotificationService notificationService;

	@GetMapping("/v1/notifications/subscribe")
	public SseEmitter subscribe(
		@RequestHeader(name = "Member-Id", required = true) final long memberId,
		HttpServletResponse response
	) throws IOException {
		// NGINX REVERSE PLOXY PROTECT BUFFERING
		response.setHeader("X-Accel-Buffering", "no");
		return notificationService.subscribe(memberId);
	}
}
