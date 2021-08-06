package com;

import com.example.myfirstapp.RegisterActivity;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class RegisterActivityTests {
    static RegisterActivity registerActivity;

    @BeforeClass
    public static void setup() {
        registerActivity = new RegisterActivity();
    }
    @Test
    public void checkIfEmailIsEmpty() {
        assertTrue(registerActivity.isEmailForNewUserEmpty(""));
        assertFalse(registerActivity.isEmailForNewUserEmpty("abc@email.com"));
    }
    @Test
    public void checkIfEmailIsValid() {
        assertTrue(registerActivity.isValidEmailAddress("jasper@dal.ca"));
        assertFalse(registerActivity.isValidEmailAddress("abc.email.com"));
    }
    @Test
    public void checkIfFullNameIsEmpty() {
        assertTrue(registerActivity.isFullNameEmpty(""));
        assertFalse(registerActivity.isFullNameEmpty("Jasper123"));
    }
    @Test
    public void checkIfUserNameIsValid(){
        assertTrue(registerActivity.isUserNameValid("jasper234"));
    }

    @Test
    public void checkIfPasswordEmpty(){
        assertTrue(registerActivity.isPasswordForNewUserEmpty(""));
        assertFalse(registerActivity.isPasswordForNewUserEmpty("non-empty"));
    }

    @AfterClass
    public static void tearDown(){
        System.gc();
    }
}
