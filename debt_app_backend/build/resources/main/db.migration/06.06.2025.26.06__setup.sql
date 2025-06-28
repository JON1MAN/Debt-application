CREATE TABLE `groups`
(
    id         INT          NOT NULL,
    created_at datetime     NULL,
    updated_at datetime     NULL,
    name       VARCHAR(255) NULL,
    CONSTRAINT pk_groups PRIMARY KEY (id)
);

CREATE TABLE user_group
(
    group_id INT NOT NULL,
    user_id  INT NOT NULL
);

CREATE TABLE users
(
    id              INT          NOT NULL,
    created_at      datetime     NULL,
    updated_at      datetime     NULL,
    email           VARCHAR(255) NULL,
    hashed_password VARCHAR(255) NULL,
    first_name      VARCHAR(255) NULL,
    last_name       VARCHAR(255) NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE user_group
    ADD CONSTRAINT fk_usegro_on_group FOREIGN KEY (group_id) REFERENCES `groups` (id);

ALTER TABLE user_group
    ADD CONSTRAINT fk_usegro_on_user FOREIGN KEY (user_id) REFERENCES users (id);