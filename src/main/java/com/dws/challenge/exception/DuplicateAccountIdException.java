package com.dws.challenge.exception;


/* Exception class for throwing error message as duplicate account */
public class DuplicateAccountIdException extends RuntimeException {
	
	public DuplicateAccountIdException(String message) {
		super(message);
	}
}
