package com.superamigos.domain.user;

import com.superamigos.domain.user.dto.UserCreationData;
import com.superamigos.domain.user.dto.UserDetailsData;
import com.superamigos.infra.exceptions.ValidationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(UserCreationData data) {
        User user = new User(data, passwordEncoder);
        return repository.save(user);
    }

    public UserDetailsData update(UserDetailsData data)  {
        User user = repository.findById(data.id())
            .orElseThrow(() -> new ValidationException("User with id " + data.id() + " not found"));
        user.updateData(data);
        repository.save(user);
        return new UserDetailsData(user);
    }

    public List<UserDetailsData> findAll() {
        return repository.findAll().stream()
            .map(UserDetailsData::new)
            .collect(Collectors.toList());
    }

    public User findById(Long id){
        return repository.findById(id)
            .orElseThrow(() -> new ValidationException("User with id " + id + " not found"));
    }

    public List<UserDetailsData> findByName(String name) {
        return repository.findByName(name).stream()
            .map(UserDetailsData::new)
            .collect(Collectors.toList());
    }

    public UserDetailsData findByUsername(String username) {
        User user = repository.findByUsername(username);
        return new UserDetailsData(user);
    }

    public void delete(Long id) {
        User user = repository.findById(id)
            .orElseThrow(() -> new ValidationException("User with id " + id + " not found"));
        repository.deleteById(id);
    }
}
