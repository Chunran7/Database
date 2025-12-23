package com.chun.back.controller;

import com.chun.back.pojo.Result;
import com.chun.back.pojo.Video;
import com.chun.back.service.VideoService;
import com.chun.back.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

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
        List<Video> list = videoService.list(page, pageSize, sortBy, order, keyword, viewerId);
        return Result.success(list);
    }

    @GetMapping("/latest")
    public Result latest(@RequestParam(defaultValue = "8") int count, HttpServletRequest request) {
        Long viewerId = tryGetViewerId(request);
        return Result.success(videoService.latest(count, viewerId));
    }

    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id, HttpServletRequest request) {
        Long viewerId = tryGetViewerId(request);
        Video v = videoService.getById(id, viewerId);
        if (v == null) return Result.error("视频不存在或已删除");
        return Result.success(v);
    }

    @PostMapping
    public Result create(@RequestBody Video video, HttpServletRequest request) {
        Long userId = getLoginUserId(request);
        video.setUserId(userId);
        Long id = videoService.create(video);
        return Result.success(id);
    }

    @PostMapping("/{id}/like/toggle")
    public Result toggleLike(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getLoginUserId(request);
        return Result.success(videoService.toggleLike(id, userId));
    }

    @PostMapping("/{id}/favorite/toggle")
    public Result toggleFavorite(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getLoginUserId(request);
        return Result.success(videoService.toggleFavorite(id, userId));
    }
}
