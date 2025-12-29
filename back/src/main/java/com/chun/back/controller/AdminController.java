package com.chun.back.controller;

import com.chun.back.mapper.AdminApplyMapper;
import com.chun.back.mapper.AdminMapper;
import com.chun.back.pojo.Admin;
import com.chun.back.pojo.AdminApply;
import com.chun.back.pojo.Result;
import com.chun.back.utils.JwtUtil;
import com.chun.back.utils.Md5Util;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminMapper adminMapper;

    // ① bootstrap 注册：仅当 admin 表为空时允许
    @PostMapping("/register")
    public Result register(@RequestBody Map<String, String> body) {
        if (adminMapper.countAdmins() > 0) {
            return Result.error("管理员注册已关闭，请联系管理员创建账号");
        }
        return doCreateAdmin(body, true);
    }

    // ② 管理员登录：返回 token（后续拦截器鉴权用）
    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> body, HttpServletRequest request) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return Result.error("用户名或密码不能为空");
        }

        Admin a = adminMapper.findByUsername(username.trim());
        if (a == null) return Result.error("用户名不存在");
        if (a.getStatus() != null && a.getStatus() == 0) return Result.error("账号已被停用");

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

    // ③ 创建其他管理员（需要管理员登录后才能调用）
    @PostMapping("/admins")
    public Result createAdmin(@RequestBody Map<String, String> body) {
        return doCreateAdmin(body, false);
    }

    private Result doCreateAdmin(Map<String, String> body, boolean bootstrap) {
        String username = body.get("username");
        String password = body.get("password");
        String nickname = body.get("nickname");
        String email = body.get("email");

        if (username == null || username.isBlank()) return Result.error("用户名不能为空");
        if (password == null || password.isBlank()) return Result.error("密码不能为空");
        username = username.trim();

        if (username.length() < 3 || username.length() > 50) return Result.error("用户名长度需在3~50之间");
        if (password.length() < 6) return Result.error("密码至少6位");

        if (adminMapper.findByUsername(username) != null) return Result.error("用户名已存在");

        Admin a = new Admin();
        a.setUsername(username);
        a.setPasswordHash(Md5Util.getMD5String(password));
        a.setNickname(nickname);
        a.setEmail(email);
        a.setStatus(1);

        int rows = adminMapper.insert(a);
        if (rows <= 0) return Result.error("创建失败");

        // bootstrap 可以直接提示创建成功；是否自动返回 token 也行（这里简单返回 id）
        return Result.success(Map.of("id", a.getId(), "bootstrap", bootstrap));
    }

    @Autowired
    private AdminApplyMapper adminApplyMapper;

    @PostMapping("/apply")
    public Result apply(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return Result.error("用户名/密码不能为空");
        }

        username = username.trim();

        // 如果已经是管理员，不允许申请
        if (adminMapper.findByUsername(username) != null) {
            return Result.error("该用户名已是管理员");
        }

        // 已提交过申请
        if (adminApplyMapper.findByUsername(username) != null) {
            return Result.error("已提交申请，请等待审核");
        }

        AdminApply a = new AdminApply();
        a.setUsername(username);
        a.setPasswordHash(com.chun.back.utils.Md5Util.getMD5String(password));
        a.setNickname(body.get("nickname"));
        a.setEmail(body.get("email"));
        a.setReason(body.get("reason"));

        adminApplyMapper.insert(a);
        return Result.success("申请已提交，等待初始管理员审核");
    }
    @GetMapping("/apply/list")
    public Result listApply(@RequestParam(defaultValue = "0") int status,
                            HttpServletRequest request) {
        assertRootAdmin(request);
        return Result.success(adminApplyMapper.listByStatus(status));
    }

    @org.springframework.transaction.annotation.Transactional
    @PostMapping("/apply/{id}/approve")
    public Result approve(@PathVariable Long id, HttpServletRequest request) {
        Long reviewerId = assertRootAdmin(request);

        AdminApply a = adminApplyMapper.findById(id);
        if (a == null) return Result.error("申请不存在");
        if (a.getStatus() != null && a.getStatus() != 0) return Result.error("该申请已处理");

        // 再次校验：防止并发或重复
        if (adminMapper.findByUsername(a.getUsername()) != null) {
            adminApplyMapper.review(id, 2, reviewerId, "已存在同名管理员，自动拒绝");
            return Result.error("已存在同名管理员");
        }

        // 创建管理员
        Admin admin = new Admin();
        admin.setUsername(a.getUsername());
        admin.setPasswordHash(a.getPasswordHash());
        admin.setNickname(a.getNickname());
        admin.setEmail(a.getEmail());
        admin.setStatus(1);
        admin.setIsRoot(0);
        adminMapper.insert(admin);

        // 更新申请状态
        adminApplyMapper.review(id, 1, reviewerId, "通过");
        return Result.success("已通过并创建管理员账号");
    }
    @PostMapping("/apply/{id}/reject")
    public Result reject(@PathVariable Long id,
                         @RequestBody Map<String, String> body,
                         HttpServletRequest request) {
        Long reviewerId = assertRootAdmin(request);

        AdminApply a = adminApplyMapper.findById(id);
        if (a == null) return Result.error("申请不存在");
        if (a.getStatus() != null && a.getStatus() != 0) return Result.error("该申请已处理");

        String remark = body.getOrDefault("remark", "拒绝");
        adminApplyMapper.review(id, 2, reviewerId, remark);
        return Result.success("已拒绝");
    }

}
