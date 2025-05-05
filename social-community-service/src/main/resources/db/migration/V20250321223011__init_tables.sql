CREATE TABLE forum_categories
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE forum_threads
(
    id          UUID PRIMARY KEY,
    category_id UUID      NOT NULL,
    title       VARCHAR(255) NOT NULL,
    user_id     UUID         NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES forum_categories (id)
);
CREATE INDEX idx_forum_threads_category ON forum_threads(category_id);
CREATE INDEX idx_forum_threads_user     ON forum_threads(user_id);

CREATE TABLE forum_posts
(
    id             UUID PRIMARY KEY,
    thread_id      UUID NOT NULL,
    user_id        UUID NOT NULL,
    parent_post_id UUID,
    content        TEXT    NOT NULL,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_edited      BOOLEAN   DEFAULT FALSE,
    edited_by      INTEGER,
    likes_count    INTEGER   DEFAULT 0,
    attachment_url TEXT,
    ip_address     INET,
    CONSTRAINT fk_thread FOREIGN KEY (thread_id) REFERENCES forum_threads (id)
);
CREATE INDEX idx_forum_posts_thread ON forum_posts(thread_id);
CREATE INDEX idx_forum_posts_user   ON forum_posts(user_id);

CREATE TABLE blog_posts
(
    id           UUID PRIMARY KEY,
    user_id      UUID                NOT NULL,
    category_id  UUID,
    title        VARCHAR(255)        NOT NULL,
    slug         VARCHAR(255) UNIQUE NOT NULL,
    summary      TEXT,
    content      TEXT                NOT NULL,
    status       VARCHAR(20)         NOT NULL DEFAULT 'draft',
    views_count  INTEGER                      DEFAULT 0,
    likes_count  INTEGER                      DEFAULT 0,
    published_at TIMESTAMP,
    created_at   TIMESTAMP                    DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP                    DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_blog_posts_user     ON blog_posts(user_id);
CREATE INDEX idx_blog_posts_category ON blog_posts(category_id);
CREATE INDEX idx_blog_posts_status   ON blog_posts(status);


CREATE TABLE blog_comments
(
    id           UUID PRIMARY KEY,
    blog_post_id UUID NOT NULL,
    user_id      UUID    NOT NULL,
    content      TEXT    NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_blog_post FOREIGN KEY (blog_post_id) REFERENCES blog_posts (id)

);
CREATE INDEX idx_blog_comments_post ON blog_comments(blog_post_id);
CREATE INDEX idx_blog_comments_user ON blog_comments(user_id);

CREATE TABLE friend_requests
(
    id            UUID PRIMARY KEY,
    requester_id  UUID      NOT NULL,
    target_id     UUID      NOT NULL,
    status        VARCHAR(20) NOT NULL DEFAULT 'PENDING',  -- PENDING, ACCEPTED, REJECTED
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    responded_at  TIMESTAMP
);
CREATE INDEX idx_friend_requests_requester ON friend_requests(requester_id);
CREATE INDEX idx_friend_requests_target    ON friend_requests(target_id);

CREATE TABLE friendships
(
    id          UUID    PRIMARY KEY,
    user_id     UUID    NOT NULL,
    friend_id   UUID    NOT NULL,
    since       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uc_friendship UNIQUE (user_id, friend_id)
);
CREATE INDEX idx_friendships_user   ON friendships(user_id);
CREATE INDEX idx_friendships_friend ON friendships(friend_id);

CREATE TABLE chat_rooms
(
    id           UUID      PRIMARY KEY,
    name         VARCHAR(255),
    type         VARCHAR(20) NOT NULL,  -- DIRECT, GROUP
    created_by   UUID      NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_chat_rooms_creator  ON chat_rooms(created_by);

CREATE TABLE chat_participants
(
    room_id  UUID NOT NULL,
    user_id  UUID NOT NULL,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_chat_participants PRIMARY KEY (room_id, user_id),
    CONSTRAINT fk_cp_room FOREIGN KEY (room_id) REFERENCES chat_rooms(id)
);
CREATE INDEX idx_chat_participants_user ON chat_participants(user_id);

CREATE TABLE chat_messages
(
    id         UUID      PRIMARY KEY,
    room_id    UUID      NOT NULL,
    sender_id  UUID      NOT NULL,
    content    TEXT      NOT NULL,
    sent_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status     VARCHAR(20) NOT NULL DEFAULT 'SENT',  -- SENT, DELIVERED, READ
    CONSTRAINT fk_cm_room FOREIGN KEY (room_id) REFERENCES chat_rooms(id)
);
CREATE INDEX idx_chat_messages_room   ON chat_messages(room_id, sent_at);
CREATE INDEX idx_chat_messages_sender ON chat_messages(sender_id);