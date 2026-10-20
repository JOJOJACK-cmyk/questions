package com.example.post.controller;
import com.example.post.dto.Page;
import com.example.post.dto.Post;
import com.example.post.service.PostService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
@Controller
public class PostController {
    private final PostService service;
    public PostController(PostService service) { this.service = service; }
    @GetMapping("/") public String index() { return "index"; }
    @GetMapping("/Post/list")
    public String list(@RequestParam(defaultValue="1") int page, Model model) {
        Page pagination = new Page(page, service.count());
        model.addAttribute("posts", service.list(pagination));
        model.addAttribute("page", pagination);
        return "Post/list";
    }
    @GetMapping("/Post/read")
    public String read(@RequestParam("PostNo") Long postNo, Model model) {
        model.addAttribute("post", service.read(postNo));
        return "Post/read";
    }
    @GetMapping("/Post/insert")
    public String insertForm(Model model) {
        model.addAttribute("post", new Post());
        return "Post/insert";
    }
    @PostMapping("/Post/insert")
    public String insert(@Valid @ModelAttribute("post") Post post, BindingResult errors) {
        if (errors.hasErrors()) return "Post/insert";
        // 생성 요청에서는 클라이언트가 전달한 식별자를 사용하지 않는다.
        post.setPostNo(null);
        service.insert(post);
        return "redirect:/Post/read?PostNo=" + post.getPostNo();
    }
    @GetMapping("/Post/update")
    public String updateForm(@RequestParam("PostNo") Long postNo, Model model) {
        model.addAttribute("post", service.read(postNo));
        return "Post/update";
    }
    @PostMapping("/Post/update")
    public String update(@RequestParam("PostNo") Long postNo,
                         @Valid @ModelAttribute("post") Post post, BindingResult errors) {
        post.setPostNo(postNo);
        if (errors.hasErrors()) return "Post/update";
        service.update(post);
        return "redirect:/Post/read?PostNo=" + postNo;
    }
    @PostMapping("/Post/delete")
    public String delete(@RequestParam("PostNo") Long postNo) {
        service.delete(postNo);
        return "redirect:/Post/list";
    }
}
