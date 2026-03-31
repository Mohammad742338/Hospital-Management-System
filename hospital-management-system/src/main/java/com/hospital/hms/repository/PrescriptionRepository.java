package com.hospital.hms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.hms.entity.Prescription;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Integer> {
	// Get all medicines prescribed in a specific medical record
	List<Prescription> findByMedicalRecordId(Integer medicalRecordId);

	List<Prescription> findByMedicalRecordPatientId(Integer patientId);
}
