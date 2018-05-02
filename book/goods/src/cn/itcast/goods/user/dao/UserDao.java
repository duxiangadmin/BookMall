package cn.itcast.goods.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.goods.user.domain.User;
import cn.itcast.jdbc.TxQueryRunner;


/**
 * 用户模块持久层
 * @author Administrator
 *
 */
public class UserDao {
	private QueryRunner runner = new TxQueryRunner();
	
	/**
	 * 通过激活码查用户
	 * @throws SQLException 
	 */
	public User findUserByCode(String code) throws SQLException{
		String sql ="select * from t_user where activationCode = ?";
		return runner.query(sql, new BeanHandler<User>(User.class),code);
	}
	
	/**
	 * 修改用户激活状态
	 * @throws SQLException 
	 */
	public void updateStatus(String uid ,boolean status) throws SQLException{
		String sql ="update t_user set status=? where uid =?";
		runner.update(sql, status,uid);
	}
	
	
	/**
	 * ajax校验账号是否存在
	 * @param loginname
	 * @return
	 * @throws SQLException
	 */
	public boolean ajaxValidateLoginname(String loginname) throws SQLException{
		String sql = "select count(*) from t_user where loginname = ?";
		Number number =(Number) runner.query(sql, new ScalarHandler(),loginname);
		return number.intValue() == 0;
	}
	
	/**
	 * ajax校验邮箱是否存在
	 */
	
	public boolean ajaxValidateEmail(String email) throws SQLException{
		String sql = "select count(*) from t_user where email = ?";
		Number number =(Number) runner.query(sql, new ScalarHandler(),email);
		return number.intValue() == 0;
	}
	
	/**
	 * 添加用户
	 * @throws SQLException 
	 */
	
	public void add(User user) throws SQLException{
		String sql="insert into t_user value(?,?,?,?,?,?)";
		Object[] param ={user.getUid(),user.getLoginname(),user.getLoginpass(),
		                 user.getEmail(),user.isStatus(),user.getActivationCode()};
		runner.update(sql, param);
	}
	
	/**
	 * 登陆方法
	 * @throws SQLException 
	 */
	public User login(String loginname,String loginpass) throws SQLException{
		String sql ="select * from t_user where loginname=? and loginpass=?";
		return runner.query(sql, new BeanHandler<User>(User.class),loginname,loginpass);
	}
	
	/**
	 * 根据uid和老密码，校验是否存在
	 * @throws SQLException 
	 */
	
	public boolean findUidAndPass(String uid,String oldpass) throws SQLException{
		String sql ="select count(*) from t_user where uid =? and loginpass=?";
		Number number = (Number)runner.query(sql, new ScalarHandler(),uid,oldpass);
		return number.intValue()> 0;
	}
	
	/**
	 * 修改密码
	 * @param uid
	 * @param password
	 * @throws SQLException
	 */
	public void updatePassword(String uid,String newpassword) throws SQLException{
		String sql ="update t_user set loginpass=? where uid=?";
		runner.update(sql, newpassword,uid);
	}
	
	
}
