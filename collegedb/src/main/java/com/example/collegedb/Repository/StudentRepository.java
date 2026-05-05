package com.example.collegedb.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.collegedb.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUserUserId(Long userId);
    Optional<Student> findByEmailIgnoreCase(String email);
    List<Student> findByDepartmentDepartmentNameIgnoreCase(String departmentName);
}
