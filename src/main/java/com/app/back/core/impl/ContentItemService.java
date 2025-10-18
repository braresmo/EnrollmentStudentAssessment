package com.app.back.core.impl;

import com.app.back.core.IContentItem;
import com.app.back.model.ContentItem;
import com.app.back.model.Module;
import com.app.back.repository.ContentItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import java.util.List;
import java.util.Optional;

@Service
public class ContentItemService implements IContentItem {

    @Autowired
    private ContentItemRepository contentItemRepository;

    @Override
    public ContentItem save(ContentItem contentItem) throws InternalServerError, Exception {
        // For content items, we typically allow multiple items with the same title/type in different modules
        
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
}
