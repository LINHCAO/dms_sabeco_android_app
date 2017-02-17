/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.control;

import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.viettel.dms.dto.control.SpinnerItemDTO;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.sabeco.R;

/**
 * Spinner text co header
 * @author: TruongHN
 * @version: 1.1
 * @since: 1.0
 */
public class VNMSpinnerTextAdapter extends ArrayAdapter<SpinnerItemDTO> {
	Context context;
	Vector<SpinnerItemDTO>items = new Vector<SpinnerItemDTO>();
	public boolean showHint = true;
	private String hint = "";
	TextView tvHint = null;

	public VNMSpinnerTextAdapter(final Context context, final int textViewResourceId,
			final Vector<SpinnerItemDTO> objects) {
		super(context, textViewResourceId ,android.R.id.text1, objects);
		this.items = objects;
		this.context = context;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = null;
		tvHint = new TextView(parent.getContext());
		if (showHint && hint != null) {
			SpannableObject objSku = new SpannableObject();
			objSku.addSpan(hint, ImageUtil.getColor(R.color.BLACK),
					android.graphics.Typeface.BOLD);
			tvHint.setText(objSku.getSpan());
			tvHint.setVisibility(View.VISIBLE);
		}else {
			tvHint.setVisibility(View.GONE);
		}
		v = tvHint;
		return v;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = null;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.vnm_spinner_text_row, parent, false);
		}
		if (position < items.size()){
			TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
			if (tvName != null){
				tvName.setText(items.get(position).name);
			}
			TextView tvContent = (TextView) convertView.findViewById(R.id.tvContent);
			if (tvContent != null && !StringUtil.isNullOrEmpty(items.get(position).content)){
				tvContent.setText(items.get(position).content);
				tvContent.setVisibility(View.VISIBLE);
			}else{
				tvContent.setVisibility(View.GONE);
			}
		}
		v = convertView;
		return v;
	}
	
	
	public void setHint(String strHint) {
		this.hint = strHint;
	}
}
