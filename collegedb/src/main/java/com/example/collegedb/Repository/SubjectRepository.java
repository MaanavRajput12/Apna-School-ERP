package com.example.collegedb.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.collegedb.entity.Subject;
public interface SubjectRepository extends JpaRepository<Subject, Long> {}
