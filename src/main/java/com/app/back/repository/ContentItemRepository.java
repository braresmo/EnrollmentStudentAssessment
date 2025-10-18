package com.app.back.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.back.model.ContentItem;
import com.app.back.model.Module;

@Repository
public interface ContentItemRepository extends JpaRepository<ContentItem, Integer> {
	
	List<ContentItem> findByModule(Module module);
	
	List<ContentItem> findByType(String type);
	
	List<ContentItem> findByTitle(String title);

}
