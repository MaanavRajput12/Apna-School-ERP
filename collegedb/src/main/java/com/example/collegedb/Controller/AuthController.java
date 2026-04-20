package com.example.collegedb.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.collegedb.Repository.AdminRepository;
import com.example.collegedb.Repository.FacultyRepository;
import com.example.collegedb.Repository.StudentRepository;
import com.example.collegedb.Repository.UsersRepository;
import com.example.collegedb.Response.LoginResponse;
import com.example.collegedb.dto.auth.LoginRequest;
import com.example.collegedb.entity.Admin;
import com.example.collegedb.entity.Faculty;
import com.example.collegedb.entity.Student;
import com.example.collegedb.entity.Users;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UsersRepository usersRepository;
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
        UsersRepository usersRepository,
        StudentRepository studentRepository,
        FacultyRepository facultyRepository,
        AdminRepository adminRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.usersRepository = usersRepository;
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        String username = request.getEmail().trim();
        Users user = usersRepository.findByUsername(username)
            .orElseThrow(UnauthorizedException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException();
        }

        String role = user.getRole() == null ? "" : user.getRole().trim().toUpperCase();
        Student student = studentRepository.findByUserUserId(user.getUserId()).orElse(null);
        Faculty faculty = facultyRepository.findByUserUserId(user.getUserId()).orElse(null);
        Admin admin = adminRepository.findByUserUserId(user.getUserId()).orElse(null);

        return new LoginResponse(
            user.getUserId(),
            role,
            student != null ? student.getStudentId() : null,
            faculty != null ? faculty.getFacultyId() : null,
            admin != null ? admin.getAdminId() : null
        );
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    private static class UnauthorizedException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        UnauthorizedException() {
            super("Invalid credentials");
        }
    }
}
