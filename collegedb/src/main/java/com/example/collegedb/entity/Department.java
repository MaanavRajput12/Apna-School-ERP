package com.example.collegedb.entity;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "department")

public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentId;

    private String departmentName;
    private Long numberOfStudents;
    private Long NumberOfFaculties;

    @OneToMany(mappedBy = "department")
    private List<Course> Course;

    @OneToMany(mappedBy = "department")
    private List<Timetable> Timetable;

    @OneToMany(mappedBy = "department")
    private List<Subject> subjects;

    // Getters and Setters
    public Long getDepartmentId() {return departmentId;}
    public void setDepartmentId(Long departmentId) {this.departmentId = departmentId;}
    public String getDepartmentName() {return departmentName;}
    public void setDepartmentName(String departmentName) {this.departmentName = departmentName;}

    public Long getNumberOfStudents() {return numberOfStudents;}
    public void setNumberOfStudents(Long numberOfStudents) {this.numberOfStudents = numberOfStudents;}
    public Long getNumberOfFaculties() {return NumberOfFaculties;}
    public void setNumberOfFaculties(Long numberOfFaculties) {NumberOfFaculties = numberOfFaculties;}

    public List<Course> getCourses() {return Course;}
    public void setCourses(List<Course> courses) {this.Course = courses;}

    public List<Timetable> getTimetables() {return Timetable;}
    public void setTimetables(List<Timetable> timetables) {this.Timetable = timetables;}

    public List<Subject> getSubjects() { return subjects; }
    public void setSubjects(List<Subject> subjects) { this.subjects = subjects; }

}
