package com.app.back.model;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;



@Entity
@Table(name = "course")
public class Course {
	
	@Id
	@Column(name = "id_course")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idCourse;
	
	@Column(name = "id_tenant")
	private Integer idTenat;
	
	@Column(name = "code",nullable = false)
	@NotNull
	private String code;
	
	@Column(name = "title",nullable = false)
	@NotNull
	private String title;
	
	@Column(name = "status", unique = true,nullable = false)
	@NotNull
	private String status;
	
	@Column(name = "published_At",nullable = false)
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date publishedAt;
	
	@OneToMany(
	        mappedBy = "course",
	        cascade = CascadeType.ALL,
	        orphanRemoval = true
	    )
	private List<Module> modules = new ArrayList<>();
	
	@OneToMany(
		    mappedBy = "course",
		    cascade = CascadeType.ALL,
		    orphanRemoval = true
		)
	private Set<Enrollment> enrollments = new HashSet<>();
	/*@PrePersist
	private void generarEstadoyFechaCreacion() {
		this.setEstado(1);
		this.setpublishedAt(new Date());
	}
	*/
	


	public Integer getIdCourse() {
		return idCourse;
	}




	public void setIdCourse(Integer idCourse) {
		this.idCourse = idCourse;
	}

	public Integer getIdTenat() {
		return idTenat;
	}


	public void setidTenat(Integer idTenat) {
		this.idTenat = idTenat;
	}



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
	
    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }
    
    public Set<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(Set<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

}
