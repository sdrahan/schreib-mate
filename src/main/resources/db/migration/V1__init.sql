CREATE TABLE assignment_topic
(
    id                 BIGINT AUTO_INCREMENT,
    creation_date      DATETIME     NULL,
    last_modified_date DATETIME     NULL,
    topic_de           VARCHAR(255) NOT NULL,
    topic_ru           VARCHAR(255) NOT NULL,
    topic_en           VARCHAR(255) NOT NULL,
    description_de     TEXT         NOT NULL,
    description_ru     TEXT         NOT NULL,
    description_en     TEXT         NOT NULL,
    keywords_de        TEXT         NOT NULL,
    keywords_ru        TEXT         NOT NULL,
    keywords_en        TEXT         NOT NULL,
    active             BOOLEAN DEFAULT TRUE,
    CONSTRAINT pk_assignment_topic_id PRIMARY KEY (id)
);

CREATE TABLE user
(
    id                 BIGINT AUTO_INCREMENT,
    creation_date      DATETIME,
    last_modified_date DATETIME     NULL,
    telegram_id        BIGINT,
    telegram_username  VARCHAR(255) NULL,
    chat_id            BIGINT,
    language           VARCHAR(3) NOT NULL DEFAULT 'DE',
    CONSTRAINT pk_user_id PRIMARY KEY (id),
    CONSTRAINT uq_user_telegram_id UNIQUE (telegram_id)
);

CREATE TABLE assignment
(
    id                  BIGINT AUTO_INCREMENT,
    creation_date       DATETIME     NULL,
    last_modified_date  DATETIME     NULL,
    user_id             BIGINT       NOT NULL,
    topic_id            BIGINT       NOT NULL,
    state               VARCHAR(100) NOT NULL,
    telegram_message_id INTEGER,
    CONSTRAINT pk_assignment_id PRIMARY KEY (id),
    CONSTRAINT fk_assignment_user_id FOREIGN KEY (user_id) REFERENCES user (id),
    CONSTRAINT fk_assignment_assignment_topic_id FOREIGN KEY (topic_id) REFERENCES assignment_topic (id)
);