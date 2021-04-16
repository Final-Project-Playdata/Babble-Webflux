package babble.webflux.controller;

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
			@RequestHeader(value = "Range", required = false) String httpRangeList, ServerHttpRequest request,
			@PathVariable("path") String path) throws Exception {

//		String result = saveService.checkJwt(request);
//
//		System.out.println(path);
//		if (result.equals("fail")) {
//			throw new Exception("인증실패");
//		}

		path = path.replace("-", "/");
		System.out.println(path);

		return Mono.just(streamService.streamAudio(path, httpRangeList));
	}

	@GetMapping("image")
	public Mono<ResponseEntity<byte[]>> streamImage(
			@RequestHeader(value = "Range", required = false) String httpRangeList, ServerHttpRequest request)
			throws Exception {

//		String result = saveService.checkJwt(request);
//
//		if (result.equals("fail")) {
//			throw new Exception("인증실패");
//		}
//		String path = "‪C:\\ITstudy\\12.project\\python\\011.jpeg"; //에러발생
		String path = "C:\\ITstudy\\12.project\\BabbleWebflux\\..\\python\\011.jpeg";

		Path currentDir = Paths.get("../../"); // currentDir = "."
		Path fullPath = currentDir.toAbsolutePath(); // fullPath = "/Users/guest/workspace"

		System.out.println(fullPath);
		System.out.println(path);
		return Mono.just(streamService.streamImage(path, httpRangeList));
	}

}
