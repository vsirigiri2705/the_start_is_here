package dev.thestartishere.session;
import dev.thestartishere.api.ApiModels.*;
import dev.thestartishere.identity.*;
import dev.thestartishere.security.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.time.Instant;
import java.util.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SessionService {
  private final UserRepository users; private final RefreshSessionRepository sessions; private final BCryptPasswordEncoder passwords; private final TokenService tokens; private final SecurityProperties p; private final SecureRandom random=new SecureRandom();
  public SessionService(UserRepository u,RefreshSessionRepository s,BCryptPasswordEncoder b,TokenService t,SecurityProperties p){users=u;sessions=s;passwords=b;tokens=t;this.p=p;}
  @Transactional public IssuedSession login(String email,String password){var user=users.findByEmailIgnoreCase(email.trim()).filter(u->u.getStatus()==UserStatus.ACTIVE).filter(u->passwords.matches(password,u.getPasswordHash())).orElseThrow(()->new BadCredentialsException("Invalid credentials"));return create(user,UUID.randomUUID());}
  @Transactional(noRollbackFor=BadCredentialsException.class) public IssuedSession refresh(String raw){var now=Instant.now();var old=sessions.findByTokenHash(hash(raw)).orElseThrow(()->new BadCredentialsException("Invalid session"));if(old.getRotatedAt()!=null){sessions.revokeFamily(old.getFamilyId(),now,"TOKEN_REUSE");throw new BadCredentialsException("Invalid session");}if(!old.activeAt(now)){throw new BadCredentialsException("Invalid session");}var user=users.findById(old.getUserId()).filter(u->u.getStatus()==UserStatus.ACTIVE).orElseThrow(()->new BadCredentialsException("Invalid session"));var next=create(user,old.getFamilyId());old.rotate(next.sessionId(),now);return next;}
  @Transactional public void logout(String raw){if(raw==null||raw.isBlank())return;sessions.findByTokenHash(hash(raw)).ifPresent(s->s.revoke(Instant.now(),"LOGOUT"));}
  private IssuedSession create(User user,UUID family){var bytes=new byte[32];random.nextBytes(bytes);var raw=Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);var now=Instant.now();var entity=new RefreshSession(user.getId(),family,hash(raw),now,now.plus(p.refreshTokenTtl()));sessions.save(entity);var summary=new UserSummary(user.getId(),user.getEmail(),user.getDisplayName(),user.roleNames());return new IssuedSession(new SessionResponse(tokens.issue(user),"Bearer",p.accessTokenTtl().toSeconds(),summary),raw,entity.getId());}
  private String hash(String raw){try{return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(raw.getBytes(StandardCharsets.UTF_8)));}catch(NoSuchAlgorithmException e){throw new IllegalStateException(e);}}
  public record IssuedSession(SessionResponse response,String refreshToken,UUID sessionId){}
}
