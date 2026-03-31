package com.hospital.hms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.hms.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
	boolean existsByNameIgnoreCase(String name);

	Optional<Department> findByNameIgnoreCase(String name);
}
