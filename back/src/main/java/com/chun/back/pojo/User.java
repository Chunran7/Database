package com.chun.back.pojo;

import java.time.LocalDateTime;

import lombok.Data;



@Data
public class User {
    private Integer id;          // 修改为Integer以匹配数据库INT类型
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String userPic;      // 对应user_pic字段
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}