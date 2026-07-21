package dev.thestartishere.api;
import static dev.thestartishere.api.ApiModels.*;
import dev.thestartishere.security.SecurityProperties;
import dev.thestartishere.session.SessionService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/v1/auth")
public class AuthController {
  public static final String COOKIE="learning_hub_refresh";
  private final SessionService sessions; private final SecurityProperties security;
  public AuthController(SessionService sessions,SecurityProperties security){this.sessions=sessions;this.security=security;}
  @PostMapping("/login") public ResponseEntity<SessionResponse> login(@Valid @RequestBody LoginRequest request){return response(sessions.login(request.email(),request.password()));}
  @PostMapping("/refresh") public ResponseEntity<SessionResponse> refresh(@CookieValue(name=COOKIE,required=false) String refresh){if(refresh==null)throw new org.springframework.security.authentication.BadCredentialsException("Invalid session");return response(sessions.refresh(refresh));}
  @PostMapping("/logout") public ResponseEntity<Void> logout(@CookieValue(name=COOKIE,required=false) String refresh){sessions.logout(refresh);return ResponseEntity.noContent().header(HttpHeaders.SET_COOKIE,cookie("",true).toString()).build();}
  private ResponseEntity<SessionResponse> response(SessionService.IssuedSession issued){return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie(issued.refreshToken(),false).toString()).body(issued.response());}
  private ResponseCookie cookie(String value,boolean clear){return ResponseCookie.from(COOKIE,value).httpOnly(true).secure(security.secureCookie()).sameSite("Strict").path("/api/v1/auth").maxAge(clear?java.time.Duration.ZERO:security.refreshTokenTtl()).build();}
}
