package com.chun.back.controller;

import com.chun.back.pojo.Article;
import com.chun.back.pojo.Result;
import com.chun.back.service.ArticleService;
import com.chun.back.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

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
        List<Article> list = articleService.list(page, pageSize, sortBy, order, keyword, viewerId);
        return Result.success(list);
    }

    @GetMapping("/latest")
    public Result latest(@RequestParam(defaultValue = "3") int count, HttpServletRequest request) {
        Long viewerId = tryGetViewerId(request);
        return Result.success(articleService.latest(count, viewerId));
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id, HttpServletRequest request) {
        Long viewerId = tryGetViewerId(request);
        Article a = articleService.getById(id, viewerId);
        if (a == null) return Result.error("文章不存在或已删除");
        return Result.success(a);
    }

    @PostMapping
    public Result create(@RequestBody Article article, HttpServletRequest request) {
        Long userId = getLoginUserId(request);
        article.setUserId(userId);
        Long id = articleService.create(article);
        return Result.success(id);
    }

    @PostMapping("/{id}/like/toggle")
    public Result toggleLike(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getLoginUserId(request);
        return Result.success(articleService.toggleLike(id, userId));
    }

    @PostMapping("/{id}/favorite/toggle")
    public Result toggleFavorite(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getLoginUserId(request);
        return Result.success(articleService.toggleFavorite(id, userId));
    }
}
