package desafio.backend.uol.exception;

import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import desafio.backend.uol.dto.ExceptionDTO;

@RestControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<ExceptionDTO> threatNoSuchElementException(
			NoSuchElementException exception) {
		ExceptionDTO dto = new ExceptionDTO("Essa lista não possui mais usuários disponíveis!", 
				"400");
		return ResponseEntity.badRequest().body(dto);
	}
}