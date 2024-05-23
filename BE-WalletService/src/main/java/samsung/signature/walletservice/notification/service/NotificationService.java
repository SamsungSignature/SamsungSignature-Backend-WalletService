package samsung.signature.walletservice.notification.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;
import samsung.signature.walletservice.notification.repository.EmitterRepository;

@RequiredArgsConstructor
@Service
public class NotificationService {
	private final EmitterRepository emitterRepository;

	public SseEmitter subscribe(long memberId) throws IOException {
		return emitterRepository.save(memberId);
	}

	public void publish(long memberId, Object data){
		SseEmitter emitter = emitterRepository.findById(memberId);
		if (emitter != null) {
			try {
				emitter.send(SseEmitter.event().data(data));
			} catch (Exception e) {
				emitterRepository.deleteById(memberId);
			}
		}
	}
}
