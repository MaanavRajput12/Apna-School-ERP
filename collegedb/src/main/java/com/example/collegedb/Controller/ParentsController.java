package com.example.collegedb.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.collegedb.Response.ParentsResponse;
import com.example.collegedb.Service.ParentsService;
import com.example.collegedb.entity.Parents;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/parents")
public class ParentsController {

    private static final Logger logger = LoggerFactory.getLogger(ParentsController.class);
    private final ParentsService parentsService;

    public ParentsController(ParentsService parentsService) {
        this.parentsService = parentsService;
    }

    @GetMapping("/{id}")
    public ParentsResponse getParentsById(@PathVariable Long id) {
        logger.info("Fetching parent record with ID: {}", id);
        return parentsService.getParentsById(id);
    }

    @GetMapping
    public List<ParentsResponse> getAllParents() {
        logger.info("Fetching all parent records...");
        return parentsService.getAllParents();
    }

    @PostMapping
    public ParentsResponse createParent(@Valid @RequestBody Parents parent) {
        logger.info("Creating new parent record for: {}", parent.getParentName());
        return parentsService.createParent(parent);
    }

    @PutMapping("/{id}")
    public ParentsResponse updateParent(@PathVariable Long id, @RequestBody Parents updatedParent) {
        logger.info("Updating parent record with ID: {}", id);
        return parentsService.updateParent(id, updatedParent);
    }

    @DeleteMapping("/{id}")
    public void deleteParent(@PathVariable Long id) {
        logger.warn("Attempting to delete parent record with ID: {}", id);
        parentsService.deleteParent(id);
    }

    @PatchMapping("/{id}")
    public ParentsResponse patchParent(@PathVariable Long id, @RequestBody Parents updatedFields) {
        logger.info("Patching parent record with ID: {}", id);
        return parentsService.patchParent(id, updatedFields);
    }
}
