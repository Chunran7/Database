package com.chun.back.service.impl;

import com.chun.back.mapper.UserFollowMapper;
import com.chun.back.mapper.UserMapper;
import com.chun.back.pojo.User;
import com.chun.back.service.UserService;
import com.chun.back.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserFollowMapper userFollowMapper;

    @Override
    public User findByUserName(String username) {
        return userMapper.findByUserName(username);
    }

    @Override
    public void register(String username, String password) {
        String md5 = Md5Util.getMD5String(password);
        User u = new User();
        u.setUsername(username);
        u.setPasswordHash(md5);
        userMapper.insert(u);
    }

    @Override
    public User getMe(Long userId) {
        User u = userMapper.selectById(userId);
        if (u == null) return null;
        u.setFollowerCount(userMapper.followerCount(userId));
        u.setFollowingCount(userMapper.followingCount(userId));
        u.setFollowed(false);
        return u;
    }

    @Override
    public User getProfile(Long targetId, Long viewerId) {
        User u = userMapper.selectById(targetId);
        if (u == null) return null;
        u.setFollowerCount(userMapper.followerCount(targetId));
        u.setFollowingCount(userMapper.followingCount(targetId));
        if (viewerId != null) {
            u.setFollowed(userMapper.isFollowed(viewerId, targetId) > 0);
        } else {
            u.setFollowed(false);
        }
        return u;
    }

    @Override
    public void updateProfile(Long userId, String nickname, String email, String userPic) {
        User u = new User();
        u.setId(userId);
        u.setNickname(nickname);
        u.setEmail(email);
        u.setUserPic(userPic);
        userMapper.updateProfile(u);
    }

    @Override
    public User toggleFollow(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            return getProfile(followingId, followerId);
        }
        boolean existed = userFollowMapper.exists(followerId, followingId) > 0;
        if (existed) {
            userFollowMapper.delete(followerId, followingId);
        } else {
            userFollowMapper.insert(followerId, followingId);
        }
        return getProfile(followingId, followerId);
    }
}
