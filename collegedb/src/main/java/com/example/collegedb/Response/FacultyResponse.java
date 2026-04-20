package com.example.collegedb.Response;

public class FacultyResponse {
    private final Long facultyId;
    private final String facultyName;
    private final String email;
    private final String designation;
    private final String department;
    private final Long phone;
    private final String address;
    private final Long userId;
    

    public FacultyResponse(Long facultyId, String facultyName, String email, String designation, String department, Long phone, String address, Long userId) {
        this.facultyId = facultyId;
        this.facultyName = facultyName;
        this.email = email;
        this.designation = designation;
        this.department = department;
        this.phone = phone;
        this.address = address;
        this.userId = userId;
    }

    public Long getFacultyId() { return facultyId; }
    public String getFacultyName() { return facultyName; }
    public String getEmail() { return email; }
    public String getDesignation() { return designation; }
    public String getDepartment() { return department; }
    public Long getPhone() { return phone; }
    public String getAddress() { return address; }
    public Long getUserId() { return userId; }
}
