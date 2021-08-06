
package com.example.myfirstapp;

import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import java.util.List;

public class ManagePostsActivity extends AppCompatActivity implements View.OnClickListener{
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
        setContentView(R.layout.manage_posts_page);

        //populating the list
        this.populateList();

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

    protected void populateList() {
        Spinner itemList = (Spinner) findViewById(R.id.currencySelector);
        List<String> items = new ArrayList<String>();
        items.add("$");
        items.add("€");
        @SuppressLint("ResourceType") ArrayAdapter<String> itemListAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, items);
        itemListAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        itemList.setAdapter(itemListAdapter);
    }

    /**
     * Connect to the firebase database and check if a user is already logged in
     */
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
                    if (childDataSnapshot.child("userId").getValue().equals("user" + currentUser.getUid())) {
                        System.out.println("FOUND");
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
                    if (!postsFound.isEmpty()) {
                        displayPost();
                    } else {
                        setPostTitle("");
                        setPostDescription("Sorry, no posts found :(");
                        setPostPrice("0");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void displayPost(){
        String postTitle = postsFound.get(postIndex).child("title").getValue().toString();
        String postDescription = postsFound.get(postIndex).child("description").getValue().toString();
        String postPrice = postsFound.get(postIndex).child("price").getValue().toString();
        setPostTitle(postTitle);
        setPostDescription(postDescription);
        setPostPrice(postPrice);
        if (!imagesFound.isEmpty()){
            ImageView adImgView = findViewById(R.id.adImgView);
            if (!imagesFound.get(postIndex).isEmpty()){
                Glide.with(getApplicationContext()).load(imagesFound.get(postIndex).get(0)).into(adImgView);
            }
            else{
                adImgView.setImageDrawable(getResources().getDrawable(R.drawable.ic_no_image));
            }
        }
    }

    public void setPostTitle(String newTitle){
        TextView postTitle = findViewById(R.id.postTitleText);
        postTitle.setText(newTitle);
    }

    public void setPostDescription(String newDescription){
        TextView description = findViewById(R.id.postDescriptionText);
        description.setText(newDescription);
    }

    public void setPostPrice(String newPrice){
        TextView price = findViewById(R.id.postPriceText);
        price.setText(newPrice);
    }

    /**
     * Switches page to the user's main feed page
     */
    public void switchToMainFeed() {
        Intent intent = new Intent(this, MainFeedActivity.class);
        startActivity(intent);
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


    @Override
    public void onClick(View view) {

    }
}
