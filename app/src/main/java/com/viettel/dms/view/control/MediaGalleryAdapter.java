/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.control;

import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.viettel.dms.dto.db.MediaItemDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.sabeco.R;

/**
 * 
 * Mo ta cho class
 * 
 * @author: ThanhNN8
 * @version: 1.0
 * @since: 1.0
 */
public class MediaGalleryAdapter extends ArrayAdapter<MediaItemDTO> {
	int resourceLayoutId;
	Context m_conContext;
	boolean isShowAvatar;
	List<MediaItemDTO> listDTO = null;
	public MediaGalleryAdapter(Context context, int resourceLayoutId,
			Vector<MediaItemDTO> mediaList, boolean isShowAvatar) {
		super(context, resourceLayoutId, mediaList);
		m_conContext = context;
		this.listDTO = mediaList;
		this.resourceLayoutId = resourceLayoutId;
		this.isShowAvatar = isShowAvatar;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		row = vi.inflate(R.layout.layout_media_item, null);
		ImageView imgView = (ImageView) row
		.findViewById(R.id.imgViewMedia);
		ImageView imgViewBoder = (ImageView) row
				.findViewById(R.id.imgViewBoder);
		MediaItemDTO mediaItemDTO = listDTO.get(position);
		if (mediaItemDTO.isSelected == true) {
			imgViewBoder.setVisibility(View.VISIBLE);
		} else {
			imgViewBoder.setVisibility(View.GONE);
		}
		if (mediaItemDTO.mediaType == 1) {
			imgView.setImageResource(R.drawable.videoicon);
		} else {
			if (mediaItemDTO.thumbUrl != null
					&& !"".equals(mediaItemDTO.thumbUrl)) {
				//imgView.setTag(ServerPath.IMAGE_PRODUCT_VNM + mediaItemDTO.thumbUrl);
				imgView.setTag(GlobalInfo.getInstance().getServerImageProductVNM() + mediaItemDTO.thumbUrl);
			}
		}
		return row;
	}
}
