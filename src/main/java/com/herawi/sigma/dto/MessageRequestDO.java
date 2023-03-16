package com.herawi.sigma.dto;


public class MessageRequestDO {
    private String message;
    private String username;

    public MessageRequestDO() {
    }

    public MessageRequestDO(String message, String username) {
        this.message = message;
        this.username = username;
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
}
