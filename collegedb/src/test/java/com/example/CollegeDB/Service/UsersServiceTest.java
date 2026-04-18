package com.example.CollegeDB.Service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.collegedb.Exception.ConflictException;
import com.example.collegedb.Exception.ResourceNotFoundException;
import com.example.collegedb.Repository.UsersRepository;
import com.example.collegedb.Service.UsersService;
import com.example.collegedb.dto.users.UsersCreateRequest;
import com.example.collegedb.dto.users.UsersPatchRequest;
import com.example.collegedb.entity.Users;

class UsersServiceTest {

    private UsersRepository usersRepository;
    private UsersService usersService;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        usersRepository = org.mockito.Mockito.mock(UsersRepository.class);
        passwordEncoder = new BCryptPasswordEncoder();
        usersService = new UsersService(usersRepository, passwordEncoder);
    }

    @Test
    void createUserHashesPassword() {
        UsersCreateRequest request = new UsersCreateRequest();
        request.setUsername("alice");
        request.setPassword("password123");
        request.setRole("ADMIN");

        when(usersRepository.findByUsername("alice")).thenReturn(Optional.empty());
        when(usersRepository.save(any(Users.class))).thenAnswer(invocation -> {
            Users saved = invocation.getArgument(0);
            saved.setUserId(1L);
            return saved;
        });

        var response = usersService.createUser(request);
        ArgumentCaptor<Users> usersCaptor = ArgumentCaptor.forClass(Users.class);

        assertEquals(1L, response.getUserId());
        assertEquals("alice", response.getUsername());
        verify(usersRepository).save(usersCaptor.capture());
        assertTrue(passwordEncoder.matches("password123", usersCaptor.getValue().getPassword()));
    }

    @Test
    void createUserRejectsDuplicateUsername() {
        UsersCreateRequest request = new UsersCreateRequest();
        request.setUsername("alice");
        request.setPassword("password123");
        request.setRole("ADMIN");

        Users existing = new Users();
        existing.setUserId(10L);
        existing.setUsername("alice");

        when(usersRepository.findByUsername("alice")).thenReturn(Optional.of(existing));

        assertThrows(ConflictException.class, () -> usersService.createUser(request));
        verify(usersRepository, never()).save(any(Users.class));
    }

    @Test
    void patchUserHashesPasswordWhenProvided() {
        Users existing = new Users();
        existing.setUserId(5L);
        existing.setUsername("old-user");
        existing.setPassword("old-hash");
        existing.setRole("STUDENT");

        UsersPatchRequest request = new UsersPatchRequest();
        request.setPassword("new-password");

        when(usersRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(usersRepository.save(any(Users.class))).thenAnswer(invocation -> invocation.getArgument(0));

        usersService.patchUser(5L, request);

        assertTrue(passwordEncoder.matches("new-password", existing.getPassword()));
    }

    @Test
    void deleteUserThrowsWhenMissing() {
        when(usersRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usersService.deleteUser(99L));
    }

    @Test
    void patchUserRejectsBlankUsername() {
        Users existing = new Users();
        existing.setUserId(5L);
        existing.setUsername("old-user");
        existing.setPassword("old-hash");
        existing.setRole("STUDENT");

        UsersPatchRequest request = new UsersPatchRequest();
        request.setUsername("   ");

        when(usersRepository.findById(5L)).thenReturn(Optional.of(existing));

        assertThrows(IllegalArgumentException.class, () -> usersService.patchUser(5L, request));
    }
}
