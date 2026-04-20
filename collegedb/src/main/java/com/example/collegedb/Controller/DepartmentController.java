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
import com.example.collegedb.Response.DepartmentResponse;
import com.example.collegedb.entity.Department;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/departments")

public class DepartmentController {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    private DepartmentRepository departmentRepository;

    @GetMapping
    public List<DepartmentResponse> getAll() {
        logger.info("Fetching all departments...");
        return departmentRepository.findAll().stream()
            .map(d -> new DepartmentResponse(
                d.getDepartmentId(),
                d.getDepartmentName(),
                d.getNumberOfStudents(),
                d.getNumberOfFaculties()
            ))
            .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public DepartmentResponse getById(@PathVariable Long id) {
        logger.info("Fetching department with ID: {}", id);
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
        logger.info("Department with ID: {} fetched successfully", id);
        return new DepartmentResponse(
            department.getDepartmentId(),
            department.getDepartmentName(),
            department.getNumberOfStudents(),
            department.getNumberOfFaculties()
        );
    }

    @PostMapping
    public DepartmentResponse create(@Valid @RequestBody Department department) {
        logger.info("Creating new department: {}", department.getDepartmentName());
        Department saved = departmentRepository.save(department);
        logger.debug("Department created with ID: {}", saved.getDepartmentId());
        return new DepartmentResponse(
            saved.getDepartmentId(),
            saved.getDepartmentName(),
            saved.getNumberOfStudents(),
            saved.getNumberOfFaculties()
        );
    }
    @PutMapping("/{id}")
    public DepartmentResponse update(@PathVariable Long id, @RequestBody Department entity) {
        logger.info("Received request to update department with ID: {}", id);
        Department department = departmentRepository.findById(id)
            .orElseThrow(() ->{
                logger.error("Department not found with ID: {}", id);
                return new ResourceNotFoundException("Department not found with ID: " + id);
            });

        logger.debug("Current department data: Name={}, Students={}, Faculties={}", 
            department.getDepartmentName(), 
            department.getNumberOfStudents(), 
            department.getNumberOfFaculties());

        department.setDepartmentName(entity.getDepartmentName());
        department.setNumberOfStudents(entity.getNumberOfStudents());
        department.setNumberOfFaculties(entity.getNumberOfFaculties());
        
        Department updated = departmentRepository.save(department);

        logger.info("Successfully updated department with ID: {}", id);

        return new DepartmentResponse(
            updated.getDepartmentId(),
            updated.getDepartmentName(),
            updated.getNumberOfStudents(),
            updated.getNumberOfFaculties()
        );
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        logger.warn("Attempting to delete department with ID: {}", id);
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));
        departmentRepository.delete(department);
        logger.info("Department deleted successfully with ID: {}", id);
    }

    @PatchMapping("/{id}")
    public DepartmentResponse patch(@PathVariable Long id, @RequestBody Department entity) {
        logger.info("Received request to patch department with ID: {}", id);
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));

        if (entity.getDepartmentName() != null) {
            department.setDepartmentName(entity.getDepartmentName());
        }
        if (entity.getNumberOfStudents() != null) {
            department.setNumberOfStudents(entity.getNumberOfStudents());
        }
        if (entity.getNumberOfFaculties() != null) {
            department.setNumberOfFaculties(entity.getNumberOfFaculties());
        }

        Department updated = departmentRepository.save(department);
        logger.info("Successfully patched department with ID: {}", id);

        return new DepartmentResponse(
            updated.getDepartmentId(),
            updated.getDepartmentName(),
            updated.getNumberOfStudents(),
            updated.getNumberOfFaculties()
        );
    }
}
