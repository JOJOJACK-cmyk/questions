package com.example.post.service;
import com.example.post.dto.Page;
import com.example.post.dto.Post;
import java.util.List;
public interface PostService {
    List<Post> list(Page page);
    long count();
    Post read(Long postNo);
    void insert(Post post);
    void update(Post post);
    void delete(Long postNo);
}
