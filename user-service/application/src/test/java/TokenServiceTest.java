import com.example.common.security.JwtProperties;
import com.application.security.JwtProvider;
import com.example.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {
    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        JwtProperties props = new JwtProperties();
        props.setSecret("my-very-secret-key-for-testing-1234567890");
        props.setAccessTokenExpiration("10m");
        props.setRefreshTokenExpiration("1h");

        jwtProvider = new JwtProvider(props);
    }

    @Test
    void shouldGenerateAndValidateAccessToken() {
        User user = new User();
        user.setEmail("user@example.com");

        String token = jwtProvider.generateAccessToken(user);

        assertThat(token).isNotBlank();
        assertTrue(jwtProvider.validateToken(token));
        assertThat(jwtProvider.getEmailFromToken(token)).isEqualTo("user@example.com");
    }

    @Test
    void shouldGenerateAndValidateRefreshToken() {
        User user = new User();
        user.setEmail("refresh@example.com");

        String token = jwtProvider.generateRefreshToken(user);

        assertThat(token).isNotBlank();
        assertTrue(jwtProvider.validateToken(token));
        assertThat(jwtProvider.getEmailFromToken(token)).isEqualTo("refresh@example.com");
    }

    @Test
    void shouldInvalidateCorruptedToken() {
        String fakeToken = "invalid.token.value";

        assertFalse(jwtProvider.validateToken(fakeToken));
    }

    @Test
    void shouldReturnFalseOnExpiredToken() throws InterruptedException {
        JwtProperties props = new JwtProperties();
        props.setSecret("another-secret-for-test-0123456789");
        props.setAccessTokenExpiration("1");

        JwtProvider shortLived = new JwtProvider(props);
        User user = new User();
        user.setEmail("expired@example.com");

        String token = shortLived.generateAccessToken(user);

        Thread.sleep(5);
        assertFalse(shortLived.validateToken(token));
    }
}
