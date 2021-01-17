package com.example.ajinafro.models;

public class Comments {
    private String content;
    private String user_id;

    public Comments(String content, String user_id) {
        this.content = content;
        this.user_id = user_id;
    }

    public Comments() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Comments{" +
                "content='" + content + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
