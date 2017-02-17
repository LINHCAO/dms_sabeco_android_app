/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.db.ShopParamDTO;
import com.viettel.dms.dto.db.StaffDTO;
import com.viettel.dms.dto.view.AttendanceDTO;
import com.viettel.dms.dto.view.GSNPPTakeAttendaceDTO;
import com.viettel.dms.dto.view.GsnppRouteSupervisionItem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.DMSTableView;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.SupervisorActivity;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * man hinh cham cong giam sat nha phan phoi
 * @author: DungNT19
 * @version: 1.0
 * @since: Jun 9, 2012
 */

public class SuperVisorTakeAttendanceView extends BaseFragment implements
		OnClickListener, OnEventControlListener, VinamilkTableListener {

	public static final String TAG = SuperVisorTakeAttendanceView.class.getName();

	private static final int MENU_STAFF_TAB = 0;
	private static final int MENU_STAFF_POSITION = 1;
	private static final int MENU_STAFF_TIMEKEEPING = 2;
	private static final int MENU_STAFF_GOING_ONLINE = 3;
	private static final int MENU_LIST_ORDER = 4;

	private DMSTableView tbListAttendace;
	private SupervisorActivity parent;
	private boolean checkSendRequest = false;
	GSNPPTakeAttendaceDTO attendanceDTO = new GSNPPTakeAttendaceDTO(); //dto view
	List<String> listTitle = new ArrayList<String>();

	/**
	 * method get instance
	 * @author: HaiTC3
	 * @since: 9:07:56 AM | Jun 9, 2012
	 * @return
	 * @return: FindProductAddOrderListView
	 * @throws:
	 */
	public static SuperVisorTakeAttendanceView getInstance() {
		SuperVisorTakeAttendanceView instance = new SuperVisorTakeAttendanceView();
		// Supply index input as an argument.
		Bundle args = new Bundle();
		instance.setArguments(args);

		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parent = (SupervisorActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.layout_supervisor_attendance_list, null);
		View view = super.onCreateView(inflater, v, savedInstanceState);
		SpannableObject obj = new SpannableObject();
		obj.addSpan(StringUtil.getString(R.string.TITLE_HEADER_TITLE_TAKE_ATTENDANCE) + " ");
		obj.addSpan(DateUtils.getCurrentDate(), ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.BOLD);
		
		setTitleHeaderView(obj.getSpan());

		tbListAttendace = (DMSTableView) view.findViewById(R.id.tbPromotionList);
		tbListAttendace.setListener(this);

		// menu bar
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_INVOICE_LIST), R.drawable.icon_order,
				MENU_LIST_ORDER);
		addMenuItem(StringUtil.getString(R.string.TEXT_STAFF_GOING_ONLINE), R.drawable.icon_task,MENU_STAFF_GOING_ONLINE);
		addMenuItem(StringUtil.getString(R.string.TEXT_STAFF_TIMEKEEPING), R.drawable.icon_clock, MENU_STAFF_TIMEKEEPING);
		addMenuItem(StringUtil.getString(R.string.TEXT_VIEW_POSITION), R.drawable.icon_map, MENU_STAFF_POSITION);
		addMenuItem(StringUtil.getString(R.string.TEXT_MONITORING), R.drawable.menu_customer_icon, MENU_STAFF_TAB);
		
		setMenuItemFocus(3);

		// request get list promotion
		if (!checkSendRequest) {
			checkSendRequest = true;
			getListSaleForAttendance();
		} else {
			renderLayout(attendanceDTO);
		}
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (checkSendRequest) {
		} else {
			// layout ds sp ban
			renderLayout(attendanceDTO);
			// layout ds sp khuyen mai
			checkSendRequest = true;
		}
	}

	/**
	 * Lay danh sach san pham chuong trinh khuyen mai
	 * 
	 * @author: SoaN
	 * @return: void
	 * @throws:
	 */
	private void getListSaleForAttendance() {
		int indexPage = 0;
		String page = " limit "
				+ (indexPage * Constants.NUM_ITEM_PER_PAGE)
				+ "," + Constants.NUM_ITEM_PER_PAGE;
		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_STAFF_ID, String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));
		data.putString(IntentConstants.INTENT_PAGE, page);
		data.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
		data.putString(IntentConstants.INTENT_PARENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopIdProfile);
		parent.showProgressDialog(StringUtil.getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GSNPP_GET_LIST_SALE_FOR_ATTENDANCE;
		SuperviorController.getInstance().handleViewEvent(e);
	}
	
	/**
	 * 
	*  render du lieu cho man hinh
	*  @author: ThanhNN8
	*  @param model
	*  @return: void
	*  @throws:
	 */
	private void renderLayout(GSNPPTakeAttendaceDTO model) {
		tbListAttendace.clearAllDataAndHeader();
		if (listTitle.size() >=4) {
			SuperVisorTakeAttendanceRow gsbhHeaderInfo = new SuperVisorTakeAttendanceRow(parent, this);
			gsbhHeaderInfo.renderHeader(listTitle.get(2),listTitle.get(3));
			initHeaderTable(tbListAttendace, gsbhHeaderInfo);
		}
		int pos = (tbListAttendace.getPagingControl().getCurrentPage() - 1) * Constants.NUM_ITEM_PER_PAGE + 1;
		if (model.listStaff.size() > 0) {
			for (AttendanceDTO dto: model.listStaff) {
				SuperVisorTakeAttendanceRow row = new SuperVisorTakeAttendanceRow(parent, this);
				row.setClickable(true);
				row.setOnClickListener(this);
				row.setTag(Integer.valueOf(pos));
				row.renderLayout(pos, dto, attendanceDTO.listInfo.get(0).code);
				row.setListener(this);
				pos++;
				tbListAttendace.addRow(row);
			}
		} else {
			tbListAttendace.showNoContentRow();
		}
	}


	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent event = modelEvent.getActionEvent();
		switch (event.action) {
		case ActionEventConstant.GSNPP_GET_LIST_SALE_FOR_ATTENDANCE: {
			tbListAttendace.getPagingControl().totalPage = -1;
			tbListAttendace.getPagingControl().setCurrentPage(1);
			attendanceDTO.listInfo.clear();
			attendanceDTO.listStaff.clear();

			GSNPPTakeAttendaceDTO tempDTO = (GSNPPTakeAttendaceDTO) modelEvent.getModelData();
			attendanceDTO.listInfo.addAll(tempDTO.listInfo);
			attendanceDTO.shopPosition = tempDTO.shopPosition;
			if(attendanceDTO.shopPosition.lat <= 0 || attendanceDTO.shopPosition.lng <= 0) {
				parent.showDialog(StringUtil.getString(R.string.TEXT_TAKE_ATTENDANCE_SHOP_HAVE_NO_POSITION));
			}
			
			//Check enough param
			boolean notEnoughParams = false;
			for(ShopParamDTO dto : attendanceDTO.listInfo) {
				if(StringUtil.isNullOrEmpty(dto.code)) {
					notEnoughParams = true;
					break;
				}
			}
			if(notEnoughParams) {
				renderLayout(attendanceDTO);
				GlobalUtil.showDialogConfirm(this, parent, StringUtil.getString(R.string.TEXT_ERROR_ATTENDANCE),
						StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), -1, null, false);
				break;
			} 

			// Kiem tra co khai bao du lieu cham cong khong
			if(attendanceDTO.listInfo.size() <= 0){
				renderLayout(attendanceDTO);
				GlobalUtil.showDialogConfirm(this, parent, StringUtil.getString(R.string.TEXT_ERROR_ATTENDANCE),
						StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), -1, null, false);
				break;
			}
			
			// List header
			listTitle.clear();
			listTitle.add(StringUtil.getString(R.string.TEXT_HEADER_TABLE_STAFF_CODE));
			listTitle.add(StringUtil.getString(R.string.TEXT_HEADER_TABLE_STAFF_NAME));
			listTitle
					.add(attendanceDTO.listInfo.get(0).name + " - " + attendanceDTO.listInfo.get(1).name);
			listTitle.add(StringUtil.getString(R.string.TEXT_ATTENDANCE_AFTER) + " "
					+ attendanceDTO.listInfo.get(1).name);
			listTitle.add("");

			for (AttendanceDTO attTemp : tempDTO.listStaff) {
				boolean notExist = true;
				for (AttendanceDTO attDTO : attendanceDTO.listStaff) {
					if (attTemp.staffId == attDTO.staffId) {
						notExist = false;

						if (!StringUtil.isNullOrEmpty(attTemp.time1)) {
							SimpleDateFormat formatter = new SimpleDateFormat(DateUtils.DATE_FORMAT_ATTENDANCE);
							Date createDate = new Date();
							Date startDate = new Date();
							Date endDate = new Date();

							String startTime = attTemp.time1.split(" ")[0] + " " + attendanceDTO.listInfo.get(0).code;
							String endTime = attTemp.time1.split(" ")[0] + " " + attendanceDTO.listInfo.get(1).code;

							try {
								createDate = formatter.parse(attTemp.time1);
								startDate = formatter.parse(startTime);
								endDate = formatter.parse(endTime);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
							}

							// Trong thoi gian cho phep
							if (createDate.compareTo(startDate) >= 0 && createDate.compareTo(endDate) <= 0) {
								//Neu cham cong chua hop le thi moi lay record nay
								if (StringUtil.isNullOrEmpty(attDTO.time1) || 
										(attDTO.onTime == false && attTemp.distance1 < attDTO.distance1)) {
									attDTO.distance1 = attTemp.distance1;
									attDTO.time1 = attTemp.time1;
									attDTO.position1.lat = attTemp.position1.lat;
									attDTO.position1.lng = attTemp.position1.lng;

									if (!StringUtil.isNullOrEmpty(attendanceDTO.listInfo.get(2).code) && attDTO.distance1 > Double.parseDouble(attendanceDTO.listInfo.get(2).code)) {//
										attDTO.onTime = false;
									} else {
										attDTO.onTime = true;
									}
								} else {

								}
								// Sau thoi gian cho phep & chua co log hop le
								// trong khoang thoi gian cho phep
							} else if (createDate.compareTo(endDate) > 0 && attDTO.onTime == false) {
								if (!StringUtil.isNullOrEmpty(attendanceDTO.listInfo.get(2).code) && attTemp.distance1 <= Double.parseDouble(attendanceDTO.listInfo.get(2).code)
										&& StringUtil.isNullOrEmpty(attDTO.time2)) {
									attDTO.time2 = attTemp.time1;
									attDTO.position2.lat = attTemp.position1.lat;
									attDTO.position2.lng = attTemp.position1.lng;
									attDTO.distance2 = attTemp.distance1;
								}
							} else {// Truoc thoi gian cho phep
								// attTemp.time2 = "";
								// attTemp.position2.lat = -1;
								// attTemp.position2.lng = -1;
								// attTemp.distance2 = -1;
							}
						}
					}
				}

				if (notExist) {
					if (!StringUtil.isNullOrEmpty(attTemp.time1) && attTemp.position1.lat > 0
							&& attTemp.position1.lng > 0) {
						SimpleDateFormat formatter = new SimpleDateFormat(DateUtils.DATE_FORMAT_ATTENDANCE);
						Date createDate = new Date();
						Date startDate = new Date();
						Date endDate = new Date();

						String startTime = attTemp.time1.split(" ")[0] + " " + attendanceDTO.listInfo.get(0).code;
						String endTime = attTemp.time1.split(" ")[0] + " " + attendanceDTO.listInfo.get(1).code;

						try {
							createDate = formatter.parse(attTemp.time1);
							startDate = formatter.parse(startTime);
							endDate = formatter.parse(endTime);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
						}

						// Trong thoi gian cho phep
						if (createDate.compareTo(startDate) >= 0 && createDate.compareTo(endDate) <= 0) {
							if (!StringUtil.isNullOrEmpty(attendanceDTO.listInfo.get(2).code) && attTemp.distance1 > Double.parseDouble(attendanceDTO.listInfo.get(2).code)) {//
								attTemp.onTime = false;
							} else {
								attTemp.onTime = true;
							}
							// Sau thoi gian cho phep
						} else if (createDate.compareTo(endDate) > 0) {
							if (!StringUtil.isNullOrEmpty(attendanceDTO.listInfo.get(2).code) && attTemp.distance1 <= Double.parseDouble(attendanceDTO.listInfo.get(2).code)) {
								attTemp.time2 = attTemp.time1;
								attTemp.position2.lat = attTemp.position1.lat;
								attTemp.position2.lng = attTemp.position1.lng;
								attTemp.distance2 = attTemp.distance1;
							} else {
								attTemp.time2 = "";
								attTemp.position2.lat = -1;
								attTemp.position2.lng = -1;
								attTemp.distance2 = -1;
							}
							attTemp.time1 = "";
							attTemp.position1.lat = -1;
							attTemp.position1.lng = -1;
							attTemp.distance1 = -1;
						} else {// Truoc thoi gian cho phep
							attTemp.time1 = "";
							attTemp.position1.lat = -1;
							attTemp.position1.lng = -1;
							attTemp.distance1 = -1;
						}
					} else {
						attTemp.time1 = "";
						attTemp.position1.lat = -1;
						attTemp.position1.lng = -1;
						attTemp.distance1 = -1;
					}
					attendanceDTO.listStaff.add(attTemp);
				}

			}
			renderLayout(attendanceDTO);
			requestInsertLogKPI(HashMapKPI.GSNPP_CHAMCONGNHANVIEN, modelEvent.getActionEvent().startTimeFromBoot);
			break;
		}
		default:
			break;
		}
		parent.closeProgressDialog();
		super.handleModelViewEvent(modelEvent);
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		ActionEvent event = modelEvent.getActionEvent();
		switch (event.action) {
		case ActionEventConstant.GSNPP_GET_LIST_SALE_FOR_ATTENDANCE: {
			if (event.tag == 11) {
				attendanceDTO = new GSNPPTakeAttendaceDTO();
				renderLayout(attendanceDTO);
			}
			break;
		}
		default:
			super.handleErrorModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		ActionEvent e = new ActionEvent();
		switch (eventType) {
		case MENU_STAFF_TAB: {
			e.action = ActionEventConstant.GET_GSNPP_ROUTE_SUPERVISION;
			e.sender = this;
			SuperviorController.getInstance().handleSwitchFragment(e);
			break;
		}
		case MENU_STAFF_POSITION: {
			Bundle b = new Bundle();
			e = new ActionEvent();
			e.action = ActionEventConstant.SUPERVISE_STAFF_POSITION;
			e.sender = this;
			e.viewData = b;
			SuperviorController.getInstance().handleSwitchFragment(e);
			break;
		}
		case MENU_LIST_ORDER:
			this.gotoListOrder();
			break;
		case MENU_STAFF_GOING_ONLINE:
			this.gotoReportVisitCustomerOnPlan();
			break;
		default:
			break;
		}
		super.onEvent(eventType, control, data);
	}

	/**
	 *
	 * go go report visit customer on plan view - update for CR0074
	 *
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 21, 2013
	 */
	public void gotoListOrder() {
		Bundle b = new Bundle();
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GO_TO_LIST_ORDER;
		e.sender = this;
		e.viewData = b;
		UserController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * 
	 * go go report visit customer on plan view - update for CR0074
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 21, 2013
	 */
	public void gotoReportVisitCustomerOnPlan() {
		Bundle b = new Bundle();
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GO_TO_REPORT_VISIT_CUSTOMER_ON_PLAN;
		e.sender = this;
		e.viewData = b;
		SuperviorController.getInstance().handleSwitchFragment(e);
	}
	

	@Override
	public void onClick(View v) {
		super.onClick(v);
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
//		if (control == tbListAttendace) {
			if (action == ActionEventConstant.GO_TO_MAP_FROM_REPORT_VISIT_CUSTOMER) {
				this.gotoMapOfNVBH((AttendanceDTO) data);
			}
//		}

	}
	
	/**
	 * 
	 * hien thi ban do di tuyen cua nhan vien ban hang duoc chon
	 * 
	 * @param data
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 22, 2013
	 */
	public void gotoMapOfNVBH(AttendanceDTO data) {
		GsnppRouteSupervisionItem dto = new GsnppRouteSupervisionItem();
		dto.aStaff = new StaffDTO();
		dto.aStaff.staffId = Long.valueOf(data.staffId);
		dto.aStaff.staffCode = data.staffCode;
		dto.aStaff.shopId = Integer.valueOf(GlobalInfo.getInstance().getProfile().getUserData().shopId);
		dto.aStaff.name = data.name;
		boolean hasPosition = false;
		if (data.time1 != null && !"".equals(data.time1)) {
			hasPosition = true;
			dto.lat = data.position1.lat;
			dto.lng = data.position1.lng;
		} else if (data.time2 != null && !"".equals(data.time2)) {
			hasPosition = true;
			dto.lat = data.position2.lat;
			dto.lng = data.position2.lng;
		}
		
		if (hasPosition) {
			Bundle b = new Bundle();
			b.putSerializable(IntentConstants.INTENT_STAFF_DTO, dto);
			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.SUPERVISE_STAFF_POSITION2;
			e.sender = this;
			e.viewData = b;
			SuperviorController.getInstance().handleSwitchFragment(e);
		}
	}
	
	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if(this.isVisible()){
				getListSaleForAttendance();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}
}
