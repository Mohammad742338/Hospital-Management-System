package com.hospital.hms.exception;

public class NotFoundException extends RuntimeException {
	public NotFoundException(String message) {
		super(message);
	}
}