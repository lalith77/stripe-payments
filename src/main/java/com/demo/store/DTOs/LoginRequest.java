package com.demo.store.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @Email
    @NotBlank(message = "email can not be empty")
    private String email;
    @NotBlank(message = "password can not be empty")
    private String password;
}
