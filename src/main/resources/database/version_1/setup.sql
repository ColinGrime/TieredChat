CREATE TABLE IF NOT EXISTS tieredchat_users (
    uuid CHAR(36) NOT NULL,
    channel_preference VARCHAR(100),
    PRIMARY KEY (uuid)
);

CREATE TABLE IF NOT EXISTS tieredchat_filter (
    uuid CHAR(36) NOT NULL,
    message VARCHAR(100),
    FOREIGN KEY (uuid) REFERENCES tieredchat_users(uuid)
);