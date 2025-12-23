package com.chun.back.mapper;

import com.chun.back.pojo.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Select("""
        SELECT c.id, c.post_id, c.user_id, c.parent_id, c.root_id, c.reply_user_id,
               c.content, c.is_deleted, c.create_time,
               COALESCE(u.nickname, u.username) AS author,
               u.user_pic AS author_pic,
               COALESCE(ru.nickname, ru.username) AS reply_user_name
        FROM comment c
        LEFT JOIN users u ON c.user_id = u.id
        LEFT JOIN users ru ON c.reply_user_id = ru.id
        WHERE c.post_id = #{postId}
        ORDER BY c.create_time ASC, c.id ASC
        """)
    List<Comment> selectByPostId(Long postId);

    @Select("SELECT id, post_id, user_id, parent_id, root_id, reply_user_id, content, is_deleted, create_time FROM comment WHERE id=#{id}")
    Comment selectById(Long id);

    @Insert("INSERT INTO comment(post_id, user_id, parent_id, root_id, reply_user_id, content, is_deleted, create_time) " +
            "VALUES(#{postId}, #{userId}, #{parentId}, #{rootId}, #{replyUserId}, #{content}, 0, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Comment comment);

    @Update("UPDATE comment SET root_id=#{rootId} WHERE id=#{id}")
    int updateRootId(Long id, Long rootId);

    @Select("SELECT COUNT(*) FROM comment WHERE id=#{commentId} AND user_id=#{userId} AND is_deleted=0")
    int isOwnerAndNotDeleted(Long commentId, Long userId);

    @Update("UPDATE comment SET is_deleted=1 WHERE id=#{commentId} AND user_id=#{userId} AND is_deleted=0")
    int softDelete(Long commentId, Long userId);
}
