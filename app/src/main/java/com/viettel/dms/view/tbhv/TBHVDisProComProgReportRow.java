/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.TBHVDisProComProgReportDTO;
import com.viettel.dms.dto.view.TBHVDisProComProgReportItem;
import com.viettel.dms.dto.view.TBHVDisProComProgReportItemResult;
import com.viettel.dms.dto.view.TBHVDisProComProgReportNPPDTO;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * Row Man hinh bao cao chuong trinh chung bay TBHV row: 01-04. Báo cáo tiến độ
 * chung CTTB ngay (TBHV)
 * 
 * @author: HoanPD1
 * @version: 1.0
 * @since: 1.0
 */
public class TBHVDisProComProgReportRow extends TableRow implements OnClickListener {

	public static final int ACTION_CLICK_NPP_CODE = 0;
	public static final int ACTION_CLICK_GSNPP_NAME = 1;

	public static final int OBJECT_TYPE_USE_TBHV = 0;
	public static final int OBJECT_TYPE_USE_NPP = 1;
	// view
	private View view;
	// ten NPP
	public TextView tvNPPCode;
	// ten GSNPP
	public TextView tvTenGSNPP;
	// tong
	public TextView tvTotal;
	// layout level
	public LinearLayout llLevel;

	protected VinamilkTableListener listener;

	public VinamilkTableListener getListener() {
		return listener;
	}

	public void setListener(VinamilkTableListener listener) {
		this.listener = listener;
	}

	TBHVDisProComProgReportItem data;

	public TBHVDisProComProgReportRow(Context context, boolean sum, int typeObject) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (sum) {
			if (typeObject == OBJECT_TYPE_USE_TBHV) {
				view = vi.inflate(R.layout.layout_tbhv_dis_pro_com_prog_report_row_sum, this);
			} else if (typeObject == OBJECT_TYPE_USE_NPP) {
				view = vi.inflate(R.layout.layout_tbhv_dis_pro_com_prog_report_row_npp_sum, this);
			}
		} else {
			if (typeObject == OBJECT_TYPE_USE_TBHV) {
				view = vi.inflate(R.layout.layout_tbhv_dis_pro_com_prog_report_row, this);
			} else if (typeObject == OBJECT_TYPE_USE_NPP) {
				view = vi.inflate(R.layout.layout_tbhv_dis_pro_com_prog_report_npp_row, this);
			}
		}
		if (!sum) {
			TableRow row = (TableRow) view.findViewById(R.id.row);
			row.setOnClickListener(this);
			tvNPPCode = (TextView) view.findViewById(R.id.tvNPPCode);
			tvNPPCode.setOnClickListener(this);
			if (typeObject == OBJECT_TYPE_USE_TBHV) {
				tvTenGSNPP = (TextView) view.findViewById(R.id.tvTenGSNPP);
				tvTenGSNPP.setOnClickListener(this);
				tvTenGSNPP.setTextColor(ImageUtil.getColor(R.color.COLOR_LINK));
			}
		}
		tvTotal = (TextView) view.findViewById(R.id.tvTotal);
		llLevel = (LinearLayout) view.findViewById(R.id.layoutLevel);
	}

	/**
	 * render row
	 * 
	 * @param item
	 * @param arrLevel
	 */
	public void render(TBHVDisProComProgReportItem item, ArrayList<String> arrLevel, int standarPercentAlow, int widthTVLevel) {
		this.data = item;
		tvNPPCode.setText(item.codeNPP);
		tvTenGSNPP.setText(item.nameGSNPP);
		boolean isRed = false; // bien de kiem tra co mot cell nao bi highlight mau do ko
		for (int i = 0; i < arrLevel.size(); i++) {
			TBHVDisProComProgReportItemResult rs = item.arrLevelCode.get(i);
			int currentPercentNotPSDS = 0;
			if (rs.resultNumber != 0 && rs.joinNumber > 0) {
				currentPercentNotPSDS = (int) (((float) rs.resultNumber / (float) rs.joinNumber) * 100);
			}

			if (currentPercentNotPSDS > standarPercentAlow) {
				llLevel.addView(createColumns(widthTVLevel, rs.resultNumber + "/" + rs.joinNumber,
						ImageUtil.getColor(R.color.RED), Typeface.NORMAL));
				isRed = true;
			} else {
				llLevel.addView(createColumns(widthTVLevel, rs.resultNumber + "/" + rs.joinNumber,
						ImageUtil.getColor(R.color.BLACK), Typeface.NORMAL));
			}
		}

		TableRow.LayoutParams param = new TableRow.LayoutParams();
		param.width = GlobalUtil.dip2Pixel(widthTVLevel + 10);
		param.height = LayoutParams.FILL_PARENT;
		param.setMargins(GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1),
				GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1));
		tvTotal.setLayoutParams(param);
		
		tvTotal.setText(item.itemResultTotal.resultNumber + "/" + item.itemResultTotal.joinNumber);
		int currentPercentNotPSDSTotal = 0;
		if (item.itemResultTotal.resultNumber != 0 && item.itemResultTotal.joinNumber > 0) {
			currentPercentNotPSDSTotal = (int) (((float) item.itemResultTotal.resultNumber / (float) item.itemResultTotal.joinNumber) * 100);
		}
		if (currentPercentNotPSDSTotal > standarPercentAlow || isRed == true) {
			tvTotal.setTextColor(ImageUtil.getColor(R.color.RED));
		}
		
	}

	/**
	 * 
	 * render layout row for cttb npp
	 * 
	 * @param item
	 * @param arrLevel
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 31, 2012
	 */
	public void renderRowForCTTBNPP(TBHVDisProComProgReportItem item, ArrayList<String> arrLevel, int percentProgress, int widthTVLevel) {
		this.data = item;
		tvNPPCode.setText(item.codeNPP + " - " + item.nameNPP);
		boolean isRed = false;
		for (int i = 0; i < arrLevel.size(); i++) {
			TBHVDisProComProgReportItemResult rs = item.arrLevelCode.get(i);

			int currentPercentNotPSDS = 0;
			if (rs.resultNumber != 0 && rs.joinNumber > 0) {
				currentPercentNotPSDS = (int) (((float) rs.resultNumber / (float) rs.joinNumber) * 100);
			}
			if (currentPercentNotPSDS > percentProgress) {
				llLevel.addView(createColumns(widthTVLevel, rs.resultNumber + "/" + rs.joinNumber,
						ImageUtil.getColor(R.color.RED), Typeface.NORMAL));
				isRed = true;
			} else {
				llLevel.addView(createColumns(widthTVLevel, rs.resultNumber + "/" + rs.joinNumber,
						ImageUtil.getColor(R.color.BLACK), Typeface.NORMAL));
			}
		}
		
		TableRow.LayoutParams param = new TableRow.LayoutParams();
		param.width = GlobalUtil.dip2Pixel(widthTVLevel + 10);
		param.height = LayoutParams.FILL_PARENT;
		param.setMargins(GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1),
				GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1));
		tvTotal.setLayoutParams(param);
		tvTotal.setText(item.itemResultTotal.resultNumber + "/" + item.itemResultTotal.joinNumber);
		int currentPercentNotPSDSTotal = 0;
		if (item.itemResultTotal.resultNumber != 0 && item.itemResultTotal.joinNumber > 0) {
			currentPercentNotPSDSTotal = (int) (((float) item.itemResultTotal.resultNumber / (float) item.itemResultTotal.joinNumber) * 100);
		}
		if (currentPercentNotPSDSTotal > percentProgress || isRed) {
			tvTotal.setTextColor(ImageUtil.getColor(R.color.RED));
		}
	}

	/**
	 * render sum row
	 * 
	 * @param dto
	 */
	public void renderSum(TBHVDisProComProgReportDTO dto, int percentProgress, int widthTVLevel) {
		boolean isRed = false;
		for (int i = 0; i < dto.arrLevelCode.size(); i++) {
			TBHVDisProComProgReportItemResult rs = dto.arrResultTotal.get(i);
			boolean isHaveError = false;
			int current = 0;
			if (rs.resultNumber != 0 && rs.joinNumber > 0) {
				current = (int) (((float) rs.resultNumber / (float) rs.joinNumber) * 100);
			}
			if (current > percentProgress) {
				isHaveError = true;
			}
			if (isHaveError) {
				llLevel.addView(createColumns(widthTVLevel, rs.resultNumber + "/" + rs.joinNumber,
						ImageUtil.getColor(R.color.RED), Typeface.BOLD));
				isRed = true;
			} else {
				llLevel.addView(createColumns(widthTVLevel, rs.resultNumber + "/" + rs.joinNumber,
						ImageUtil.getColor(R.color.BLACK), Typeface.BOLD));
			}
		}
		
		TableRow.LayoutParams param = new TableRow.LayoutParams();
		param.width = GlobalUtil.dip2Pixel(widthTVLevel + 10);
		param.height = LayoutParams.FILL_PARENT;
		param.setMargins(GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1),
				GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1));
		tvTotal.setLayoutParams(param);
		
		tvTotal.setText(dto.dtoResultTotal.resultNumber + "/" + dto.dtoResultTotal.joinNumber);
		int currentPercentNotPSDSTotal = 0;
		if (dto.dtoResultTotal.resultNumber != 0 && dto.dtoResultTotal.joinNumber > 0) {
			currentPercentNotPSDSTotal = (int) (((float) dto.dtoResultTotal.resultNumber / (float) dto.dtoResultTotal.joinNumber) * 100);
		}
		if (currentPercentNotPSDSTotal > percentProgress || isRed) {
			tvTotal.setTextColor(ImageUtil.getColor(R.color.RED));
		}
	}

	/**
	 * 
	 * render sum row for cttb npp
	 * 
	 * @param dto
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 31, 2012
	 */
	public void renderSum(TBHVDisProComProgReportNPPDTO dto, int percentProgress, int widthTVLevel) {
		boolean isRed = false;
		for (int i = 0; i < dto.arrLevelCode.size(); i++) {
			TBHVDisProComProgReportItemResult rs = dto.arrResultTotal.get(i);
			
			boolean isHaveError = false;
			int current = 0;
			if (rs.resultNumber != 0 && rs.joinNumber > 0) {
				current = (int) (((float) rs.resultNumber / (float) rs.joinNumber) * 100);
			}
			if (current > percentProgress) {
				isHaveError = true;
			}
			
			if (isHaveError) {
				llLevel.addView(createColumns(widthTVLevel, rs.resultNumber + "/" + rs.joinNumber,
						ImageUtil.getColor(R.color.RED), Typeface.BOLD));
				isRed = true;
			} else {
				llLevel.addView(createColumns(widthTVLevel, rs.resultNumber + "/" + rs.joinNumber,
						ImageUtil.getColor(R.color.BLACK), Typeface.BOLD));
			}
		}
		TableRow.LayoutParams param = new TableRow.LayoutParams();
		param.width = GlobalUtil.dip2Pixel(widthTVLevel + 10);
		param.height = LayoutParams.FILL_PARENT;
		param.setMargins(GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1),
				GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1));
		tvTotal.setLayoutParams(param);
		tvTotal.setText(dto.dtoResultTotal.resultNumber + "/" + dto.dtoResultTotal.joinNumber);
		int currentPercentNotPSDSTotal = 0;
		if (dto.dtoResultTotal.resultNumber != 0 && dto.dtoResultTotal.joinNumber > 0) {
			currentPercentNotPSDSTotal = (int) (((float) dto.dtoResultTotal.resultNumber / (float) dto.dtoResultTotal.joinNumber) * 100);
		}
		if (currentPercentNotPSDSTotal > percentProgress || isRed) {
			tvTotal.setTextColor(ImageUtil.getColor(R.color.RED));
		}
	}

	/**
	 * get item level
	 * 
	 * @param width
	 * @param title
	 * @param resourceColor
	 * @param type
	 * @return
	 * @return: TextView
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 4, 2013
	 */

	private TextView createColumns(int width, String title, int resourceColor, int type) {
		TextView myTextView = new TextView(getContext());
		TableRow.LayoutParams param = new TableRow.LayoutParams();
		param.width = GlobalUtil.dip2Pixel(width);
		param.height = LayoutParams.FILL_PARENT;
		param.setMargins(GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1),
				GlobalUtil.dip2Pixel(1));
		myTextView.setLayoutParams(param);
		myTextView.setDuplicateParentStateEnabled(true);
		myTextView.setBackgroundResource(R.drawable.style_row_default);
		myTextView.setText(title);
		myTextView.setTextColor(resourceColor);
		myTextView.setTypeface(null, type);
		myTextView.setGravity(Gravity.CENTER);

		return myTextView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View paramView) {
		// TODO Auto-generated method stub
		if (listener != null) {
			if (paramView == tvNPPCode) {
				listener.handleVinamilkTableRowEvent(ACTION_CLICK_NPP_CODE, paramView, data);
			} else if (paramView == tvTenGSNPP) {
				listener.handleVinamilkTableRowEvent(ACTION_CLICK_GSNPP_NAME, paramView, data);
			}
		}
	}

}
