package com.example.collegedb.Service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.collegedb.Exception.ConflictException;
import com.example.collegedb.Exception.ResourceNotFoundException;
import com.example.collegedb.Repository.UsersRepository;
import com.example.collegedb.Response.UsersResponse;
import com.example.collegedb.dto.users.UsersCreateRequest;
import com.example.collegedb.dto.users.UsersPatchRequest;
import com.example.collegedb.dto.users.UsersUpdateRequest;
import com.example.collegedb.entity.Users;

@Service
@Transactional
public class UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<UsersResponse> getAllUsers() {
        return usersRepository.findAll().stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public UsersResponse getUserById(Long id) {
        return toResponse(getExistingUser(id));
    }

    public UsersResponse createUser(UsersCreateRequest request) {
        String normalizedUsername = normalizeUsername(request.getUsername());
        validateUniqueUsername(normalizedUsername, null);

        Users user = new Users();
        user.setUsername(normalizedUsername);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        return toResponse(usersRepository.save(user));
    }

    public UsersResponse updateUser(Long id, UsersUpdateRequest request) {
        Users user = getExistingUser(id);
        String normalizedUsername = normalizeUsername(request.getUsername());
        validateUniqueUsername(normalizedUsername, id);

        user.setUsername(normalizedUsername);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        return toResponse(usersRepository.save(user));
    }

    public UsersResponse patchUser(Long id, UsersPatchRequest request) {
        Users user = getExistingUser(id);

        if (request.getUsername() != null) {
            String normalizedUsername = normalizeUsername(request.getUsername());
            validateUniqueUsername(normalizedUsername, id);
            user.setUsername(normalizedUsername);
        }
        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        return toResponse(usersRepository.save(user));
    }

    public void deleteUser(Long id) {
        Users user = getExistingUser(id);
        usersRepository.delete(user);
    }

    private Users getExistingUser(Long id) {
        return usersRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    private void validateUniqueUsername(String username, Long existingUserId) {
        usersRepository.findByUsername(username)
            .filter(existing -> !existing.getUserId().equals(existingUserId))
            .ifPresent(existing -> {
                throw new ConflictException("Username already exists: " + username);
            });
    }

    private String normalizeUsername(String username) {
        String normalizedUsername = username.trim();
        if (normalizedUsername.isEmpty()) {
            throw new IllegalArgumentException("Username must not be blank");
        }
        return normalizedUsername;
    }

    private UsersResponse toResponse(Users user) {
        return new UsersResponse(user.getUserId(), user.getUsername(), user.getRole());
    }
}
