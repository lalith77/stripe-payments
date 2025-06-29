package com.demo.store.DTOs;

import com.demo.store.validation.Lowercase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserRegisterRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must have less than 255 characters")
    private String name;
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Lowercase
    private String email;
    @NotBlank(message = "password must be valid")
    @Size(min = 6, max=25, message = "password must be between 6 to 25 characters")
    private String password;
}
