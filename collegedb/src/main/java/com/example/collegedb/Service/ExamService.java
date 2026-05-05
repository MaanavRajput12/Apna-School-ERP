package com.example.collegedb.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.collegedb.Exception.ResourceNotFoundException;
import com.example.collegedb.Repository.ExamRepository;
import com.example.collegedb.Response.ExamResponse;
import com.example.collegedb.entity.Exam;

@Service
@Transactional
public class ExamService {

    private final ExamRepository examRepository;

    public ExamService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @Transactional(readOnly = true)
    public List<ExamResponse> getAllExams() {
        return examRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ExamResponse getExamById(Long id) {
        return toResponse(getExistingExam(id));
    }

    public ExamResponse createExam(Exam exam) {
        return toResponse(examRepository.save(exam));
    }

    public ExamResponse updateExam(Long id, Exam examDetails) {
        Exam exam = getExistingExam(id);
        exam.setExamType(examDetails.getExamType());
        exam.setExamDate(examDetails.getExamDate());
        exam.setExamTime(examDetails.getExamTime());
        exam.setTotaMarks(examDetails.getTotaMarks());
        exam.setSubjectName(examDetails.getSubjectName());
        exam.setSubject(examDetails.getSubject());
        return toResponse(examRepository.save(exam));
    }

    public void deleteExam(Long id) {
        Exam exam = getExistingExam(id);
        examRepository.delete(exam);
    }

    public ExamResponse patchExam(Long id, Exam examDetails) {
        Exam exam = getExistingExam(id);
        if (examDetails.getExamType() != null) {
            exam.setExamType(examDetails.getExamType());
        }
        if (examDetails.getExamDate() != null) {
            exam.setExamDate(examDetails.getExamDate());
        }
        if (examDetails.getExamTime() != null) {
            exam.setExamTime(examDetails.getExamTime());
        }
        if (examDetails.getTotaMarks() != null) {
            exam.setTotaMarks(examDetails.getTotaMarks());
        }
        if (examDetails.getSubjectName() != null) {
            exam.setSubjectName(examDetails.getSubjectName());
        }
        if (examDetails.getSubject() != null) {
            exam.setSubject(examDetails.getSubject());
        }
        return toResponse(examRepository.save(exam));
    }

    private Exam getExistingExam(Long id) {
        return examRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Exam not found with ID: " + id));
    }

    private ExamResponse toResponse(Exam exam) {
        return new ExamResponse(
            exam.getExamId(),
            exam.getExamType(),
            exam.getExamDate(),
            exam.getExamTime(),
            exam.getTotaMarks(),
            exam.getSubjectName()
        );
    }
}
