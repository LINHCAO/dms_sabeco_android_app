/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tnpg.customer;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.db.OpProductDTO;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.HeaderTableInfo;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Competitor Sale Row CompetitorSaleRow.java
 * 
 * @version: 1.0
 * @since: 08:31:50 20 Jan 2014
 */
public class CompetitorSaleRow extends TableRow implements TextWatcher,
		OnClickListener {

	private final Context con;
	private final View v;
	private int type;
	private final GlobalBaseActivity parent;
	private View[] arrView;

	private TextView tvSTTCompet;
	private TextView tvMMHCompet;
	private TextView tvMHNameCompet;
	private TextView tvConvfactNameCompet;
	private EditText etNumberSaleCompet;
	private TableRow rowCompetitor;
	private LinearLayout llEditTextCompet;
	private OpProductDTO opProductDTO;

	private CompetitorSaleRow(Context context) {
		super(context);
		con = context;
		parent = (GlobalBaseActivity) con;
		LayoutInflater vi = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = vi.inflate(R.layout.activity_competitor_sale_row, this, false);
	}

	public CompetitorSaleRow(Context context, int type) {
		this(context);
		this.type = type;
		initView(v);
	}

	/**
	 * render layout
	 * 
	 * @author: dungdq3
	 * @param:
	 * @return: void
	 */
	public void renderLayout(int pos, OpProductDTO opProductDTO) {

		this.opProductDTO = opProductDTO;
		tvSTTCompet.setText("" + pos);
		tvMMHCompet.setText(opProductDTO.getProductCode());
		tvMHNameCompet.setText(opProductDTO.getProductName());
		if (!StringUtil.isNullOrEmpty(opProductDTO.getUOM2())) {
			tvConvfactNameCompet.setText(opProductDTO.getUOM2().trim());
		} else {
			tvConvfactNameCompet.setText("");
		}
		// etNumberSaleCompet.requestFocus();
	}

	/**
	 * Khởi tạo màn hình
	 * 
	 * @author: dungdq3
	 * @param: View v
	 * @return: void
	 */
	private void initView(View v) {
		setDrawingCacheEnabled(true);
		buildDrawingCache();
		rowCompetitor = (TableRow) v.findViewById(R.id.rowCompetitor);
		llEditTextCompet = (LinearLayout) v.findViewById(R.id.llEditTextCompet);
		tvSTTCompet = (TextView) v.findViewById(R.id.tvSTTCompet);
		tvMMHCompet = (TextView) v.findViewById(R.id.tvMMHCompet);
		tvMHNameCompet = (TextView) v.findViewById(R.id.tvMHNameCompet);
		tvConvfactNameCompet = (TextView) v.findViewById(R.id.tvConvfactNameCompet);
		etNumberSaleCompet = (EditText) v.findViewById(R.id.etNumberSaleCompet);
		etNumberSaleCompet.addTextChangedListener(this);
		setOnClickListener(this);

		rowCompetitor.removeAllViews();
		arrView = new View[] { tvSTTCompet, tvMMHCompet, tvMHNameCompet,
				tvConvfactNameCompet, llEditTextCompet };

		for (View vi : arrView) {
			addView(vi);
		}
	}

	@Override
	public void afterTextChanged(Editable arg0) {
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

		if (!StringUtil.isNullOrEmpty(etNumberSaleCompet.getText().toString())) {
			long quantity = Long.parseLong(etNumberSaleCompet.getText().toString());
			if (type == 0) {
				opProductDTO.setQuantity(quantity);
			} else {
				if (quantity <= 0) {
					parent.showDialog(StringUtil.getString(R.string.TEXT_BIGGER_THAN_ZERO));
					etNumberSaleCompet.setText("");
				} else {
					opProductDTO.setQuantity(quantity);
				}
			}
		}else if(StringUtil.isNullOrEmpty(etNumberSaleCompet.getText().toString()) && opProductDTO.getQuantity()>0){
			opProductDTO.setQuantity(0);
		}
	}

	@Override
	public void onClick(View v) {

		if (v == this && con != null) {
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity) con);
		}
	}
	
	/**
	 * Lay header cho table
	 * @author: dungdq3
	 * @since: 4:08:07 PM Feb 7, 2014
	 * @return: void
	 * @throws:  
	 */
	public View getHeaderForTable(){
		LinearLayout header = new LinearLayout(con);
		LinearLayout.LayoutParams lp = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		header.setLayoutParams(lp);
		
		for (View v : arrView) {
			HeaderTableInfo h = new HeaderTableInfo(v.getTag().toString(), v.getLayoutParams().width);
			header.addView(h.render(con,  ImageUtil.getColor(R.color.BLACK), ImageUtil.getColor(R.color.TABLE_HEADER_BG)));
		}
		return header;
	}
}
