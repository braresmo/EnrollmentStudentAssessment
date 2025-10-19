package com.app.back.core.impl;

import com.app.back.core.IContentItem;
import com.app.back.model.ContentItem;
import com.app.back.model.Module;
import com.app.back.repository.ContentItemRepository;
import com.app.back.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import java.util.List;
import java.util.Optional;

@Service
public class ContentItemService implements IContentItem {

    @Autowired
    private ContentItemRepository contentItemRepository;
    
    @Autowired
    private ModuleRepository moduleRepository;

    @Override
    public ContentItem save(ContentItem contentItem) throws InternalServerError, Exception {
        // Ensure the module is properly loaded from database
        if (contentItem.getModule() != null && contentItem.getModule().getModuleId() > 0) {
            Optional<Module> module = moduleRepository.findById(contentItem.getModule().getModuleId());
            if (module.isPresent()) {
                contentItem.setModule(module.get());
            } else {
                throw new Exception("Module not found with ID: " + contentItem.getModule().getModuleId());
            }
        } else {
            throw new Exception("Module is required for content item");
        }
        
        // Case 1: Create a new content item
        if (contentItem.getContentId() == 0) { // contentId is int, 0 means new
            return contentItemRepository.save(contentItem);
        }

        // Case 2: Update existing content item
        if (contentItem.getContentId() > 0) {
            Optional<ContentItem> existingContentItem = findById(contentItem.getContentId());
            if (existingContentItem.isPresent()) {
                return contentItemRepository.save(contentItem);
            }
        }
        
        throw new Exception("Error saving content item: Content item ID not found for update.");
    }

    @Override
    public void delete(Integer id) throws InternalServerError {
        contentItemRepository.deleteById(id);
    }

    @Override
    public List<ContentItem> findAll() {
        return contentItemRepository.findAll();
    }

    @Override
    public Optional<ContentItem> findById(Integer id) {
        return contentItemRepository.findById(id);
    }

    @Override
    public List<ContentItem> findByModule(Module module) {
        return contentItemRepository.findByModule(module);
    }

    @Override
    public List<ContentItem> findByType(String type) {
        return contentItemRepository.findByType(type);
    }

    @Override
    public List<ContentItem> findByTitle(String title) {
        return contentItemRepository.findByTitle(title);
    }
    
    public List<ContentItem> findByModuleId(Integer moduleId) {
        return contentItemRepository.findByModuleId(moduleId);
    }
    
    public ContentItem saveWithModuleId(ContentItem contentItem, Integer moduleId) throws Exception {
        // Load the module from database
        Optional<Module> module = moduleRepository.findById(moduleId);
        if (module.isEmpty()) {
            throw new Exception("Module not found with ID: " + moduleId);
        }
        
        // Set the module to the content item
        contentItem.setModule(module.get());
        
        // Use the regular save logic
        return save(contentItem);
    }
}
