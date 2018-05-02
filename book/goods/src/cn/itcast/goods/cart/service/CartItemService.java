package cn.itcast.goods.cart.service;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.cart.dao.CartItemDao;
import cn.itcast.goods.cart.domain.CartItem;

/**
 * 购物车业务层
 * @author Administrator
 *
 */
public class CartItemService {
	private CartItemDao cartItemDao = new CartItemDao();
	
	/**
	 * 加载多个cartitem
	 */
	public List<CartItem> loadCartItems(String cartItemIds) {
		try {
			return cartItemDao.loadCartItems(cartItemIds);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 批量删除功能
	 * @param cartItemIds
	 */
	public void batchDelete(String cartItemIds) {
		try {
			cartItemDao.batchDelete(cartItemIds);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 修改购物车条目数量
	 * @param cartItemId
	 * @param quantity
	 * @return
	 */
	public CartItem updateQuantity(String cartItemId, int quantity) {
		try {
			cartItemDao.updateQuantity(cartItemId, quantity);
			return cartItemDao.findByCartItemId(cartItemId);
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 我的购物车
	 * @param uid
	 * @return
	 */
	public List<CartItem> myCart(String uid){
		try {
			return cartItemDao.findByUser(uid);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	
	/**
	 * 添加条目
	 */
	
	public void add(CartItem cartItem){
		try {
			/*
			 * 1. 使用uid和bid去数据库中查询这个条目是否存在
			 */
			CartItem _cartItem = cartItemDao.findByUidAndBid(
					cartItem.getUser().getUid(),
					cartItem.getBook().getBid());
			if(_cartItem == null) {//如果原来没有这个条目，那么添加条目
				cartItem.setCartItemId(CommonUtils.uuid());
				cartItemDao.addCartItem(cartItem);
			} else {//如果原来有这个条目，修改数量
				// 使用原有数量和新条目数量之各，来做为新的数量
				int quantity = cartItem.getQuantity() + _cartItem.getQuantity();
				// 修改这个老条目的数量
				cartItemDao.updateQuantity(_cartItem.getCartItemId(), quantity);
			}
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
