package com.example.collegedb.Repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.collegedb.entity.Faculty;
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Optional<Faculty> findByUserUserId(Long userId);
}
