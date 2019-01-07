package cn.tedu.store.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录拦截器
 * @author UID-ECD
 *
 */
public class LoginInterceptor implements HandlerInterceptor{
	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handle) throws Exception{
		//获取Session对象
		HttpSession session=request.getSession();
		//判断Session中是否存在id
		if(session.getAttribute("id")==null){
			//为null，即没有id，即没有登录
			response.sendRedirect("../web/login.html");
			//拦截
			return false;
		}else{
			//非null,即存在id,即已经登录
			return true;
		}
	}
}
