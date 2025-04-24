create table games(
    game_id uuid primary key unique not null,
    player_black_id uuid unique not null,
    player_white_id uuid unique not null,
    status varchar(20) not null check ( status in ('CHECKMATE', 'DRAW', 'SURRENDER') ),
    moves jsonb,
    game_type varchar(50) not null,
    created_at timestamp without time zone default now(),
    updated_at timestamp without time zone
);

CREATE INDEX idx_games_player_one_id ON games(player_black_id);
CREATE INDEX idx_games_player_two_id ON games(player_white_id);
