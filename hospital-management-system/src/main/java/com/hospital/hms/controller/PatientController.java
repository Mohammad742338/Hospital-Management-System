package com.hospital.hms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.hms.dto.ResponseStructure;
import com.hospital.hms.entity.Patient;
import com.hospital.hms.service.PatientService;

@RestController
@RequestMapping("/hospital/patient")
public class PatientController {

	@Autowired
	private PatientService service;

	@PostMapping("/register")
	public ResponseEntity<ResponseStructure<Patient>> register(@RequestBody Patient patient) {
		return service.registerPatient(patient);
	}

	@GetMapping("/all")
	public ResponseEntity<ResponseStructure<List<Patient>>> getAll() {
		return service.getAllPatients();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Patient>> getById(@PathVariable Integer id) {
		return service.getPatientById(id);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ResponseStructure<Patient>> update(@PathVariable Integer id, @RequestBody Patient patient) {
		return service.updatePatient(id, patient);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<String>> delete(@PathVariable Integer id) {
		return service.deletePatient(id);
	}

	@GetMapping("/contact/{contact}")
	public ResponseEntity<ResponseStructure<Patient>> getByContact(@PathVariable String contact) {
		return service.getPatientByContact(contact);
	}

	@GetMapping("/age-greater/{age}")
	public ResponseEntity<ResponseStructure<List<Patient>>> getByAge(@PathVariable Integer age) {
		return service.getPatientsByAge(age);
	}

	@GetMapping("/doctor/{doctorId}")
	public ResponseEntity<ResponseStructure<List<Patient>>> getByDoctor(@PathVariable Integer doctorId) {
		return service.getPatientsByDoctor(doctorId);
	}
}
