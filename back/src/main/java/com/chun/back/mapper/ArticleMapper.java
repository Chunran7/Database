package com.chun.back.mapper;

import com.chun.back.pojo.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

import java.util.List;

@Mapper
public interface ArticleMapper {
    @Select("SELECT * FROM article WHERE id = #{id}")
    Article getArticleById(Long id);

    @Select("SELECT * FROM article")
    List<Article> getArticleList();

    @Select("SELECT * FROM article ORDER BY create_time DESC LIMIT #{count}")
    List<Article> getLatestArticleList(Integer count);

    // 插入文章，返回受影响行数
    @Insert("INSERT INTO article(title, first_picture, description, content, author, create_time, update_time, views, likes) VALUES(#{title}, #{firstPicture}, #{description}, #{content}, #{author}, #{createTime}, #{updateTime}, #{views}, #{likes})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertArticle(Article article);
}