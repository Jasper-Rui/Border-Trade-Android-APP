package com;

import com.example.myfirstapp.LoginActivity;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginActivityTests {

    static LoginActivity loginActivity;

    @BeforeClass
    public static void setup() {
        loginActivity = new LoginActivity();
    }

    @Test
    public void checkIfEmailIsEmpty() {
        assertTrue(loginActivity.isEmpty(""));
        assertFalse(loginActivity.isEmpty("abc@email.com"));
    }

    @Test
    public void checkIfPasswordIsEmpty() {
        assertTrue(loginActivity.isEmpty(""));
        assertFalse(loginActivity.isEmpty("password"));
    }

    @Test
    public void checkIfEmailIsValid() {
        assertTrue(loginActivity.isValidEmailAddress("jasper@dal.ca"));
        assertFalse(loginActivity.isValidEmailAddress("abc.email.com"));
    }

    @AfterClass
    public static void tearDown(){
        System.gc();
    }
}