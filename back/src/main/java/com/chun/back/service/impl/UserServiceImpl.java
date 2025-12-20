package com.chun.back.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chun.back.mapper.UserMapper;
import com.chun.back.pojo.User;
import com.chun.back.service.UserService;
import com.chun.back.utils.Md5Util;


@Service
public class UserServiceImpl implements UserService{

    @Autowired 
    private UserMapper userMapper;
    @Override
    public User findByUserName(String username) {
        User u=userMapper.findByUserName(username);
        return u;
    }

    @Override
    public void register(String username, String password) {
        //加密
        String md5String=Md5Util.getMD5String(password);
        userMapper.add(username,md5String);
    }


}
