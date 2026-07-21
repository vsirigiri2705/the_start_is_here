package dev.thestartishere.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.*;

@Configuration
public class SecurityConfig {
  @Bean BCryptPasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
  @Bean JwtEncoder jwtEncoder(SecurityProperties p){return new NimbusJwtEncoder(new ImmutableSecret<>(new SecretKeySpec(p.jwtSecret().getBytes(StandardCharsets.UTF_8),"HmacSHA256")));}
  @Bean JwtDecoder jwtDecoder(SecurityProperties p){var d=NimbusJwtDecoder.withSecretKey(new SecretKeySpec(p.jwtSecret().getBytes(StandardCharsets.UTF_8),"HmacSHA256")).macAlgorithm(MacAlgorithm.HS256).build();var issuer=JwtValidators.createDefaultWithIssuer(p.issuer());var audience=(OAuth2TokenValidator<Jwt>) jwt->jwt.getAudience().contains(p.audience())?OAuth2TokenValidatorResult.success():OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token","Required audience is missing",null));d.setJwtValidator(new DelegatingOAuth2TokenValidator<>(issuer,audience));return d;}
  @Bean SecurityFilterChain filterChain(HttpSecurity http,ProblemSupport problems) throws Exception {
    var roles=new JwtGrantedAuthoritiesConverter(); roles.setAuthoritiesClaimName("roles"); roles.setAuthorityPrefix("ROLE_"); var converter=new JwtAuthenticationConverter(); converter.setJwtGrantedAuthoritiesConverter(roles);
    http.csrf(c->c.disable()).cors(c->{}).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(a->a.requestMatchers("/api/v1/auth/**","/actuator/health").permitAll().requestMatchers(HttpMethod.OPTIONS,"/**").permitAll().anyRequest().hasAnyRole("MEMBER","ADMIN"))
      .oauth2ResourceServer(o->o.jwt(j->j.jwtAuthenticationConverter(converter)).authenticationEntryPoint(problems));
    return http.build();
  }
  @Bean CorsConfigurationSource cors(SecurityProperties p){var c=new CorsConfiguration();c.setAllowedOrigins(p.allowedOrigins());c.setAllowedMethods(List.of("GET","POST","OPTIONS"));c.setAllowedHeaders(List.of("Authorization","Content-Type"));c.setAllowCredentials(true);var source=new UrlBasedCorsConfigurationSource();source.registerCorsConfiguration("/**",c);return source;}
}
