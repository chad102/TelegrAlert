-- liquibase formatted sql

-- changeset chad102:1
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    chat_id INT,
    notification_text TEXT,
    date_and_time TIMESTAMP
)