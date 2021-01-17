package com.example.ajinafro.models;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class Posts {
    private Adresse adresse;
    private ArrayList<String> comments;//comments_id
    private Timestamp created_at;
    private String description;
    private ArrayList<String> likes;//users_ids
    private String photo;
    private String publisher;

    public Posts(Adresse adresse, ArrayList<String> comments, Timestamp created_at, String description, ArrayList<String> likes, String photo, String publisher) {
        this.adresse = adresse;
        this.comments = comments;
        this.created_at = created_at;
        this.description = description;
        this.likes = likes;
        this.photo = photo;
        this.publisher = publisher;
    }

    public Posts() {
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        return "Posts{" +
                "adresse=" + adresse.toString() +
                ", comments=" + comments +
                ", created_at=" + created_at +
                ", description='" + description + '\'' +
                ", likes=" + likes +
                ", photo='" + photo + '\'' +
                ", publisher='" + publisher + '\'' +
                '}';
    }
}
