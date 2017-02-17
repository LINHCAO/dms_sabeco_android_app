/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.control;

import java.util.Vector;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.viettel.dms.dto.db.MediaItemDTO;

/**
 * 
 * Mo ta cho class
 * 
 * @author: ThanhNN8
 * @version: 1.0
 * @since: 1.0
 */
public class MediaListAdapter extends ArrayAdapter<MediaItemDTO> {
	int resourceLayoutId;
	Context m_conContext;
	boolean isShowAvatar;
	public MediaListAdapter(Context context, int resourceLayoutId,
			Vector<MediaItemDTO> mediaList, boolean isShowAvatar) {
		super(context, resourceLayoutId, mediaList);
		m_conContext = context;
		this.resourceLayoutId = resourceLayoutId;
		this.isShowAvatar = isShowAvatar;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		return row;
	}
}
