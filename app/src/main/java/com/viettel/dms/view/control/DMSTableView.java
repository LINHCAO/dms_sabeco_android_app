/**
 * Copyright 2015 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.view.control;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.view.control.DMSColSortManager.OnSortChange;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * DMSTableView.java
 *
 * @author: dungdq3
 * @version: 1.0
 * @since: 11:35:27 AM Sep 18, 2014
 */
public class DMSTableView extends LinearLayout implements
		OnEventControlListener, View.OnTouchListener {
	public static final int ACTION_CHANGE_PAGE = 1;
	public static final int LIMIT_ROW_PER_PAGE = 10;

	// listener control load more for table
	private VinamilkTableListener mListener;

	private final Context con;

	// number item in one page
	private int numItemInPage = Constants.NUM_ITEM_PER_PAGE;
	// table content === table row list for this table
	private LinearLayout tbContent;
	// control paging for table
	private PagingControl tbPaging;
	private TextView tvNoResult;
	DMSTableRow header;// header cho table view
	ArrayList<DMSTableRow> lstChildRow = new ArrayList<DMSTableRow>();

	public DMSTableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		con = context;
		initView(context);
	}

	public DMSTableView(Context context) {
		super(context);
		con = context;
		initView(context);
	}

	public DMSTableView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		con = context;
		initView(context);
	}

	/**
	 * init controls in table view
	 *
	 * @author : PhucNT param :
	 */
	private void initView(Context context) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View table = inflater.inflate(R.layout.layout_dms_table, this);
		tbContent = (LinearLayout) table.findViewById(R.id.tbContent);
		tbPaging = (PagingControl) table.findViewById(R.id.paging);
		tbPaging.setItemListener(this);
		tbPaging.setAction(ACTION_CHANGE_PAGE);
		tvNoResult = (TextView) table.findViewById(R.id.tvNoResult);
		tvNoResult.setVisibility(GONE);
	}

	public void setNumItemsPage(int num) {
		this.numItemInPage = num;
	}

	public void setListener(VinamilkTableListener l) {
		mListener = l;
	}

	public void disablePagingControl() {
		tbPaging.setVisibility(GONE);
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
	public void setTotalSize(int count, int currentPage) {
		int page = 0;
		if (count % numItemInPage == 0) {
			page = count / numItemInPage;
		} else {
			page = count / numItemInPage + 1;
		}

		if (count <= numItemInPage) {
			tbPaging.setVisibility(View.GONE);
		} else {
			tbPaging.setVisibility(View.VISIBLE);
			// tbPaging.setTotalPage(page);
		}
		tbPaging.setTotalPage(page);
		tbPaging.setCurrentPage(currentPage);
	}

	 /**
	 * add du lieu cho table
	 * @author: Tuanlt11
	 * @param row
	 * @return: void
	 * @throws:
	*/
	public void addRow(DMSTableRow row) {
		tbContent.addView(row);
		lstChildRow.add(row);
		tvNoResult.setVisibility(GONE);
	}

	/**
	 * add row sum
	 * @author: duongdt3
	 * @since: 11:06:49 3 Apr 2015
	 * @return: void
	 * @throws:
	 * @param row
	 */
	public void addRowSum(DMSTableRow row) {
		if (lstChildRow != null && !lstChildRow.isEmpty()) {
			addRow(row);
		}
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub
		// updateTable();
		if (mListener != null && control == tbPaging) {
			mListener.handleVinamilkTableloadMore(this, data);
		}
	}


	 /**
	 * Add header cho tableview
	 * @author: Tuanlt11
	 * @param header
	 * @return: void
	 * @throws:
	*/
	public void addHeader(DMSTableRow header){
		addHeader(header, null);
	}

	/**
	 * add header with sort feature
	 * @author: duongdt3
	 * @since: 09:53:30 27 Mar 2015
	 * @return: void
	 * @throws:
	 * @param header
	 * @param lstSort
	 * @param sortListener
	 */
	public void addHeader(DMSTableRow header, ArrayList<String> lstTitle, SparseArray<DMSColSortInfo> lstSort, OnSortChange sortListener){
		addHeader(header, lstTitle);
		//add sort info for sort manager
		setColSortInfoList(lstSort, sortListener);
	}

	 /**
	 * Add header cho table view va title cho header
	 * @author: Tuanlt11
	 * @param header
	 * @param lstTitle: ds title cho header
	 * @return: void
	 * @throws:
	*/
	public void addHeader(DMSTableRow header, ArrayList<String> lstTitle){
		this.header = header;
		// render header
		this.header.getHeaderTable(lstTitle);
		tbContent.addView(header, 0);
	}

	 /**
	 * Clear noi dung cua table
	 * @author: Tuanlt11
	 * @return: void
	 * @throws:
	*/
	public void clearAllData() {
		clearAllData(false);
	}

	/**
	 * clear data with clear header
	 * @author: duongdt3
	 * @since: 11:15:28 4 Apr 2015
	 * @return: void
	 * @throws:
	 */
	public void clearAllDataAndHeader() {
		clearAllData(true);
	}

	/**
	 * clear data with option clear header
	 * @author: duongdt3
	 * @since: 11:14:35 4 Apr 2015
	 * @return: void
	 * @throws:
	 * @param clearHeader
	 */
	private void clearAllData(boolean clearHeader) {
		// clear header
		if (clearHeader){
			tbContent.removeAllViews();
			header = null;
		}else {
			for (int i = 1, size = tbContent.getChildCount(); i < size; i++) {
				tbContent.removeViewAt(i);
				i--;
				size--;
			}
		}
		showNoContentRow();
		lstChildRow.clear();
	}

	/**
	 * Xoa tat ca row trong table va add row, title cho header
	 *
	 * @author: Tuanlt11
	 * @param header
	 * @param listTitle: ds list title cho header
	 * @return: void
	 * @throws:
	 */
	public void clearAllData(DMSTableRow header, ArrayList<String> lstTitle) {
		tbContent.removeAllViews();
		showNoContentRow();
		this.header = header;
		this.header.getHeaderTable(lstTitle);
		tbContent.addView(header);
		lstChildRow.clear();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// ẩn khi nhấn vào header
		GlobalUtil.forceHideKeyboardInput(con, this);
		return false;
	}


	/**
	 * set text for text view no result
	 *
	 * @author: dungdq3
	 * @since: 4:59:39 PM Sep 19, 2014
	 * @return: void
	 * @throws:
	 * @param noResult
	 *            :
	 */
	public void showNoContentRow(String noResult) {
		if (isShowNoContent) {
			tvNoResult.setVisibility(VISIBLE);
			tvNoResult.setText(noResult);
		}
	}

	public void showNoContentRow() {
		if (isShowNoContent) {
			tvNoResult.setVisibility(VISIBLE);
		}
	}

	/**
	 * get current page
	 *
	 * @author: dungdq3
	 * @since: 10:06:00 AM Oct 22, 2014
	 * @return: int
	 * @throws:
	 * @return:
	 */
	public int getCurrentPage() {
		return tbPaging.getCurrentPage();
	}

	 /**
	 * Enable tat cac su kien cua header
	 * @author: Tuanlt11
	 * @param isEnable
	 * @return
	 * @throws:
	*/
	public void enableAllHeaderEvent(boolean isEnable) {
		for (int i = 0, size = header.getChildCount(); i < size; i++) {
			View child = header.getChildAt(i);
			if (child instanceof ViewGroup) {
				for (int j = 0, siz = ((ViewGroup) child).getChildCount(); j < siz; j++) {
					child.setEnabled(isEnable);
				}
			} else {
				child.setEnabled(isEnable);
			}
		}
	}

	 /**
	 *  enable su kien  cho child cu theo trong header
	 * @author: Tuanlt11
	 * @param child
	 * @param isEnable
	 * @return: void
	 * @throws:
	*/
	public void enableHeaderEvent(View child, boolean isEnable) {
		child.setEnabled(isEnable);
	}

	 /**
	 * Tra ve ds row con  trong table
	 * @author: Tuanlt11
	 * @return
	 * @return: ArrayList<DMSTableRow>
	 * @throws:
	*/
	public ArrayList<DMSTableRow> getListChildRow(){
		return lstChildRow;
	}

	/**
	 * header sort manager
	 */
	DMSColSortManager sortManager = new DMSColSortManager(this);

	/**
	 * set column info
	 * @author: duongdt3
	 * @since: 13:46:37 26 Mar 2015
	 * @return: void
	 * @throws:
	 * @param lstSort
	 * @param sortColSeq
	 * @param sortType
	 * @param sortListener
	 */
	public void setColSortInfoList(SparseArray<DMSColSortInfo> lstSort, OnSortChange sortListener){
		if (header != null) {
			//listener sort change
			sortManager.setSortListener(sortListener);
			//set sort info
			sortManager.setHeaderSortInfo(header, lstSort);
		}
	}

	/**
	 * reset sort info
	 * @author: duongdt3
	 * @since: 09:29:15 27 Mar 2015
	 * @return: void
	 * @throws:
	 */
	public void resetSortInfo() {
		sortManager.resetCurrentSortInfo();
	}

	/**
	 * @return the currentSortInfo
	 */
	public DMSSortInfo getSortInfo() {
		return (sortManager != null ? sortManager.getCurrentSortInfo() : null);
	}

	/**
	 * check exists header
	 * @author: duongdt3
	 * @since: 11:27:57 6 Apr 2015
	 * @return: void
	 * @throws:
	 */
	public boolean isHeaderExists() {
		return (header != null);
	}
	
	private boolean isShowNoContent = true;
	
	/**
	 * @return the isShowNoContent
	 */
	public boolean isShowNoContent() {
		return isShowNoContent;
	}
	
	/**
	 * @param isShowNoContent the isShowNoContent to set
	 */
	public void setShowNoContent(boolean isShowNoContent) {
		this.isShowNoContent = isShowNoContent;
	}

	/**
	 * remove row
	 * @author: duongdt3
	 * @since: 10:08:51 8 Apr 2015
	 * @return: void
	 * @throws:
	 * @param indexParent
	 */
	public void removeRowAt(int indexRemove) {
		int headerIndex = header != null ? 1 : 0;
		int size = tbContent.getChildCount() - headerIndex;
		if (indexRemove >= 0 && indexRemove < size) {
			tbContent.removeViewAt(indexRemove + headerIndex);
		}
	}

	/**
	 * @author: duongdt3
	 * @since: 10:14:30 8 Apr 2015
	 * @return: OrderProductRow
	 * @throws:
	 * @param indexRow
	 * @return
	 */
	public DMSTableRow getRowAt(int indexRow) {
		DMSTableRow row = null;
		int headerIndex = header != null ? 1 : 0;
		int size = tbContent.getChildCount() - headerIndex;
		if (indexRow >= 0 && indexRow < size) {
			row = (DMSTableRow) tbContent.getChildAt(indexRow + headerIndex);
		}
		return row;
	}
}