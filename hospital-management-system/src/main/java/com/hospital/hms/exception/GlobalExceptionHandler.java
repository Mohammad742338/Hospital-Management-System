package com.hospital.hms.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hospital.hms.dto.ResponseStructure;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// 1. Handle your custom IdNotFoundException
	@ExceptionHandler(IdNotFoundException.class)
	public ResponseEntity<ResponseStructure<String>> handleIdNotFound(IdNotFoundException ex) {
		ResponseStructure<String> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.NOT_FOUND.value());
		structure.setMessage(ex.getMessage());
		structure.setData("Check the ID and try again.");

		return new ResponseEntity<>(structure, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ResponseStructure<String>> handleNotFound(NotFoundException ex) {
		ResponseStructure<String> response = new ResponseStructure<>();
		response.setStatusCode(HttpStatus.NOT_FOUND.value());
		response.setMessage(ex.getMessage());
		response.setData(null);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ResponseStructure<String>> handleDataIntegrity(DataIntegrityViolationException ex) {
		ResponseStructure<String> response = new ResponseStructure<>();
		response.setStatusCode(HttpStatus.BAD_REQUEST.value());
		response.setMessage(ex.getMessage());
		response.setData(null);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AppointmentConflictException.class)
	public ResponseEntity<ResponseStructure<String>> handleConflict(AppointmentConflictException ex) {
		ResponseStructure<String> response = new ResponseStructure<>();
		response.setStatusCode(HttpStatus.CONFLICT.value());
		response.setMessage(ex.getMessage());
		response.setData(null);
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}
}