package com.example.collegedb.Response;

public class TimetableResponse {
    private final Long timeTableId;
    private final Long semester;
    private final String day;
    private final String timeSlot;
    private final String departmentName;
    private final String facultyName;
    public TimetableResponse(Long timeTableId, Long semester, String day, String timeSlot, String departmentName, String facultyName) {
        this.timeTableId = timeTableId;
        this.semester = semester;
        this.day = day;
        this.timeSlot = timeSlot;
        this.departmentName = departmentName;
        this.facultyName = facultyName;
    }

    // Getters
    public Long getTimeTableId() { return timeTableId; }
    public Long getSemester() { return semester; }
    public String getDay() { return day; }
    public String getTimeSlot() { return timeSlot; }
    public String getDepartmentName() { return departmentName; }
    public String getFacultyName() { return facultyName; }
}
