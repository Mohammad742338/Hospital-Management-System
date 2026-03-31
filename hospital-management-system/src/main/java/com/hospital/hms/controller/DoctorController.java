package com.hospital.hms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.hms.dto.ResponseStructure;
import com.hospital.hms.entity.Day;
import com.hospital.hms.entity.Doctor;
import com.hospital.hms.service.DoctorService;

@RestController
@RequestMapping("/hospital/doctor")
public class DoctorController {

	@Autowired
	private DoctorService service;

	// Save single doctor linked to a department
	@PostMapping("/save/{deptId}")
	public ResponseEntity<ResponseStructure<Doctor>> saveDoctor(@RequestBody Doctor doctor,
			@PathVariable Integer deptId) {
		return service.saveDoctor(doctor, deptId);
	}

	// ✅ Added — Save all doctors linked to a department
	@PostMapping("/saveAll/{deptId}")
	public ResponseEntity<ResponseStructure<List<Doctor>>> saveAll(@RequestBody List<Doctor> doctors,
			@PathVariable Integer deptId) {
		return service.saveAllDoctors(doctors, deptId);
	}

	// ✅ Added — Get all doctors
	@GetMapping("/all")
	public ResponseEntity<ResponseStructure<List<Doctor>>> findAll() {
		return service.getAllDoctors();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Doctor>> findById(@PathVariable Integer id) {
		return service.getDoctorById(id);
	}

	// ✅ Added — Delete doctor by ID
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<String>> delete(@PathVariable Integer id) {
		return service.deleteDoctor(id);
	}

	@GetMapping("/dept/{deptId}")
	public ResponseEntity<ResponseStructure<List<Doctor>>> findByDept(@PathVariable Integer deptId) {
		return service.getDoctorsByDept(deptId);
	}

	// ✅ Added — Get doctors by specialization
	@GetMapping("/spec/{spec}")
	public ResponseEntity<ResponseStructure<List<Doctor>>> findBySpec(@PathVariable String spec) {
		return service.getDoctorBySpec(spec);
	}

	// ✅ Added — Get doctor by appointment ID
	@GetMapping("/appointment/{appId}")
	public ResponseEntity<ResponseStructure<Doctor>> findByAppointment(@PathVariable Integer appId) {
		return service.getDoctorByAppointment(appId);
	}

	// ✅ Added — Get doctors by patient ID
	@GetMapping("/patient/{patientId}")
	public ResponseEntity<ResponseStructure<List<Doctor>>> findByPatient(@PathVariable Integer patientId) {
		return service.getDoctorsByPatient(patientId);
	}

	// ✅ Added — Get doctors by availability day
	@GetMapping("/day/{day}")
	public ResponseEntity<ResponseStructure<List<Doctor>>> findByDay(@PathVariable Day day) {
		return service.getDoctorsByDay(day);
	}
}