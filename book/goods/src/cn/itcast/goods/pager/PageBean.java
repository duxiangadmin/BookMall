package cn.itcast.goods.pager;

import java.util.List;

/**
 * 分页bean
 * @author Administrator
 *
 */
public class PageBean<T>{
	
	private int pc;//当前页
	private int tr;//总记录数
	private int ps;//每页记录数 
	private String url;//请求路径
	private List<T> listBean;//煤业显示数据
	
	
	//总页数
	public int getTp(){
		int tp = tr/ps;
		return tr%ps ==0 ? tp : tp+1;
	}
	
	public int getPc() {
		return pc;
	}
	public void setPc(int pc) {
		this.pc = pc;
	}
	public int getTr() {
		return tr;
	}
	public void setTr(int tr) {
		this.tr = tr;
	}
	public int getPs() {
		return ps;
	}
	public void setPs(int ps) {
		this.ps = ps;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<T> getListBean() {
		return listBean;
	}
	public void setListBean(List<T> listBean) {
		this.listBean = listBean;
	}
	
}
