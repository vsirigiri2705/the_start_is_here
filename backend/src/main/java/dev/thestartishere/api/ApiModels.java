package dev.thestartishere.api;
import jakarta.validation.constraints.*;
import java.time.Instant;
import java.util.*;
public final class ApiModels {
  private ApiModels(){}
  public record LoginRequest(@NotBlank @Email @Size(max=254) String email,@NotBlank @Size(max=1024) String password){}
  public record UserSummary(UUID id,String email,String displayName,Set<String> roles){}
  public record SessionResponse(String accessToken,String tokenType,long expiresIn,UserSummary user){}
  public record LearningCard(String topicSlug,String lessonSlug,String title,int progressPercent){}
  public record TopicCard(String slug,String title,String summary){}
  public record ActivityItem(UUID id,String description,Instant occurredAt){}
  public record DashboardResponse(UserSummary user,List<LearningCard> continueLearning,List<TopicCard> featuredTopics,List<ActivityItem> recentActivity){}
}
