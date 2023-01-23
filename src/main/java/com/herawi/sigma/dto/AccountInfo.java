package com.herawi.sigma.dto;

import org.springframework.web.multipart.MultipartFile;

public class AccountInfo {

    private String name;
    private String lastName;
    private MultipartFile profileImage;
    private String email;
    private long connections;

    public AccountInfo() {
    }

    public AccountInfo(String name, String lastName, MultipartFile profileImage, String email, long connections) {
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

    public MultipartFile getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(MultipartFile profileImage) {
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
