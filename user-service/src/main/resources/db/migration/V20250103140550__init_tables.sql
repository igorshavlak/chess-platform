CREATE TYPE game_mode AS ENUM ('BLITZ', 'CLASSIC', 'BULLET', 'CHESS960');

CREATE TABLE user_statistics
(
    user_id               UUID PRIMARY KEY,
    mode                  game_mode NOT NULL,

    -- Рейтинги для різних типів ігор
    rating                INT       NOT NULL DEFAULT 400,

    -- Загальна статистика ігор
    games_played          INT                DEFAULT 0,
    games_won             INT                DEFAULT 0,
    games_lost            INT                DEFAULT 0,
    games_drawn           INT                DEFAULT 0,

    -- Деталізована статистика результатів
    games_resigned        INT                DEFAULT 0,
    games_timeout         INT                DEFAULT 0,
    games_abandoned       INT                DEFAULT 0,
    draws_by_agreement    INT                DEFAULT 0,
    draws_by_stalemate    INT                DEFAULT 0,
    draws_by_repetition   INT                DEFAULT 0,

    -- Турнірна статистика
    tournaments_played    INT                DEFAULT 0,
    tournaments_won       INT                DEFAULT 0,

    -- Динаміка рейтингу та серії
    win_streak            INT                DEFAULT 0,
    loss_streak           INT                DEFAULT 0,
    highest_rating        INT       NOT NULL DEFAULT 400,
    peak_rating_date      TIMESTAMP,
    rating_volatility     NUMERIC(5, 2)      DEFAULT 0,


    -- Аналітичні дані (за результатами аналізу партій)
    total_brilliant_moves INT                DEFAULT 0,

    -- Історія рейтингу та відкриттів
    rating_history        JSONB,
    opening_win_rate      NUMERIC(5, 2)      DEFAULT 0,

    -- Інформація про останню гру
    last_game_played      TIMESTAMP
);
create table user_profiles
(
    user_id      UUID PRIMARY KEY,
    username     varchar(255),
    name         varchar(255),
    surname      varchar(255),
    bio          text,
    locations    varchar(255),
    social_links jsonb
);
create table user_settings
(
    user_id       UUID PRIMARY KEY,
    preferences   jsonb,
    notifications BOOLEAN NOT NULL DEFAULT TRUE
);

-- Індекси для таблиці user_statistics
-- Індекс для поля mode, щоб швидко відбирати статистику за режимом гри
CREATE INDEX idx_user_statistics_mode ON user_statistics (mode);

-- Індекс для рейтингу (загального рейтингу)
CREATE INDEX idx_user_statistics_rating ON user_statistics (rating);

-- Індекс для дати останньої гри, якщо запити фільтрують за останніми іграми
CREATE INDEX idx_user_statistics_last_game_played ON user_statistics (last_game_played);

-- Індекс для улюбленого дебюту, якщо плануються запити за цим полем
-- CREATE INDEX idx_user_statistics_favorite_opening ON user_statistics (favorite_opening);

-- За потреби можна створити композитний індекс для комбінації полів (наприклад, режим гри та рейтинг)
CREATE INDEX idx_user_statistics_mode_rating ON user_statistics (mode, rating);


-- Індекси для таблиці user_profiles
-- Індекс для поля locations для швидкого пошуку за локацією
CREATE INDEX idx_user_profiles_locations ON user_profiles (locations);

-- GIN-індекс для JSONB-поля social_links, якщо плануються запити по ключам JSON
CREATE INDEX idx_user_profiles_social_links ON user_profiles USING gin (social_links);


-- Індекси для таблиці user_settings
-- GIN-індекс для JSONB-поля preferences, що дозволяє ефективно виконувати запити по внутрішнім ключам
CREATE INDEX idx_user_settings_preferences ON user_settings USING gin (preferences);

-- Індекс для поля notifications, якщо фільтрація за статусом сповіщень є частою
CREATE INDEX idx_user_settings_notifications ON user_settings (notifications);

INSERT INTO user_profiles (user_id,
                           username,
                           name,
                           surname,
                           bio,
                           locations)
VALUES ('a41d2e66-69a2-45ab-bb37-7805e154dfcd',
        'igorshavlak',
        'igor',
        'shavlak',
        'Software developer with 10 years of experience',
        'Kyiv, Ukraine'),
       ('5010d815-62bd-4996-80fa-a028728b03f6',
        'igorshavlak2',
        'igor',
        'shavlak',
        'Front-end engineer specialising in React',
        'Lviv, Ukraine'),
    ('9a0050e9-fc66-4686-b7f0-4f86774fdbdc',
     'igorshavlak3',
     'igor',
     'shavlak',
     'test123'
     'Kyiv','Ukraine'
       ),
    ('2c525b6a-e8f9-40cc-b65f-dd18aadcc8ed',
     'igorshavlak4',
     'igor',
     'shavlak',
     'test1234'
         'Kyiv','Ukraine'
       );
INSERT INTO user_statistics(user_id,
                            mode)
VALUES ('a41d2e66-69a2-45ab-bb37-7805e154dfcd',
        'CLASSIC'),
       ('5010d815-62bd-4996-80fa-a028728b03f6',
        'CLASSIC'),
       ('9a0050e9-fc66-4686-b7f0-4f86774fdbdc',
        'CLASSIC'),
       ('2c525b6a-e8f9-40cc-b65f-dd18aadcc8ed',
        'CLASSIC')