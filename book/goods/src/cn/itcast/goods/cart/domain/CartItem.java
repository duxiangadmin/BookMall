package cn.itcast.goods.cart.domain;

import java.math.BigDecimal;

import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.user.domain.User;

/**
 * 购物车条目
 * @author Administrator
 *
 */
public class CartItem {
	private String cartItemId;//主键
	private int quantity;//数量
	private Book book;//对应的书本
	private User user;//对应谁购买的
	
	/*
	 *返回小计方法
	 */
	public double getSubtotal(){
		/*
		 * 使用BigDecimal不会有误差
		 * 要求必须使用String类型构造器
		 */
		BigDecimal b1 = new BigDecimal(book.getCurrPrice()+"");
		BigDecimal b2 = new BigDecimal(quantity+"");
		return b1.multiply(b2).doubleValue();
	}
	
	public String getCartItemId() {
		return cartItemId;
	}
	public void setCartItemId(String cartItemId) {
		this.cartItemId = cartItemId;
	}
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
