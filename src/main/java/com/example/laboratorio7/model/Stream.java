package com.example.laboratorio7.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Stream {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    private Long id;
    private String title;
    private List<Post> posts = new ArrayList<>();

    public Stream(String title) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.title = title;
    }

    public void addPost(Post post) {
        posts.add(post);
        post.setStream(this);
    }

    public List<Post> getAllPosts() {
        return posts;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
}
