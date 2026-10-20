package com.example.post.mapper;
import com.example.post.dto.Page;
import com.example.post.dto.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
@Mapper
public interface PostMapper {
    List<Post> selectList(Page page);
    long count();
    Post selectById(@Param("postNo") Long postNo);
    int insert(Post post);
    int update(Post post);
    int delete(@Param("postNo") Long postNo);
}
