package com.example.ajinafro.models;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class UserAccountDetails implements Serializable {
    private static final String TAG = "UserAccountDetails";
    private String bio;
    private String city;
    private String email;
    private String fullname;
    private String phone;
    private ArrayList<String> posts;
    private String profile_image ;
    private ArrayList<String> saved_posts;
    private String username;

    public UserAccountDetails() {
    }

    public UserAccountDetails(String bio, String city, String email, String fullname, String phone, ArrayList<String> posts, String profile_image, ArrayList<String> saved_posts, String username) {
        if (phone==null) phone="";
        if (posts==null)posts=new ArrayList<String>();
        profile_image="https://firebasestorage.googleapis.com/v0/b/ajinsafro-db.appspot.com/o/profile-default.jpg?alt=media&token=430a5da5-9f46-46a0-b816-08ecc26e49fa";
        if(saved_posts==null)saved_posts=new ArrayList<String>();
        if(bio==null)bio="";
        if(city==null)city="";
        this.bio = bio;
        this.city = city;
        this.email = email;
        this.fullname = fullname;
        this.phone = phone;
        this.posts = posts;
        this.profile_image = profile_image;
        this.saved_posts = saved_posts;
        this.username = username;
        if(this.getProfile_image()==null){
            Log.d(TAG, "UserAccountDetails: "+"null image inserted");
        }
    }

    public String getBio() {
        if(this.bio==null) return "";
        return bio;
    }

    public String getCity() {
        if(this.city==null)return "";
        return city;
    }

    public String getEmail() {
        if(this.email==null)return "";
        return email;
    }

    public String getFullname() {
        if(this.fullname==null)return "";
        return fullname;
    }

    public String getPhone() {
        if(this.phone==null)return "";
        return phone;
    }

    public ArrayList<String> getPosts() {
        if (this.posts==null) return new ArrayList<String>();
        return posts;
    }

    public String getProfile_image() {
        if(profile_image==null){
            profile_image="https://firebasestorage.googleapis.com/v0/b/ajinsafro-db.appspot.com/o/profile-default.jpg?alt=media&token=430a5da5-9f46-46a0-b816-08ecc26e49fa";
        }
        return profile_image;
    }

    public ArrayList<String> getSaved_posts() {
        if (this.saved_posts==null) return new ArrayList<String>();
        return saved_posts;
    }

    public String getUsername() {
        return username;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPosts(ArrayList<String> posts) {
        this.posts = posts;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public void setSaved_posts(ArrayList<String> saved_posts) {
        this.saved_posts = saved_posts;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserAccountDetails{" +
                "bio='" + bio + '\'' +
                ", city='" + city + '\'' +
                ", email='" + email + '\'' +
                ", fullname='" + fullname + '\'' +
                ", phone='" + phone + '\'' +
                ", posts=" + posts +
                ", profile_image='" + profile_image + '\'' +
                ", saved_posts=" + saved_posts +
                ", username='" + username + '\'' +
                '}';
    }
}
