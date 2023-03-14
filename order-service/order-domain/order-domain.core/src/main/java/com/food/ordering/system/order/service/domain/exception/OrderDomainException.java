package com.food.ordering.system.order.service.domain.exception;

import com.food.ordering.system.domain.exception.DomainException;

public class OrderDomainException extends DomainException {

	private static final long serialVersionUID = 4749712792803796765L;

	public OrderDomainException(String message, Throwable cause) {
		super(message, cause);
	}

	public OrderDomainException(String message) {
		super(message);
	}
}