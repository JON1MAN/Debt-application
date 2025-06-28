CREATE TABLE debts
(
    id          INT AUTO_INCREMENT NOT NULL,
    created_at  datetime           NULL,
    updated_at  datetime           NULL,
    title       VARCHAR(255)       NULL,
    receiver    VARCHAR(255)       NULL,
    amount      DOUBLE             NOT NULL,
    user_id     INT                NOT NULL,
    receiver_id INT                NOT NULL,
    CONSTRAINT pk_debts PRIMARY KEY (id)
);

ALTER TABLE debts
    ADD CONSTRAINT FK_DEBTS_ON_RECEIVER FOREIGN KEY (receiver_id) REFERENCES users (id);

ALTER TABLE debts
    ADD CONSTRAINT FK_DEBTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);