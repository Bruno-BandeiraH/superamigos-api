package com.superamigos.domain.user;

import com.superamigos.domain.user.dto.UserDetailsData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetailsData findByUsername(String name);

    Optional<List<UserDetailsData>> findByName(String name);
}
