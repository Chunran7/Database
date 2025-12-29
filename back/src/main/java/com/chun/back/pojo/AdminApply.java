package com.chun.back.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminApply {
    private Long id;
    private String username;

    @JsonIgnore
    private String passwordHash; // 对应表里 password

    private String nickname;
    private String email;
    private String adminPic;
    private String reason;

    /** 0待审核 1通过 2拒绝 */
    private Integer status;

    private Long reviewerId;
    private String remark;
    private LocalDateTime reviewTime;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
