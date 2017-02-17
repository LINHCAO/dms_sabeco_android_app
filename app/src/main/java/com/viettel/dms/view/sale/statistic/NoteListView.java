/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.statistic;

import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.FragmentMenuContanst;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.dto.db.ApParamDTO;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.FeedBackDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableView;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.sale.customer.CustomerFeedBackDto;
import com.viettel.dms.view.sale.customer.PostFeedbackView;
import com.viettel.sabeco.R;

/**
 * MH cần thực hiện(theo doi khac phuc NVBH)
 * 
 * @author : YenNTH
 * 
 */
public class NoteListView extends BaseFragment implements
		OnEventControlListener, OnClickListener, VinamilkTableListener,
		OnItemSelectedListener {
	private GlobalBaseActivity parent;
	/*action*/
	private static final int ACTION_OK = 0;
	private static final int ACTION_CANCEL = 1;
	private static final int ACTION_DELETE = 2;
	private static final int ACTION_CLOSE = 3;
	private static final int ACTION_DONE = 4;
	private static final int ACTION_ROW_CLICK = 5;
	private static final int ACTION_DONE_OK = 9;
	private static final int ACTION_DONE_CANCEL = 10;
	/*action*/
	Spinner spStatus;// spinner trang thai
	Spinner spTypeProblem;// spinner loai van de
	private DMSTableView tbNoteList;
	private CustomerFeedBackDto dto = new CustomerFeedBackDto();// dto van de
	Vector<ApParamDTO> vTypeProblem;// dto loai van de 

	private String[] arrChooseTypeProblem;
	private boolean isUpdateData = false;
	private int curSelection = -1;
	private int curSelectionTypeProblem = -1;
	// alert popup problem detail
	AlertDialog alertFollowProblemDetail;
	// popup problem detail
	SalePopupProblemDetailView salePopupProblemDetail;
	View currentRow;
	Button btAddNote;
	private CustomerDTO customer;
	private boolean isGetTotalPage;
	public static final String TAG = NoteListView.class.getName();
	

	public static NoteListView newInstance() {
		NoteListView f = new NoteListView();
		// Supply index input as an argument.
		Bundle args = new Bundle();
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parent = (GlobalBaseActivity) getActivity();
		isGetTotalPage=false;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_note_list_fragment, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(StringUtil.getString(R.string.TEXT_HEADER_TITLE_NOTE_LIST));
		initHeadermenu();
		spTypeProblem = (Spinner) view.findViewById(R.id.spTypeProblem);
		spTypeProblem.setSelection(0);
		spStatus = (Spinner) view.findViewById(R.id.spStatus);
		SpinnerAdapter adapterChoose = new SpinnerAdapter(parent,R.layout.simple_spinner_item, Constants.ARRCHOOSE);
		spStatus.setAdapter(adapterChoose);
		spStatus.setSelection(0);
		curSelection = spStatus.getSelectedItemPosition();
		spStatus.setOnItemSelectedListener(this);
		tbNoteList = (DMSTableView) view.findViewById(R.id.tbNoteList);
		btAddNote = (Button) view.findViewById(R.id.btAddNote);
		btAddNote.setOnClickListener(this);
		initHeaderTable(tbNoteList, new NoteListRow(parent, this,ACTION_ROW_CLICK, null));
		tbNoteList.setListener(this);
		getListTypeProblemNvbhGsnpp();
		return v;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		spTypeProblem.setSelection(0);
		spStatus.setSelection(0);
	}
	/**
	 * Lấy danh sách cần thực hiện NVBH
	 * 
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	private void getNoteList(int page) {
		if (!parent.isShowProgressDialog()) {
			parent.showLoadingDialog();
		}
		Bundle bundle = new Bundle();
		if (curSelection <= 0) {// tat ca
			bundle.putString(IntentConstants.INTENT_STATE, Constants.STR_BLANK);// tat ca
		} else {
			bundle.putString(IntentConstants.INTENT_STATE,getStatus(Constants.ARRCHOOSE[curSelection]));
		}
		if (curSelectionTypeProblem <= 0) {// tat ca
			bundle.putString(IntentConstants.INTENT_TYPE, Constants.STR_BLANK);
		} else {
			bundle.putString(IntentConstants.INTENT_TYPE,((ApParamDTO) vTypeProblem.elementAt(curSelectionTypeProblem - 1)).apParamCode);
		}
		bundle.putString(IntentConstants.INTENT_STAFF_ID,String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, Constants.STR_BLANK);
		bundle.putString(IntentConstants.INTENT_DONE_DATE, Constants.STR_BLANK);
		bundle.putInt(IntentConstants.INTENT_PAGE, page);
		bundle.putBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE, isGetTotalPage);

		ActionEvent e = new ActionEvent();
		e.viewData = bundle;
		e.action = ActionEventConstant.NOTE_LIST_VIEW;
		e.sender = this;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * lay danh sach loai van de
	 * 
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	private void getListTypeProblemNvbhGsnpp() {
		if (!parent.isShowProgressDialog()) {
			parent.showLoadingDialog();
		}
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GET_LIST_TYPE_PROBLEM_NVBH_GSNPP;
		e.sender = this;
		SaleController.getInstance().handleViewEvent(e);
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (GlobalBaseActivity) getActivity();
		customer = (CustomerDTO) getArguments().getSerializable(IntentConstants.INTENT_CUSTOMER);
	}

	/**
	 * Lay danh sach trang thai
	 * 
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	private String getStatus(String status) {
		String d = Constants.STR_BLANK;
		if (status.equals(Constants.NOT_DONE)) {
			d = FeedBackDTO.FEEDBACK_NOT_DONE;
		} else if (status.equals(Constants.DONE)) {
			d = FeedBackDTO.FEEDBACK_DONE;
		} else if (status.equals(Constants.APPROVED)) {
			d = FeedBackDTO.FEEDBACK_APPROVED;
		} else {
			d = Constants.STR_BLANK; // tat ca
		}
		return d;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_LIST_TYPE_PROBLEM_NVBH_GSNPP:
			vTypeProblem = (Vector<ApParamDTO>) modelEvent.getModelData();
			if (vTypeProblem != null && vTypeProblem.size() > 0) {
				arrChooseTypeProblem = new String[vTypeProblem.size() + 1];
				arrChooseTypeProblem[0] = StringUtil.getString(R.string.ALL);
				for (int i = 1, size = vTypeProblem.size() + 1; i < size; i++) {
					arrChooseTypeProblem[i] = ((ApParamDTO) vTypeProblem.elementAt(i - 1)).apParamName;
				}
				SpinnerAdapter adapterChoose = new SpinnerAdapter(parent,R.layout.simple_spinner_item, arrChooseTypeProblem);
				spTypeProblem.setAdapter(adapterChoose);
				spTypeProblem.setSelection(0);
				curSelectionTypeProblem = spTypeProblem.getSelectedItemPosition();
				spTypeProblem.setOnItemSelectedListener(this);
			}
			isGetTotalPage = true;
			getNoteList(1);
			break;
		case ActionEventConstant.NOTE_LIST_VIEW:
			parent.closeProgressDialog();
			CustomerFeedBackDto tempDto = (CustomerFeedBackDto) modelEvent.getModelData();
			if (isUpdateData) {
				isUpdateData = false;
				dto.currentPage = -1;
			}
			if (dto == null) {
				dto = tempDto;
			} else {
				dto.arrItem = tempDto.arrItem;
				dto.totalFeedBack = tempDto.totalFeedBack;
			}
			renderLayout();
			break;
		case ActionEventConstant.UPDATE_FEEDBACK:
			parent.closeProgressDialog();
			parent.showDialog(StringUtil.getString(R.string.TEXT_UPDATE_FEEDBACK_SUCC));
			isGetTotalPage = true;
			getNoteList(dto.currentPage);
			break;
		case ActionEventConstant.DELETE_FEEDBACK:
			parent.showDialog(StringUtil.getString(R.string.TEXT_DELETE_FEEDBACK_SUCC));
			dto.currentPage = -1;
			isGetTotalPage = true;
			getNoteList(1);
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		parent.closeProgressDialog();
		switch (e.action) {
		case ActionEventConstant.DELETE_FEEDBACK:
			View v = (View) e.userData;
			v.setEnabled(true);
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	/**
	 * renderLayout
	 * 
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	private void renderLayout() {
		if (isGetTotalPage) {
			tbNoteList.setTotalSize(dto.totalFeedBack,tbNoteList.getPagingControl().getCurrentPage());
		}
		int pos = 1 + Constants.NUM_ITEM_PER_PAGE * (tbNoteList.getPagingControl().getCurrentPage() - 1);
		tbNoteList.clearAllData();
		if (dto.arrItem != null && dto.arrItem.size() > 0) {
			for (FeedBackDTO feedBackDTO: dto.arrItem) {
				NoteListRow row = new NoteListRow(parent, this,ACTION_ROW_CLICK, feedBackDTO);
				row.cbDone.setOnClickListener(this);
				row.cbDone.setTag(feedBackDTO);
				row.render(pos, feedBackDTO);
				pos++;
				tbNoteList.addRow(row);
			}
		} else {
			tbNoteList.showNoContentRow();
		}
	}

	/**
	 * update Feedback Row
	 * 
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	private void updateFeedbackRow(FeedBackDTO item) {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.action = ActionEventConstant.UPDATE_FEEDBACK;
		e.viewData = item;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * 
	 * reset all value
	 * 
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	private void resetAllValue() {
		spStatus.setSelection(0);
		curSelection = spStatus.getSelectedItemPosition();
		if (dto != null) {
			dto.currentPage = -1;
		}
		getListTypeProblemNvbhGsnpp();
	}

	/**
	 * 
	 * init header for menu
	 * 
	 * @return: void
	 * @throws:
	 * @author: YenNTH
	 * @date: Jan 2, 2013
	 */
	private void initHeadermenu() {
		// enable menu bar
		enableMenuBar(this, FragmentMenuContanst.NVBH_REPORT_MENU_COMMANDs, FragmentMenuContanst.INDEX_MENU_NVBH_NEED_DONE_VIEW);
		
	}

	/**
	 * 
	 * hien thi chi tiet van de
	 * 
	 * @param dto
	 * @return: void
	 * @throws:
	 * @author: YenNTH
	 */
	private void showFollowProblemDetail(FeedBackDTO dto) {
		if (alertFollowProblemDetail == null) {
			Builder build = new AlertDialog.Builder(parent,R.style.CustomDialogTheme);
			salePopupProblemDetail = new SalePopupProblemDetailView(parent,this, ACTION_CLOSE, ACTION_DELETE, ACTION_DONE);
			build.setView(salePopupProblemDetail.viewLayout);
			alertFollowProblemDetail = build.create();
			Window window = alertFollowProblemDetail.getWindow();
			window.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 255,255, 255)));
			window.setLayout(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			window.setGravity(Gravity.CENTER);
		}
		salePopupProblemDetail.renderLayoutWithObject(dto);
		alertFollowProblemDetail.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cbNote:
			CheckBox cb = (CheckBox) v;
			if (cb.isChecked()) {
				cb.setEnabled(false);
				FeedBackDTO item = (FeedBackDTO) cb.getTag();
				Vector<Object> vt;
				vt = new Vector<Object>();
				vt.add(item);
				vt.add(cb);
				dto.currentPage = tbNoteList.getPagingControl()
						.getCurrentPage();
				GlobalUtil
						.showDialogConfirmCanBackAndTouchOutSide(
								this,
								parent,
								StringUtil
										.getString(R.string.TEXT_CONFIRM_UPDATE_PROBLEM_DONE_2),
								StringUtil.getString(R.string.TEXT_AGREE),
								ACTION_DONE_OK, StringUtil
										.getString(R.string.TEXT_CANCEL),
								ACTION_DONE_CANCEL, vt, false, false);

			}
			break;
		case R.id.btAddNote:
			Bundle b = new Bundle();
			b.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, "01-03");
			b.putSerializable(IntentConstants.INTENT_CUSTOMER, customer);
			b.putInt(IntentConstants.INTENT_FROM,
					PostFeedbackView.FROM_NOTE_LIST_VIEW);
			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.POST_FEEDBACK;
			e.sender = this;
			e.viewData = b;
			SaleController.getInstance().handleSwitchFragment(e);
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onEvent(int eventType, View control, Object data) {
		ActionEvent e = new ActionEvent();
		Vector<Object> vt;
		View v;
		FeedBackDTO item;
		switch (eventType) {
		case ACTION_OK:
			vt = (Vector<Object>) data;
			item = (FeedBackDTO) vt.elementAt(0);
			v = (View) vt.elementAt(1);
			dto.currentPage = tbNoteList.getPagingControl().getCurrentPage();

			item.status = FeedBackDTO.FEEDBACK_STATUS_DELETE;
			item.updateDate = DateUtils.now();
			item.updateUser = GlobalInfo.getInstance().getProfile().getUserData().userName;
			parent.showLoadingDialog();
			e.sender = this;
			e.action = ActionEventConstant.DELETE_FEEDBACK;
			e.viewData = item;
			e.userData = v;
			SaleController.getInstance().handleViewEvent(e);
			break;
		case ACTION_CANCEL:
			vt = (Vector<Object>) data;
			v = (View) vt.elementAt(1);
			v.setEnabled(true);
			break;
		case ACTION_DELETE:
			dto.currentPage = tbNoteList.getPagingControl().getCurrentPage();
			currentRow.setEnabled(false);
			vt = new Vector<Object>();
			vt.add(data);
			vt.add(currentRow);
			GlobalUtil
					.showDialogConfirmCanBackAndTouchOutSide(
							this,
							parent,
							StringUtil
									.getString(R.string.TEXT_CONFIRM_DELETE_PROBLEM_DONE),
							StringUtil.getString(R.string.TEXT_AGREE),
							ACTION_OK,
							StringUtil.getString(R.string.TEXT_CANCEL),
							ACTION_CANCEL, vt, false, false);
			if (this.alertFollowProblemDetail.isShowing()) {
				this.alertFollowProblemDetail.dismiss();
			}
			break;
		case ACTION_CLOSE:
			if (this.alertFollowProblemDetail.isShowing()) {
				this.alertFollowProblemDetail.dismiss();
			}
			break;
		case ACTION_DONE:
			dto.currentPage = tbNoteList.getPagingControl().getCurrentPage();
			currentRow.setEnabled(false);
			vt = new Vector<Object>();
			vt.add(data);
			vt.add(currentRow);

			GlobalUtil
					.showDialogConfirmCanBackAndTouchOutSide(
							this,
							parent,
							StringUtil
									.getString(R.string.TEXT_CONFIRM_UPDATE_PROBLEM_DONE_2),
							StringUtil.getString(R.string.TEXT_AGREE),
							ACTION_DONE_OK, StringUtil
									.getString(R.string.TEXT_CANCEL),
									ACTION_CANCEL, vt, false, false);
			if (this.alertFollowProblemDetail.isShowing()) {
				this.alertFollowProblemDetail.dismiss();
			}
			break;
		case ACTION_DONE_OK: {
			vt = (Vector<Object>) data;
			item = (FeedBackDTO) vt.elementAt(0);
			item.doneDate = DateUtils.now();
			item.updateDate = DateUtils.now();
			item.updateUser = String.valueOf(GlobalInfo.getInstance()
					.getProfile().getUserData().userCode);
			item.status = FeedBackDTO.FEEDBACK_STATUS_STAFF_DONE;
			updateFeedbackRow(item);
			break;
		}
		case ACTION_DONE_CANCEL: {
			vt = (Vector<Object>) data;
			CheckBox cb = (CheckBox) vt.elementAt(1);
			cb.setEnabled(true);
			cb.setChecked(false);
		}
		default:
			super.onEvent(eventType, control, data);
			break;
		}
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		if (control == tbNoteList) {
			dto.currentPage = tbNoteList.getPagingControl().getCurrentPage();
			isGetTotalPage = false;
			getNoteList(dto.currentPage);
		}
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
		if(action == ACTION_ROW_CLICK){
			this.currentRow = control;
			if((FeedBackDTO) data != null){
				showFollowProblemDetail((FeedBackDTO) data);
			}
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if (arg0 == spStatus) {
			if (curSelection != spStatus.getSelectedItemPosition()) {
				curSelection = spStatus.getSelectedItemPosition();
				dto.currentPage = -1;
				isGetTotalPage = true;
				getNoteList(1);
			}
		} else if (arg0 == spTypeProblem) {
			if (curSelectionTypeProblem != spTypeProblem
					.getSelectedItemPosition()) {
				curSelectionTypeProblem = spTypeProblem
						.getSelectedItemPosition();
				dto.currentPage = -1;
				isGetTotalPage = true;
				getNoteList(1);
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				// cau request du lieu man hinh
				isUpdateData = true;
				resetAllValue();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}
	
}
