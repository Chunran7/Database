package com.chun.back.mapper;

import com.chun.back.pojo.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostMapper {

    @Select("""
        SELECT p.id, p.user_id, p.title, p.content, p.views, p.like_count, p.reply_count, p.is_deleted, p.create_time, p.update_time,
               COALESCE(u.nickname, u.username) AS author,
               u.user_pic AS author_pic
        FROM post p
        LEFT JOIN `user` u ON p.user_id = u.id
        WHERE p.id = #{id} AND p.is_deleted = 0
        """)
    Post selectByIdWithAuthor(Long id);

    @Select("""
        SELECT p.id, p.user_id, p.title, p.content, p.views, p.like_count, p.reply_count, p.is_deleted, p.create_time, p.update_time,
               COALESCE(u.nickname, u.username) AS author,
               u.user_pic AS author_pic
        FROM post p
        LEFT JOIN `user` u ON p.user_id = u.id
        WHERE p.is_deleted = 0
          AND (#{keyword} IS NULL OR #{keyword} = '' OR p.title LIKE CONCAT('%', #{keyword}, '%') OR p.content LIKE CONCAT('%', #{keyword}, '%'))
        ORDER BY ${sortBy} ${order}
        LIMIT #{pageSize} OFFSET #{offset}
        """)
    List<Post> list(int pageSize, int offset, String sortBy, String order, String keyword);

    @Insert("INSERT INTO post(user_id, title, content, views, like_count, reply_count, is_deleted, create_time, update_time) " +
            "VALUES(#{userId}, #{title}, #{content}, 0, 0, 0, 0, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Post post);

    @Update("UPDATE post SET views = views + 1 WHERE id = #{postId} AND is_deleted=0")
    int incViews(Long postId);

    @Update("UPDATE post SET reply_count = reply_count + 1 WHERE id = #{postId} AND is_deleted=0")
    int incReplyCount(Long postId);

    @Update("UPDATE post SET reply_count = IF(reply_count>0, reply_count-1, 0) WHERE id = #{postId} AND is_deleted=0")
    int decReplyCount(Long postId);

    @Update("UPDATE post SET like_count = #{likeCount} WHERE id = #{postId} AND is_deleted=0")
    int updateLikeCount(Long postId, Integer likeCount);
}
