/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.image;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.view.DisplayProgrameModel;
import com.viettel.dms.dto.view.ImageListDTO;
import com.viettel.dms.dto.view.ImageListItemDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableView;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * 
 * 04-01. Man hinh danh sach hinh anh cua khach hang
 * 
 * @author quangvt1
 * @version: 1.0
 * @since: 1.1
 */
public class ImageListView extends BaseFragment implements
		OnEventControlListener, VinamilkTableListener, OnTouchListener,OnItemSelectedListener  {
	public static final String TAG = ImageListView.class.getName();
	private GlobalBaseActivity parent; // parent
	// Table
	private DMSTableView tbCusList; // table danh sach khach hang
	private int currentPage = -1; // page hien tai
	private boolean isGetTotalPage = true; // co lay tong so page khong(chi lay
											// khi load lan dau)?
	// EditText
//	private VNMEditTextClearable edCusCode; // ma khach hang
	private VNMEditTextClearable edCusName; // ten khach hang
	private VNMEditTextClearable edFromDate; // Tu ngay(ngay chup)
	private VNMEditTextClearable edToDate; // Den ngay(ngay chup)
	// Button
	private Button btSearch; // nut tim kiem
	private Button btReInput; // nut nhap lai
	// Spinner tuyen
	private Spinner spLine; // combobox tuyen
	private SpinnerAdapter adLine; // adapter cho combobox tuyen
	private int selectedLineIndex; // index chon combobox tuyen
	// Thong tin search
//	private String cusCodeSearch = null; // tim kiem theo Ma KH
	private String cusNameSearch = null; // tim kiem theo Ten KH
	private String fromDateSearch = null; // tim kiem ngay bat dau cua anh
	private String toDateSearch = null; // tim kiem ngay ket thuc cua anh
	private boolean isSearch = false; // cho biet dang o che do mac dinh / dang
										// tim kiem
	// Gia tri mac dinh
	private String strFromDateDefault; // chuoi ngay bat dau mac dinh
	private String strToDateDefault; // chuoi ngay ket thuc mac dinh
	private String strFromDateSearchDefault; // chuoi ngay tim kiem bat dau mac
												// dinh
	private String strToDateSearchDefault; // chuoi ngay tim kiem ket thuc mac
											// dinh
	private int selectedLineDefault; // index lua chon tuyen mac dinh
	private String strEmpty = "";
	// Variables
	public ImageListDTO imgListDTO; // DTO de render
	boolean isReload = false; // cho biet khi goi ham onCreateView co load lại
								// khong
	private boolean isUpdateData = false; // update du lieu lai khong?
	// Cho biet fomDate/toDate duoc Touch
	private static final int DATE_FROM_CONTROL = 1; // touch from date
	private static final int DATE_TO_CONTROL = 2; // touch to date
	private int currentCalender; // index luu dang touch from/to date

	/***
	 * Tra ve the hien cua ImageListView
	 * 
	 * @author quangvt1
	 * @return ImageListView
	 */
	public static ImageListView getInstance() {
		Bundle args = new Bundle();
		args.putBoolean("isReload", true);

		ImageListView imgListView = new ImageListView();
		imgListView.setArguments(args);
		return imgListView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (GlobalBaseActivity) activity;
	}

	/**
	 * 
	 * khoi tao control khi load man hinh
	 * 
	 * @author quangvt1
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return: v
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.layout_image_list, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(getString(R.string.TITLE_VIEW_IMAGE_LIST));

		initData(v);

		if (imgListDTO != null && currentPage > 0) {
			spLine.setAdapter(adLine);
			renderLayout();
		} else {
			
			adLine = new SpinnerAdapter(parent, R.layout.simple_spinner_item,
					Constants.ARRAY_LINE_CHOOSE);
			spLine.setAdapter(adLine);
			selectedLineDefault = Constants.ARRAY_LINE_CHOOSE.length - 1;
			selectedLineIndex = selectedLineDefault;
			spLine.setSelection(selectedLineIndex);
			getCustomerList(1, true);
		}

		return v;

	}

	/**
	 * Khoi tao du lieu
	 * @author: QuangVT
	 * @since: 8:38:04 AM Mar 1, 2014
	 * @return: void
	 * @throws:  
	 * @param v
	 */
	private void initData(View v) {
//		edCusCode = (VNMEditTextClearable) v.findViewById(R.id.edCusCode);
		edCusName = (VNMEditTextClearable) v.findViewById(R.id.edCusName);
		edFromDate = (VNMEditTextClearable) v.findViewById(R.id.edFromDate);
		edToDate = (VNMEditTextClearable) v.findViewById(R.id.edToDate);

		edFromDate.setOnTouchListener(this);
		edFromDate.setIsHandleDefault(false);
		edToDate.setOnTouchListener(this);
		edToDate.setIsHandleDefault(false);

		final Date dateFrom = DateUtils.getFirstDateOfOffsetMonth(-2);
		final Date dateTo = new Date();
		final String strPattern = DateUtils.defaultDateFormat.toPattern();
		strFromDateDefault = DateUtils.convertDateTimeWithFormat(dateFrom,
				strPattern);
		strToDateDefault = DateUtils.convertDateTimeWithFormat(dateTo,
				strPattern);
		edFromDate.setText(strFromDateDefault);
		edToDate.setText(strToDateDefault);

		final String dateFormat = DateUtils.DATE_FORMAT_DEFAULT;// "dd/MM/yyyy";
		final String sqlFormat = DateUtils.DATE_FORMAT_SQL_DEFAULT;//"yyyy-MM-dd";
		strFromDateSearchDefault = DateUtils.convertFormatDate(edFromDate.getText().toString(), dateFormat, sqlFormat);
		strToDateSearchDefault = DateUtils.convertFormatDate(edToDate.getText().toString(), dateFormat, sqlFormat);
		fromDateSearch = strFromDateSearchDefault;
		toDateSearch = strToDateSearchDefault;

//		cusCodeSearch = edCusCode.getText().toString().trim();
		cusNameSearch = edCusName.getText().toString().trim();

		btSearch = (Button) v.findViewById(R.id.btSearch);
		btSearch.setOnClickListener(this);

		tbCusList = (DMSTableView) v.findViewById(R.id.tbCusList);
		tbCusList.setListener(this);
//		layoutTableHeader(tbCusList);
		initHeaderTable(tbCusList, new ImageListRow(parent, this));
		spLine = (Spinner) v.findViewById(R.id.spLine);
		spLine.setOnItemSelectedListener(this);
		btReInput = (Button) v.findViewById(R.id.btReInput);
		btReInput.setOnClickListener(this);
	}

	/**
	 * Tao header cho table
	 * 
	 * @author quangvt1
	 * @param table
	 * @return: void
	 */
//	private void layoutTableHeader(VinamilkTableView table) {
//		final String[] TABLE_TITLES = TableDefineContanst.TABLECUSIMAGETITLES;
//		final int[] TABLE_WIDTHS = TableDefineContanst.TABLECUSIMAGEWIDTHS;
//		final int TEXT_COLOR = ImageUtil.getColor(R.color.BLACK);
//		final int BG_COLOR = ImageUtil.getColor(R.color.TABLE_HEADER_BG);
//
//		final VinamilkHeaderTable headerTable = table.getHeaderView();
//		headerTable
//				.addColumns(TABLE_WIDTHS, TABLE_TITLES, TEXT_COLOR, BG_COLOR);
//	}

	/***
	 * Reset cac bien ve gia tri mac dinh
	 * 
	 * @author QuangVT
	 * @return void
	 */
	private void resetAllValue() {
		cusNameSearch = strEmpty;
//		cusCodeSearch = strEmpty;
		fromDateSearch = strFromDateSearchDefault;
		toDateSearch = strToDateSearchDefault;
		selectedLineIndex = selectedLineDefault;
		currentPage = -1;
		isSearch = false;

		// Set lai cho cac control tren man hinh
		resetLayout();
	}

	/**
	 * Reset gia tri tren layout ve gia tri mac dinh
	 * 
	 * @author quangvt1
	 * @return: void
	 */
	private void resetLayout() {
		edFromDate.setText(strFromDateDefault);
		edToDate.setText(strToDateDefault);
//		edCusCode.setText(strEmpty);
		edCusName.setText(strEmpty);
		spLine.setSelection(Constants.ARRAY_LINE_CHOOSE.length - 1);
	}

	/**
	 * Cap nhat thoi gian bat dau/ ket thuc khi chon tren dialog
	 * 
	 * @author quangvt1
	 * @param dayOfMonth
	 * @param monthOfYear
	 * @param year
	 */
	public void updateDate(int dayOfMonth, int monthOfYear, int year) {
		String sDay = String.valueOf(dayOfMonth);
		String sMonth = String.valueOf(monthOfYear + 1);
		if (dayOfMonth < 10) {
			sDay = "0" + sDay;
		}
		if (monthOfYear + 1 < 10) {
			sMonth = "0" + sMonth;
		}

		if (!DateUtils.checkDateInOffsetMonth(sDay, sMonth, year, -2)) {
			GlobalUtil.showDialogConfirm(this, parent,
					StringUtil.getString(R.string.TEXT_DATE_TIME_INVALID),
					StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), 0, null,
					false);
			return;
		}

		StringBuilder strDate = new StringBuilder();
		strDate.append(sDay).append("/").append(sMonth).append("/")
				.append(year).append(" ");
		if (currentCalender == DATE_FROM_CONTROL) {
			edFromDate.setTextColor(ImageUtil.getColor(R.color.BLACK));
			edFromDate.setText(strDate);
		} else if (currentCalender == DATE_TO_CONTROL) {
			edToDate.setTextColor(ImageUtil.getColor(R.color.BLACK));
			edToDate.setText(strDate);
		}
	}

	/**
	 * Lay danh sach khach hang
	 * 
	 * @author: QuangVT
	 * @param : page
	 * @param : getTotalPage
	 * @return: void
	 * @throws:
	 */
	private void getCustomerList(int page, boolean getTotalPage) {
		parent.showLoadingDialog();
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();

		isGetTotalPage = getTotalPage;
		data.putInt(IntentConstants.INTENT_PAGE, page);
		data.putBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE, getTotalPage);
		data.putLong(IntentConstants.INTENT_STAFF_ID, GlobalInfo.getInstance()
				.getProfile().getUserData().id);
		data.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance()
				.getProfile().getUserData().shopId);
//		data.putString(IntentConstants.INTENT_CUSTOMER_CODE, cusCodeSearch);
		data.putString(IntentConstants.INTENT_CUSTOMER_NAME,
				StringUtil.getEngStringFromUnicodeString(cusNameSearch.trim()));
		data.putBoolean(IntentConstants.INTENT_IS_SEARCH, isSearch);

		final String lineChoose = Constants.ARRAY_LINE_CHOOSE[selectedLineIndex];
		data.putString(IntentConstants.INTENT_VISIT_PLAN,
				DateUtils.getVisitPlan(lineChoose));
		data.putString(IntentConstants.INTENT_FIND_ORDER_FROM_DATE,
				fromDateSearch);
		data.putString(IntentConstants.INTENT_FIND_ORDER_TO_DATE, toDateSearch);

		e.viewData = data;
		e.action = ActionEventConstant.GET_LIST_IMAGE_CUS;
		e.sender = this;
		SaleController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_LIST_IMAGE_CUS:
			ImageListDTO tempDto = (ImageListDTO) modelEvent.getModelData();
			if (imgListDTO != null) {
				imgListDTO.listItem.clear();
				imgListDTO.listItem.addAll(tempDto.listItem);
			} else {
				imgListDTO = tempDto;
			}

			if (isGetTotalPage) {
				imgListDTO.totalCustomer = tempDto.totalCustomer;
			}

			if (isUpdateData) {
				isUpdateData = false;
				currentPage = -1;
			}

			renderLayout();
			parent.closeProgressDialog();
			requestInsertLogKPI(HashMapKPI.NVBH_DANHSACHHINHANH, modelEvent.getActionEvent().startTimeFromBoot);

			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		super.handleErrorModelViewEvent(modelEvent);
	}

	/**
	 * 
	 * Render table
	 * 
	 * @author: QuangVT
	 * @return: void
	 * @throws:
	 */
	private void renderLayout() { 
		if (currentPage <= 0) {
			tbCusList.setTotalSize(imgListDTO.totalCustomer,1);
			currentPage = tbCusList.getPagingControl().getCurrentPage();
		} else {
			tbCusList.setTotalSize(imgListDTO.totalCustomer,currentPage);
			tbCusList.getPagingControl().setCurrentPage(currentPage);
		}

		int offset = Constants.NUM_ITEM_PER_PAGE
				* (tbCusList.getPagingControl().getCurrentPage() - 1);
		int pos = 1 + offset;

		// Reader danh sach khach hang
		tbCusList.clearAllData();
		if (imgListDTO.listItem.size() > 0) {
//			tbCusList.hideNoContentRow();
			for (ImageListItemDTO imageListItemDTO : imgListDTO.listItem) {
				ImageListRow row = new ImageListRow(parent, this);
				row.renderLayout(pos, imageListItemDTO);
				row.setListener(this);
				tbCusList.addRow(row);
				pos++;
			}
		} else {
			tbCusList.showNoContentRow();
		}
	}

	/**
	 * Xu ly khi click cac button
	 * 
	 * @author quangvt1
	 * @param : v
	 * @return: void
	 * @throws:
	 */
	@Override
	public void onClick(View v) {
		if (v == btSearch) {
			GlobalUtil.forceHideKeyboard(parent);

			final String currFromDate = edFromDate.getText().toString().trim();
			final String currToDate = edToDate.getText().toString().trim();
//			final String currCusCode = edCusCode.getText().toString().trim();
			final String currCusName = edCusName.getText().toString().trim();
			final int currSelectLine = spLine.getSelectedItemPosition();

			isSearch = isSearching(currFromDate, currToDate,
					currCusName, currSelectLine);

			String dateTimePattern = StringUtil
					.getString(R.string.TEXT_DATE_TIME_PATTERN);
			Pattern pattern = Pattern.compile(dateTimePattern);

			String fromDateValid = validateDate(currFromDate, pattern);
			if (!StringUtil.isNullOrEmpty(currFromDate)
					&& fromDateValid == null) {
				parent.showDialog(StringUtil
						.getString(R.string.TEXT_START_DATE_SEARCH_SYNTAX_ERROR));
				return;
			}

			String toDateValid = validateDate(currToDate, pattern);
			if (!StringUtil.isNullOrEmpty(currToDate) && toDateValid == null) {
				parent.showDialog(StringUtil
						.getString(R.string.TEXT_END_DATE_SEARCH_SYNTAX_ERROR));
				return;
			}

			if (fromDateValid != null && toDateValid != null
					&& DateUtils.compareDate(fromDateValid, toDateValid) == 1) {
				GlobalUtil
						.showDialogConfirm(this, parent, StringUtil
								.getString(R.string.TEXT_DATE_TIME_INVALID_2),
								StringUtil
										.getString(R.string.TEXT_BUTTON_CLOSE),
								0, null, false);
				return;
			}

			// Kiem tra du lieu seach co thay doi khong?
			final boolean isDateSearchChange = validateDataSearchChange(
					fromDateValid, toDateValid, currCusName,
					currSelectLine);
			if (isDateSearchChange) {
				fromDateSearch = fromDateValid;
				toDateSearch = toDateValid;
//				cusCodeSearch = currCusCode;
				cusNameSearch = currCusName;
				selectedLineIndex = currSelectLine;

				tbCusList.getPagingControl().totalPage = -1;
				currentPage = -1;
				getCustomerList(1, true);
			} else {
				// Nhan Search nhung du lieu search khong thay doi thi khong lam
				// gi ca
			}
		} else if (v == btReInput) {
			resetLayout();
		}
	}

	/**
	 * Kiem tra du lieu search co phai gia tri mac dinh khong Dung : return true
	 * Ngươc lại : return false
	 * 
	 * @author quangvt1
	 * @param currSelectLine
	 * @param currCusName
	 * @param currToDate
	 * @param currFromDate
	 * @return
	 */
	private boolean isSearching(String currFromDate, String currToDate,
			String currCusName, int currSelectLine) {
		if (!currFromDate.equals(strFromDateDefault))
			return true;

		if (!currToDate.equals(strToDateDefault))
			return true;

		if (!StringUtil.isNullOrEmpty(currCusName))
			return true;

//		if (!StringUtil.isNullOrEmpty(currCusCode))
//			return true;

		if (currSelectLine != selectedLineDefault)
			return true;

		return false;
	}

	/**
	 * Kiem tra du lieu search co thay doi khong
	 * 
	 * @author quangvt1
	 * @param fromDateValid
	 * @param toDateValid
	 * @param currCusName
	 * @return
	 */
	private boolean validateDataSearchChange(String fromDateValid,
			String toDateValid, String currCusName,
			int currSelectLine) {
		// Kiem tra from date : 2 chuoi khac nhau
		if (!fromDateValid.equals(fromDateSearch))
			return true;

		// Kiem tra to date : 2 chuoi khac nhau
		if (!toDateValid.equals(toDateSearch))
			return true;

		// Kiem tra ma KH
//		if (!currCusCode.equals(cusCodeSearch))
//			return true;

		// Kiem tra ten KH
		if (!currCusName.equals(cusNameSearch))
			return true;

		// Kiem tra tuyen
		if (currSelectLine != selectedLineIndex)
			return true;
		
		// dungdq sua loi ko tim kiem ko back ra man hinh nay
//		return false;
		return true;
	}

	/***
	 * Kiem tra chuoi ngay co dung voi mau khong - Tra ve chuoi ngay theo dinh
	 * dang :yyyy-MM-dd neu chuoi hop le - Nguoc lai tra ve chuoi rong
	 * 
	 * @author quangvt1
	 * @param strDate
	 * @param pattern
	 * @return
	 */
	private String validateDate(final String strDate, Pattern pattern) {
		String result = Constants.STR_BLANK;
		if (!StringUtil.isNullOrEmpty(strDate)) {
			Matcher matcher = pattern.matcher(strDate);
			if (matcher.matches()) {
				try {
					Date date = StringUtil.stringToDate(strDate, "");
					result = StringUtil.dateToString(date,DateUtils.DATE_FORMAT_SQL_DEFAULT);
				} catch (Exception ex) {
				}
			}
		}
		return result;
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		if (control == tbCusList) {
			currentPage = tbCusList.getPagingControl().getCurrentPage();
			getCustomerList(currentPage, false);
		}
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
		switch (action) {
		case ImageListRow.ACTION_VIEW_ALBUM: {
			isReload = true;
			ImageListItemDTO dto = (ImageListItemDTO) data;

			CustomerDTO customer = new CustomerDTO();
			customer.customerId = dto.customerId;
			customer.customerCode = dto.customerCode;
			customer.customerName = dto.customerName;
			customer.setLat(dto.lat);
			customer.setLng(dto.lng);
			customer.setStreet(dto.street);
			customer.setHouseNumber(dto.houseNumber);

			if (!isSearch) {
				gotoListAlbumUser(customer);
			} else {
				gotoListAlbumUserForSearch(customer);
			}

			break;
		}
		default:
			break;
		}
	}

	private void gotoListAlbumUserForSearch(CustomerDTO customer) {
		isReload = true;
		final String fromDate = edFromDate.getText().toString().trim();
		final String toDate = edToDate.getText().toString().trim();

		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, "03-01");
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ID,
				customer.getCustomerId());
		bundle.putString(IntentConstants.INTENT_CUSTOMER_NAME,
				customer.getCustomerName());
		bundle.putString(IntentConstants.INTENT_CUSTOMER_CODE,
				customer.getCustomerCode());
		bundle.putString(IntentConstants.INTENT_FIND_ORDER_FROM_DATE, fromDate);
		bundle.putString(IntentConstants.INTENT_FIND_ORDER_TO_DATE, toDate);
		bundle.putInt(IntentConstants.INTENT_TYPE, 4);

		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = bundle;
		e.action = ActionEventConstant.GO_TO_LIST_ALBUM_USER_FOR_SEARCH;
		SaleController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * Toi man hinh ds album cua KH
	 * 
	 * @author quangvt1
	 * @param customer
	 * @return: void
	 * @throws:
	 */
	private void gotoListAlbumUser(CustomerDTO customer) {
		isReload = true;

		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, "03-01");
		bundle.putSerializable(IntentConstants.INTENT_CUSTOMER, customer);
		bundle.putInt(IntentConstants.INTENT_TYPE, 4);
		bundle.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo
				.getInstance().getProfile().getUserData().shopId);

		DisplayProgrameModel displayModel = new DisplayProgrameModel();
		displayModel.setModelData(imgListDTO.listPrograme);
		bundle.putSerializable(IntentConstants.INTENT_DISPLAY_PROGRAM_MODEL,
				displayModel);

		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = bundle;
		e.action = ActionEventConstant.GO_TO_LIST_ALBUM_USER;
		SaleController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * Nhan Broadcast
	 * 
	 * @author quangvt1
	 * @param action
	 * @param extras
	 */
	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				isUpdateData = true;
				resetAllValue();
				getCustomerList(1, true);
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v == edFromDate) {
			if (!v.onTouchEvent(event)) {
				currentCalender = DATE_FROM_CONTROL;
				parent.fragmentTag = ImageListView.TAG;
				parent.showDatePickerWithDate(
						GlobalBaseActivity.DATE_DIALOG_ID, edFromDate.getText()
								.toString(), true);
			}
		}

		if (v == edToDate) {
			if (!v.onTouchEvent(event)) {
				currentCalender = DATE_TO_CONTROL;
				parent.fragmentTag = ImageListView.TAG;
				parent.showDatePickerWithDate(
						GlobalBaseActivity.DATE_DIALOG_ID, edToDate.getText()
								.toString(), true);
			}
		}
		return false;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg0 == spLine) {
			if (selectedLineIndex != spLine.getSelectedItemPosition()) {
				selectedLineIndex = spLine.getSelectedItemPosition();
				onClick(btSearch);
			}
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
