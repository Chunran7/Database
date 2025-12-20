package com.chun.back.service;

import com.chun.back.pojo.User;

public interface UserService {

    
    User findByUserName(String username);

    void register(String username, String password);

}
