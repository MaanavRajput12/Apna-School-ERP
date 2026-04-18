package com.example.collegedb.Response;

public class AdminResponse {
    private Long adminId;
    private String username;
    private String dob;
    private Long userId;

    public AdminResponse(Long adminId, String username, String dob, Long userId) {
        this.adminId = adminId;
        this.username = username;
        this.dob = dob;
        this.userId = userId;
    }

    public Long getAdminId() {return adminId;}
    public void setAdminId(Long adminId) {this.adminId = adminId;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getDob() {return dob;}
    public void setDob(String dob) {this.dob = dob;}

    public Long getUserId() {return userId;}
    public void setUserId(Long userId) {this.userId = userId;}
}
