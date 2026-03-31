package com.hospital.hms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hospital.hms.dto.ResponseStructure;
import com.hospital.hms.entity.Day;
import com.hospital.hms.entity.Department;
import com.hospital.hms.entity.Doctor;
import com.hospital.hms.exception.IdNotFoundException;
import com.hospital.hms.exception.NotFoundException;
import com.hospital.hms.repository.DepartmentRepository;
import com.hospital.hms.repository.DoctorRepository;

@Service
public class DoctorService {

	@Autowired
	private DoctorRepository doctorRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	// 1. Save Doctor
	public ResponseEntity<ResponseStructure<Doctor>> saveDoctor(Doctor doctor, Integer deptId) {
		// Check if Department exists
		Optional<Department> deptOptional = departmentRepository.findById(deptId);

		if (deptOptional.isEmpty()) {
			throw new IdNotFoundException("Cannot add Doctor. Department ID " + deptId + " does not exist.");
		}

		// No two doctors with the same name in the same dept

		Department dept = deptOptional.get();
		doctor.setDepartment(dept); // Link the doctor to the found department

		Doctor savedDoctor = doctorRepository.save(doctor);

		ResponseStructure<Doctor> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.CREATED.value());
		structure.setMessage("Doctor saved successfully in " + dept.getName());
		structure.setData(savedDoctor);

		return new ResponseEntity<>(structure, HttpStatus.CREATED);
	}

	// 2. Save All Doctors
	public ResponseEntity<ResponseStructure<List<Doctor>>> saveAllDoctors(List<Doctor> doctors, Integer deptId) {
		Optional<Department> deptOptional = departmentRepository.findById(deptId);
		if (deptOptional.isEmpty()) {
			throw new IdNotFoundException("Cannot add Doctors. Department ID " + deptId + " does not exist.");
		}
		Department dept = deptOptional.get();
		for (Doctor doctor : doctors) {
			doctor.setDepartment(dept);
		}
		List<Doctor> savedList = doctorRepository.saveAll(doctors);
		ResponseStructure<List<Doctor>> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.CREATED.value());
		structure.setMessage("All doctors saved successfully in " + dept.getName());
		structure.setData(savedList);
		return new ResponseEntity<>(structure, HttpStatus.CREATED);
	}

	// 3. Get All Doctors
	public ResponseEntity<ResponseStructure<List<Doctor>>> getAllDoctors() {
		List<Doctor> list = doctorRepository.findAll();
		ResponseStructure<List<Doctor>> structure = new ResponseStructure<>();

		if (list.isEmpty())
			throw new NotFoundException("No doctors found in the database.");

		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Retrieved " + list.size() + " doctors.");
		structure.setData(list);
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// 4. Get Doctor By ID
	public ResponseEntity<ResponseStructure<Doctor>> getDoctorById(Integer id) {
		Optional<Doctor> optional = doctorRepository.findById(id);

		if (optional.isEmpty()) {
			throw new IdNotFoundException("Doctor ID " + id + " not found.");
		}

		ResponseStructure<Doctor> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Doctor found.");
		structure.setData(optional.get());

		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// 5. Delete Doctor
	public ResponseEntity<ResponseStructure<String>> deleteDoctor(Integer id) {
		Optional<Doctor> optional = doctorRepository.findById(id);

		if (optional.isEmpty()) {
			throw new IdNotFoundException("Delete Failed: Doctor ID " + id + " not found.");
		}

		doctorRepository.delete(optional.get());

		ResponseStructure<String> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Doctor deleted.");
		structure.setData("ID " + id + " removed.");

		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// 6. Get Doctors By Department
	public ResponseEntity<ResponseStructure<List<Doctor>>> getDoctorsByDept(Integer deptId) {
		Optional<Department> deptOptional = departmentRepository.findById(deptId);

		if (deptOptional.isEmpty())
			throw new IdNotFoundException("Cannot fetch doctors. Dept ID " + deptId + " not found.");

		List<Doctor> doctors = doctorRepository.findByDepartmentId(deptId);

		ResponseStructure<List<Doctor>> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Found " + doctors.size() + " doctors in " + deptOptional.get().getName());
		structure.setData(doctors);

		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// 7. Get Doctor by Specification (Specialization)
	public ResponseEntity<ResponseStructure<List<Doctor>>> getDoctorBySpec(String spec) {
		List<Doctor> list = doctorRepository.findBySpecializationIgnoreCase(spec);
		ResponseStructure<List<Doctor>> structure = new ResponseStructure<>();

		if (list.isEmpty())
			throw new NotFoundException("No doctors found with specialization: " + spec + " in the database.");

		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Doctors found with specialization: " + spec);
		structure.setData(list);
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// 8. Get Doctor by Appointment ID
	public ResponseEntity<ResponseStructure<Doctor>> getDoctorByAppointment(Integer appId) {
		Optional<Doctor> optional = doctorRepository.findByAppointments_Id(appId);

		if (optional.isEmpty()) {
			throw new IdNotFoundException("No doctor found associated with Appointment ID: " + appId);
		}

		ResponseStructure<Doctor> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Doctor retrieved via Appointment.");
		structure.setData(optional.get());
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// 9. Get Doctors by Patient ID
	public ResponseEntity<ResponseStructure<List<Doctor>>> getDoctorsByPatient(Integer patientId) {
		List<Doctor> doctors = doctorRepository.findByMedicalRecords_Patient_Id(patientId);

		ResponseStructure<List<Doctor>> structure = new ResponseStructure<>();

		if (doctors.isEmpty())
			throw new IdNotFoundException(
					"No doctors found who treated Patient ID: " + patientId + " in the database.");

		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Found doctors who treated Patient ID: " + patientId);
		structure.setData(doctors);
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// 10. Get Doctors by Availability
	public ResponseEntity<ResponseStructure<List<Doctor>>> getDoctorsByDay(Day day) {
		List<Doctor> doctors = doctorRepository.findByAvailabilityDays(day);

		ResponseStructure<List<Doctor>> structure = new ResponseStructure<>();

		if (doctors.isEmpty())
			throw new NotFoundException("No doctors available on " + day);

		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Doctors available on " + day);
		structure.setData(doctors);
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}
}
