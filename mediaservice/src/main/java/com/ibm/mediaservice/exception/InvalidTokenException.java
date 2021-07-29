package com.ibm.mediaservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidTokenException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidTokenException(String message) {
		super(message);
	}

	
}
