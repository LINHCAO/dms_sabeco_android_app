/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.promotion;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.constants.TableDefineContanst;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.dto.db.DisplayProgrameDTO;
import com.viettel.dms.dto.view.CustomerAttentProgrameDTO;
import com.viettel.dms.dto.view.ListCustomerAttentProgrameDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.SalesPersonActivity;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * Chi tiet chuong trinh khuyen mai (interface)
 * 
 * @author: SoaN
 * @version: 1.0
 * @since: Jun 13, 2012
 */

public class ListCustomerAttentProgram extends BaseFragment implements
		OnEventControlListener, OnClickListener, VinamilkTableListener {

	public static final String TAG = ListCustomerAttentProgram.class.getName();

	private VinamilkTableView tbCustomerList;
	//id chuong trinh
	private TextView tvProgrameId;
	//ten chuong trinh
	private TextView tvProgrameName;
	//ngay bat dau
	private TextView tvProgrameStartDate;
	//ngay ket thuc
	private TextView tvProgrameEndDate;
	//loai chuong trinh
	private TextView tvProgrameType;
	//tong khach hang tham gia
	private TextView tvCustomerAttentTotal;
	//kiem tra man hinh khoi tao lan dau tien
	private Boolean isFirstTime = true;
	//nhap ma khach hang
	private VNMEditTextClearable etInputCustomerCode;
	//nhap ten khach hang
	private VNMEditTextClearable etInputCustomerName;
	//button find
	private Button btFind;
	//string tong so khach hang tham gia
	private String totalCustomerAttentPrograme;
	private SalesPersonActivity parent;
	private DisplayProgrameDTO dto;
	//luu lai bien de check luc tim kiem
	private String customerCode = "";
	private String customerName = "";
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			parent = (SalesPersonActivity) activity;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

	}

	public static ListCustomerAttentProgram getInstance(Bundle data) {
		ListCustomerAttentProgram instance = new ListCustomerAttentProgram();
		// Supply index input as an argument.
		instance.setArguments(data);

		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.view.main.BaseFragment#onCreateView(android.view
	 * .LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(
				R.layout.layout_customer_list, null);
		View view = super.onCreateView(inflater, v, savedInstanceState);

		tbCustomerList = (VinamilkTableView) view
				.findViewById(R.id.tbCustomerList);
		tvProgrameId = (TextView) view
				.findViewById(R.id.tvProgrameId);
		tvProgrameName = (TextView) view
				.findViewById(R.id.tvProgrameName);
		tvProgrameStartDate = (TextView) view
				.findViewById(R.id.tvProgrameStartDate);
		tvProgrameEndDate = (TextView) view
				.findViewById(R.id.tvProgrameEndDate);
		tvProgrameType = (TextView) view
				.findViewById(R.id.tvProgrameType);
		tvCustomerAttentTotal = (TextView) view
				.findViewById(R.id.tvCustomerAttentTotal);
		etInputCustomerCode = (VNMEditTextClearable) view
				.findViewById(R.id.etInputCustomerCode);
		etInputCustomerName = (VNMEditTextClearable) view
				.findViewById(R.id.etInputCustomerName);
		btFind = (Button) view.findViewById(R.id.btFind);
		btFind.setOnClickListener(this);
		totalCustomerAttentPrograme = tvCustomerAttentTotal.getText().toString();
		dto = (DisplayProgrameDTO) getArguments().getSerializable(IntentConstants.INTENT_DATA);
		// renderLayout();
		// menu bar
		// request get list display programe
		tvProgrameId.setText(dto.displayProgrameCode);
		tvProgrameName.setText(dto.displayProgrameName);
		tvProgrameStartDate.setText(dto.fromDate);
		tvProgrameEndDate.setText(dto.toDate);
		tvProgrameType.setText(dto.displayProgrameType);
		isFirstTime = true;
		searchListCustomerAttentPrograme(true);
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_DETAIL_DISPLAY_PROGRAME));
		return view;
	}

	/**
	 * 
	 * Lay thong tin chuong trinh va khach hang tham gia
	 * 
	 * @author: ThanhNN8
	 * @return: void
	 * @throws:
	 */
	private void searchListCustomerAttentPrograme(boolean repareReset) {
		// TODO Auto-generated method stub
		parent.showProgressDialog(getString(R.string.loading));
		Bundle data = new Bundle();
		int page = 0;
		if (!repareReset) {
			page = tbCustomerList.getPagingControl().getCurrentPage() - 1;
		}
		String strPage = " limit "
				+ (page * Constants.NUM_ITEM_PER_PAGE)
				+ "," + Constants.NUM_ITEM_PER_PAGE;
		data.putString(IntentConstants.INTENT_PAGE, strPage);
		data.putLong(IntentConstants.INTENT_DISPLAY_PROGRAM_ID, dto.displayProgrameId);
		data.putString(IntentConstants.INTENT_DISPLAY_PROGRAM_CODE, dto.displayProgrameCode);
		data.putString(IntentConstants.INTENT_CUSTOMER_CODE, customerCode);
		data.putString(IntentConstants.INTENT_CUSTOMER_NAME, customerName);
		data.putBoolean(IntentConstants.INTENT_CHECK_PAGGING, !repareReset);
		//hien tai chua co staff ID
		data.putLong(IntentConstants.INTENT_STAFF_ID, GlobalInfo.getInstance().getProfile().getUserData().id);
		ActionEvent e = new ActionEvent();
		if (repareReset) {
			e.tag = 11;
		}
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_LIST_CUSTOMER_ATTENT_PROGRAME;
		SaleController.getInstance().handleViewEvent(e);
	}
	
	/**
	 * 
	*  render du lieu cho man hinh
	*  @author: ThanhNN8
	*  @param dto
	*  @return: void
	*  @throws:
	 */
	private void renderLayout(ListCustomerAttentProgrameDTO dto) {
		int pos = (tbCustomerList.getPagingControl().getCurrentPage() - 1) * Constants.NUM_ITEM_PER_PAGE + 1;
		List<TableRow> listRows = new ArrayList<TableRow>();
		int size = dto.getListCustomer().size();
		for (int i = 0; i < size; i++) {
			CustomerAttentProgrameDTO dtoCustomerAttent = dto.getListCustomer().get(i);
			CustomerRow row = new CustomerRow(parent, null);
			
			row.setClickable(true);
			row.setOnClickListener(this);
			row.setTag(pos);
			row.renderLayout(pos, dtoCustomerAttent);
			row.setListener(this);
			pos++;

			listRows.add(row);
		}
		if (isFirstTime) {
			tbCustomerList.setListener(this);
			tbCustomerList.getPagingControl().setVisibility(View.VISIBLE);
			tbCustomerList.getHeaderView().addColumns(
					TableDefineContanst.CUS_PROMOTION_TABLE_WIDTHS,
					TableDefineContanst.CUS_PROMOTION_TABLE_TITLES, ImageUtil.getColor(R.color.BLACK),ImageUtil.getColor(R.color.TABLE_HEADER_BG));
			tbCustomerList.setNumItemsPage(Constants.NUM_ITEM_PER_PAGE);
			isFirstTime = false;
		}
		if (tbCustomerList.getPagingControl().totalPage < 0) {
			tbCustomerList.setTotalSize(dto.getTotalSize());
			String totalText = totalCustomerAttentPrograme + " (" + dto.getTotalSize() + ")";
			tvCustomerAttentTotal.setText(totalText);
		}

//		tbCustomerList.getPagingControl().setCurrentPage(1);
		tbCustomerList.addContent(listRows);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.view.listener.OnEventControlListener#onEvent(int,
	 * android.view.View, java.lang.Object)
	 */
	@Override
	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.view.main.BaseFragment#handleModelViewEvent(com.
	 * viettel.vinamilk.global.ModelEvent)
	 */
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		ActionEvent action = modelEvent.getActionEvent();
		switch (action.action) {
		case ActionEventConstant.GET_LIST_CUSTOMER_ATTENT_PROGRAME: {
			if (action.tag == 11) {
				reset();
			}
			ListCustomerAttentProgrameDTO viewDTO = (ListCustomerAttentProgrameDTO) modelEvent.getModelData();
			renderLayout(viewDTO);
			break;
		}

		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
		parent.closeProgressDialog();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.view.main.BaseFragment#handleErrorModelViewEvent
	 * (com.viettel.vinamilk.global.ModelEvent)
	 */
	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		parent.closeProgressDialog();
		ActionEvent action = modelEvent.getActionEvent();
		switch (action.action) {
		case ActionEventConstant.GET_LIST_CUSTOMER_ATTENT_PROGRAME: {
			if (action.tag == 11) {
				List<CustomerAttentProgrameDTO> listCustomer = new ArrayList<CustomerAttentProgrameDTO>();
				ListCustomerAttentProgrameDTO viewDTO = new ListCustomerAttentProgrameDTO();
				viewDTO.setListCustomer(listCustomer);
				viewDTO.setTotalSize(0);
				renderLayout(viewDTO);
			}
			break;
		}

		default:
			break;
		}
		super.handleErrorModelViewEvent(modelEvent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0 == btFind) {
			GlobalUtil.forceHideKeyboard(parent);
			if (etInputCustomerCode.getText().length() > 0) {
				customerCode = etInputCustomerCode.getText().toString().trim();
			} else {
				customerCode = "";
			}
			if (etInputCustomerName.getText().length() > 0) {
				customerName =  etInputCustomerName.getText().toString().trim();
			} else {
				customerName = "";
			}
			searchListCustomerAttentPrograme(true);
		} 
	}

	/**
	*  reset du lieu de tim kiem
	*  @author: ThanhNN8
	*  @return: void
	*  @throws: 
	*/
	private void reset() {
		// TODO Auto-generated method stub
		tbCustomerList.getPagingControl().totalPage = -1;
		tbCustomerList.getPagingControl().setCurrentPage(1);
	}

	/* (non-Javadoc)
	 * @see com.viettel.vinamilk.view.listener.VinamilkTableListener#handleVinamilkTableloadMore(android.view.View, java.lang.Object)
	 */
	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		// TODO Auto-generated method stub
		searchListCustomerAttentPrograme(false);
	}

	/* (non-Javadoc)
	 * @see com.viettel.vinamilk.view.listener.VinamilkTableListener#handleVinamilkTableRowEvent(int, android.view.View, java.lang.Object)
	 */
	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if(this.isVisible()){
				customerCode = "";
				customerName = "";
				etInputCustomerCode.setText("");
				etInputCustomerName.setText("");
				searchListCustomerAttentPrograme(true);
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

}
