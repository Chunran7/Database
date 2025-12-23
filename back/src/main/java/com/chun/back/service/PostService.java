package com.chun.back.service;

import com.chun.back.pojo.Post;

import java.util.List;
import java.util.Map;

public interface PostService {

    List<Post> list(int page, int pageSize, String sortBy, String order, String keyword, Long viewerId);

    Post getById(Long postId, Long viewerId);

    Long create(Long userId, String title, String content);

    Map<String, Object> toggleLike(Long postId, Long userId);

    Map<String, Object> toggleFavorite(Long postId, Long userId);
}
