package babble.webflux.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import babble.webflux.service.StreamService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class StreamController {

	private final StreamService streamService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@CrossOrigin
	@GetMapping("audio/{path}")
	public Mono<ResponseEntity<byte[]>> streamAudio(
			@RequestHeader(value = "Range", required = false) String httpRangeList, @PathVariable("path") String path)
			throws Exception {

		if (path == null) {
			throw new Exception("경로가 존재하지 않습니다.");
		}
		
		logger.info("Audio : {}", path);

		return Mono.just(streamService.streamAudio(path, httpRangeList));
	}

	@GetMapping("image/{path}")
	public Mono<ResponseEntity<byte[]>> streamImage(
			@RequestHeader(value = "Range", required = false) String httpRangeList, @PathVariable("path") String path)
			throws Exception {

		if (path == null) {
			throw new Exception("경로가 존재하지 않습니다.");
		}

		logger.info("Image : {}", path);
		
		return Mono.just(streamService.streamImage(path, httpRangeList));
	}

}
