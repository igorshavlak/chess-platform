
create table user_statistics(
  id uuid primary key,
  rating int not null default 0,
  games_played int default 0,
  games_won int default 0
);
create table user_profiles (
    id UUID primary key,
    user_id uuid unique not null,
    bio text,
    locations varchar(255),
    social_links jsonb
);
create table user_settings (
    id uuid primary key default gen_random_uuid(),
    user_id uuid unique not null,
    preferences jsonb,
    notifications BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_user_profiles_user_id ON user_statistics(id);
CREATE INDEX idx_user_profiles_user_id ON user_statistics(rating);
CREATE INDEX idx_user_profiles_user_id ON user_profiles(user_id);
CREATE INDEX idx_user_settings_user_id ON user_settings(user_id);
