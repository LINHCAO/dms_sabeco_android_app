/**
 * Copyright 2015 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.view.control;

import android.view.View;

/**
 * DMS Col Sort Info
 * @author: duongdt3
 * @version: 1.0 
 * @since:  09:40:16 26 Mar 2015
 */
public class DMSColSortInfo{
	private View view;
	private int colSeq;
	private DMSSortInfo sortInfo;
	
	public DMSColSortInfo(int colSeq, int sortAction) {
		super();
		this.colSeq = colSeq;
		this.sortInfo = new DMSSortInfo(sortAction, DMSSortInfo.NO_SORT_TYPE);
	}

	/**
	 * @return the view
	 */
	public View getView() {
		return view;
	}

	/**
	 * @param view the view to set
	 */
	public void setView(View view) {
		this.view = view;
	}

	/**
	 * @return the sortInfo
	 */
	public DMSSortInfo getSortInfo() {
		return sortInfo;
	}

	/**
	 * @param sortInfo the sortInfo to set
	 */
	public void setSortInfo(DMSSortInfo sortInfo) {
		this.sortInfo = sortInfo;
	}

	/**
	 * @return the colSeq
	 */
	public int getColSeq() {
		return colSeq;
	}

	/**
	 * @param colSeq the colSeq to set
	 */
	public void setColSeq(int viewSeq) {
		this.colSeq = viewSeq;
	}
	
	public static interface OnChangeSortState{
		void onChangeSortState(DMSColSortInfo info);
	}

	OnChangeSortState listener;
	/**
	 * @author: duongdt3
	 * @since: 10:57:36 26 Mar 2015
	 * @return: void
	 * @throws:  
	 */
	public void setChangeSortStateListener(OnChangeSortState listener) {
		this.listener = listener;
	}
	
	/**
	 * @author: duongdt3
	 * @since: 10:16:57 26 Mar 2015
	 * @return: void
	 * @throws:  
	 */
	public void changeSortState() {
		switch (sortInfo.getSortType()) {
		case DMSSortInfo.ASC_TYPE:
			setSortType(DMSSortInfo.DESC_TYPE);
			break;
		case DMSSortInfo.DESC_TYPE:
			setSortType(DMSSortInfo.ASC_TYPE);
			break;
		case DMSSortInfo.NO_SORT_TYPE:
			setSortType(DMSSortInfo.ASC_TYPE);
			break;
		}
	}
	
	public void resetSortState() {
		setSortType(DMSSortInfo.NO_SORT_TYPE);
	}

	/**
	 * @author: duongdt3
	 * @since: 10:36:28 26 Mar 2015
	 * @return: void
	 * @throws:  
	 * @param sortType2
	 */
	public void setSortType(int pSortType) {
		switch (pSortType) {
		case DMSSortInfo.ASC_TYPE:
		case DMSSortInfo.DESC_TYPE:
		case DMSSortInfo.NO_SORT_TYPE:
			break;
			//mac dinh la tang dan
		default:
			pSortType = DMSSortInfo.ASC_TYPE;
			break;
		}
		sortInfo.setSortType(pSortType);
		if (listener != null) {
			listener.onChangeSortState(this);
		}
	}
	
	/**
	 * init info
	 * @author: duongdt3
	 * @since: 11:07:14 26 Mar 2015
	 * @return: void
	 * @throws:  
	 */
	public void init() {
		if (this.view != null) {
			//re-set padding, prepare for set icon
			int pLeft = view.getPaddingLeft();
			int pRight = view.getPaddingRight();
			int pTop = view.getPaddingTop();
			int pBottom = view.getPaddingBottom();
			//change padding for set icon sort
			if (pBottom > 0) {
				pTop += pBottom; 
				pBottom = 0;
				view.setPadding(pLeft, pTop, pRight, pBottom);
			}
		}
		
		if (listener != null) {
			listener.onChangeSortState(this);
		}
	}
}
