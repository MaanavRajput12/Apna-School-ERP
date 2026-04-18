package com.example.collegedb.dto.users;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UsersPatchRequest {

    @Size(max = 50, message = "Username must be at most 50 characters")
    private String username;

    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    private String password;

    @Pattern(
        regexp = "ADMIN|STUDENT|FACULTY|PARENT",
        message = "Role must be one of ADMIN, STUDENT, FACULTY, or PARENT"
    )
    private String role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
