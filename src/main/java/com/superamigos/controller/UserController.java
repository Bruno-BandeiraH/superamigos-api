package com.superamigos.controller;

import com.superamigos.domain.user.User;
import com.superamigos.domain.user.UserService;
import com.superamigos.domain.user.dto.ChangePasswordData;
import com.superamigos.domain.user.dto.UserCreationData;
import com.superamigos.domain.user.dto.UserDetailsData;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearer-key")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    @Transactional
    public ResponseEntity create(@RequestBody @Valid UserCreationData data, UriComponentsBuilder uriBuilder) {
        User user = service.create(data);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(new UserDetailsData(user));
    }

    @PutMapping
    @Transactional
    public ResponseEntity update(@RequestBody @Valid UserDetailsData data) {
        UserDetailsData userData = new UserDetailsData(service.update(data));
        return ResponseEntity.ok(userData);
    }

    @PutMapping("/password/{id}")
    @Transactional
    public ResponseEntity changePassword(@RequestBody @Valid ChangePasswordData data, @PathVariable Long id) {
        service.changePassword(data, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/username/{username}")
    public ResponseEntity findByUsername(@PathVariable String username) {
        UserDetailsData userData = service.findByUsername(username);
        return ResponseEntity.ok(userData);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity findByName(@PathVariable String name) {
        List<UserDetailsData> data = service.findByName(name);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable Long id) {
        User user = service.findById(id);
        return ResponseEntity.ok(new UserDetailsData(user));
    }

    @GetMapping("/all")
    public ResponseEntity findAll() {
        List<UserDetailsData> users = service.findAll();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
