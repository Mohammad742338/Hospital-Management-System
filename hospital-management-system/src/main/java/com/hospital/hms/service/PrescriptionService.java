package com.hospital.hms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hospital.hms.dto.ResponseStructure;
import com.hospital.hms.entity.MedicalRecord;
import com.hospital.hms.entity.Prescription;
import com.hospital.hms.exception.IdNotFoundException;
import com.hospital.hms.repository.MedicalRecordRepository;
import com.hospital.hms.repository.PrescriptionRepository;

@Service
public class PrescriptionService {

	@Autowired
	private PrescriptionRepository prescriptionRepository;

	@Autowired
	private MedicalRecordRepository mrRepository;

	// 1. Create Prescription
	public ResponseEntity<ResponseStructure<Prescription>> createPrescription(Prescription p, Integer mrId) {
		Optional<MedicalRecord> mrOpt = mrRepository.findById(mrId);

		if (mrOpt.isEmpty()) {
			throw new IdNotFoundException("Medical Record ID " + mrId + " not found. Cannot prescribe.");
		}

		p.setMedicalRecord(mrOpt.get());
		Prescription saved = prescriptionRepository.save(p);

		ResponseStructure<Prescription> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.CREATED.value());
		structure.setMessage("Prescription generated.");
		structure.setData(saved);
		return new ResponseEntity<>(structure, HttpStatus.CREATED);
	}

	// 2. Get Prescription By ID
	public ResponseEntity<ResponseStructure<Prescription>> getPrescriptionById(Integer id) {
		Optional<Prescription> opt = prescriptionRepository.findById(id);
		if (opt.isEmpty()) {
			throw new IdNotFoundException("Prescription ID " + id + " not found.");
		}
		ResponseStructure<Prescription> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value()); // ✅ Fixed
		structure.setMessage("Prescription found."); // ✅ Fixed
		structure.setData(opt.get());
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// 3. Get Prescriptions by MR ID
	public ResponseEntity<ResponseStructure<List<Prescription>>> getByMR(Integer mrId) {
		List<Prescription> list = prescriptionRepository.findByMedicalRecordId(mrId);
		if (list.isEmpty())
			throw new IdNotFoundException("No prescriptions found.");
		ResponseStructure<List<Prescription>> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value()); // ✅ Fixed
		structure.setMessage("Prescriptions found for MR ID: " + mrId); // ✅ Fixed
		structure.setData(list);
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// 4. Get Prescriptions by Patient ID
	public ResponseEntity<ResponseStructure<List<Prescription>>> getByPatient(Integer patientId) {
		List<Prescription> list = prescriptionRepository.findByMedicalRecordPatientId(patientId);
		if (list.isEmpty())
			throw new IdNotFoundException("No prescriptions found.");
		ResponseStructure<List<Prescription>> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value()); // ✅ Fixed
		structure.setMessage("Prescriptions found for patient ID: " + patientId); // ✅ Fixed
		structure.setData(list);
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}
}