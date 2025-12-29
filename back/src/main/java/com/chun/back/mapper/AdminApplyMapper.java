package com.chun.back.mapper;

import com.chun.back.pojo.AdminApply;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AdminApplyMapper {

    @Insert("""
        INSERT INTO admin_apply(username, password, nickname, email, reason, status)
        VALUES(#{username}, #{passwordHash}, #{nickname}, #{email}, #{reason}, 0)
    """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(AdminApply a);

    @Select("SELECT * FROM admin_apply WHERE username=#{username} LIMIT 1")
    @Results(id="AdminApplyMap", value={
            @Result(column="password", property="passwordHash"),
            @Result(column="reviewer_admin_id", property="reviewerAdminId"),
            @Result(column="review_remark", property="reviewRemark"),
            @Result(column="review_time", property="reviewTime"),
            @Result(column="create_time", property="createTime")
    })
    AdminApply findByUsername(@Param("username") String username);

    @Select("SELECT * FROM admin_apply WHERE id=#{id} LIMIT 1")
    @ResultMap("AdminApplyMap")
    AdminApply findById(@Param("id") Long id);

    @Select("SELECT * FROM admin_apply WHERE status=#{status} ORDER BY id DESC")
    @ResultMap("AdminApplyMap")
    List<AdminApply> listByStatus(@Param("status") int status);

    @Update("""
        UPDATE admin_apply
        SET status=#{status},
            reviewer_admin_id=#{reviewerAdminId},
            review_remark=#{reviewRemark},
            review_time=NOW()
        WHERE id=#{id}
    """)
    int review(@Param("id") Long id,
               @Param("status") int status,
               @Param("reviewerAdminId") Long reviewerAdminId,
               @Param("reviewRemark") String reviewRemark);


}
