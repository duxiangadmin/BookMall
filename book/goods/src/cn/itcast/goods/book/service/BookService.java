package cn.itcast.goods.book.service;

import java.sql.SQLException;

import cn.itcast.goods.book.dao.BookDao;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.pager.PageBean;

public class BookService {
	private BookDao bookDao = new  BookDao();
	
	/**
	 * 查询图书个数
	 * @param cid
	 * @return
	 */
	public int findBookCount(String cid){
		try {
		return	bookDao.findBookCount(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 按分类查
	 * @param cid
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByCategory(String cid,int pc){
		try {
			return bookDao.findByCategory(cid, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 按作者查
	 * @param author
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByAuthor(String author,int pc){
		try {
			return bookDao.findByAuthor(author, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 按书名查
	 * @param category
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findBybname(String bname,int pc){
		try {
			return bookDao.findBybname(bname, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 按出版社查
	 * @param press
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByPress(String press,int pc){
		try {
			return bookDao.findByPress(press, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 多条件组合欻性能
	 * @param book
	 * @param pc
	 * @return
	 */
	public PageBean<Book> findByCombination(Book book,int pc){
		try {
			return bookDao.findByComBination(book, pc);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 根据id查,加载图书
	 * @param bid
	 * @param pc
	 * @return
	 */
	public Book load(String bid){
		try {
			return bookDao.findBybid(bid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 添加图书
	 * @param book
	 */
	public void addBook(Book book){
		try {
			bookDao.addBook(book);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 修改图书
	 * @param book
	 */
	public void edit(Book book) {
		try {
			bookDao.edit(book);
		} catch (Exception e) {

		}
	}

	/**
	 * 删除图书
	 * @param bid
	 */
	public void del(String bid) {
		try {
			bookDao.del(bid);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	} 
	
	
}
