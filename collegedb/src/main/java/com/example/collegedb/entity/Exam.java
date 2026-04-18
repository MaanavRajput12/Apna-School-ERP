package com.example.collegedb.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "exams")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long examId;

    private String examType;
    private LocalDate examDate;
    private LocalTime examTime;
    private Integer totaMarks;
    private String subjectName;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    // Getters and Setters
    public Long getExamId() {return examId;}
    public void setExamId(Long examId) {this.examId = examId;}

    public String getExamType() {return examType;}
    public void setExamType(String examType) {this.examType = examType;}

    public LocalDate getExamDate() {return examDate;}
    public void setExamDate(LocalDate examDate) {this.examDate = examDate;}

    public LocalTime getExamTime() {return examTime;}
    public void setExamTime(LocalTime examTime) {this.examTime = examTime;}

    public Integer getTotaMarks() {return totaMarks;}
    public void setTotaMarks(Integer totaMarks) {this.totaMarks = totaMarks;}

    public String getSubjectName() {return subjectName;}
    public void setSubjectName(String subjectName) {this.subjectName = subjectName;}

    public Subject getSubject() {return subject;}
    public void setSubject(Subject subject) {this.subject = subject;}

}
