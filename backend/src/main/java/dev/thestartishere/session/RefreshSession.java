package dev.thestartishere.session;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity @Table(name="refresh_session")
public class RefreshSession {
  @Id private UUID id;
  @Column(name="user_id") private UUID userId;
  @Column(name="family_id") private UUID familyId;
  @Column(name="token_hash") private String tokenHash;
  @Column(name="expires_at") private Instant expiresAt;
  @Column(name="rotated_at") private Instant rotatedAt;
  @Column(name="revoked_at") private Instant revokedAt;
  @Column(name="replaced_by_session_id") private UUID replacedBySessionId;
  @Column(name="revoke_reason") private String revokeReason;
  @Column(name="created_at") private Instant createdAt;
  protected RefreshSession() {}
  public RefreshSession(UUID userId,UUID familyId,String hash,Instant now,Instant expires){id=UUID.randomUUID();this.userId=userId;this.familyId=familyId;tokenHash=hash;createdAt=now;expiresAt=expires;}
  public UUID getId(){return id;} public UUID getUserId(){return userId;} public UUID getFamilyId(){return familyId;} public Instant getExpiresAt(){return expiresAt;} public Instant getRotatedAt(){return rotatedAt;} public Instant getRevokedAt(){return revokedAt;}
  public boolean activeAt(Instant now){return revokedAt==null && rotatedAt==null && expiresAt.isAfter(now);}
  public void rotate(UUID replacement,Instant now){rotatedAt=now;replacedBySessionId=replacement;}
  public void revoke(Instant now,String reason){if(revokedAt==null){revokedAt=now;revokeReason=reason;}}
}
