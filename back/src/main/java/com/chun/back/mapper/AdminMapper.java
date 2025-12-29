package com.chun.back.mapper;

import com.chun.back.pojo.Admin;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminMapper {

    @Select("SELECT COUNT(*) FROM admin")
    long countAdmins();

    @Select("SELECT id, username, password AS password_hash, nickname, email, admin_pic, status, create_time, update_time " +
            "FROM admin WHERE username = #{username} LIMIT 1")
    Admin findByUsername(String username);

    @Select("SELECT id, username, password AS password_hash, nickname, email, admin_pic, status, create_time, update_time " +
            "FROM admin WHERE id = #{id} LIMIT 1")
    Admin selectById(Long id);

    @Select("SELECT MIN(id) FROM admin")
    Long firstAdminId();

    @Insert("INSERT INTO admin(username, password, nickname, email, admin_pic, status, create_time, update_time) " +
            "VALUES(#{username}, #{passwordHash}, #{nickname}, #{email}, #{adminPic}, #{status}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Admin admin);

    @Update("UPDATE admin SET last_login_time = NOW(), last_login_ip = #{ip}, update_time = NOW() WHERE id = #{id}")
    int updateLoginInfo(@Param("id") Long id, @Param("ip") String ip);
    @Select("SELECT id, status FROM admin WHERE id = #{id} LIMIT 1")
    Admin selectBasicById(Long id);

}
