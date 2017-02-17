/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.control;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.sabeco.R;
/**
 * 
 *  create header control for vinamilk table
 *  @author: HaiTC3
 *  @version: 1.0
 *  @since: 1.0
 */
public class VinamilkHeaderTable extends LinearLayout implements OnClickListener, OnCheckedChangeListener {
	
	private TableRow header;

	private OnEventControlListener myListener;

	public VinamilkHeaderTable(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public VinamilkHeaderTable(Context context) {
		super(context);
		initView(context);
	}
	public VinamilkHeaderTable(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View table = inflater.inflate(R.layout.layout_vinamilk_table_header,
				this);
		header = (TableRow)table.findViewById(R.id.table);
	}
	
	public void removeAllColumns(){
		header.removeAllViews();
	}
	
	/**
	 * 
	*  add columns for table header use text color and background color default
	*  @author: PhucNT
	*  @param widths
	*  @param titles
	*  @return: void
	*  @throws:
	 */
	public void addColumns(int[] widths, String[] titles) {
		for (int i = 0, size = widths.length; i < size; i++) {
			TextView tv = createColumnHeader(widths[i],titles[i], 
					ImageUtil.getColor(R.color.BLACK), ImageUtil.getColor(R.color.WHITE));
			tv.setOnClickListener(this);
			header.addView(tv);
		}
	}
	
	/**
	 * 
	*  add custom header row
	*  @author: TamPQ
	*  @throws:
	 */
	public void addHeader(View v){
		header.addView(v);
	}
	
	/**
	 * 
	*  add columns for table header with text color and background color
	*  @author: PhucNT
	*  @param widths
	*  @param titles
	*  @param textColor
	*  @param bgColor
	*  @return: void
	*  @throws:
	 */
	public void addColumns(int[] widths, String[] titles, int textColor, int bgColor) {
		
		for (int i = 0; i < widths.length; i++) {
			TextView tv = createColumnHeader(widths[i],titles[i], textColor, bgColor);
			tv.setOnClickListener(this);
			header.addView(tv);
		}
	}
	
	/**
	 * 
	*  add control not textview to header row
	*  @author: HaiTC3
	*  @param v
	*  @return: void
	*  @throws:
	 */
	public void addColumnIsCheckBoxControl(int widths, int bgColor){
		header.addView(this.createColumnHeaderCheckBox(widths, bgColor));
	}
	
	public void resetCheckBoxCheckAll(){
		if(header.getChildAt(header.getChildCount()-1) != null){
			LinearLayout llChecked = (LinearLayout) header.getChildAt(header.getChildCount()-1);
			CheckBox cbSelected = (CheckBox) llChecked.getChildAt(0);
			cbSelected.setChecked(false);
		}
	}
	
	
	/**
	 * 
	*  add columns use style defined
	*  @author: PhucNT
	*  @param widths
	*  @param style
	*  @return: void
	*  @throws:
	 */
	public void addColumns(int[] widths, int style){
		for (int i = 0; i < widths.length; i++) {
			TextView tv = createColumnHeader(widths[i],style);
			header.addView(tv);
		}
	}
	
	/**
	*  Lay so luong header
	*  @author: SoaN
	*  @return: int
	*  @throws:
	 */
	public int getHeaderCount(){
		return header.getChildCount();
	}
	
	/**
	 * 
	*  create column header with text color and background color
	*  @author: PhucNT
	*  @param width
	*  @param title
	*  @param textColor
	*  @param bgColor
	*  @return
	*  @return: TextView
	*  @throws:
	 */
	private TextView createColumnHeader(int width,String title, int textColor, int bgColor){
		TextView myTextView = new TextView(getContext());
		//myTextView.setTextAppearance(getContext(), R.style.HeaderVinamilkTable);
		TableRow.LayoutParams param=new TableRow.LayoutParams();
		param.width=GlobalUtil.dip2Pixel(width);
		param.height=LayoutParams.MATCH_PARENT;
		param.bottomMargin = GlobalUtil.dip2Pixel(1);
		param.topMargin = GlobalUtil.dip2Pixel(1);
		param.leftMargin = GlobalUtil.dip2Pixel(1);
		param.rightMargin = GlobalUtil.dip2Pixel(1);
		myTextView.setLayoutParams(param);
		myTextView.setMinHeight(GlobalUtil.dip2Pixel(35));
		myTextView.setText(title);
		myTextView.setPadding(0, GlobalUtil.dip2Pixel(5), 0, GlobalUtil.dip2Pixel(5));
		myTextView.setTextColor(textColor);
		myTextView.setBackgroundColor(bgColor);
		myTextView.setTypeface(null, Typeface.BOLD);
		myTextView.setGravity(Gravity.CENTER);
		
		return myTextView;
	}
	
	/**
	 * 
	*  create column checkbox in header
	*  @author: HaiTC3
	*  @param width
	*  @param bgColor
	*  @return
	*  @return: View
	*  @throws:
	 */
	private View createColumnHeaderCheckBox(int width, int bgColor){
		LinearLayout llCheckBox = new LinearLayout(getContext());
		TableRow.LayoutParams llparam=new TableRow.LayoutParams();
		llparam.width = GlobalUtil.dip2Pixel(width);
		llparam.height = LayoutParams.MATCH_PARENT;
		llparam.bottomMargin = GlobalUtil.dip2Pixel(1);
		llparam.topMargin = GlobalUtil.dip2Pixel(1);
		llparam.leftMargin = GlobalUtil.dip2Pixel(1);
		llparam.rightMargin = GlobalUtil.dip2Pixel(1);
		llCheckBox.setLayoutParams(llparam);
		llCheckBox.setBackgroundColor(bgColor);
		
		CheckBox cbSelectedAll = new CheckBox(getContext());
		TableRow.LayoutParams param=new TableRow.LayoutParams();
		param.width=GlobalUtil.dip2Pixel(width);
		param.height=LayoutParams.MATCH_PARENT;
		param.leftMargin=GlobalUtil.dip2Pixel(15);
		cbSelectedAll.setButtonDrawable(R.drawable.checkbox_selector);
		cbSelectedAll.setLayoutParams(param);
		cbSelectedAll.setPadding(0, GlobalUtil.dip2Pixel(0), 0, GlobalUtil.dip2Pixel(0));
		cbSelectedAll.setBackgroundColor(bgColor);
		cbSelectedAll.setGravity(Gravity.CENTER_VERTICAL);
		cbSelectedAll.setOnCheckedChangeListener(this);
		
		llCheckBox.addView(cbSelectedAll);
		return llCheckBox;
	}
	
	/**
	 * 
	*  create column header with style defined
	*  @author: PhucNT
	*  @param width
	*  @param style
	*  @return
	*  @return: TextView
	*  @throws:
	 */
	private TextView createColumnHeader(int width, int style){
		TextView myTextView = new TextView(getContext(), null, style);
		myTextView.setWidth(GlobalUtil.dip2Pixel(width));
		return myTextView;
	}

	/**
	 * @return the myListener
	 */
	public OnEventControlListener getMyListener() {
		return myListener;
	}

	/**
	 * @param myListener the myListener to set
	 */
	public void setMyListener(OnEventControlListener myListener) {
		this.myListener = myListener;
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int columnIndex = 0;
		for (int i = 0, size = header.getChildCount(); i < size; i++) {
			if (arg0 == header.getChildAt(i)) {
				columnIndex = i;
			}
		}
		if (myListener != null){
			myListener.onEvent(ActionEventConstant.HEADER_CLICK, VinamilkHeaderTable.this, columnIndex);
		}
	}

	/* (non-Javadoc)
	 * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton, boolean)
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (myListener != null){
			int check = 0;
			if(isChecked){
				check = 1;
			}
			else{
				check = 0;
			}
			myListener.onEvent(ActionEventConstant.HEADER_CLICK, VinamilkHeaderTable.this, String.valueOf(check));
		}
	}

	/**
	 * Add complex header to header table
	 * @author: duongdt3
	 * @since: 11:12:28 21 Nov 2013
	 * @return: void
	 * @throws:  
	 * @param tableHeader
	 */
	public void addHeader(HeaderTableInfo[] tableHeader) {
		//remove all
		removeAllColumns();
		
		//add header info into header
		for (int i = 0; i < tableHeader.length; i++) {
			View view = tableHeader[i].render(getContext(), ImageUtil.getColor(R.color.BLACK), ImageUtil.getColor(R.color.TABLE_HEADER_BG));
			addHeader(view);
		}
		 
	}
}
