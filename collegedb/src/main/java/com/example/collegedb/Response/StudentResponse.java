package com.example.collegedb.Response;

import java.time.LocalDate;

public class StudentResponse {
    private final Long studentId;
    private final String name;
    private final String email;
    private final LocalDate dob;
    private final String gender;
    private final String rollNo;
    private final Long phoneNo;
    private final String address;
    private boolean active;
    @SuppressWarnings("FieldMayBeFinal")
    private String semester;
    private final String departmentName;
    @SuppressWarnings("FieldMayBeFinal")
    private String feesStatus;
    private final String courseName;
    private final String userId;

    public StudentResponse(Long studentId, String name, String email, LocalDate dob, String gender, String rollNo, Long phoneNo, String address, String semester, String departmentName, String feesStatus, String courseName, String userId, boolean active) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
        this.rollNo = rollNo;
        this.phoneNo = phoneNo;
        this.address = address;
        this.semester = semester;
        this.departmentName = departmentName;
        this.feesStatus = feesStatus;
        this.courseName = courseName;
        this.userId = userId;
        this.active = active;
    }

    // Getters
    public Long getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public LocalDate getDob() { return dob; }
    public String getGender() { return gender; }
    public String getRollNo() { return rollNo; }
    public Long getPhoneNo() { return phoneNo; }
    public String getAddress() { return address; }
    public String getSemester() { return semester; }
    public String getDepartmentName() { return departmentName; }
    public String getFeesStatus() { return feesStatus; }
    public String getCourseName() { return courseName; }
    public String getUserId() {return userId;}
    public boolean getActive() { return active; }

}
