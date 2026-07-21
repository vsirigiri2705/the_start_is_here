package dev.thestartishere.identity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.*;

@Entity @Table(name="app_user")
public class User {
  @Id private UUID id;
  private String email;
  @Column(name="display_name") private String displayName;
  @Column(name="password_hash") private String passwordHash;
  @Enumerated(EnumType.STRING) private UserStatus status;
  @Column(name="created_at") private Instant createdAt;
  @Column(name="updated_at") private Instant updatedAt;
  @Version private long version;
  @ElementCollection(fetch=FetchType.EAGER)
  @CollectionTable(name="user_role", joinColumns=@JoinColumn(name="user_id"))
  @Column(name="role_id")
  private Set<Short> roleIds = new HashSet<>();
  protected User() {}
  public User(UUID id,String email,String displayName,String passwordHash,UserStatus status,Set<Short> roleIds){this.id=id;this.email=email.toLowerCase(Locale.ROOT);this.displayName=displayName;this.passwordHash=passwordHash;this.status=status;this.roleIds=roleIds;this.createdAt=this.updatedAt=Instant.now();}
  public UUID getId(){return id;} public String getEmail(){return email;} public String getDisplayName(){return displayName;} public String getPasswordHash(){return passwordHash;} public UserStatus getStatus(){return status;}
  public Set<String> roleNames(){var names=new TreeSet<String>(); roleIds.forEach(id->names.add(id==2?"ADMIN":"MEMBER")); return names;}
}
