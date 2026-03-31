package com.hospital.hms.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.hms.entity.MedicalRecord;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Integer> {
	// Get full history for a patient
	List<MedicalRecord> findByPatientIdOrderByVisitDateDesc(Integer patientId);

	List<MedicalRecord> findByPatient_Id(Integer patientId);

	List<MedicalRecord> findByVisitDate(LocalDate date);

	List<MedicalRecord> findByDoctor_Id(Integer doctorId);
}
