CREATE TABLE user
(
    id                 BIGINT AUTO_INCREMENT,
    creation_date      DATETIME,
    last_modified_date DATETIME     NULL,
    telegram_id        BIGINT,
    telegram_username  VARCHAR(255) NULL,
    chat_id            BIGINT,
    CONSTRAINT pk_user_id PRIMARY KEY (id),
    CONSTRAINT uq_user_telegram_id UNIQUE (telegram_id)
);

CREATE TABLE assignment
(
    id                 BIGINT AUTO_INCREMENT,
    creation_date      DATETIME     NULL,
    last_modified_date DATETIME     NULL,
    user_id            BIGINT       NOT NULL,
    topic              VARCHAR(255) NOT NULL,
    state              VARCHAR(100) NOT NULL,
    CONSTRAINT pk_assignment_id PRIMARY KEY (id),
    CONSTRAINT fk_assignment_user_id FOREIGN KEY (user_id) REFERENCES user (id)
);