package com.hospital.hms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.hms.dto.ResponseStructure;
import com.hospital.hms.entity.Prescription;
import com.hospital.hms.service.PrescriptionService;

@RestController
@RequestMapping("/hospital/prescription")
public class PrescriptionController {

	@Autowired
	private PrescriptionService service;

	@PostMapping("/save/{mrId}")
	public ResponseEntity<ResponseStructure<Prescription>> save(@RequestBody Prescription p,
			@PathVariable Integer mrId) {
		return service.createPrescription(p, mrId);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Prescription>> findById(@PathVariable Integer id) {
		return service.getPrescriptionById(id);
	}

	@GetMapping("/mr/{mrId}")
	public ResponseEntity<ResponseStructure<List<Prescription>>> findByMR(@PathVariable Integer mrId) {
		return service.getByMR(mrId);
	}

	@GetMapping("/patient/{patientId}")
	public ResponseEntity<ResponseStructure<List<Prescription>>> findByPatient(@PathVariable Integer patientId) {
		return service.getByPatient(patientId);
	}
}
