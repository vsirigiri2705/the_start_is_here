package dev.thestartishere;

import dev.thestartishere.security.SecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(SecurityProperties.class)
public class LearningHubApplication {
  public static void main(String[] args) { SpringApplication.run(LearningHubApplication.class, args); }
}
