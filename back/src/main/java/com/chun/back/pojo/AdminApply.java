package com.chun.back.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminApply {
    private Long id;
    private String username;

    @JsonIgnore
    private String passwordHash; // 映射表字段 password

    private String nickname;
    private String email;
    private String reason;

    private Integer status; // 0待审核 1通过 2拒绝
    private Long reviewerAdminId;
    private String reviewRemark;
    private LocalDateTime reviewTime;
    private LocalDateTime createTime;
}
