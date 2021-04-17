package babble.webflux.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import babble.webflux.constants.JwtProperties;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SaveService {

	public String getFolder(String time) {
		String[] seperatedTime = time.split("-");
		return seperatedTime[0] + "\\" + seperatedTime[1] + "\\" + seperatedTime[2];
	}

	public Mono<String> saveImage(Flux<FilePart> file) throws Exception {

		return file.flatMap(it -> {
			String uploadFolder = "C:\\imagetest\\";

			// 파일명 검증(해킹이나 오류 막기위해)
			String filename = it.filename();
			if (filename.contains("..")) {
				return null;
			}
			
			String[] seperatedName = filename.split("\\.");

			// 폴더 생성
			File uploadPath = new File(uploadFolder + seperatedName[0] + "." + seperatedName[1],
					getFolder(seperatedName[2]));
			if (uploadPath.exists() == false) {
				uploadPath.mkdirs();
			}

			return it.transferTo(Paths.get(uploadPath + "\\" + it.filename()));
		}).then(Mono.just("OK"));

	}

	public Mono<String> saveAudio(Flux<FilePart> file) throws Exception {
		return file.flatMap(it -> {
			String uploadFolder = "C:\\audiotest\\";

			// 파일명 검증(해킹이나 오류 막기위해)
			String filename = it.filename();
			if (filename.contains("..")) {
				return null;
			}
			String[] seperatedName = filename.split("\\.");

			// 폴더 생성
			File uploadPath = new File(uploadFolder + seperatedName[0] + "." + seperatedName[1],
					getFolder(seperatedName[2]));
			if (uploadPath.exists() == false) {
				uploadPath.mkdirs();
			}

			return it.transferTo(Paths.get(uploadPath + "\\" + it.filename()));
		}).then(Mono.just("OK"));

	}

	public String checkJwt(ServerHttpRequest request) {

		String header = request.getHeaders().get("Authorization").get(0);
		String result = "fail";
		if (header != null && header.startsWith(JwtProperties.TOKEN_PREFIX)) {

			String token = header.replace(JwtProperties.TOKEN_PREFIX, "");
			DecodedJWT jwtToken = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token);

			if (jwtToken != null) {

				String ip = request.getRemoteAddress().getAddress().getHostName();
				String agent = request.getHeaders().get("User-Agent").get(0);
				String jwtUserIp = jwtToken.getClaim("ip").asString();
				String jwtUserAgent = jwtToken.getClaim("User-Agent").asString();

				if (ip.equals(jwtUserIp) && agent.equals(jwtUserAgent)) {
					result = jwtToken.getClaim("username").asString();
				}

			}

		}
		return result;
	}

}
