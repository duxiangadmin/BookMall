package cn.itcast.goods.user.web.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.user.domain.User;
import cn.itcast.goods.user.service.UserService;
import cn.itcast.goods.user.service.exception.UserException;
import cn.itcast.servlet.BaseServlet;
import jdk.nashorn.internal.ir.RuntimeNode.Request;

/**
 * userweb层
 * @author Administrator
 *
 */
public class UserServlet extends BaseServlet {
	private UserService userService = new UserService();
	
	/**
	 * ajax校验账号是否存在
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxValiDateLoginname(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String loginname =request.getParameter("loginname"); 
		boolean bool = userService.ajaxValidateLoginname(loginname);
		response.getWriter().print(bool);
		return null;
	}
	
	/**
	 * ajax校验邮箱是否存在
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxValiDateEmail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
//		
		boolean bool = userService.ajaxValidateEmail(email);
		response.getWriter().print(bool);
		return null;
	}
	
	/**
	 * ajax校验验证码
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxValiDateVerifyCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		得到表单中的验证码
		String verifyCode = request.getParameter("verifyCode");
//		得到session中保存的真实验证码
		String vCode = (String) request.getSession().getAttribute("vCode");
//		忽略大小写进行比较
		boolean bool = verifyCode.equalsIgnoreCase(vCode);
//		向ajax发送回馈信息
		response.getWriter().print(bool);
		
		return null;
	}
	
	
	/**
	 * ajax校验密码
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void ajaxValidateLoginpass(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		获取表单中的密码
		String loginpass = request.getParameter("loginpass");
//		获取原session种保存用户的信息
		User user = (User) request.getSession().getAttribute("sessionUser");
		
//		System.out.println(loginpass+"--"+user.getLoginpass());
//		如果密码
		if(loginpass.equals(user.getLoginpass())){
//			System.out.println(loginpass != user.getLoginpass());
			response.getWriter().print(true);
		}
	}
	
	

	/**
	 * 注册功能
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public String regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		封装表单数据到bean对象中
		
		User formUser = CommonUtils.toBean(request.getParameterMap(), User.class);
//		后台逐一校验表单所有信息
		Map<String, String> errors = validateRegist(formUser, request.getSession());
		if(errors.size() > 0 ){
			request.setAttribute("form", formUser);
			request.setAttribute("errors", errors);
			return "f:/jsps/user/regist.jsp";
		}
//		数据库插入数据
		userService.regist(formUser);
//		向前台转发信息
		request.setAttribute("code", "success");
		request.setAttribute("msg", "注册成功，请马上到邮箱激活！");
		return "f:/jsps/msg.jsp";
	}
	
	/**
	 * 逐一校验表单上的数据
	 * @param formUser
	 * @param session
	 * @return
	 */
	public Map<String, String> validateRegist(User formUser,HttpSession session){
		Map<String, String> errors = new HashMap<String, String>();
		/*
		 * 1. 校验登录名
		 */
		String loginname = formUser.getLoginname();
		if(loginname == null || loginname.trim().isEmpty()) {
			errors.put("loginname", "用户名不能为空！");
		} else if(loginname.length() < 3 || loginname.length() > 20) {
			errors.put("loginname", "用户名长度必须在3~20之间！");
		} else if(!userService.ajaxValidateLoginname(loginname)) {
			errors.put("loginname", "用户名已被注册！");
		}
		
		/*
		 * 2. 校验登录密码
		 */
		String loginpass = formUser.getLoginpass();
		if(loginpass == null || loginpass.trim().isEmpty()) {
			errors.put("loginpass", "密码不能为空！");
		} else if(loginpass.length() < 3 || loginpass.length() > 20) {
			errors.put("loginpass", "密码长度必须在3~20之间！");
		}
		
		/*
		 * 3. 确认密码校验
		 */
		String reloginpass = formUser.getReloginpass();
		if(reloginpass == null || reloginpass.trim().isEmpty()) {
			errors.put("reloginpass", "确认密码不能为空！");
		} else if(!reloginpass.equals(loginpass)) {
			errors.put("reloginpass", "两次输入不一致！");
		}
		
		/*
		 * 4. 校验email
		 */
		String email = formUser.getEmail();
		if(email == null || email.trim().isEmpty()) {
			errors.put("email", "Email不能为空！");
		} else if(!email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
			errors.put("email", "Email格式错误！");
		} else if(!userService.ajaxValidateEmail(email)) {
			errors.put("email", "Email已被注册！");
		}
		
		/*
		 * 5. 验证码校验
		 */
		String verifyCode = formUser.getVerifyCode();
		String vcode = (String) session.getAttribute("vCode");
		if(verifyCode == null || verifyCode.trim().isEmpty()) {
			errors.put("verifyCode", "验证码不能为空！");
		} else if(!verifyCode.equalsIgnoreCase(vcode)) {
			errors.put("verifyCode", "验证码错误！");
		}
		
		return errors;
	}
	
/**
 * 激活账号方法
 */
	
	public String activation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1. 获取参数激活码
		 * 2. 用激活码调用service方法完成激活
		 *   > service方法有可能抛出异常, 把异常信息拿来，保存到request中，转发到msg.jsp显示
		 * 3. 保存成功信息到request，转发到msg.jsp显示。
		 */
		String activationCode = request.getParameter("activationCode");
		try {
			userService.activation(activationCode);
		} catch (UserException e) {
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("code", "error");//通知msg.jsp异常错误
			return "f:/jsps/msg.jsp";
		}
		request.setAttribute("msg","账号激活成功");
		request.setAttribute("code", "success");
		return "f:/jsps/msg.jsp";
	}
	
	
	/**
	 * 登陆方法
	 */
	
	public String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		 * 1. 封装表单数据到User
		 * 2. 校验表单数据
		 * 3. 使用service查询，得到User
		 * 4. 查看用户是否存在，如果不存在：
		 *   * 保存错误信息：用户名或密码错误
		 *   * 保存用户数据：为了回显
		 *   * 转发到login.jsp
		 * 5. 如果存在，查看状态，如果状态为false：
		 *   * 保存错误信息：您没有激活
		 *   * 保存表单数据：为了回显
		 *   * 转发到login.jsp
		 * 6. 登录成功：
		   * 　　* 保存当前查询出的user到session中
		 *   * 保存当前用户的名称到cookie中，注意中文需要编码处理。
		 */
		/*
		 * 1. 封装表单数据到User
		 */
		User formUser = CommonUtils.toBean(request.getParameterMap(),User.class);
		/*
		 * 2. 校验表单数据
		 */
		
		Map<String, String> errors = validateLogin(formUser, request.getSession());
		if(errors.size() > 0 ){
			request.setAttribute("form", formUser);
			request.setAttribute("errors", errors);
			return "r:/jsps/user/login.jsp";
		}
		
		/*
		 * 3. 使用service查询，得到User
		 */
		
		User user = userService.login(formUser);
		/*
		 * 4. 查看用户是否存在，如果不存在：
		 *   * 保存错误信息：用户名或密码错误
		 *   * 保存用户数据：为了回显
		 *   * 转发到login.jsp
		 */
		if(user==null){
			request.setAttribute("msg", "用户名或密码错误");
			request.setAttribute("user", formUser);
			return "f:/jsps/user/login.jsp";
		}else{
			//5校验账号是否激活
			if(!user.isStatus()){
//				没有激活保存错误信息
				request.setAttribute("msg", "账号未激活");
//				保存登录信息,方便回显
				request.setAttribute("user", user);
				return "f:/jsps/user/login.jsp";
			}else{
//				此时标识登录成功
//				保存用户到session
				request.getSession().setAttribute("sessionUser", user);
//				保存用户名到cookie
				String loginname = user.getLoginname();
				loginname = URLEncoder.encode(loginname,"utf-8");//用户名编码(避免中文)
				Cookie cookie = new Cookie("loginname", loginname);
				cookie.setMaxAge(60 * 60 * 24 * 10);
				response.addCookie(cookie);
//				登陆成功使用重定向，使用转发会造成刷新页面重新登陆
				return "r:/index.jsp";
			}
		}
		
		
//		return null;
	}
	
	
	/**
	 * 登陆校验,逐一校验表单上的数据
	 * @param formUser
	 * @param session
	 * @return
	 */
	public Map<String, String> validateLogin(User formUser,HttpSession session){
		Map<String, String> errors = new HashMap<String, String>();
		/*
		 * 校验账号
		 */
		String loginname = formUser.getLoginname();
		if(loginname ==null || loginname.trim().isEmpty()){
			errors.put("loginname", "账号不能为空");
		}
		if(loginname.length()<3|| loginname.length() > 20){
			errors.put("loginname", "账号长度不对");
		}
		if(userService.ajaxValidateLoginname(loginname)){
			errors.put("loginname", "账号不存在");
		}
		/*
		 * 校验密码
		 */
		String loginpass = formUser.getLoginpass();
		if(loginpass ==null || loginpass.trim().isEmpty()){
			errors.put("loginpass", "账号不能为空");
		}
		if(loginpass.length()<3|| loginpass.length() > 20){
			errors.put("loginpass", "账号长度不对");
		}
		
		/*
		 * 校验验证嘛
		 */
		String verifyCode = formUser.getVerifyCode();
		String vcode = (String) session.getAttribute("vCode");
		if(verifyCode == null || verifyCode.trim().isEmpty()) {
			errors.put("verifyCode", "验证码不能为空！");
		} else if(!verifyCode.equalsIgnoreCase(vcode)) {
			errors.put("verifyCode", "验证码错误！");
		}
		return errors;
	}
	
	/**
	 *修改密码
	 */
	
	public String updatePassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		/*
		 * 获取表单提交的密码和原session种保存的密码
		 */
		User formUser = CommonUtils.toBean(request.getParameterMap(), User.class);
		User user = (User) request.getSession().getAttribute("sessionUser");
		System.out.println(user);
		System.out.println(formUser);
		
		if(user == null){
			request.setAttribute("msg", "你还没有登陆");
			return "f:/jsps/user/login.jsp";
		}
		/*
		 * 校验密码	
		 */
		Map<String, String> errors = validateUpdatePass(formUser, request.getSession());
		if(errors.size() > 0 ){
			request.setAttribute("form", formUser);
			request.setAttribute("errors", errors);
			return "r:/jsps/user/login.jsp";
		}
		
		/*
		 * 修改密码
		 */
		try {
			userService.updatePass(user.getUid(),formUser.getLoginpass(),formUser.getNewpass());
//			保存成功信息
			request.setAttribute("msg", "修改成功");
//			保存正确信息
			request.setAttribute("code", "success");
//			清除所有session
			request.getSession().invalidate();
			return "f:/jsps/msg.jsp";
		} catch (UserException e) {
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("user", formUser);
			return "f:/jsps/user/pwd.jsp";
		}
	}
	
	/**
	 * 校验修改密码时提交的表单数据
	 */
	public Map<String, String> validateUpdatePass(User formUser,HttpSession session){
		Map<String, String> errors = new HashMap<String, String>();
				
		/*
		 * 校验密码
		 */
		String loginpass = formUser.getLoginpass();
		if(loginpass ==null || loginpass.trim().isEmpty()){
			errors.put("loginpass", "账号不能为空");
		}
		if(loginpass.length()<3|| loginpass.length() > 20){
			errors.put("loginpass", "账号长度不对");
		}
		
		/*
		 * 校验两次密码是否一致
		 */
		String newpass = formUser.getNewpass();
		String reloginpass = formUser.getReloginpass();
		if(!newpass.equals(reloginpass)){
			errors.put("reloginpass", "两次输入密码不一致");
		}		
		
		/*
		 * 校验验证嘛
		 */
		String verifyCode = formUser.getVerifyCode();
		String vcode = (String) session.getAttribute("vCode");
		if(verifyCode == null || verifyCode.trim().isEmpty()) {
			errors.put("verifyCode", "验证码不能为空！");
		} else if(!verifyCode.equalsIgnoreCase(vcode)) {
			errors.put("verifyCode", "验证码错误！");
		}
		return errors;
	}
	
	/**
	 * 退出登陆
	 */
	public String quit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().removeAttribute("sessionUser");
		request.getSession().invalidate();
		return "r:/jsps/user/login.jsp";
	}
}
