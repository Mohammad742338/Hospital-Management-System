package com.hospital.hms.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hospital.hms.dto.ResponseStructure;
import com.hospital.hms.entity.Appointment;
import com.hospital.hms.entity.Doctor;
import com.hospital.hms.entity.Patient;
import com.hospital.hms.entity.Status;
import com.hospital.hms.exception.AppointmentConflictException;
import com.hospital.hms.exception.IdNotFoundException;
import com.hospital.hms.exception.NotFoundException;
import com.hospital.hms.repository.AppointmentRepository;
import com.hospital.hms.repository.DoctorRepository;
import com.hospital.hms.repository.PatientRepository;

@Service
public class AppointmentService {

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	// 1. Book Appointment
	public ResponseEntity<ResponseStructure<Appointment>> bookAppointment(Appointment app, Integer patientId,
			Integer doctorId) {
		Optional<Patient> pOpt = patientRepository.findById(patientId);
		Optional<Doctor> dOpt = doctorRepository.findById(doctorId);

		if (pOpt.isEmpty())
			throw new IdNotFoundException("Patient not found with " + patientId + " .");
		if (dOpt.isEmpty())
			throw new IdNotFoundException("Doctor not found with " + doctorId + " .");

		Optional<Appointment> existingPatientApp = appointmentRepository.findByPatient_IdAndDate(patientId,
				app.getDate());
		if (existingPatientApp.isPresent()) {
			throw new AppointmentConflictException("Patient already has an appointment on this date.");
		}

		Optional<Appointment> existingDoctorApp = appointmentRepository.findByDoctor_IdAndDateAndTime(doctorId,
				app.getDate(), app.getTime());
		if (existingDoctorApp.isPresent()) {
			throw new AppointmentConflictException("Doctor is already busy at this time slot.");
		}

		app.setPatient(pOpt.get());
		app.setDoctor(dOpt.get());
		app.setStatus(Status.BOOKED);

		Appointment saved = appointmentRepository.save(app);

		ResponseStructure<Appointment> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.CREATED.value());
		structure.setMessage("Appointment Booked Successfully");
		structure.setData(saved);
		return new ResponseEntity<>(structure, HttpStatus.CREATED);
	}

	// 2. Get All
	public ResponseEntity<ResponseStructure<List<Appointment>>> getAll() {
		List<Appointment> list = appointmentRepository.findAll();

		if (list.isEmpty())
			throw new NotFoundException("No Appointment found in the database.");
		ResponseStructure<List<Appointment>> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Retrieved " + list.size() + " appointments.");
		structure.setData(list);
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// 3. Get By ID
	public ResponseEntity<ResponseStructure<Appointment>> getById(Integer id) {
		Optional<Appointment> opt = appointmentRepository.findById(id);
		if (opt.isEmpty())
			throw new IdNotFoundException("Appointment ID " + id + "  not found.");

		ResponseStructure<Appointment> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Appointment found."); // ✅ Fix #9 applied here too
		structure.setData(opt.get());
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// 4. Cancel Appointment
	public ResponseEntity<ResponseStructure<Appointment>> cancelAppointment(Integer id) {
		Optional<Appointment> opt = appointmentRepository.findById(id);
		if (opt.isEmpty())
			throw new IdNotFoundException("Cannot cancel appointment. ID " + id + " not found.");

		Appointment app = opt.get();
		app.setStatus(Status.CANCELLED);

		ResponseStructure<Appointment> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value()); // ✅ Fixed
		structure.setMessage("Appointment Cancelled");
		structure.setData(appointmentRepository.save(app));
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// 5. Update Status
	public ResponseEntity<ResponseStructure<Appointment>> updateStatus(Integer id, Status status) {
		Optional<Appointment> opt = appointmentRepository.findById(id);
		if (opt.isEmpty())
			throw new IdNotFoundException("Cannot update " + id + " ID not found.");

		Appointment app = opt.get();
		app.setStatus(status);

		ResponseStructure<Appointment> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value()); // ✅ Fixed
		structure.setMessage("Status updated to " + status);
		structure.setData(appointmentRepository.save(app));
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// 6. Get By Date
	public ResponseEntity<ResponseStructure<List<Appointment>>> getByDate(LocalDate date) {
		List<Appointment> list = appointmentRepository.findByDate(date);

		if (list.isEmpty())
			throw new NotFoundException("No appointments found for date " + date + " .");// Not fixed

		ResponseStructure<List<Appointment>> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value()); // ✅ Fixed
		structure.setMessage("Appointments found for date: " + date); // ✅ Fixed
		structure.setData(list);
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// 7. Get By Patient
	public ResponseEntity<ResponseStructure<List<Appointment>>> getByPatient(Integer patientId) {
		List<Appointment> list = appointmentRepository.findByPatient_Id(patientId);

		if (list.isEmpty())
			throw new IdNotFoundException("No appointments found with the patient ID " + patientId + " .");

		ResponseStructure<List<Appointment>> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value()); // ✅ Fixed
		structure.setMessage("Appointments found for patient ID: " + patientId); // ✅ Fixed
		structure.setData(list);
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}
}