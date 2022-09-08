package currencyExchangeMicroservice.errorHandling;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

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
