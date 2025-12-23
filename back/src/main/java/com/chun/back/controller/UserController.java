package com.chun.back.controller;

import com.chun.back.pojo.Result;
import com.chun.back.pojo.User;
import com.chun.back.service.UserService;
import com.chun.back.utils.JwtUtil;
import com.chun.back.utils.Md5Util;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

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

    @PostMapping("/register")
    public Result register(@RequestParam String username, @RequestParam String password) {
        User exist = userService.findByUserName(username);
        if (exist != null) {
            return Result.error("用户名已存在");
        }
        userService.register(username, password);
        return Result.success();
    }

    @PostMapping("/login")
    public Result login(@RequestParam String username, @RequestParam String password) {
        User u = userService.findByUserName(username);
        if (u == null) {
            return Result.error("用户名不存在");
        }
        String md5 = Md5Util.getMD5String(password);
        if (u.getPasswordHash() == null || !u.getPasswordHash().equals(md5)) {
            return Result.error("密码错误");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", u.getId());
        claims.put("username", u.getUsername());
        String token = JwtUtil.genToken(claims);
        return Result.success(token);
    }

    @GetMapping("/me")
    public Result me(HttpServletRequest request) {
        Long userId = getLoginUserId(request);
        User me = userService.getMe(userId);
        if (me == null) return Result.error("用户不存在");
        return Result.success(me);
    }

    @GetMapping("/profile/{id}")
    public Result profile(@PathVariable Long id, HttpServletRequest request) {
        Long viewerId = tryGetViewerId(request);
        User profile = userService.getProfile(id, viewerId);
        if (profile == null) return Result.error("用户不存在");
        return Result.success(profile);
    }

    @PutMapping("/profile")
    public Result updateProfile(@RequestBody Map<String, String> body, HttpServletRequest request) {
        Long userId = getLoginUserId(request);
        String nickname = body.get("nickname");
        String email = body.get("email");
        String userPic = body.get("userPic");
        userService.updateProfile(userId, nickname, email, userPic);
        return Result.success();
    }

    @PostMapping("/follow/{id}/toggle")
    public Result toggleFollow(@PathVariable Long id, HttpServletRequest request) {
        Long followerId = getLoginUserId(request);
        User profile = userService.toggleFollow(followerId, id);
        return Result.success(profile);
    }
}
