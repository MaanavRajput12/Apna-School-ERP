package com.example.collegedb.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "timetables")
public class Timetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timeTableId;

    private Long semester;
    private String day;
    private String timeSlot;

    @ManyToOne
    @JoinColumn(name= "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    // Getters and Setters
    public Long getTimeTableId() {return timeTableId;}
    public void setTimeTableId(Long timeTableId) {this.timeTableId = timeTableId;}

    public Long getSemester() {return semester;}
    public void setSemester(Long semester) {this.semester = semester;}

    public String getDay() {return day;}
    public void setDay(String day) {this.day = day;}

    public String getTimeSlot() {return timeSlot;}
    public void setTimeSlot(String timeSlot) {this.timeSlot = timeSlot;}

    public Department getDepartment() {return department;}
    public void setDepartment(Department department) {this.department = department;}

    public Faculty getFaculty() {return faculty;}
    public void setFaculty(Faculty faculty) {this.faculty = faculty;}
}
