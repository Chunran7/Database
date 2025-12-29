package com.chun.back.mapper;

import com.chun.back.pojo.AdminApply;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AdminApplyMapper {

    @Select("SELECT 1 FROM admin_apply WHERE username = #{username} AND status = 0 LIMIT 1")
    Integer existsPendingUsername(String username);

    @Insert("INSERT INTO admin_apply(username, password, nickname, email, admin_pic, reason, status, create_time, update_time) " +
            "VALUES(#{username}, #{passwordHash}, #{nickname}, #{email}, #{adminPic}, #{reason}, 0, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AdminApply apply);

    @Select("SELECT id, username, password AS password_hash, nickname, email, admin_pic, reason, status, reviewer_id, remark, review_time, create_time, update_time " +
            "FROM admin_apply WHERE id = #{id} LIMIT 1")
    AdminApply findById(Long id);

    @Select("SELECT id, username, password AS password_hash, nickname, email, admin_pic, reason, status, reviewer_id, remark, review_time, create_time, update_time " +
            "FROM admin_apply WHERE status = #{status} ORDER BY create_time ASC")
    List<AdminApply> listByStatus(int status);

    @Update("UPDATE admin_apply SET status = #{status}, reviewer_id = #{reviewerId}, remark = #{remark}, review_time = NOW(), update_time = NOW() " +
            "WHERE id = #{id} AND status = 0")
    int review(@Param("id") Long id,
               @Param("status") int status,
               @Param("reviewerId") Long reviewerId,
               @Param("remark") String remark);
}
