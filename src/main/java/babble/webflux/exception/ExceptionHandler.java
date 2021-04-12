package babble.webflux.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestControllerAdvice;;

@RestControllerAdvice
public class ExceptionHandler {

	@org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
	public String handleException(Exception exception, Model model) {
		model.addAttribute("exception", exception);
		return "errors";
	}

}