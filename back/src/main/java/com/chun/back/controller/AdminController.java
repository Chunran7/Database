package com.chun.back.controller;

import com.chun.back.mapper.AdminMapper;
import com.chun.back.mapper.UserMapper;
import com.chun.back.pojo.Admin;
import com.chun.back.pojo.Result;
import com.chun.back.utils.JwtUtil;
import com.chun.back.utils.Md5Util;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import com.chun.back.mapper.ArticleMapper;
import com.chun.back.mapper.VideoMapper;
import com.chun.back.mapper.PostMapper;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private PostMapper postMapper;

    // ============================
    // 1) 管理员账号：登录
    // ============================

    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> body, HttpServletRequest request) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return Result.error("用户名或密码不能为空");
        }
        username = username.trim();

        Admin a = adminMapper.findByUsername(username);
        if (a == null)
            return Result.error("用户名不存在");
        if (a.getStatus() != null && a.getStatus() == 0)
            return Result.error("账号已被停用");

        String md5 = Md5Util.getMD5String(password);
        if (a.getPasswordHash() == null || !a.getPasswordHash().equals(md5)) {
            return Result.error("密码错误");
        }

        adminMapper.updateLoginInfo(a.getId(), request.getRemoteAddr());

        Map<String, Object> claims = new HashMap<>();
        claims.put("adminId", a.getId());
        claims.put("username", a.getUsername());
        claims.put("type", "admin");

        String token = JwtUtil.genToken(claims);
        return Result.success(token);
    }

    @PostMapping("/admins")
    public Result createAdmin(@RequestBody Map<String, String> body, HttpServletRequest request) {
        assertAdminLogin(request); // 任何管理员都可以创建其他管理员
        return doCreateAdmin(body);
    }

    private Result doCreateAdmin(Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String nickname = body.get("nickname");
        String email = body.get("email");

        if (username == null || username.isBlank())
            return Result.error("用户名不能为空");
        if (password == null || password.isBlank())
            return Result.error("密码不能为空");
        username = username.trim();

        if (username.length() < 3 || username.length() > 50)
            return Result.error("用户名长度需在3~50之间");
        if (password.length() < 6)
            return Result.error("密码至少6位");

        if (adminMapper.findByUsername(username) != null)
            return Result.error("用户名已存在");

        Admin a = new Admin();
        a.setUsername(username);
        a.setPasswordHash(Md5Util.getMD5String(password));
        a.setNickname(nickname);
        a.setEmail(email);
        a.setStatus(1);

        int rows = adminMapper.insert(a);
        if (rows <= 0)
            return Result.error("创建失败");

        return Result.success(Map.of("id", a.getId()));
    }

    // ============================
    // 3) 管理员功能（示例）
    // ============================

    @GetMapping("/stats")
    public Result stats(HttpServletRequest request) {
        // 只要是管理员登录就能看数据看板（不要求 root）
        assertAdminLogin(request);

        Map<String, Object> data = new HashMap<>();
        data.put("userCount", userMapper.countAll());
        data.put("articleCount", articleMapper.countNotDeleted());
        data.put("videoCount", videoMapper.countNotDeleted());
        data.put("postCount", postMapper.countNotDeleted());
        return Result.success(data);
    }

    @GetMapping("/users")
    public Result users() {
        return Result.success(userMapper.listAll());
    }

    @PutMapping("/users/{id}/status")
    public Result updateUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        if (status == null || (status != 0 && status != 1))
            return Result.error("status 只能是 0/1");
        int rows = userMapper.updateStatus(id, status);
        return rows > 0 ? Result.success() : Result.error("更新失败");
    }

    // ============================
    // helper：鉴权 & 初始管理员判定
    // ============================

    private Long getLoginAdminId(HttpServletRequest request) {
        Object obj = request.getAttribute("adminClaims");
        if (obj instanceof Map<?, ?> m) {
            Object id = m.get("adminId");
            if (id != null)
                return Long.valueOf(id.toString());
        }

        // 兜底：从 header 解析（即使没配拦截器也能用）
        String auth = request.getHeader("Authorization");
        if (auth == null || auth.isBlank())
            return null;
        String token = auth.startsWith("Bearer ") ? auth.substring(7) : auth;
        try {
            Map<String, Object> claims = JwtUtil.parseToken(token);
            Object type = claims.get("type");
            if (type == null || !"admin".equals(type.toString()))
                return null;
            Object id = claims.get("adminId");
            return id == null ? null : Long.valueOf(id.toString());
        } catch (Exception e) {
            return null;
        }
    }

    private Long assertAdminLogin(HttpServletRequest request) {
        Long adminId = getLoginAdminId(request);
        if (adminId == null)
            throw new RuntimeException("需要管理员登录");
        return adminId;
    }

}
