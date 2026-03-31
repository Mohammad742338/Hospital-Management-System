package com.hospital.hms.exception;

public class AppointmentConflictException extends RuntimeException {
	public AppointmentConflictException(String message) {
		super(message);
	}
}
