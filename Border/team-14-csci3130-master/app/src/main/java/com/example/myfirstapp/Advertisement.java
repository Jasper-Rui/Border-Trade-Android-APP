package com.example.myfirstapp;

import android.os.Bundle;

import java.text.DecimalFormat;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Advertisement {
    //Variables
    String title;
    Double price;
    String description;
    String userId;
    String postDate;


    //getters and setters
    public String getTitle() { return title; }

    public void setTitle(String t) {this.title = t;}


    public Double getPrice() { return price; }

    public void setPrice(Double p) {this.price = (p);}


    public String getDescription() { return description; }

    public void setDescription(String d) { this.description = d;}

    public String getUserId() { return userId; }

    public void setUserId(String id) { this.userId = id;}

    public void setPostDate(String pDate){
        this.postDate = pDate;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.advertisement);
//
//        Button addAdButton = findViewById(R.id.addAd);
//        Button editAdButton = findViewById(R.id.editAd);
//        Button removeAdButton = findViewById(R.id.removeAd);
//    }
//
//
//    @Override
//    public void onClick(View v) {
//
//    }
}
