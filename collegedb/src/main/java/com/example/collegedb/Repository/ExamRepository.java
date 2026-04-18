package com.example.collegedb.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.collegedb.entity.Exam;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    
}
