package com.example.laboratorio7.model;

import com.example.laboratorio7.model.Stream;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Post {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    private Long id;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private Stream stream;

    public Post(String content, String author) {
        this.id = ID_GENERATOR.getAndIncrement();
        this.content = content;
        this.author = author;
        this.createdAt = LocalDateTime.now();
    }

    public Post() {

    }

    public Long getId() { return id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setStream(Stream stream) { this.stream = stream; }
    public Stream getStream() { return stream; }

    public void setId(long andIncrement) {
        this.id = andIncrement;
    }
}