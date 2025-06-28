package com.demo.store.controller;

import com.demo.store.DTOs.ChangePasswordRequest;
import com.demo.store.DTOs.UserDto;
import com.demo.store.DTOs.UserRegisterRequest;
import com.demo.store.DTOs.UserUpdateRequest;
import com.demo.store.Mappers.UserMapper;
import com.demo.store.entities.User;
import com.demo.store.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDto).toList();
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest,
                                              UriComponentsBuilder uriComponentsBuilder) {
        User user = userMapper.toEntity(userRegisterRequest);
        System.out.println(user);
        userRepository.save(user);

        UserDto userDto = userMapper.toDto(user);
        URI uri = uriComponentsBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id,
                                              @RequestBody UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        userMapper.toUser(userUpdateRequest, user);
        userRepository.save(user);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/change-password/{id}")
    public ResponseEntity<Void> changeUserPassword(@PathVariable Long id,
                                                   @RequestBody ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        if (!user.getPassword().equals(changePasswordRequest.getOldPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        user.setPassword(changePasswordRequest.getNewPassword());
        userRepository.save(user);
        return ResponseEntity.noContent().build();
    }

}

