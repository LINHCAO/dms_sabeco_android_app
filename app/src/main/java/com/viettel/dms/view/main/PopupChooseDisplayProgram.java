/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.db.DisplayProgrameDTO;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnImageTakingPopupListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;


/**
 * popup chon chuong trinh cham trung bay, chup anh
 * @author BANGHN
 * @version 1.0
 */
public class PopupChooseDisplayProgram extends LinearLayout implements VinamilkTableListener, OnClickListener {
	private GlobalBaseActivity parent;
	// listtener
	protected OnImageTakingPopupListener listener;
	//table hien thi chuong trinh trung bay
	VinamilkTableView tbTableList;
	//textview caption: cau thong bao tren popup
	TextView tvCaption;
	//title
	TextView tvTitle;
	//linear menu chup hinh
	LinearLayout llMenuPopup;
	LinearLayout llTableDP;
	
	//textview menu chup hinh diem ban
	TextView tvImageLocation;
	//textview menu anh trung bay
	TextView tvImageCTTB;
	//textview menu anh doi thu canh tranh
	TextView tvImageDTCT;
	
	//Data: Danh sach chuong trinh trung bay
	ArrayList<DisplayProgrameDTO> listDP;
	public View view;
	public PopupChooseDisplayProgram(Context context, OnImageTakingPopupListener listener) {
		super(context);
		parent = (GlobalBaseActivity) context;
		this.listener = listener;
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = (LinearLayout) inflater.inflate(
				R.layout.layout_popup_display_program, this);
		
		tvTitle = (TextView)view.findViewById(R.id.tvTitle);
		tvCaption = (TextView)view.findViewById(R.id.tvCaption);
		tvImageLocation = (TextView)view.findViewById(R.id.tvImageLocation);
		tvImageLocation.setOnClickListener(this);
		tvImageCTTB = (TextView)view.findViewById(R.id.tvImageCTTB);
		tvImageCTTB.setOnClickListener(this);
		tvImageDTCT = (TextView)view.findViewById(R.id.tvImageDTCT);
		tvImageDTCT.setOnClickListener(this);
		tbTableList = (VinamilkTableView)view.findViewById(R.id.tbTableList);
		llTableDP = (LinearLayout)view.findViewById(R.id.llTableDP);
		llMenuPopup = (LinearLayout)view.findViewById(R.id.llMenuPopup);
		layoutTableHeader(view);
	}
	
	
	/**
	 * Title popup thong bao
	 * @author: BANGHN
	 * @param str
	 */
	public void setTitlePopup(CharSequence str){
		tvTitle.setText(str);
	}
	
	/**
	 * set noi dung huong dan thong bao tren popup
	 * @author: BANGHN
	 * @param str
	 */
	public void setCaptionPopup(CharSequence str){
		tvCaption.setText(str);
		tvCaption.setVisibility(View.VISIBLE);
	}

	/**
	 * Noi dung chuong trinh trung bay (list cttb)
	 * @author: BANGHN
	 * @param listDP
	 */
	public void setDataDisplayPrograme(ArrayList<DisplayProgrameDTO> listDP){
		this.listDP = listDP;
		renderLayout(listDP);
	}
	
	/**
	 * Khoi tao table view
	 * @author: BANGHN
	 * @param v
	 */
	private void layoutTableHeader(View v) {
		String[] LIST_CUSTOMER_TABLE_WIDTHS = {"Mã CTTB", "Tên CTTB" };
		int[] LIST_CUSTOMER_TABLE_TITLES = { 140, 294 };
		tbTableList.getHeaderView().addColumns(LIST_CUSTOMER_TABLE_TITLES, LIST_CUSTOMER_TABLE_WIDTHS,
				ImageUtil.getColor(R.color.BLACK), ImageUtil.getColor(R.color.TABLE_HEADER_BG));
	}
	
	/**
	 * render du lieu danh sach chuong trinh trung bay
	 * @author: BANGHN
	 * @param listDP
	 */
	public void renderLayout(ArrayList<DisplayProgrameDTO> listDP){
		llTableDP.setVisibility(View.VISIBLE);
		if(listDP != null && listDP.size() > 0){
			List<TableRow> listRows = new ArrayList<TableRow>();
			for (int i = 0; i < listDP.size(); i++){
				PopupChooseDisplayProgramRow row = new PopupChooseDisplayProgramRow(parent);
				row.setListener(this);
				row.renderLayout(listDP.get(i));
				listRows.add(row);
			}
			tbTableList.addContent(listRows);
		}else{
			llTableDP.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvImageLocation:
			if (listener != null) {
				listener.onImageTakingPopupEvent(
						OnImageTakingPopupListener.ACTION_TAKING_IMAGE_LOCATION,
						null);
			}
			break;
		case R.id.tvImageCTTB:
			if (listener != null) {
				listener.onImageTakingPopupEvent(
						OnImageTakingPopupListener.ACTION_TAKING_IMAGE_CTTB,
						null);
			}
			break;
		case R.id.tvImageDTCT:
			if (listener != null) {
				listener.onImageTakingPopupEvent(
						OnImageTakingPopupListener.ACTION_TAKING_IMAGE_RIVAl,
						null);
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
		//gui lai su kien chon CTTB tren view xu ly
		if(listener != null){
			listener.onImageTakingPopupEvent(OnImageTakingPopupListener.ACTION_TAKING_IMAGE_RIVAl, data);
		}
	}
}
