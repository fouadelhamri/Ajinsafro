package com.example.ajinafro.models;

import java.io.Serializable;
import java.util.ArrayList;

public class UserAccountDetails implements Serializable {
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
        this.bio = bio;
        this.city = city;
        this.email = email;
        this.fullname = fullname;
        this.phone = phone;
        this.posts = posts;
        this.profile_image = profile_image;
        this.saved_posts = saved_posts;
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public String getCity() {
        return city;
    }

    public String getEmail() {
        return email;
    }

    public String getFullname() {
        return fullname;
    }

    public String getPhone() {
        return phone;
    }

    public ArrayList<String> getPosts() {
        return posts;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public ArrayList<String> getSaved_posts() {
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
