package com;

import com.example.myfirstapp.User;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;


public class UserTests {

    //    test User methods
    @Test
    public void setEmail() {
        User basicUser = new User();
        basicUser.setEmail("new.email@email.com");
        assertEquals("setEmail() did not set the expected value", "new.email@email.com", basicUser.getEmail());
    }

    @Test
    public void setFullName() {
        User basicUser = new User();
        basicUser.setFullName("Jane Does");
        assertEquals("setUsername() did not set the expected value", "Jane Does", basicUser.getFullName());
    }

    @Test
    public void setPassword() {
        User basicUser = new User();
        basicUser.setPassword("password123");
        assertEquals("setPassword() did not set the expected value", "password123", basicUser.getPassword());
    }

    @Test
    public void getEmail() {
        User basicUser = new User();
        basicUser.setEmail("abc@email.com");
        assertEquals("getEmail() did not return the expected value","abc@email.com", basicUser.getEmail());
    }

    @Test
    public void getFullName() {
        User basicUser = new User();
        basicUser.setFullName("John Doe");
        assertEquals("getFullName() did not return the expected value", "John Doe", basicUser.getFullName());
    }

    @Test
    public void getPassword() {
        User basicUser = new User();
        basicUser.setPassword("password");
        assertEquals("getPassword() did not return the expected value", "password", basicUser.getPassword());
    }
}
