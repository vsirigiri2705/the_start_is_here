package dev.thestartishere.security;
import java.time.*;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
@ConfigurationProperties("app.security")
public record SecurityProperties(String issuer,String audience,Duration accessTokenTtl,Duration refreshTokenTtl,boolean secureCookie,List<String> allowedOrigins,String jwtSecret) {}
