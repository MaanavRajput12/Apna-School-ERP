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

import com.example.collegedb.Response.UsersResponse;
import com.example.collegedb.Service.UsersService;
import com.example.collegedb.dto.users.UsersCreateRequest;
import com.example.collegedb.dto.users.UsersPatchRequest;
import com.example.collegedb.dto.users.UsersUpdateRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UsersController {
    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public List<UsersResponse> getAllUsers() {
        logger.info("Fetching all users...");
        return usersService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UsersResponse getUserById(@PathVariable Long id) {
        logger.info("Fetching user with ID: {}", id);
        return usersService.getUserById(id);
    }

    @PostMapping
    public UsersResponse createUser(@Valid @RequestBody UsersCreateRequest request) {
        logger.info("Creating new user with username: {}", request.getUsername());
        return usersService.createUser(request);
    }

    @PutMapping("/{id}")
    public UsersResponse updateUser(@PathVariable Long id, @Valid @RequestBody UsersUpdateRequest request) {
        logger.info("Updating user with ID: {}", id);
        return usersService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        logger.info("Deleting user with ID: {}", id);
        usersService.deleteUser(id);
    }

    @PatchMapping("/{id}")
    public UsersResponse patchUser(@PathVariable Long id, @Valid @RequestBody UsersPatchRequest request) {
        logger.info("Patching user with ID: {}", id);
        return usersService.patchUser(id, request);
    }
}
