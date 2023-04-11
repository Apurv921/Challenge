package com.dws.challenge.exception;


/* If account does not have sufficient this exception class will be called */
public class InvalidAccountIdException  extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidAccountIdException(String message) {
		super(message);
	}

}
