package com.chun.back.service;

import com.chun.back.pojo.User;

public interface UserService {

    User findByUserName(String username);

    void register(String username, String password);

    User getMe(Long userId);

    User getProfile(Long targetId, Long viewerId);

    void updateProfile(Long userId, String nickname, String email, String userPic);

    /**
     * 关注 / 取消关注
     * @return 更新后的 profile（followed + 计数）
     */
    User toggleFollow(Long followerId, Long followingId);
}
