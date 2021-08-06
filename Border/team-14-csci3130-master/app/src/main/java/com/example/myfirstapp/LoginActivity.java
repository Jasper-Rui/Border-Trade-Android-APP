package com.example.myfirstapp;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

/**
 * Class to handle the activity pertaining logging in of the user and the login page.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
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
        setContentView(R.layout.login);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        Button moveToRegisterButton = findViewById(R.id.moveToRegisterButton);
        moveToRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch2RegisterWindow();
            }
        });

        //initiating the Firebase
        initializeDatabase();
    }

    /**
     * Connect to the firebase database and check if a user is already logged in
     */
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
            switchToMainFeed();
        }
        else {
            System.out.println("\nNO CURRENT USER LOGGED IN\n");
        }
    }

    /**
     * Gets the text from the email textField
     * @return the email that has been inputted
     */
    protected String getEmail() {
        EditText email = findViewById(R.id.email);
        return email.getText().toString().trim();
    }
    /**
     * Gets the text from the password textField
     * @return the password that has been inputted
     */
    protected String getPassword() {
        EditText password = findViewById(R.id.password);
        return password.getText().toString().trim();
    }

    /**
     * Checks if a string - email - is empty
     * @param s string
     * @return true if it is empty else false
     */
    public boolean isEmpty(String s) {
        return s.isEmpty();
    }

    /**
     * Checks if a string - email - is valid
     * @param email email string
     * @return true if it is valid else false
     */
    public boolean isValidEmailAddress(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";
        Pattern check = Pattern.compile(emailRegex);

        if (email == null) {
            return false;
        }
        else if (!check.matcher(email).matches()){
            return false;
        }
        else
            return true;
    }

    /**
     * Switches page to the user's main feed page
     */
    public void switchToMainFeed() {
        Intent intent = new Intent(this, MainFeedActivity.class);
        startActivity(intent);
    }

    /**
     * Switches page to the register page
     */
    public void switch2RegisterWindow() {
        Intent intent=new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Sets the status text view on the login page with a string
     * @param message the string message
     */
    protected void setStatusMessage(String message) {
        TextView statusLabel = findViewById(R.id.statusLabelPost);
        statusLabel.setText(message);
    }

    @Override
    public void onClick(View view) {
        String email = getEmail();
        String password = getPassword();
        String errorMessage = new String();

        if (isEmpty(email)){
            errorMessage = getResources().getString(R.string.EMPTY_EMAIL_ADDRESS);
        }
        else if(isEmpty(password)){
            errorMessage = getResources().getString((R.string.EMPTY_PASSWORD));
        }
        else if(!isValidEmailAddress(email)){
            errorMessage=getResources().getString(R.string.INVALID_EMAIL);
        }
        else {
            errorMessage = getResources().getString((R.string.EMPTY_STRING));
        }

        if (errorMessage.isEmpty()){
            // no errors in login textFields
            // check database!

            //sign in with email and password
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // Could not log in
                            if (!task.isSuccessful()){
                                setStatusMessage("Invalid email or password.");
                            }
                            // Successful login
                            else {
                                currentUser = task.getResult().getUser();
                                System.out.println("\nLOGGED IN USER UID: "+currentUser.getUid()+"\n");
                                switchToMainFeed();
                            }
                        }
                    });

        }
        else{
            setStatusMessage(errorMessage);
        }
    }
}
