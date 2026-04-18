package com.example.collegedb.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "parents")
public class Parents {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long parentId;

    private String parentName;
    private String email;
    private String contactNumber;
    private String relationship;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    // Getters and Setters
    public Long getParentId() {return parentId;}
    public void setParentId(Long parentId) {this.parentId = parentId;}

    public String getParentName() {return parentName;}
    public void setParentName(String parentName) {this.parentName = parentName;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getContactNumber() {return contactNumber;}
    public void setContactNumber(String contactNumber) {this.contactNumber = contactNumber;}

    public String getRelationship() {return relationship;}
    public void setRelationship(String relationship) {this.relationship = relationship;}

    public Student getStudent() {return student;}
    public void setStudent(Student student) {this.student = student;}

    public Users getUser() {return user;}
    public void setUser(Users user) {this.user = user;}
}
