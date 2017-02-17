/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.pg;

import android.app.Activity;
import android.os.Bundle;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.viettel.dms.constants.FragmentMenuContanst;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.dto.view.GeneralStatisticsTNPGViewDTO;
import com.viettel.dms.dto.view.GeneralStatisticsTNPGViewDTO.CustomerInfo;
import com.viettel.dms.dto.view.GeneralStatisticsViewDTO.GeneralStatisticsInfo;
import com.viettel.dms.dto.view.ListStaffDTO.StaffItem;
import com.viettel.dms.dto.view.ProgressSoldViewDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.control.DMSTableView;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.sale.statistic.NVBHGeneralStatisticsDateDetailRow;
import com.viettel.dms.view.sale.statistic.NVBHGeneralStatisticsMonthDetailRow;
import com.viettel.dms.view.sale.statistic.NVBHGeneralStatisticsView;
import com.viettel.dms.view.tnpg.statistic.TNPGGeneralStatisticsRow;
import com.viettel.sabeco.R;

import java.util.ArrayList;
import java.util.List;

/**
 * TNPG General Statistic View PGGeneralStatisticView.java
 * 
 * @version: 1.0
 * @since: 08:32:06 20 Jan 2014
 */
public class PGGeneralStatisticView extends BaseFragment implements
		OnEventControlListener, VinamilkTableListener, OnTouchListener,
		OnItemSelectedListener {
	// TAG of fragment
	public static final String TAG = PGGeneralStatisticView.class.getName();
	// parent Activity
	private GlobalBaseActivity parent;
	// BEGIN USER INFO
	private String userId;
	private String shopId;
	// END USER INFO

	// BEGIN DECLARE
	private TextView tvCustomerName;
	private TextView tvDiaChi;
	private TextView tvSoldPercent;
	private VNMEditTextClearable edDate;
	private Spinner spinnerNVTT;
	private DMSTableView tbStatistics;
	private GeneralStatisticsTNPGViewDTO dto;
	private float progressSold;
	private ArrayList<StaffItem> listStaff = null;
	private int posStaffCurrent = -1;
	// END DECLARE
	private boolean isCodeSelectedSpniner = false;
	private int typeRequest = NVBHGeneralStatisticsView.TYPE_REQUEST_TNPG;
	@SuppressWarnings("unused")
	private String parrentStaffId;

	// BEGIN NEW INSTANCE
	public static PGGeneralStatisticView newInstance(Bundle b) {
		PGGeneralStatisticView instance = new PGGeneralStatisticView();
		instance.setArguments(b);
		return instance;
	}

	public static PGGeneralStatisticView getInstance(Bundle b) {
		return newInstance(b);
	}

	// END NEW INSTANCE

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (GlobalBaseActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_tttt_general_report_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		initUserInfo();
		initView(v);
		initHeaderText();
		initHeaderMenu();
		initData();
		return v;
	}

	/**
	 * initUserInfo from other View send
	 * 
	 * @author: duongdt3
	 * @since: 19:28:40 5 Dec 2013
	 * @return: void
	 * @throws:
	 */
	private void initUserInfo() {
		Bundle param = getArguments();
		if (param != null) {
			typeRequest = param.getInt(IntentConstants.INTENT_TYPE_REQUEST, NVBHGeneralStatisticsView.TYPE_REQUEST_TNPG);
			// case GSBH login
			switch (typeRequest) {
			case NVBHGeneralStatisticsView.TYPE_REQUEST_TNPG: {
				userId = GlobalInfo.getInstance().getProfile().getUserData().id
						+ "";
				shopId = GlobalInfo.getInstance().getProfile().getUserData().shopId;
				parrentStaffId = "";
			}
				break;
			default: {
				// case GST login, view detail report
				userId = param.getString(IntentConstants.INTENT_STAFF_ID);
				shopId = param.getString(IntentConstants.INTENT_SHOP_ID);
				parrentStaffId = param.getString(IntentConstants.INTENT_PARENT_STAFF_ID);
			}
				break;
			}
		}
	}

	/**
	 * Get vies from layout
	 * 
	 * @author: duongdt3
	 * @since: 19:28:21 5 Dec 2013
	 * @return: void
	 * @throws:
	 * @param view
	 */
	private void initView(View view) {
		tvCustomerName = (TextView) view.findViewById(R.id.tvCustomerName);
		tvDiaChi = (TextView) view.findViewById(R.id.tvDiaChi);
		tvSoldPercent = (TextView) view.findViewById(R.id.tvSoldPercent);
		edDate = (VNMEditTextClearable) view.findViewById(R.id.edDate);
		edDate.setOnTouchListener(this);
		edDate.setIsHandleDefault(false);

		spinnerNVTT = (Spinner) view.findViewById(R.id.spinnerNVTT);
		spinnerNVTT.setOnItemSelectedListener(this);
		tbStatistics = (DMSTableView) view.findViewById(R.id.tbStatistics);
		tbStatistics.setListener(this);

		// Mac dinh an chi tiet thang cua GST
		LinearLayout llGSTDetailMonth = (LinearLayout) view.findViewById(R.id.llGSTDetailMonth);
		llGSTDetailMonth.setVisibility(View.INVISIBLE);

		// neu la GST xem khong can hien thi chi tiet KH
		if (typeRequest == NVBHGeneralStatisticsView.TYPE_REQUEST_GST_DATE_DETAIL
				|| typeRequest == NVBHGeneralStatisticsView.TYPE_REQUEST_GST_MONTH_DETAIL) {
			View llHeaderInfoTNPG = view.findViewById(R.id.llHeaderInfoTNPG);
			llHeaderInfoTNPG.setVisibility(View.GONE);

			// neu la chi tiet thang tu GST thi hien them Tien do thang
			if (typeRequest == NVBHGeneralStatisticsView.TYPE_REQUEST_GST_MONTH_DETAIL) {
				tvSoldPercent = (TextView) view.findViewById(R.id.tvSoldPercentDetailMonth);
				llGSTDetailMonth.setVisibility(View.VISIBLE);

				TextView tvTitleDate = (TextView) view.findViewById(R.id.tvTitleDate);
				tvTitleDate.setText(StringUtil.getString(R.string.TEXT_DATE_SALE_PRODUCT));

				// for GST view month detail
				TextView tvDateGSTMonthDetail = (TextView) view.findViewById(R.id.tvDate);
				String strDateNow = DateUtils.getDayOfWeek() + ", "
						+ DateUtils.getCurrentDate();
				tvDateGSTMonthDetail.setVisibility(View.VISIBLE);
				tvDateGSTMonthDetail.setText(strDateNow);

				// an EditText chon ngay
				edDate.setVisibility(View.GONE);
			}
		}

	}

	/**
	 * initHeaderText
	 * 
	 * @author: duongdt3
	 * @since: 19:28:14 5 Dec 2013
	 * @return: void
	 * @throws:
	 */
	private void initHeaderText() {
		String title = "";
		// thêm ngày tháng năm vào title
		String strBold = StringUtil.getBoldText(DateUtils.getCurrentDate());

		// Moi loai request co title header rieng
		switch (typeRequest) {
		case NVBHGeneralStatisticsView.TYPE_REQUEST_TNPG: {
			title = StringUtil.getString(R.string.TITLE_VIEW_REPORT_DATE_TNPG);
		}
			break;
		case NVBHGeneralStatisticsView.TYPE_REQUEST_GST_DATE_DETAIL: {

			title = StringUtil.getString(R.string.TITLE_VIEW_REPORT_DATE_TNPG_DETAIL_GST)
					+ " " + strBold;
		}
			break;
		case NVBHGeneralStatisticsView.TYPE_REQUEST_GST_MONTH_DETAIL: {
			title = StringUtil.getString(R.string.TITLE_VIEW_REPORT_MONTH_TNPG_DETAIL_GST)
					+ " " + strBold;
		}
			break;
		default:
			break;
		}

		Spanned titleSpan = StringUtil.getHTMLText(title);
		setTitleHeaderView(titleSpan);

	}

	/**
	 * initHeaderMenu
	 * 
	 * @author: duongdt3
	 * @since: 19:27:52 5 Dec 2013
	 * @return: void
	 * @throws:
	 */
	private void initHeaderMenu() {
		if (typeRequest == NVBHGeneralStatisticsView.TYPE_REQUEST_TNPG) {
			// ko co menu, vi chi co 1 man hinh
		} else if (typeRequest == NVBHGeneralStatisticsView.TYPE_REQUEST_GST_DATE_DETAIL) {
			enableMenuBar(this, FragmentMenuContanst.GST_REPORT_MENU_COMMANDs, FragmentMenuContanst.INDEX_MENU_GSBH_GENERAL_STATISTICS_DATE);
		} else if (typeRequest == NVBHGeneralStatisticsView.TYPE_REQUEST_GST_MONTH_DETAIL) {
			enableMenuBar(this, FragmentMenuContanst.GST_REPORT_MENU_COMMANDs, FragmentMenuContanst.INDEX_MENU_GSBH_GENERAL_STATISTICS_MONTH);
		}
	}

	/**
	 * request data on first time
	 * 
	 * @author: duongdt3
	 * @since: 19:28:02 5 Dec 2013
	 * @return: void
	 * @throws:
	 */
	private void initData() {
		isCodeSelectedSpniner = false;
		posStaffCurrent = -1;
		edDate.setText(DateUtils.getCurrentDate());
		listStaff = null;
//		requestData();
	}

	/**
	 * Get data from DB
	 * 
	 * @author: duongdt3
	 * @since: 19:27:34 5 Dec 2013
	 * @return: void
	 * @throws:
	 */
	private void requestData() {
		// show dialog loading...
		this.parent.showProgressDialog(StringUtil.getString(R.string.loading));

		// create request
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_STAFF_OWNER_ID, userId);
		data.putString(IntentConstants.INTENT_SHOP_ID, shopId);
		String date = edDate.getText().toString();
		String dateInNocontent = DateUtils.getCurrentDate();

		// nếu rỗng, mặc định là ngày hiện tại
		if (StringUtil.isNullOrEmpty(date)) {
			date = dateInNocontent.toString();
		} else {
			// no content text
			dateInNocontent = date.toString();
		}

		// doi format ngay, su dung trong SQLite
		// chuyển về dạng không có thời gian
		date = DateUtils.convertDateOneFromFormatToAnotherFormat(date, DateUtils.DATE_FORMAT_DEFAULT, DateUtils.DATE_FORMAT_SQL_DEFAULT);
		data.putString(IntentConstants.INTENT_DATE, date);

		// set no content text when request
		tbStatistics.showNoContentRow("Không có dữ liệu phát sinh sản lượng ngày "
				+ dateInNocontent);

		// neu da co list staff, ko load nua
		data.putBoolean(IntentConstants.INTENT_IS_LOAD_STAFF_LIST, (listStaff == null));

		// add staff_id = ID PG muốn lấy
		if (listStaff != null && listStaff.size() > 0) {
			int selectPosition = spinnerNVTT.getSelectedItemPosition();
			// co chon
			if (selectPosition != Spinner.INVALID_POSITION) {
				StaffItem staffInfo = listStaff.get(selectPosition);

				// chon staff thi them dk chon staff
				data.putString(IntentConstants.INTENT_STAFF_ID, staffInfo.id);
			}
		}

		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_GENERAL_STATISTICS_REPORT_INFO_FOR_TNPG;
		// send request
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * render progress info
	 * 
	 * @author: duongdt3
	 * @since: 15:11:33 12 Dec 2013
	 * @return: void
	 * @throws:
	 * @param info
	 */
	private void renderProgress(ProgressSoldViewDTO info) {
		if (info != null) {
			String strSoldPercent = StringUtil.parseProgressSold(info.progressSold);
			tvSoldPercent.setText(strSoldPercent);
		}
	}

	/**
	 * render customer info in header info panel
	 * 
	 * @author: duongdt3
	 * @since: 19:35:39 27 Nov 2013
	 * @return: void
	 * @throws:
	 * @param customerInfo
	 */
	void renderHeaderCustomerInfo(CustomerInfo customerInfo) {
		if (customerInfo != null) {
			tvCustomerName.setText(customerInfo.cusName);
			tvDiaChi.setText(customerInfo.address);
		}
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		// close Progress Dialog
		this.parent.closeProgressDialog();

		switch (modelEvent.getActionEvent().action) {
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		// close Progress Dialog
		this.parent.closeProgressDialog();
		// show Error Message
		// this.parent.showDialog(StringUtil.getString(R.string.MESSAGE_ERROR_HAPPEN));
		switch (modelEvent.getActionEvent().action) {
		default:
			super.handleErrorModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
		default:
			super.onEvent(eventType, control, data);
			break;
		}
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		// TODO Auto-generated method stub
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				// Re request data
				initData();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	/**
	 * Ham tra ve khi picker date
	 * 
	 * @author: duongdt3
	 * @param dayOfMonth
	 * @param monthOfYear
	 * @param year
	 * @return: void
	 * @throws:
	 */
	public void updateDate(int dayOfMonth, int monthOfYear, int year) {
		String sDay = DateUtils.getDateString(DateUtils.defaultDateFormat.toPattern(), dayOfMonth, monthOfYear, year);
		boolean isAfterNow = DateUtils.compareDateWithNow(dayOfMonth, monthOfYear, year) > 0;
		// Neu lon hon ngay Hien Tai, thong bao loi
		if (isAfterNow) {
			GlobalUtil.showDialogConfirm(this, parent, StringUtil.getString(R.string.TEXT_LABLE_TNPG_INFO_REPORT_DATE_ERROR), StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), 0, null, false);
		} else {
			edDate.setTextColor(ImageUtil.getColor(R.color.BLACK));
			// Month is 0 based so add 1
			edDate.setText(sDay);

			// request data for Date choose
			requestData();
		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v == edDate) {
			if (!edDate.onTouchEvent(event)) {
				parent.fragmentTag = PGGeneralStatisticView.TAG;
				parent.showDatePickerWithDate(GlobalBaseActivity.DATE_DIALOG_ID, edDate.getText().toString(), true);
			}
		}

		return false;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {

		// neu spiner NVTT co chon
		if (pos != posStaffCurrent) {

			if (isCodeSelectedSpniner) {
				// neu la code selected thi ko can request
				isCodeSelectedSpniner = false;
			} else {
				// neu la nguoi dung click chon thi se request
				requestData();
			}
			posStaffCurrent = pos;
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
}
