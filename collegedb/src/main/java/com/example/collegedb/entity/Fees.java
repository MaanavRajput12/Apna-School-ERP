package com.example.collegedb.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "fees")

public class Fees {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feesId;

    private Long amount;
    private String feesStatus;
    private LocalDate dueDate;

    @OneToOne(mappedBy = "fees")
    private Student student;

    // Getters and Setters
    public Long getFeesId() { return feesId; }
    public void setFeesId(Long feesId) { this.feesId = feesId;}
    
    public Long getAmount() { return amount; }
    public void setAmount(Long amount) { this.amount = amount; }

    public String getFeesStatus() { return feesStatus; }
    public void setFeesStatus(String feesStatus) { this.feesStatus = feesStatus; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
}
