package com.chun.back.mapper;

import com.chun.back.pojo.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    // 兼容当前数据库中的 `user` 表：
    // - 表名：user（不是 users）
    // - 密码列：password（不是 password_hash）
    // - 无 status 列
    // 通过别名将 password 映射到 pojo 的 passwordHash 字段。
    @Select("SELECT id, username, password AS password_hash, nickname, email, user_pic, 1 AS status, create_time, update_time " +
            "FROM `user` WHERE username = #{username}")
    User findByUserName(String username);

    @Insert("INSERT INTO `user`(username, password, nickname, email, user_pic, create_time, update_time) " +
            "VALUES(#{username}, #{passwordHash}, NULL, NULL, NULL, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Select("SELECT id, username, nickname, email, user_pic, 1 AS status, create_time, update_time FROM `user` WHERE id = #{id}")
    User selectById(Long id);

    @Update("UPDATE `user` SET nickname=#{nickname}, email=#{email}, user_pic=#{userPic}, update_time=NOW() WHERE id=#{id}")
    int updateProfile(User user);

    @Select("SELECT COUNT(*) FROM user_follow WHERE followee_id = #{userId}")
    int followerCount(Long userId);

    @Select("SELECT COUNT(*) FROM user_follow WHERE follower_id = #{userId}")
    int followingCount(Long userId);

    @Select("SELECT COUNT(*) FROM user_follow WHERE follower_id = #{viewerId} AND followee_id = #{targetId}")
    int isFollowed(Long viewerId, Long targetId);
}
