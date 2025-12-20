package com.chun.back.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chun.back.mapper.ArticleMapper;
import com.chun.back.pojo.Article;
import com.chun.back.service.ArticleService;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    public Article getArticleById(Long id) {
        return articleMapper.getArticleById(id);
    }

    @Override
    public List<Article> getArticleList() {
        return articleMapper.getArticleList();
    }

    @Override
    public List<Article> getLatestArticleList(Integer count) {
        return articleMapper.getLatestArticleList(count);
    }

    @Override
    public int insertArticle(Article article) {
        return articleMapper.insertArticle(article);
    }

}