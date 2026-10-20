package com.example.post.service;
import com.example.post.dto.Page;
import com.example.post.dto.Post;
import com.example.post.mapper.PostMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
@Service
@Transactional(readOnly=true)
public class PostServiceImpl implements PostService {
    private final PostMapper mapper;
    public PostServiceImpl(PostMapper mapper) { this.mapper = mapper; }
    @Override public List<Post> list(Page page) { return mapper.selectList(page); }
    @Override public long count() { return mapper.count(); }
    @Override public Post read(Long postNo) {
        Post post = mapper.selectById(postNo);
        if (post == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 없습니다.");
        return post;
    }
    @Override @Transactional public void insert(Post post) {
        if (mapper.insert(post) != 1) throw new IllegalStateException("등록에 실패했습니다.");
    }
    @Override @Transactional public void update(Post post) {
        if (mapper.update(post) != 1) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 없습니다.");
    }
    @Override @Transactional public void delete(Long postNo) {
        if (mapper.delete(postNo) != 1) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 없습니다.");
    }
}
