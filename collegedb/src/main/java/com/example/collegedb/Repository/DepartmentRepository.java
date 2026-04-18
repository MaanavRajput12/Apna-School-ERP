package com.example.collegedb.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.collegedb.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {}