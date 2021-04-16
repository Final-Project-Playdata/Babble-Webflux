package babble.webflux.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import babble.webflux.constants.StreamConstants;

@Service
public class StreamService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Prepare the content.
	 *
	 * @param fileName String.
	 * @param fileType String.
	 * @param range    String.
	 * @return ResponseEntity.
	 */
	public ResponseEntity<byte[]> streamAudio(String path, String range) {
		long rangeStart = 0;
		long rangeEnd;
		byte[] data;
		Long fileSize;
		try {
			fileSize = getFileSize(path);
			System.out.println(fileSize);
			if (range == null) {
				return ResponseEntity.status(HttpStatus.OK)
						.header(StreamConstants.CONTENT_TYPE, StreamConstants.AUDIO_CONTENT)
						.header(StreamConstants.CONTENT_LENGTH, String.valueOf(fileSize))
						.body(readByteRange(path, rangeStart, fileSize - 1)); // Read the object and convert it
																				// as bytes
			}
			String[] ranges = range.split("-");
			rangeStart = Long.parseLong(ranges[0].substring(6));
			if (ranges.length > 1) {
				rangeEnd = Long.parseLong(ranges[1]);
			} else {
				rangeEnd = fileSize - 1;
			}
			if (fileSize < rangeEnd) {
				rangeEnd = fileSize - 1;
			}
			data = readByteRange(path, rangeStart, rangeEnd);
		} catch (IOException e) {
			logger.error("Exception while reading the file {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
		return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
				.header(StreamConstants.CONTENT_TYPE, StreamConstants.AUDIO_CONTENT)
				.header(StreamConstants.ACCEPT_RANGES, StreamConstants.BYTES)
				.header(StreamConstants.CONTENT_LENGTH, contentLength).header(StreamConstants.CONTENT_RANGE,
						StreamConstants.BYTES + " " + rangeStart + "-" + rangeEnd + "/" + fileSize)
				.body(data);

	}

	public ResponseEntity<byte[]> streamImage(String path, String range) {
		long rangeStart = 0;
		long rangeEnd;
		byte[] data;
		Long fileSize;
		try {
			fileSize = getFileSize(path);
			if (range == null) {
				return ResponseEntity.status(HttpStatus.OK)
						.header(StreamConstants.CONTENT_TYPE, StreamConstants.IMAGE_CONTENT)
						.header(StreamConstants.CONTENT_LENGTH, String.valueOf(fileSize))
						.body(readByteRange(path, rangeStart, fileSize - 1)); // Read the object and convert it
																				// as bytes
			}
			String[] ranges = range.split("-");
			rangeStart = Long.parseLong(ranges[0].substring(6));
			if (ranges.length > 1) {
				rangeEnd = Long.parseLong(ranges[1]);
			} else {
				rangeEnd = fileSize - 1;
			}
			if (fileSize < rangeEnd) {
				rangeEnd = fileSize - 1;
			}
			data = readByteRange(path, rangeStart, rangeEnd);
		} catch (IOException e) {
			logger.error("Exception while reading the file {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		String contentLength = String.valueOf((rangeEnd - rangeStart) + 1);
		return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
				.header(StreamConstants.CONTENT_TYPE, StreamConstants.IMAGE_CONTENT)
				.header(StreamConstants.ACCEPT_RANGES, StreamConstants.BYTES)
				.header(StreamConstants.CONTENT_LENGTH, contentLength).header(StreamConstants.CONTENT_RANGE,
						StreamConstants.BYTES + " " + rangeStart + "-" + rangeEnd + "/" + fileSize)
				.body(data);

	}

	/**
	 * ready file byte by byte.
	 *
	 * @param filename String.
	 * @param start    long.
	 * @param end      long.
	 * @return byte array.
	 * @throws IOException exception.
	 */
	public byte[] readByteRange(String requestPath, long start, long end) throws IOException {
		Path path = Paths.get(requestPath);
		try (InputStream inputStream = (Files.newInputStream(path));
				ByteArrayOutputStream bufferedOutputStream = new ByteArrayOutputStream()) {
			byte[] data = new byte[StreamConstants.BYTE_RANGE];
			int nRead;
			while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
				bufferedOutputStream.write(data, 0, nRead);
			}
			bufferedOutputStream.flush();
			byte[] result = new byte[(int) (end - start) + 1];
			System.arraycopy(bufferedOutputStream.toByteArray(), (int) start, result, 0, result.length);
			for(int i = 0; i < 10; i++) {
				System.out.println(result[i]);
			}
			return result;
		}
	}

	/**
	 * Content length.
	 *
	 * @param fileName String.
	 * @return Long.
	 */
	public Long getFileSize(String requestPath) {
		return sizeFromFile(Paths.get(requestPath));
	}

	/**
	 * Getting the size from the path.
	 *
	 * @param path Path.
	 * @return Long.
	 */
	private Long sizeFromFile(Path path) {
		try {
			return Files.size(path);
		} catch (IOException ioException) {
			logger.error("Error while getting the file size", ioException);
		}
		return 0L;
	}
}