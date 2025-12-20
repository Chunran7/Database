package com.chun.back.service;

import com.chun.back.pojo.Post;
import java.util.List;

public interface PostService {
    /**
     * 根据ID获取帖子
     * @param id 帖子ID
     * @return 帖子对象
     */
    Post getPostById(Long id);
    
    /**
     * 获取所有帖子
     * @return 帖子列表
     */
    List<Post> getAllPosts(int page, int pageSize, String sortBy, String order);
    
    /**
     * 创建新帖子
     * @param post 帖子对象
     * @return 是否创建成功
     */
    boolean createPost(Post post);
}