package babble.webflux.controller;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import babble.webflux.service.SaveService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SaveController {

	private final SaveService saveService;

	@PostMapping("audio")
	public String saveAudio(@RequestParam(value = "file", required = false) MultipartFile file,
			ServerHttpRequest request) throws Exception {

		String result = saveService.checkJwt(request);

		if (result.equals("fail")) {
			throw new Exception("인증실패");
		}

		saveService.saveAudio(file, result);

		return "success";
	}

	@PostMapping("image")
	public String savetImage(@RequestParam("file") MultipartFile file, ServerHttpRequest request) throws Exception {

		String result = saveService.checkJwt(request);

		if (result.equals("fail")) {
			throw new Exception("인증실패");
		}

		saveService.saveImage(file, result);

		return "success";
	}
}
