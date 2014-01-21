package net.yunstudio.util.view.treeview;

import java.util.ArrayList;
import java.util.List;

import android.view.View;

public class TreeItem {
	private View view;
	private TreeItem parent;
	private int padding=30;
	private List<TreeItem> childrens=new ArrayList<TreeItem>(0);
	public boolean nextIsExpand=true;
	private int viewHeight;  
	private int left,top,bottom,right;
	
	
	public TreeItem(){}
	/**
	 * 初始化TreeItem
	 * @param view
	 * @param parent
	 */
	public TreeItem(View view, TreeItem parent) {
		super();
		this.view = view;
		this.parent = parent;
	}
	/**
	 * 初始化TreeItem
	 * @param view
	 * @param parent
	 * @param viewHeight 所持有的view的高度，如果不设置的话，那么每个节点的高度只能在显示一次之后才能获取，这也就意味着该节点在第一次展开之前是没有动画的
	 */
	public TreeItem(View view, TreeItem parent,int viewHeight) {
		super();
		this.view = view;
		this.parent = parent;
		this.viewHeight=viewHeight;
	}
	/**
	 * 初始化TreeItem
	 * @param view
	 * @param parent
	 * @param viewHeight 所持有的view的高度，如果不设置的话，那么每个节点的高度只能在显示一次之后才能获取，这也就意味着该节点在第一次展开之前是没有动画的
	 * @param padding 每一子级的缩进 px，padding_left=this.left+this.padding
	 */
	public TreeItem(View view, TreeItem parent,int viewHeight,int padding) {
		super();
		this.view = view;
		this.parent = parent;
		this.viewHeight=viewHeight;
		this.padding=padding;
	}
	/**
	 * 设置view的padding，其中left会加上this.padding作为每一级的缩进
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public void setRelativePadding(int left,int top,int right,int bottom) {
		this.left=left;
		this.top=top;
		this.right=right;
		this.bottom=bottom;
	}

	public int getPadding() {
		return padding;
	}
	public void setPadding(int padding) {
		this.padding = padding;
	}
	public View getView() {
		view.setPadding(getLevel()*padding+left,top,right,bottom);
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public TreeItem getParent() {
		return parent;
	}

	public void setParent(TreeItem parent) {
		this.parent = parent;
	}

	/**
	 * 动态获取该节点的级数
	 * @return
	 */
	public int getLevel() {
		int level=0;
		TreeItem localParent=parent;
		
		while (localParent!=null) {
			level++;
			localParent=localParent.getParent();
		}
		
		return level;
	}
	public List<TreeItem> getChildrens() {
		return childrens;
	}
	public int getViewHeight() {
		if(view==null||view.getHeight()==0){
			return viewHeight;
		}
		return view.getHeight();
	}
	public void setViewHeight(int viewHeight) {
		this.viewHeight = viewHeight;
	}

}
