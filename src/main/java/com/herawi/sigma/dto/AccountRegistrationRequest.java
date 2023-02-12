package com.herawi.sigma.dto;


import com.herawi.sigma.constants.Gender;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

public class AccountRegistrationRequest {
    private String name;
    private String lastName;
    private String email;
    private String password;
    private LocalDate dob;
    private Gender gender ;
    private MultipartFile img;

    public AccountRegistrationRequest() {
    }

    public AccountRegistrationRequest(String name, String lastName, String email, String password, String dob,Gender gender, MultipartFile img) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dob = LocalDate.parse(dob);
        this.gender = gender;
        this.img = img;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toLowerCase().trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.toLowerCase().trim();
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

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = LocalDate.parse(dob);
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public MultipartFile getImg() {
        return img;
    }

    public void setImg(MultipartFile img) {
        this.img = img;
    }
}
