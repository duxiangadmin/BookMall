package cn.itcast.goods.category.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.category.domain.Category;
import cn.itcast.jdbc.TxQueryRunner;
/**
 * 分类模块持久层
 * @author Administrator
 *
 */
public class CategoryDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/*
	 * 将一个map映射到category中
	 */
	private Category toCategory(Map<String,Object> map) {
		/*
		 * map {cid:xx, cname:xx, pid:xx, desc:xx, orderBy:xx}
		 * Category{cid:xx, cname:xx, parent:(cid=pid), desc:xx}
		 */
		Category category = CommonUtils.toBean(map, Category.class);
		String pid = (String)map.get("pid");
		if(pid != null) {//如果父分类ID不为空，
			/*
			 * 使用一个父分类对象来拦截pid
			 * 再把父分类设置给category
			 */
			Category parent = new Category();
			parent.setCid(pid);
			category.setParent(parent);
		}
		return category;
	}
	
	/*
	 * 将多个map(<list<MAP>)转换为多个category（list<category>）
	 */
	
	private List<Category> toCategoryList(List<Map<String,Object>> mapList) {
		List<Category> categoryList = new ArrayList<Category>();//创建一个空集合
		for(Map<String,Object> map : mapList) {//循环遍历每个Map
			Category c = toCategory(map);//把一个Map转换成一个Category
			categoryList.add(c);//添加到集合中
		}
		return categoryList;//返回集合
	}
	
	
	/**
	 * 返回所有分类
	 * @return
	 * @throws SQLException 
	 */
	public List<Category> findAll() throws SQLException {
		/*
		 * 1. 查询出所有一级分类
		 */
		String sql ="select * from t_category where pid is null";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler());
		List<Category> parents = toCategoryList(mapList);
		/*
		 * 2. 循环遍历所有的一级分类，为每个一级分类加载它的二级分类 
		 */
		for(Category parent : parents) {
			// 查询出当前父分类的所有子分类
			List<Category> children = findByParent(parent.getCid());
			// 设置给父分类
			parent.setChildren(children);
		}
		return parents;
	}
	
	/**
	 * 通过父分类查询子分类
	 * @param pid
	 * @return
	 * @throws SQLException 
	 */
	public List<Category> findByParent(String pid) throws SQLException {
		String sql = "select * from t_category where pid=?";
		List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler(), pid);
		return toCategoryList(mapList);
	}
	
	
	/**
	 * 添加分类即可添加一级分类也可添加二级分类
	 * @param category
	 * @throws SQLException
	 */
	public void add(Category category) throws SQLException {
		String sql ="insert into t_category(cid,cname,pid,`desc`) values(?,?,?,?)";
		String pid = null;
		if(category.getParent() != null){
			pid = category.getParent().getCid();
		}
		Object[] param = {category.getCid(),category.getCname(),pid,category.getDesc()};
		qr.update(sql, param);		
	}
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<Category> findParent() throws SQLException {
		/*
		 * 1. 查询出所有一级分类
		 */
		String sql ="select * from t_category where pid is null";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler());
		List<Category> parents = toCategoryList(mapList);
		
		return parents;
	}
	
	/**
	 * 查询出指定的一级分类信息
	 * @return
	 * @throws SQLException
	 */
	public Category findByParentCid(String cid) throws SQLException {
		/*
		 * 1. 查询出所有一级分类及二级分类
		 */
		String sql ="select * from t_category where cid =?";
//		return qr.query(sql, new BeanHandler<Category>(Category.class),cid);
		return toCategory(qr.query(sql, new MapHandler(),cid));
	}
	
	/**
	 * 修改分类，可修改一二级分类
	 * @param category
	 * @throws SQLException 
	 */
	public void updateParent(Category category) throws SQLException{
		String sql ="update t_category set cname =?,pid=?,`desc`=? where cid =?";
		String pid = null;
		if(category.getParent() != null){
			pid = category.getParent().getCid();
		}
		Object[] param = {category.getCname(),pid,category.getDesc(),category.getCid()};
		qr.update(sql,param);
		
	}
	
	/**
	 * 查询出指定父分类下游几个子分类
	 * @param category
	 * @throws SQLException
	 */
	public int findChildCount(String cid) throws SQLException{
		String sql ="select count(*) from t_Category where pid =?";
		Number number = (Number) qr.query(sql, new ScalarHandler(),cid);
		return number == null ? 0 :number.intValue();
		
	}
	
	/**
	 * 删除分类
	 * @param category
	 * @throws SQLException
	 */
	public void deleteparent(String cid) throws SQLException{
		String sql ="delete from t_category where cid =?";
		qr.update(sql,cid);
		
	}
	
	
	
}
