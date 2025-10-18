package com.app.back.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.back.model.ContentItem;

public interface ContentItemRepository extends JpaRepository<ContentItem, Integer> {
	
	public Optional<ContentItem> findByCode(String code);

}
