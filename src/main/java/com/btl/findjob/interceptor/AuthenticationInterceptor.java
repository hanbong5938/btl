package com.btl.findjob.interceptor;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.btl.findjob.model.SessionListener;
import com.btl.findjob.service.UserService;


public class AuthenticationInterceptor extends HandlerInterceptorAdapter{
	

	
@Autowired
UserService userservice;

	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
			System.out.println("XMLHttpRequest".equals(request.getHeader("x-requested-with")));
			HttpSession session = request.getSession();
			Object obj = session.getAttribute("user");
			String grade = (String) session.getAttribute("grade");
		
			RequestDispatcher logininterceptor = request.getRequestDispatcher("/logininterceptor");
			RequestDispatcher gradeceptor = request.getRequestDispatcher("/gradeceptor");
			//로그인이 안되어있을경우.. 컨트롤러 요청으로 가지 않도록 false 반환
			if(obj==null) {
				if("XMLHttpRequest".equals(request.getHeader("x-requested-with"))) {
					response.sendError(403);
					return false;
				}
				logininterceptor.forward(request,response);
				return false; // 컨트롤러 uri로의 요청으로 가지않도록 false
			}
			else if(grade.equals("5")) {
				gradeceptor.forward(request,response);
				return false;
			}
			return true; // 요청 true
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		super.postHandle(request, response, handler, modelAndView);
	}

}
