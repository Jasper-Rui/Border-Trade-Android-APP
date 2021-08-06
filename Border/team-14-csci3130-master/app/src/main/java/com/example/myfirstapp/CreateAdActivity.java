package com.example.myfirstapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class CreateAdActivity extends AppCompatActivity implements View.OnClickListener {

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
    DatabaseReference LocationRef;
    DatabaseReference smshistory;

    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference imagesRef;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    Button Location;
    TextView UserLocation;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;

    public static ArrayList<Uri> imagesToUpload;
    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_advertisement);

        //Assign Variables
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        UserLocation = findViewById(R.id.UserLocation);
        Location = findViewById(R.id.Location);


        Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check permission
                if (ActivityCompat.checkSelfPermission(CreateAdActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //if permission granted
                    //getCurrentLocation();
                    switchToMap();

                } else {
                    //when permission denied
                    ActivityCompat.requestPermissions(CreateAdActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }

                //switchToMap();
            }
        });

        Button backToMainFeed = findViewById(R.id.backButtonCreateAd);
        backToMainFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToMainFeed();
            }
        });


        imagesToUpload = new ArrayList<>();
        System.out.println(imagesToUpload.size());

        Button addPhotosButton = findViewById(R.id.addPhotoButton);
        addPhotosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
            }
        });

        Button postAdButton = findViewById(R.id.postAdButton);
        postAdButton.setOnClickListener(this);

        //initiating the Firebase
        initializeDatabase();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (imagesToUpload.size() >= 3){
                setStatusMessage("Maximum number of images exceeded");
            }
            else{
                System.out.println("Uploading image...");
                System.out.println(data.getData());
                Uri file = data.getData();
                imagesToUpload.add(file);
                ImageView imageView;
                if (imagesToUpload.size() == 3){
                    imageView = findViewById(R.id.imageView3);
                }
                else if(imagesToUpload.size() == 2){
                    imageView = findViewById(R.id.imageView2);
                }
                else{
                    imageView = findViewById(R.id.imageView1);
                }
                imageView.setImageURI(file);
            }
        }
    }

    /*private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<android.location.Location>() {
            @Override
            public void onComplete(@NonNull Task<android.location.Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        //Initialize geoCoder
                        Geocoder geocoder = new Geocoder(CreateAdActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        //set text view
                        UserLocation.setText(Html.fromHtml("<font color = '#6200EE'><b>Addressline : </b><br></font>"
                        + addresses.get(0).getAddressLine(0)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

     */
    




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

        smshistory = users.child("SMS history");

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null){
            System.out.println("\nCURRENT USER UID: "+currentUser.getUid()+"\n");
        }
        else {
            System.out.println("\nNO CURRENT USER LOGGED IN\n");
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }


    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    /**
     * Switches page to the user's main feed page
     */
    public void switchToMainFeed() {
        Intent intent = new Intent(this, MainFeedActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    /**
     * Switches page to the Map page
     */
    public void switchToMap() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }


    /**
     * Gets the text from the TitleInput textField
     * @return the title that has been inputted
     */
    protected String getPostTitle() {
        EditText postTitle = findViewById(R.id.TitleInput);
        return postTitle.getText().toString().trim();
    }

    /**
     * Gets the text from the DescriptionInput textField
     * @return the description that has been inputted
     */
    protected String getPostDescription() {
        EditText postDescription = findViewById(R.id.DescriptionInput);
        return postDescription.getText().toString().trim();
    }

    /**
     * Gets the text from the valueInput textField
     * @return the description that has been inputted
     */
    protected String getPostValue() {
        EditText postValue = findViewById(R.id.valueInput);
        return postValue.getText().toString().trim();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected String getPostDate(){
        LocalDate date = java.time.LocalDate.now();
        System.out.println(date.toString());
        return date.toString();
    }

    /**
     * Sets the status text view on the login page with a string
     * @param message the string message
     */
    protected void setStatusMessage(String message) {
        TextView statusLabel = findViewById(R.id.statusLabelPost);
        statusLabel.setText(message);
    }

    /**
     * Checks if a string - email - is empty
     * @param s string
     * @return true if it is empty else false
     */
    public boolean isEmpty(String s) {
        return s.isEmpty();
    }

    public void savePostToDB(String title, String description, double price, String uid, String date){
        Advertisement newAd = new Advertisement();
        newAd.setTitle(title);
        newAd.setDescription(description);
        newAd.setPrice(price);
        newAd.setUserId("user"+currentUser.getUid());
        newAd.setPostDate(date);
        posts.child("post"+uid).setValue(newAd);
        if (imagesToUpload.size() > 0){
            saveImagesToDB(uid);
        }
    }

    public void saveImagesToDB(String uid){
        StorageReference imagesForPostRef = imagesRef.child("post"+uid);
        for(int i=0; i<imagesToUpload.size(); i++){
            Uri file = imagesToUpload.get(i);
            StorageReference newRef = imagesForPostRef.child("image"+(i+1));
            UploadTask uploadTask = newRef.putFile(file);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("Could not upload file");
                    setStatusMessage("Could not save images to database");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("Uploaded file successfully");
                }
            });
        }
    }

    // post the ad
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        String title = getPostTitle();
        String description = getPostDescription();
        String price = getPostValue();
        String postDate = getPostDate();
        String errorMessage = new String();

        if (isEmpty(title)){
            errorMessage = getResources().getString(R.string.EMPTY_POST_TITLE);
        }
        else if(isEmpty(description)){
            errorMessage = getResources().getString((R.string.EMPTY_POST_DESCRIPTION));
        }
//        else if(!isValidPostTitle(title)){
//            errorMessage="Invalid title";
//        }
        else if(isEmpty(price)){
            System.out.println("VALUE IS EMPTY!");
            errorMessage = getResources().getString(R.string.EMPTY_POST_VALUE);
        }
        else {
            errorMessage = getResources().getString((R.string.EMPTY_STRING));
        }

        // success - create ad
        if (errorMessage.isEmpty()){
            UUID puid = UUID.randomUUID();
            String postId = puid.toString();
            System.out.println("Title: " +title);

            savePostToDB(title, description, Double.parseDouble(price), postId, postDate);
            switchToMainFeed();

        }
        else{
            setStatusMessage(errorMessage);
        }

    }
}
