package com.superamigos.domain.user;

import com.superamigos.domain.user.dto.ChangePasswordData;
import com.superamigos.domain.user.dto.UserCreationData;
import com.superamigos.domain.user.dto.UserDetailsData;
import com.superamigos.infra.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    private User existingUser;

    @BeforeEach
    void setUp() {
        existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("Bruno Bandeira");
        existingUser.setUsername("bruno.bandeira");
        existingUser.setPassword("senha123");
        existingUser.setStatusPhrase("Hello World!");
    }

    @Test
    void shouldCreateUserWithEncryptedPassword() {
        // given
        UserCreationData data = new UserCreationData("Bruno", "bruninho", "123");

        // when
        when(passwordEncoder.encode("123")).thenReturn("encoded123");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.create(data);

        // then
        assertThat(result.getPassword()).isEqualTo("encoded123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should update only not null fields")
    void shouldUpdateOnlyNonNullFields() {
        // given
        UserDetailsData data = new UserDetailsData(1L, null, "Cool.username", null);
        when(userRepository.findById(data.id())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        // when
        UserDetailsData result = userService.update(data);

        // then
        assertThat(result.username()).isEqualTo("Cool.username");
        assertThat(result.statusPhrase()).isEqualTo("Hello World!");
        assertThat(result.name()).isEqualTo("Bruno Bandeira"); // Manteve o original

        verify(userRepository).save(existingUser);
    }

    @Test
    @DisplayName("Deve alterar senha quando senha atual está correta")
    void shouldChangePasswordWhenCurrentPasswordIsCorrect() {
        // given
        Long userId = 1L;
        ChangePasswordData data = new ChangePasswordData("senha", "soSenha");
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("senha", "senha123")).thenReturn(true);
        when(passwordEncoder.encode("soSenha")).thenReturn("encodedsoSenha");

        // when
        userService.changePassword(data, userId);

        // then
        assertThat(existingUser.getPassword()).isEqualTo("encodedsoSenha");
        verify(userRepository).save(existingUser);
    }

    @Test
    @DisplayName("Should throw exception when current password is incorrect")
    void shouldThrowExceptionWhenCurrentPasswordIsIncorrect() {
        // Given
        Long userId = 1L;
        ChangePasswordData data = new ChangePasswordData("wrongPassword", "newPassword");
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("wrongPassword", "senha123")).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> userService.changePassword(data, userId))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("Wrong password!");

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(999L))
            .isInstanceOf(ValidationException.class);
    }



}