CREATE TABLE app_user (
  id uuid PRIMARY KEY,
  email varchar(254) NOT NULL,
  display_name varchar(120) NOT NULL,
  password_hash varchar(100) NOT NULL,
  status varchar(16) NOT NULL CHECK (status IN ('INVITED','ACTIVE','LOCKED','DISABLED')),
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  version bigint NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX uk_app_user_email_normalized ON app_user (lower(email));
CREATE TABLE role (id smallint PRIMARY KEY, name varchar(16) NOT NULL UNIQUE CHECK (name IN ('MEMBER','ADMIN')));
INSERT INTO role(id,name) VALUES (1,'MEMBER'),(2,'ADMIN');
CREATE TABLE user_role (
  user_id uuid NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  role_id smallint NOT NULL REFERENCES role(id),
  PRIMARY KEY(user_id, role_id)
);
CREATE TABLE refresh_session (
  id uuid PRIMARY KEY,
  user_id uuid NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  family_id uuid NOT NULL,
  token_hash varchar(64) NOT NULL UNIQUE,
  expires_at timestamptz NOT NULL,
  rotated_at timestamptz,
  revoked_at timestamptz,
  replaced_by_session_id uuid REFERENCES refresh_session(id),
  revoke_reason varchar(64),
  created_at timestamptz NOT NULL,
  CONSTRAINT refresh_expiry_after_creation CHECK (expires_at > created_at)
);
CREATE INDEX ix_refresh_session_family ON refresh_session(family_id);
