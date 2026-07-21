package dev.thestartishere.api;
import static dev.thestartishere.api.ApiModels.*;
import java.time.Instant;
import java.util.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/v1")
public class MemberController {
  @GetMapping("/users/me") public UserSummary me(@AuthenticationPrincipal Jwt jwt){return user(jwt);}
  @GetMapping("/dashboard") public DashboardResponse dashboard(@AuthenticationPrincipal Jwt jwt){return new DashboardResponse(user(jwt),List.of(),List.of(new TopicCard("spring-security","Spring Security","Understand authentication and authorization by building it."),new TopicCard("react","React","Build accessible, interactive interfaces."),new TopicCard("aws","AWS","Practice deploying the complete application.")),List.of());}
  private UserSummary user(Jwt jwt){return new UserSummary(UUID.fromString(jwt.getSubject()),jwt.getClaimAsString("email"),jwt.getClaimAsString("displayName"),new TreeSet<>(jwt.getClaimAsStringList("roles")));}
}
