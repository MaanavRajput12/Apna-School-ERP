package com.example.collegedb.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.collegedb.entity.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
}
