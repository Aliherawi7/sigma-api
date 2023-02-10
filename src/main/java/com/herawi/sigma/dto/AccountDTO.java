package com.herawi.sigma.dto;


public class AccountDTO {

    private String name;
    private String lastName;
    private String email;
    private long connections;
    private byte[] profileImage;

    public AccountDTO() {
    }

    public AccountDTO(String name, String lastName, byte[] profileImage, String email, long connections) {
        this.name = name;
        this.lastName = lastName;
        this.profileImage = profileImage;
        this.email = email;
        this.connections = connections;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public byte[] getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getConnections() {
        return connections;
    }

    public void setConnections(long connections) {
        this.connections = connections;
    }
}
