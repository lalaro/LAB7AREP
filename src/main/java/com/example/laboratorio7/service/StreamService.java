package com.example.laboratorio7.service;

import com.example.laboratorio7.model.Post;
import com.example.laboratorio7.model.Stream;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class StreamService implements CommandLineRunner {

    private Stream stream;
    private final List<Post> posts = new ArrayList<>();
    private final AtomicLong postIdGenerator = new AtomicLong(1);

    @Override
    public void run(String... args) {
        stream = new Stream("principal");
    }

    public Stream getMainStream() {
        return stream;
    }

    public Post savePost(Post post) {
        post.setId(postIdGenerator.getAndIncrement());
        post.setStream(stream);
        posts.add(post);
        stream.addPost(post);
        return post;
    }

    public List<Post> getAllPosts() {
        return new ArrayList<>(posts);
    }
}
