package com.example.collegedb.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "facultySchedule")
public class FacultySchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long facultyScheduleId;

    @ManyToOne
    @JoinColumn(name="facultyId")
    private Faculty faculty;

    @ManyToOne
    @JoinColumn(name="subjectId")
    private Subject subject;

    private LocalDateTime scheduleTime;
    private String classroom;

    // Getters and Setters
    public Long getFacultyScheduleId() { return facultyScheduleId; }
    public void setFacultyScheduleId(Long facultyScheduleId) { this.facultyScheduleId = facultyScheduleId; }

    public Faculty getFaculty() { return faculty; }
    public void setFaculty(Faculty faculty) { this.faculty = faculty; }

    public Subject getSubject() { return subject; }
    public void setSubject(Subject subject) { this.subject = subject; }

    public LocalDateTime getScheduleTime() { return scheduleTime; }
    public void setScheduleTime(LocalDateTime scheduleTime) { this.scheduleTime = scheduleTime; }
    
    public String getClassroom() { return classroom; }
    public void setClassroom(String classroom) { this.classroom = classroom; }

}
