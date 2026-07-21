package dev.thestartishere.session;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import dev.thestartishere.identity.*;
import dev.thestartishere.security.*;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class SessionServiceTest {
  UserRepository users=mock(UserRepository.class); RefreshSessionRepository sessions=mock(RefreshSessionRepository.class); TokenService tokens=mock(TokenService.class); BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(4);
  SecurityProperties props=new SecurityProperties("https://issuer.local","learning-hub-api",Duration.ofMinutes(10),Duration.ofDays(7),false,List.of(),"test-secret-that-is-at-least-thirty-two-bytes-long");
  SessionService service; User active;
  @BeforeEach void setup(){service=new SessionService(users,sessions,encoder,tokens,props);active=new User(UUID.randomUUID(),"member@example.com","Member",encoder.encode("correct"),UserStatus.ACTIVE,Set.of((short)1));when(tokens.issue(any())).thenReturn("access");when(sessions.save(any())).thenAnswer(i->i.getArgument(0));}
  @Test void validLoginReturnsAccessAndPersistsOnlyHash(){when(users.findByEmailIgnoreCase("MEMBER@example.com")).thenReturn(Optional.of(active));var issued=service.login("MEMBER@example.com","correct");assertThat(issued.response().accessToken()).isEqualTo("access");assertThat(issued.refreshToken()).doesNotContain("=");var captor=ArgumentCaptor.forClass(RefreshSession.class);verify(sessions).save(captor.capture());assertThat(captor.getValue().activeAt(Instant.now())).isTrue();}
  @Test void invalidAndDisabledAccountsUseSameFailure(){when(users.findByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());assertThatThrownBy(()->service.login("none@example.com","bad")).isInstanceOf(BadCredentialsException.class);var disabled=new User(UUID.randomUUID(),"off@example.com","Off",encoder.encode("correct"),UserStatus.DISABLED,Set.of((short)1));when(users.findByEmailIgnoreCase("off@example.com")).thenReturn(Optional.of(disabled));assertThatThrownBy(()->service.login("off@example.com","correct")).isInstanceOf(BadCredentialsException.class);}
  @Test void replayRevokesWholeFamily(){var family=UUID.randomUUID();var old=new RefreshSession(active.getId(),family,"unused",Instant.now().minusSeconds(60),Instant.now().plusSeconds(60));old.rotate(UUID.randomUUID(),Instant.now());when(sessions.findByTokenHash(anyString())).thenReturn(Optional.of(old));assertThatThrownBy(()->service.refresh("replayed")).isInstanceOf(BadCredentialsException.class);verify(sessions).revokeFamily(eq(family),any(),eq("TOKEN_REUSE"));}
  @Test void logoutIsIdempotent(){service.logout(null);verifyNoInteractions(sessions);when(sessions.findByTokenHash(anyString())).thenReturn(Optional.empty());service.logout("unknown");verify(sessions).findByTokenHash(anyString());}
}
