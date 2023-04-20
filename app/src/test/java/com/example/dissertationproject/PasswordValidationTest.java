package com.example.dissertationproject;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import org.junit.Test;

public class PasswordValidationTest {

    /**
     *
     * Test cases to determine if the password validation RegEx is accepts valid passwords and
     * rejects invalid passwords
     *
     */
    private CredentialValidation validation = new CredentialValidation();


    /**
     * Checking if the RegEx accepts valid passwords
     */
    @Test
    public void testValidPasswords(){
        assertTrue(validation.isPasswordValid("PASSword@123"));
        assertTrue(validation.isPasswordValid("Password$123"));
        assertTrue(validation.isPasswordValid("A!@#&()–a1"));
        assertTrue(validation.isPasswordValid("A~$^+=<>a1"));
        assertTrue(validation.isPasswordValid("0123456789$abcdefgAB"));
        assertTrue(validation.isPasswordValid("123Aa$Aa"));
    }

    /**
     * Checking if the RegEx rejects invalid passwords
     */
    @Test
    public void testInvalidPasswords(){
        assertFalse(validation.isPasswordValid("pass"));
        assertFalse(validation.isPasswordValid("password"));
        assertFalse(validation.isPasswordValid("PASSWORD"));
        assertFalse(validation.isPasswordValid("12345678"));
        assertFalse(validation.isPasswordValid("password$%"));
        assertFalse(validation.isPasswordValid("password $%"));
        assertFalse(validation.isPasswordValid("pasSword1 $%"));
        assertFalse(validation.isPasswordValid("pasSword$%"));
        assertFalse(validation.isPasswordValid("pasSword123"));
        assertFalse(validation.isPasswordValid("PASSWORD123"));
        assertFalse(validation.isPasswordValid("PASSW %$ORD123"));
        assertFalse(validation.isPasswordValid("pasSword1$%abcdefghijklmnopqrstuv"));
        assertFalse(validation.isEmailValid(""));
    }
}
