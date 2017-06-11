package com.blue.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blue.bean.User;
import com.blue.dao.UserDao;
import com.blue.exception.ActiveUserException;
import com.blue.exception.LoginException;
import com.blue.exception.RegistException;
import com.blue.service.UserService;
import com.blue.utils.MailUtils;
import com.blue.utils.Md5Utils;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	// 注册功能
	//在service层对password进行md5加密
	@Transactional
	@Override
	public void regist(User user) throws Exception {
		user.setPassword(Md5Utils.md5(user.getPassword()));
		
		userDao.addUser(user);
		
		//发送邮件操作
		String emailMsg= "注册成功，请在12小时内<a href='http://localhost:8091/estore/user/activecode/" + user.getActivecode()+ "'>激活</a>，激活码是"+ user.getActivecode();
		
		MailUtils.sendMail(user.getEmail(), emailMsg);
		
		/*String msg= "注册成功，请在12小时内<a href='http://localhost:8091/estore/user/activecode/" + user.getActivecode()+ "'>激活</a>";
		MailUtils.sendMail("935105579@qq.com", msg);*/
	}

	@Override
	public User login(String username, String password) throws LoginException {
		User user= null;
		
		user = userDao.findUserByUserNameAndPassword(username, Md5Utils.md5(password));
		if (user==null) {
			throw new LoginException("用户名或密码错误！");
		}
		// 判断用户的状态。
		if (0== user.getState()) {
			// 用户状态未激活，不能进行登录操作
			throw new LoginException("用户未激活！！");
			
		}
		
		return user;
	}

	@Transactional
	@Override
	public void activeUser(String activecode) throws Exception {
		User user = null;
		user = userDao.findUserByActiveCode(activecode);
		
		if (user==null) {
			throw new RegistException("根据激活码查找用户失败");
		}
		
		long time= System.currentTimeMillis()- user.getUpdatetime().getTime();//单位：ms 毫秒
		// 判断它是否超时 开发是12小时，测试 1分钟
		if (time> 12*60*60*1000) {
			throw new ActiveUserException("激活码过期，请重新注册！");
		}
		
		// 进行激活操作
		userDao.activeUserByActivecode(activecode);
	}

}
