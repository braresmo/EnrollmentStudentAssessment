package com.app.back.controller;

import com.app.back.core.impl.ContentItemService;
import com.app.back.core.impl.ModuleService;
import com.app.back.dto.ContentItemDTO;
import com.app.back.model.ContentItem;
import com.app.back.model.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/content")
public class ContentItemController {

    @Autowired
    private ContentItemService contentItemService;

    @Autowired
    private ModuleService moduleService;

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<ContentItem>> listar() {
        try {
            return ResponseEntity.ok(contentItemService.findAll());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ContentItem> save(@RequestBody ContentItemDTO contentItemDTO) {
        try {
            // Create ContentItem from DTO
            ContentItem contentItem = new ContentItem();
            contentItem.setType(contentItemDTO.getType());
            contentItem.setTitle(contentItemDTO.getTitle());
            contentItem.setUri(contentItemDTO.getUri());
            contentItem.setDurationSec(contentItemDTO.getDurationSec());

            // Find and set the module
            Optional<Module> module = moduleService.findById(contentItemDTO.getModuleId());
            if (module.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            contentItem.setModule(module.get());

            return ResponseEntity.ok(contentItemService.save(contentItem));
        } catch (InternalServerError e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<List<ContentItem>> delete(@PathVariable Integer id) {
        try {
            Optional<ContentItem> contentItem = contentItemService.findById(id);
            if (contentItem.isPresent()) {
                contentItemService.delete(id);
                return ResponseEntity.ok(contentItemService.findAll());
            } else {
                return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ContentItem> update(@RequestBody ContentItemDTO contentItemDTO) {
        try {
            // Find existing content item
            Optional<ContentItem> existingContentItem = contentItemService.findById(contentItemDTO.getContentId());
            if (existingContentItem.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Update content item fields
            ContentItem contentItem = existingContentItem.get();
            contentItem.setType(contentItemDTO.getType());
            contentItem.setTitle(contentItemDTO.getTitle());
            contentItem.setUri(contentItemDTO.getUri());
            contentItem.setDurationSec(contentItemDTO.getDurationSec());

            // Update module if provided and different
            if (contentItemDTO.getModuleId() != null) {
                Optional<Module> module = moduleService.findById(contentItemDTO.getModuleId());
                if (module.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                contentItem.setModule(module.get());
            }

            return ResponseEntity.ok(contentItemService.save(contentItem));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/findByType/{type}")
    public ResponseEntity<List<ContentItem>> findByType(@PathVariable String type) {
        try {
            return ResponseEntity.ok(contentItemService.findByType(type));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/findByTitle/{title}")
    public ResponseEntity<List<ContentItem>> findByTitle(@PathVariable String title) {
        try {
            return ResponseEntity.ok(contentItemService.findByTitle(title));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/findByModuleId/{moduleId}")
    public ResponseEntity<List<ContentItem>> findByModuleId(@PathVariable Integer moduleId) {
        try {
            List<ContentItem> contentItems = contentItemService.findByModuleId(moduleId);
            return ResponseEntity.ok(contentItems);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/getByModule/{moduleId}")
    public ResponseEntity<List<ContentItem>> getByModule(@PathVariable Integer moduleId) {
        try {
            List<ContentItem> contentItems = contentItemService.findByModuleId(moduleId);
            return ResponseEntity.ok(contentItems);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/getById/{id}")
    public ResponseEntity<ContentItem> getById(@PathVariable Integer id) {
        try {
            Optional<ContentItem> contentItem = contentItemService.findById(id);
            if (contentItem.isPresent()) {
                return ResponseEntity.ok(contentItem.get());
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping(path = "/saveWithModuleId/{moduleId}")
    public ResponseEntity<ContentItem> saveWithModuleId(@PathVariable Integer moduleId, @RequestBody ContentItem contentItem) {
        try {
            ContentItem savedItem = contentItemService.saveWithModuleId(contentItem, moduleId);
            return ResponseEntity.ok(savedItem);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
