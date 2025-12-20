package com.chun.back.mapper;

import com.chun.back.pojo.Post;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PostMapper {

    @Select("SELECT id, user_id, title, content, create_time, update_time, like_count, reply_count FROM post WHERE id = #{id}")
    Post getPostById(Long id);

    @Select("SELECT id, user_id, title, content, create_time, update_time, like_count, reply_count FROM post ORDER BY ${sortBy} ${order} LIMIT #{pageSize} OFFSET #{offset}")
    List<Post> getAllPosts(int pageSize, int offset, String sortBy, String order);
    
    @Insert("INSERT INTO post(user_id, title, content, create_time, update_time) VALUES(#{userId}, #{title}, #{content}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertPost(Post post);
}