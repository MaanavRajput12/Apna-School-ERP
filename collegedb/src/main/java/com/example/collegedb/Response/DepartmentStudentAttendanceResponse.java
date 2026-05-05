package com.example.collegedb.Response;

public class DepartmentStudentAttendanceResponse {
    private final Long studentId;
    private final String name;
    private final String rollNo;
    private final String department;
    private final String course;
    private final String semester;
    private final double attendancePercentage;

    public DepartmentStudentAttendanceResponse(
        Long studentId,
        String name,
        String rollNo,
        String department,
        String course,
        String semester,
        double attendancePercentage
    ) {
        this.studentId = studentId;
        this.name = name;
        this.rollNo = rollNo;
        this.department = department;
        this.course = course;
        this.semester = semester;
        this.attendancePercentage = attendancePercentage;
    }

    public Long getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    public String getRollNo() {
        return rollNo;
    }

    public String getDepartment() {
        return department;
    }

    public String getCourse() {
        return course;
    }

    public String getSemester() {
        return semester;
    }

    public double getAttendancePercentage() {
        return attendancePercentage;
    }
}
