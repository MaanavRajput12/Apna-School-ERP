package com.example.collegedb.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.collegedb.entity.Timetable;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {}
