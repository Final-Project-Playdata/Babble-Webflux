package babble.webflux.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
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

	@GetMapping("index")
	public String index() {
		System.out.println("index");
		return "index";
	}

	@CrossOrigin
	@GetMapping("audio/{path}")
	public Mono<ResponseEntity<byte[]>> streamAudio(
			@RequestHeader(value = "Range", required = false) String httpRangeList, @PathVariable("path") String path)
			throws Exception {

		return Mono.just(streamService.streamAudio(path, httpRangeList));
	}

	@GetMapping("image/{path}")
	public Mono<ResponseEntity<byte[]>> streamImage(
			@RequestHeader(value = "Range", required = false) String httpRangeList, @PathVariable("path") String path)
			throws Exception {

		return Mono.just(streamService.streamImage(path, httpRangeList));
	}

}
