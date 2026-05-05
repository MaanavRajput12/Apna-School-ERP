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

import com.example.collegedb.Response.ExamResponse;
import com.example.collegedb.Service.ExamService;
import com.example.collegedb.entity.Exam;

@RestController
@RequestMapping("/exams")
public class ExamController {

    private static final Logger logger = LoggerFactory.getLogger(ExamController.class);
    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping
    public ExamResponse createExam(@RequestBody Exam exam) {
        logger.info("Creating new exam with details: {}", exam);
        return examService.createExam(exam);
    }

    @GetMapping("/{id}")
    public ExamResponse getExamById(@PathVariable Long id) {
        logger.info("Fetching exam with ID: {}", id);
        return examService.getExamById(id);
    }

    @GetMapping
    public List<ExamResponse> getAllExams() {
        logger.info("Fetching all exams...");
        return examService.getAllExams();
    }

    @PutMapping("/{id}")
    public ExamResponse updateExam(@PathVariable Long id, @RequestBody Exam examDetails) {
        logger.info("Updating exam with ID: {}", id);
        return examService.updateExam(id, examDetails);
    }

    @DeleteMapping("/{id}")
    public String deleteExam(@PathVariable Long id) {
        logger.info("Deleting exam with ID: {}", id);
        examService.deleteExam(id);
        return "Exam deleted successfully";
    }

    @PatchMapping("/{id}")
    public ExamResponse patchExam(@PathVariable Long id, @RequestBody Exam examDetails) {
        logger.info("Patching exam with ID: {}", id);
        return examService.patchExam(id, examDetails);
    }
}
