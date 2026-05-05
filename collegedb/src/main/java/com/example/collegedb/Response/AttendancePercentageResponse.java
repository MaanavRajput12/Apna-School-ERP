package com.example.collegedb.Response;

public class AttendancePercentageResponse {
    private final Long studentId;
    private final double percentage;

    public AttendancePercentageResponse(Long studentId, double percentage) {
        this.studentId = studentId;
        this.percentage = percentage;
    }

    public Long getStudentId() {
        return studentId;
    }

    public double getPercentage() {
        return percentage;
    }
}
