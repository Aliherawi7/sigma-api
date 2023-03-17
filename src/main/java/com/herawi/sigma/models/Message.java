package com.herawi.sigma.models;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Message  implements Comparable<Message>{
    @Id
    private long id;
    private String text;
    private String senderUsername;
    private String receiverUsername;
    private LocalDateTime dateTime;
    private boolean seen;
    private String senderProfileImageUrl;
    private String receiverProfileImageUrl;

    public Message() {
        dateTime = LocalDateTime.now();
    }

    public Message(long id, String text, String senderUsername, String receiverUsername, boolean seen) {
        this.id = id;
        this.text = text;
        setSenderUsername(senderUsername);
        setReceiverUsername(receiverUsername);
        this.dateTime = LocalDateTime.now();
        this.seen = seen;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername.trim().toLowerCase();
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername.trim().toLowerCase();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getSenderProfileImageUrl() {
        return senderProfileImageUrl;
    }

    public void setSenderProfileImageUrl(String senderProfileImageUrl) {
        this.senderProfileImageUrl = senderProfileImageUrl;
    }

    public String getReceiverProfileImageUrl() {
        return receiverProfileImageUrl;
    }

    public void setReceiverProfileImageUrl(String receiverProfileImageUrl) {
        this.receiverProfileImageUrl = receiverProfileImageUrl;
    }

    @Override
    public int compareTo(Message o) {
        return this.getDateTime().compareTo(o.dateTime);
    }
}
