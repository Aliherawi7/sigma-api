package com.herawi.sigma.dto;

public class FriendRequestRegisterationDTO {
    private String requestReceiverUserName;
    private String requestSenderUserName;

    public FriendRequestRegisterationDTO(String requestReceiverUserName, String requestSenderUserName) {
        this.requestReceiverUserName = requestReceiverUserName;
        this.requestSenderUserName = requestSenderUserName;
    }

    public String getRequestReceiverUserName() {
        return requestReceiverUserName;
    }

    public void setRequestReceiverUserName(String requestReceiverUserName) {
        this.requestReceiverUserName = requestReceiverUserName;
    }

    public String getRequestSenderUserName() {
        return requestSenderUserName;
    }

    public void setRequestSenderUserName(String requestSenderUserName) {
        this.requestSenderUserName = requestSenderUserName;
    }
}
