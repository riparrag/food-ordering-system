package com.food.ordering.system.domain.exception;

public class DomainException extends RuntimeException {

	private static final long serialVersionUID = -5079574700937322372L;

	public DomainException(String message, Throwable cause) {
		super(message, cause);
	}

	public DomainException(String message) {
		super(message);
	}
	
}