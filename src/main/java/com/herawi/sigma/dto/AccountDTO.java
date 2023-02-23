package com.herawi.sigma.dto;


import com.herawi.sigma.constants.Gender;

public class AccountDTO {

    private String name;
    private String lastName;
    private String userName;
    private String email;
    private long connections;
    private Gender gender;
    private byte[] profileImage;

    public AccountDTO() {
    }

    public AccountDTO(String name, String lastName,String userName, byte[] profileImage, String email, long connections, Gender gender) {
        this.name = name;
        this.lastName = lastName;
        this.userName = userName;
        this.profileImage = profileImage;
        this.email = email;
        this.connections = connections;
        this.gender = gender;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
