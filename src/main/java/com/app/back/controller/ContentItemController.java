package com.app.back.controller;

import com.app.back.core.impl.ContentItemService;
import com.app.back.model.ContentItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/contentitem")
public class ContentItemController {

    @Autowired
    private ContentItemService contentItemService;

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
    public ResponseEntity<ContentItem> save(@RequestBody ContentItem contentItem) {
        try {
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
    public ResponseEntity<ContentItem> update(@RequestBody ContentItem contentItem) {
        try {
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
