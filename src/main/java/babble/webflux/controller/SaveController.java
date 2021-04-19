package babble.webflux.controller;

import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import babble.webflux.service.SaveService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class SaveController {

	private final SaveService saveService;

	@CrossOrigin
	@PostMapping(value = "audio", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Mono<String> saveAudio(@RequestPart("audio") Flux<FilePart> file, ServerHttpRequest request)
			throws Exception {

		String result = saveService.checkJwt(request);

		if (result.equals("fail")) {
			throw new Exception("인증실패");
		}
		return saveService.saveAudio(file);

	}
	
	@CrossOrigin
	@PostMapping(value = "image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Mono<String> savetImage(@RequestPart("image") Flux<FilePart> file, ServerHttpRequest request)
			throws Exception {

		String result = saveService.checkJwt(request);
		if (result.equals("fail")) {
			throw new Exception("인증실패");
		}

		return saveService.saveImage(file);
	}
}
