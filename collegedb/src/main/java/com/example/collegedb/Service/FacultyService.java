package com.example.collegedb.Service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.collegedb.Exception.ConflictException;
import com.example.collegedb.Exception.ResourceNotFoundException;
import com.example.collegedb.Repository.FacultyRepository;
import com.example.collegedb.Repository.UsersRepository;
import com.example.collegedb.Response.FacultyResponse;
import com.example.collegedb.entity.Faculty;
import com.example.collegedb.entity.Users;

@Service
@Transactional
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public FacultyService(
        FacultyRepository facultyRepository,
        UsersRepository usersRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.facultyRepository = facultyRepository;
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<FacultyResponse> getAllFaculty() {
        return facultyRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public FacultyResponse getFacultyById(Long id) {
        return toResponse(getExistingFaculty(id));
    }

    public FacultyResponse createFaculty(Faculty faculty) {
        if (faculty.getLoginPassword() != null && !faculty.getLoginPassword().trim().isEmpty()) {
            faculty.setUser(upsertFacultyUser(faculty.getUser(), faculty.getEmail(), faculty.getLoginPassword()));
        }
        return toResponse(facultyRepository.save(faculty));
    }

    public FacultyResponse updateFaculty(Long id, Faculty entity) {
        Faculty existing = getExistingFaculty(id);
        existing.setFacultyName(entity.getFacultyName());
        existing.setEmail(entity.getEmail());
        existing.setDesignation(entity.getDesignation());
        existing.setDepartment(entity.getDepartment());
        existing.setPhone(entity.getPhone());
        existing.setAddress(entity.getAddress());
        if (entity.getLoginPassword() != null && !entity.getLoginPassword().trim().isEmpty()) {
            existing.setUser(upsertFacultyUser(existing.getUser(), entity.getEmail(), entity.getLoginPassword()));
        } else if (existing.getUser() != null) {
            existing.getUser().setUsername(entity.getEmail().trim());
            existing.getUser().setRole("FACULTY");
        }
        return toResponse(facultyRepository.save(existing));
    }

    public void deleteFaculty(Long id) {
        Faculty faculty = getExistingFaculty(id);
        facultyRepository.delete(faculty);
    }

    public FacultyResponse patchFaculty(Long id, Faculty entity) {
        Faculty existing = getExistingFaculty(id);
        if (entity.getFacultyName() != null) {
            existing.setFacultyName(entity.getFacultyName());
        }
        if (entity.getEmail() != null) {
            existing.setEmail(entity.getEmail());
        }
        if (entity.getDesignation() != null) {
            existing.setDesignation(entity.getDesignation());
        }
        if (entity.getDepartment() != null) {
            existing.setDepartment(entity.getDepartment());
        }
        if (entity.getPhone() != null) {
            existing.setPhone(entity.getPhone());
        }
        if (entity.getAddress() != null) {
            existing.setAddress(entity.getAddress());
        }
        if (entity.getLoginPassword() != null && !entity.getLoginPassword().trim().isEmpty()) {
            existing.setUser(upsertFacultyUser(existing.getUser(), existing.getEmail(), entity.getLoginPassword()));
        }
        return toResponse(facultyRepository.save(existing));
    }

    private Faculty getExistingFaculty(Long id) {
        return facultyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with ID: " + id));
    }

    private Users upsertFacultyUser(Users existingUser, String email, String rawPassword) {
        String username = email.trim();
        usersRepository.findByUsername(username)
            .filter(foundUser -> existingUser == null || !foundUser.getUserId().equals(existingUser.getUserId()))
            .ifPresent(foundUser -> {
                throw new ConflictException("Username already exists: " + username);
            });

        Users user = existingUser != null ? existingUser : new Users();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword.trim()));
        user.setRole("FACULTY");
        return usersRepository.save(user);
    }

    private FacultyResponse toResponse(Faculty faculty) {
        return new FacultyResponse(
            faculty.getFacultyId(),
            faculty.getFacultyName(),
            faculty.getEmail(),
            faculty.getDesignation(),
            faculty.getDepartment(),
            faculty.getPhone(),
            faculty.getAddress(),
            faculty.getUser() != null ? faculty.getUser().getUserId() : null
        );
    }
}
