package cn.itcast.goods.book.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.category.domain.Category;
import cn.itcast.goods.pager.Expression;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.goods.pager.PageConstants;
import cn.itcast.jdbc.TxQueryRunner;

public class BookDao {
	private QueryRunner qr = new TxQueryRunner();
	
	
	public Book findBybid(String bid) throws SQLException{
		String sql = "SELECT * FROM t_book b, t_category c WHERE b.cid=c.cid AND b.bid=?";
		// 一行记录中，包含了很多的book的属性，还有一个cid属性
		Map<String,Object> map = qr.query(sql, new MapHandler(), bid);
		// 把Map中除了cid以外的其他属性映射到Book对象中
		Book book = CommonUtils.toBean(map, Book.class);
		// 把Map中cid属性映射到Category中，即这个Category只有cid
		Category category = CommonUtils.toBean(map, Category.class);
		// 两者建立关系
		book.setCategory(category);
		
		// 把pid获取出来，创建一个Category parnet，把pid赋给它，然后再把parent赋给category
		if(map.get("pid") != null) {
			Category parent = new Category();
			parent.setCid((String)map.get("pid"));
			category.setParent(parent);
		}	
		return book;
	}
	
	/**
	 * 按分类查询
	 * @throws SQLException 
	 */
	
	public PageBean<Book> findByCategory(String cid,int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("cid", "=", cid));
		return findByCriteria(exprList, pc);
	}
	
	/**
	 * 按书名模糊查询
	 * @param category
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findBybname(String bname,int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("bname", "like", "%"+bname+"%"));
		return findByCriteria(exprList, pc);
	}
	
	/**
	 * 按作者模糊查询
	 * @param author
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByAuthor(String author,int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("author", "like", "%"+author+"%"));
		return findByCriteria(exprList, pc);
	}
	/**
	 * 按出版社查
	 * @param press
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByPress(String press,int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("press", "like", "%"+press+"%"));
		return findByCriteria(exprList, pc);
	}
	
	/**
	 * 多条件模糊查询
	 * @param book
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByComBination(Book book,int pc) throws SQLException{
		List<Expression> exprList = new ArrayList<Expression>();
		exprList.add(new Expression("bname", "like", "%"+book.getBname()+"%"));
		exprList.add(new Expression("author", "like", "%"+book.getAuthor()+"%"));
		exprList.add(new Expression("press", "like", "%"+book.getPress()+"%"));
		return findByCriteria(exprList, pc);
	}
	
	
	
	
	/**
	 * 通用的查询方法
	 * @throws SQLException 
	 */
	public PageBean<Book> findByCriteria(List<Expression> exprList,int pc) throws SQLException{
		/*
		 * 得到ps
		 * 得到tr
		 * 得到beanlist
		 * 创建pagebean，返回
		 */
//		1.得到ps
		int ps = PageConstants.BOOK_PAGE_SIZE;
//		2.通过expression获得
		StringBuilder whereSql = new StringBuilder(" where 1=1 ");
		List<Object> params = new ArrayList<Object>();
		for(Expression expr : exprList){
			whereSql.append(" and ").append(expr.getName()).append(" ")
			.append(expr.getOperator()).append(" ");
			if(!(expr.getOperator().equals("is null"))){
				whereSql.append("?");
				params.add(expr.getValue());
			}
		}
		
//		3.得到tr总记录数
		String sql = "select count(*) from t_book "+whereSql; 
		Number number = (Number) qr.query(sql, new ScalarHandler(),params.toArray());
		int tr = number.intValue();
		
		/*
		 * 4. 得到beanList，即当前页记录
		 */
		sql = "select * from t_book" + whereSql + " order by orderBy limit ?,?";
		params.add((pc-1) * ps);//当前页首行记录的下标
		params.add(ps);//一共查询几行，就是每页记录数
		
		List<Book> beanList = qr.query(sql, new BeanListHandler<Book>(Book.class), 
				params.toArray());
//		System.out.println(beanList);
		
//		创建pageban(其中没有url，由servlet完成)
		PageBean<Book> pb = new PageBean<Book>();
		pb.setPc(pc);
		pb.setPs(ps);
		pb.setTr(tr);
		pb.setListBean(beanList);
		
		
		return pb;
	}
	/**
	 * 根据分类id查询图书个数
	 * @param cid
	 * @return
	 * @throws SQLException
	 */
	public int findBookCount(String cid) throws SQLException{
		String sql ="select count(*) from t_book where cid =?";
		Number number = (Number) qr.query(sql, new ScalarHandler(),cid);
		return number ==null ?0 :number.intValue();
	}
	
	public static void main(String[] args) throws SQLException {
		BookDao bookDao = new BookDao();
		List<Expression> exprList = new ArrayList<Expression>();
		bookDao.findByCategory("5F79D0D246AD4216AC04E9C5FAB3199E", 1);
//		bookDao.findByCriteria(exprList, 1);
		
	}

	/**
	 * 添加图书
	 * @param book
	 * @throws SQLException
	 */
	public void addBook(Book book) throws SQLException {
		String sql = "insert into t_book(bid,bname,author,price,currPrice," +
				"discount,press,publishtime,edition,pageNum,wordNum,printtime," +
				"booksize,paper,cid,image_w,image_b)" +
				" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params = {book.getBid(),book.getBname(),book.getAuthor(),
				book.getPrice(),book.getCurrPrice(),book.getDiscount(),
				book.getPress(),book.getPublishtime(),book.getEdition(),
				book.getPageNum(),book.getWordNum(),book.getPrinttime(),
				book.getBooksize(),book.getPaper(), book.getCategory().getCid(),
				book.getImage_w(),book.getImage_b()};
		qr.update(sql,params);
	}

	/**
	 * 修改图书
	 * @param book
	 * @throws SQLException 
	 */
	public void edit(Book book) throws SQLException {
		String sql = "update t_book set bname=?,author=?,price=?,currPrice=?," +
				"discount=?,press=?,publishtime=?,edition=?,pageNum=?,wordNum=?," +
				"printtime=?,booksize=?,paper=?,cid=? where bid=?";
		Object[] params= {book.getBname(),book.getAuthor(),
				book.getPrice(),book.getCurrPrice(),book.getDiscount(),
				book.getPress(),book.getPublishtime(),book.getEdition(),
				book.getPageNum(),book.getWordNum(),book.getPrinttime(),
				book.getBooksize(),book.getPaper(), book.getCategory().getCid(),
				book.getBid()};
		qr.update(sql,params);
	}
	
	
	/**
	 * 删除图书
	 * @param bid
	 * @throws SQLException 
	 */
	public void del(String bid) throws SQLException {
		String sql ="delete from t_book where bid =?";
		qr.update(sql, bid);
	}
}
