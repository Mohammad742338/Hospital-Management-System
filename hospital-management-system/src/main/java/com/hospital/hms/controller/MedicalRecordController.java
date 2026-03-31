package com.hospital.hms.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.hms.dto.ResponseStructure;
import com.hospital.hms.entity.MedicalRecord;
import com.hospital.hms.service.MedicalRecordService;

@RestController
@RequestMapping("/hospital/mr")
public class MedicalRecordController {

	@Autowired
	private MedicalRecordService service;

	@PostMapping("/create/{appointmentId}")
	public ResponseEntity<ResponseStructure<MedicalRecord>> create(@RequestBody MedicalRecord mr,
			@PathVariable Integer appointmentId) {
		return service.createMedicalRecord(mr, appointmentId);
	}

	@GetMapping("/all")
	public ResponseEntity<ResponseStructure<List<MedicalRecord>>> findAll() {
		return service.getAllMR();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<MedicalRecord>> findById(@PathVariable Integer id) {
		return service.getMRById(id);
	}

	@PutMapping("/{id}") // ✅ Added
	public ResponseEntity<ResponseStructure<MedicalRecord>> update(@PathVariable Integer id,
			@RequestBody MedicalRecord mr) {
		return service.updateMR(id, mr);
	}

	@GetMapping("/patient/{patientId}/status/{status}")
	public ResponseEntity<ResponseStructure<List<MedicalRecord>>> findByPatient(@PathVariable Integer patientId) {
		return service.getMRByPatient(patientId);
	}

	@GetMapping("/date/{date}")
	public ResponseEntity<ResponseStructure<List<MedicalRecord>>> findByDate(@PathVariable LocalDate date) {
		return service.getMRByDate(date);
	}

	@GetMapping("/pageNumber/{pageNumber}/pageSize/{pageSize}/sort/{field}")
	public ResponseEntity<ResponseStructure<Page<MedicalRecord>>> getByPagesAndSort(@PathVariable Integer pageNumber,
			@PathVariable Integer pageSize, @PathVariable String field) {
		return service.getMRByPaginationAndSorting(pageNumber, pageSize, field);
	}
}