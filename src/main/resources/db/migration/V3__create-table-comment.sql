CREATE TABLE IF NOT EXISTS t_comment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(255) NOT NULL,
    user_id BINARY(16) NOT NULL,
    post_id INT NOT NULL
);

ALTER TABLE t_comment ADD FOREIGN KEY (user_id) REFERENCES t_user (id);
ALTER TABLE t_comment ADD FOREIGN KEY (post_id) REFERENCES t_post (id);