package com.example.collegedb.Response;

public class SubjectResponse {
    private final Long subjectId;
    private final String name;
    private final String syllabus;
    private final String courseName;
    private final String facultyName;

    public SubjectResponse(Long subjectId, String name, String courseName, String facultyName, String syllabus) {
        this.subjectId = subjectId;
        this.name = name;
        this.syllabus = syllabus;
        this.courseName = courseName;
        this.facultyName = facultyName;
    }

    public Long getSubjectId() { return subjectId; }
    public String getName() { return name; }
    public String getSyllabus() { return syllabus; }
    public String getCourseName() { return courseName; }
    public String getFacultyName() { return facultyName; }
}
