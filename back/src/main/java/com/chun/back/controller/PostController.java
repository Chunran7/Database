package com.chun.back.controller;

import com.chun.back.pojo.Post;
import com.chun.back.pojo.Result;
import com.chun.back.service.CommentService;
import com.chun.back.service.PostService;
import com.chun.back.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    private Long getLoginUserId(HttpServletRequest request) {
        Object obj = request.getAttribute("claims");
        if (obj instanceof Map<?, ?> m) {
            Object id = m.get("id");
            if (id != null) return Long.valueOf(id.toString());
        }
        return null;
    }

    private Long tryGetViewerId(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth == null || auth.isBlank()) return null;
        String token = auth.startsWith("Bearer ") ? auth.substring(7) : auth;
        try {
            Map<String, Object> claims = JwtUtil.parseToken(token);
            Object id = claims.get("id");
            return id == null ? null : Long.valueOf(id.toString());
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int pageSize,
                       @RequestParam(required = false) String sortBy,
                       @RequestParam(required = false) String order,
                       @RequestParam(required = false) String keyword,
                       HttpServletRequest request) {
        Long viewerId = tryGetViewerId(request);
        List<Post> list = postService.list(page, pageSize, sortBy, order, keyword, viewerId);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id, HttpServletRequest request) {
        Long viewerId = tryGetViewerId(request);
        Post post = postService.getById(id, viewerId);
        if (post == null) return Result.error("帖子不存在或已删除");
        return Result.success(post);
    }

    @PostMapping
    public Result create(@RequestBody Map<String, String> body, HttpServletRequest request) {
        Long userId = getLoginUserId(request);
        String title = body.get("title");
        String content = body.get("content");
        if (title == null || title.isBlank() || content == null || content.isBlank()) {
            return Result.error("标题和内容不能为空");
        }
        Long id = postService.create(userId, title, content);
        return Result.success(id);
    }

    @GetMapping("/{id}/comments")
    public Result comments(@PathVariable Long id) {
        return Result.success(commentService.listTree(id));
    }

    @PostMapping("/{id}/like/toggle")
    public Result toggleLike(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getLoginUserId(request);
        return Result.success(postService.toggleLike(id, userId));
    }

    @PostMapping("/{id}/favorite/toggle")
    public Result toggleFavorite(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getLoginUserId(request);
        return Result.success(postService.toggleFavorite(id, userId));
    }
}
