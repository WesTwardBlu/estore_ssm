package com.blue.service;

import com.blue.bean.User;
import com.blue.exception.LoginException;

public interface UserService {
	// 注册操作
		public void regist(User user) throws Exception;

		// 登录操作
		public User login(String username, String password) throws LoginException;

		// 激活操作
		public void activeUser(String activecode) throws Exception;
}	
