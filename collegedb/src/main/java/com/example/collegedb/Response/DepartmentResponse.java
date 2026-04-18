package com.example.collegedb.Response;

public class DepartmentResponse {
    private final Long departmentId;
    private final String departmentName;
    @SuppressWarnings("FieldMayBeFinal")
    private Long numberOfStudents;
    @SuppressWarnings("FieldMayBeFinal")
    private Long numberOfFaculties;

    public DepartmentResponse(Long departmentId, String departmentName, Long numberOfStudents, Long numberOfFaculties) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.numberOfStudents = numberOfStudents;
        this.numberOfFaculties = numberOfFaculties;
    }

    // Getters
    public Long getDepartmentId() { return departmentId; }
    public String getDepartmentName() { return departmentName; }
    public Long getNumberOfStudents() { return numberOfStudents; }
    public Long getNumberOfFaculties() { return numberOfFaculties; }

}
