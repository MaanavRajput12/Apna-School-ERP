package com.example.collegedb.dto.attendance;

import java.time.LocalDate;

import com.example.collegedb.entity.enums.AttendanceStatus;

import jakarta.validation.constraints.NotNull;

public class AttendanceRequestDTO {
    @NotNull
    private Long studentId;

    @NotNull
    private LocalDate date;

    private Long subjectId;

    @NotNull
    private AttendanceStatus status;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }
}
