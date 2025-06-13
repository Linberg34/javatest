import com.application.exceptions.UserNotFoundException;
import com.application.service.implementations.UserServiceImpl;
import com.example.entities.User;
import com.example.common.enums.Role;
import com.example.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void shouldRegisterUserCorrectly() {
        String email = "test@example.com";
        String rawPassword = "password123";
        String encodedPassword = "encoded-password";
        String username = "testuser";

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        User result = userService.register(email,  username, rawPassword);

        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getPassword()).isEqualTo(encodedPassword);
        assertThat(result.getUsername()).isEqualTo(username);
        assertThat(result.getRoles()).containsExactly(Role.CLIENT);
        assertThat(result.isActive()).isTrue();
    }

    @Test
    void shouldReturnUserById() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setEmail("find@test.com");

        when(userRepository.findById(userId)).thenReturn(user);

        User result = userService.getById(userId);

        assertThat(result).isEqualTo(user);
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(null);

        assertThatThrownBy(() -> userService.getById(userId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(userId.toString());
    }

    @Test
    void shouldUpdateUser() {
        UUID userId = UUID.randomUUID();
        User existing = new User();
        existing.setId(userId);
        existing.setEmail("old@test.com");
        existing.setUsername("old");
        existing.setPassword("oldpass");

        when(userRepository.findById(userId)).thenReturn(existing);
        when(passwordEncoder.encode("newpass")).thenReturn("encoded-newpass");
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        User result = userService.update(userId, "new@test.com", "newpass", "newuser");

        assertThat(result.getEmail()).isEqualTo("new@test.com");
        assertThat(result.getUsername()).isEqualTo("newuser");
        assertThat(result.getPassword()).isEqualTo("encoded-newpass");
    }

    @Test
    void shouldSetUserInactiveOnDelete() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setActive(true);

        when(userRepository.findById(userId)).thenReturn(user);
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        userService.delete(userId);

        assertThat(user.isActive()).isFalse();
        verify(userRepository).save(user);
    }

    @Test
    void shouldReturnOnlyActiveUsers() {
        User active = new User();
        active.setActive(true);
        User inactive = new User();
        inactive.setActive(false);

        when(userRepository.findAll()).thenReturn(List.of(active, inactive));

        List<User> users = userService.getAll();

        assertThat(users).containsExactly(active);
    }
}
