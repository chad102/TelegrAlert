package pro.sky.telegrambot.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class NotificationTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "chat_id")
    private long chatId;

    @Column(columnDefinition = "TEXT", nullable = false)
    String notification_text;

    @Column(name = "notification_date_and_time", nullable = false)
    LocalDateTime notification_date_and_time;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getNotification_text() {
        return notification_text;
    }

    public void setNotification_text(String notification_text) {
        this.notification_text = notification_text;
    }

    public LocalDateTime getNotification_date_and_time() {
        return notification_date_and_time;
    }

    public void setNotification_date_and_time(LocalDateTime notification_date_and_time) {
        this.notification_date_and_time = notification_date_and_time;
    }
}
