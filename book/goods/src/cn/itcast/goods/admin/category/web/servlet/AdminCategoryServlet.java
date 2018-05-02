package cn.itcast.goods.admin.category.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.service.BookService;
import cn.itcast.goods.category.domain.Category;
import cn.itcast.goods.category.service.CategoryService;
import cn.itcast.servlet.BaseServlet;

public class AdminCategoryServlet extends BaseServlet {
	private CategoryService categoryService = new CategoryService();
	private BookService bookService = new BookService();
	/**
	 * 查询所有分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<Category> list = categoryService.findAll();
		req.setAttribute("parents", list);
		return "f:/adminjsps/admin/category/list.jsp";
	}
	
	/**
	 * 添加一级分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addPrent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Category category = CommonUtils.toBean(req.getParameterMap(), Category.class);
		category.setCid(CommonUtils.uuid());
		categoryService.add(category);
		return findAll(req,resp);
	}
	
	/**
	 * 查询所有一级分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findParent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pid = req.getParameter("pid");
		List<Category> parent = categoryService.findParent();
		req.setAttribute("pid", pid);
		req.setAttribute("parent", parent);
		return "f:/adminjsps/admin/category/add2.jsp";
	}
	
	
	/**
	 * 添加er级分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addChild(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Category child = CommonUtils.toBean(req.getParameterMap(), Category.class);
		child.setCid(CommonUtils.uuid());
		
		String pid = req.getParameter("pid");
		Category parent = new Category();
		parent.setCid(pid);
		child.setParent(child);
		
		categoryService.add(child);
		return findAll(req,resp);

	}
	
	/**
	 * 修改一级分类准备
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String updateParentPre(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cid = req.getParameter("cid");
		Category category = categoryService.findByCategoryCid(cid);
		req.setAttribute("category", category);
		return "f:/adminjsps/admin/category/edit.jsp";
	}
	
	/**
	 * 正式修改一级分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editParent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Category category = CommonUtils.toBean(req.getParameterMap(), Category.class);
		categoryService.updateParent(category);
		return findAll(req, resp);
	}
	
	/**
	 * 修改二级分类准备
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String updateChildPre(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String pid = req.getParameter("pid");
		Category child = categoryService.findByCategoryCid(pid);
		req.setAttribute("child",child);
		req.setAttribute("parents", categoryService.findParent());
		return "f:/adminjsps/admin/category/edit2.jsp";
	}
	
	/**
	 * 修改二级分类正式
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String updateChild(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Category child = CommonUtils.toBean(req.getParameterMap(), Category.class);
		
		String pid =req.getParameter("pid");
		
		Category parent = new Category();
		parent.setCid(pid);
		
		child.setParent(parent);
		
		categoryService.updateParent(child);
		return findAll(req, resp);
	}
	
	/**
	 * 删除一级分了
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String deleteParent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cid = req.getParameter("cid");
		int count = categoryService.findChildCount(cid);
		if(count > 0){
			req.setAttribute("msg", "还有子分类，不能删除，如需删除，请先删除所有子分类");
			return "f:/adminjsps/msg.jsp";
		}else{
			categoryService.delete(cid);
			return findAll(req, resp);
		}
	}
	
	/**
	 * 删除二级分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String deleteChild(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String cid = req.getParameter("cid");
		int count = bookService.findBookCount(cid);
		if(count > 0){
			req.setAttribute("msg", "该分类下还存在图书，请删除图书后再来删除");
			return "f:/adminjsps/msg.jsp";
		}else{
			categoryService.delete(cid);
			return findAll(req, resp);
		}
	}
	
	
}
