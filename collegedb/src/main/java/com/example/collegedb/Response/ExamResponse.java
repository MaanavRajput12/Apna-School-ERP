package com.example.collegedb.Response;

import java.time.LocalDate;
import java.time.LocalTime;

public class ExamResponse {
    private final Long examId;
    private final String examType;
    private final LocalDate examDate;
    private final LocalTime examTime;
    private final Integer totalMarks;
    private final String subjectName;
    
    public ExamResponse(Long examId, String examType, LocalDate examDate, LocalTime examTime, Integer totalMarks, String subjectName) {
        this.examId = examId;
        this.examType = examType;
        this.examDate = examDate;
        this.examTime = examTime;
        this.totalMarks = totalMarks;
        this.subjectName = subjectName;
    }

    public Long getExamId() {return examId;}
    public String getExamType() {return examType;}
    public LocalDate getExamDate() {return examDate;}
    public LocalTime getExamTime() {return examTime;}
    public Integer getTotalMarks() {return totalMarks;}
    public String getSubjectName() {return subjectName;}
}
