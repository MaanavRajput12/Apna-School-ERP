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
import com.example.collegedb.Repository.FeesRepository;
import com.example.collegedb.Repository.StudentRepository;
import com.example.collegedb.Response.FeesResponse;
import com.example.collegedb.entity.Fees;
import com.example.collegedb.entity.Student;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/fees")
public class FeesController {
    private static final Logger logger = LoggerFactory.getLogger(FeesController.class);

    @Autowired
    private FeesRepository feesRepository;

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping
    public List<FeesResponse> getAll() {
        logger.info("Fetching all fees records...");
        return feesRepository.findAll().stream()
            .map(f -> new FeesResponse(
                f.getFeesId(),
                f.getAmount(),
                f.getFeesStatus(),
                f.getDueDate().toString(),
                f.getStudent() != null ? f.getStudent().getStudentId() : null,
                f.getStudent() != null ? f.getStudent().getName() : null
            ))
            .collect(Collectors.toList());
    }
    @PostMapping
    public FeesResponse create(@Valid @RequestBody Fees fees) {
        logger.info("Creating new fees record with amount: {}", fees.getAmount());
        Fees saved = feesRepository.save(fees);
        linkStudent(saved, fees.getStudentId());
        logger.debug("Fees record created with ID: {}", saved.getFeesId());
        return new FeesResponse(
            saved.getFeesId(),
            saved.getAmount(),
            saved.getFeesStatus(),
            saved.getDueDate().toString(),
            saved.getStudent() != null ? saved.getStudent().getStudentId() : null,
            saved.getStudent() != null ? saved.getStudent().getName() : null
        );
    }
    @PutMapping("{id}")
    public FeesResponse update(@PathVariable Long id, @RequestBody Fees updatedFees) {
    logger.info("Updating fees record with ID: {}", id);

    Fees existingFees = feesRepository.findById(id)
        .orElseThrow(() -> {
            logger.error("Fees not found with ID: {}", id);
            return new ResourceNotFoundException("Fees not found with ID: " + id);
        });

    existingFees.setAmount(updatedFees.getAmount());
    existingFees.setFeesStatus(updatedFees.getFeesStatus());
    existingFees.setDueDate(updatedFees.getDueDate());

    Fees updated = feesRepository.save(existingFees);
    linkStudent(updated, updatedFees.getStudentId());
    logger.info("Successfully updated fees record with ID: {}", id);

    return new FeesResponse(
        updated.getFeesId(),
        updated.getAmount(),
        updated.getFeesStatus(),
        updated.getDueDate().toString(),
        updated.getStudent() != null ? updated.getStudent().getStudentId() : null,
        updated.getStudent() != null ? updated.getStudent().getName() : null
    );
}

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        logger.warn("Attempting to delete fees record with ID: {}", id);
        Fees fees = feesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Fees record not found with ID: " + id));
        feesRepository.delete(fees);
        logger.info("Fees record deleted successfully with ID: {}", id);
    }

    @PatchMapping("{id}")
    public FeesResponse patch(@PathVariable Long id, @RequestBody Fees updatedFields) {
        logger.info("Patching fees record with ID: {}", id);

        Fees existingFees = feesRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("Fees not found with ID: {}", id);
                return new ResourceNotFoundException("Fees not found with ID: " + id);
            });

        if (updatedFields.getAmount() != null) {
            existingFees.setAmount(updatedFields.getAmount());
        }
        if (updatedFields.getFeesStatus() != null) {
            existingFees.setFeesStatus(updatedFields.getFeesStatus());
        }
        if (updatedFields.getDueDate() != null) {
            existingFees.setDueDate(updatedFields.getDueDate());
        }

        Fees updated = feesRepository.save(existingFees);
        linkStudent(updated, updatedFields.getStudentId());
        logger.info("Successfully patched fees record with ID: {}", id);

        return new FeesResponse(
            updated.getFeesId(),
            updated.getAmount(),
            updated.getFeesStatus(),
            updated.getDueDate().toString(),
            updated.getStudent() != null ? updated.getStudent().getStudentId() : null,
            updated.getStudent() != null ? updated.getStudent().getName() : null
        );
    }

    private void linkStudent(Fees fees, Long studentId) {
        if (studentId == null) {
            return;
        }
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + studentId));
        student.setFees(fees);
        studentRepository.save(student);
        fees.setStudent(student);
    }
    
}
