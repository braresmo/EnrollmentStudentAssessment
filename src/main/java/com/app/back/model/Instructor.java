package com.app.back.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an Instructor user in the system.
 * This entity inherits from the User class using a JOINED table strategy.
 */
@Entity
@Table(name = "instructor")
@PrimaryKeyJoinColumn(name = "user_id") // Links to the User table
public class Instructor extends User {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instructor_id")
    private Integer instructorId;

    @Column(name = "employee_number", unique = true, nullable = false)
    @NotNull
    private String employeeNumber;

    @Column(name = "office_hours")
    private String officeHours;

    // An instructor can create and manage many courses.
    @OneToMany(
        mappedBy = "instructor",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Set<Course> courses = new HashSet<>();

    // Getters and Setters
    public Integer getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
    }
    
    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getOfficeHours() {
        return officeHours;
    }

    public void setOfficeHours(String officeHours) {
        this.officeHours = officeHours;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }
}
