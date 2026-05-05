package com.example.collegedb.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.collegedb.Exception.ResourceNotFoundException;
import com.example.collegedb.Repository.AdminRepository;
import com.example.collegedb.Response.AdminResponse;
import com.example.collegedb.entity.Admin;

@Service
@Transactional
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Transactional(readOnly = true)
    public List<AdminResponse> getAllAdmins() {
        return adminRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public AdminResponse getAdminById(Long id) {
        return toResponse(getExistingAdmin(id));
    }

    public AdminResponse createAdmin(Admin admin) {
        return toResponse(adminRepository.save(admin));
    }

    public AdminResponse updateAdmin(Long id, Admin adminDetails) {
        Admin admin = getExistingAdmin(id);
        admin.setUsername(adminDetails.getUsername());
        admin.setPassword(adminDetails.getPassword());
        admin.setDob(adminDetails.getDob());
        admin.setUser(adminDetails.getUser());
        return toResponse(adminRepository.save(admin));
    }

    public void deleteAdmin(Long id) {
        Admin admin = getExistingAdmin(id);
        adminRepository.delete(admin);
    }

    public AdminResponse patchAdmin(Long id, Admin adminDetails) {
        Admin admin = getExistingAdmin(id);
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
        return toResponse(adminRepository.save(admin));
    }

    private Admin getExistingAdmin(Long id) {
        return adminRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Admin not found with ID: " + id));
    }

    private AdminResponse toResponse(Admin admin) {
        return new AdminResponse(
            admin.getAdminId(),
            admin.getUsername(),
            admin.getDob(),
            admin.getUser() != null ? admin.getUser().getUserId() : null
        );
    }
}
