package com.example.collegedb.Response;

public class FacultyScheduleResponse {
    private Long facultyScheduleId;
    private Long facultyId;
    private Long departmentId;
    private String departmentName;
    private Long subjectId;
    private String scheduleTime;
    private String classroom;

    public FacultyScheduleResponse(Long facultyScheduleId, Long facultyId, Long departmentId, String departmentName, Long subjectId, String scheduleTime, String classroom) {
        this.facultyScheduleId = facultyScheduleId;
        this.facultyId = facultyId;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.subjectId = subjectId;
        this.scheduleTime = scheduleTime;
        this.classroom = classroom;
    }

    public Long getFacultyScheduleId() { return facultyScheduleId; }
    public void setFacultyScheduleId(Long facultyScheduleId) { this.facultyScheduleId = facultyScheduleId; }
    public Long getFacultyId() { return facultyId; }
    public void setFacultyId(Long facultyId) { this.facultyId = facultyId; }
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
    public String getScheduleTime() { return scheduleTime; }
    public void setScheduleTime(String scheduleTime) { this.scheduleTime = scheduleTime; }
    public String getClassroom() { return classroom; }
    public void setClassroom(String classroom) { this.classroom = classroom; }
}
