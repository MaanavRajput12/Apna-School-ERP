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

import com.example.collegedb.Repository.FacultyRepository;
import com.example.collegedb.Response.FacultyResponse;
import com.example.collegedb.entity.Faculty;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private static final Logger logger = LoggerFactory.getLogger(FacultyController.class);

    @Autowired
    private FacultyRepository facultyRepository;

    @GetMapping
    public List<FacultyResponse> getAll() {
        logger.info("Fetching all faculty records...");
        return facultyRepository.findAll().stream()
            .map(f -> new FacultyResponse(
                f.getFacultyId(),
                f.getFacultyName(),
                f.getEmail(),
                f.getDesignation(),
                f.getDepartment(),
                f.getPhone(),
                f.getAddress()    
            ))
            .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public FacultyResponse getById(@PathVariable Long id) {
        logger.info("Fetching faculty record with ID: {}", id);
        Faculty faculty = facultyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Faculty not found with ID: " + id));
        return new FacultyResponse(
            faculty.getFacultyId(),
            faculty.getFacultyName(),
            faculty.getEmail(),
            faculty.getDesignation(),
            faculty.getDepartment(),
            faculty.getPhone(),
            faculty.getAddress()
        );
    }

    @PostMapping
    public FacultyResponse create(@Valid @RequestBody com.example.collegedb.entity.Faculty faculty) {
        logger.info("Creating new faculty record: {}", faculty.getFacultyName());
        Faculty saved = facultyRepository.save(faculty);
        logger.debug("Faculty record created with ID: {}", saved.getFacultyId());
        return new FacultyResponse(
            saved.getFacultyId(),
            saved.getFacultyName(),
            saved.getEmail(),
            saved.getDesignation(),
            saved.getDepartment(),
            saved.getPhone(),
            saved.getAddress()
        );
    }

    @PutMapping("{id}")
    public FacultyResponse update(@PathVariable Long id, @RequestBody Faculty entity) {
        logger.info("Updating faculty record with ID: {}", id);
        Faculty existing = facultyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Faculty not found with ID: " + id));

        existing.setFacultyName(entity.getFacultyName());
        existing.setEmail(entity.getEmail());
        existing.setDesignation(entity.getDesignation());
        existing.setDepartment(entity.getDepartment());
        existing.setPhone(entity.getPhone());
        existing.setAddress(entity.getAddress());

        Faculty updated = facultyRepository.save(existing);
        logger.debug("Faculty record updated with ID: {}", updated.getFacultyId());

        return new FacultyResponse(
            updated.getFacultyId(),
            updated.getFacultyName(),
            updated.getEmail(),
            updated.getDesignation(),
            updated.getDepartment(),
            updated.getPhone(),
            updated.getAddress()
        );
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        logger.warn("Attempting to delete faculty record with ID: {}", id);
        Faculty faculty = facultyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Faculty not found with ID: " + id));
        facultyRepository.delete(faculty);
        logger.info("Faculty record deleted successfully with ID: {}", id);
    }
    
    @PatchMapping("{id}")
    public FacultyResponse patch(@PathVariable Long id, @RequestBody Faculty entity) {
        logger.info("Patching faculty record with ID: {}", id);
        Faculty existing = facultyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Faculty not found with ID: " + id));

        if (entity.getFacultyName() != null) {
            existing.setFacultyName(entity.getFacultyName());
        }
        if (entity.getEmail() != null) {
            existing.setEmail(entity.getEmail());
        }
        if (entity.getDesignation() != null) {
            existing.setDesignation(entity.getDesignation());
        }
        if (entity.getDepartment() != null) {
            existing.setDepartment(entity.getDepartment());
        }
        if (entity.getPhone() != null) {
            existing.setPhone(entity.getPhone());
        }
        if (entity.getAddress() != null) {
            existing.setAddress(entity.getAddress());
        }

        Faculty updated = facultyRepository.save(existing);
        logger.debug("Faculty record patched with ID: {}", updated.getFacultyId());

        return new FacultyResponse(
            updated.getFacultyId(),
            updated.getFacultyName(),
            updated.getEmail(),
            updated.getDesignation(),
            updated.getDepartment(),
            updated.getPhone(),
            updated.getAddress()
        );
    }

}
