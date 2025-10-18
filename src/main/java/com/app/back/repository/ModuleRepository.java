package com.app.back.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.back.model.Module;

public interface ModuleRepository extends JpaRepository<Module, Integer> {
	
	public Optional<Module> findByCode(String code);

}
