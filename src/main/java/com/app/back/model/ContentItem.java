package com.app.back.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "content_item")
public class ContentItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private int contentId;

    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @NotNull
    @Column(name = "uri", nullable = false)
    private String uri;

   
    @Column(name = "duration_sec")
    private Integer durationSec;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;
    

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "content_completions_by_student", joinColumns = @JoinColumn(name = "content_id"))
    @Column(name = "student_id")
    private Set<Integer> completedByStudentIds = new HashSet<>();


 /*
    public void markCompleted(Student student) {
        if (student != null && student.getUserId() != null) {
            this.completedByStudentIds.add(student.getUserId());
        }
    }
*/

    // --- Getters and Setters ---

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Integer getDurationSec() {
        return durationSec;
    }

    public void setDurationSec(Integer durationSec) {
        this.durationSec = durationSec;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }
    
    public Set<Integer> getCompletedByStudentIds() {
        return completedByStudentIds;
    }

    public void setCompletedByStudentIds(Set<Integer> completedByStudentIds) {
        this.completedByStudentIds = completedByStudentIds;
    }
}

