/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.viettel.dms.download.ExternalStorage;
import com.viettel.dms.dto.photo.PhotoDTO;
import com.viettel.dms.global.ServerPath;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.sale.image.FullImageView;
import com.viettel.sabeco.R;

/**
 * Thumbnail Image Adapter cho Chi tiet Hinh anh
 * 
 * @author: TamPQ
 * @version: 1.0
 * @since: 1.0
 */
public class ThumbnailImageAdapter extends BaseAdapter {
	private ArrayList<PhotoDTO> list = new ArrayList<PhotoDTO>();
	private int mGalleryItemBackground;
	private Context context;
	private BaseFragment fragParent;

	public ThumbnailImageAdapter(Context c, BaseFragment fragParent, ArrayList<PhotoDTO> list) {
		this.list = list;
		this.context = c;
		this.fragParent = fragParent;
		TypedArray a = c.obtainStyledAttributes(R.styleable.Gallery1);
		mGalleryItemBackground = a.getResourceId(R.styleable.Gallery1_android_galleryItemBackground, 0);
		a.recycle();
	}

	@Override
	public int getCount() {
		if (list != null) {
			return list.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup pt) {
		View row = convertView;
		ImageView imageItem = null;

		if (convertView == null) {
			LayoutInflater layout = (LayoutInflater) ((GlobalBaseActivity) context)
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = layout.inflate(R.layout.layout_thumbnail_item_view, null);
			imageItem = (ImageView) row.findViewById(R.id.thumbnail_item);
			imageItem.setScaleType(ImageView.ScaleType.FIT_XY);
			imageItem.setBackgroundResource(mGalleryItemBackground);
			row.setTag(imageItem);
		} else {
			imageItem = (ImageView) row.getTag();
		}
		imageItem.setImageResource(R.drawable.album_default);
		if (!StringUtil.isNullOrEmpty(list.get(position).thumbUrl)) {
			if (list.get(position).thumbUrl.contains(ExternalStorage.SDCARD_PATH))
				imageItem.setTag(list.get(position).thumbUrl);
			else
				imageItem.setTag(ServerPath.IMAGE_PATH + list.get(position).thumbUrl);
		}

		// get more
		if (position == list.size() - 3) {
			fragParent.onEvent(FullImageView.ACTION_GET_MORE_PHOTO, null, null);
		}

		return row;
	}
}
