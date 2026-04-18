package com.example.collegedb.Response;
import java.time.LocalDate;

public class AttendanceResponse {
    private final Long attendanceId;
    private final Long studentId;
    private final LocalDate date;
    private final Boolean present;
    private final Long subjectId;
    private final Long facultyId;

    public AttendanceResponse(Long attendanceId, Long studentId, LocalDate date, Boolean present, Long subjectId, Long facultyId) {
        this.attendanceId = attendanceId;
        this.studentId = studentId;
        this.date = date;
        this.present = present;
        this.subjectId = subjectId;
        this.facultyId = facultyId;
    }

    // Getters
    public Long getAttendanceId() { return attendanceId; }
    public Long getStudentId() { return studentId; }
    public LocalDate getDate() { return date; }
    public Boolean getPresent() { return present; }
    public Long getSubjectId() { return subjectId; }
    public Long getFacultyId() { return facultyId; }
}
