package cn.itcast.goods.admin.admin.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.admin.admin.domain.Admin;
import cn.itcast.goods.admin.admin.service.AdminService;
import cn.itcast.servlet.BaseServlet;

public class AdminServlet extends BaseServlet {
	private AdminService adminService = new AdminService();
	
	public String login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Admin formAdmin = CommonUtils.toBean(req.getParameterMap(), Admin.class);
		Admin admin = adminService.login(formAdmin);
		if(admin == null){
			req.setAttribute("code", "error");
			req.setAttribute("msg", "账号不存在");
			return "f:/adminjsps/login.jsp";
		}else{
			req.getSession().setAttribute("sessionAdmin", admin);
			Cookie cookie =new Cookie("adminName", admin.getAdminName());
			cookie.setMaxAge(60 * 60 * 24 * 10);
			resp.addCookie(cookie);
			return "r:/adminjsps/admin/index.jsp";
		}		
	}
}
