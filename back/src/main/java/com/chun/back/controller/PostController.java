package com.chun.back.controller;

import com.chun.back.pojo.Post;
import com.chun.back.pojo.Result;
import com.chun.back.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * 发布新帖子
     * 
     * @param post 帖子信息
     * @return 发布结果
     */
    @PostMapping
    public Result createPost(@RequestBody Post post, HttpServletRequest request) {
        // 从拦截器中取出解析好的claims
        Object c = request.getAttribute("claims");
        if (c == null) {
            return Result.error("需要登录");
        }
        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> claims = (java.util.Map<String, Object>) c;
        // 设置当前登录用户id到帖子实体中
        Object idObj = claims.get("id");
        if (idObj instanceof Number) {
            post.setUserId(((Number) idObj).longValue());
        } else if (idObj instanceof String) {
            try {
                post.setUserId(Long.parseLong((String) idObj));
            } catch (NumberFormatException ignored) {
            }
        }

        boolean success = postService.createPost(post);
        if (success) {
            return Result.success("帖子发布成功");
        } else {
            return Result.error("帖子发布失败");
        }
    }

    /**
     * 根据ID查询帖子
     * 
     * @param id 帖子ID
     * @return 帖子信息
     */
    @GetMapping("/{id}")
    public Result getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        if (post != null) {
            return Result.success(post);
        } else {
            return Result.error("未找到帖子");
        }
    }

    /**
     * 查询所有帖子
     * 
     * @return 帖子列表
     */
    @GetMapping("/list")
    public Result getAllPosts(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "create_time") String sortBy,
            @RequestParam(defaultValue = "desc") String order) {
        List<Post> posts = postService.getAllPosts(page, pageSize, sortBy, order);
        return Result.success(posts);
    }
}