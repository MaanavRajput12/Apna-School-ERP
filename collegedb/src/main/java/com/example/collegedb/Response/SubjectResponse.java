package com.example.collegedb.Response;

public class SubjectResponse {
    private final Long subjectId;
    private final String name;
    private final String syllabus;
    private final String departmentName;
    private final String facultyName;
    private final boolean active;

    public SubjectResponse(Long subjectId, String name, String syllabus, String departmentName, String facultyName, boolean active) {
        this.subjectId = subjectId;
        this.name = name;
        this.syllabus = syllabus;
        this.departmentName = departmentName;
        this.facultyName = facultyName;
        this.active = active;
    }

    public Long getSubjectId() { return subjectId; }
    public String getName() { return name; }
    public String getSyllabus() { return syllabus; }
    public String getDepartmentName() { return departmentName; }
    public String getFacultyName() { return facultyName; }
    public boolean isActive() { return active; }
}
