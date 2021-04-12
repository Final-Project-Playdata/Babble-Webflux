package babble.webflux.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import babble.webflux.service.SaveService;
import babble.webflux.service.StreamService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class StreamController {

	private final StreamService videoStreamService;

	private final SaveService saveService;

	@GetMapping("index")
	public String index() {
		System.out.println("index");
		return "index";
	}

	@GetMapping("audio")
	public Mono<ResponseEntity<byte[]>> streamAudio(
			@RequestHeader(value = "Range", required = false) String httpRangeList, ServerHttpRequest request,
			@RequestHeader("Path") String path) throws Exception {

		String result = saveService.checkJwt(request);

		if (result.equals("fail")) {
			throw new Exception("인증실패");
		}

		return Mono.just(videoStreamService.streamAudio(path, httpRangeList));
	}

	@GetMapping("image")
	public Mono<ResponseEntity<byte[]>> streamImage(
			@RequestHeader(value = "Range", required = false) String httpRangeList, ServerHttpRequest request, @RequestParam("path") String path)
			throws Exception {

		String result = saveService.checkJwt(request);

		if (result.equals("fail")) {
			throw new Exception("인증실패");
		}

		return Mono.just(videoStreamService.streamImage(path, httpRangeList));
	}

}
