package com.herawi.sigma.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class FriendRequest {

    @Id
    private long id;
    private long requestSenderId;
    private long requestReceiverId;
    private LocalDateTime requestDate;
    private boolean seen;
    private boolean isAccepted;

    public FriendRequest() {
        this.requestDate = LocalDateTime.now();
        this.seen = false;
        isAccepted = false;
    }

    public FriendRequest(long id, long requestSenderId, long requestReceiverId, LocalDateTime requestDate, boolean seen, boolean isAccepted) {
        this.id = id;
        this.requestSenderId = requestSenderId;
        this.requestReceiverId = requestReceiverId;
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

    public long getRequestSenderId() {
        return requestSenderId;
    }

    public void setRequestSenderId(long requestSenderId) {
        this.requestSenderId = requestSenderId;
    }

    public long getRequestReceiverId() {
        return requestReceiverId;
    }

    public void setRequestReceiverId(long requestReceiverId) {
        this.requestReceiverId = requestReceiverId;
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
