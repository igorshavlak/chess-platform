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

COPY puzzles(PuzzleId, FEN, Moves, Rating, RatingDeviation, Popularity, NbPlays, Themes, GameUrl, OpeningTags)
    FROM 'resources/puzzles/lichess_db_puzzle.csv'
    WITH (FORMAT csv, HEADER true);

