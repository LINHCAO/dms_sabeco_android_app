/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.promotion;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.db.ProInfoDTO;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * 
 * row CTKM NVBH
 * 
 * @author: YenNTH
 * @version: 1.0
 * @since: 1.0
 */
public class PromotionProgrameRow extends DMSTableRow implements OnClickListener{
	public static final int ACTION_CLICK_CODE_PROGRAM = 1;
//	Context _context;
//	View view;
	//so thu tu
	TextView tvSTT;
	//ma chuong trinh
	TextView tvCode;
	//ten chuong trinh
	TextView tvName;
	//ngay bat dau - ket thuc cua chuong trinh
	TextView tvDate;
	// thong bao khong co du lieu
//	TextView tvNoResultInfo;
	
	ProInfoDTO currentDTO;
	// listener
//	protected VinamilkTableListener listener;
//	private LinearLayout row;

	public PromotionProgrameRow(Context context, PromotionProgramView lis) {
		super(context, R.layout.layout_promotion_row);
		setListener(lis);
//		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		view = vi.inflate(R.layout.layout_promotion_row, this);
//		row = (LinearLayout)this.findViewById(R.id.row);
//		row.setOnClickListener(this);
		tvSTT = (TextView) this.findViewById(R.id.tvPromotionSTT);
		tvCode = (TextView) this.findViewById(R.id.tvPromotionCode);
		tvCode.setOnClickListener(this);
		tvName = (TextView) this.findViewById(R.id.tvPromotionName);
		tvDate = (TextView) this.findViewById(R.id.tvPromotionDate);
//		tvNoResultInfo = (TextView) view.findViewById(R.id.tvNoResultInfo);
	}

	/**
	 * 
	 * lay chieu rong cua mot row
	 * 
	 * @author: SOAN
	 * @return
	 * @return: int[]
	 * @throws:
	 */
//	public int[] getWidthColumns(){
//	
//		final int size = row.getChildCount();
//		final int[] widths = new int[size];
//		
//		ViewTreeObserver vto = this.getViewTreeObserver();
//		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
//			@Override
//			public void onGlobalLayout() {
//				for (int i = 0; i < size; i++) {
//					widths[i] = row.getChildAt(i).getWidth();		
//				}
//				getViewTreeObserver()
//						.removeGlobalOnLayoutListener(this);
//			}
//		});
//		return widths;
//		
//	}
//	public void setListener(VinamilkTableListener listener) {
//		this.listener = listener;
//	}
//	 
	/**
	 * set màu nền cho các view.
	 * @author: quangvt1
	 * @since: 09:00:23 12-06-2014
	 * @return: void
	 * @throws:  
	 * @param color
	 */
	private void setBackGroundColor(int color) { 
		tvSTT.setBackgroundColor(color);
		tvCode.setBackgroundColor(color);
		tvName.setBackgroundColor(color);
		tvDate.setBackgroundColor(color); 
	}

	/**
	 * 
	 * render du lieu cho mot row
	 * 
	 * @author: ThanhNN8
	 * @param position
	 * @param item
	 * @return: void
	 * @throws:
	 */
	public void renderLayout(int position, ProInfoDTO item) {
		this.currentDTO = item;
		tvSTT.setText(Constants.STR_BLANK + position);
		tvCode.setText(item.PRO_INFO_CODE);
		tvName.setText(item.PRO_INFO_NAME);
		tvDate.setText(item.FROM_DATE + " - " + item.TO_DATE);
		
		if(item.isNeedTypeQuantity){
			setBackGroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_CLOSED));
		}
	}

	/**
	 * 
	 * Layout row thong bao khong co du lieu
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return: void
	 * @throws:
	 */
	public void renderLayoutNoResult() {
		tvSTT.setVisibility(View.GONE);
		tvCode.setVisibility(View.GONE);
		tvName.setVisibility(View.GONE);
		tvDate.setVisibility(View.GONE);
//		tvNoResultInfo.setVisibility(View.VISIBLE);
//		tvNoResultInfo.setText(StringUtil.getString(R.string.TEXT_CTKM_INFO_NORESULT));
	}
	
//	public View getView() {
//		return view;
//	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == tvCode && !currentDTO.isNeedTypeQuantity) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.GO_TO_PROMOTION_PROGRAM_DETAIL, null, this.currentDTO);
		} else if (v == tvCode && currentDTO.isNeedTypeQuantity) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.GO_TO_PROMOTION_PROGRAM_DONE, null, this.currentDTO);
		} else if (context != null){
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity)context);
		}
	}
}
