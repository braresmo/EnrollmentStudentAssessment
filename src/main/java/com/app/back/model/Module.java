package com.app.back.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "module")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Module {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "module_id")
    private int moduleId;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    // A Module must belong to a Course (Many-to-One relationship)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_course", nullable = false)
    @JsonIgnoreProperties({"modules", "enrollments", "hibernateLazyInitializer", "handler"})
    private Course course;

    // A Module can contain many ContentItems (One-to-Many relationship)
    @OneToMany(
        mappedBy = "module",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    @JsonIgnore
    private List<ContentItem> contentItems = new ArrayList<>();



    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<ContentItem> getContentItems() {
        return contentItems;
    }

    public void setContentItems(List<ContentItem> contentItems) {
        this.contentItems = contentItems;
    }
}
