package com.example.collegedb.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.collegedb.Exception.UnauthorizedException;
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

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final UsersRepository usersRepository;
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
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

    public LoginResponse login(LoginRequest request) {
        String username = request.getEmail().trim();
        Users user = usersRepository.findByUsername(username)
            .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        String role = user.getRole() == null ? "" : user.getRole().trim().toUpperCase();
        Student student = studentRepository.findByUserUserId(user.getUserId())
            .or(() -> studentRepository.findByEmailIgnoreCase(username))
            .orElse(null);
        Faculty faculty = facultyRepository.findByUserUserId(user.getUserId())
            .or(() -> facultyRepository.findByEmailIgnoreCase(username))
            .orElse(null);
        Admin admin = adminRepository.findByUserUserId(user.getUserId()).orElse(null);

        return new LoginResponse(
            user.getUserId(),
            role,
            student != null ? student.getStudentId() : null,
            faculty != null ? faculty.getFacultyId() : null,
            admin != null ? admin.getAdminId() : null
        );
    }
}
