package com.app.back.dto;

import java.util.Date;

public class CourseCreateDTO {
    private String code;
    private String title;
    private String status;
    private Date publishedAt;
    private Integer instructorId;
    private Integer tenantId;

    // Constructors
    public CourseCreateDTO() {}

    public CourseCreateDTO(String code, String title, String status, Date publishedAt, Integer instructorId, Integer tenantId) {
        this.code = code;
        this.title = title;
        this.status = status;
        this.publishedAt = publishedAt;
        this.instructorId = instructorId;
        this.tenantId = tenantId;
    }

    // Getters and Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Integer getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Integer instructorId) {
        this.instructorId = instructorId;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }
}