CREATE TABLE task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(80) NOT NULL,
    description VARCHAR(80) NOT NULL,
    status VARCHAR(80) NOT NULL,
    created_on DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deadline DATETIME NOT NULL,
    assigned_to BIGINT NOT NULL,
    FOREIGN KEY (assigned_to) REFERENCES user_system(id)
);