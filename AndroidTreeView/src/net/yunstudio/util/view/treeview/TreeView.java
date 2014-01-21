package net.yunstudio.util.view.treeview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

public class TreeView extends LinearLayout{
	
//	private List<TreeItem> items;
	private List<TreeItem> sortedItems;
	private int animTime;

	public TreeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(LinearLayout.VERTICAL);
	}
	/**
	 * initialize data,you must make sure that each item has parent and the parent is also a one of the items, except the top ones.
	 * @param items the data to show 
	 * @param index the index of the tree to insert to
	 * 
	 */
	public void initData(Collection<TreeItem> items,int index){
		initData(items,index,0);
	}
	/**
	 * initialize data,you must make sure that each item has parent except the top ones.
	 * @param items the data to show 
	 * @param index the index of the tree to insert to
	 * @param animTime if you want expand animation, 
	 * you can set the time(ms) of animation,otherwise you can set 0.
	 * 
	 */
	public void initData(Collection<TreeItem> items,int index,int animTime){
		if(items==null||items.size()==0){
			return ;
		}

		sortItemList(items);
		
		int size=sortedItems.size();
		
		initAddIndex=index<0?-Integer.MAX_VALUE:index;
		
		for (int i=0;i<size;i++) {
			TreeItem item=sortedItems.get(i);
			recuseShow(item);
		}
		
		enabledAnim(animTime);
	}
	private boolean isAnim=false;
	/**
	 * 设置为0则关闭动画
	 * @param animTime
	 */
	public void enabledAnim(int animTime) {
		if(animTime<=0){
			isAnim=false;
			return ;
		}
		this.animTime=animTime;
		isAnim=true;
	}
	
	private int initAddIndex; 
	
	private void recuseShow(TreeItem item){
		View view=item.getView();
		addView(view,initAddIndex);
		if(item.getParent()!=null){
			view.setVisibility(View.GONE);
		}else {
			view.setVisibility(View.VISIBLE);
		}
		initAddIndex++;
		List<TreeItem> childrens=item.getChildrens();
		if(childrens.size()>0){
			for (TreeItem it : childrens) {
				recuseShow(it);
			}
		}
	}
	
	private void sortItemList(Collection<TreeItem> items) {
		//把items按照层级关系存放，sortedItems只存放顶层的item
		sortedItems=new ArrayList<TreeItem>(5);
		for (TreeItem item : items) {
			if(item.getParent()==null){
				sortedItems.add(item);
			}else {
				item.getParent().getChildrens().add(item);
			}
		}
		
	}
	
	
	private int viewIndex=0;
	private int animHeight=0;
	private void addChild(TreeItem item,boolean isRecurse){
		if(item.getChildrens().size()>0){
			List<TreeItem> list=item.getChildrens();
			for (TreeItem it :	list) {
				View view=it.getView();
				if(view.isShown()){
					continue;
				}
				viewIndex++;
				view.setVisibility(View.VISIBLE);
				if(isAnim){
					animHeight-=it.getViewHeight();
				}
				it.nextIsExpand=true;
				if(isRecurse){
					addChild(it,true);
				}
			}
		}
	}
	private synchronized void removeChild(TreeItem item,boolean isRecurse){
		if(item.getChildrens().size()>0){
			List<TreeItem> list=item.getChildrens();
			for (TreeItem it :	list) {
				View view=it.getView();
				if(!view.isShown()){
					continue;
				}
				
				TranslateAnimation ta=new TranslateAnimation(0, 0, 0, 0);
				ta.setFillBefore(true);
				ta.setDuration(1000);
				view.startAnimation(ta);
				view.setVisibility(View.GONE);
				if(isAnim){
					animHeight+=it.getViewHeight();
				}
				if(isRecurse){
					removeChild(it,true);
				}
			}
		}
	}

	private void animAdd(){
		TranslateAnimation ta=new TranslateAnimation(
				Animation.ABSOLUTE, 0, 
				Animation.ABSOLUTE, 0, 
				Animation.ABSOLUTE, animHeight, 
				Animation.ABSOLUTE, 0);
		ta.setFillBefore(true);
		ta.setFillAfter(false);
		ta.setDuration(animTime);
		
		for (int i = viewIndex+1; i < getChildCount(); i++) {
			View view=getChildAt(i);
			if(view.isShown()){
				view.setAnimation(ta);
			}
		}
		ta.startNow();
		animHeight=0;
	}
	private void animRemove(){
		TranslateAnimation ta=new TranslateAnimation(
				Animation.ABSOLUTE, 0, 
				Animation.ABSOLUTE, 0, 
				Animation.ABSOLUTE, animHeight, 
				Animation.ABSOLUTE, 0);
		ta.setFillAfter(false);
		ta.setFillBefore(true);
		ta.setDuration(animTime);
		
		int startAnimIndex;
		startAnimIndex=viewIndex+1;
		for (int i = startAnimIndex; i < getChildCount(); i++) {
			View view=getChildAt(i);
			if(view.isShown()){
//				view.startAnimation(ta);
				view.setAnimation(ta);
			}
		}
		ta.startNow();
		animHeight=0;
	}
	
	/**
	 * 展开某一个节点的下一级子元素
	 * @param item
	 */
	public void expand(TreeItem item){
		viewIndex=indexOfChild(item.getView());
		addChild(item,false);
		if(isAnim){
			animAdd();
		}
	}
	
	/**
	 * 展开某一个节点的所有子元素
	 * @param item
	 */
	public void expandAllChildren(TreeItem item) {
		viewIndex=indexOfChild(item.getView());
		addChild(item,true);
		if(isAnim){
			animAdd();
		}
	}
	/**
	 * 展开所有节点
	 * @param item
	 */
	public void expandAll(){
		if(sortedItems==null){
			return ;
		}
		for (TreeItem item : sortedItems) {
			expandAllChildren(item);
		}
	}
	/**
	 * 收缩某一个节点的所有子元素
	 * @param item
	 */
	public void contractAllChildren(TreeItem item) {
		viewIndex=indexOfChild(item.getView())+1;
		removeChild(item,true);
		if(isAnim){
			animRemove();
		}
	}

	/**
	 * 收缩所有节点
	 * @param item
	 */
	public void contractAll(){
		if(sortedItems==null){
			return ;
		}
		for (TreeItem item : sortedItems) {
			contractAllChildren(item);
		}
	}
	/**
	 * 绑定点击事件，每隔一次展开、每隔一次收缩
	 * @param item
	 */
	public void bind(TreeItem item) {
		if(item.nextIsExpand){
			expand(item);
		}else {
			contractAllChildren(item);
		}
		item.nextIsExpand=!item.nextIsExpand;
	}
	
	
}
