package com.example.myfirstapp.ChatPart;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myfirstapp.Fragments.ChatFragment;
import com.example.myfirstapp.Fragments.ProfileFragment;
import com.example.myfirstapp.Fragments.UserFragment;
import com.example.myfirstapp.LoginActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.myfirstapp.Model.Users;
import com.example.myfirstapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    CircleImageView imageView;
    TextView username;

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
        setContentView(R.layout.activity_main_chat);

        imageView = findViewById(R.id.profile_image);
        username = findViewById(R.id.usernameonmainactivity);

        toolbar = (Toolbar)findViewById(R.id.toolbarmain);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        TabLayout tabLayout = findViewById(R.id.tablayout);
        ViewPager viewPager = findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragment(new ChatFragment(), "Chats");
        viewPagerAdapter.addFragment(new UserFragment(), "Users");
        //viewPagerAdapter.addFragment(new ProfileFragment(), "Profile");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        initializeDatabase();
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                Users users = snapshot.getValue(Users.class);

                username.setText(currentUser.getUid()); // set the text of the user on textivew in toolbar
                imageView.setImageResource(R.drawable.ic_no_image);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





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
    public void switch2LoginWindow(){
        Intent intent=new Intent(this, LoginActivity.class);
        startActivity(intent);
    }




    class ViewPagerAdapter extends FragmentPagerAdapter {

        ArrayList<Fragment> fragments;
        ArrayList<String> titles;


        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);

            this.fragments = new ArrayList<>();
            this. titles = new ArrayList<>();

        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment (Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.signOutButton) {

            firebaseAuth.signOut();
            finish();
            return  true;


        }

        return super.onOptionsItemSelected(item);
    }


    private void Status (final String status) {


        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);



    }

    @Override
    protected void onResume() {
        super.onResume();
        Status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Status("offline");
    }


}