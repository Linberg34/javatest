import com.application.security.JwtProvider;
import com.application.service.implementations.AuthServiceImpl;
import com.example.entities.User;
import com.example.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("hashed-password");
    }



    @Test
    void shouldThrowIfUserNotFoundOnLogin() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(null);

        assertThrows(RuntimeException.class, () -> authService.login("unknown@example.com", "any-password"));
    }

    @Test
    void shouldThrowIfPasswordDoesNotMatch() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);
        when(passwordEncoder.matches("wrong-password", "hashed-password")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> authService.login("test@example.com", "wrong-password"));
    }



    @Test
    void shouldThrowIfRefreshTokenInvalid() {
        when(jwtProvider.validateToken("invalid-token")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> authService.refreshToken("invalid-token"));
    }


}
