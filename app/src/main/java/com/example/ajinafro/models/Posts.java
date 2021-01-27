package com.example.ajinafro.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;

public class Posts {
    private Adresse adresse;
    private ArrayList<String> comments;//comments_id
    @ServerTimestamp
    private Timestamp created_at;

    private String description;
    private ArrayList<String> likes;//users_ids
    private ArrayList<String> photo;
    private String publisher;
    private String name;
    public Posts(String name,Adresse adresse, ArrayList<String> comments, Timestamp created_at, String description, ArrayList<String> likes, ArrayList<String> photo, String publisher) {
        this.adresse = adresse;
        this.comments = comments;
        this.created_at = created_at;
        this.description = description;
        this.likes = likes;
        this.photo = photo;
        this.publisher = publisher;
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public ArrayList<String> getPhoto() {
        return photo;
    }

    public void setPhoto(ArrayList<String> photo) {
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
                "name="+name+
                "adresse=" + adresse.toString() +
                ", comments=" + comments +
                ", created_at=" + created_at +
                ", description='" + description + '\'' +
                ", likes=" + likes +
                ", photo='" + photo.toString() + '\'' +
                ", publisher='" + publisher + '\'' +
                '}';
    }
}
