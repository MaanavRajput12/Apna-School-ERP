package com.example.collegedb.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.collegedb.Response.AdminResponse;
import com.example.collegedb.Service.AdminService;
import com.example.collegedb.entity.Admin;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admins")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping
    public AdminResponse createAdmin(@RequestBody Admin admin) {
        logger.info("Received request to create admin: {}", admin.getUsername());
        return adminService.createAdmin(admin);
    }

    @GetMapping("/{id}")
    public AdminResponse getAdminById(@PathVariable Long id) {
        logger.info("Fetching admin with ID: {}", id);
        return adminService.getAdminById(id);
    }

    @GetMapping
    public List<AdminResponse> getAllAdmins() {
        logger.info("Fetching all admins");
        return adminService.getAllAdmins();
    }

    @PutMapping("/{id}")
    public AdminResponse updateAdmin(@Valid @PathVariable Long id, @RequestBody Admin adminDetails) {
        logger.info("Updating admin with ID: {}", id);
        return adminService.updateAdmin(id, adminDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteAdmin(@PathVariable Long id) {
        logger.info("Deleting admin with ID: {}", id);
        adminService.deleteAdmin(id);
    }

    @PatchMapping("/{id}")
    public AdminResponse partialUpdateAdmin(@PathVariable Long id, @RequestBody Admin adminDetails) {
        logger.info("Partially updating admin with ID: {}", id);
        return adminService.patchAdmin(id, adminDetails);
    }
}
