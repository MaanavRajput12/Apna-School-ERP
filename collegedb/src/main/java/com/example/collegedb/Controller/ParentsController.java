package com.example.collegedb.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.collegedb.Repository.ParentsRepository;
import com.example.collegedb.Response.ParentsResponse;
import com.example.collegedb.entity.Parents;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/parents")
public class ParentsController {
    private static final Logger logger = LoggerFactory.getLogger(ParentsController.class);

    @Autowired
    private ParentsRepository parentsRepository;

    @GetMapping("/{id}")
    public ParentsResponse getParentsById(@PathVariable Long id) {
        logger.info("Fetching parent record with ID: {}", id);
        Parents parent = parentsRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Parent not found with ID: " + id));
        
        return new ParentsResponse(
            parent.getParentId(),
            parent.getParentName(),
            parent.getEmail(),
            parent.getContactNumber(),
            parent.getRelationship(),
            parent.getStudent() != null ? parent.getStudent().getStudentId() : null,
            parent.getUser() != null ? parent.getUser().getUserId() : null
        );
    }

    @GetMapping
    public List<ParentsResponse> getAllParents() {
        logger.info("Fetching all parent records...");
        return parentsRepository.findAll().stream()
            .map(parent -> new ParentsResponse(
                parent.getParentId(),
                parent.getParentName(),
                parent.getEmail(),
                parent.getContactNumber(),
                parent.getRelationship(),
                parent.getStudent() != null ? parent.getStudent().getStudentId() : null,
                parent.getUser() != null ? parent.getUser().getUserId() : null
            ))
            .collect(Collectors.toList());
    }

    @PostMapping
    public ParentsResponse createParent(@Valid @RequestBody Parents parent) {
        logger.info("Creating new parent record for: {}", parent.getParentName());
        Parents saved = parentsRepository.save(parent);
        logger.debug("Parent record created with ID: {}", saved.getParentId());
        return new ParentsResponse(
            saved.getParentId(),
            saved.getParentName(),
            saved.getEmail(),
            saved.getContactNumber(),
            saved.getRelationship(),
            saved.getStudent() != null ? saved.getStudent().getStudentId() : null,
            saved.getUser() != null ? saved.getUser().getUserId() : null
        );
    }
    
    @PutMapping("/{id}")
    public ParentsResponse updateParent(@PathVariable Long id, @RequestBody Parents updatedParent) {
        logger.info("Updating parent record with ID: {}", id);

        Parents existingParent = parentsRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Parent not found with ID: {}", id);
                return new RuntimeException("Parent not found with ID: " + id);
            });

        existingParent.setParentName(updatedParent.getParentName());
        existingParent.setEmail(updatedParent.getEmail());
        existingParent.setContactNumber(updatedParent.getContactNumber());
        existingParent.setRelationship(updatedParent.getRelationship());
        existingParent.setStudent(updatedParent.getStudent());
        existingParent.setUser(updatedParent.getUser());

        Parents saved = parentsRepository.save(existingParent);
        logger.debug("Parent record updated with ID: {}", saved.getParentId());
        return new ParentsResponse(
            saved.getParentId(),
            saved.getParentName(),
            saved.getEmail(),
            saved.getContactNumber(),
            saved.getRelationship(),
            saved.getStudent() != null ? saved.getStudent().getStudentId() : null,
            saved.getUser() != null ? saved.getUser().getUserId() : null
        );
    }

    @DeleteMapping("/{id}")
    public void deleteParent(@PathVariable Long id) {
        logger.warn("Attempting to delete parent record with ID: {}", id);
        Parents parent = parentsRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Parent record not found with ID: " + id));
        parentsRepository.delete(parent);
        logger.info("Parent record deleted successfully with ID: {}", id);
    }
    
    @PatchMapping("/{id}")
    public ParentsResponse patchParent(@PathVariable Long id, @RequestBody Parents updatedFields) {
        logger.info("Patching parent record with ID: {}", id);
        Parents existingParent = parentsRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Parent not found with ID: " + id));

        if (updatedFields.getParentName() != null) {
            existingParent.setParentName(updatedFields.getParentName());
        }
        if (updatedFields.getEmail() != null) {
            existingParent.setEmail(updatedFields.getEmail());
        }
        if (updatedFields.getContactNumber() != null) {
            existingParent.setContactNumber(updatedFields.getContactNumber());
        }
        if (updatedFields.getRelationship() != null) {
            existingParent.setRelationship(updatedFields.getRelationship());
        }
        if (updatedFields.getStudent() != null) {
            existingParent.setStudent(updatedFields.getStudent());
        }
        if (updatedFields.getUser() != null) {
            existingParent.setUser(updatedFields.getUser());
        }

        Parents saved = parentsRepository.save(existingParent);
        logger.debug("Parent record patched with ID: {}", saved.getParentId());
        return new ParentsResponse(
            saved.getParentId(),
            saved.getParentName(),
            saved.getEmail(),
            saved.getContactNumber(),
            saved.getRelationship(),
            saved.getStudent() != null ? saved.getStudent().getStudentId() : null,
            saved.getUser() != null ? saved.getUser().getUserId() : null
        );
    }
}
