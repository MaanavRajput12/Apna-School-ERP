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

import com.example.collegedb.Repository.SubjectRepository;
import com.example.collegedb.Response.SubjectResponse;
import com.example.collegedb.entity.Subject;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    private static final Logger logger = LoggerFactory.getLogger(SubjectController.class);

    @Autowired
    private SubjectRepository subjectRepository;
    
    @GetMapping
    public List<SubjectResponse> getAll() {
        logger.info("Fetching all subjects...");
        return subjectRepository.findAll().stream()
            .map(s -> new SubjectResponse(
                s.getSubjectId(),
                s.getName(),
                s.getSyllabus(),
                s.getCourse() != null ? s.getCourse().getCourseName() : null,
                s.getFaculty() != null ? s.getFaculty().getFacultyName() : null
            ))
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public SubjectResponse getById(@PathVariable Long id) {
        logger.info("Fetching subject with ID: {}", id);
        Subject subject = subjectRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Subject not found with ID: " + id));
        logger.info("Subject with ID: {} fetched successfully", id);
        return new SubjectResponse(
            subject.getSubjectId(),
            subject.getName(),
            subject.getSyllabus(),
            subject.getCourse() != null ? subject.getCourse().getCourseName() : null,
            subject.getFaculty() != null ? subject.getFaculty().getFacultyName() : null
        );
    }

    @PostMapping
    public SubjectResponse create(@Valid @RequestBody Subject subject) {
        logger.info("Creating new subject: {}", subject.getName());
        Subject saved = subjectRepository.save(subject);
        logger.debug("Subject created with ID: {}", saved.getSubjectId());
        return new SubjectResponse(
            saved.getSubjectId(),
            saved.getName(),
            saved.getSyllabus(),
            saved.getCourse() != null ? saved.getCourse().getCourseName() : null,
            saved.getFaculty() != null ? saved.getFaculty().getFacultyName() : null
        );
    }

    @PutMapping("/{id}")
    public SubjectResponse update(@PathVariable Long id, @RequestBody Subject updatedData) {
        logger.info("Updating subject with ID: {}", id);

        Subject subject = subjectRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Subject not found with ID: {}", id);
                return new RuntimeException("Subject not found with ID: " + id);
            });

        subject.setName(updatedData.getName());
        subject.setSyllabus(updatedData.getSyllabus());
        subject.setCourse(updatedData.getCourse());
        subject.setFaculty(updatedData.getFaculty());

        Subject updated = subjectRepository.save(subject);
        logger.info("Successfully updated subject with ID: {}", id);

        return new SubjectResponse(
            updated.getSubjectId(),
            updated.getName(),
            updated.getSyllabus(),
            updated.getCourse() != null ? updated.getCourse().getCourseName() : null,
            updated.getFaculty() != null ? updated.getFaculty().getFacultyName() : null
        );
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        logger.warn("Attempting to delete subject with ID: {}", id);
        Subject subject = subjectRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Subject not found with ID: " + id));
        subjectRepository.delete(subject);
        logger.info("Subject deleted successfully with ID: {}", id);
    }

    @PatchMapping("/{id}")
    public SubjectResponse patch(@PathVariable Long id, @RequestBody Subject updatedData) {
        logger.info("Patching subject with ID: {}", id);

        Subject subject = subjectRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Subject not found with ID: {}", id);
                return new RuntimeException("Subject not found with ID: " + id);
            });

        if (updatedData.getName() != null) {
            subject.setName(updatedData.getName());
        }
        if (updatedData.getSyllabus() != null) {
            subject.setSyllabus(updatedData.getSyllabus());
        }
        if (updatedData.getCourse() != null) {
            subject.setCourse(updatedData.getCourse());
        }
        if (updatedData.getFaculty() != null) {
            subject.setFaculty(updatedData.getFaculty());
        }

        Subject updated = subjectRepository.save(subject);
        logger.info("Successfully patched subject with ID: {}", id);

        return new SubjectResponse(
            updated.getSubjectId(),
            updated.getName(),
            updated.getSyllabus(),
            updated.getCourse() != null ? updated.getCourse().getCourseName() : null,
            updated.getFaculty() != null ? updated.getFaculty().getFacultyName() : null
        );
    }
}