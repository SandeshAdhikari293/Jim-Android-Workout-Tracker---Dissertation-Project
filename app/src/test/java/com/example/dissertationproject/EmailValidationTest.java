package com.example.dissertationproject;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import org.junit.Test;

public class EmailValidationTest {
    /**
     *
     * Test the RegEx to determine whether it correctly identifies correct and incorrect email
     * addresses
     *
     */

    private CredentialValidation validation = new CredentialValidation();

    /**
     * Tests to see if valid email addresses will be accepted
     */

    @Test
    public void testValidCommonEmailAddresses(){
        assertTrue(validation.isEmailValid("test123@test.com"));
        assertTrue(validation.isEmailValid("test123@test.co.uk"));
        assertTrue(validation.isEmailValid("email@example.com"));
        assertTrue(validation.isEmailValid("firstname.lastname@example.com"));
        assertTrue(validation.isEmailValid("email@subdomain.example.com"));
        assertTrue(validation.isEmailValid("firstname+lastname@example.com"));
//        assertTrue(validation.isEmailValid("email@123.123.123.123"));
//        assertTrue(validation.isEmailValid("email@[123.123.123.123]"));
        assertTrue(validation.isEmailValid("1234567890@example.com"));
        assertTrue(validation.isEmailValid("email@example-one.com"));
        assertTrue(validation.isEmailValid("_______@example.com"));
        assertTrue(validation.isEmailValid("email@example.name"));
        assertTrue(validation.isEmailValid("email@example.museum"));
        assertTrue(validation.isEmailValid("email@example.co.jp"));
        assertTrue(validation.isEmailValid("firstname-lastname@example.com"));
    }

    @Test
    public void testValidUnusualEmailAddresses(){
        assertTrue(validation.isEmailValid("much.”more\\ unusual”@example.com"));
        assertTrue(validation.isEmailValid("very.unusual.”@”.unusual.com@example.com"));
        assertTrue(validation.isEmailValid("very.”(),:;<>[]”.VERY.”very@\\\\ \"very”.unusual@strange.example.com"));
    }


    /**
     * Tests to see if invalid email addresses will be rejected
     */
    @Test
    public void testInvalidCommonEmailAddresses(){
        assertFalse(validation.isEmailValid("plainaddress"));
        assertFalse(validation.isEmailValid("#@%^%#$@#$@#.com"));
        assertFalse(validation.isEmailValid("@example.com"));
        assertFalse(validation.isEmailValid("Joe Smith <email@example.com>"));
        assertFalse(validation.isEmailValid("email.example.com"));
        assertFalse(validation.isEmailValid("email@example@example.com"));
        assertFalse(validation.isEmailValid(".email@example.com"));
        assertFalse(validation.isEmailValid("email.@example.com"));
        assertFalse(validation.isEmailValid("email..email@example.com"));
        assertFalse(validation.isEmailValid("あいうえお@example.com"));
        assertFalse(validation.isEmailValid("email@example.com (Joe Smith)"));
        assertFalse(validation.isEmailValid("email@example"));
        assertFalse(validation.isEmailValid("email@-example.com"));
        assertFalse(validation.isEmailValid("email@example.web"));
        assertFalse(validation.isEmailValid("email@111.222.333.44444"));
        assertFalse(validation.isEmailValid("email@example..com"));
        assertFalse(validation.isEmailValid("Abc..123@example.com"));

    }

    /**
     * Tests to see if invalid email addresses will be rejected
     */
    @Test
    public void testInvalidUnusualEmailAddresses(){
        assertFalse(validation.isEmailValid("”(),:;<>[\\]@example.com"));
        assertFalse(validation.isEmailValid("just”not”right@example.com"));
        assertFalse(validation.isEmailValid("this\\ is\"really\"not\\allowed@example.com"));
    }



}
