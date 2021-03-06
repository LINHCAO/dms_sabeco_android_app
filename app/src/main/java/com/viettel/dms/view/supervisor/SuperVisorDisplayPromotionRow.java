/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.db.DisplayProgrameDTO;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * SuperVisor Display Promotion Row
 * SuperVisorDisplayPromotionRow.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:30:35 14 Jan 2014
 */
@SuppressLint("ViewConstructor")
public class SuperVisorDisplayPromotionRow extends TableRow implements OnClickListener {

	public static final int ACTION_CLICK_CODE_PROGRAM = 1;
	Context _context;
	View view;
	//so thu tu
	TextView tvSTT;
	//ma chuong trinh
	TextView tvCode;
	//ten chuong trinh
	TextView tvName;
	//tu ngay
	TextView tvDateStart;
	//den ngay
	TextView tvDateEnd;
	//loai chuong trinh
	TextView tvDisplayPromotionDepart;
	//thong bao khong co du lieu
	TextView tvNoResultInfo;

	// listener
	protected VinamilkTableListener listener;
	private TableRow row;
	//private Object currentDTO;

	public SuperVisorDisplayPromotionRow(Context context, View aRow) {
		super(context);
		_context = context;
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_supervisor_display_promotion_row, this);
		row = (TableRow)view.findViewById(R.id.row);
		row.setOnClickListener(this);
		tvSTT = (TextView) view.findViewById(R.id.tvDisplayPromotionSTT);
		tvCode = (TextView) view.findViewById(R.id.tvDisplayPromotionCode);
//		tvCode.setOnClickListener(this);
		tvName = (TextView) view.findViewById(R.id.tvDisplayPromotionName);
		tvDateStart = (TextView) view.findViewById(R.id.tvDisplayPromotionDateStart);
		tvDateEnd = (TextView) view.findViewById(R.id.tvDisplayPromotionDateEnd);
		tvDisplayPromotionDepart = (TextView) view.findViewById(R.id.tvDisplayPromotionDepart);
		tvNoResultInfo = (TextView) view.findViewById(R.id.tvNoResultInfo);
	}
	/**
	 * 
	*  lay chieu rong cua row
	*  @author: ThanhNN8
	*  @return
	*  @return: int[]
	*  @throws:
	 */
	public int[] getWidthColumns(){
	
		final int size = row.getChildCount();
		final int[] widths = new int[size];
		
		ViewTreeObserver vto = this.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				for (int i = 0; i < size; i++) {
					widths[i] = row.getChildAt(i).getWidth();		
				}
				getViewTreeObserver()
						.removeGlobalOnLayoutListener(this);
			}
		});
		return widths;
		
	}
	public void setListener(VinamilkTableListener listener) {
		this.listener = listener;
	}
	
	/**
	 * 
	*  render du lieu cho row
	*  @author: ThanhNN8
	*  @param position
	*  @param item
	*  @return: void
	*  @throws:
	 */
	public void renderLayout(int position, DisplayProgrameDTO item) {
		//currentDTO = item;
		tvSTT.setText("" + position);
		tvCode.setText(item.displayProgrameCode);
		tvName.setText(item.displayProgrameName);
		tvDateStart.setText(item.fromDate);
		tvDateEnd.setText(item.toDate);
		tvDisplayPromotionDepart.setText(item.cat);
	}
	
	/**
	 * 
	*  Layout row thong bao khong co du lieu
	*  @author: Nguyen Thanh Dung
	*  @return: void
	*  @throws:
	 */
	public void renderLayoutNoResult() {
		tvSTT.setVisibility(View.GONE);
		tvCode.setVisibility(View.GONE);
		tvName.setVisibility(View.GONE);
		tvDateStart.setVisibility(View.GONE);
		tvDateEnd.setVisibility(View.GONE);
		tvDisplayPromotionDepart.setVisibility(GONE);
		tvNoResultInfo.setVisibility(View.VISIBLE);
		tvNoResultInfo.setText(StringUtil.getString(R.string.TEXT_CTTB_INFO_NORESULT));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		if (v == tvCode) {
//			listener.handleVinamilkTableRowEvent(ActionEventConstant.GO_TO_LIST_CUSTOMER_ATTEND_PROGRAM, null, this.currentDTO);
//		}
		if (v == row && _context != null){
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity)_context);
		}
	}
	public View getView() {
		return view;
	}
}
