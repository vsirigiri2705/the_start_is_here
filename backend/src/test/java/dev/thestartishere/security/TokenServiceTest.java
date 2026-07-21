package dev.thestartishere.security;
import static org.assertj.core.api.Assertions.*;
import dev.thestartishere.identity.*;
import java.util.*;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;

class TokenServiceTest {
  private static final String SECRET="test-secret-that-is-at-least-thirty-two-bytes-long";
  private final SecurityProperties props=new SecurityProperties("https://test-issuer.local","learning-hub-api",java.time.Duration.ofMinutes(10),java.time.Duration.ofDays(7),false,List.of("http://localhost:5173"),SECRET);
  @Test void issuesContractClaimsAndValidatesIssuerAndAudience(){var config=new SecurityConfig();var user=new User(UUID.randomUUID(),"MEMBER@EXAMPLE.COM","Member","hash",UserStatus.ACTIVE,Set.of((short)1));var raw=new TokenService(config.jwtEncoder(props),props).issue(user);var jwt=config.jwtDecoder(props).decode(raw);assertThat(jwt.getIssuer().toString()).isEqualTo("https://test-issuer.local");assertThat(jwt.getAudience()).containsExactly("learning-hub-api");assertThat(jwt.getClaimAsStringList("roles")).containsExactly("MEMBER");assertThat(jwt.getSubject()).isEqualTo(user.getId().toString());}
  @Test void rejectsIncorrectSignature(){var raw=new TokenService(new SecurityConfig().jwtEncoder(props),props).issue(new User(UUID.randomUUID(),"m@example.com","M","h",UserStatus.ACTIVE,Set.of((short)1)));var other=new SecurityProperties("https://test-issuer.local","learning-hub-api",java.time.Duration.ofMinutes(10),java.time.Duration.ofDays(7),false,List.of(),"different-secret-that-is-at-least-thirty-two-bytes");assertThatThrownBy(()->new SecurityConfig().jwtDecoder(other).decode(raw)).isInstanceOf(JwtException.class);}
}
