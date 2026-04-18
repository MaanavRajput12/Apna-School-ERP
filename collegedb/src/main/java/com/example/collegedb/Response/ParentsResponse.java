package com.example.collegedb.Response;

public class ParentsResponse {
    private Long parentId;
    private String parentName;
    private String email;
    private String contactNumber;
    private String relationship;
    private Long studentId;
    private Long userId;

    public ParentsResponse(Long parentId, String parentName, String email, String contactNumber, String relationship, Long studentId, Long userId) {
        this.parentId = parentId;
        this.parentName = parentName;
        this.email = email;
        this.contactNumber = contactNumber;
        this.relationship = relationship;
        this.studentId = studentId;
        this.userId = userId;
    }

    // Getters and Setters
    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
    public String getParentName() { return parentName; }
    public void setParentName(String parentName) { this.parentName = parentName; }  
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }


}
