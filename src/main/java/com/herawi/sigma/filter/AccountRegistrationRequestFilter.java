package com.herawi.sigma.filter;

import com.herawi.sigma.dto.AccountRegistrationRequest;
import org.springframework.core.type.filter.RegexPatternTypeFilter;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AccountRegistrationRequestFilter {
    public static boolean filter(AccountRegistrationRequest accountRegistrationRequest) {
        if (accountRegistrationRequest.getName() == null ||
                accountRegistrationRequest.getLastName() == null ||
                accountRegistrationRequest.getDob() == null ||
                accountRegistrationRequest.getEmail() == null ||
                accountRegistrationRequest.getPassword() == null) {
            return false;
        }
        boolean isNameAndLastNameAreValid = filterNameOrLastName(accountRegistrationRequest.getName())
                && filterNameOrLastName(accountRegistrationRequest.getLastName());


        boolean isEmailValid = filterEmail(accountRegistrationRequest.getEmail());

        boolean isDOBValid = filterDOB(accountRegistrationRequest.getDob());
        boolean isPasswordValid = filterPassword(accountRegistrationRequest.getPassword());

        System.out.println("name lastname"+isNameAndLastNameAreValid);
        System.out.println("dob"+isDOBValid);
        System.out.println("password"+ isPasswordValid);
        System.out.println("email"+isEmailValid);

        return isNameAndLastNameAreValid && isDOBValid && isEmailValid && isPasswordValid;
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
}
