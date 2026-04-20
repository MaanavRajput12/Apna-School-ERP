package com.example.collegedb.Response;

public class LoginResponse {
    private final Long userId;
    private final String role;
    private final Long studentId;
    private final Long facultyId;
    private final Long adminId;

    public LoginResponse(Long userId, String role, Long studentId, Long facultyId, Long adminId) {
        this.userId = userId;
        this.role = role;
        this.studentId = studentId;
        this.facultyId = facultyId;
        this.adminId = adminId;
    }

    public Long getUserId() { return userId; }
    public String getRole() { return role; }
    public Long getStudentId() { return studentId; }
    public Long getFacultyId() { return facultyId; }
    public Long getAdminId() { return adminId; }
}
