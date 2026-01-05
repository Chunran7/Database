package com.chun.back.mapper;

import com.chun.back.pojo.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {

  @Select("""
      SELECT a.id, a.user_id, a.title, a.first_picture, a.description, a.content,
             a.views, a.like_count, a.is_deleted, a.create_time, a.update_time,
             CASE
               WHEN u.id IS NULL THEN '用户不存在'
               WHEN u.status = 0 THEN '账号已封禁'
               ELSE COALESCE(NULLIF(u.nickname,''), u.username)
             END AS author,
             CASE
               WHEN u.status = 0 THEN NULL
               ELSE u.user_pic
             END AS author_pic

      FROM article a
      LEFT JOIN `user` u ON a.user_id = u.id
      WHERE a.id = #{id} AND a.is_deleted = 0
      """)
  Article selectByIdWithAuthor(Long id);

  @Select("""
      SELECT a.id, a.user_id, a.title, a.first_picture, a.description, a.content,
             a.views, a.like_count, a.is_deleted, a.create_time, a.update_time,
             CASE
               WHEN u.id IS NULL THEN '用户不存在'
               WHEN u.status = 0 THEN '账号已封禁'
               ELSE COALESCE(NULLIF(u.nickname,''), u.username)
             END AS author,
             CASE
               WHEN u.status = 0 THEN NULL
               ELSE u.user_pic
             END AS author_pic

      FROM article a
      LEFT JOIN `user` u ON a.user_id = u.id
      WHERE a.is_deleted = 0
        AND (#{keyword} IS NULL OR #{keyword} = '' OR a.title LIKE CONCAT('%', #{keyword}, '%') OR a.description LIKE CONCAT('%', #{keyword}, '%'))
      ORDER BY ${sortBy} ${order}
      LIMIT #{pageSize} OFFSET #{offset}
      """)
  List<Article> list(int pageSize, int offset, String sortBy, String order, String keyword);

  @Select("""
      SELECT a.id, a.user_id, a.title, a.first_picture, a.description, a.content,
             a.views, a.like_count, a.is_deleted, a.create_time, a.update_time,
             CASE
               WHEN u.id IS NULL THEN '用户不存在'
               WHEN u.status = 0 THEN '账号已封禁'
               ELSE COALESCE(NULLIF(u.nickname,''), u.username)
             END AS author,
             CASE
               WHEN u.status = 0 THEN NULL
               ELSE u.user_pic
             END AS author_pic

      FROM article a
      LEFT JOIN `user` u ON a.user_id = u.id
      WHERE a.is_deleted = 0
      ORDER BY a.create_time DESC
      LIMIT #{count}
      """)
  List<Article> latest(Integer count);

  @Insert("INSERT INTO article(user_id, title, first_picture, description, content, views, like_count, is_deleted, create_time, update_time) "
      +
      "VALUES(#{userId}, #{title}, #{firstPicture}, #{description}, #{content}, 0, 0, 0, NOW(), NOW())")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  int insert(Article article);

  // 仅“查看/浏览”不应改变更新时间（避免 ON UPDATE 自动更新时间戳）
  @Update("UPDATE article SET views = views + 1, update_time = update_time WHERE id = #{articleId} AND is_deleted=0")
  int incViews(Long articleId);

  // 点赞数变化不应影响“编辑/更新时间”展示
  @Update("UPDATE article SET like_count = #{likeCount}, update_time = update_time WHERE id = #{articleId} AND is_deleted=0")
  int updateLikeCount(Long articleId, Integer likeCount);

  // ------------------- 我的点赞/收藏 -------------------

  @Select("""
      SELECT a.id, a.user_id, a.title, a.first_picture, a.description, a.content,
             a.views, a.like_count, a.is_deleted, a.create_time, a.update_time,
             CASE
               WHEN u.id IS NULL THEN '用户不存在'
               WHEN u.status = 0 THEN '账号已封禁'
               ELSE COALESCE(NULLIF(u.nickname,''), u.username)
             END AS author,
             CASE
               WHEN u.status = 0 THEN NULL
               ELSE u.user_pic
             END AS author_pic

      FROM article_like al
      JOIN article a ON al.article_id = a.id
      LEFT JOIN `user` u ON a.user_id = u.id
      WHERE al.user_id = #{userId} AND a.is_deleted = 0
      ORDER BY al.create_time DESC, al.id DESC
      """)
  List<Article> listLikedByUser(Long userId);

  @Select("""
      SELECT a.id, a.user_id, a.title, a.first_picture, a.description, a.content,
             a.views, a.like_count, a.is_deleted, a.create_time, a.update_time,
             CASE
               WHEN u.id IS NULL THEN '用户不存在'
               WHEN u.status = 0 THEN '账号已封禁'
               ELSE COALESCE(NULLIF(u.nickname,''), u.username)
             END AS author,
             CASE
               WHEN u.status = 0 THEN NULL
               ELSE u.user_pic
             END AS author_pic

      FROM article_favorite af
      JOIN article a ON af.article_id = a.id
      LEFT JOIN `user` u ON a.user_id = u.id
      WHERE af.user_id = #{userId} AND a.is_deleted = 0
      ORDER BY af.create_time DESC, af.id DESC
      """)
  List<Article> listFavoritedByUser(Long userId);

  // 管理后台统计
  @Select("SELECT COUNT(*) FROM article WHERE is_deleted = 0")
  long countNotDeleted();

  // 管理：设置 is_deleted 标记（0/1）
  @Update("UPDATE article SET is_deleted = #{isDeleted} WHERE id = #{articleId}")
  int updateIsDeleted(@Param("articleId") Long articleId, @Param("isDeleted") int isDeleted);

  // 管理员使用：支持包含已删除文章的列表（如果 includeDeleted = 1 则忽略 is_deleted 过滤）
  @Select({
      "<script>",
      "SELECT a.id, a.user_id, a.title, a.first_picture, a.description, a.content,",
      "       a.views, a.like_count, a.is_deleted, a.create_time, a.update_time,",
      "       CASE",
      "         WHEN u.id IS NULL THEN '用户不存在'",
      "         WHEN u.status = 0 THEN '账号已封禁'",
      "         ELSE COALESCE(NULLIF(u.nickname,''), u.username)",
      "       END AS author,",
      "       CASE",
      "         WHEN u.status = 0 THEN NULL",
      "         ELSE u.user_pic",
      "       END AS author_pic",
      "FROM article a",
      "LEFT JOIN `user` u ON a.user_id = u.id",
      "<where>",
      "  <if test='includeDeleted == null or includeDeleted == 0'>",
      "    AND a.is_deleted = 0",
      "  </if>",
      "  <if test=\"keyword != null and keyword != ''\">",
      "    AND (a.title LIKE CONCAT('%', #{keyword}, '%') OR a.description LIKE CONCAT('%', #{keyword}, '%'))",
      "  </if>",
      "</where>",
      "ORDER BY ${sortBy} ${order}",
      "LIMIT #{pageSize} OFFSET #{offset}",
      "</script>"
  })
  List<Article> listForAdmin(@Param("pageSize") int pageSize, @Param("offset") int offset,
      @Param("sortBy") String sortBy, @Param("order") String order,
      @Param("keyword") String keyword, @Param("includeDeleted") Integer includeDeleted);

  @Delete("DELETE FROM article WHERE id = #{articleId}")
  int hardDeleteById(@Param("articleId") Long articleId);

}
