package cn.itcast.goods.category.service;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.goods.category.dao.CategoryDao;
/**
 * 分类模块业务层
 * @author Administrator
 *
 */
import cn.itcast.goods.category.domain.Category;
public class CategoryService {
	private CategoryDao categoryDao = new CategoryDao();
	
	public List<Category> findByChild(String pid){
		try {
		return	categoryDao.findByParent(pid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}		
	}
	/**
	 * 查询指定付父分类下游几个子分类
	 * @param cid
	 * @return
	 */
	public int findChildCount(String cid){
		try {
			return categoryDao.findChildCount(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 删除一级enlei i
	 */
	public void delete(String cid){
		try {
			categoryDao.deleteparent(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	/**
	 * 添加分类
	 * @param category
	 */
	public void add(Category category){
		try {
			categoryDao.add(category);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 查询所有分类
	 */
	public List<Category> findAll(){
		try {
			return categoryDao.findAll();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 查询所有一级分类
	 */
	
	public List<Category> findParent(){
		try {
			return categoryDao.findParent();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 加载分类，修改准备
	 * @param cid
	 * @return
	 */
	public Category findByCategoryCid(String cid){
		try {
		return categoryDao.findByParentCid(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * xiugai
	 * @param category
	 */
	public void updateParent(Category category){
		try {
			categoryDao.updateParent(category);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
