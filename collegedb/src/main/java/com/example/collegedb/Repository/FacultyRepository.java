package com.example.collegedb.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.collegedb.entity.Faculty;
public interface FacultyRepository extends JpaRepository<Faculty, Long> {}
