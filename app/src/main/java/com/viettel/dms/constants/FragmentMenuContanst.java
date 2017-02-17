/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.constants;

import android.app.Activity;
import android.os.Bundle;

import com.viettel.dms.controller.PGController;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.sabeco.R;

/**
 * Fragment Menu Contanst
 * FragmentMenuContanst.java
 * @version: 1.0 
 * @since:  08:22:35 20 Jan 2014
 */
public class FragmentMenuContanst {
	// info of MenuItem
	public static class MenuItemInfo {
		public String text;
		public int iconId;
		public int action;

		public MenuItemInfo(int textId, int iconId, int action) {
			this.text = StringUtil.getString(textId);
			this.iconId = iconId;
			this.action = action;
		}

		public MenuItemInfo(String text, int iconId, int action) {
			this.text = text;
			this.iconId = iconId;
			this.action = action;
		}
	}

	public static abstract class MenuCommand {
		Bundle data = null;
		BaseFragment sender;

		public void setDataInfo(Bundle data) {
			this.data = data;
		}

		public Bundle getDataInfo() {
			return this.data;
		}

		public abstract MenuItemInfo[] getListMenuInfo();

		abstract void doAction(ActionEvent e);

		public boolean onActionMenuEvent(BaseFragment sender, int actionMenu,
				int indexMenuFocus) {
			boolean result = false;
			this.sender = sender;
			// kiểm tra có action này trong mảng hay không
			for (int i = 0, length = getListMenuInfo().length; i < length; i++) {
				if (getListMenuInfo()[i].action == actionMenu) {
					// kiểm tra có là menu hiện hay không
					indexMenuFocus = indexMenuFocus - 1;
					if (i != indexMenuFocus) {
						// khác index menu hiện tại
						result = true;
					}
					// nếu là index menu hiện tại, không xử lý tiếp
					break;
				}
			}

			// nếu có tồn tại trong mảng action, thì tiếp tục xử lý
			if (result) {
				ActionEvent e = new ActionEvent();
				Bundle b = (this.data  == null ? new Bundle() : this.data);
				e.viewData = b;
				e.sender = sender;
				e.action = actionMenu;
				// do action, cho từng loại màn hình quy định riêng
				doAction(e);
			}

			return result;
		}
	}

	//BEGIN REPORT MENU FOR SALE
	private static final MenuItemInfo[] NVBH_REPORT_MENUs = {
			new MenuItemInfo(StringUtil.getString(R.string.TEXT_HEADER_MENU_NEED_DONE),R.drawable.icon_order,ActionEventConstant.GO_TO_NVBH_NEED_DONE_VIEW),
			new MenuItemInfo(StringUtil.getString(R.string.TEXT_HEADER_MENU_PSDS),R.drawable.icon_list_star,ActionEventConstant.ACTION_REPORT_CUSTOMER_NOT_PSDS_IN_MONTH_SALE),
			new MenuItemInfo(StringUtil.getString(R.string.TEXT_HEADER_MENU_GENERAL_STATISTICS),R.drawable.icon_reminders,ActionEventConstant.GO_TO_GENERAL_STATISTICS) };
	
	//menu index
	public static final int INDEX_MENU_NVBH_NEED_DONE_VIEW = 1;
	public static final int INDEX_MENU_NVBH_REPORT_CUSTOMER_NOT_PSDS = 2;
	public static final int INDEX_MENU_NVBH_GENERAL_STATISTICS = 3;
	/**
	 * Menu command for GSMT_REPORT_MENUs group
	 */
	public static final MenuCommand NVBH_REPORT_MENU_COMMANDs = new MenuCommand() {
		@Override
		public MenuItemInfo[] getListMenuInfo() {
			return NVBH_REPORT_MENUs;
		}

		@Override
		void doAction(ActionEvent e) {
			SaleController.getInstance().handleSwitchFragment(e);
		}
	};
	//END REPORT MENU FOR SALE
	
	//BEGIN REPORT MENU FOR SUPERVISOR
	private static final MenuItemInfo[] GSBH_REPORT_MENUs = {
		 new MenuItemInfo(StringUtil.getString(R.string.TEXT_HEADER_MENU_PSDS),R.drawable.icon_list_star,ActionEventConstant.ACTION_REPORT_CUSTOMER_NOT_PSDS_IN_MONTH_SALE)
		,new MenuItemInfo(StringUtil.getString(R.string.TEXT_HEADER_MENU_REPORT_DATE),R.drawable.icon_reminders,ActionEventConstant.GO_TO_GENERAL_STATISTICS) };
	
	//menu index
	public static final int INDEX_MENU_GSNPP_REPORT_CUSTOMER_NOT_PSDS = 1;
	public static final int INDEX_MENU_GSNPP_GENERAL_STATISTICS = 2;
	
	/**
	 * Menu command for GSMT_REPORT_MENUs group
	 */
	public static final MenuCommand GSBH_REPORT_MENU_COMMANDs = new MenuCommand() {

		@Override
		public MenuItemInfo[] getListMenuInfo() {
			return GSBH_REPORT_MENUs;
		}

		@Override
		void doAction(ActionEvent e) {
			SuperviorController.getInstance().handleSwitchFragment(e);
		}
	};
	//END REPORT MENU FOR SUPERVISOR
		
	//BEGIN REPORT MENU FOR GST
	private static final MenuItemInfo[] GST_REPORT_MENUs = {
		 new MenuItemInfo(StringUtil.getString(R.string.TEXT_HEADER_MENU_PSDS),R.drawable.icon_list_star,ActionEventConstant.ACTION_REPORT_CUSTOMER_NOT_PSDS_IN_MONTH_SALE)
		//,new MenuItemInfo(StringUtil.getString(R.string.TEXT_HEADER_MENU_CTHTBH),R.drawable.icon_gift,ActionEventConstant.GO_TO_CTHTBH)
		,new MenuItemInfo(StringUtil.getString(R.string.TEXT_HEADER_MENU_REPORT_MONTH),R.drawable.icon_calendar,ActionEventConstant.GO_TO_GENERAL_STATISTICS_MONTH)
		,new MenuItemInfo(StringUtil.getString(R.string.TEXT_HEADER_MENU_REPORT_DATE),R.drawable.icon_reminders,ActionEventConstant.GO_TO_GENERAL_STATISTICS_DATE) 
	};
	
	//menu index
	public static final int INDEX_MENU_GSBH_REPORT_CUSTOMER_NOT_PSDS = 1;
	public static final int INDEX_MENU_GSBH_GENERAL_STATISTICS_MONTH = 2;
	public static final int INDEX_MENU_GSBH_GENERAL_STATISTICS_DATE = 3;
	
	/**
	 * Menu command for GSMT_REPORT_MENUs group
	 */
	public static final MenuCommand GST_REPORT_MENU_COMMANDs = new MenuCommand() {

		@Override
		public MenuItemInfo[] getListMenuInfo() {
			return GST_REPORT_MENUs;
		}

		@Override
		void doAction(ActionEvent e) {
			TBHVController.getInstance().handleSwitchFragment(e);
		}
	};
	//END REPORT MENU FOR GST

	//BEGIN REPORT MENU FOR NVBH CUSTOMER 
	private static final MenuItemInfo[] NVBH_CUSTOMER_MENUs = {
			new MenuItemInfo(StringUtil.getString(R.string.TEXT_NEW_CUS_LIST), R.drawable.icon_add_user, ActionEventConstant.GET_NEW_CUSTOMER_LIST),
			new MenuItemInfo(StringUtil.getString(R.string.TEXT_INVOICE_LIST), R.drawable.icon_order, ActionEventConstant.GO_TO_LIST_ORDER),
			new MenuItemInfo(StringUtil.getString(R.string.TEXT_ROUTE), R.drawable.icon_map, ActionEventConstant.GO_TO_CUSTOMER_ROUTE),
			new MenuItemInfo(StringUtil.getString(R.string.TEXT_CUS_LIST), R.drawable.menu_customer_icon, ActionEventConstant.GET_CUSTOMER_LIST) 
	};
	
	public static final int INDEX_MENU_NVBH_NEW_CUSTOMER_LIST = 1;
	public static final int INDEX_MENU_NVBH_LIST_ORDER = 2;
	public static final int INDEX_MENU_NVBH_CUSTOMER_ROUTE = 3;
	public static final int INDEX_MENU_NVBH_CUSTOMER_LIST = 4;

	public static MenuCommand NVBH_CUSTOMER_MENU_COMMANDs = new MenuCommand() {
		
		@Override
		public MenuItemInfo[] getListMenuInfo() {
			return NVBH_CUSTOMER_MENUs;
		}
		
		@Override
		void doAction(ActionEvent e) {
			if (e.action == ActionEventConstant.GET_CUSTOMER_LIST) {
				SaleController.getInstance().handleSwitchFragment(e);
			}else{
				UserController.getInstance().handleSwitchFragment(e);
			}
		}
	};
	//END REPORT MENU FOR NVBH CUSTOMER 

	//BEGIN REPORT MENU FOR GSNPP STAFF
	private static final MenuItemInfo[] GSNPP_STAFF_MENUs = {
			new MenuItemInfo(StringUtil.getString(R.string.TEXT_INVOICE_LIST), R.drawable.icon_order, ActionEventConstant.GO_TO_LIST_ORDER),
			new MenuItemInfo(StringUtil.getString(R.string.TEXT_STAFF_GOING_ONLINE), R.drawable.icon_task, ActionEventConstant.GO_TO_REPORT_VISIT_CUSTOMER_ON_PLAN),
			new MenuItemInfo(StringUtil.getString(R.string.TEXT_STAFF_TIMEKEEPING), R.drawable.icon_clock, ActionEventConstant.GSNPP_GET_LIST_SALE_FOR_ATTENDANCE),
			new MenuItemInfo(StringUtil.getString(R.string.TEXT_VIEW_POSITION), R.drawable.icon_map, ActionEventConstant.SUPERVISE_STAFF_POSITION),
			new MenuItemInfo(StringUtil.getString(R.string.TEXT_MONITORING), R.drawable.menu_customer_icon, ActionEventConstant.GET_GSNPP_ROUTE_SUPERVISION)
	};

	public static final int INDEX_MENU_GSNPP_INVOICE_LIST = 1;
	public static final int INDEX_MENU_GSNPP_STAFF_GOING_ONLINE = 2;
	public static final int INDEX_MENU_GSNPP_STAFF_TIMEKEEPING = 3;
	public static final int INDEX_MENU_GSNPP_VIEW_POSITION = 4;
	public static final int INDEX_MENU_GSNPP_MONITORING = 5;

	public static MenuCommand GSNPP_STAFF_MENU_COMMANDs = new MenuCommand() {

		@Override
		public MenuItemInfo[] getListMenuInfo() {
			return GSNPP_STAFF_MENUs;
		}

		@Override
		void doAction(ActionEvent e) {
			if (e.action == ActionEventConstant.GO_TO_LIST_ORDER) {
				UserController.getInstance().handleSwitchFragment(e);
			}else{
				SuperviorController.getInstance().handleSwitchFragment(e);
			}
		}
	};
	//END REPORT MENU FOR GSNPP STAFF

	//BEGIN REPORT MENU FOR GSBH STAFF
	private static final MenuItemInfo[] GSBH_STAFF_MENUs = {
			new MenuItemInfo(StringUtil.getString(R.string.TEXT_INVOICE_LIST), R.drawable.icon_order, ActionEventConstant.GO_TO_LIST_ORDER),
			new MenuItemInfo(StringUtil.getString(R.string.TEXT_STAFF_GOING_ONLINE), R.drawable.icon_task, ActionEventConstant.TBHV_VISIT_CUS_NOTIFICATION),
			new MenuItemInfo(StringUtil.getString(R.string.TEXT_STAFF_TIMEKEEPING), R.drawable.icon_clock, ActionEventConstant.TBHV_ATTENDANCE),
			new MenuItemInfo(StringUtil.getString(R.string.TEXT_VIEW_POSITION), R.drawable.icon_map, ActionEventConstant.TBHV_SUPERVISE_GSNPP_POSITION)
	};

	public static final int INDEX_MENU_GSBH_INVOICE_LIST = 1;
	public static final int INDEX_MENU_GSBH_STAFF_GOING_ONLINE = 2;
	public static final int INDEX_MENU_GSBH_STAFF_TIMEKEEPING = 3;
	public static final int INDEX_MENU_GSBH_VIEW_POSITION = 4;

	public static MenuCommand GSBH_STAFF_MENU_COMMANDs = new MenuCommand() {

		@Override
		public MenuItemInfo[] getListMenuInfo() {
			return GSBH_STAFF_MENUs;
		}

		@Override
		void doAction(ActionEvent e) {
			if (e.action == ActionEventConstant.GO_TO_LIST_ORDER) {
				UserController.getInstance().handleSwitchFragment(e);
			}else{
				TBHVController.getInstance().handleSwitchFragment(e);
			}
		}
	};
	//END REPORT MENU FOR GSBH STAFF

	//BEGIN REPORT MENU FOR NVBH CUSTOMER 
		private static final MenuItemInfo[] NVBH_PROGRAM_MENUs = {
				new MenuItemInfo(StringUtil.getString(R.string.TEXT_ATTEND), R.drawable.icon_add_user, ActionEventConstant.GO_TO_CUSTOMER_ATTEND_PROGRAM),
				new MenuItemInfo(StringUtil.getString(R.string.TEXT_INFORMATION), R.drawable.icon_detail, ActionEventConstant.GO_TO_PROMOTION_PROGRAM_DETAIL) 
		}; 
		
		public static final int INDEX_MENU_NVBH_PROGRAM_DONE = 1;
		public static final int INDEX_MENU_NVBH_CUSTOMER_ATTEND_PROGRAM = 2;
		public static final int INDEX_MENU_NVBH_PROGRAM_INFO = 3;

		public static MenuCommand NVBH_PROGRAM_MENU_COMMANDs = new MenuCommand() {
			
			@Override
			public MenuItemInfo[] getListMenuInfo() {
				return NVBH_PROGRAM_MENUs;
			}
			
			@Override
			void doAction(ActionEvent e) {
				GlobalUtil.popBackStack((Activity)GlobalInfo.getInstance().getActivityContext());
				SaleController.getInstance().handleSwitchFragment(e);
			}
		};
		//END REPORT MENU FOR NVBH CUSTOMER
		
		private static final MenuItemInfo[] NVBH_PROGRAM_MENU_HAVE_DONE = {
			new MenuItemInfo(StringUtil.getString(R.string.TEXT_DONE), R.drawable.icon_task, ActionEventConstant.GO_TO_PROMOTION_PROGRAM_DONE), 
			new MenuItemInfo(StringUtil.getString(R.string.TEXT_ATTEND), R.drawable.icon_add_user, ActionEventConstant.GO_TO_CUSTOMER_ATTEND_PROGRAM),
			new MenuItemInfo(StringUtil.getString(R.string.TEXT_INFORMATION), R.drawable.icon_detail, ActionEventConstant.GO_TO_PROMOTION_PROGRAM_DETAIL)
		}; 
		public static MenuCommand NVBH_PROGRAM_MENU_COMMAND_HAVE_DONE = new MenuCommand() {
			
			@Override
			public MenuItemInfo[] getListMenuInfo() {
				return NVBH_PROGRAM_MENU_HAVE_DONE;
			}
			
			@Override
			void doAction(ActionEvent e) {
				GlobalUtil.popBackStack((Activity)GlobalInfo.getInstance().getActivityContext());
				SaleController.getInstance().handleSwitchFragment(e);
			}
		};

	//BEGIN MENU PG
	private static final MenuItemInfo[] PG_MENUs = {
			new MenuItemInfo(StringUtil.getString(R.string.TEXT_MENU_SALES), R.drawable.menu_customer_icon, ActionEventConstant.GO_TO_PG_ORDER_VIEW),
			new MenuItemInfo(StringUtil.getString(R.string.TEXT_HEADER_MENU_GENERAL_STATISTICS), R.drawable.icon_calendar, ActionEventConstant.GO_TO_PG_REPORT_ORDER_VIEW)
	};

	public static final int INDEX_MENU_PG_ORDER = 1;
	public static final int INDEX_MENU_PG_REPORT = 2;
	public static MenuCommand PG_MENU_COMMAND = new MenuCommand() {
		@Override
		public MenuItemInfo[] getListMenuInfo() {
			return PG_MENUs;
		}
		@Override
		void doAction(ActionEvent e) {
			GlobalUtil.popBackStack((Activity)GlobalInfo.getInstance().getActivityContext());
			PGController.getInstance().handleSwitchFragment(e);
		}
	};
	//END MENU PG
}
