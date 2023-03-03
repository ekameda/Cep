package br.com.wipro.exception;

public class InvalidZipCodeException extends RuntimeException {
	
	public InvalidZipCodeException(String message) {
        super(message);
    }
}
