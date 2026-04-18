package com.example.collegedb.Response;

public class FacultyScheduleResponse {
    private Long facultyScheduleId;
    private long facultyId;
    private long subjectId;
    private String scheduleTime;
    private String classroom;

    public FacultyScheduleResponse(Long facultyScheduleId, long facultyId, long subjectId, String scheduleTime, String classroom) {
        this.facultyScheduleId = facultyScheduleId;
        this.facultyId = facultyId;
        this.subjectId = subjectId;
        this.scheduleTime = scheduleTime;
        this.classroom = classroom;
    }

    public Long getFacultyScheduleId() { return facultyScheduleId; }
    public void setFacultyScheduleId(Long facultyScheduleId) { this.facultyScheduleId = facultyScheduleId; }
    public long getFacultyId() { return facultyId; }
    public void setFacultyId(long facultyId) { this.facultyId = facultyId; }
    public long getSubjectId() { return subjectId; }
    public void setSubjectId(long subjectId) { this.subjectId = subjectId; }
    public String getScheduleTime() { return scheduleTime; }
    public void setScheduleTime(String scheduleTime) { this.scheduleTime = scheduleTime; }
    public String getClassroom() { return classroom; }
    public void setClassroom(String classroom) { this.classroom = classroom; }
}
