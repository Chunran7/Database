package com.chun.back.pojo;

import java.time.LocalDateTime;

import lombok.Data;



@Data
public class User {
    private int id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String userPic;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}