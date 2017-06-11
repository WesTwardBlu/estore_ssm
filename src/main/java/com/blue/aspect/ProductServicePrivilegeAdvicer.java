package com.blue.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import com.blue.annotation.PrivilegeInfo;
import com.blue.bean.User;
import com.blue.dao.PrivilegeDao;
import com.blue.exception.PrivilegeException;

//权限切面
@Aspect
public class ProductServicePrivilegeAdvicer {
	@Autowired
	PrivilegeDao pdao;
	
	//定义切入点 带User参数的方法才会切入，才需要验证权限
	@Pointcut("execution(* com.blue.service.impl.ProductServiceImpl.*(com.blue.bean.User,..))")
	private void allMethod(){}
	
	//定义权限检查，采用前置通知
	@Before("allMethod()")
	public void beforAdvice(JoinPoint joinPoint) throws PrivilegeException{
	  MethodSignature methodSignature	= (MethodSignature) joinPoint.getSignature();
	  Method method= methodSignature.getMethod();
	  Object[] args= joinPoint.getArgs();
	  
	// 1.判断是否有注解
		if (method.isAnnotationPresent(PrivilegeInfo.class)) {
			// 2。得到注册对象
			PrivilegeInfo pif = method
					.getAnnotation(PrivilegeInfo.class);
			// 3.得到权限名称
			String pname = pif.value();

			// 4.得到用户
			User user = (User) args[0];

			if (user == null) {
				throw new PrivilegeException(); // 抛出权限不足异常
			}
			// 5.根据用户查询是否具有权限
			if (!pdao.selectPrivilegesByRole(user.getRole()).contains(pname)) {
				throw new PrivilegeException(); // 抛出权限不足异常
			}

		}
	}
}
