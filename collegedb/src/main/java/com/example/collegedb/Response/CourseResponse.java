package com.example.collegedb.Response;

public class CourseResponse {
    private final Long courseId;
    private final String courseName;
    private final Integer credits;
    private final Long departmentId;
    private final String departmentName;

    public CourseResponse(Long courseId, String courseName, Integer credits, Long departmentId, String departmentName) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }

    public Long getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public Integer getCredits() { return credits; }
    public Long getDepartmentId() { return departmentId; }
    public String getDepartmentName() { return departmentName; }
}
