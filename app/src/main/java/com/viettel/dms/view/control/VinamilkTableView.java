/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.control;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * Tao mot bang table layout co bo tron
 * 
 * @author: PhucNT
 * @version: 1.0
 * @since: 1.0
 */
public class VinamilkTableView extends LinearLayout implements
		OnEventControlListener, View.OnTouchListener {
	
	public static final int ACTION_CHANGE_PAGE = 1;
	public static final int LIMIT_ROW_PER_PAGE = 10;

	private final Context context;
	private final View v;
	// listener control load more for table
	private VinamilkTableListener mListener;

	// number item in one page
	private int numItemInPage = Constants.NUM_ITEM_PER_PAGE;
	// list table row
	private List<TableRow> listRowView = new ArrayList<TableRow>();
	private List<RelativeLayout> listRelativeView = new ArrayList<RelativeLayout>();
	
	// table content === table row list for this table
	private TableLayout tbContent;
	// header for table
	private VinamilkHeaderTable tbHeader;
	// control paging for table
	private PagingControl tbPaging;
	// total size
	private int totalSize;
	// duongdt
	private View rowNoContentSimpleHeader;
	private View rowNoContentComplexHeader;
	private TextView tvNoContentSimpleHeader;
	private TextView tvNoContentComplexHeader;

	/**
	 * init for vinamilk table view with context, attributeset
	 * 
	 * @param context
	 * @param attrs
	 */
	public VinamilkTableView(Context con, AttributeSet attrs) {
		super(con, attrs);
		context = con;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inflater.inflate(R.layout.layout_vinamilk_table, this);
		initView();
	}

	public VinamilkTableView(Context con) {
		super(con);
		context = con;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inflater.inflate(R.layout.layout_vinamilk_table, this);
		initView();
	}

	public VinamilkTableView(Context con, AttributeSet attrs, int defStyle) {
		super(con, attrs, defStyle);
		context = con;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = inflater.inflate(R.layout.layout_vinamilk_table, this);
		initView();
	}

	/**
	 * init controls in table view
	 * 
	 * @author : PhucNT param :
	 */
	private void initView() {
		tbHeader = (VinamilkHeaderTable) v.findViewById(R.id.header);
		tbHeader.setMyListener(this);
		//ẩn bàn phím khi nhấn vào header table 
		tbHeader.setOnTouchListener(this);
		tbContent = (TableLayout) v.findViewById(R.id.tbContent);
		tbPaging = (PagingControl) v.findViewById(R.id.paging);
		tbPaging.setItemListener(this);
		tbPaging.setAction(ACTION_CHANGE_PAGE);

		// duongdt no content row
		rowNoContentSimpleHeader = v.findViewById(R.id.rowNoContentSimpleHeader);
		rowNoContentComplexHeader = v.findViewById(R.id.rowNoContentComplexHeader);
		tvNoContentSimpleHeader = (TextView) v.findViewById(R.id.tvNoContentSimpleHeader);
		tvNoContentComplexHeader = (TextView)v.findViewById(R.id.tvNoContentComplexHeader);
	}

	public void setNumItemsPage(int num) {
		this.numItemInPage = num;
	}

	public void setListener(VinamilkTableListener l) {
		mListener = l;
	}

	public void disablePagingControl() {
		tbPaging.setVisibility(View.GONE);
	}

	public VinamilkHeaderTable getHeaderView() {
		//show header when call
		tbHeader.setVisibility(View.VISIBLE);
		return tbHeader;
	}

	public PagingControl getPagingControl() {
		return tbPaging;
	}

	/**
	 * set total so luong row trong bang
	 * 
	 * @author: PhucNT
	 * @return: void
	 * @throws:
	 */
	public void setTotalSize(int count) {
		this.totalSize = count;
		int page = 0;
		if (count % numItemInPage == 0) {
			page = count / numItemInPage;
		} else {
			page = count / numItemInPage + 1;
		}

		if (totalSize <= numItemInPage) {
			tbPaging.setVisibility(View.GONE);
		} else {
			tbPaging.setVisibility(View.VISIBLE);
			// tbPaging.setTotalPage(page);
		}
		tbPaging.setTotalPage(page);
		tbPaging.setCurrentPage(1);

	}

	/**
	 * tao nhung row cho table layout
	 * 
	 * @author: PhucNT
	 * @return: void
	 * @throws:
	 */
	public void addContent(List<TableRow> listRow) {

		// remove all object in list
		this.getListRowView().clear();

		for (TableRow row: listRow) {
			this.getListRowView().add(row);
		}
		
		// mac dinh hien thi trang dau tien
		updateTable();
	}

	/**
	 * tao nhung row cho table layout
	 * 
	 * @author: PhucNT
	 * @return: void
	 * @throws:
	 */
	public void addHighLightContent(List<TableRow> listRow, int isHighlight) {

		// remove all object in list
		this.getListRowView().clear();

		int size = listRow.size();
		for (int i = 0; i < size; i++) {
			this.getListRowView().add(listRow.get(i));
		}
		// mac dinh hien thi trang dau tien
		updateTable();
	}

	/**
	 * tao nhung row cho table layout
	 * 
	 * @author: PhucNT
	 * @return: void
	 * @throws:
	 */
	private void updateTable() {
		// mac dinh hien thi trang dau tien
		tbContent.removeAllViews();
		for (TableRow row: getListRowView()) {
			tbContent.addView(row);
		}

		// duongdt add no content row
		if (getListRowView().isEmpty()) {
			// show no content row
			showNoContentRow();
		} else {
			// show no content row
			hideNoContentRow();
		}
	}

	/**
	 * Show row no content
	 * 
	 * @author: duongdt 19:39:52 12 Oct 2013
	 * @return: void
	 * @throws:
	 */
	public void showNoContentRow() {
		// have header simple
		if (tbHeader.getHeaderCount() > 0) {
			rowNoContentSimpleHeader.setVisibility(VISIBLE);
			rowNoContentComplexHeader.setVisibility(GONE);
		} else {
			// if complex thì GONE tbContent
			rowNoContentSimpleHeader.setVisibility(GONE);
			rowNoContentComplexHeader.setVisibility(VISIBLE);
		}
		tbContent.setVisibility(GONE);
	}
	
	/**
	 * Hiển thị dòng với nội dung mong muốn.
	 * @author: dungdq3
	 * @return: void
	 */
	public void showNoContentRowWithString(String strContent) {
		// have header simple
		if (tbHeader.getHeaderCount() > 0) {
			rowNoContentSimpleHeader.setVisibility(VISIBLE);
			tvNoContentSimpleHeader.setText(strContent);
			rowNoContentComplexHeader.setVisibility(GONE);
		} else {
			// if complex thì GONE tbContent
			rowNoContentSimpleHeader.setVisibility(GONE);
			rowNoContentComplexHeader.setVisibility(VISIBLE);
			tvNoContentComplexHeader.setText(strContent);
		}
		tbContent.setVisibility(GONE);
	}

	/**
	 * Set text display when no row
	 * @param textNoContent
	 */
	public  void setNoContentText(String textNoContent) {
		tvNoContentComplexHeader.setText(textNoContent);
		tvNoContentSimpleHeader.setText(textNoContent);
	}
	
	/**
	 * Hide row no content
	 * 
	 * @author: duongdt 19:39:52 12 Oct 2013
	 * @return: void
	 * @throws:
	 */
	public void hideNoContentRow() {
		rowNoContentSimpleHeader.setVisibility(GONE);
		rowNoContentComplexHeader.setVisibility(GONE);
		tbContent.setVisibility(VISIBLE);
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub
		// updateTable();
		if (mListener != null && control == tbPaging) {
			mListener.handleVinamilkTableloadMore(this, data);
		} else if (mListener != null && control == tbHeader) {
			mListener.handleVinamilkTableRowEvent(eventType, this, data);
		}
	}

	/**
	 * @return the listRowView
	 */
	public List<TableRow> getListRowView() {
		return listRowView;
	}
	
	public List<RelativeLayout> getListRelativeView() {
		return listRelativeView;
	}
	
	/**
	 * @param listRowView
	 *            the listRowView to set
	 */
	public void setListRowView(List<TableRow> listRowView) {
		this.listRowView = listRowView;
	}

	@Override
	public boolean isInEditMode() {
		return true;
	}
	
	public void addRelativeLayout(RelativeLayout rl){
		tbContent.addView(rl);
		listRelativeView.add(rl);
	}

	/**
	* Thêm 1 dòng vào table.
	* @author: dungdq3
	* @param: TableRow row
	* @return: void
	*/
	public void addRow(TableRow row) {
		tbContent.addView(row);
		getListRowView().add(row);
	}

	/**
	* Kiểm tra xem có dòng nào ko?
	* @author: dungdq3
	* @return: void
	*/
	public void isEmptyOrNot() {
		// duongdt add no content row
		if (getListRowView().isEmpty()) {
			// show no content row
			showNoContentRow();
		} else {
			// show no content row
			hideNoContentRow();
		}
	}

	/**
	* Xóa toàn bộ dữ liệu của table
	* @author: dungdq3
	* @return: void
	*/
	public void clearAllData() {
		// TODO Auto-generated method stub
		getListRowView().clear();
		tbContent.removeAllViews();
		hideNoContentRow();
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		//ẩn khi nhấn vào header
		if (v == tbHeader) {
			GlobalUtil.forceHideKeyboardInput(getContext(), this);
		}
		return false;
	}
}
