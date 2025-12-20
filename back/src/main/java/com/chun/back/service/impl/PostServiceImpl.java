package com.chun.back.service.impl;

import com.chun.back.mapper.PostMapper;
import com.chun.back.pojo.Post;
import com.chun.back.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;
    
    @Override
    public Post getPostById(Long id) {
        Post post = postMapper.getPostById(id);
        return post;
    }
    
    @Override
    public List<Post> getAllPosts(int page, int pageSize, String sortBy, String order) {
        // 不做验证，直接使用传入的参数（仅用于测试环境）
        int offset = (page - 1) * pageSize;
        List<Post> posts = postMapper.getAllPosts(pageSize, offset, sortBy, order);
        return posts;
    }
    
    @Override
    public boolean createPost(Post post) {
        // 设置创建时间和更新时间
        post.setCreateTime(LocalDateTime.now());
        post.setUpdateTime(LocalDateTime.now());
        // 默认点赞数和回复数为0
        if (post.getLikeCount() == null) post.setLikeCount(0);
        if (post.getReplyCount() == null) post.setReplyCount(0);
        
        int result = postMapper.insertPost(post);
        return result > 0;
    }
}