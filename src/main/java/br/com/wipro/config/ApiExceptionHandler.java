package br.com.wipro.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.wipro.exception.ExternalAPIException;
import br.com.wipro.exception.InvalidZipCodeException;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler({Exception.class, ExternalAPIException.class})
    public ResponseEntity<ApiError> handleException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        		.body(ApiError.builder().error("Unespected error!").build());
    }
	
	@ExceptionHandler({InvalidZipCodeException.class})
    public ResponseEntity<ApiError> handleException(InvalidZipCodeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        		.body(ApiError.builder().error(exception.getMessage()).build());
    }

}
