package com.herawi.sigma.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Notification {
    @Id
    private long id;
    private String title;
    private String message;
    private long userId;
    private LocalDateTime localDateTime;
    private boolean isSeen;

    public Notification() {
        this.localDateTime = LocalDateTime.now();
    }

    public Notification(long id, String title, String message, long userId, LocalDateTime localDateTime, boolean isSeen) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.userId = userId;
        this.localDateTime = localDateTime;
        this.isSeen = isSeen;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }
}
