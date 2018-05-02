package cn.itcast.goods.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class LoginFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		Object user = req.getSession().getAttribute("sessionUser");
		if(user == null){
			req.setAttribute("code", "error");
			req.setAttribute("msg", "你还没有登陆");
			req.getRequestDispatcher("/jsps/msg.jsp").forward(req, response);
		}else{
			filter.doFilter(req, response);
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException { 
		// TODO Auto-generated method stub 

	}

}
