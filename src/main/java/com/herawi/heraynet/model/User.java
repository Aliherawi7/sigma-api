package com.herawi.heraynet.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
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
    private String email;
    private String password;
    private String phoneNumber;
    private String userName;
    private boolean accountLocked;
    private int failedAttempt;
    private Date lockTime;
    private String location;

}
