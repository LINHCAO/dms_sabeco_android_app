/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.control;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.viettel.sabeco.R;


/**
 *  tien ich delete dialog, alertDialog wrapper (interface)
 *  @author: AnhND
 *  @version: 1.0
 *  @since: Jun 16, 2011
 */
public class PopupDialog implements OnKeyListener, DialogInterface {

	protected Object userData = null;//data user dinh kem
	protected View dialogView = null;//dialog view
	protected Context context = null;//context cua activity chua dialog
	protected CustomDialog dialog = null;//alert dialog
	protected String text = "";
	protected android.view.View.OnClickListener clickListener = null;//clickListener
	//cancel listener khi user close dialog bang nut back
	protected OnCancelListener cancelListener = null;
	int resId = 0;
	/**
	 * @param context
	 */
	public PopupDialog(Context context,int resId,String str) {
		// TODO Auto-generated constructor stub
		this.context = context;
		text = str;
		this.resId = resId;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	public void setResId(int resId){
		this.resId = resId;
	}
	/**
	 * 
	*  show dialog
	*  @author: AnhND
	*  @param context
	*  @return
	*  @return: DeleteDialog
	*  @throws:
	 */
	public void show() {
		if (dialog == null) {
			CustomDialog.Builder builder = new CustomDialog.Builder(context);
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			dialogView = inflater.inflate(R.layout.custom_dialog, null);
			if (clickListener != null){
				dialogView.setOnClickListener(clickListener);
			}
			ImageView imgView = (ImageView) dialogView.findViewById(R.id.icon);
			TextView tvText = (TextView) dialogView.findViewById(R.id.title);
			imgView.setImageResource(resId);
			tvText.setText(text);
			builder.setContentView(dialogView);
			dialog = builder.create();
		}
		dialog.setOnKeyListener(this);
		dialog.show();
		
	}
	
	/**
	 * 
	*  set onClickListener
	*  @author: AnhND
	*  @param listener
	*  @return: void
	*  @throws:
	 */
	public void setOnClickListener(android.view.View.OnClickListener listener) {
		this.clickListener = listener;
		if (dialogView != null && listener != null) {
			dialogView.setOnClickListener(clickListener);
		}
	}
	
	/**
	 * 
	*  set user data
	*  @author: AnhND
	*  @return: void
	*  @throws:
	 */
	public void setUserData(Object obj) {
		userData = obj;
	}
	
	/**
	 * 
	*  get user data
	*  @author: AnhND
	*  @return
	*  @return: Object
	*  @throws:
	 */
	public Object getUserData() {
		return userData;
	}
	
	/**
	 * 
	*  get dialog view
	*  @author: AnhND
	*  @return
	*  @return: View
	*  @throws:
	 */
	public View getDialogView(){
		return dialogView;
	}
	
	/**
	 * 
	*  set cancel listener, goi onCancel khi user nhan nut back
	*  @author: AnhND
	*  @param listener
	*  @return: void
	*  @throws:
	 */
	public void setOnCancelListener(OnCancelListener listener) {
		this.cancelListener = listener;
	}

	/* (non-Javadoc)
	 * @see android.content.DialogInterface.OnKeyListener#onKey(android.content.DialogInterface, int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if (cancelListener != null) {
				cancelListener.onCancel(this);
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see android.content.DialogInterface#cancel()
	 */
	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		if (dialog != null) {
			dialog.cancel();
		}
	}

	/* (non-Javadoc)
	 * @see android.content.DialogInterface#dismiss()
	 */
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		if (dialog != null) {
			dialog.dismiss();
		}
	}
}
