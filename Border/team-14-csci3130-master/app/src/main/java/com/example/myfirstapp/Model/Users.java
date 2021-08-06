package com.example.myfirstapp.Model;
// this user class is for chat

public class Users {


    String username, imageURL, id, status, password;

    public Users(String username, String imageURL, String id, String status, String password) {
        this.username = username;
        this.imageURL = imageURL;
        this.id = id;
        this.status = status;
        this.password = password;

    }

    public Users() {
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
