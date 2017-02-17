/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tnpg.order;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.TNPGController;
import com.viettel.dms.dto.db.ActionLogDTO;
import com.viettel.dms.dto.view.CustomerListItem;
import com.viettel.dms.dto.view.PGTakeAttendanceDTO;
import com.viettel.dms.dto.view.PGTakeAttendanceViewDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

import java.util.ArrayList;

/**
 * MH Cham cong cua to truong tiep thi
 * 
 * @author: Nguyen Thanh Dung
 * @version: 1.0
 * @since: Nov 23, 2013
 */

public class TNPGTakeAttendanceView extends BaseFragment {
	public static final String TAG = TNPGTakeAttendanceView.class.getName();
	private static final int ACTION_SAVE_TAKE_ATTENDANCE_OK = 0;
	private static final int ACTION_CANCEL_SAVE_TAKE_ATTENDANCE = 1;
	
	public static final int ACTION_AGRRE_BACK = 2;
	public static final int ACTION_CANCEL_BACK = 3;
	public static final int ACTION_SAVE = 4;
	public static final int ACTION_NOTIFY_TAKE_ATTENDANCE_OK = 5;
	public static final int ACTION_IGNORE = 6;

	private PGTakeAttendanceAdapter thumbs = null;
	PGTakeAttendanceViewDTO takeAttendanceDto = new PGTakeAttendanceViewDTO();

	private GlobalBaseActivity parent; // parent
	private GridView gvImageView;
	//private Button btSave;
	//private int totalImage;
	boolean isFirstInit;
	private String customerId;
	private CustomerListItem customerListItem;
	//private double distance;
	private long staffID;
	private String startTime;
	
	/**
	 * Mo ta chuc nang cua ham
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param data
	 * @return
	 * @return: ListAlbumUserView
	 * @throws:
	 */

	public static TNPGTakeAttendanceView getInstance(Bundle data) {
		TNPGTakeAttendanceView instance = new TNPGTakeAttendanceView();
		instance.setArguments(data);
		return instance;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			parent = (GlobalBaseActivity) activity;
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_tnpg_take_attendance_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_HEADER_TAKE_ATTENDANCE_PG));
		if (getArguments().getSerializable(IntentConstants.INTENT_CUSTOMER_LIST_ITEM) != null) {
			customerListItem = (CustomerListItem) getArguments().getSerializable(IntentConstants.INTENT_CUSTOMER_LIST_ITEM);
		}
		if (getArguments().getLong(IntentConstants.INTENT_STAFF_ID)>0) {
			staffID = getArguments().getLong(IntentConstants.INTENT_STAFF_ID);
		}
//		if (getArguments().getDouble(IntentConstants.INTENT_DISTANCE)>0) {
//			distance = getArguments().getDouble(IntentConstants.INTENT_DISTANCE);
//		}

		customerId = customerListItem.aCustomer.getCustomerId();

		// Lay doi tuong
		gvImageView = (GridView) view.findViewById(R.id.gvImageView);

		//btSave = (Button) view.findViewById(R.id.btSave);
		//btSave.setOnClickListener(this);
		if (isFirstInit) {
			gvImageView.setAdapter(thumbs);
			thumbs.notifyDataSetChanged();
		} else {
			isFirstInit = true;
			getListPG();
			startTime = DateUtils.now();
		}
		
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_SAVE_ATTENDANCE), R.drawable.icon_save, ACTION_SAVE);
		addMenuItem(StringUtil.getString(R.string.TEXT_IGNORE), R.drawable.icon_ignore, ACTION_IGNORE);
		
		return v;
	}

	/**
	 * get List Album Of User
	 * @author: PhucNT - thanhnn
	 * @return: void
	 * @throws:
	 */
	private void getListPG() {
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_STAFF_ID, String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, customerId);
		bundle.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
		

		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GET_LIST_PG_FOR_TAKE_ATTENDANCE;
		e.sender = this;
		e.viewData = bundle;
		TNPGController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		switch (modelEvent.getActionEvent().action) {
		case ActionEventConstant.GET_LIST_PG_FOR_TAKE_ATTENDANCE: {
			takeAttendanceDto = (PGTakeAttendanceViewDTO) modelEvent.getModelData();
			try {
				thumbs = new PGTakeAttendanceAdapter(parent,takeAttendanceDto.listPG);
				gvImageView.setAdapter(thumbs);
				thumbs.notifyDataSetChanged();
			} catch (Exception e1) {
			}
			
			break;
		}
		case ActionEventConstant.SAVE_PG_TAKE_ATTENDANCE: {
			insertActionLog();
			parent.removeMenuCloseCustomer();
			GlobalUtil.showDialogConfirm(this, parent, StringUtil.getString(R.string.SAVE_PG_TAKE_ATTENDANCE_SUCCESS), StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), ACTION_NOTIFY_TAKE_ATTENDANCE_OK, null, false);
			break;
		}
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
		// TODO Auto-generated method stub

	}
	
	/**
	 * Insert action log sau khi kiem ton
	 * @author: dungnt19
	 * @since: 19:53:23 12-12-2013
	 * @return: void
	 * @throws:
	 */
	private void insertActionLog() {
		try {
			ActionLogDTO action = new ActionLogDTO();
			action.staffId = GlobalInfo.getInstance().getProfile()
					.getUserData().id;
			action.lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo()
					.getLatitude();
			action.lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo()
					.getLongtitude();
			action.aCustomer.customerId = Long.parseLong(customerId);
			action.objectId = customerId;
			action.objectType = ActionLogDTO.TYPE_PG_TAKE_ATTENDANCE;
			action.startTime = this.startTime;
			action.isOr = customerListItem.isOr;
			action.endTime = DateUtils.now();
			parent.requestInsertActionLog(action);
		} catch (NumberFormatException e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
	}
	
	/**
	* Chuyen sang man hinh kiem ton doi thu
	* @author: dungdq3
	* @return: void
	*/
	private void goToCheckRemainComptitor() {
		// TODO Auto-generated method stub
		if(customerListItem.isTodayCheckedRemainCompetitor>0 && customerListItem.isTodayCheckedSaleCompetitor>0){
			parent.showDialog(StringUtil.getString(R.string.FINISH_ALL_ACTION));
		}else{
			ActionEvent e = new ActionEvent();
			e.sender = this;
			Bundle bundle = new Bundle();
			bundle.putLong(IntentConstants.INTENT_STAFF_ID, staffID);
			//bundle.putDouble(IntentConstants.INTENT_DISTANCE, distance);
			bundle.putSerializable(IntentConstants.INTENT_CUSTOMER_LIST_ITEM, customerListItem);
			if(customerListItem.isTodayCheckedRemainCompetitor<1){
				bundle.putInt(IntentConstants.INTENT_TYPE, 0);
			}else{
				bundle.putInt(IntentConstants.INTENT_TYPE, 1);
			}
			//cusDto.distance
			e.viewData = bundle;
			e.action = ActionEventConstant.GO_TO_CHECK_REMAIN_COMPETITOR;
			TNPGController.getInstance().handleSwitchFragment(e);
		}
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		//setMenuItemFocus(eventType);
		switch (eventType) {
		case ACTION_SAVE_TAKE_ATTENDANCE_OK: {
			Bundle bundle = new Bundle();
			bundle.putSerializable(IntentConstants.INTENT_TTTT_TAKE_ATTENDANCE_DTO, takeAttendanceDto);
			bundle.putString(IntentConstants.INTENT_STAFF_ID, String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));
			bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, customerId);

			ActionEvent e = new ActionEvent();
			e.sender = this;
			e.viewData = bundle;
			e.action = ActionEventConstant.SAVE_PG_TAKE_ATTENDANCE;

			TNPGController.getInstance().handleViewEvent(e);
			break;
		}
		case ACTION_CANCEL_SAVE_TAKE_ATTENDANCE: {
			break;
		}
		case ACTION_AGRRE_BACK:
			GlobalUtil.popBackStack(getActivity());
			break;
		case ACTION_SAVE:
			GlobalUtil.showDialogConfirm(this, parent,
					StringUtil.getString(R.string.TEXT_CONFIRM_SAVE_TAKE_ATTENDANCE),
					StringUtil.getString(R.string.TEXT_AGREE), ACTION_SAVE_TAKE_ATTENDANCE_OK,
					StringUtil.getString(R.string.TEXT_DENY), ACTION_CANCEL_SAVE_TAKE_ATTENDANCE, null);
			break;
		case ACTION_NOTIFY_TAKE_ATTENDANCE_OK:
		case ACTION_IGNORE: {
			goToCheckRemainComptitor();
			break;
		}
		default:
			break;
		}
	}
	

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		super.handleErrorModelViewEvent(modelEvent);
	}

	public class OnWorkCheckChange implements OnCheckedChangeListener {
		private ViewHolder holder;

		public OnWorkCheckChange(ViewHolder _holder) {
			this.holder = _holder;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (buttonView == holder.cbOnWork) {
				if (!holder.onUpdateCheck) {
					holder.data.isOnWork = isChecked;
					holder.updateChangeOnWork();
				} else {
					holder.onUpdateCheck = false;
				}
			} else if (buttonView == holder.cbDress) {
				holder.data.isDress = isChecked;
			} else if (buttonView == holder.cbFollowRule) {
				holder.data.isFollowRule = isChecked;
			}
		}
	}

	public class TimeOnWorkTimeChange implements OnTimeChangedListener {
		private ViewHolder holder;

		public TimeOnWorkTimeChange(ViewHolder _holder) {
			this.holder = _holder;
		}

		@Override
		public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
			if (!holder.onUpdateTime) {
				holder.data.hourOfDay = hourOfDay;
				holder.data.minute = minute;
				holder.data.timeOnWork = DateUtils.getDateString(
						DateUtils.DATE_FORMAT_NOW, hourOfDay, minute);
			} else {
				holder.onUpdateTime = false;
			}
		}

	}

	/**
	 * Addapter grid view pg TNPGTakeAttendanceView.java
	 * 
	 * @author: dungnt19
	 * @version: 1.0
	 * @since: 19:49:04 12-12-2013
	 */
	public class PGTakeAttendanceAdapter extends BaseAdapter {

		private ArrayList<PGTakeAttendanceDTO> list = new ArrayList<PGTakeAttendanceDTO>();

		public PGTakeAttendanceAdapter(Context c,
				ArrayList<PGTakeAttendanceDTO> list) {
			this.list = list;
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
			ViewHolder holder = null;

			if (convertView == null) {
				LayoutInflater layout = (LayoutInflater) ((GlobalBaseActivity) parent)
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = layout.inflate(R.layout.layout_tnpg_take_attendance_item,
						null);
				holder = new ViewHolder();
				holder.tvStaffCode = (TextView) row
						.findViewById(R.id.tvStaffCode);
				holder.tvStaffName = (TextView) row
						.findViewById(R.id.tvStaffName);
				holder.cbOnWork = (CheckBox) row.findViewById(R.id.cbOnWork);
				holder.cbDress = (CheckBox) row.findViewById(R.id.cbDress);
				holder.cbFollowRule = (CheckBox) row
						.findViewById(R.id.cbFollowRule);
				holder.tpTimeOnWork = (TimePicker) row
						.findViewById(R.id.tpTimeOnWork);
				holder.cbOnWork
						.setOnCheckedChangeListener(new OnWorkCheckChange(
								holder));
				holder.cbDress
						.setOnCheckedChangeListener(new OnWorkCheckChange(
								holder));
				holder.cbFollowRule
						.setOnCheckedChangeListener(new OnWorkCheckChange(
								holder));
				holder.tpTimeOnWork
						.setOnTimeChangedListener(new TimeOnWorkTimeChange(
								holder));

				row.setTag(holder);
			} else {
				holder = (ViewHolder) row.getTag();
			}

			holder.data = list.get(position);
			holder.updateLayout();

			return row;
		}

	}

	public static class ViewHolder {
		public TextView tvStaffCode;
		public TextView tvStaffName;
		public CheckBox cbOnWork;
		public CheckBox cbDress;
		public CheckBox cbFollowRule;
		public TimePicker tpTimeOnWork;
		public boolean onUpdateTime;
		public boolean onUpdateCheck;

		public PGTakeAttendanceDTO data;

		public void updateLayout() {
			tvStaffCode.setText(data.staffCode);
			tvStaffName.setText(data.staffName);
			onUpdateTime = true;
			cbOnWork.setChecked(data.isOnWork);
			updateChangeOnWork();
		}

		private void updateChangeOnWork() {
			if (data.isOnWork) {
				cbDress.setEnabled(data.isOnWork);
				cbFollowRule.setEnabled(data.isOnWork);
			} else {
				data.isDress = data.isOnWork;
				data.isFollowRule = data.isOnWork;
				cbDress.setEnabled(data.isOnWork);
				cbFollowRule.setEnabled(data.isOnWork);
			}

			cbDress.setChecked(data.isDress);
			cbFollowRule.setChecked(data.isFollowRule);

			onUpdateTime = true;
			tpTimeOnWork.setCurrentHour(data.hourOfDay);
			onUpdateTime = true;
			tpTimeOnWork.setCurrentMinute(data.minute);
		}

	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW: {
			if (this.isVisible()) {
				// cau request du lieu man hinh
				getListPG();
			}
			break;
		}
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	/**
	 * Bắt sự kiện khi nhấn back
	 * @author: dungdq3
	 * @return: void
	 */
	public int onBackPressed() {

		int handleBack = 0;
		String titleDialog=StringUtil.getString(R.string.TEXT_CONFIRM_BACK_FIRST_LAYOUT)+" "+StringUtil.getString(R.string.TEXT_TAKE_ATTENDANCE)+"?";
		GlobalUtil.showDialogConfirm(this, parent,titleDialog,
				StringUtil.getString(R.string.TEXT_AGREE), ACTION_AGRRE_BACK,
				StringUtil.getString(R.string.TEXT_DENY), ACTION_CANCEL_BACK,
				null);
		return handleBack;
	}
}
