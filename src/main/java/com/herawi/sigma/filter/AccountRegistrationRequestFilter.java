package com.herawi.sigma.filter;

import com.herawi.sigma.constants.Gender;
import com.herawi.sigma.dto.AccountRegistrationRequest;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class AccountRegistrationRequestFilter {

    public static FilterResponse filter(AccountRegistrationRequest accountRegistrationRequest) {
        Map<String, String> failedFields = new HashMap<>();
        if (accountRegistrationRequest.getName() == null ||
                accountRegistrationRequest.getLastName() == null ||
                accountRegistrationRequest.getDob() == null ||
                accountRegistrationRequest.getEmail() == null ||
                accountRegistrationRequest.getPassword() == null) {
            failedFields.put("error_message","the fields should not be empty");
            return new FilterResponse(failedFields, false);
        }
//        boolean isNameAndLastNameAreValid = filterNameOrLastName(accountRegistrationRequest.getName())
//                && filterNameOrLastName(accountRegistrationRequest.getLastName());
        if(!filterNameOrLastName(accountRegistrationRequest.getName())){
            failedFields.put("name","Invalid name, name should have at least 3 characters");
        }
        if(!filterNameOrLastName(accountRegistrationRequest.getLastName())){
            failedFields.put("lastName","Invalid lastName, last name should have at least 3 characters");
        }
        if(!filterGender(accountRegistrationRequest.getGender())){
            failedFields.put("gender","Invalid gender type, Gender should be (male) or (female)");
        }

        if(!filterEmail(accountRegistrationRequest.getEmail())){
            failedFields.put("email","Invalid email");
        }
        if(!filterDOB(accountRegistrationRequest.getDob())){
            failedFields.put("dob", "age should be 18 or higher");
        }
        if(!filterPassword(accountRegistrationRequest.getPassword())){
            failedFields.put("password","invalid password. password should contain numbers," +
                    " letters, and character. and has at least 8 characters");
        }
        if(failedFields.size()>0){
            return new FilterResponse(failedFields, false);
        }
        return new FilterResponse(null, true);
    }
    public static boolean filterPassword(String password){
//        String regex = "^(?=.*[0-9])"
//                       + "(?=.*[a-z])(?=.*[A-Z])"
//                       + "(?=.*[@#$%^&+=])"
//                       + "(?=\\S+$).{8,20}$";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(password);
//        return matcher.matches();
        return password.length() >= 5;
    }
    public static boolean filterNameOrLastName(String name){
        return (name.length() > 2);
    }
    public static boolean filterDOB(LocalDate dob){
        return LocalDate.now().getYear() - dob.getYear() >= 18;
    }
    public static boolean filterEmail(String email){
        String regex = "^[a-zA-Z0-9_!#$%&amp;'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean filterGender(Gender gender){
        if (gender != null){
            return Arrays.stream(Gender.values()).anyMatch(item -> item.name().equals(gender.getValue()));
        }else {
            return false;
        }

    }
}

