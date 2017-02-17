/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TableRow;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.constants.TableDefineContanst;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.dto.db.DisplayProgrameDTO;
import com.viettel.dms.dto.view.ComboboxDisplayProgrameDTO;
import com.viettel.dms.dto.view.DisplayProgrameItemDTO;
import com.viettel.dms.dto.view.DisplayProgrameModel;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.SupervisorActivity;
import com.viettel.sabeco.R;

/**
 * Chuong trinh trung bay
 * 
 * @author: SoaN
 * @version: 1.0
 * @since: Jun 9, 2012
 */

public class SuperVisorDisplayProgramView extends BaseFragment implements
		OnClickListener, OnEventControlListener, OnItemSelectedListener,
		VinamilkTableListener {

	public static final String TAG = SuperVisorDisplayProgramView.class.getName();
	
	public static final int ACTION_PROMOTION = 0;
	public static final int ACTION_DISPLAY = 1;
	public static final int ACTION_PRODUCT_LIST = 2;

	private VinamilkTableView tbPromotionList;
	
	private Spinner spinnerSalePath;//nganh hang
	private boolean isFirstTime;//kiem tra lan dau tien vao man hinh
	private List<DisplayProgrameItemDTO> listDepart = null;
	
	private ComboboxDisplayProgrameDTO comboboxData;
	private DisplayProgrameModel displayData;
	
	private boolean checkSendRequest = true;

	private SupervisorActivity parent;
	
	private int comboboxdepartselected = 0;
	private int totalPage; //bien tong so record
	private int currentPage = 1; //page hien tai

	/**
	 * 
	 * method get instance
	 * 
	 * @author: HaiTC3
	 * @since: 9:07:56 AM | Jun 9, 2012
	 * @param index
	 * @return
	 * @return: FindProductAddOrderListView
	 * @throws:
	 */
	public static SuperVisorDisplayProgramView getInstance(int index) {
//		if (instance == null) {
			SuperVisorDisplayProgramView instance = new SuperVisorDisplayProgramView();
			// Supply index input as an argument.
			Bundle args = new Bundle();
			args.putInt("index", index);
			instance.setArguments(args);
//		}

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
		isFirstTime = true;
		comboboxdepartselected = 0;
		ViewGroup v = (ViewGroup) inflater.inflate(
				R.layout.layout_supervisor_display_promotion_list, null);
		View view = super.onCreateView(inflater, v, savedInstanceState);
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_SUPERVISOR_LIST_DISPLAY_PROGRAME));

		tbPromotionList = (VinamilkTableView) view
				.findViewById(R.id.tbPromotionList);
		tbPromotionList.setListener(this);
		
		this.spinnerSalePath = (Spinner) v.findViewById(R.id.spinnerSalePath);
		spinnerSalePath.setOnItemSelectedListener(this);
		
		// renderLayout();
		// tbPromotionList.getHeaderView().addColumns(TableDefineContanst.PROMOTION_TABLE_WIDTHS,
		// TableDefineContanst.PROMOTION_TABLE_TITLES);
		// menu bar
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_PRODUCT),R.drawable.icon_product_list, ACTION_PRODUCT_LIST);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_CTTB),R.drawable.menu_manage_icon, ACTION_DISPLAY);
		addMenuItem(StringUtil.getString(R.string.TEXT_CTKM),R.drawable.menu_promotion_icon, ACTION_PROMOTION);
		setMenuItemFocus(2);
		
		// request get danh sach loai CT va danh sach nganh hang
		if (checkSendRequest) {
//			getListCombobox();
		// request get list display programe
			searchDisplayProgram(true, true);
		}
		return view;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (checkSendRequest) {
		} else {
			//cap nhat lai table
			tbPromotionList.setTotalSize(totalPage);
			tbPromotionList.getPagingControl().setCurrentPage(currentPage);
			// layout ds sp ban
			updateData(comboboxData);
			// layout ds sp khuyen mai
			renderLayout(displayData);
			// cap nhat thoi gian chuyen
			checkSendRequest = true;
		}
	}

	// /**
	// * Mo ta chuc nang cua ham
	// * @author: SoaN
	// * @return: void
	// * @throws:
	// */
	//
	// private void getListDisplayPrograme() {
	//
	// ActionEvent e = new ActionEvent();
	// e.viewData = " limit "
	// + ((tbPromotionList.getPagingControl().getCurrentPage() - 1) *
	// LIMIT_ROW_PER_PAGE)
	// + "," + LIMIT_ROW_PER_PAGE;
	// e.sender = this;
	// e.action = ActionEventConstant.GET_LIST_DISPLAY_PROGRAME;
	// UserController.getInstance().handleViewEvent(e);
	//
	// }

//	/**
//	*  Lay danh sach loai CT va danh sach nganh hang
//	*  @author: ThanhNN8
//	*  @return: void
//	*  @throws: 
//	*/
//	private void getListCombobox() {
//		// TODO Auto-generated method stub
//		ActionEvent e = new ActionEvent();
//		e.sender = this;
//		e.action = ActionEventConstant.GET_LIST_COMBOBOX_DISPLAY_PROGRAME;
//		SuperviorController.getInstance().handleViewEvent(e);
//	}

	/**
	 * search display programe
	 * 
	 * @author: SoaN
	 * @return: void
	 * @throws:
	 */

	private void searchDisplayProgram(boolean repareReset, boolean requestCombobox) {
		parent.showProgressDialog(StringUtil.getString(R.string.loading));
		Bundle data = new Bundle();
		int page = 0;
		if (!repareReset) {
			page = (tbPromotionList.getPagingControl().getCurrentPage() - 1);
		}
		String strPage = " limit "
				+ (page * Constants.NUM_ITEM_PER_PAGE)
				+ "," + Constants.NUM_ITEM_PER_PAGE;
		data.putString(IntentConstants.INTENT_PAGE, strPage);
		data.putBoolean(IntentConstants.INTENT_CHECK_PAGGING, !repareReset);
		data.putBoolean(IntentConstants.INTENT_CHECK_COMBOBOX, requestCombobox);
		if (listDepart == null) {
			data.putString(IntentConstants.INTENT_DISPLAY_DEPART, "");
		} else {
			int selectionSP = spinnerSalePath.getSelectedItemPosition();
			if(selectionSP < 0) {
				selectionSP = 0;
			}
			DisplayProgrameItemDTO dtoDepart = listDepart.get(selectionSP);
			data.putString(IntentConstants.INTENT_DISPLAY_DEPART, dtoDepart.value);
		}
		//hien tai chua co staff ID
		data.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
//		data.putInt(IntentConstants.INTENT_STAFF_ID, GlobalInfo.getInstance().getProfile().getUserData().id);
//		data.putString(IntentConstants.INTENT_PROMOTION_CODE, etCode.getText().toString());
//		data.putString(IntentConstants.INTENT_PROMOTION_NAME, etName.getText().toString());
		ActionEvent e = new ActionEvent();
		if (repareReset) {
			e.tag = 11;
		}
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_LIST_DISPLAY_PROGRAM;
		SuperviorController.getInstance().handleViewEvent(e);

	}
	
	/**
	 * 
	*  render du lieu man hinh
	*  @author: ThanhNN8
	*  @param model
	*  @return: void
	*  @throws:
	 */
	private void renderLayout(DisplayProgrameModel model) {
		if (model == null || model.getModelData() == null) {
			return;
		}
		int pos = (tbPromotionList.getPagingControl().getCurrentPage() - 1) * Constants.NUM_ITEM_PER_PAGE + 1;
		List<TableRow> listRows = new ArrayList<TableRow>();
		if (model.getModelData().size() > 0) {
			for (int i = 0, n = model.getModelData().size(); i < n; i++) {
				SuperVisorDisplayPromotionRow row = new SuperVisorDisplayPromotionRow(parent, null);

				row.setClickable(true);
				row.setOnClickListener(this);
				row.setTag(Integer.valueOf(pos));
				row.renderLayout(pos, model.getModelData().get(i));
				row.setListener(this);

				pos++;

				listRows.add(row);
			}
		} else {
			SuperVisorDisplayPromotionRow row = new SuperVisorDisplayPromotionRow(parent, null);

			row.setClickable(true);
			row.setOnClickListener(this);
			row.renderLayoutNoResult();

			listRows.add(row);
		}
		if (isFirstTime) {
			tbPromotionList.getHeaderView().addColumns(
				TableDefineContanst.SUPER_DIS_PROMOTION_TABLE_WIDTHS,
				TableDefineContanst.SUPER_DIS_PROMOTION_TABLE_TITLES, ImageUtil.getColor(R.color.BLACK),ImageUtil.getColor(R.color.TABLE_HEADER_BG));
			
			isFirstTime = false;
		}
		tbPromotionList.setNumItemsPage(Constants.NUM_ITEM_PER_PAGE);
		if (tbPromotionList.getPagingControl().totalPage < 0) {
			totalPage = model.getTotal();
			tbPromotionList.setTotalSize(model.getTotal());
		}
		tbPromotionList.addContent(listRows);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent actionEvent = modelEvent.getActionEvent();
		switch (actionEvent.action) {
		case ActionEventConstant.GET_LIST_DISPLAY_PROGRAM: {
			if (actionEvent.tag == 11) {
				tbPromotionList.getPagingControl().totalPage = -1;
				tbPromotionList.getPagingControl().setCurrentPage(1);
			}
			displayData = (DisplayProgrameModel) modelEvent.getModelData();
			ComboboxDisplayProgrameDTO comboDTO = displayData.getComboboxDTO();
			if (comboDTO != null) {
				comboboxData = comboDTO;
				updateData(comboboxData);
			}
			renderLayout(displayData);
			break;
		}
		default:
			break;
		}
		parent.closeProgressDialog();
		super.handleModelViewEvent(modelEvent);
	}

	/**
	*  Cap nhat du lieu cho loai CT va nganh hang
	*  @author: ThanhNN8
	*  @param modelData
	*  @return: void
	*  @throws: 
	*/
	private void updateData(ComboboxDisplayProgrameDTO modelData) {
		// TODO Auto-generated method stub
		if (listDepart == null) {
			listDepart = new ArrayList<DisplayProgrameItemDTO>();
			DisplayProgrameItemDTO itemDTO = new DisplayProgrameItemDTO();
			itemDTO.name = StringUtil.getString(R.string.ALL);
			itemDTO.value = "";
			listDepart.add(itemDTO);
			if (modelData.listDepartPrograme != null) {
				for (int j = 0, jlength = modelData.listDepartPrograme.size(); j < jlength; j++) {
					itemDTO = modelData.listDepartPrograme.get(j);
					listDepart.add(itemDTO);
				}
			}
		}
		int lengthDepart = listDepart.size();
		String departName[] = new String[lengthDepart];
		// khoi tao gia tri cho nganh hang
		for (int i = 0; i < lengthDepart; i++) {
			DisplayProgrameItemDTO dto = listDepart.get(i);
			departName[i] = dto.name;
		}

		SpinnerAdapter adapterDepart = new SpinnerAdapter(parent,
				R.layout.simple_spinner_item, departName);
		this.spinnerSalePath.setAdapter(adapterDepart);
		spinnerSalePath.setSelection(comboboxdepartselected);
		
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		ActionEvent actionEvent = modelEvent.getActionEvent();
		switch (actionEvent.action) {
		case ActionEventConstant.GET_LIST_DISPLAY_PROGRAM: {
			if (actionEvent.tag == 11) {
				DisplayProgrameModel model = new DisplayProgrameModel();
				List<DisplayProgrameDTO> modelData = new ArrayList<DisplayProgrameDTO>();
				model.setModelData(modelData);
				model.setTotal(0);
				renderLayout(model);
			}
			break;
		}

		default:
			break;
		}
		super.handleErrorModelViewEvent(modelEvent);
	}


	@Override
	public void onEvent(int eventType, View control, Object data) {
		if(eventType == ACTION_PROMOTION){
			comboboxdepartselected = 0;
			gotoPromotionProgramView();
		}  else if (eventType == ACTION_PRODUCT_LIST) {
			gotoProductListView();
		}
	}

	/**
	 * 
	*  Chuyen toi ds san pham
	*  @author: ThanhNN8
	*  @return: void
	*  @throws:
	 */
	private void gotoProductListView() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = new Bundle();
		e.action = ActionEventConstant.GO_TO_PRODUCT_LIST;
		SuperviorController.getInstance().handleSwitchFragment(e);
	}
	
	/**
	 * 
	 *  chuyen den man hinh danh sach trung bay
	 *  @author: ThanhNN8
	 *  @return: void
	 *  @throws:
	 */
	private void gotoPromotionProgramView() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = new Bundle();
		e.action = ActionEventConstant.GO_TO_PROMOTION_PROGRAM;
		SuperviorController.getInstance().handleSwitchFragment(e);
	}
	
	@Override
	public void onClick(View v) {
//		if (v == btSearch) {
//			searchDisplayProgram();
//		} 
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		currentPage = tbPromotionList.getPagingControl().getCurrentPage();
		searchDisplayProgram(false, false);
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
		if (action == ActionEventConstant.GO_TO_LIST_CUSTOMER_ATTEND_PROGRAM) {
			checkSendRequest = false;
			gotoCustomerAttentProgView((DisplayProgrameDTO) data);
		}
	}
	
	/**
	 * 
	*  chuyen den man hinh khach hang tham gia
	*  @author: ThanhNN8
	*  @param dto
	*  @return: void
	*  @throws:
	 */
	private void gotoCustomerAttentProgView(DisplayProgrameDTO dto) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(IntentConstants.INTENT_DATA, dto);
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = bundle;
		e.action = ActionEventConstant.GO_TO_LIST_CUSTOMER_ATTEND_PROGRAM;
		SuperviorController.getInstance().handleSwitchFragment(e);
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		if (arg0 == spinnerSalePath) {
			if (comboboxdepartselected != spinnerSalePath.getSelectedItemPosition()) {
				comboboxdepartselected = spinnerSalePath.getSelectedItemPosition();
				searchDisplayProgram(true, false);
			}
		}
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
	 */
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if(this.isVisible()){
				comboboxdepartselected = 0;
				spinnerSalePath.setSelection(comboboxdepartselected);
				searchDisplayProgram(true, true);
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

}
