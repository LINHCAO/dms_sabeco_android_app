/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.TBHVBusinessSupportPrograme;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * 
 *  Row cua MH CTKM dung cho lop TBHVPromotionProgramView
 *  @author: Nguyen Thanh Dung
 *  @version: 1.1
 *  @since: 1.0
 */
@SuppressLint("ViewConstructor")
public class TBHVBusinessSupportProgrameRow extends TableRow implements OnClickListener{
	public static final int ACTION_CLICK_CODE_PROGRAM = 1;
	Context _context;
	View view;
	TextView tvSTT;//so thu tu
	TextView tvCode;//ma chuong trinh+
	TextView tvName;//ten chuong trinh
	TBHVBusinessSupportPrograme currentDTO;//du lieu cua row
	protected VinamilkTableListener listener;// listener xu ly su kien tren row
	private TableRow row;//dung de xu ly hide ban phim

	public TBHVBusinessSupportProgrameRow(Context context, View aRow) {
		super(context);
		_context = context;
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_tbhv_business_support_programe_row, this);
		row = (TableRow)view.findViewById(R.id.row);
		row.setOnClickListener(this);
		tvSTT = (TextView) view.findViewById(R.id.tvPromotionSTT);
		tvCode = (TextView) view.findViewById(R.id.tvPromotionCode);
		tvCode.setOnClickListener(this);
		tvName = (TextView) view.findViewById(R.id.tvPromotionName);
	}
	
	public void setListener(VinamilkTableListener listener) {
		this.listener = listener;
	}
	
	/**
	 * 
	*  render du lieu cho row
	*  @author: Nguyen Thanh Dung
	*  @param position
	*  @param item
	*  @return: void
	*  @throws:
	 */
	public void renderLayout(int position, TBHVBusinessSupportPrograme item) {
		this.currentDTO = item;
		tvSTT.setText("" + position);
		tvCode.setText(item.getName());
//		tvName.setText(String.valueOf(item.getCount()));
		SpannableObject spanObject = new SpannableObject();
		spanObject.addSpan(StringUtil.getString(R.string.TEXT_HAVE));
		spanObject.addSpan(" ");
		spanObject.addSpan(item.getCount() + "", ImageUtil.getColor(R.color.RED), android.graphics.Typeface.BOLD);
		spanObject.addSpan(" ");
		spanObject.addSpan(StringUtil.getString(R.string.TEXT_PROGRAM_RUNNING));
		tvName.setText(spanObject.getSpan());
	}

	public View getView() {
		return view;
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == tvCode) {
			listener.handleVinamilkTableRowEvent(currentDTO.getAction(), null, this.currentDTO);
		} else if (v == row && _context != null){
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity)_context);
		}
	}
}
