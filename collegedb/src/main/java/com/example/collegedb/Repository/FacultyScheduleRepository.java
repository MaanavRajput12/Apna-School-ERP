package com.example.collegedb.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.collegedb.entity.FacultySchedule;

public interface FacultyScheduleRepository extends JpaRepository<FacultySchedule, Long> {
    
}
