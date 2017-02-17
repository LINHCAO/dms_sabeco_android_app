/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.customer;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.dto.view.AlbumDTO;
import com.viettel.dms.dto.view.CustomerInfoDTO;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.main.AbstractAlertDialog;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.sabeco.R;

/**
 * Dialog cap nhat avatar
 * 
 * @author: dungdq3
 * @version: 1.0
 * @since: 1.0
 */
public class CustomerUpdateAvatar extends AbstractAlertDialog implements
		android.view.View.OnClickListener, OnTouchListener {

	private final View v;
	private TextView tvTakePhoto;
	private TextView tvChoosePhoto;
	private TextView tvDelete;
	private CustomerInfoDTO customerInfoDTO;
	private BaseFragment fragment;

	private static final int ACTION_DELETE_AVATAR = 6;
	private static final int ACTION_CANCEL_AVATAR = 7;

	public CustomerUpdateAvatar(Context context, BaseFragment base,
			CustomerInfoDTO customerInfo, boolean hasAvatar) {
		// hasAvatar == true: da co avatar
		super(context, base, StringUtil.getString(R.string.TEXT_UPDATE_AVATAR));
		fragment = base;
		v=setViewLayout(R.layout.layout_customer_update_avatar);
		customerInfoDTO = customerInfo;
		tvTakePhoto = (TextView) v.findViewById(R.id.tvTakePhoto);
		tvChoosePhoto = (TextView) v.findViewById(R.id.tvChoosePicture);
		tvDelete = (TextView) v.findViewById(R.id.tvDelete);
		tvTakePhoto.setOnClickListener(this);
		tvChoosePhoto.setOnClickListener(this);
		tvDelete.setOnClickListener(this);
		if (hasAvatar) {
			tvDelete.setVisibility(View.VISIBLE);
		} else {
			tvDelete.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == tvTakePhoto) {
			setEventFromAlert(ActionEventConstant.ACTION_TAKE_PHOTO, tvTakePhoto, null);
			dismiss();
		} else if (v == tvDelete) {
			showDialogDeleteAvatar();
		} else if(v==tvChoosePhoto) {
			chooseAvatarFromAlbum();
		} 
	}

	/**
	* Chọn ảnh đại diện từ album hình ảnh đã có sẵn
	* @author: dungdq3
	* @return: void
	* @date: Jan 3, 2014
	*/
	private void chooseAvatarFromAlbum() {
		// TODO Auto-generated method stub
		dismiss();
		Bundle bundle = new Bundle();
		AlbumDTO albumDTO= new AlbumDTO();
		albumDTO.setAlbumType(Constants.TYPE_HADB);
		albumDTO.setAlbumTitle(StringUtil.getString(R.string.TEXT_PHOTO_SALE_POSITION));
		bundle.putInt(IntentConstants.INTENT_TYPE, 0);
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, customerInfoDTO.getCustomer().getCustomerId());
		bundle.putString(IntentConstants.INTENT_SHOP_ID, "");
		bundle.putString(IntentConstants.INTENT_CUSTOMER_NAME, customerInfoDTO.getCustomer().getCustomerName());
		bundle.putString(IntentConstants.INTENT_CUSTOMER_CODE, customerInfoDTO.getCustomer().getCustomerCode());
		bundle.putSerializable(IntentConstants.INTENT_ALBUM_INFO, albumDTO);

		if(fragment != null){
			bundle.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE,  fragment.getMenuIndexString());
		}
		
		switchFragment(ActionEventConstant.GO_TO_ALBUM_DETAIL_USER, bundle, SaleController.getInstance());
	}

	/**
	 * HIện thị dialog Xóa hình ảnh đại diện
	 * 
	 * @author: dungdq3
	 * @return: void
	 * @date: Jan 3, 2014
	 */
	private void showDialogDeleteAvatar() {
		// TODO Auto-generated method stub
		showDialogFromAlert(StringUtil.getString(R.string.TEXT_DELETE_AVATAR),
				StringUtil.getString(R.string.TEXT_AGREE),
				ACTION_DELETE_AVATAR,
				StringUtil.getString(R.string.TEXT_CANCEL),
				ACTION_CANCEL_AVATAR, null);
	}

	/**
	* Ẩn hiện menu xóa ảnh đại diện
	* @author: dungdq3
	* @param: boolean hasAvatar
	* @return: void
	* @date: Jan 6, 2014
	*/
	public void setHideMenuDelete(boolean hasAvatar) {
		// TODO Auto-generated method stub
		// nếu đã có avatar (hasAvatar==true) thì sẽ hiện menu Xóa, ngược lại thì ko!
		if(hasAvatar){
			tvDelete.setVisibility(View.VISIBLE);
		}else{
			tvDelete.setVisibility(View.GONE);
		}
	}
}
