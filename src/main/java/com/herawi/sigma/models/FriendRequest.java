package com.herawi.sigma.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class FriendRequest {

    @Id
    private long id;
    private String requestSenderUserName;
    private String requestReceiverUserName;
    private LocalDateTime requestDate;
    private boolean seen;
    private boolean isAccepted;

    public FriendRequest() {
        this.requestDate = LocalDateTime.now();
        this.seen = false;
        isAccepted = false;
    }

    public FriendRequest(long id, String requestSenderUserName, String requestReceiverUserName, LocalDateTime requestDate, boolean seen, boolean isAccepted) {
        this.id = id;
        this.requestSenderUserName = requestSenderUserName;
        this.requestReceiverUserName = requestReceiverUserName;
        this.requestDate = requestDate;
        this.seen = seen;
        this.isAccepted = isAccepted;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRequestSenderUserName() {
        return requestSenderUserName;
    }

    public void setRequestSenderUserName(String requestSenderUserName) {
        this.requestSenderUserName = requestSenderUserName;
    }

    public String getRequestReceiverUserName() {
        return requestReceiverUserName;
    }

    public void setRequestReceiverUserName(String requestReceiverUserName) {
        this.requestReceiverUserName = requestReceiverUserName;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }
}
