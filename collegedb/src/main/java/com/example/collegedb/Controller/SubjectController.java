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

import com.example.collegedb.Exception.ResourceNotFoundException;
import com.example.collegedb.Repository.DepartmentRepository;
import com.example.collegedb.Repository.FacultyRepository;
import com.example.collegedb.Repository.SubjectRepository;
import com.example.collegedb.Response.SubjectResponse;
import com.example.collegedb.entity.Department;
import com.example.collegedb.entity.Faculty;
import com.example.collegedb.entity.Subject;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    private static final Logger logger = LoggerFactory.getLogger(SubjectController.class);

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private FacultyRepository facultyRepository;
    
    @GetMapping
    public List<SubjectResponse> getAll() {
        logger.info("Fetching all subjects...");
        return subjectRepository.findAll().stream()
            .map(s -> new SubjectResponse(
                s.getSubjectId(),
                s.getName(),
                s.getSyllabus(),
                s.getDepartment() != null ? s.getDepartment().getDepartmentName() : null,
                s.getFaculty() != null ? s.getFaculty().getFacultyName() : null,
                s.isActive()
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
            subject.getDepartment() != null ? subject.getDepartment().getDepartmentName() : null,
            subject.getFaculty() != null ? subject.getFaculty().getFacultyName() : null,
            subject.isActive()
        );
    }

    @PostMapping
    public SubjectResponse create(@Valid @RequestBody Subject subject) {
        logger.info("Creating new subject: {}", subject.getName());
        subject.setDepartment(resolveDepartment(subject));
        subject.setFaculty(resolveFaculty(subject));
        subject.setActive(true);
        Subject saved = subjectRepository.save(subject);
        logger.debug("Subject created with ID: {}", saved.getSubjectId());
        return new SubjectResponse(
            saved.getSubjectId(),
            saved.getName(),
            saved.getSyllabus(),
            saved.getDepartment() != null ? saved.getDepartment().getDepartmentName() : null,
            saved.getFaculty() != null ? saved.getFaculty().getFacultyName() : null,
            saved.isActive()
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
        subject.setDepartment(resolveDepartment(updatedData));
        subject.setFaculty(resolveFaculty(updatedData));

        Subject updated = subjectRepository.save(subject);
        logger.info("Successfully updated subject with ID: {}", id);

        return new SubjectResponse(
            updated.getSubjectId(),
            updated.getName(),
            updated.getSyllabus(),
            updated.getDepartment() != null ? updated.getDepartment().getDepartmentName() : null,
            updated.getFaculty() != null ? updated.getFaculty().getFacultyName() : null,
            updated.isActive()
        );
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        logger.warn("Attempting to soft delete subject with ID: {}", id);
        Subject subject = subjectRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Subject not found with ID: " + id));
        subject.setActive(false);
        subjectRepository.save(subject);
        logger.info("Subject soft deleted successfully with ID: {}", id);
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
        if (updatedData.getDepartment() != null) {
            subject.setDepartment(resolveDepartment(updatedData));
        }
        if (updatedData.getFaculty() != null) {
            subject.setFaculty(resolveFaculty(updatedData));
        }

        Subject updated = subjectRepository.save(subject);
        logger.info("Successfully patched subject with ID: {}", id);

        return new SubjectResponse(
            updated.getSubjectId(),
            updated.getName(),
            updated.getSyllabus(),
            updated.getDepartment() != null ? updated.getDepartment().getDepartmentName() : null,
            updated.getFaculty() != null ? updated.getFaculty().getFacultyName() : null,
            updated.isActive()
        );
    }

    private Department resolveDepartment(Subject subject) {
        if (subject.getDepartment() == null || subject.getDepartment().getDepartmentId() == null) {
            return null;
        }

        Long departmentId = subject.getDepartment().getDepartmentId();
        return departmentRepository.findById(departmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + departmentId));
    }

    private Faculty resolveFaculty(Subject subject) {
        if (subject.getFaculty() == null || subject.getFaculty().getFacultyId() == null) {
            return null;
        }

        Long facultyId = subject.getFaculty().getFacultyId();
        return facultyRepository.findById(facultyId)
            .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with ID: " + facultyId));
    }
}
