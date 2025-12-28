package com.chun.back.mapper;

import com.chun.back.pojo.Admin;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AdminMapper {

    // 用于“首次开放注册/bootstrapping”
    @Select("SELECT COUNT(*) FROM admin")
    long countAdmins();

    @Select("SELECT id, username, password AS password_hash, nickname, email, admin_pic, status, create_time, update_time " +
            "FROM admin WHERE username = #{username} LIMIT 1")
    Admin findByUsername(String username);

    @Insert("INSERT INTO admin(username, password, nickname, email, admin_pic, status, create_time, update_time) " +
            "VALUES(#{username}, #{passwordHash}, #{nickname}, #{email}, #{adminPic}, #{status}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Admin admin);

    @Update("UPDATE admin SET last_login_time = NOW(), last_login_ip = #{ip}, update_time = NOW() WHERE id = #{id}")
    int updateLoginInfo(Long id, String ip);
}
