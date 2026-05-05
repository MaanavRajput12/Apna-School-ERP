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

import com.example.collegedb.Response.FacultyResponse;
import com.example.collegedb.Service.FacultyService;
import com.example.collegedb.entity.Faculty;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private static final Logger logger = LoggerFactory.getLogger(FacultyController.class);
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping
    public List<FacultyResponse> getAll() {
        logger.info("Fetching all faculty records...");
        return facultyService.getAllFaculty();
    }

    @GetMapping("{id}")
    public FacultyResponse getById(@PathVariable Long id) {
        logger.info("Fetching faculty record with ID: {}", id);
        return facultyService.getFacultyById(id);
    }

    @PostMapping
    public FacultyResponse create(@Valid @RequestBody Faculty faculty) {
        logger.info("Creating new faculty record: {}", faculty.getFacultyName());
        return facultyService.createFaculty(faculty);
    }

    @PutMapping("{id}")
    public FacultyResponse update(@PathVariable Long id, @RequestBody Faculty entity) {
        logger.info("Updating faculty record with ID: {}", id);
        return facultyService.updateFaculty(id, entity);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        logger.warn("Attempting to delete faculty record with ID: {}", id);
        facultyService.deleteFaculty(id);
    }

    @PatchMapping("{id}")
    public FacultyResponse patch(@PathVariable Long id, @RequestBody Faculty entity) {
        logger.info("Patching faculty record with ID: {}", id);
        return facultyService.patchFaculty(id, entity);
    }
}
