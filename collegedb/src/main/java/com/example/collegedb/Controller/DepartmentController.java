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

import com.example.collegedb.Response.DepartmentResponse;
import com.example.collegedb.Service.DepartmentService;
import com.example.collegedb.entity.Department;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public List<DepartmentResponse> getAll() {
        logger.info("Fetching all departments...");
        return departmentService.getAllDepartments();
    }

    @GetMapping("/{id}")
    public DepartmentResponse getById(@PathVariable Long id) {
        logger.info("Fetching department with ID: {}", id);
        return departmentService.getDepartmentById(id);
    }

    @PostMapping
    public DepartmentResponse create(@Valid @RequestBody Department department) {
        logger.info("Creating new department: {}", department.getDepartmentName());
        return departmentService.createDepartment(department);
    }

    @PutMapping("/{id}")
    public DepartmentResponse update(@PathVariable Long id, @RequestBody Department entity) {
        logger.info("Received request to update department with ID: {}", id);
        return departmentService.updateDepartment(id, entity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        logger.warn("Attempting to delete department with ID: {}", id);
        departmentService.deleteDepartment(id);
    }

    @PatchMapping("/{id}")
    public DepartmentResponse patch(@PathVariable Long id, @RequestBody Department entity) {
        logger.info("Received request to patch department with ID: {}", id);
        return departmentService.patchDepartment(id, entity);
    }
}
