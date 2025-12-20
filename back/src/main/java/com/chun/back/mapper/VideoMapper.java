package com.chun.back.mapper;

import com.chun.back.pojo.Video;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface VideoMapper {
    @Select("SELECT * FROM video WHERE id = #{id}")
    Video getVideoById(Long id);

    @Select("SELECT * FROM video")
    List<Video> getVideoList();

    @Select("SELECT * FROM video ORDER BY create_time DESC LIMIT #{count}")
    List<Video> getLatestVideoList(Integer count);

    @Insert("INSERT INTO video(title, url, cover, description, author, create_time, update_time, views) VALUES(#{title}, #{url}, #{cover}, #{description}, #{author}, #{createTime}, #{updateTime}, #{views})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertVideo(Video video);
}
