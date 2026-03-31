package com.hospital.hms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.hms.entity.Day;
import com.hospital.hms.entity.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

	// Find doctors from particular Department
	List<Doctor> findByDepartmentId(Integer departmentId);

	// 1. Find by Specialization
	List<Doctor> findBySpecializationIgnoreCase(String specialization);

	// 2. Find by Availability Day
	List<Doctor> findByAvailabilityDays(Day day);

	// 3. Find Doctor by Appointment ID ✅ Fixed: singular → plural
	Optional<Doctor> findByAppointments_Id(Integer appointmentId);

	// 4. Find Doctors by Patient ID
	List<Doctor> findByMedicalRecords_Patient_Id(Integer patientId);
}