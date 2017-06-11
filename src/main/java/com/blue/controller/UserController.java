package com.blue.controller;

import java.net.URLEncoder;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blue.bean.User;
import com.blue.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Resource(name="userService")
	private UserService userService;
	
	@RequestMapping("/preregist")
	public  String preRegist(HttpServletRequest request){
		return "regist";
	}
	//注册操作
	@RequestMapping(value="/regist",method= RequestMethod.POST)
	public String regist( @ModelAttribute @Valid User user,BindingResult bindingResult,
			@RequestParam String checkcode,HttpSession session,Model model){//里面的参数自动装入
		// 验证码操作
		String _checkcode= (String) session.getAttribute("checkcode_session");
		session.removeAttribute("checkcode_session");//清除缓存，防止下次改变验证码的缓存
		if (checkcode==null || !checkcode.equals(_checkcode)) {
			model.addAttribute("regist.message", "验证码不正确");
			bindingResult.reject("regist.message", "验证码不正确");
			return "regist";
		}
		if (bindingResult.hasErrors()) {
			return "regist";
		}
		// 手动封装一个激活码
		user.setActivecode(UUID.randomUUID().toString());
		try {
			userService.regist(user);
			return "redirect:/user/toregist";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "regist";
		}
	}
	
	@RequestMapping("/toregist")
	public @ResponseBody String toRegist(HttpServletRequest request){
		return "注册成功，激活后请<a href='" + request.getContextPath()
							+ "/index'>登录</a>";
	}
	
	@RequestMapping("/login")
	public String login( @RequestParam(value="username",defaultValue="") String username,@RequestParam(value="password",defaultValue="") String password,String remember,
			HttpServletResponse response,HttpServletRequest request,HttpSession session){
		// 1.调用service中登录方法
		try {
			User user = userService.login(username, password);
			//记住用户名,把用户名放到Cookie中，返回给浏览器
			if (null!= user) {
				if ("on".equals(remember)) {
					Cookie cookie= new Cookie("saveusername", URLEncoder.encode(username, "utf-8"));// 存储utf-8码
					cookie.setMaxAge(7*24*60*60);//单位：秒
					cookie.setPath("/");
					response.addCookie(cookie);
				}else{
					Cookie cookie= new Cookie("saveusername", URLEncoder.encode(username, "utf-8"));// 存储utf-8码
					cookie.setMaxAge(0);//单位：秒
					cookie.setPath("/");
					response.addCookie(cookie);
				}
				
				// 登录成功后，将用户存储到session中.
//				session.invalidate();
				session.removeAttribute("user");
				session.setAttribute("user", user);
				
				return "redirect:/index";
			}else {
				request.setAttribute("login.message", "用户名或密码错误");
				return "page";
			} 
			
			
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("login.message", e.getMessage());
			return "page";
		}
	}
	
	// 注销操作
	@RequestMapping("/logout")
	public String logout(HttpSession session){
		session.invalidate();
		return "redirect:/index";
	}
	
	// 激活用户操作
	@RequestMapping("/activecode/{activecode}")
	public @ResponseBody String activecode(@PathVariable(value="activecode") String activecode,HttpServletRequest request){
		try {
			userService.activeUser(activecode);
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage() + ",重新<a href='" + request.getContextPath()+ "/WEB-INF/jsp/regist.jsp'>注册</a>";
			
		}
		return "用户激活成功,请<a href='" + request.getContextPath() + "/index'>回首页</a>";
	}
	
}
