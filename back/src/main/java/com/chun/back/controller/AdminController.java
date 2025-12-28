package com.chun.back.controller;

import com.chun.back.mapper.AdminMapper;
import com.chun.back.mapper.UserMapper;
import com.chun.back.pojo.Admin;
import com.chun.back.pojo.Result;
import com.chun.back.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminMapper adminMapper;

    public AdminController(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        Admin a = adminMapper.findByUsername(username);
        if (a == null) return Result.error("用户名不存在");
        if (a.getStatus() != null && a.getStatus() == 0) return Result.error("账号已被停用");

        String md5 = Md5Util.getMD5String(password);
        if (a.getPasswordHash() == null || !a.getPasswordHash().equals(md5)) {
            return Result.error("密码错误");
        }

        return Result.success("登录成功");
    }

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/users")
    public Result users() {
        return Result.success(userMapper.listAll());
    }

    @PutMapping("/users/{id}/status")
    public Result updateUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        if (status == null || (status != 0 && status != 1)) return Result.error("status 只能是 0/1");
        int rows = userMapper.updateStatus(id, status);
        return rows > 0 ? Result.success() : Result.error("更新失败");
    }

}
