package com.example.collegedb.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.collegedb.entity.Course;
public interface CourseRepository extends JpaRepository<Course, Long> {}
