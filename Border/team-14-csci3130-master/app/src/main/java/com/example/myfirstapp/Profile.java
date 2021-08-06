package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myfirstapp.ChatPart.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class Profile extends AppCompatActivity {

    //Initialize variable
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

    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference imagesRef;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    private Button mBtn;

    ArrayList<DataSnapshot> postsFound;
    ArrayList<ArrayList<Uri>> imagesFound;
    int postIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mBtn = (Button) findViewById(R.id.btnMakePayment);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch2Payment();
            }
        });

        Button signOutButton = findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
            }
        });

        Button homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToMainFeed();
            }
        });

        Button postButton = findViewById(R.id.postButton);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch2CreateAdWindow();
            }
        });

        Button searchLocation = findViewById(R.id.searchLocationButton);
        searchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch2SearchWindow();
            }
        });

        Button chatbutton = findViewById(R.id.chatButton);
        chatbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch2ChatWindow();
            }
        });

        Button SendMessage = findViewById(R.id.SendMessageButton);
        SendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch2SendMessageWindow();
            }
        });

        Button managePosts = findViewById(R.id.ManagePostsButton);
        managePosts.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                switchToManagePosts();;
            }

        });

        postsFound = new ArrayList<>();
        imagesFound = new ArrayList<>();
        postIndex = 0;

        //initiating the Firebase
        initializeDatabase();
        retrieveAndDisplayNameAndEmail();
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

        if (currentUser != null){
            System.out.println("\nCURRENT USER UID: "+currentUser.getUid()+"\n");
        }
        else{
            System.out.println("\nNO CURRENT USER LOGGED IN\n");
        }

        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null){
                    switch2LoginWindow();
                }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public void retrieveAndDisplayNameAndEmail(){
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot usersSnapshot = snapshot.child("Users");
                String uName = usersSnapshot.child(currentUser.getUid()).child("username").getValue().toString();
                String email = usersSnapshot.child(currentUser.getUid()).child("email").getValue().toString();
                setUsername(uName);
                setEmail(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setUsername(String uName){
        TextView username = findViewById(R.id.username);
        username.setText(uName);
    }

    public void setEmail(String newEmail){
        TextView email = findViewById(R.id.email);
        email.setText(newEmail);
    }

    public void switch2Payment(){
        Intent intent = new Intent(this, PaypalActivity.class);
        startActivity(intent);
    }

    public void switch2SearchWindow(){
        Intent intent = new Intent(this, SearchLocationActivity.class);
        startActivity(intent);
    }

    public void switch2LoginWindow(){
        Intent intent=new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void switch2SendMessageWindow(){
        Intent intent=new Intent(this, SendMessageActivity.class);
        startActivity(intent);
    }

    public void switch2ChatWindow(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    /**
     * Switches page to the user's main feed page
     */
    public void switchToMainFeed() {
        Intent intent = new Intent(this, MainFeedActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }


    public void switch2CreateAdWindow(){
        Intent intent = new Intent(this, CreateAdActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void switchToManagePosts(){
        Intent intent = new Intent(this, ManagePostsActivity.class);
        startActivity(intent);
    }


}