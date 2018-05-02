package cn.itcast.goods.admin.book.web.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.book.service.BookService;
import cn.itcast.goods.category.domain.Category;
import cn.itcast.goods.category.service.CategoryService;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.servlet.BaseServlet;

public class AdminBookServlet extends BaseServlet {
	private BookService bookService = new BookService();
	private CategoryService categoryService = new CategoryService();
	
	/**
	 * 加载图书
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 查询出所有的一级分类
		 */
		List<Category> parents = categoryService.findParent();
		req.setAttribute("parents", parents);
		
		/*
		 * 根据bid查询出所有的book对象
		 */
		String bid = req.getParameter("bid");
		Book book = bookService.load(bid);
		req.setAttribute("book", book);
		/*
		 * 根据当前图书所属的一级分类查询出所有二级分类
		 */
		String pid = book.getCategory().getParent().getCid();
		req.setAttribute("children", categoryService.findByChild(pid));
		
		
		return "f:/adminjsps/admin/book/desc.jsp";
	}
	

	/**
	 * 查询所有分类
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findCategoryAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 通过service得到所有的分类 2. 保存到request中，转发到left.jsp
		 */
		List<Category> parents = categoryService.findAll();
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/book/left.jsp";
	}

	/*
	 * 获得pc
	 */
	private int getpc(HttpServletRequest req) {
		int pc = 1;
		String param = req.getParameter("pc");
		if (param != null && !(param.trim().isEmpty())) {
			try {
				pc = Integer.valueOf(param);
			} catch (RuntimeException e) {
			}
		}
		return pc;
	}

	private String getUrl(HttpServletRequest req) {
		String url = req.getRequestURL() + "?" + req.getQueryString();
		int index = url.indexOf("&pc=");
		if (index != -1) {
			url = url.substring(0, index);
		}
		return url;
	}

	/**
	 * 按分类查,左侧分类导航条使用的查询
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByCategory(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1得到pc
		 */
		int pc = getpc(req);
		/*
		 * 2得到url 因为每一个pagebean中的参数不仅相同，所以需要截取,在下面重新设置
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
		return "f:/adminjsps/admin/book/list.jsp";
	}

	/**
	 * 按作者查
	 * 
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
		return "f:/adminjsps/admin/book/list.jsp";
	}

	/**
	 * 根据出版社进行查询
	 * 
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
		return "f:/adminjsps/admin/book/list.jsp";
	}

	/**
	 * 根据书名进行查询
	 * 
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
		return "f:/adminjsps/admin/book/list.jsp";
	}

	/**
	 * 多条件组合查询
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */

	public String findByCombinAction(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 得到pc：如果页面传递，使用页面的，如果没传，pc=1
		 */
		int pc = getpc(req);
		/*
		 * 2. 得到url：...
		 */
		String url = getUrl(req);
		/*
		 * 3. 获取查询条件，本方法就是cid，即分类的id
		 */
		Book criteria = CommonUtils.toBean(req.getParameterMap(), Book.class);
		/*
		 * 4. 使用pc和cid调用service#findByCategory得到PageBean
		 */
		PageBean<Book> pb = bookService.findByCombination(criteria, pc);
		/*
		 * 5. 给PageBean设置url，保存PageBean，转发到/jsps/book/list.jsp
		 */
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/adminjsps/admin/book/list.jsp";
	}

	/**
	 * genuine指定ID查询图书
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
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
		return "f:/adminjsps/admin/book/desc.jsp";
	}
	/**
	 * 添加图书第一步，准备，获取一级分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addBookPre(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Category> parent = categoryService.findParent();
		req.setAttribute("parents", parent);
		return "f:/adminjsps/admin/book/add.jsp";
	}
	
	/**
	 * ajax修改一级分类显示二级分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String ajaxFindByChild(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pid = req.getParameter("pid");
		List<Category> category = categoryService.findByChild(pid);
		String json = toJson(category);
		System.out.println(json);
		resp.getWriter().print(json);
		return null;
	}
	
	private String toJson(Category category){
		StringBuilder sb = new StringBuilder("{");
		sb.append("\"cid\"").append(":").append("\"").append(category.getCid()).append("\"");
		sb.append(",");
		sb.append("\"cname\"").append(":").append("\"").append(category.getCname()).append("\"");
		sb.append("}");
		return sb.toString();
	}
	
	private String toJson(List<Category> categoriesList){
		StringBuilder sb = new StringBuilder("[");
		for(int i=0;i<categoriesList.size();i++){
			sb.append(toJson(categoriesList.get(i)));
			if(i < categoriesList.size()-1){
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	/**
	 * 编辑图书
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map map = req.getParameterMap();
		Book book = CommonUtils.toBean(map, Book.class);
		Category category = CommonUtils.toBean(map, Category.class);
		book.setCategory(category);
		
		try {
			bookService.edit(book);
		} catch (Exception e) {
			req.setAttribute("msg", "保存失败，请重新保存");
		}
		req.setAttribute("msg", "保存成功");
		return "f:/adminjsps/msg.jsp";
	}
	
	
	/**
	 * 删除图书
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
String bid = req.getParameter("bid");
		
		/*
		 * 删除图片
		 */
		Book book = bookService.load(bid);
		String savepath = this.getServletContext().getRealPath("/");//获取真实的路径
		System.out.println(savepath);
		new File(savepath, book.getImage_w()).delete();//删除文件
		new File(savepath, book.getImage_b()).delete();//删除文件
		
		bookService.del(bid);//删除数据库的记录
		
		req.setAttribute("msg", "删除图书成功！");
		return "f:/adminjsps/msg.jsp";
	}
	

}
