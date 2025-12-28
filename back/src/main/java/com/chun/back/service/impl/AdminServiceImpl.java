package com.chun.back.service.impl;

import com.chun.back.mapper.AdminMapper;
import com.chun.back.pojo.dto.AdminRegisterDTO;
import com.chun.back.pojo.Admin;
import com.chun.back.service.AdminService;
import com.chun.back.utils.Md5Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper;

    public AdminServiceImpl(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    @Override
    @Transactional
    public Long register(AdminRegisterDTO dto) {
        // 1) bootstrap 限制：只有当 admin 表为空才允许公开注册
        if (adminMapper.countAdmins() > 0) {
            throw new RuntimeException("管理员注册已关闭，请联系已有管理员创建账号");
        }

        // 2) 基本校验
        if (dto == null || !StringUtils.hasText(dto.getUsername()) || !StringUtils.hasText(dto.getPassword())) {
            throw new RuntimeException("用户名和密码不能为空");
        }
        if (dto.getUsername().length() < 3 || dto.getUsername().length() > 50) {
            throw new RuntimeException("用户名长度需在3~50之间");
        }
        if (dto.getPassword().length() < 6) {
            throw new RuntimeException("密码至少6位");
        }

        // 3) 唯一性校验
        if (adminMapper.findByUsername(dto.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 4) 入库
        Admin admin = new Admin();
        admin.setUsername(dto.getUsername());
        admin.setPasswordHash(Md5Util.getMD5String(dto.getPassword())); // ✅
        admin.setNickname(dto.getNickname());
        admin.setEmail(dto.getEmail());
        admin.setStatus(1);

        adminMapper.insert(admin);
        return admin.getId();
    }

    // 简单 MD5（如果你项目里已有 Md5Util，就用你自己的工具类替换掉）
    private String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("密码加密失败");
        }
    }
}
