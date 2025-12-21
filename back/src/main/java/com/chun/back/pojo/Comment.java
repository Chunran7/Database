package com.chun.back.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private Long id;
    private Long parentId;
    private Long rootId;
    private Long postId;
    private Long userId;
    private Long replyUserId;
    private Integer isDeleted;
    private String content;
    private LocalDateTime createTime;
}