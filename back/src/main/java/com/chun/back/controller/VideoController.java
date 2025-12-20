package com.chun.back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.chun.back.pojo.Video;
import com.chun.back.pojo.Result;
import com.chun.back.service.VideoService;

import java.util.List;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping("/{id}")
    public Result getVideoById(@PathVariable Long id) {
        Video video = videoService.getVideoById(id);
        if (video != null) {
            return Result.success(video);
        } else {
            return Result.error("未找到视频");
        }
    }

    @GetMapping("/list")
    public Result getVideoList() {
        List<Video> videos = videoService.getVideoList();
        if (videos.isEmpty()) {
            return Result.error("没有视频");
        } else {
            return Result.success(videos);
        }
    }

    @GetMapping("/latest")
    public Result getLatestVideoList(@RequestParam(defaultValue = "8") Integer count) {
        List<Video> videos = videoService.getLatestVideoList(count);
        if (videos.isEmpty()) {
            return Result.error("没有视频");
        } else {
            return Result.success(videos);
        }
    }

    @PostMapping("")
    public Result createVideo(@RequestBody Video video) {
        java.sql.Date now = new java.sql.Date(System.currentTimeMillis());
        if (video.getCreateTime() == null)
            video.setCreateTime(now);
        video.setUpdateTime(now);
        int rows = videoService.insertVideo(video);
        if (rows > 0) {
            return Result.success("添加成功");
        } else {
            return Result.error("添加失败");
        }
    }
}
