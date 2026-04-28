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
import com.example.collegedb.Repository.ExamRepository;
import com.example.collegedb.Response.ExamResponse;
import com.example.collegedb.entity.Exam;

@RestController
@RequestMapping("/exams")
public class ExamController {
    private static final Logger logger = LoggerFactory.getLogger(ExamController.class);
    @Autowired
    private ExamRepository examRepository;

    @PostMapping
    public ExamResponse createExam(@RequestBody Exam exam) {
        logger.info("Creating new exam with details: {}", exam);
        Exam savedExam = examRepository.save(exam);
        logger.info("Exam created with ID: {}", savedExam.getExamId());
        return new ExamResponse(
            savedExam.getExamId(),
            savedExam.getExamType(),
            savedExam.getExamDate(),
            savedExam.getExamTime(),
            savedExam.getTotaMarks(),
            savedExam.getSubjectName()
        );
    }

    @GetMapping("/{id}")
    public ExamResponse getExamById(@PathVariable Long id) {
        logger.info("Fetching exam with ID: {}", id);
        Exam exam = examRepository.findById(id).orElseThrow(() -> {
            logger.error("Exam not found with ID: {}", id);
            return new ResourceNotFoundException("Exam not found with ID: " + id);
        });
        logger.info("Exam found: {}", exam);
        return new ExamResponse(
            exam.getExamId(),
            exam.getExamType(),
            exam.getExamDate(),
            exam.getExamTime(),
            exam.getTotaMarks(),
            exam.getSubjectName()
        );
    }

    @GetMapping
    public List<ExamResponse> getAllExams() {
        logger.info("Fetching all exams...");
        List<Exam> exams = examRepository.findAll();
        logger.info("Total exams found: {}", exams.size());
        return exams.stream()
            .map(exam -> new ExamResponse(
                exam.getExamId(),
                exam.getExamType(),
                exam.getExamDate(),
                exam.getExamTime(),
                exam.getTotaMarks(),
                exam.getSubjectName()
            ))
            .collect(Collectors.toList());
    }
    

    @PutMapping("/{id}")
    public ExamResponse updateExam(@PathVariable Long id, @RequestBody Exam examDetails) {
        logger.info("Updating exam with ID: {}", id);
        Exam exam = examRepository.findById(id).orElseThrow(() -> {
            logger.error("Exam not found with ID: {}", id);
            return new ResourceNotFoundException("Exam not found with ID: " + id);
        });

        exam.setExamType(examDetails.getExamType());
        exam.setExamDate(examDetails.getExamDate());
        exam.setExamTime(examDetails.getExamTime());
        exam.setTotaMarks(examDetails.getTotaMarks());
        exam.setSubjectName(examDetails.getSubjectName());
        exam.setSubject(examDetails.getSubject());

        Exam updatedExam = examRepository.save(exam);
        logger.info("Exam updated: {}", updatedExam);
        return new ExamResponse(
            updatedExam.getExamId(),
            updatedExam.getExamType(),
            updatedExam.getExamDate(),
            updatedExam.getExamTime(),
            updatedExam.getTotaMarks(),
            updatedExam.getSubjectName()
        );
    }
    
    @DeleteMapping("/{id}")
    public String deleteExam(@PathVariable Long id) {
        logger.info("Deleting exam with ID: {}", id);
        Exam exam = examRepository.findById(id).orElseThrow(() -> {
            logger.error("Exam not found with ID: {}", id);
            return new ResourceNotFoundException("Exam not found with ID: " + id);
        });
        examRepository.delete(exam);
        logger.info("Exam deleted with ID: {}", id);
        return "Exam deleted successfully";
    }

    @PatchMapping("/{id}")
    public ExamResponse patchExam(@PathVariable Long id, @RequestBody Exam examDetails) {
        logger.info("Patching exam with ID: {}", id);
        Exam exam = examRepository.findById(id).orElseThrow(() -> {
            logger.error("Exam not found with ID: {}", id);
            return new ResourceNotFoundException("Exam not found with ID: " + id);
        });

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

        Exam updatedExam = examRepository.save(exam);
        logger.info("Exam patched: {}", updatedExam);
        return new ExamResponse(
            updatedExam.getExamId(),
            updatedExam.getExamType(),
            updatedExam.getExamDate(),
            updatedExam.getExamTime(),
            updatedExam.getTotaMarks(),
            updatedExam.getSubjectName()
        );
    }
}
