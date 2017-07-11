package com.test.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.test.controller.ManageController;

/**
 * 登录拦截器
 * 
 * @author Victory
 * 
 */
public class LoginInterceptor implements HandlerInterceptor {

	private static final Logger log = Logger.getLogger(HandlerInterceptor.class);

	/**
	 * 整个请求处理完毕回调方法
	 */
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {
		log.debug("afterCompletion");
	}

	/**
	 * 后处理回调方法
	 */
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {
		log.debug("postHandle");
	}

	/**
	 * 预处理回调方法
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		log.debug("preHandle");
		HttpSession session = request.getSession();
		if (session.getAttribute(ManageController.SESSION_ID) == null) {
			response.sendRedirect("../manage/login.html");
		}
		return true;
	}
}
