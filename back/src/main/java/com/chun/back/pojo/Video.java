package com.chun.back.pojo;

import java.sql.Date;
import lombok.Data;

@Data
public class Video {
    private Long id;
    private String title;
    private String url;
    private String cover;
    private String description;
    private String author;
    private Date createTime;
    private Date updateTime;
    private Integer views;
}
