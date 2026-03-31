package com.hospital.hms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hospital.hms.dto.ResponseStructure;
import com.hospital.hms.entity.Department;
import com.hospital.hms.exception.IdNotFoundException;
import com.hospital.hms.exception.NotFoundException;
import com.hospital.hms.repository.DepartmentRepository;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;

	// 1. Create Department
	public ResponseEntity<ResponseStructure<Department>> saveDepartment(Department dept) {
		if (departmentRepository.existsByNameIgnoreCase(dept.getName())) {
			throw new DataIntegrityViolationException("Department with name '" + dept.getName() + "' already exists!");
		}
		Department savedDept = departmentRepository.save(dept);

		ResponseStructure<Department> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.CREATED.value());
		structure.setMessage("Department created successfully.");
		structure.setData(savedDept);
		return new ResponseEntity<>(structure, HttpStatus.CREATED);
	}

	// 2. Get All Department
	public ResponseEntity<ResponseStructure<List<Department>>> getAllDepartments() {
		List<Department> list = departmentRepository.findAll();

		ResponseStructure<List<Department>> structure = new ResponseStructure<>();

		if (list.isEmpty())
			throw new NotFoundException("No department found in the database.");

		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Found " + list.size() + " departments.");
		structure.setData(list);
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// 3. Get Department By ID
	public ResponseEntity<ResponseStructure<Department>> getDepartmentById(Integer id) {
		Optional<Department> dept = departmentRepository.findById(id);

		if (dept.isEmpty())
			throw new IdNotFoundException("Department ID " + id + " not found.");

		ResponseStructure<Department> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Department found.");
		structure.setData(dept.get());
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// 4. Update Department
	public ResponseEntity<ResponseStructure<Department>> updateDepartment(Integer id, Department dept) {
		Optional<Department> optional = departmentRepository.findById(id);

		if (optional.isEmpty()) {
			throw new IdNotFoundException("Update Failed: Dept ID " + id + " does not exist.");
		}

		Department department = optional.get();
		department.setName(dept.getName());

		Department updated = departmentRepository.save(department);

		ResponseStructure<Department> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Department Updated");
		structure.setData(updated);

		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// 5. Delete Department (Logic: Cannot delete if Doctors exist)
	public ResponseEntity<ResponseStructure<String>> deleteDepartment(Integer id) {
		Optional<Department> optional = departmentRepository.findById(id);

		if (optional.isEmpty()) {
			throw new IdNotFoundException("Delete Failed: Dept ID " + id + " does not exist.");
		}

		Department dept = optional.get();

		if (dept.getDoctor() != null && !dept.getDoctor().isEmpty()) {
			throw new DataIntegrityViolationException(
					"Cannot delete: Department still has " + dept.getDoctor().size() + " doctors.");
		}

		departmentRepository.delete(dept);

		ResponseStructure<String> structure = new ResponseStructure<>();
		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Department Deleted");
		structure.setData("ID " + id + " removed successfully.");

		return new ResponseEntity<>(structure, HttpStatus.OK);
	}

	// 6. Get Department By Name (Manual Optional Check)
	public ResponseEntity<ResponseStructure<Department>> getDepartmentByName(String name) {
		Optional<Department> optional = departmentRepository.findByNameIgnoreCase(name);

		ResponseStructure<Department> structure = new ResponseStructure<>();

		if (optional.isEmpty())
			throw new IdNotFoundException("Department with name '" + name + "' not found.");

		structure.setStatusCode(HttpStatus.OK.value());
		structure.setMessage("Department found.");
		structure.setData(optional.get());
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}
}