package com.herawi.sigma.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Notification {
    @Id
    @SequenceGenerator(name = "notification_sequence", sequenceName = "notification_sequence", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_sequence")
    private long id;
    private String title;
    private String message;
    private String userName;
    private LocalDateTime localDateTime;
    private boolean seen;

    public Notification() {
        this.localDateTime = LocalDateTime.now();
    }

    public Notification(long id, String title, String message, String userName, LocalDateTime localDateTime, boolean isSeen) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.userName = userName;
        this.localDateTime = localDateTime;
        this.seen = isSeen;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        seen = seen;
    }
}
