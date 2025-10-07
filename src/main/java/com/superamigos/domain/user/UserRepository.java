package com.superamigos.domain.user;

import com.superamigos.domain.user.dto.UserDetailsData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    UserDetailsData findByUsername(String name);
}
