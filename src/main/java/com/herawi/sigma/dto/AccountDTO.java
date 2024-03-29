package com.herawi.sigma.dto;


import com.herawi.sigma.constants.Gender;

public class AccountDTO {

    private String name;
    private String lastName;
    private String userName;
    private String email;
    private long friends;
    private Gender gender;
    private String profilePictureUrl;

    public AccountDTO() {
    }

    public AccountDTO(String name, String lastName, String userName, String profilePictureUrl, String email, long friends, Gender gender) {
        this.name = name;
        this.lastName = lastName;
        this.userName = userName;
        this.profilePictureUrl = profilePictureUrl;
        this.email = email;
        this.friends = friends;
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

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getFriends() {
        return friends;
    }

    public void setFriends(long friends) {
        this.friends = friends;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", friends=" + friends +
                ", gender=" + gender +
                ", profilePictureUrl='" + profilePictureUrl + '\'' +
                '}';
    }
}
