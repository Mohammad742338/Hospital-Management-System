package com.hospital.hms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.hms.dto.ResponseStructure;
import com.hospital.hms.entity.Department;
import com.hospital.hms.service.DepartmentService;

@RestController
@RequestMapping("/hospital/department")
public class DepartmentController {

	@Autowired
	private DepartmentService service;

	@PostMapping
	public ResponseEntity<ResponseStructure<Department>> save(@RequestBody Department dept) {
		return service.saveDepartment(dept);
	}

	@GetMapping
	public ResponseEntity<ResponseStructure<List<Department>>> findAll() {
		return service.getAllDepartments();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ResponseStructure<Department>> findById(@PathVariable Integer id) {
		return service.getDepartmentById(id);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ResponseStructure<Department>> update(@PathVariable Integer id,
			@RequestBody Department dept) {
		return service.updateDepartment(id, dept);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseStructure<String>> delete(@PathVariable Integer id) {
		return service.deleteDepartment(id);
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<ResponseStructure<Department>> getByName(@PathVariable String name) {
		return service.getDepartmentByName(name);
	}
}