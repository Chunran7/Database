package com.chun.back.pojo;

import java.sql.Date;
import lombok.Data;

@Data
public class Article {
    private Long id;
    private String title;
    private String firstPicture;
    private String description;
    private String content;
    private String author;
    private Date createTime;
    private Date updateTime;
    private Integer views;
    private Integer likes;
}