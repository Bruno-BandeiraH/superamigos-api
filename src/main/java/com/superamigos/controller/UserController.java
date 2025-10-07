package com.superamigos.controller;

import com.superamigos.domain.user.User;
import com.superamigos.domain.user.UserRepository;
import com.superamigos.domain.user.UserService;
import com.superamigos.domain.user.dto.UserCreationData;
import com.superamigos.domain.user.dto.UserDetailsData;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository repository;
    private final UserService service;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository repository, PasswordEncoder passwordEncoder, UserService service) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.service = service;
    }

    @PostMapping
    @Transactional
    public ResponseEntity create(@RequestBody @Valid UserCreationData data, UriComponentsBuilder uriBuilder) {
        User user = new User(data, passwordEncoder);
        repository.save(user);
        var uri = uriBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(new UserDetailsData(user));
    }

    @PutMapping
    @Transactional
    public ResponseEntity update(@RequestBody @Valid UserDetailsData data) {
        UserDetailsData userData = new UserDetailsData(service.update(data));
        return ResponseEntity.ok(userData);
    }

    @GetMapping("/{username}")
    public ResponseEntity findByName(@PathVariable String username) {
        UserDetailsData userData = repository.findByUsername(username);
    }
}
