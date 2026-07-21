package dev.thestartishere.security;
import dev.thestartishere.identity.User;
import java.time.*;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
@Service
public class TokenService {
  private final JwtEncoder encoder; private final SecurityProperties p; private final Clock clock;
  @Autowired
  public TokenService(JwtEncoder encoder,SecurityProperties p){this(encoder,p,Clock.systemUTC());} TokenService(JwtEncoder e,SecurityProperties p,Clock c){encoder=e;this.p=p;clock=c;}
  public String issue(User user){var now=clock.instant();var claims=JwtClaimsSet.builder().issuer(p.issuer()).audience(java.util.List.of(p.audience())).subject(user.getId().toString()).issuedAt(now).expiresAt(now.plus(p.accessTokenTtl())).id(UUID.randomUUID().toString()).claim("roles",user.roleNames()).claim("email",user.getEmail()).claim("displayName",user.getDisplayName()).build();return encoder.encode(JwtEncoderParameters.from(JwsHeader.with(org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS256).build(),claims)).getTokenValue();}
}
