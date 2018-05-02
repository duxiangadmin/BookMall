package cn.itcast.goods.admin.book.web.servlet;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.book.service.BookService;
import cn.itcast.goods.category.domain.Category;
import cn.itcast.goods.category.service.CategoryService;

public class AdminAddBookServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		接受表单三步
//		1创建工场
		DiskFileItemFactory factory = new DiskFileItemFactory();
//		2通过工场创建解析器
		ServletFileUpload sfu = new ServletFileUpload(factory);
		sfu.setFileSizeMax(80*1024);
//		3通过request得到list<filtem>
		List<FileItem> fileitemlist = null ;
		try {
			fileitemlist = sfu.parseRequest(request);
		} catch (Exception e) {
			String msg = "你上床的文件超过80kb";
			error(msg, request, response);
			return ;
		}
		
//		4把fileitemlist中的普通字段封装到category和book对象中
//		先将每一个元素保存到map中
		Map<String, Object> map = new HashMap<String, Object>();
		for(FileItem fileitem: fileitemlist){
			if(fileitem.isFormField()){
				map.put(fileitem.getFieldName(), fileitem.getString("utf-8"));
			}
		}
//		再将map转换成对应的对象
		Book book = CommonUtils.toBean(map, Book.class);
		Category category = CommonUtils.toBean(map, Category.class);
		book.setCategory(category);
		
		/*
		 * 保存图片
		 */
//		获取文件名
		FileItem fileItem = fileitemlist.get(1);
		String fieldName = fileItem.getName();		
//		截取文件名，因浏览器不同，可能带绝对路径
		int indexOf = fieldName.lastIndexOf("\\");
		if(indexOf != -1){
			fieldName = fieldName.substring(indexOf+1);
		}
//		给图片命加上uuid_
		fieldName = CommonUtils.uuid()+"_"+fieldName;
//		
//		检验文件的扩展名
		if(!fieldName.toLowerCase().endsWith(".jpg")){
			error("图片必须是jpg格式的", request, response);
			return ;
		}
		
//		校验图片尺寸(必须先创建图片)
//			获取图片的真实路径
		String savapath= this.getServletContext().getRealPath("/book_img");
//		创建目标文件
		File file = new File(savapath,fieldName);
//		保存临时文件
		try {
			fileItem.write(file);//保存之后会删除
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
//		校验尺寸
		ImageIcon icon = new ImageIcon(file.getAbsolutePath());
		Image image = icon.getImage();
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		if(width >350 || height >350){
			error("尺寸必须在350*350之间", request, response);
			file.delete();
			return;
		}
		
//		保存图片路径到book对象中
		book.setImage_w("book_img/"+fieldName);

		
		
		
		/*
		 * 保存图片小兔
		 */
//		获取文件名
		fileItem = fileitemlist.get(2);
		fieldName = fileItem.getName();		
//		截取文件名，因浏览器不同，可能带绝对路径
		indexOf = fieldName.lastIndexOf("\\");
		if(indexOf != -1){
			fieldName = fieldName.substring(indexOf+1);
		}
//		给图片命加上uuid_
		fieldName = CommonUtils.uuid()+"_"+fieldName;
//		
//		检验文件的扩展名
		if(!fieldName.toLowerCase().endsWith(".jpg")){
			error("图片必须是jpg格式的", request, response);
			return ;
		}
		
//		校验图片尺寸(必须先创建图片)
//			获取图片的真实路径
		savapath= this.getServletContext().getRealPath("/book_img");
//		创建目标文件
		file = new File(savapath,fieldName);
//		保存临时文件
		try {
			fileItem.write(file);//保存之后会删除
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
//		校验尺寸
		icon = new ImageIcon(file.getAbsolutePath());
		image = icon.getImage();
		width = image.getWidth(null);
		height = image.getHeight(null);
		if(width >350 || height >350){
			error("尺寸必须在350*350之间", request, response);
			file.delete();
			return;
		}
		
//		保存图片路径到book对象中
		book.setImage_b("book_img/"+fieldName);
		
//		调用service保存book对象
		book.setBid(CommonUtils.uuid());
		BookService bookService = new BookService();
		bookService.addBook(book);
		
//		保存成功信息，转发到msg。jsp
		request.setAttribute("msg", "图书上传成功");
		request.getRequestDispatcher("/adminjsps/msg.jsp").forward(request, response);
		
	}
	
	private void error( String msg ,HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("msg",msg);
		request.setAttribute("parents",new CategoryService().findParent());
		request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
	}

}
