package com.hospital.hms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.hms.entity.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

	boolean existsByEmail(String email);

	Optional<Patient> findByContactNumber(String contactNumber);

	List<Patient> findByAgeGreaterThan(Integer age);

	List<Patient> findByMedicalRecord_Doctor_Id(Integer doctorId);
}
