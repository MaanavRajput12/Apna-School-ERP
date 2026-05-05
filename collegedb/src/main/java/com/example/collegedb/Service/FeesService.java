package com.example.collegedb.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.collegedb.Exception.ResourceNotFoundException;
import com.example.collegedb.Repository.FeesRepository;
import com.example.collegedb.Repository.StudentRepository;
import com.example.collegedb.Response.FeesResponse;
import com.example.collegedb.entity.Fees;
import com.example.collegedb.entity.Student;

@Service
@Transactional
public class FeesService {

    private final FeesRepository feesRepository;
    private final StudentRepository studentRepository;

    public FeesService(FeesRepository feesRepository, StudentRepository studentRepository) {
        this.feesRepository = feesRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional(readOnly = true)
    public List<FeesResponse> getAllFees() {
        return feesRepository.findAll().stream().map(this::toResponse).toList();
    }

    public FeesResponse createFees(Fees fees) {
        Fees saved = feesRepository.save(fees);
        linkStudent(saved, fees.getStudentId());
        return toResponse(saved);
    }

    public FeesResponse updateFees(Long id, Fees updatedFees) {
        Fees existingFees = getExistingFees(id);
        existingFees.setAmount(updatedFees.getAmount());
        existingFees.setFeesStatus(updatedFees.getFeesStatus());
        existingFees.setDueDate(updatedFees.getDueDate());
        Fees updated = feesRepository.save(existingFees);
        linkStudent(updated, updatedFees.getStudentId());
        return toResponse(updated);
    }

    public void deleteFees(Long id) {
        Fees fees = getExistingFees(id);
        feesRepository.delete(fees);
    }

    public FeesResponse patchFees(Long id, Fees updatedFields) {
        Fees existingFees = getExistingFees(id);
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
        return toResponse(updated);
    }

    private Fees getExistingFees(Long id) {
        return feesRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Fees not found with ID: " + id));
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

    private FeesResponse toResponse(Fees fees) {
        return new FeesResponse(
            fees.getFeesId(),
            fees.getAmount(),
            fees.getFeesStatus(),
            fees.getDueDate().toString(),
            fees.getStudent() != null ? fees.getStudent().getStudentId() : null,
            fees.getStudent() != null ? fees.getStudent().getName() : null
        );
    }
}
