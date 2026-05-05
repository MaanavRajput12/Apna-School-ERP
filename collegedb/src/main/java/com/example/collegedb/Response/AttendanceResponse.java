package com.example.collegedb.Response;
import java.time.LocalDate;

import com.example.collegedb.entity.enums.AttendanceStatus;

public class AttendanceResponse {
    private final Long id;
    private final Long studentId;
    private final LocalDate date;
    private final AttendanceStatus status;
    private final Long subjectId;

    public AttendanceResponse(Long id, Long studentId, LocalDate date, AttendanceStatus status, Long subjectId) {
        this.id = id;
        this.studentId = studentId;
        this.date = date;
        this.status = status;
        this.subjectId = subjectId;
    }

    // Getters
    public Long getId() { return id; }
    public Long getStudentId() { return studentId; }
    public LocalDate getDate() { return date; }
    public AttendanceStatus getStatus() { return status; }
    public Long getSubjectId() { return subjectId; }
}
