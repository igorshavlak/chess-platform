
CREATE TABLE puzzles
(
    PuzzleId        VARCHAR(10) PRIMARY KEY,
    FEN             TEXT NOT NULL,
    Moves           TEXT NOT NULL,
    Rating          INT,
    RatingDeviation INT,
    Popularity      INT,
    NbPlays         INT,
    Themes          TEXT,
    GameUrl         TEXT,
    OpeningTags     TEXT
);

CREATE INDEX idx_rating ON puzzles (Rating);

CREATE INDEX idx_rating_deviation ON puzzles (RatingDeviation);

CREATE INDEX idx_popularity ON puzzles (Popularity);

CREATE INDEX idx_themes ON puzzles (Themes);

CREATE INDEX idx_opening_tags ON puzzles (OpeningTags);

COPY puzzles (PuzzleId, FEN, Moves, Rating, RatingDeviation, Popularity, NbPlays, Themes, GameUrl, OpeningTags)
    FROM 'resources/puzzles/lichess_db_puzzle.csv'
    WITH (FORMAT csv, HEADER true);


CREATE TABLE user_puzzle_stats
(
    UserId          UUID PRIMARY KEY,
    Rating          INT NOT NULL DEFAULT 1500,
    RatingDeviation INT NOT NULL DEFAULT 350,
    NbPlays         INT NOT NULL DEFAULT 0,
    NbSuccess       INT NOT NULL DEFAULT 0
);
CREATE INDEX idx_user_rating ON user_puzzle_stats (Rating);
CREATE TABLE user_puzzle_history
(
    Id        UUID PRIMARY KEY,
    UserId   UUID        NOT NULL,
    PuzzleId VARCHAR(10) NOT NULL,
    Success  BOOLEAN     NOT NULL,
    Tries    INT         NOT NULL,
    SolvedAt TIMESTAMP   NOT NULL DEFAULT NOW(),
    FOREIGN KEY (PuzzleId) REFERENCES puzzles (PuzzleId)
);
CREATE INDEX idx_history_user ON user_puzzle_history (UserId);
CREATE INDEX idx_history_puzzle ON user_puzzle_history (PuzzleId);

CREATE TABLE user_rating_history
(
    Id        UUID PRIMARY KEY,
    UserId    UUID      NOT NULL,
    Rating    INT       NOT NULL,
    ChangedAt TIMESTAMP NOT NULL DEFAULT NOW()

);
CREATE INDEX idx_rating_history_user ON user_rating_history (UserId);