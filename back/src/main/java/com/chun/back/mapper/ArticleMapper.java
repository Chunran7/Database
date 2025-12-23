package com.chun.back.mapper;

import com.chun.back.pojo.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {

    @Select("""
        SELECT a.id, a.user_id, a.title, a.first_picture, a.description, a.content,
               a.views, a.like_count, a.is_deleted, a.create_time, a.update_time,
               COALESCE(u.nickname, u.username) AS author,
               u.user_pic AS author_pic
        FROM article a
        LEFT JOIN users u ON a.user_id = u.id
        WHERE a.id = #{id} AND a.is_deleted = 0
        """)
    Article selectByIdWithAuthor(Long id);

    @Select("""
        SELECT a.id, a.user_id, a.title, a.first_picture, a.description, a.content,
               a.views, a.like_count, a.is_deleted, a.create_time, a.update_time,
               COALESCE(u.nickname, u.username) AS author,
               u.user_pic AS author_pic
        FROM article a
        LEFT JOIN users u ON a.user_id = u.id
        WHERE a.is_deleted = 0
          AND (#{keyword} IS NULL OR #{keyword} = '' OR a.title LIKE CONCAT('%', #{keyword}, '%') OR a.description LIKE CONCAT('%', #{keyword}, '%'))
        ORDER BY ${sortBy} ${order}
        LIMIT #{pageSize} OFFSET #{offset}
        """)
    List<Article> list(int pageSize, int offset, String sortBy, String order, String keyword);

    @Select("""
        SELECT a.id, a.user_id, a.title, a.first_picture, a.description, a.content,
               a.views, a.like_count, a.is_deleted, a.create_time, a.update_time,
               COALESCE(u.nickname, u.username) AS author,
               u.user_pic AS author_pic
        FROM article a
        LEFT JOIN users u ON a.user_id = u.id
        WHERE a.is_deleted = 0
        ORDER BY a.create_time DESC
        LIMIT #{count}
        """)
    List<Article> latest(Integer count);

    @Insert("INSERT INTO article(user_id, title, first_picture, description, content, views, like_count, is_deleted, create_time, update_time) " +
            "VALUES(#{userId}, #{title}, #{firstPicture}, #{description}, #{content}, 0, 0, 0, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Article article);

    @Update("UPDATE article SET views = views + 1 WHERE id = #{articleId} AND is_deleted=0")
    int incViews(Long articleId);

    @Update("UPDATE article SET like_count = #{likeCount} WHERE id = #{articleId} AND is_deleted=0")
    int updateLikeCount(Long articleId, Integer likeCount);
}
