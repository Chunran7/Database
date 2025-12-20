package com.chun.back.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chun.back.pojo.Result;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    // 简单账号密码校验，仅返回成功/失败
    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        if ("seu666".equals(username) && "yilutongxing".equals(password)) {
            return Result.success("登录成功");
        } else {
            return Result.error("用户名或密码错误");
        }
    }
}
