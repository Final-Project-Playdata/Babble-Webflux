package babble.webflux.controller;

import java.nio.file.Paths;

import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import babble.webflux.service.SaveService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class SaveController {

	private final SaveService saveService;

	@CrossOrigin
	@PostMapping(value="audio", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Mono<String> saveAudio(@RequestPart("audio") Flux<FilePart> file) throws Exception {

//		String result = saveService.checkJwt(request);
//
//		if (result.equals("fail")) {
//			throw new Exception("인증실패");
//		}
//		System.out.println(request.getHeaders());
//		String requestPath = "C:\\ITstudy\\12.project\\BabbleWebflux\\..\\python\\";
//		Path path = Paths.get(requestPath + file.filename());
//		try {
//			file.transferTo(path);
//			System.out.println(file);
////			saveService.saveAudio(audio);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		return file.flatMap(it -> it.transferTo(Paths.get("C:\\ITstudy\\12.project\\BabbleWebflux\\..\\audio\\" + it.filename())))
		        .then(Mono.just("OK"));
		
	}

	@PostMapping("image")
	public String savetImage(@RequestParam("file") MultipartFile file, ServerHttpRequest request) throws Exception {

//		String result = saveService.checkJwt(request);
//
//		if (result.equals("fail")) {
//			throw new Exception("인증실패");
//		}
//
		saveService.saveImage(file);

		return "success";
	}
}
