/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

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
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.db.DisplayProgrameDTO;
import com.viettel.dms.dto.view.ComboboxDisplayProgrameDTO;
import com.viettel.dms.dto.view.DisplayProgrameItemDTO;
import com.viettel.dms.dto.view.TBHVDisplayProgrameModel;
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
import com.viettel.dms.view.main.TBHVActivity;
import com.viettel.sabeco.R;

/**
 * 
 *  Ds chuong trinh trung bay cua TBHV
 *  @author: Nguyen Thanh Dung
 *  @version: 1.1
 *  @since: 1.0
 */
public class TBHVDisplayProgramView extends BaseFragment implements
		OnClickListener, OnEventControlListener, OnItemSelectedListener,
		VinamilkTableListener {

	public static final String TAG = TBHVDisplayProgramView.class.getName();
	
	public static final int ACTION_PROMOTION = 0;
	public static final int ACTION_DISPLAY = 1;
	public static final int ACTION_PRODUCT_LIST = 2;
	
	private VinamilkTableView tbPromotionList;
	
	private Spinner spinnerSalePath;//nganh hang
	private boolean isFirstTime;//kiem tra lan dau tien vao man hinh
//	private List<DisplayProgrameItemDTO> listType = null;//ds loai chuong trinh
	private List<DisplayProgrameItemDTO> listDepart = null;//ds nganh hang
	
	private ComboboxDisplayProgrameDTO comboboxData;
	private TBHVDisplayProgrameModel displayData;
	
	private boolean checkSendRequest = true;

	private TBHVActivity parent;
	
	private int comboboxDepartSelected = 0;
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
	public static TBHVDisplayProgramView getInstance(int index) {
//		if (instance == null) {
			TBHVDisplayProgramView instance = new TBHVDisplayProgramView();
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
		parent = (TBHVActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		isFirstTime = true;
		comboboxDepartSelected = 0;
		ViewGroup v = (ViewGroup) inflater.inflate(
				R.layout.layout_tbhv_display_promotion_list, null);
		View view = super.onCreateView(inflater, v, savedInstanceState);
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_TBHV_LIST_DISPLAY_PROGRAME));

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
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_CTHTBH),R.drawable.menu_manage_icon, ACTION_DISPLAY);
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

	/**
	 * 
	*  Lay ds chuong trinh trung bay
	*  @author: Nguyen Thanh Dung
	*  @param repareReset
	*  @param requestCombobox
	*  @return: void
	*  @throws:
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
//			data.putString(IntentConstants.INTENT_DISPLAY_TYPE, "");
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
		ActionEvent e = new ActionEvent();
		if (repareReset) {
			e.tag = 11;
		}
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_TBHV_LIST_DISPLAY_PROGRAM;
		TBHVController.getInstance().handleViewEvent(e);

	}

	/**
	 * 
	*  render du lieu man hinh
	*  @author: Nguyen Thanh Dung
	*  @param model
	*  @return: void
	*  @throws:
	 */
	private void renderLayout(TBHVDisplayProgrameModel model) {
		if (model == null || model.getModelData() == null) {
			return;
		}
		int pos = (tbPromotionList.getPagingControl().getCurrentPage() - 1) * Constants.NUM_ITEM_PER_PAGE + 1;
		List<TableRow> listRows = new ArrayList<TableRow>();
		if (model.getModelData().size() > 0) {
		for (int i = 0, n = model.getModelData().size(); i < n; i++) {
			TBHVDisplayProgrameRow row = new TBHVDisplayProgrameRow(parent, null);

			row.setClickable(true);
			row.setOnClickListener(this);
			row.setTag(Integer.valueOf(pos));
			row.renderLayout(pos, model.getModelData().get(i));
			row.setListener(this);
			
			pos++;

			listRows.add(row);
		}
		} else {
			TBHVDisplayProgrameRow row = new TBHVDisplayProgrameRow(parent, null);

			row.setClickable(true);
			row.setOnClickListener(this);
			row.renderLayoutNoResult();

			listRows.add(row);
		}
		if (isFirstTime) {
			tbPromotionList.getHeaderView().addColumns(
				TableDefineContanst.TBHV_DIS_PROMOTION_TABLE_WIDTHS,
				TableDefineContanst.TBHV_DIS_PROMOTION_TABLE_TITLES, ImageUtil.getColor(R.color.BLACK),ImageUtil.getColor(R.color.TABLE_HEADER_BG));
			
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
		case ActionEventConstant.GET_TBHV_LIST_DISPLAY_PROGRAM: {
			if (actionEvent.tag == 11) {
				tbPromotionList.getPagingControl().totalPage = -1;
				tbPromotionList.getPagingControl().setCurrentPage(1);
			}
			displayData = (TBHVDisplayProgrameModel) modelEvent.getModelData();
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
	*  @author: Nguyen Thanh Dung
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
		spinnerSalePath.setSelection(comboboxDepartSelected);
		
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		ActionEvent actionEvent = modelEvent.getActionEvent();
		switch (actionEvent.action) {
		case ActionEventConstant.GET_TBHV_LIST_DISPLAY_PROGRAM: {
			if (actionEvent.tag == 11) {
				TBHVDisplayProgrameModel model = new TBHVDisplayProgrameModel();
				List<DisplayProgrameDTO> modelData = new ArrayList<DisplayProgrameDTO>();
				model.setModelData(modelData);
				model.setTotal(0);
				renderLayout(model);
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
		if(eventType == ACTION_PROMOTION){
			comboboxDepartSelected = 0;
			ActionEvent e = new ActionEvent();
			e.sender = this;
			e.viewData = new Bundle();
			e.action = ActionEventConstant.GO_TO_TBHV_PROMOTION_PROGRAM;
			TBHVController.getInstance().handleSwitchFragment(e);
		}else if (eventType == ACTION_PRODUCT_LIST) {
			gotoProductList();
		}
	}

	/**
	 * 
	 * Toi man hinh ds sp TBHV
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return: void
	 * @throws:
	 */
	private void gotoProductList() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = new Bundle();
		e.action = ActionEventConstant.GO_TO_TBHV_PRODUCT_LIST;
		TBHVController.getInstance().handleSwitchFragment(e);
	}
	
	@Override
	public void onClick(View v) {
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		currentPage = tbPromotionList.getPagingControl().getCurrentPage();
		searchDisplayProgram(false, false);
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
//		if (action == ActionEventConstant.GO_TO_LIST_CUSTOMER_ATTEND_PROGRAM) {
//			checkSendRequest = false;
//			gotoCustomerAttentProgView((DisplayProgrameDTO) data);
//		}
	}

	/**
	 * 
	*  chuyen den man hinh khach hang tham gia
	*  @author: Nguyen Thanh Dung
	*  @param dtoFollowProblemView
	*  @return: void
	*  @throws:
	 */
//	private void gotoCustomerAttentProgView(DisplayProgrameDTO dto) {
//		Bundle bundle = new Bundle();
//		bundle.putSerializable(IntentConstants.INTENT_DATA, dto);
//		ActionEvent e = new ActionEvent();
//		e.sender = this;
//		e.viewData = bundle;
//		e.action = ActionEventConstant.GO_TO_LIST_CUSTOMER_ATTEND_PROGRAM;
//		SuperviorController.getInstance().handleSwitchFragment(e);
//	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		if (arg0 == spinnerSalePath) {
			if (comboboxDepartSelected != spinnerSalePath.getSelectedItemPosition()) {
				comboboxDepartSelected = spinnerSalePath.getSelectedItemPosition();
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
				comboboxDepartSelected = 0;
				spinnerSalePath.setSelection(comboboxDepartSelected);
				searchDisplayProgram(true, true);
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

}
