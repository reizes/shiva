package com.reizes.shiva.etl.core;

public class InvalidClassException extends EtlException {

	private static final long serialVersionUID = -4570535049715073684L;

	public InvalidClassException() {
	}

	public InvalidClassException(String message) {
		super(message);
	}

	public InvalidClassException(Throwable cause) {
		super(cause);
	}

	public InvalidClassException(String message, Throwable cause) {
		super(message, cause);
	}

}
