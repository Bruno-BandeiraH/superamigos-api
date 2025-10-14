package com.superamigos.domain.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AuthenticationService authenticationService;
    private User validUser;

    @BeforeEach
    void setup() {
        validUser = new User();
        validUser.setId(1L);
        validUser.setUsername("bruno123");
        validUser.setPassword("senha123");
        validUser.setName("Bruno Silva");
    }

    @Test
    @DisplayName("Should load UserDetails when user exists")
    void shouldLoadUserDetailsWhenUserExists() {
        // given
        String username = "bruno123";
        when(userRepository.findByUsername(username)).thenReturn(validUser);

        // when
        UserDetails result = authenticationService.loadUserByUsername(username);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(validUser);
        assertThat(result.getUsername()).isEqualTo("bruno123");
        assertThat(result.getPassword()).isEqualTo("senha123");
        assertThat(result).isSameAs(validUser);
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        // given
        String nonExistentUserName = "abobrinha4";
        when(userRepository.findByUsername(nonExistentUserName)).thenReturn(null);

        // when and then
        assertThatThrownBy(() -> authenticationService.loadUserByUsername(nonExistentUserName))
            .isInstanceOf(UsernameNotFoundException.class)
            .hasMessage("User not found: " + nonExistentUserName);
    }
}