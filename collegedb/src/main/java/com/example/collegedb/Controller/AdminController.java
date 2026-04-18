package com.example.collegedb.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.collegedb.Exception.ResourceNotFoundException;
import com.example.collegedb.Repository.AdminRepository;
import com.example.collegedb.Response.AdminResponse;
import com.example.collegedb.entity.Admin;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admins")
public class AdminController {
    @Autowired  
    private AdminRepository adminRepository;


    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @PostMapping
    public AdminResponse createAdmin(@RequestBody Admin admin) {
        logger.info("Received request to create admin: {}", admin.getUsername());
        Admin savedAdmin = adminRepository.save(admin);
        logger.info("Admin created with ID: {}", savedAdmin.getAdminId());
        return new AdminResponse(
            savedAdmin.getAdminId(),
            savedAdmin.getUsername(),
            savedAdmin.getDob(),
            savedAdmin.getUser() != null ? savedAdmin.getUser().getUserId() : null
        );
    }
    
    @GetMapping("/{id}")
    public AdminResponse getAdminById(@PathVariable Long id) {
        logger.info("Fetching admin with ID: {}", id);
        Admin admin = adminRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("EntityName not found with ID: " + id));
        logger.info("Admin with ID: {} fetched successfully", id);
        return new AdminResponse(
            admin.getAdminId(),
            admin.getUsername(),
            admin.getDob(),
            admin.getUser() != null ? admin.getUser().getUserId() : null
        );
    }

    @GetMapping
    public List<AdminResponse> getAllAdmins() {
        logger.info("Fetching all admins");
        List<Admin> admins = adminRepository.findAll();
        logger.info("Total admins fetched: {}", admins.size());
        return admins.stream()
            .map(admin -> new AdminResponse(
                admin.getAdminId(),
                admin.getUsername(),
                admin.getDob(),
                admin.getUser() != null ? admin.getUser().getUserId() : null
            ))
            .collect(Collectors.toList());
    }
    @PutMapping("/{id}")
    public AdminResponse updateAdmin(@Valid @PathVariable Long id, @RequestBody Admin adminDetails) {
        logger.info("Updating admin with ID: {}", id);
        Admin admin = adminRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("EntityName not found with ID: " + id));
        logger.debug("Current admin details: {}", admin);
        admin.setUsername(adminDetails.getUsername());
        admin.setPassword(adminDetails.getPassword());
        admin.setDob(adminDetails.getDob());
        admin.setUser(adminDetails.getUser());
        
        Admin updatedAdmin = adminRepository.save(admin);
        logger.info("Admin with ID: {} updated successfully", id);
        return new AdminResponse(
            updatedAdmin.getAdminId(),
            updatedAdmin.getUsername(),
            updatedAdmin.getDob(),
            updatedAdmin.getUser() != null ? updatedAdmin.getUser().getUserId() : null
        );
    }
    
    @DeleteMapping("/{id}")
    public void deleteAdmin(@PathVariable Long id) {
        logger.info("Deleting admin with ID: {}", id);
        adminRepository.deleteById(id);
        if (!adminRepository.existsById(id)) {
            throw new ResourceNotFoundException("Admin not found with ID: " + id);
        }
        logger.info("Admin with ID: {} deleted successfully", id);
    }

    @PatchMapping("/{id}")
    public AdminResponse partialUpdateAdmin(@PathVariable Long id, @RequestBody Admin adminDetails) {
        logger.info("Partially updating admin with ID: {}", id);
        Admin admin = adminRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("EntityName not found with ID: " + id));
        logger.debug("Current admin details: {}", admin);
        
        if (adminDetails.getUsername() != null) {
            admin.setUsername(adminDetails.getUsername());
        }
        if (adminDetails.getPassword() != null) {
            admin.setPassword(adminDetails.getPassword());
        }
        if (adminDetails.getDob() != null) {
            admin.setDob(adminDetails.getDob());
        }
        if (adminDetails.getUser() != null) {
            admin.setUser(adminDetails.getUser());
        }
        
        Admin updatedAdmin = adminRepository.save(admin);
        logger.info("Admin with ID: {} partially updated successfully", id);
        return new AdminResponse(
            updatedAdmin.getAdminId(),
            updatedAdmin.getUsername(),
            updatedAdmin.getDob(),
            updatedAdmin.getUser() != null ? updatedAdmin.getUser().getUserId() : null
        );
    }
}
