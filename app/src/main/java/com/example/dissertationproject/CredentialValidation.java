package com.example.dissertationproject;

import java.util.regex.Pattern;

public class CredentialValidation {
    public static final String PASSWORD_REGEX =
            "^(?=.*[0-9])"
                    + "(?=.*[a-z])(?=.*[A-Z])"
                    + "(?=.*[@#$%^&+=])"
                    + "(?=\\S+$).{8,20}$";

    public static final String EMAIL_REGEX =
            "^[\\w!#$%&'*+/=?`{|}~^-]+" +
                    "(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+" +
                    ")*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";

    public boolean isEmailValid(String email){
        Pattern ePattern = Pattern.compile(EMAIL_REGEX);
        return ePattern.matcher(email).matches();
    }

    public boolean isPasswordValid(String password){
        Pattern pPattern = Pattern.compile(PASSWORD_REGEX);
        return pPattern.matcher(password).matches();
    }
}
