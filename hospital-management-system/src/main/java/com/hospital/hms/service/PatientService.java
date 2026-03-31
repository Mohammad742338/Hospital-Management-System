package com.hospital.hms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hospital.hms.dto.ResponseStructure;
import com.hospital.hms.entity.Patient;
import com.hospital.hms.exception.IdNotFoundException;
import com.hospital.hms.exception.NotFoundException;
import com.hospital.hms.repository.PatientRepository;

@Service
public class PatientService {

	@Autowired
	private PatientRepository repository;

	// 1. Register Patient
	public ResponseEntity<ResponseStructure<Patient>> registerPatient(Patient patient) {
		if (repository.existsByEmail(patient.getEmail())) {
			throw new DataIntegrityViolationException("Email already exists: " + patient.getEmail());
		}

		if (patient.getContactNumber() == null || patient.getContactNumber().length() != 10) {
			throw new DataIntegrityViolationException("Contact number must be exactly 10 digits.");
		}

		Patient saved = repository.save(patient);
		ResponseStructure<Patient> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.CREATED.value());
		structure.setMessage("Patient Registered Successfully");
		structure.setData(saved);
		return new ResponseEntity<>(structure, HttpStatus.CREATED);
	}

	// 2. Get All Patients
	public ResponseEntity<ResponseStructure<List<Patient>>> getAllPatients() {
		List<Patient> patients = repository.findAll();
		if (patients.isEmpty())
			throw new NotFoundException("No patients found.");
		ResponseStructure<List<Patient>> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Retrieved " + patients.size() + " patients.");
		structure.setData(patients);
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// 3. Get Patient By ID
	public ResponseEntity<ResponseStructure<Patient>> getPatientById(Integer id) {
		Optional<Patient> optional = repository.findById(id);
		if (optional.isEmpty()) {
			throw new IdNotFoundException("Patient ID " + id + " not found.");
		}
		ResponseStructure<Patient> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Patient Found");
		structure.setData(optional.get());
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// 4. Update Patient
	public ResponseEntity<ResponseStructure<Patient>> updatePatient(Integer id, Patient patient) {
		Optional<Patient> optional = repository.findById(id);
		if (optional.isEmpty()) {
			throw new IdNotFoundException("Cannot update. Patient ID " + id + " does not exist.");
		}

		Patient existing = optional.get();
		existing.setName(patient.getName());
		existing.setAge(patient.getAge());
		existing.setGender(patient.getGender());
		existing.setContactNumber(patient.getContactNumber());

		Patient updated = repository.save(existing);
		ResponseStructure<Patient> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Patient Updated Successfully");
		structure.setData(updated);
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// 5. Delete Patient
	public ResponseEntity<ResponseStructure<String>> deletePatient(Integer id) {
		Optional<Patient> optional = repository.findById(id);
		if (optional.isEmpty()) {
			throw new IdNotFoundException("Delete failed. Patient ID " + id + " not found.");
		}
		repository.delete(optional.get());
		ResponseStructure<String> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Patient Deleted Successfully");
		structure.setData("ID: " + id);
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// 6. Get Patient By Contact Number
	public ResponseEntity<ResponseStructure<Patient>> getPatientByContact(String contact) {
		Optional<Patient> optional = repository.findByContactNumber(contact);
		if (optional.isEmpty()) {
			throw new NotFoundException("No patient found with contact: " + contact);
		}
		ResponseStructure<Patient> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Patient found with contact number.");
		structure.setData(optional.get());
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// 7. Get Patients Greater Than Age
	public ResponseEntity<ResponseStructure<List<Patient>>> getPatientsByAge(Integer age) {
		List<Patient> patients = repository.findByAgeGreaterThan(age);

		if (patients.isEmpty())
			throw new NotFoundException("No patients found.");

		ResponseStructure<List<Patient>> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Found " + patients.size() + " patients older than " + age);
		structure.setData(patients);
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<List<Patient>>> getPatientsByDoctor(Integer doctorId) {
		List<Patient> patients = repository.findByMedicalRecord_Doctor_Id(doctorId);

		if (patients.isEmpty())
			throw new NotFoundException("No patients found with doctor ID " + doctorId + ".");

		ResponseStructure<List<Patient>> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Found " + patients.size() + " patients with doctor ID " + doctorId + " .");
		structure.setData(patients);
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}
}
