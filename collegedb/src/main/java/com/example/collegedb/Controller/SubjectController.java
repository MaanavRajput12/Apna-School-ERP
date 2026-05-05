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

import com.example.collegedb.Response.SubjectResponse;
import com.example.collegedb.Service.SubjectService;
import com.example.collegedb.entity.Subject;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    private static final Logger logger = LoggerFactory.getLogger(SubjectController.class);
    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public List<SubjectResponse> getAll() {
        logger.info("Fetching all subjects...");
        return subjectService.getAllSubjects();
    }

    @GetMapping("/{id}")
    public SubjectResponse getById(@PathVariable Long id) {
        logger.info("Fetching subject with ID: {}", id);
        return subjectService.getSubjectById(id);
    }

    @PostMapping
    public SubjectResponse create(@Valid @RequestBody Subject subject) {
        logger.info("Creating new subject: {}", subject.getName());
        return subjectService.createSubject(subject);
    }

    @PutMapping("/{id}")
    public SubjectResponse update(@PathVariable Long id, @RequestBody Subject updatedData) {
        logger.info("Updating subject with ID: {}", id);
        return subjectService.updateSubject(id, updatedData);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        logger.warn("Attempting to soft delete subject with ID: {}", id);
        subjectService.deleteSubject(id);
    }

    @PatchMapping("/{id}")
    public SubjectResponse patch(@PathVariable Long id, @RequestBody Subject updatedData) {
        logger.info("Patching subject with ID: {}", id);
        return subjectService.patchSubject(id, updatedData);
    }
}
