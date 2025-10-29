package com.app.back.dto;

public class ContentItemDTO {
    private Integer contentId;
    private String type;
    private String title;
    private String uri;
    private Integer durationSec;
    private Integer moduleId;

    // Constructors
    public ContentItemDTO() {}

    public ContentItemDTO(Integer contentId, String type, String title, String uri, Integer durationSec, Integer moduleId) {
        this.contentId = contentId;
        this.type = type;
        this.title = title;
        this.uri = uri;
        this.durationSec = durationSec;
        this.moduleId = moduleId;
    }

    // Getters and Setters
    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }
}