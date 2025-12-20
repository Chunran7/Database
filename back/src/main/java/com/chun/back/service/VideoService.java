package com.chun.back.service;

import com.chun.back.pojo.Video;

import java.util.List;

public interface VideoService {
    Video getVideoById(Long id);

    List<Video> getVideoList();

    List<Video> getLatestVideoList(Integer count);

    int insertVideo(Video video);
}
