/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.control;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * Paging Control
 * PagingControl.java
 * @version: 1.0 
 * @since:  08:28:39 20 Jan 2014
 */
public class PagingControl extends LinearLayout {
	//private static final int VinamilkTableView.LIMIT_ROW_PER_PAGE = 10;
	public int totalPage = -1;
	int currentPage = 1;
	OnEventControlListener listener;
	ArrayList<String> listData = new ArrayList<String>();
	HorizontalListView pagingView;
	PageAdapter adapter;
	Button btPage ;
	int action = 0;
	int oldPage=0;
	
	@SuppressWarnings("unused")
	private OnItemClickListener onPageItemClick = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v,
				int position, long id) {
			// categoryList.elementAt(categoryId).linkAvatar=categoryList.elementAt(0).linkAvatar;
			int page = Integer.parseInt(listData.get(position));
			setCurrentPage(page);
			changePageNumber(page);
			listener.onEvent(action, PagingControl.this, position + 1);
			adapter.notifyDataSetChanged();
		}
	};

	public PagingControl(Context context, AttributeSet attrs) {
		super(context, attrs);
		listener = (OnEventControlListener) context;
		// TODO Auto-generated constructor stub
		LayoutInflater in = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		in.inflate(R.layout.paging_control, this);
		btPage =(Button) findViewById(R.id.btPage);
		btPage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialogInput();
			}
		});
	}

	public void setItemListener(OnEventControlListener listener) {
		this.listener = listener;
	}

	public void setTotalPage(int page) {
		totalPage = page;
		
		listData = new ArrayList<String>();
		for (int i = 0; i < VinamilkTableView.LIMIT_ROW_PER_PAGE && i < page; i++) {
			listData.add("" + (i + 1));
		}

		/**
		 * @author: HaiTC3
		 * @since: 1.0
		 * @Description: cap nhat thay doi padding button khi thay doi so luong
		 *               page
		 */
		if (listData.size() > 0 && pagingView != null) {
			adapter = new PageAdapter();
			pagingView.setAdapter(adapter);
		}
	}

	public void setAction(int act) {
		action = act;
	}

	public void setCurrentPage(int curPage) {
		oldPage = currentPage;
		currentPage = curPage;
		btPage.setText("Trang "+currentPage+"/"+totalPage);
		handleShowDisplay();
	}

	public int getCurrentPage() {
		return currentPage;
	}
	public int getOldPage() {
		return oldPage;
	}

	private void handleShowDisplay() {
		if (pagingView == null) {
			pagingView = (HorizontalListView) findViewById(R.id.glPaging);
			adapter = new PageAdapter();
			pagingView.setAdapter(adapter);
//			pagingView.setOnItemClickListener(onPageItemClick);
		} else {
			changePageNumber(currentPage);
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * @author: PhucNT
	 * @since: 1.0
	 * @Description: khi nhan vao mot page se thay doi nhung so page lan can dam
	 *               bao so page duoc nhan phai nam o chinh giua, giong google.
	 */
	protected void changePageNumber(int position) {
		// TODO Auto-generated method stub

		if (position > VinamilkTableView.LIMIT_ROW_PER_PAGE / 2 + 1) {
			listData.clear();
			for (int i = position - VinamilkTableView.LIMIT_ROW_PER_PAGE / 2; i < position
					+ VinamilkTableView.LIMIT_ROW_PER_PAGE / 2
					&& i <= totalPage; i++) {

				listData.add("" + i);
			}
		} else {
			listData.clear();
			for (int i = 1; i <= VinamilkTableView.LIMIT_ROW_PER_PAGE && i <= totalPage; i++) {
				listData.add("" + (i));
			}
		}
	}

	public void showDialogInput() {
		AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
//		SpannableObject textConfirmed = new SpannableObject();
//		textConfirmed.addSpan("Nhảy tới trang (1<= n <="+totalPage+")",
//				ImageUtil.getColor(R.color.WHITE),
//				android.graphics.Typeface.NORMAL);
//		alert.setTitle(textConfirmed.getSpan());
		//alert.setMessage("Message");

		alert.setTitle("Nhảy tới trang (1<= n <=" + totalPage + ")");
		// Set an EditText view to get user input
		final EditText input = new EditText(getContext());
		input.setTextColor(ImageUtil.getColor(R.color.WHITE));
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		alert.setView(input);

		alert.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				// Do something with value!
				try {
					GlobalUtil.forceHideKeyboardInput(getContext(), input);
					int page = Integer.parseInt(value);
					if (0 < page && page <= totalPage) {
						setCurrentPage(page);
						changePageNumber(page);
						listener.onEvent(action, PagingControl.this, page);
						adapter.notifyDataSetChanged();
					} else {
						GlobalUtil.showToastMessage(GlobalInfo.getInstance().getActivityContext(), "Vui lòng nhập đúng số trang", Toast.LENGTH_SHORT);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					GlobalUtil.showToastMessage(GlobalInfo.getInstance().getActivityContext(), "Vui lòng nhập số trang", Toast.LENGTH_SHORT);
				}
			}
		});

		alert.setNegativeButton("Hủy bỏ",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
						
					}
				});

		alert.show();
	}

	class PageAdapter extends ArrayAdapter<String> {

		public PageAdapter() {
			super(PagingControl.this.getContext(), 0, listData);
			// TODO Auto-generated constructor stub
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
			if (position < listData.size()) {
				View row = convertView;
				VTLog.i("GET VIEW", "" + position);
				if (row == null) {
					row = new Button(PagingControl.this.getContext());
					row.setLayoutParams(new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
					row.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_button_flat_light));
					row.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
//							Toast.makeText(getContext(), ((Button)v).getText() , Toast.LENGTH_SHORT).show();
							int page = Integer.parseInt(listData.get(position));
							setCurrentPage(page);
							changePageNumber(page);
							listener.onEvent(action, PagingControl.this, position + 1);
							adapter.notifyDataSetChanged();
						}
					});
				}
				int page = Integer.parseInt(listData.get(position));
				layoutButton((Button) row, page);
				return row;
			}
			return convertView;
		}

	}

	private void layoutButton(Button bt, int page) {
		bt.setText("" + (page));
		if (currentPage == page) {
			bt.setTextColor(ImageUtil.getColor(R.color.COLOR_USER_NAME));
		} else {
			bt.setTextColor(ImageUtil.getColor(R.color.BLACK));
		}
		bt.setPadding(10, 10, 10, 10);
	}
}
