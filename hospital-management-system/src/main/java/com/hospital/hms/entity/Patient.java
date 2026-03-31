package com.hospital.hms.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Patient {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Integer age;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Gender gender;

	@Column(nullable = false, length = 15)
	private String contactNumber;

	@Column(unique = true, nullable = false)
	private String email;

	@OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
	@JsonIgnore // Prevents infinite recursion in JSON
	private List<Appointment> appointments;

	@OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<MedicalRecord> medicalRecord;

	public List<MedicalRecord> getMedicalRecord() {
		return medicalRecord;
	}

	public void setMedicalRecord(List<MedicalRecord> medicalRecord) {
		this.medicalRecord = medicalRecord;
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

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}

}
