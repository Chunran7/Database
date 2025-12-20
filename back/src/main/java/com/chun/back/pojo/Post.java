package com.chun.back.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Post {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer likeCount;  // 修改字段名以匹配数据库
    private Integer replyCount; // 修改字段名以匹配数据库
}