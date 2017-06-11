package com.blue.dao;

import com.blue.bean.User;

public interface UserDao {

    //添加用户操作
    int addUser(User record);

    //根据激活码查找用户
    User findUserByActiveCode(String activecode);
    
    //根据用户名密码查找用户
    User findUserByUserNameAndPassword(String username, String password);

    //根据激活码激活用户
    int activeUserByActivecode(String activecode);
}