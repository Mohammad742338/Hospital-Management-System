package com.hospital.hms.entity;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Doctor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String name;
	private String specialization;

	@ElementCollection(targetClass = Day.class)
	@CollectionTable(name = "doctor_availability", joinColumns = @JoinColumn(name = "doctor_id"))
	@Enumerated(EnumType.STRING)
	private Set<Day> availabilityDays;

	@OneToMany(mappedBy = "doctor")
	@JsonIgnore
	private List<MedicalRecord> medicalRecords;

	@OneToMany(mappedBy = "doctor")
	@JsonIgnore
	private List<Appointment> appointments;

	@ManyToOne
	@JoinColumn(name = "department_id")
	private Department department;

	public List<MedicalRecord> getMedicalRecords() {
		return medicalRecords;
	}

	public void setMedicalRecords(List<MedicalRecord> medicalRecords) {
		this.medicalRecords = medicalRecords;
	}

	public List<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public Set<Day> getAvailabilityDays() {
		return availabilityDays;
	}

	public void setAvailabilityDays(Set<Day> availabilityDays) {
		this.availabilityDays = availabilityDays;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}
}