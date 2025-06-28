ALTER TABLE user_group DROP FOREIGN KEY fk_usegro_on_group;
ALTER TABLE user_group DROP FOREIGN KEY fk_usegro_on_user;


ALTER TABLE `groups`
    MODIFY id INT AUTO_INCREMENT;

ALTER TABLE users
    MODIFY id INT AUTO_INCREMENT;

ALTER TABLE user_group
    ADD CONSTRAINT fk_usegro_on_group FOREIGN KEY (group_id) REFERENCES `groups` (id);

ALTER TABLE user_group
    ADD CONSTRAINT fk_usegro_on_user FOREIGN KEY (user_id) REFERENCES users (id);