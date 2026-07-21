package dev.thestartishere.session;
import java.time.Instant;
import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
public interface RefreshSessionRepository extends JpaRepository<RefreshSession,UUID> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<RefreshSession> findByTokenHash(String hash);
  @Modifying @Query("update RefreshSession s set s.revokedAt=:now, s.revokeReason=:reason where s.familyId=:family and s.revokedAt is null")
  int revokeFamily(@Param("family") UUID family,@Param("now") Instant now,@Param("reason") String reason);
}
