package com.chun.back.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chun.back.mapper.VideoMapper;
import com.chun.back.pojo.Video;
import com.chun.back.service.VideoService;

import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoMapper videoMapper;

    @Override
    public Video getVideoById(Long id) {
        return videoMapper.getVideoById(id);
    }

    @Override
    public List<Video> getVideoList() {
        return videoMapper.getVideoList();
    }

    @Override
    public List<Video> getLatestVideoList(Integer count) {
        return videoMapper.getLatestVideoList(count);
    }

    @Override
    public int insertVideo(Video video) {
        return videoMapper.insertVideo(video);
    }
}
