package com.example.collegedb.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    private String name;
    private String email;
    private LocalDate dob;
    private String gender;
    private String rollNo;
    private Long phoneNo;
    private String address;
    private String semester;
    private Boolean active = true;

    @OneToOne
    @JoinColumn(name = "fees_id")
    private Fees fees;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToOne
    @JoinColumn(name="user_id")
    private Users user;

    @OneToMany(mappedBy = "student")
    private List<Attendance> attendanceRecords;


    // Getters and Setters
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public List<Attendance> getAttendanceRecords() { return attendanceRecords; }
    public void setAttendanceRecords(List<Attendance> attendanceRecords) { this.attendanceRecords = attendanceRecords; }

    public String getRollNo() {return rollNo;}
    public void setRollNo(String rollNo) {this.rollNo = rollNo;}

    public Long getPhoneNo() {return phoneNo;}
    public void setPhoneNo(Long phoneNo) {this.phoneNo = phoneNo;}
    
    public String getAddress() {return address;}
    public void setAddress(String address) {this.address = address;}

    public String getSemester() {return semester;}
    public void setSemester(String semester) {this.semester = semester;}

    public Fees getFees() {return fees;}
    public void setFees(Fees fees) {this.fees = fees;}

    public Department getDepartment() {return department;}
    public void setDepartment(Department department) {this.department = department;}

    public Users getUser() {return user;}
    public void setUser(Users user) {this.user = user;}
}
