package com.herawi.heraynet.model;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class User {
    @Id
    private long id;
    private String name;
    private String lastName;
    private LocalDate dob;
    private LocalDateTime joinedDate;
    private boolean isActive;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    @Column(unique = true,nullable = false)
    private String phoneNumber;
    private String userName;
    private boolean accountLocked;
    private int failedAttempt;
    private Date lockTime;
    private String location;

    public User(long id) {
        this.id = id;
    }
    public User() {
    }

    public User(long id, String name, String lastName, LocalDate dob, boolean isActive, String email, String password, String phoneNumber, String userName, boolean accountLocked, int failedAttempt, Date lockTime, String location) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.dob = dob;
        this.joinedDate = LocalDateTime.now();
        this.isActive = isActive;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.accountLocked = accountLocked;
        this.failedAttempt = failedAttempt;
        this.lockTime = lockTime;
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toLowerCase().trim();
    }

    public String getLastName() {
        return lastName.toLowerCase().trim();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.toLowerCase().trim();
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public LocalDateTime getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(LocalDateTime joinedDate) {
        this.joinedDate = joinedDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase().trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName.trim().toLowerCase();
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public int getFailedAttempt() {
        return failedAttempt;
    }

    public void setFailedAttempt(int failedAttempt) {
        this.failedAttempt = failedAttempt;
    }

    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location.trim().toLowerCase();
    }

    public int getAge(){
        return LocalDate.now().getYear() - dob.getYear();
    }
}
