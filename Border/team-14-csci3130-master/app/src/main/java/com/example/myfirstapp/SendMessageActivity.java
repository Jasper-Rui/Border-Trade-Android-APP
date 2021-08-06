package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.service.carrier.CarrierMessagingService;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class SendMessageActivity extends AppCompatActivity {

    //initialize variables
    private EditText number, message;
    private Button send,goback;

    FirebaseDatabase database;
    DatabaseReference root;
    DatabaseReference users;
    DatabaseReference userNameRef;
    DatabaseReference userEmailRef;
    DatabaseReference userPasswordRef;
    DatabaseReference posts;
    DatabaseReference postTitleRef;
    DatabaseReference postDescriptionRef;
    DatabaseReference postValueRef;
    DatabaseReference smshistory;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        number = findViewById(R.id.number);
        message = findViewById(R.id.message);
        send = findViewById(R.id.send);
        goback= findViewById(R.id.GoBackToprevious);
        initializeDatabase();


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                        sendSMStoUser();
                        saveSMStToDB();
                    }
                    else{
                        requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
                    }
                }

            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchtoprofile();
            }
        });




    }
    public String getSMS() {
        String phonenum = number.getText().toString().trim();
        String textmessage = message.getText().toString().trim();
        return (phonenum +" " + textmessage).trim();
    }

    public void switchtoprofile(){
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    public void sendSMStoUser(){
        String phonenum = number.getText().toString().trim();
        String textmessage = message.getText().toString().trim();
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phonenum, null, textmessage,null,null);
            Toast.makeText(this, "Message is sent", Toast.LENGTH_SHORT).show();
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show();
        }

    }
    protected void initializeDatabase() {
        database = FirebaseDatabase.getInstance("https://team-14-csci3130-patch-1-default-rtdb.firebaseio.com/");
        root = database.getReference();

        users = root.child("Users");
        userNameRef = users.child("Name");
        userEmailRef = users.child("Email");
        userPasswordRef= users.child("Password");

        smshistory = users.child("SMS history");

        posts = root.child("Posts");
        postTitleRef = posts.child("Title");
        postDescriptionRef = posts.child("Description");
        postValueRef = posts.child("Value");

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();


    }
    public void saveSMStToDB(){

        String history = getSMS();
        currentUser.getUid();
        users.child("SMS history "+currentUser.getUid()).setValue(history);


    }

}



