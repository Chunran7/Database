package com.chun.back.mapper;

import com.chun.back.pojo.Video;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface VideoMapper {

    @Select("""
        SELECT v.id, v.user_id, v.title, v.url, v.cover, v.description,
               v.views, v.like_count, v.is_deleted, v.create_time, v.update_time,
               COALESCE(u.nickname, u.username) AS author,
               u.user_pic AS author_pic
        FROM video v
        LEFT JOIN `user` u ON v.user_id = u.id
        WHERE v.id = #{id} AND v.is_deleted = 0
        """)
    Video selectByIdWithAuthor(Long id);

    @Select("""
        SELECT v.id, v.user_id, v.title, v.url, v.cover, v.description,
               v.views, v.like_count, v.is_deleted, v.create_time, v.update_time,
               COALESCE(u.nickname, u.username) AS author,
               u.user_pic AS author_pic
        FROM video v
        LEFT JOIN `user` u ON v.user_id = u.id
        WHERE v.is_deleted = 0
          AND (#{keyword} IS NULL OR #{keyword} = '' OR v.title LIKE CONCAT('%', #{keyword}, '%') OR v.description LIKE CONCAT('%', #{keyword}, '%'))
        ORDER BY ${sortBy} ${order}
        LIMIT #{pageSize} OFFSET #{offset}
        """)
    List<Video> list(int pageSize, int offset, String sortBy, String order, String keyword);

    @Select("""
        SELECT v.id, v.user_id, v.title, v.url, v.cover, v.description,
               v.views, v.like_count, v.is_deleted, v.create_time, v.update_time,
               COALESCE(u.nickname, u.username) AS author,
               u.user_pic AS author_pic
        FROM video v
        LEFT JOIN `user` u ON v.user_id = u.id
        WHERE v.is_deleted = 0
        ORDER BY v.create_time DESC
        LIMIT #{count}
        """)
    List<Video> latest(Integer count);

    @Insert("INSERT INTO video(user_id, title, url, cover, description, views, like_count, is_deleted, create_time, update_time) " +
            "VALUES(#{userId}, #{title}, #{url}, #{cover}, #{description}, 0, 0, 0, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Video video);

    @Update("UPDATE video SET views = views + 1 WHERE id = #{videoId} AND is_deleted=0")
    int incViews(Long videoId);

    @Update("UPDATE video SET like_count = #{likeCount} WHERE id = #{videoId} AND is_deleted=0")
    int updateLikeCount(Long videoId, Integer likeCount);

    // ------------------- 我的点赞/收藏 -------------------

    @Select("""
        SELECT v.id, v.user_id, v.title, v.url, v.cover, v.description,
               v.views, v.like_count, v.is_deleted, v.create_time, v.update_time,
               COALESCE(u.nickname, u.username) AS author,
               u.user_pic AS author_pic
        FROM video_like vl
        JOIN video v ON vl.video_id = v.id
        LEFT JOIN `user` u ON v.user_id = u.id
        WHERE vl.user_id = #{userId} AND v.is_deleted = 0
        ORDER BY vl.create_time DESC, vl.id DESC
        """)
    List<Video> listLikedByUser(Long userId);

    @Select("""
        SELECT v.id, v.user_id, v.title, v.url, v.cover, v.description,
               v.views, v.like_count, v.is_deleted, v.create_time, v.update_time,
               COALESCE(u.nickname, u.username) AS author,
               u.user_pic AS author_pic
        FROM video_favorite vf
        JOIN video v ON vf.video_id = v.id
        LEFT JOIN `user` u ON v.user_id = u.id
        WHERE vf.user_id = #{userId} AND v.is_deleted = 0
        ORDER BY vf.create_time DESC, vf.id DESC
        """)
    List<Video> listFavoritedByUser(Long userId);
}
