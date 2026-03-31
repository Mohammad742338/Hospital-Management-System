package com.hospital.hms.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.hms.dto.ResponseStructure;
import com.hospital.hms.entity.Appointment;
import com.hospital.hms.entity.Status;
import com.hospital.hms.service.AppointmentService;

@RestController
@RequestMapping("/hospital/appointment")
public class AppointmentController {

	@Autowired
	private AppointmentService service;

	@PostMapping("/book/{patientId}/{doctorId}")
	public ResponseEntity<ResponseStructure<Appointment>> book(@RequestBody Appointment app,
			@PathVariable Integer patientId, @PathVariable Integer doctorId) {
		return service.bookAppointment(app, patientId, doctorId);
	}

	@GetMapping("/all")
	public ResponseEntity<ResponseStructure<List<Appointment>>> findAll() {
		return service.getAll();
	}

	@GetMapping("/{id}") // ✅ Added
	public ResponseEntity<ResponseStructure<Appointment>> findById(@PathVariable Integer id) {
		return service.getById(id);
	}

	@PutMapping("/cancel/{id}")
	public ResponseEntity<ResponseStructure<Appointment>> cancel(@PathVariable Integer id) {
		return service.cancelAppointment(id);
	}

	@PatchMapping("/updatestatus/{id}")
	public ResponseEntity<ResponseStructure<Appointment>> updateStatus(@PathVariable Integer id,
			@RequestParam Status status) {
		return service.updateStatus(id, status);
	}

	@GetMapping("/date/{date}")
	public ResponseEntity<ResponseStructure<List<Appointment>>> findByDate(@PathVariable LocalDate date) {
		return service.getByDate(date);
	}

	@GetMapping("/patient/{patientId}")
	public ResponseEntity<ResponseStructure<List<Appointment>>> findByPatient(@PathVariable Integer patientId) {
		return service.getByPatient(patientId);
	}
}