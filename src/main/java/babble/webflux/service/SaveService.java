package babble.webflux.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import babble.webflux.constants.JwtProperties;

@Service
public class SaveService {

	public String getFolder(String time) {
		return time.replace("-", File.separator);
	}

	public void saveImage(MultipartFile file) throws Exception {

		String uploadFolder = "C:/audiotest";

		// 파일명 검증(해킹이나 오류 막기위해)
		String originalFilename = file.getOriginalFilename();
		String[] seperatedName = originalFilename.split(".");
		if (originalFilename.contains("..") || !(seperatedName[0] + "." + seperatedName[2]).equals(seperatedName[0] + ".jpeg")) {
			throw new Exception("올바르지 않은 파일명입니다.");
		}

		// 폴더 생성
		File uploadPath = new File(uploadFolder + "/" + seperatedName[0], getFolder(seperatedName[1]));
		if (uploadPath.exists() == false) {
			uploadPath.mkdirs();
		}

		// 파일 경로 정리
		String fileFullPath = uploadPath + "/" + originalFilename;

		// 저장
		Path saveTO = Paths.get(fileFullPath);
		Files.copy(file.getInputStream(), saveTO);

	}

	public void saveAudio(MultipartFile file) throws Exception {

		String uploadFolder = "C:/audiotest";

		// 파일명 검증(해킹이나 오류 막기위해)
		String originalFilename = file.getOriginalFilename();
		String[] seperatedName = originalFilename.split(".");
		if (originalFilename.contains("..") || !(seperatedName[0] + "." + seperatedName[2]).equals(seperatedName[0] + ".wav")) {
			throw new Exception("올바르지 않은 파일명입니다.");
		}

		// 폴더 생성
		File uploadPath = new File(uploadFolder + "/" + seperatedName[0], getFolder(seperatedName[1]));
		if (uploadPath.exists() == false) {
			uploadPath.mkdirs();
		}

		// 파일 경로 정리
		String fileFullPath = uploadPath + "/" + originalFilename;

		// 저장
		Path saveTO = Paths.get(fileFullPath);
		Files.copy(file.getInputStream(), saveTO);

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
