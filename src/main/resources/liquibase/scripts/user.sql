-- liquibase formatted sql

-- changeset adgishev:1
CREATE TABLE notification_task (
    id SERIAL PRIMARY KEY,
    chat_id INT NOT NULL,
    notification_text TEXT NOT NULL,
    notification_date_and_time TIMESTAMP NOT NULL
);