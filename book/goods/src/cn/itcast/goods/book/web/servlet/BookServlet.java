package cn.itcast.goods.book.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.book.service.BookService;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.servlet.BaseServlet;

public class BookServlet extends BaseServlet {
	private BookService bookService = new BookService();
	/*
	 * 获得pc
	 */
	private int getpc(HttpServletRequest req){
		int pc =1;
		String param = req.getParameter("pc");
		if(param != null && !(param.trim().isEmpty())){
			try {
				pc = Integer.valueOf(param);
			} catch (RuntimeException e) {}
		}
		return pc;
	}
	
	private String getUrl(HttpServletRequest req){
		String url = req.getRequestURL()+"?"+req.getQueryString();
		int index = url.indexOf("&pc=");
		if(index != -1){
			url = url.substring(0, index);
		}
		return url;
	}
	
	/**
	 * 按分类查,左侧分类导航条使用的查询
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/*
		 * 1得到pc
		 */
		int pc = getpc(req);
		/*
		 * 2得到url
		 * 因为每一个pagebean中的参数不仅相同，所以需要截取,在下面重新设置
		 */
		String url = getUrl(req);
		/*
		 * 3获取查询条件，cid
		 */
		String cid = req.getParameter("cid");
		/*
		 * 4.获得pagebean
		 */
		PageBean<Book> pb = bookService.findByCategory(cid, pc);
		/*
		 * 5.给pagebean设置url 保存分页数据pagebean，转发到book/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
	
	/**
	 * 按作者查
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	
	public String findByAuthor(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		/*
		 * 1得到pc
		 */
		int pc = getpc(req);
		/*
		 * 2得到url
		 */
		String url = getUrl(req);
		/*
		 * 3获取查询条件，cid
		 */
		String author = req.getParameter("author");
		/*
		 * 4.获得pagebean
		 */
		PageBean<Book> pb = bookService.findByAuthor(author, pc);
		/*
		 * 5.给pagebean设置url 保存分页数据pagebean，转发到book/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
	
	/**
	 * 根据出版社进行查询
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	
	public String findByPress(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/*
		 * 1得到pc
		 */
		int pc = getpc(req);
		/*
		 * 2得到url
		 */
		String url = getUrl(req);
		/*
		 * 3获取查询条件，cid
		 */
		String press = req.getParameter("press");
		/*
		 * 4.获得pagebean
		 */
		PageBean<Book> pb = bookService.findByPress(press, pc);
		/*
		 * 5.给pagebean设置url 保存分页数据pagebean，转发到book/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
	
	/**
	 * 根据书名进行查询
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	
	public String findByBname(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/*
		 * 1得到pc
		 */
		int pc = getpc(req);
		/*
		 * 2得到url
		 */
		String url = getUrl(req);
		/*
		 * 3获取查询条件，cid
		 */
		String bname = req.getParameter("bname");
		/*
		 * 4.获得pagebean
		 */
		PageBean<Book> pb = bookService.findBybname(bname, pc);
		/*
		 * 5.给pagebean设置url 保存分页数据pagebean，转发到book/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
	
	/**
	 * 多条件组合查询
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	
	public String findByCombinAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		/*        
		 * 1得到pc
		 */
		int pc = getpc(req);
		/*
		 * 2得到url
		 */
		String url = getUrl(req);
		/*
		 * 3获取查询条件，cid
		 */
		Book book = CommonUtils.toBean(req.getParameterMap(), Book.class);
		/*
		 * 4.获得pagebean
		 */
		PageBean<Book> pb = bookService.findByCombination(book, pc);
		/*
		 * 5.给pagebean设置url 保存分页数据pagebean，转发到book/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
	
	public String findByname(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		System.out.println(req.getCharacterEncoding());
		System.out.println(req.getParameter("name"));
		return null;
	}
	
	
	public String findBybid(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String bid = req.getParameter("bid");
		/*
		 * 4.获得pagebean
		 */
		Book book = bookService.load(bid);
		/*
		 * 5.给pagebean设置url 保存分页数据pagebean，转发到book/list.jsp
		 */
		req.setAttribute("book", book);
		return "f:/jsps/book/desc.jsp";
	}
}
