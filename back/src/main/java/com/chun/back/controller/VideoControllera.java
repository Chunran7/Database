package com.chun.back.controller;


import com.chun.back.pojo.Result;
import com.chun.back.pojo.Video;
import com.chun.back.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/video")
public class VideoControllera {


    @Autowired
    VideoService videoService;

    @RequestMapping("/list")
    public Result getVideoList(){
        List<Video> videoList =videoService.getVideoList();
        if(videoList.isEmpty()){
            return Result.error("没有视频");
        }else{
            return Result.success(videoList);
        }
    }

    @RequestMapping("/{id}")
    public Result getVideoById(@PathVariable Long id){
        Video video = videoService.getVideoById(id);
        if(video == null){
            return Result.error("没有此视频");
        }else{
            return Result.success(video);
        }
    }

    @RequestMapping("/latest")
    public Result getLatestVideoList(@RequestParam(defaultValue = "3") Integer count){
        List<Video> videoList = videoService.getLatestVideoList(count);
        if(videoList.isEmpty()){
            return Result.error("没有视频");
        }else{
            return Result.success(videoList);
        }
    }

}