package com.herawi.sigma.models;


import com.herawi.sigma.constants.Gender;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "user_email_unique", columnNames = "email"),
                @UniqueConstraint(name = "user_phoneNumber_unique", columnNames = "phoneNumber")
        }
)
public class Account {
    @Id
    @SequenceGenerator(sequenceName = "account_sequence", name = "account_sequence", initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_sequence")
    private long id;
    private String name;
    private String lastName;
    private LocalDate dob;
    private LocalDateTime joinedDate;
    private boolean isActive;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    @Column(unique = true)
    private String phoneNumber;
    private String userName;
    private boolean accountLocked;
    private int failedAttempt;
    private Date lockTime;
    private String location;
    @ManyToMany(fetch =FetchType.EAGER)
    private Set<Account> friends = new LinkedHashSet<>();

    @Enumerated(EnumType.STRING)
    private Gender gender;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();


    public Account() {
        this.joinedDate = LocalDateTime.now();
    }

    public Account(long id, String name, String lastName,
                   LocalDate dob, boolean isActive, String email,
                   String password, String phoneNumber, String userName,
                   boolean accountLocked, int failedAttempt, Date lockTime,
                   String location, Gender gender) {
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
        this.gender = gender;
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

    public Collection<Account> getFriends() {
        return friends.stream()
                .sorted(Comparator.comparing(Account::getUserName))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public void setFriends(Set<Account> friends) {
        this.friends = friends;
    }

    public void addAccountToFriends(Account account){
        friends.add(account);
    }

    public boolean removeAccountFromFriends(Account p){
        return friends.remove(p);
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}
