package com.chun.back.mapper;

import com.chun.back.pojo.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("SELECT id, username, password_hash, nickname, email, user_pic, status, create_time, update_time FROM users WHERE username = #{username}")
    User findByUserName(String username);

    @Insert("INSERT INTO users(username, password_hash, nickname, email, user_pic, status, create_time, update_time) " +
            "VALUES(#{username}, #{passwordHash}, NULL, NULL, NULL, 1, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Select("SELECT id, username, nickname, email, user_pic, status, create_time, update_time FROM users WHERE id = #{id}")
    User selectById(Long id);

    @Update("UPDATE users SET nickname=#{nickname}, email=#{email}, user_pic=#{userPic}, update_time=NOW() WHERE id=#{id}")
    int updateProfile(User user);

    @Select("SELECT COUNT(*) FROM user_follow WHERE following_id = #{userId}")
    int followerCount(Long userId);

    @Select("SELECT COUNT(*) FROM user_follow WHERE follower_id = #{userId}")
    int followingCount(Long userId);

    @Select("SELECT COUNT(*) FROM user_follow WHERE follower_id = #{viewerId} AND following_id = #{targetId}")
    int isFollowed(Long viewerId, Long targetId);
}
