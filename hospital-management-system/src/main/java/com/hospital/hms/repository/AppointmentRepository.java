package com.hospital.hms.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.hms.entity.Appointment;
import com.hospital.hms.entity.Status;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

	boolean existsByDoctor_IdAndDateAndTime(Integer doctorId, LocalDate date, LocalTime time);

	// Get by Date
	List<Appointment> findByDate(LocalDate date);

	// Get by Status (BOOKED, CANCELLED, COMPLETED)
	List<Appointment> findByStatus(Status status);

	// Get by Patient ID
	List<Appointment> findByPatient_Id(Integer patientId);

	// Logic Check: Find if a patient already has an appointment on a specific date
	Optional<Appointment> findByPatient_IdAndDate(Integer patientId, LocalDate date);

	// Logic Check: Find if a doctor is already busy at a specific date and time
	Optional<Appointment> findByDoctor_IdAndDateAndTime(Integer doctorId, LocalDate date, LocalTime time);
}
