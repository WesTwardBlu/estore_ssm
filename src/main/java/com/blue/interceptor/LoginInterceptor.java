package com.blue.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.blue.bean.User;

/**
 * 用户登录拦截器
 * */
public class LoginInterceptor implements HandlerInterceptor {
	private static Log log= LogFactory.getLog(LoginInterceptor.class);

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * @return true:请求继续执行 false：请求终止
	 * */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object object) throws Exception {
		log.info(request.getRequestURI());
		HttpSession session= request.getSession();
		User user= (User) session.getAttribute("user");
		if (session==null || user== null) {
			log.info("----拦截器拦截，用户未登录----");
			request.getRequestDispatcher("WEB-INF/jsp/login.jsp").forward(request, response);
			return false;
		}
		return true;
	}

}
