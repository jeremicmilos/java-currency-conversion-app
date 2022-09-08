package cryptoWallet.errorHandling;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public @ResponseBody String handleNotFoundException(NotFoundException ex) {
		return ex.getLocalizedMessage();
	}

	@ExceptionHandler(BadRequestException.class)
	public @ResponseBody String handleBadRequestException(BadRequestException ex) {
		return ex.getLocalizedMessage();
	}
}
