package cn.itcast.goods.user.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.user.dao.UserDao;
import cn.itcast.goods.user.domain.User;
import cn.itcast.goods.user.service.exception.UserException;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;

/**
 * 用户模块业务层
 * @author Administrator
 *
 */
public class UserService {
	private UserDao userDao = new UserDao();
	
	/**
	 * ajax校验账号是否存在
	 * @param loginname
	 * @return
	 * @throws SQLException
	 */
	public boolean ajaxValidateLoginname(String loginname){
		try {
			return userDao.ajaxValidateLoginname(loginname);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 校验邮箱是否存在
	 * @param email
	 * @return
	 * @throws SQLException
	 */
	public boolean ajaxValidateEmail(String email){
		try {
			return userDao.ajaxValidateEmail(email);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 注册功能
	 * @param user
	 */
	public void regist(User user){
//		补全信息
		user.setUid(CommonUtils.uuid());
		user.setStatus(false);
		user.setActivationCode(CommonUtils.uuid() + CommonUtils.uuid());
//		向数据库插入信息
		try {
			userDao.add(user);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
//		发送激活邮件
//		获取邮件信息配置文件，将配置文件获取到properties中
		Properties properties = new Properties();
		try {
			properties.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
//		登陆邮件服务器，得到session
		String host =properties.getProperty("host");
		String name = properties.getProperty("username");
		String pass =properties.getProperty("password");
		Session session = MailUtils.createSession(host, name, pass);
//		创建Mail对象
		String from=properties.getProperty("from");
		String to =user.getEmail();
		String title=properties.getProperty("subject");
		
		String content=MessageFormat.format( properties.getProperty("content"),user.getActivationCode());
//		占位符补充激活码
		Mail mail = new Mail(from, to, title, content);
//		发送邮件
		try {
			MailUtils.send(session, mail);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 激活用户
	 * @throws UserException 
	 */
	public void activation(String code) throws UserException{
		//1激活码查询到用户
//		2.如果user为null则标识激活码无效 抛出异常 给出异常信息
//		3.查看用户状态为true，则抛出异常，表示不要二次激活
//		4.修改状态
		try {
			User user = userDao.findUserByCode(code);
			if(user ==null){throw new  UserException("无效激活码");}
			if(user.isStatus()){throw new  UserException("您已经激活");}
			userDao.updateStatus(user.getUid(), true);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}	
	}
	
	/**
	 * 登陆方法
	 * @param user
	 * @return
	 */
	
	public User login(User user){
		try {
			return userDao.login(user.getLoginname(),user.getLoginpass());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 修改密码
	 * @throws UserException 
	 */
	
	public void updatePass(String uid,String oldpass,String newpassword) throws UserException{
		try {
//			校验老密码是否合格
			boolean bool = userDao.findUidAndPass(uid, oldpass);
			if(!bool){
				throw new UserException("旧密码无效");
			}
//			修改密码
			userDao.updatePassword(uid, newpassword);			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
