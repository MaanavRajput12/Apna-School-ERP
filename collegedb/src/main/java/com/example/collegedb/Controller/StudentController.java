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

import com.example.collegedb.Response.StudentResponse;
import com.example.collegedb.Service.StudentService;
import com.example.collegedb.dto.student.StudentCreateRequest;
import com.example.collegedb.dto.student.StudentPatchRequest;
import com.example.collegedb.dto.student.StudentUpdateRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/students")
public class StudentController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<StudentResponse> getAll() {
        logger.info("Fetching all student records...");
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public StudentResponse getById(@PathVariable Long id) {
        logger.info("Fetching student record with ID: {}", id);
        return studentService.getStudentById(id);
    }

    @PostMapping
    public StudentResponse create(@Valid @RequestBody StudentCreateRequest request) {
        logger.info("Creating new student record: {}", request.getName());
        return studentService.createStudent(request);
    }

    @PutMapping("/{id}")
    public StudentResponse update(@PathVariable Long id, @Valid @RequestBody StudentUpdateRequest request) {
        logger.info("Updating student record with ID: {}", id);
        return studentService.updateStudent(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        logger.warn("Attempting to delete student record with ID: {}", id);
        studentService.deleteStudent(id);
        logger.info("Student record deleted successfully with ID: {}", id);
    }

    @PatchMapping("/{id}")
    public StudentResponse patch(@PathVariable Long id, @Valid @RequestBody StudentPatchRequest request) {
        logger.info("Patching student record with ID: {}", id);
        return studentService.patchStudent(id, request);
    }
}
