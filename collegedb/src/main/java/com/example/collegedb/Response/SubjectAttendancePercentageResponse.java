package com.example.collegedb.Response;

public class SubjectAttendancePercentageResponse {
    private final Long subjectId;
    private final String subjectName;
    private final long presentCount;
    private final long totalClasses;
    private final double percentage;

    public SubjectAttendancePercentageResponse(
        Long subjectId,
        String subjectName,
        long presentCount,
        long totalClasses,
        double percentage
    ) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.presentCount = presentCount;
        this.totalClasses = totalClasses;
        this.percentage = percentage;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public long getPresentCount() {
        return presentCount;
    }

    public long getTotalClasses() {
        return totalClasses;
    }

    public double getPercentage() {
        return percentage;
    }
}
