package com.example.collegedb.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.collegedb.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    
}
