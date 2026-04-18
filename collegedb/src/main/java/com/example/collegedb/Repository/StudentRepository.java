package com.example.collegedb.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.collegedb.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {}
