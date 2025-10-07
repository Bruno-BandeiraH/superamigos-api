package com.superamigos.domain.user;

import com.superamigos.domain.user.dto.UserDetailsData;
import com.superamigos.infra.exceptions.ValidationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
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

    public void delete(Long id) {
        User user = repository.findById(id)
            .orElseThrow(() -> new ValidationException("User with id " + id + " not found"));
        repository.deleteById(id);
    }
}
