package com;

import com.example.myfirstapp.LoginActivity;
import com.example.myfirstapp.Message;
import com.example.myfirstapp.Profile;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;
//this tests are written before implement profile code,
//maid idea is to test
//1st: if we can successfully switch to profile activity
//2dn if we can check the payment method
//3rd if we can //to be added
public class PaymentTests {

    static Profile profile;

    @Test
    public void getPaymentmethod() {

    }



    @AfterClass
    public static void tearDown(){
        System.gc();
    }
}