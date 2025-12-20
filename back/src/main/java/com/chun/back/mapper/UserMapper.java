package com.chun.back.mapper;

import com.chun.back.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;



@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUserName(String username);

    @Insert("INSERT INTO user (username, password, create_time, update_time) VALUES (#{username}, #{md5String}, NOW(), NOW())")
    void add(String username, String md5String);

}