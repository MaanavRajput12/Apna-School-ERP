package com.example.collegedb.Response;

public class CourseResponse {
    private final Long courseId;
    private final String courseName;
    private final Integer credits;

    public CourseResponse(Long courseId, String courseName, Integer credits) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
    }

    public Long getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public Integer getCredits() { return credits; }
}
