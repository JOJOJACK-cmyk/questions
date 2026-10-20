package com.example.post;
import com.example.post.dto.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest(properties={"spring.datasource.url=jdbc:h2:mem:posttest;MODE=MySQL;DB_CLOSE_DELAY=-1"})
@AutoConfigureMockMvc
@Transactional
class PostApplicationTest {
    @Autowired MockMvc mvc;
    @Autowired JdbcTemplate jdbc;
    @Test void crudAndViews() throws Exception {
        mvc.perform(get("/")).andExpect(status().isOk());
        mvc.perform(get("/Post/insert")).andExpect(status().isOk());
        mvc.perform(post("/Post/insert").param("title","제목").param("writer","작성자").param("content","내용"))
            .andExpect(status().is3xxRedirection());
        Long id = jdbc.queryForObject("SELECT MAX(post_no) FROM posts", Long.class);
        mvc.perform(get("/Post/read").param("PostNo",id.toString())).andExpect(status().isOk()).andExpect(model().attributeExists("post"));
        mvc.perform(get("/Post/update").param("PostNo",id.toString())).andExpect(status().isOk());
        mvc.perform(post("/Post/update").param("PostNo",id.toString()).param("title","수정 제목").param("writer","작성자").param("content","수정 내용"))
            .andExpect(status().is3xxRedirection());
        assertEquals("수정 제목",jdbc.queryForObject("SELECT title FROM posts WHERE post_no=?",String.class,id));
        mvc.perform(post("/Post/delete").param("PostNo",id.toString())).andExpect(redirectedUrl("/Post/list"));
        mvc.perform(get("/Post/read").param("PostNo",id.toString())).andExpect(status().isNotFound());
    }
    @Test void paginationAndInvalidInput() throws Exception {
        for(int i=0;i<23;i++) jdbc.update("INSERT INTO posts(title,writer,content) VALUES(?,?,?)","제목"+i,"작성자","내용");
        mvc.perform(get("/Post/list").param("page","2")).andExpect(status().isOk())
            .andExpect(model().attribute("posts",org.hamcrest.Matchers.hasSize(10)));
        mvc.perform(get("/Post/list").param("page","999")).andExpect(status().isOk())
            .andExpect(model().attribute("posts",org.hamcrest.Matchers.hasSize(3)));
        mvc.perform(post("/Post/insert").param("title"," ").param("writer","작성자").param("content","내용"))
            .andExpect(status().isOk()).andExpect(model().attributeHasFieldErrors("post","title"));
        assertEquals(23L,jdbc.queryForObject("SELECT COUNT(*) FROM posts",Long.class));
    }
    @Test void pageBoundaries() {
        Page empty = new Page(-3,0);
        assertEquals(1,empty.getPage()); assertEquals(0,empty.getOffset());
        Page page = new Page(6,101);
        assertEquals(6,page.getStartPage()); assertEquals(10,page.getEndPage());
        assertEquals(50,page.getOffset()); assertTrue(page.isHasPrevious()); assertTrue(page.isHasNext());
    }
}
