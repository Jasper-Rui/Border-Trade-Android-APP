package com.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myfirstapp.ChatPart.MainActivity;
import com.example.myfirstapp.ChatPart.MessageActivity;
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

public class MainFeedActivity extends AppCompatActivity implements View.OnClickListener{

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

    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference imagesRef;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    ArrayList<DataSnapshot> postsFound;
    ArrayList<ArrayList<Uri>> imagesFound;
    int postIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_feed);

        Button postButton = findViewById(R.id.postButton);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch2CreateAdWindow();
            }
        });

        Button profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch2ProfileWindow();
            }
        });

        Button chatbutton = findViewById(R.id.chatButton);
        chatbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch2ChatWindow();
            }
        });

        Button searchPostsButton = findViewById((R.id.searchPostsButton));
        searchPostsButton.setOnClickListener(this);

        postsFound = new ArrayList<>();
        imagesFound = new ArrayList<>();
        postIndex = 0;

        Button nextPostButton = findViewById(R.id.nextPostButton);
        nextPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!postsFound.isEmpty() && postIndex+1 < postsFound.size()){
                    postIndex++;
                    displayPost();
                }
            }
        });

        Button prevPostButton = findViewById(R.id.prevPostButton);
        prevPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!postsFound.isEmpty() && postIndex-1 >= 0){
                    postIndex--;
                    System.out.println(postIndex);
                    displayPost();
                }
            }
        });

        //initiating the Firebase
        initializeDatabase();

        retrieveAndDisplayAllPosts();
    }

    protected void initializeDatabase() {
        database = FirebaseDatabase.getInstance("https://team-14-csci3130-patch-1-default-rtdb.firebaseio.com/");
        root = database.getReference();

        storage = FirebaseStorage.getInstance("gs://team-14-csci3130-patch-1.appspot.com");
        storageRef = storage.getReference();
        imagesRef = storageRef.child("images");

        users = root.child("Users");
        userNameRef = users.child("Name");
        userEmailRef = users.child("Email");
        userPasswordRef= users.child("Password");

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

    protected void retrieveAndDisplayAllPosts(){
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot postsSnapshot = snapshot.child("Posts");
                for (DataSnapshot childDataSnapshot : postsSnapshot.getChildren()) {
                    postsFound.add(childDataSnapshot);
                    final ArrayList<Uri> currentPostImages = new ArrayList<>();
                    StorageReference imgRef = imagesRef.child(childDataSnapshot.getKey());
                    imgRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                        @Override
                        public void onSuccess(ListResult listResult) {
                            for (StorageReference img : listResult.getItems()){
                                img.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if(task.isSuccessful()){
                                            Uri imgUri = task.getResult();
                                            currentPostImages.add(imgUri);
                                            System.out.println("Found image");
                                        }
                                        else{
                                            System.out.println("No image found");
                                        }
                                    }
                                });
                            }
                        }
                    });
                    imagesFound.add(currentPostImages);
                }
                if (!postsFound.isEmpty()){
                    displayPost();
                }
                else{
                    setPostTitle("");
                    setPostDescription("Sorry, no posts found :(");
                    setPostPrice("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void switch2LoginWindow(){
        Intent intent=new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void switch2CreateAdWindow(){
        Intent intent = new Intent(this, CreateAdActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
    public void switch2ProfileWindow(){
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void switch2ChatWindow(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public String getSearchPostsInput(){
        EditText searchPostsInput = findViewById(R.id.searchPostsInput);
        return searchPostsInput.getText().toString().trim();
    }

    public void setPostTitle(String newTitle){
        TextView postTitle = findViewById(R.id.postTitleTextView);
        postTitle.setText(newTitle);
    }

    public void setPostDescription(String newDescription){
        TextView description = findViewById(R.id.postDescriptionTextView);
        description.setText(newDescription);
    }

    public void setPostPrice(String newPrice){
        TextView description = findViewById(R.id.postPriceTextView);

        description.setText("$" + newPrice);
    }

    public void displayPost(){
        String postTitle = postsFound.get(postIndex).child("title").getValue().toString();
        String postDescription = postsFound.get(postIndex).child("description").getValue().toString();
        String postPrice = postsFound.get(postIndex).child("price").getValue().toString();
        setPostTitle(postTitle);
        setPostDescription(postDescription);
        setPostPrice(postPrice);
        if (!imagesFound.isEmpty()){
            if (!imagesFound.get(postIndex).isEmpty()){
                ImageView adImgView = findViewById(R.id.adImgView);
                Glide.with(getApplicationContext()).load(imagesFound.get(postIndex).get(0)).into(adImgView);
            }
            else{
                ImageView adImgView = findViewById(R.id.adImgView);
                adImgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_no_image));
            }
        }
    }


    @Override
    public void onClick(View view) {
        postsFound.clear();
        postIndex = 0;
        final String searchValue = getSearchPostsInput();

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot postsSnapshot = snapshot.child("Posts");

                for (DataSnapshot childDataSnapshot : postsSnapshot.getChildren()) {
                    if(!searchValue.isEmpty() && childDataSnapshot.child("title").getValue().toString().toLowerCase().contains(searchValue.toLowerCase())){
                        postsFound.add(childDataSnapshot);
                        final ArrayList<Uri> currentPostImages = new ArrayList<>();
                        StorageReference imgRef = imagesRef.child(childDataSnapshot.getKey());
                        imgRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                            @Override
                            public void onSuccess(ListResult listResult) {
                                for (StorageReference img : listResult.getItems()) {
                                    img.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                Uri imgUri = task.getResult();
                                                currentPostImages.add(imgUri);
                                                System.out.println("Found image");
                                            } else {
                                                System.out.println("No image found");
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        imagesFound.add(currentPostImages);
                    }
                }
                if (!postsFound.isEmpty()){
                    displayPost();
                }
                else{
                    setPostTitle("");
                    setPostDescription("Sorry, no posts found for '" + searchValue + "' :(");
                    setPostPrice("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}