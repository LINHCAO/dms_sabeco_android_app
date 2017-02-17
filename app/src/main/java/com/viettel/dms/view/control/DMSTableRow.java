/**
 * Copyright 2015 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.view.control;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * VinamilkTableRow.java
 * @author: dungdq3
 * @update: tuanlt11
 * @version: 1.0
 * @since:  10:32:11 AM Sep 18, 2014
 */
public class DMSTableRow extends
		LinearLayout implements View.OnClickListener {

	protected Context context;
	protected VinamilkTableListener listener;
	private ArrayList<View> arrView;
	private int[] arrRemoveViewId;
	private float totalWeight;
	// ds title cho header
	ArrayList<String> lstTitle = null;
	// index header hien tai
	int indexTitle = 0;
	private int widthTable = 0;

	private DMSTableRow(Context context) {
		super(context);
		this.context = context;
		arrView = new ArrayList<View>();
	}

	public DMSTableRow(Context context, int layout){
		this(context, layout, GlobalUtil.getDPIScreen());
	}

	public DMSTableRow(Context context, int layout, int[] arrRemoveId){
		this(context, layout, GlobalUtil.getDPIScreen(), arrRemoveId);
	}

	public DMSTableRow(Context context, int layout, double widthPercent){
		this(context, layout, (int) (GlobalUtil.getDPIScreen() * widthPercent), null);
	}

	public DMSTableRow(Context context, int layout, double widthPercent, int[] arrRemoveId){
		this(context, layout, (int) (GlobalUtil.getDPIScreen() * widthPercent), arrRemoveId);
	}

	public DMSTableRow(Context context, int layout, int widthTable){
		this(context, layout, widthTable, null);
	}

	public DMSTableRow(Context context, int layout, int widthTable, int[] arrRemoveId){
		this(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewGroup view = (ViewGroup)inflater.inflate(layout, this, false);
		this.widthTable = widthTable;
		//add remove id view
		arrRemoveViewId = arrRemoveId;
		addView(view);
	}

	/**
	 * set listener
	 *
	 * @author: dungdq3
	 * @since: 11:00:22 AM Sep 18, 2014
	 * @return: void
	 * @throws:
	 * @param listen:
	 */
	public void setListener(VinamilkTableListener listen){
		listener = listen;
	}

	/**
	 * check in remove id array
	 * @author: duongdt3
	 * @since: 17:37:16 3 Apr 2015
	 * @return: boolean
	 * @throws:
	 * @param idView
	 * @return
	 */
	private boolean isInViewRemoveArr(int idView){
		boolean result = false;
		if (arrRemoveViewId != null) {
			for (int idRemove : arrRemoveViewId) {
				if (idView == idRemove) {
					result = true;
					break;
				}
			}
		}
		return result;
	}
	/**
	 * Thêm view vao layout
	 *
	 * @author: dungdq3
	 * @since: 10:54:59 AM Sep 18, 2014
	 * @return: void
	 * @throws: :
	 */
	private void addView(ViewGroup row){
		setLayoutParams(row.getLayoutParams());
		setOrientation(((LinearLayout)row).getOrientation());
		if (row != null) {
			int childcount = row.getChildCount();
			for (int i = 0; i < childcount; i++) {

				View childAt = row.getChildAt(i);
				//Ignore remove view
				if (childAt != null && !isInViewRemoveArr(childAt.getId())) {
					arrView.add(childAt);
				}
			}
			row.removeAllViews();
		}
		removeAllViews();// add to this

		boolean isHaveWidth = false;
		for (View v : arrView) {
			this.addView(v);
			LinearLayout.LayoutParams llp = ((LinearLayout.LayoutParams) v.getLayoutParams());
			if (llp.width == 0
					&& llp.weight > 0f) {
				totalWeight += llp.weight;
			} else{
				isHaveWidth = true;
			}
		}
		// neu co mot view trong row set width thi ko tinh lai kich thuoc nua
		if(!isHaveWidth)
			renderAgain(widthTable);

	}

	/**
	 * set background color for row
	 *
	 * @author: dungdq3
	 * @since: 11:02:55 AM Sep 18, 2014
	 * @return: void
	 * @throws:
	 * @param color:
	 */
	protected void setBackgroundRowByColor(int color){
		for (View v: arrView) {
			GlobalUtil.setBackgroundByColor(v, color);
		}
	}

	/**
	 * set background resource
	 *
	 * @author: dungdq3
	 * @since: 2:33:59 PM Oct 13, 2014
	 * @return: void
	 * @throws:
	 * @param resource:
	 */
	protected void setBackgroundRowResource(int resource){
		for (View v: arrView) {
			GlobalUtil.setBackgroundByDrawable(v, resource);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == this) {
			GlobalUtil.forceHideKeyboardInput(context, v);
		}
	}

	/**
	 * set text color for all item in row
	 *
	 * @author: dungdq3
	 * @since: 11:18:32 AM Sep 18, 2014
	 * @return: void
	 * @throws:
	 * @param color:
	 */
	protected void setTextColorForRow(int color){
		for (View v : arrView) {
			if(v != null && v instanceof TextView){
				((TextView) v).setTextColor(color);
			}
		}
	}

	/**
	 * Render lai row, co the lam dong column cuoi cung
	 *
	 * @author: dungdq3
	 * @since: 3:00:17 PM Sep 23, 2014
	 * @return: void
	 * @throws:
	 * @param totalWidth:
	 */
	private void renderAgain(int totalWidth) {
		int totalWidthRender = 0;
		for (int i = 0, size = arrView.size(); i < size; i++) {
			View v = arrView.get(i);
			LinearLayout.LayoutParams llp = ((LinearLayout.LayoutParams) v.getLayoutParams());
			int leftMargin = llp.leftMargin;
			int rightMargin = llp.rightMargin;
			int totalMargin = leftMargin + rightMargin;
			int widthRender = (int) ((llp.weight / totalWeight) * totalWidth);
			//phần tử cuối cùng, sẽ dùng width còn lại để render
			if (i == size - 1) {
				widthRender = totalWidth - totalWidthRender;
			}
			//sum width include margin
			totalWidthRender += widthRender;

			//subtract margin, set width view
			widthRender -= totalMargin;
			v.getLayoutParams().width = widthRender;
		}
	}

	 /**
	 * render Header
	 * @author: Tuanlt11
	 * @return: void
	 * @throws:
	*/
	private void renderHeader(View row) {
		row.setBackgroundColor(ImageUtil.getColor(R.color.TABLE_BG));
		if (row instanceof ViewGroup) {
//			row.setBackgroundColor(ImageUtil.getColor(R.color.TABLE_HEADER_BG));
//			row.setPadding( GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(5),  GlobalUtil.dip2Pixel(1),  GlobalUtil.dip2Pixel(1));
			for (int i = 0, size = ((ViewGroup) row).getChildCount(); i < size; i++) {
				renderHeader(((ViewGroup) row).getChildAt(i));
			}
		} else {
			TextView textView = changeViewToTextView(row);
			textView.setMinHeight(GlobalUtil.dip2Pixel(35));
			textView.setPadding(0, GlobalUtil.dip2Pixel(5), 0,
					GlobalUtil.dip2Pixel(5));
			textView.setTextColor(ImageUtil.getColor(R.color.BLACK));
			textView.setBackgroundColor(ImageUtil.getColor(R.color.TABLE_HEADER_BG));
			textView.setTypeface(null, Typeface.BOLD);
			textView.setGravity(Gravity.CENTER);
			textView.setVisibility(View.VISIBLE);
			//clear listener, fix loi fire event khi click header
			textView.setOnClickListener(null);
			textView.setOnTouchListener(null);

			if (lstTitle != null && lstTitle.size() > indexTitle) {
				textView.setText(lstTitle.get(indexTitle));
				indexTitle++;
			}
		}
	}

	/**
	 * remove image cua view
	 *
	 * @author: tuanlt11
	 * @since: 5:25:11 PM Oct 8, 2014
	 * @return: void
	 * @throws:
	 * @param row:
	 */
	public void removeImage(View row) {
		if (row instanceof ImageView)
			((ImageView) row).setImageDrawable(null);
		else if (row instanceof CheckBox){
			((CheckBox) row).setButtonDrawable(android.R.color.transparent);
		}
	}

	 /**
	 * render Header
	 * @author: Tuanlt11
	 * @return: void
	 * @throws:
	*/
	public void getHeaderTable() {
		getHeaderTable(null);
	}

	 /**
	 * render header voi list title
	 * @author: Tuanlt11
	 * @param listTitle
	 * @return: void
	 * @throws:
	*/
	public void getHeaderTable(ArrayList<String> lstTitle) {
		this.lstTitle = lstTitle;
		for (View v : arrView) {
			renderHeader(v);
		}
	}

	public void showRowSum(CharSequence title,View... params) {
		showRowSum(title, false, params);
	}
	 /**
	 * Hien thi row sum, an di nhung view truyen vao
	 * @author: Tuanlt11
	 * @param params
	 * @return: void
	 * @throws:
	*/
	public void showRowSum(CharSequence title, boolean isGroupTotalView, View... params) {
		int i = 0;
		int totalWidth = 0;
		for (View viewItem : params) {
			TextView textView = null;
			//viewItem.setBackgroundColor(Color.WHITE);
			LayoutParams param = (LayoutParams) viewItem.getLayoutParams();

			if (viewItem instanceof ViewGroup) {
				ViewGroup viewGroup = (ViewGroup) viewItem;
				for (int j = 0, size = viewGroup.getChildCount(); j < size; j++) {
					View viewChild = viewGroup.getChildAt(j);
					textView = changeViewToTextView(viewChild);
				}
			} else {
				textView = changeViewToTextView(viewItem);
			}

			if (textView != null) {
				if (i == 0) {
					// phan tu dau tien mat di 1 dp
					param.width = param.width + param.rightMargin;
					param.rightMargin = 0;
					textView.setText("");
					if (isGroupTotalView) {
						totalWidth += param.width - 10;
						param.width = 10;
					}
				} else if (i == params.length - 1) {
					// phan tu cuoi mat di 1 dp
					param.width = param.width + param.leftMargin;
					param.leftMargin = 0;

					if (isGroupTotalView) {
						totalWidth += param.width;
						param.width = totalWidth;
					}
					
					textView.setTextColor(ImageUtil.getColor(R.color.BLACK));
					textView.setTypeface(null, Typeface.BOLD);
					textView.setText(title);

					if (textView != viewItem) {
						LayoutParams paramTv = (LayoutParams) textView.getLayoutParams();
						textView.setLayoutParams(paramTv);
						textView.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
						textView.setPadding(GlobalUtil.dip2Pixel(5), GlobalUtil.dip2Pixel(5),
								GlobalUtil.dip2Pixel(5), GlobalUtil.dip2Pixel(5));
					}
				} else {
					// cai phan tu o giua mat moi ben 1 dp => mat 2dp
					param.width = param.width + param.leftMargin + param.rightMargin;
					param.leftMargin = 0;
					param.rightMargin = 0;
					textView.setText("");
					
					if (isGroupTotalView) {
						totalWidth += param.width - 10;
						param.width = 10;
					}
				}
			}
			i++;
		}
	}

	/**
	 * Hien thi row sum, an di nhung view truyen vao
	 * @author: Tuanlt11
	 * @param params
	 * @return: void
	 * @throws:
	*/
	public void showRowSum(CharSequence title,int color,View... params) {
		int i = 0;
		LayoutParams paramTotal = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		for (View viewItem : params) {
			TextView textView = null;
			viewItem.setBackgroundColor(color);
			LayoutParams param = (LayoutParams) viewItem.getLayoutParams();
			if (viewItem instanceof TextView) {
				textView = (TextView) viewItem;
			} else {
				textView = changeViewToTextView(viewItem);
			}
			paramTotal.width += param.width + param.leftMargin + param.rightMargin;
			if (i == params.length -1 ) {
				// set title vao view nam giua
				paramTotal.setMargins(1,1,1,1);
				textView.setLayoutParams(paramTotal);
				textView.setTextColor(ImageUtil.getColor(R.color.BLACK));
				textView.setTypeface(null, Typeface.BOLD);
				textView.setGravity(android.view.Gravity.CENTER);
				textView.setText(title);
			} else {
				textView.setVisibility(View.GONE);
			}
			i++;
		}
	}

	 /**
	 * Doi cac view khac nhu edittext, image thanh textview
	 * @author: Tuanlt11
	 * @return: void
	 * @throws:
	*/
	protected TextView changeViewToTextView(View row){
		removeImage(row);
		TextView textView;
		if (TextView.class.equals(row.getClass())) {
			textView = (TextView) row;
			// th textview
			if (StringUtil.isNullOrEmpty(textView.getText().toString())) {
				if (textView.getTag() != null){
					textView.setText(textView.getTag().toString());
				}
			}
		} else {
			// tb ko phai la textview
			textView = new TextView(context);

			android.view.ViewGroup.LayoutParams params = row.getLayoutParams();
			if (row instanceof EditText || row instanceof CheckBox) {
				LinearLayout.LayoutParams paramsEdit = (LayoutParams) params;
				paramsEdit.leftMargin = 0;
				paramsEdit.rightMargin = 0;
				paramsEdit.topMargin = 0;
				paramsEdit.bottomMargin = 0;

				paramsEdit.width = LinearLayout.LayoutParams.MATCH_PARENT;
				paramsEdit.height = LinearLayout.LayoutParams.MATCH_PARENT;
			}
			textView.setLayoutParams(params);

			if (row.getTag() != null){
				textView.setText(row.getTag().toString());
			}
			try {
				ViewGroup parent = (ViewGroup) row.getParent();
				parent.removeView(row);
				parent.addView(textView);
			} catch (Exception e) {
				VTLog.v("x", "x", e);
			}
		}
		return textView;
	}

//	protected TextView changeViewToTextView(View row){
//		removeImage(row);
//		TextView textView;
//		if (TextView.class.equals(row.getClass())) {
//			textView = (TextView) row;
//			// th textview
//			if (StringUtil.isNullOrEmpty(textView.getText().toString())) {
//				if (textView.getTag() != null){
//					textView.setText(textView.getTag().toString());
//				}
//			}
//		} else {
//			// tb ko phai la textview
//			textView = new TextView(context);
//
//			android.view.ViewGroup.LayoutParams params = row.getLayoutParams();
//			LinearLayout.LayoutParams paramsEdit = (LayoutParams) params;
//			paramsEdit.leftMargin = 0;
//			paramsEdit.rightMargin = 0;
//			paramsEdit.topMargin = 0;
//			paramsEdit.bottomMargin = 0;
//			
//			paramsEdit.width = LinearLayout.LayoutParams.MATCH_PARENT;
//			paramsEdit.height = LinearLayout.LayoutParams.MATCH_PARENT;
//			//set edit params
//			textView.setLayoutParams(paramsEdit);
//
//			if (row.getTag() != null){
//				textView.setText(row.getTag().toString());
//			}
//			try {
//				ViewGroup parent = (ViewGroup) row.getParent();
//				parent.removeView(row);
//				parent.addView(textView);
//			} catch (Exception e) {
//				VTLog.v("x", "x", e);
//			}
//		}
//		return textView;
//	}

	/**
	 * display double data in TextView
	 * @author: duongdt3
	 * @since: 14:39:50 2 Apr 2015
	 * @return: void
	 * @throws:
	 * @param tv
	 * @param number
	 */
	protected void display(TextView tv, double number){
		StringUtil.display(tv, number);
	}

	/**
	 * display long data in TextView
	 * @author: duongdt3
	 * @since: 14:40:09 2 Apr 2015
	 * @return: void
	 * @throws:
	 * @param tv
	 * @param number
	 */
	protected void display(TextView tv, long number){
		StringUtil.display(tv, number);
	}

	/**
	 * display int data in TextView
	 * @author: duongdt3
	 * @since: 14:40:29 2 Apr 2015
	 * @return: void
	 * @throws:
	 * @param tv
	 * @param number
	 */
	protected void display(TextView tv, int number){
		StringUtil.display(tv, number);
	}

	/**
	 * display CharSequence data in TextView
	 * @author: duongdt3
	 * @since: 14:40:37 2 Apr 2015
	 * @return: void
	 * @throws:
	 * @param tv
	 * @param str
	 */
	protected void display(TextView tv, CharSequence str){
		StringUtil.display(tv, str);
	}

	/**
	 * display percent
	 * @author: duongdt3
	 * @since: 15:45:56 3 Apr 2015
	 * @return: void
	 * @throws:
	 * @param tv
	 * @param percent
	 */
	protected void displayPercent(TextView tv, double percent){
		StringUtil.displayPercent(tv, percent);
	}

	/**
	 * set color cho percent
	 * @author: hoanpd1
	 * @since: 15:03:01 07-04-2015
	 * @return: void
	 * @throws:
	 * @param tv
	 * @param progress
	 * @param standarPercentAlow
	 * @param color
	 */
	protected void setTextColorPercent(TextView tv, double progress, double standarPercentAlow, int color) {
		tv.setTextColor(ImageUtil.getColor(R.color.BLACK));
		if (progress < standarPercentAlow) {
			tv.setTextColor(color);
		}
	}

	/**
	 * set textview color for multi textview
	 * @author: duongdt3
	 * @since: 09:52:37 13 Apr 2015
	 * @return: void
	 * @throws:
	 * @param color
	 * @param views
	 */
	protected void setTextColor(int color, TextView ... views){
		GlobalUtil.setTextColor(color, views);
	}

	protected void setTextColorResource(int idColor, TextView ... views){
		GlobalUtil.setTextColor(ImageUtil.getColor(idColor), views);
	}
	
	protected void setBackgroundColor(int color, View ... views){
		GlobalUtil.setBackgroundColor(color, views);
	}
	
	protected void setBackgroundColorResource(int idColor, View ... views){
		GlobalUtil.setBackgroundColor(ImageUtil.getColor(idColor), views);
	}
	
	 /**
	 * set type face textview
	 * @author: Tuanlt11
	 * @param typeFace
	 * @param views
	 * @return: void
	 * @throws:
	*/
	protected void setTypeFace(int typeFace, TextView ... views){
		GlobalUtil.setTypeFace(typeFace, views);
	}
}
