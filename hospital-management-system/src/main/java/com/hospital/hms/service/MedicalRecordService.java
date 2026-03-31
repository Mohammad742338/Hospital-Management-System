package com.hospital.hms.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hospital.hms.dto.ResponseStructure;
import com.hospital.hms.entity.Appointment;
import com.hospital.hms.entity.MedicalRecord;
import com.hospital.hms.exception.IdNotFoundException;
import com.hospital.hms.exception.NotFoundException;
import com.hospital.hms.repository.AppointmentRepository;
import com.hospital.hms.repository.MedicalRecordRepository;

@Service
public class MedicalRecordService {

	@Autowired
	private MedicalRecordRepository mrRepository;

	@Autowired
	private AppointmentRepository appointmentRepository;

	// 1. Create Medical Record
	public ResponseEntity<ResponseStructure<MedicalRecord>> createMedicalRecord(MedicalRecord mr,
			Integer appointmentId) {
		Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);

		if (appointmentOpt.isEmpty()) {
			throw new IdNotFoundException("Appointment ID " + appointmentId + " not found.");
		}

		Appointment appointment = appointmentOpt.get();

		if (!appointment.getStatus().name().equalsIgnoreCase("COMPLETED")) {
			throw new DataIntegrityViolationException(
					"Cannot create MR. Appointment status is " + appointment.getStatus() + ". It must be COMPLETED.");
		}

		mr.setPatient(appointment.getPatient());
		mr.setDoctor(appointment.getDoctor());
		mr.setVisitDate(LocalDate.now());

		MedicalRecord savedMR = mrRepository.save(mr);

		ResponseStructure<MedicalRecord> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.CREATED.value());
		structure.setMessage("Medical Record generated successfully.");
		structure.setData(savedMR);
		return new ResponseEntity<>(structure, HttpStatus.CREATED);
	}

	// 2. Get All MR
	public ResponseEntity<ResponseStructure<List<MedicalRecord>>> getAllMR() {
		List<MedicalRecord> list = mrRepository.findAll();
		if (list.isEmpty())
			throw new NotFoundException("No medical records found in the database.");

		ResponseStructure<List<MedicalRecord>> structure = new ResponseStructure<>();

		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Retrieved all records.");
		structure.setData(list);

		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// 3. Get MR by ID
	public ResponseEntity<ResponseStructure<MedicalRecord>> getMRById(Integer id) {
		Optional<MedicalRecord> opt = mrRepository.findById(id);
		if (opt.isEmpty()) {
			throw new IdNotFoundException("Medical Record ID " + id + " not found.");
		}
		ResponseStructure<MedicalRecord> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Medical Record found.");
		structure.setData(opt.get());
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// 4. Update MR
	public ResponseEntity<ResponseStructure<MedicalRecord>> updateMR(Integer id, MedicalRecord mr) {
		Optional<MedicalRecord> opt = mrRepository.findById(id);
		if (opt.isEmpty()) {
			throw new IdNotFoundException("Update failed. Record ID " + id + " not found.");
		}
		MedicalRecord existing = opt.get();
		existing.setDiagnoses(mr.getDiagnoses());
		existing.setTreatment(mr.getTreatment());

		ResponseStructure<MedicalRecord> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Medical Record updated.");
		structure.setData(mrRepository.save(existing));
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// 5. Get MR by Patient ID
	public ResponseEntity<ResponseStructure<List<MedicalRecord>>> getMRByPatient(Integer patientId) {
		List<MedicalRecord> list = mrRepository.findByPatient_Id(patientId);

		if (list.isEmpty())
			throw new IdNotFoundException("No medical records found with patient ID " + patientId + ".");

		ResponseStructure<List<MedicalRecord>> structure = new ResponseStructure<>();

		structure.setStatusCode(HttpStatus.OK.value()); // ✅ Fixed
		structure.setMessage("Records found for patient ID: " + patientId); // ✅ Fixed
		structure.setData(list);

		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// 6. Get MR by Visit Date
	public ResponseEntity<ResponseStructure<List<MedicalRecord>>> getMRByDate(LocalDate date) {
		List<MedicalRecord> list = mrRepository.findByVisitDate(date);
		if (list.isEmpty())
			throw new NotFoundException("No medical records found of date " + date + "	.");
		ResponseStructure<List<MedicalRecord>> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value()); // ✅ Fixed
		structure.setMessage("Records found for date: " + date); // ✅ Fixed
		structure.setData(list);
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// 7. Get by Pagination
	public ResponseEntity<ResponseStructure<Page<MedicalRecord>>> getMRByPaginationAndSorting(Integer pageNumber,
			Integer pageSize, String field) {
		Page<MedicalRecord> page = mrRepository
				.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(field).descending()));

		if (page.isEmpty())
			if (page.isEmpty())
				throw new NotFoundException("No medical records found.");

		ResponseStructure<Page<MedicalRecord>> structure = new ResponseStructure<>();

		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Retrieved medical records page " + pageNumber);
		structure.setData(page);
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}
}