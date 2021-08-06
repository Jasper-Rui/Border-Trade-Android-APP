package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
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
    DatabaseReference SMS;
    DatabaseReference smshistory;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        //attaching the event handler
        Button goBack = findViewById(R.id.GoBackToprevious);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch2LoginWindow();
            }
        });

        Button button = findViewById(R.id.button);
        button.setOnClickListener(this);
        initializeDatabase();

    }

    protected void initializeDatabase() {
        database = FirebaseDatabase.getInstance("https://team-14-csci3130-patch-1-default-rtdb.firebaseio.com/");
        root = database.getReference();

        users = root.child("Users");
        userNameRef = users.child("Name");
        userEmailRef = users.child("Email");
        userPasswordRef= users.child("Password");

        SMS = root.child("SMS");
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
    }
    public   String getFullName(){
        EditText fullName = findViewById(R.id.editTextTextPersonName);
        return fullName.getText().toString().trim();
    }

    public String getEmailForNewUser(){
        EditText email = findViewById(R.id.editTextTextPersonName2);
        return email.getText().toString().trim();
    }

    public String getPasswordForNewUser() {
        EditText password = findViewById(R.id.passwordInput);
        return password.getText().toString().trim();
    }
    public boolean isEmailForNewUserEmpty(String EmailForNewUser) {
        return EmailForNewUser.isEmpty();
    }
    public boolean isFullNameEmpty(String fullName) {
        return fullName.isEmpty();
    }
    public boolean isPasswordForNewUserEmpty(String PasswordForNewUser) {
        return PasswordForNewUser.isEmpty();
    }
    //name must have number&character[a_zA_Z]
    public boolean isUserNameValid(String userName){
        return !userName.isEmpty();
    }

    /*At least one digit [0-9]
     At least one lowercase character [a-z]
     At least one uppercase character [A-Z]
     At least one special character [*.!@#$%^&(){}[]:;<>,.?/~_+-=|\]
     At least 8 characters in length,at most 20.*/
    public boolean isValidPassword(String password){
        String pwRegex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!]).{8,20}$";
        Pattern check = Pattern.compile(pwRegex) ;
        if(password == null){
            return  false;
        }
        else return check.matcher(password).matches();
    }

    public boolean isValidEmailAddress(String emailAddress) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";
        Pattern check = Pattern.compile(emailRegex);

        if (emailAddress == null) {
            return false;
        }
        else if (!check.matcher(emailAddress).matches()){
            return false;
        }
        else
            return true;

    }

    public void switchToMainFeed() {
        Intent intent = new Intent(this, MainFeedActivity.class);
        startActivity(intent);
    }
    public void switch2LoginWindow(){
        Intent intent=new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void setNotificationMessage(String message) {
        TextView notification = findViewById(R.id.statuslabelforregister);
        notification.setText(message);
    }

    protected void saveFullNameToDB(String fullName) {
        //save user name to Firebase
        userNameRef.setValue(fullName);
    }

    public void saveUserToDB(String fullName, String email, String password, String uid){
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFullName(fullName);
        newUser.setPassword(password);
        users.child("user"+uid).setValue(newUser);
    }

    public void onClick(View view) {
        final String fullName = getFullName();
        final String emailForNewUser = getEmailForNewUser();
        final String password = getPasswordForNewUser();
        String errorMessage = new String();
        if (isEmailForNewUserEmpty(emailForNewUser) || isFullNameEmpty(fullName) || isPasswordForNewUserEmpty(password)) {
            errorMessage = getResources().getString(R.string.EMPTY_FIELDS);
        } else if (!isValidEmailAddress(emailForNewUser)) {
            errorMessage = "Please check you email format";
        } else if (!isValidPassword(password)) {
            errorMessage = "Invalid password";
        } else if (!isUserNameValid(fullName)) {
            errorMessage = "Invalid name";
        } else {
            errorMessage = getResources().getString(R.string.EMPTY_STRING);
        }

        setNotificationMessage(errorMessage);
        registerUser(fullName, password, emailForNewUser);

        if (errorMessage.isEmpty()){
            // no errors in login textFields
            // check database!

            firebaseAuth.createUserWithEmailAndPassword(emailForNewUser, password)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                setNotificationMessage("Could not create an account with those credentials.");
                            }
                            else{
                                currentUser = task.getResult().getUser();
                                String uid = currentUser.getUid();
                                System.out.println("UID: "+uid);
                                saveUserToDB(fullName, emailForNewUser, password, uid);
                                switchToMainFeed();
                            }
                        }
                    });
        }
    }

    private void registerUser(final String username, final String password, final String email) {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    FirebaseUser user = firebaseAuth.getCurrentUser();

                    users = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());


                    if (user!=null) {

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("username", username);
                        hashMap.put("email", email);
                        hashMap.put("id", user.getUid());
                        hashMap.put("imageURL", "default");
                        hashMap.put("status", "offline");
                        hashMap.put("password", password);


                        users.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                                if (task.isSuccessful()) {

                                    Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(RegisterActivity.this,
                                           MainFeedActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK ));


                                }
                            }
                        });
                    }

                }



            }
        });

    }




}
