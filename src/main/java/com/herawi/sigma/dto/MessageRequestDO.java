package com.herawi.sigma.dto;


import java.time.LocalDateTime;

public class MessageRequestDO {
    private String message;
    private String username;
    private LocalDateTime sentDateTime;


    public MessageRequestDO() {
    }

    public MessageRequestDO(String message, String username, LocalDateTime sentDateTime) {
        this.message = message;
        this.username = username;
        this.sentDateTime = sentDateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getSentDateTime() {
        return sentDateTime;
    }

    public void setSentDateTime(LocalDateTime sentDateTime) {
        this.sentDateTime = sentDateTime;
    }
}
