package com.app.back.model;

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
 * Represents a Student user in the system.
 * This entity inherits from the User class using a JOINED table strategy.
 */
@Entity
@Table(name = "student")
@PrimaryKeyJoinColumn(name = "user_id") // Links to the User table
@JsonIgnoreProperties({"password", "hibernateLazyInitializer", "handler"})
public class Student extends User {

    @Column(name = "student_number", unique = true, nullable = false)
    @NotNull
    private String studentNumber;

    @Column(name = "cohort")
    private String cohort;

    // A student can have many enrollments, representing the courses they are in.
    @OneToMany(
        mappedBy = "student",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonIgnoreProperties({"student", "course"})
    private Set<Enrollment> enrollments = new HashSet<>();

    // Getters and Setters
    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getCohort() {
        return cohort;
    }

    public void setCohort(String cohort) {
        this.cohort = cohort;
    }

    public Set<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(Set<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

}

