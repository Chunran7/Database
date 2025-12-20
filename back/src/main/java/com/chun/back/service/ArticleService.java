package com.chun.back.service;

import com.chun.back.pojo.Article;

import java.util.List;

public interface ArticleService {
    Article getArticleById(Long id);

    List<Article> getArticleList();

    List<Article> getLatestArticleList(Integer count);

    // 插入文章
    int insertArticle(Article article);
}