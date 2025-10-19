package com.app.back.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@JsonIgnoreProperties({"password", "hibernateLazyInitializer", "handler"})
public class Instructor extends User {

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
    @JsonIgnore
    private Set<Course> courses = new HashSet<>();

    // Getters and Setters
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
