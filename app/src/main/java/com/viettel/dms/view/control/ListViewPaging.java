/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.control;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.sabeco.R;

/**
 * @author banghn
 *	com.viettel.vinamilk.view.control.ListViewPaging
 */
public class ListViewPaging extends LinearLayout {
	OnEventControlListener listener;
	Context mContext;
	ListView lvList;
	PagingControl paging;
	LinearLayout header;
	
	public ListViewPaging(Context context) {
		super(context);
		listener = (OnEventControlListener) context;
		mContext = context;
		initListViewPaging();
	}
	
	public ListViewPaging(Context context, AttributeSet attrs) {
		super(context, attrs);
		listener = (OnEventControlListener) context;
		mContext = context;
		initListViewPaging();
	}

	/**
	 * Action change page
	 * @author : BangHN
	 * since : 3:33:49 PM
	 */
	public void setActionChangePage(int act){
		paging.setAction(act);
	}
	
	
	public int getCurrentPage(){
		return paging.getCurrentPage();
	}
	
	public void setTotalPage(int page){
		paging.setTotalPage(page);
	}
	
	public int getTotalPage(){
		return paging.totalPage;
	}
	
	public void setCurrentPage(int page){
		paging.setCurrentPage(page);
	}
	

	/**
	 * init layout control list paging
	 * @author : BangHN
	 * since : 10:16:44 AM
	 */
	private void initListViewPaging(){
		LayoutInflater in = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = in.inflate(R.layout.layout_list_view_paging, this);
		lvList = (ListView)v.findViewById(R.id.lVList);
		paging = (PagingControl)v.findViewById(R.id.paging);
		header = (LinearLayout)v.findViewById(R.id.header);
	}
	
	
	/**
	 * Set adapter for listview 
	 * @author : BangHN
	 * since : 10:17:18 AM
	 */
	public void setAdapter(ListAdapter adapter){
		lvList.setAdapter(adapter);
	}
	
	/**
	 * Add header outside listview
	 * @author : BangHN
	 * since : 10:17:33 AM
	 */
	public void addHeaderView(View header){
		this.header.addView(header);
	}
	
	
	/**
	 * Add header inside listview
	 * @author : BangHN
	 * since : 10:17:48 AM
	 */
	public void addHeaderViewInListView(View header){
		lvList.addHeaderView(header);		
	}
	
	/**
	 * Remove header view (inside & outside)
	 * @author : BangHN
	 * since : 10:18:05 AM
	 */
	public void removeHeaderView(View header){
		lvList.removeHeaderView(header);
		this.header.removeAllViews();
	}

	/**
	 * An hien control paging
	 * @author : BangHN
	 * since : 10:29:57 AM
	 */
	public void setPagingVisible(int visible){
		paging.setVisibility(visible);
	}
}
