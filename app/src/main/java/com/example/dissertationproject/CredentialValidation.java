/**
 * @author Sandesh Adhikari
 */
package com.example.dissertationproject;

import java.util.regex.Pattern;

public class CredentialValidation {
    //The regex for password validation
    public static final String PASSWORD_REGEX =
            "^(?=.*[0-9])"
                    + "(?=.*[a-z])(?=.*[A-Z])"
                    + "(?=.*[@#$%^&+=])"
                    + "(?=\\S+$).{8,20}$";

    //The regex for email validation
    public static final String EMAIL_REGEX =
            "^[\\w!#$%&'*+/=?`{|}~^-]+" +
                    "(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+" +
                    ")*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}";

    /**
     * Check whether an email is valid or not according to the regex
     * @param email the email in string form
     * @return      boolean determining if it is valid or not
     */
    public boolean isEmailValid(String email){
        Pattern ePattern = Pattern.compile(EMAIL_REGEX);
        return ePattern.matcher(email).matches();
    }

    /**
     * Check whether an password is valid or not according to the regex
     * @param password  the password in string form
     * @return          boolean determining if it is valid or not
     */
    public boolean isPasswordValid(String password){
        Pattern pPattern = Pattern.compile(PASSWORD_REGEX);
        return pPattern.matcher(password).matches();
    }
}
