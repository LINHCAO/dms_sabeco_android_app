/**
 * Copyright 2015 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.view.control;

import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.viettel.dms.view.control.DMSColSortInfo.OnChangeSortState;

/**
 * DMS Col Sort Manager
 * 
 * @author: duongdt3
 * @version: 1.0
 * @since: 13:40:04 26 Mar 2015
 */
public class DMSColSortManager {
	private SparseArray<DMSColSortInfo> lstSortInfo;
	private DMSColSortInfo currentColSortInfo;

	public static interface OnSortChange {
		void onSortChange(DMSTableView tb, DMSSortInfo sortInfo);
	}

	public DMSColSortManager(DMSTableView tb) {
		this.tb = tb;
	}

	private DMSTableView tb;
	private OnSortChange sortListener;

	/**
	 * @param listener
	 *            the listener to set
	 */
	public void setSortListener(OnSortChange listener) {
		this.sortListener = listener;
	}

	/**
	 * set header sort info
	 * 
	 * @author: duongdt3
	 * @since: 10:02:10 26 Mar 2015
	 * @return: void
	 * @throws:
	 * @param lstSort
	 */
	public void setHeaderSortInfo(ViewGroup row, SparseArray<DMSColSortInfo> lstSort) {
		this.lstSortInfo = lstSort;
			if (this.lstSortInfo != null && row != null) {
				// view seq begin at 0
				int viewSeq = 0;
				// set view into ColSortInfo List
				for (int i = 0, size = row.getChildCount(); i < size; i++) {
					View view = row.getChildAt(i);
					View viewCell = null;
					// la viewgroup co chua view con ben trong, can xu ly them
					if (view instanceof ViewGroup) {
						ViewGroup viewGroup = (ViewGroup) view;
						// truong hop viewGroup chi co 1 view con, day la view can xu ly
						// sort
						int childCount = viewGroup.getChildCount();
						if (childCount == 1) {
							viewCell = viewGroup.getChildAt(0);
						} else if(childCount > 1){
							// neu co nhieu view con, can phan lua chon dung view
							View lastView = viewGroup.getChildAt(childCount - 1);
							if (lastView instanceof ViewGroup) {
								ViewGroup lastGroup = (ViewGroup) lastView;
								for (int j = 0, sizeLastGr = lastGroup.getChildCount(); j < sizeLastGr; j++) {
									View viewInLastGr = lastGroup.getChildAt(j);
									if (viewInLastGr != null && viewInLastGr instanceof TextView) {
										// tang so thu tu view len
										viewSeq++;
										//get info same viewSeq
										DMSColSortInfo info = this.lstSortInfo.get(viewSeq);
										if (info != null) {
											// set view
											info.setView(viewInLastGr);
										}
									}
								}
							}
							
						}
					} else {// la view khong can xu ly them
						viewCell = view;
					}
					
					// hien tai viewCell can phai la TextView moi co the add them
					// Drawable
					// co the sau nay bo sung cac loai view khac, ko can la TextView
					if (viewCell != null && viewCell instanceof TextView) {
						// tang so thu tu view len
						viewSeq++;
						//get info same viewSeq
						DMSColSortInfo info = this.lstSortInfo.get(viewSeq);
						if (info != null) {
							// set view
							info.setView(viewCell);
						}
					}
					
					// add listener, init sort info
					// add listener click change sort
					for (int iSort = 0, sizeSort = this.lstSortInfo.size(); iSort < sizeSort; iSort++) {
						DMSColSortInfo info = this.lstSortInfo.valueAt(iSort);
						info.setChangeSortStateListener(onSortState);
						View viewInfo = info.getView();
						if (viewInfo != null) {
							// init info before ready
							viewInfo.setOnClickListener(onHeaderClick);
						}
						info.init();
					}
			}
		}
	}
	
	OnClickListener onHeaderClick = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			DMSSortInfo sortInfo = doChangeSort(view);
			
			if (sortInfo != null && sortListener != null) {
				sortListener.onSortChange(tb, sortInfo);
			}
		}
	};

	OnChangeSortState onSortState = new OnChangeSortState() {
		@Override
		public void onChangeSortState(DMSColSortInfo info) {
			View view = info.getView();
			if (view != null && view instanceof TextView) {
				TextView tView = (TextView) view;
				int type = info.getSortInfo().getSortType();
				tView.setCompoundDrawablePadding(0);
				switch (type) {
//				case DMSSortInfo.ASC_TYPE:
//					tView.setCompoundDrawablesWithIntrinsicBounds(
//							0, 0, 0, R.drawable.icon_sort_asc);
//					break;
//				case DMSSortInfo.DESC_TYPE:
//					tView.setCompoundDrawablesWithIntrinsicBounds(
//							0, 0, 0, R.drawable.icon_sort_desc);
//					break;
//				case DMSSortInfo.NO_SORT_TYPE:
//					tView.setCompoundDrawablesWithIntrinsicBounds(
//							0, 0, 0, R.drawable.icon_sort_default);
//					break;
				default:
					tView.setCompoundDrawablesWithIntrinsicBounds(
							0, 0, 0, 0);
					break;
				}
			}
		}
	};
	
	/**
	 * set sort for col with sortType
	 * 
	 * @author: duongdt3
	 * @since: 10:05:36 26 Mar 2015
	 * @return: void
	 * @throws:
	 * @param sortColSeq
	 * @param sortType
	 */
	public void setCurrentSortInfo(int sortColSeq, int sortType) {
		if (this.lstSortInfo != null) {
			DMSColSortInfo info = this.lstSortInfo.get(sortColSeq);
			if (info != null) {
				setCurrentSortInfo(info);
			}
		}
	}

	/**
	 * do change sort
	 * 
	 * @author: duongdt3
	 * @since: 10:37:46 26 Mar 2015
	 * @return: DMSSortInfo
	 * @throws:
	 * @param view
	 * @return
	 */
	private DMSSortInfo doChangeSort(View view) {
		DMSColSortInfo sortInfo = null;
		for (int i = 0, size = this.lstSortInfo.size(); i < size; i++) {
			DMSColSortInfo info = this.lstSortInfo.valueAt(i);
			if (view == info.getView()) {
				sortInfo = info;
				break;
			}
		}
		
		if (sortInfo != null) {
			setCurrentSortInfo(sortInfo);
		}

		return getCurrentSortInfo();
	}

	/**
	 * @return the currentSortInfo
	 */
	public DMSSortInfo getCurrentSortInfo() {
		return (currentColSortInfo != null ? currentColSortInfo.getSortInfo()
				: null);
	}

	/**
	 * set current sort info
	 * 
	 * @author: duongdt3
	 * @since: 11:11:05 26 Mar 2015
	 * @return: void
	 * @throws:
	 * @param sortInfo
	 */
	private void setCurrentSortInfo(DMSColSortInfo sortInfo) {
		if (sortInfo != null) {
			// is not current info, reset sort state current (current not null)
			if (currentColSortInfo != null && sortInfo != currentColSortInfo) {
				currentColSortInfo.resetSortState();
			}
			// set col sort info
			currentColSortInfo = sortInfo;
			// change state sort
			currentColSortInfo.changeSortState();
		}
	}

	/**
	 * reset current sort info
	 * 
	 * @author: duongdt3
	 * @since: 09:29:54 27 Mar 2015
	 * @return: void
	 * @throws:
	 */
	public void resetCurrentSortInfo() {
		// reset sort info
		if (currentColSortInfo != null) {
			currentColSortInfo.resetSortState();
		}

		// set null sort info
		currentColSortInfo = null;
	}
}
