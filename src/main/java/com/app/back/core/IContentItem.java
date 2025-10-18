package com.app.back.core;

import com.app.back.model.ContentItem;
import com.app.back.model.Module;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import java.util.List;
import java.util.Optional;

public interface IContentItem {

    public ContentItem save(ContentItem contentItem) throws InternalServerError, Exception;

    public void delete(Integer id) throws InternalServerError;

    public List<ContentItem> findAll();

    public Optional<ContentItem> findById(Integer id);

    public List<ContentItem> findByModule(Module module);
    
    public List<ContentItem> findByType(String type);
    
    public List<ContentItem> findByTitle(String title);
}
