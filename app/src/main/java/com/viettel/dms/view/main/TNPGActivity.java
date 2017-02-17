/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.main;

import android.app.ActionBar.LayoutParams;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.TNPGController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.control.MenuItemDTO;
import com.viettel.dms.dto.db.ActionLogDTO;
import com.viettel.dms.dto.db.MediaItemDTO;
import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.ServerLogger;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.TransactionProcessManager;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.tbhv.TBHVGsnppNvbhPositionView;
import com.viettel.dms.view.tbhv.TBHVReportProgressDateView;
import com.viettel.dms.view.tbhv.TBHVRouteSupervisionView;
import com.viettel.dms.view.tnpg.customer.CompetitorSalePG;
import com.viettel.dms.view.tnpg.followproblem.TTTTFollowProblemView;
import com.viettel.dms.view.tnpg.order.TNPGTakeAttendanceView;
import com.viettel.sabeco.R;
import com.viettel.utils.ImageValidatorTakingPhoto;
import com.viettel.utils.VTLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Activity chinh: cua nhan vien tiep thi
 * @author : BangHN since : 1.1
 */
public class TNPGActivity extends GlobalBaseActivity implements OnClickListener, OnItemClickListener {
    public static final int CONFIRM_EXIT_APP_OK = 11;
    public static final int CONFIRM_EXIT_APP_CANCEL = 12;
    //dong y ket thuc ghe tham
    private static final int ACTION_FINISH_VISIT_CUS_OK = 16;
    //cancel ket thuc ghe tham
    private static final int ACTION_FINISH_VISIT_CUS_CANCEL = 17;
    // confirm show camera
    public static final int CONFIRM_SHOW_CAMERA_WHEN_CLICK_CLOSE_DOOR = 13;
    boolean isTakePhotoFromMenuCloseCustomer = false;
    boolean isShowMenu = false;
    ImageView iconLeft;
    TextView tvTitleMenu;
    LinearLayout llMenu;// layout header icon

    ArrayList<MenuItemDTO> listMenu;
    ListView lvMenu;
    MenuAdapter menuAdapter;
    boolean isShowTextInMenu = false;

    boolean isSaveActionLogAndCloseCustomerAfterTakePhoto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        if (GlobalInfo.getInstance().isDebugMode) {
            setTitleName(GlobalInfo.getInstance().getProfile().getUserData().userCode);
        } else {
            setTitleName(GlobalInfo.getInstance().getProfile().getUserData().userCode);
        }
        llMenuUpdate.setVisibility(View.VISIBLE);
        llMenuGPS.setVisibility(View.VISIBLE);

        llShowHideMenu.setOnClickListener(this);
        iconLeft = (ImageView) llShowHideMenu.findViewById(R.id.ivLeftIcon);
        tvTitleMenu = (TextView) findViewById(R.id.tvTitleMenu);
        llMenu = (LinearLayout) findViewById(R.id.llMenu);
        lvMenu = (ListView) findViewById(R.id.lvMenu);

        initMenu();
        showHideMenuText(isShowMenu);
        checkVisitFromActionLog();
        // check va gui toan bo log dang con ton dong
        TransactionProcessManager.getInstance().startChecking(TransactionProcessManager.SYNC_NORMAL);
    }

    /**
     * Kiểm tra thông tin ghé thăm của tttt.
     * @author: dungdq3
     * @return: void
     */
    private void checkVisitFromActionLog() {
        // TODO Auto-generated method stub
        ActionEvent e = new ActionEvent();
        Vector<String> v = new Vector<String>();
        v.add(IntentConstants.INTENT_STAFF_ID);
        v.add(String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));

        e.viewData = v;
        e.action = ActionEventConstant.CHECK_VISIT_FROM_ACTION_LOG;
        e.sender = this;
        TNPGController.getInstance().handleViewEvent(e);
    }

    /**
     * Khoi tao menu chuong trinh
     *
     * @author : BangHN since : 4:41:56 PM
     */
    private void initMenu() {
        listMenu = new ArrayList<MenuItemDTO>();
        MenuItemDTO itemProblem = new MenuItemDTO(StringUtil.getString(R.string.TEXT_PROBLEMS_MANAGE),
                R.drawable.menu_problem_icon);
        MenuItemDTO itemCustomer = new MenuItemDTO(StringUtil.getString(R.string.TEXT_CUSTOMER_LIST_1),
                R.drawable.menu_customer_icon);
        MenuItemDTO itemReport = new MenuItemDTO(StringUtil.getString(R.string.TEXT_PROGRESS_SALE),
                R.drawable.menu_report_icon);


        listMenu.add(itemReport);
        listMenu.add(itemCustomer);
        listMenu.add(itemProblem);


        menuAdapter = new MenuAdapter(this, 0, listMenu);
        lvMenu.setAdapter(menuAdapter);
        lvMenu.setOnItemClickListener(this);
        listMenu.get(1).setSelected(true);
        lvMenu.setSelection(1);
//		gotoPGReportProgressDate();
        gotoCustomerListPG();
        menuAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v == llShowHideMenu) {
            isShowMenu = !isShowMenu;

            if (!isShowMenu) {
                iconLeft.setVisibility(View.GONE);
                tvTitleMenu.setVisibility(View.GONE);
            } else {
                iconLeft.setVisibility(View.VISIBLE);
                tvTitleMenu.setVisibility(View.VISIBLE);
            }
            showHideMenuText(isShowMenu);
        } else {
            super.onClick(v);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (CompetitorSalePG.TAG.equals(GlobalInfo.getInstance().getCurrentTag())) {
            CompetitorSalePG frag = (CompetitorSalePG) getFragmentManager().findFragmentByTag(
                    CompetitorSalePG.TAG);
            if (frag != null) {
                frag.onBackPressed();
            }
        } else if (TNPGTakeAttendanceView.TAG.equals(GlobalInfo.getInstance().getCurrentTag())) {
            TNPGTakeAttendanceView frag = (TNPGTakeAttendanceView) getFragmentManager().findFragmentByTag(
                    TNPGTakeAttendanceView.TAG);
            if (frag != null) {
                frag.onBackPressed();
            }
        }else{
            if (isValidateMenu(arg2))
                return;
            for (int i = 0; i < listMenu.size(); i++) {
                if (i == arg2)
                    listMenu.get(i).setSelected(true);
                else
                    listMenu.get(i).setSelected(false);
            }
            menuAdapter.notifyDataSetChanged();
            showDetails(arg2);
        }
    }

    /**
     * Show cac manhinh detail
     *
     * @author : BangHN since : 4:42:12 PM
     */
    void showDetails(int index) {
        switch (index) {
            // theo doi khac phuc van de
            case 2:
                gotoFollowProblemView();
                break;
            // khach hang
            case 1:
                gotoCustomerListPG();
                break;
            // tien do ban hang
            case 0:
                gotoPGReportProgressDate();
                break;
            default:
                break;
        }
    }

    /**
     * Toi man hinh ds giam sat lo trinh
     * @author: TamPQ
     * @return: void
     * @throws:
     */
    private void gotoCustomerListPG() {
        Bundle b = new Bundle();
        ActionEvent e = new ActionEvent();
        e.sender = this;
        e.viewData = b;
        e.action = ActionEventConstant.GO_TO_CUSTOMER_LIST_PG;
        TNPGController.getInstance().handleSwitchFragment(e);
    }



    /**
     * Toi man hinh theo doi van de
     * @author: YenNTH
     * @return: void
     * @throws:
     */
    private void gotoFollowProblemView() {
        ActionEvent e = new ActionEvent();
        e.action = ActionEventConstant.GO_TO_TTTT_FOLLOW_LIST_PROBLEM;
        e.sender = this;
        TNPGController.getInstance().handleSwitchFragment(e);
    }

    /**
     * validate khi nhan menu chinh
     *
     * @author : BangHN since : 1.0
     */
    private boolean isValidateMenu(int index) {
        boolean isOk = false;
        FragmentManager fm = getFragmentManager();
        BaseFragment fg;
        if (listMenu.get(index).isSelected()) {
            switch (index) {
                case 0:
                    fg = (BaseFragment) fm.findFragmentByTag(TTTTFollowProblemView.TAG);
                    if (fg != null && fg.isVisible()) {
                        isOk = true;
                    }
                    break;
                case 1:
                    fg = (BaseFragment) fm.findFragmentByTag(TBHVRouteSupervisionView.TAG);
                    if (fg != null && fg.isVisible()) {
                        isOk = true;
                    }
                    break;
                case 2:
                    fg = (BaseFragment) fm.findFragmentByTag(TBHVReportProgressDateView.TAG);
                    if (fg != null && fg.isVisible()) {
                        isOk = true;
                    }
                    break;
                default:
                    break;
            }
        }
        return isOk;
    }

    /**
     * Show full menu ben trai
     *
     * @author : BangHN since : 4:42:25 PM
     */
    public void showFullMenu(boolean isShowFull) {
        isShowTextInMenu = isShowFull;
        menuAdapter.notifyDataSetChanged();
    }

    /**
     * An hien full menu ben trai
     *
     * @author : BangHN since : 4:42:55 PM
     */
    private void showHideMenuText(boolean isShow) {
        showFullMenu(isShow);
        if (isShow) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(GlobalUtil.dip2Pixel(200),
                    LayoutParams.FILL_PARENT);
            RelativeLayout.LayoutParams llMenuParam = new RelativeLayout.LayoutParams(GlobalUtil.dip2Pixel(200),
                    LayoutParams.WRAP_CONTENT);
            lvMenu.setLayoutParams(layoutParams);
            llMenu.setLayoutParams(llMenuParam);
            iconLeft.setVisibility(View.VISIBLE);
            tvTitleMenu.setVisibility(View.VISIBLE);
        } else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(GlobalUtil.dip2Pixel(48),
                    LayoutParams.FILL_PARENT);
            RelativeLayout.LayoutParams llMenuParam = new RelativeLayout.LayoutParams(GlobalUtil.dip2Pixel(50),
                    LayoutParams.WRAP_CONTENT);
            lvMenu.setLayoutParams(layoutParams);
            llMenu.setLayoutParams(llMenuParam);
            iconLeft.setVisibility(View.GONE);
            tvTitleMenu.setVisibility(View.GONE);

        }
    }

    /**
     * adapter list menu fragment
     *
     * @author : BangHN since : 1:55:59 PM version :
     */
    class MenuAdapter extends ArrayAdapter<MenuItemDTO> {
        List<MenuItemDTO> modelMenu;
        Context mContext;

        public MenuAdapter(Context context, int textViewResourceId, List<MenuItemDTO> listMenu) {
            super(context, textViewResourceId, listMenu);
            modelMenu = listMenu;
            mContext = context;
        }

        @Override
        public MenuItemDTO getItem(int position) {
            return listMenu.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            VTLog.i("GET VIEW", "" + position);
            MenuItemRow cell = null;
            if (row == null) {
                LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = vi.inflate(R.layout.layout_fragment_menu_item, null);
                cell = new MenuItemRow(TNPGActivity.this, row);
                row.setTag(cell);
            } else {
                cell = (MenuItemRow) row.getTag();
            }
            cell.populateFrom(getItem(position));
            if (isShowTextInMenu) {
                cell.tvText.setVisibility(View.VISIBLE);
            } else {
                cell.tvText.setVisibility(View.GONE);
            }
            return row;
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() == 1) {
            // finish();
            GlobalUtil.showDialogConfirm(this, StringUtil.getString(R.string.TEXT_CONFIRM_EXIT), "Đồng ý",
                    CONFIRM_EXIT_APP_OK, "Hủy bỏ", CONFIRM_EXIT_APP_CANCEL, null);
        } else if (fm.getBackStackEntryCount() > 1) {
            int handleBack = -1;
            // dinh nghia cac luong xu ly su kien back dac biet
            if (TBHVGsnppNvbhPositionView.TAG.equals(GlobalInfo.getInstance().getCurrentTag())) {
                //TBHVGsnppNvbhPositionView frag = (TBHVGsnppNvbhPositionView) getFragmentManager().findFragmentByTag(
                //		TBHVGsnppNvbhPositionView.TAG);
                //if (frag != null) {
                //	handleBack = frag.onBackPressed();
                //}
            }else if (CompetitorSalePG.TAG.equals(GlobalInfo.getInstance().getCurrentTag())) {
                CompetitorSalePG frag = (CompetitorSalePG) getFragmentManager().findFragmentByTag(
                        CompetitorSalePG.TAG);
                if (frag != null) {
                    handleBack = frag.onBackPressed();
                }
            }else if (TNPGTakeAttendanceView.TAG.equals(GlobalInfo.getInstance().getCurrentTag())) {
                TNPGTakeAttendanceView frag = (TNPGTakeAttendanceView) getFragmentManager().findFragmentByTag(
                        TNPGTakeAttendanceView.TAG);
                if (frag != null) {
                    handleBack = frag.onBackPressed();
                }
            }
            if (handleBack == -1) {
                GlobalInfo.getInstance().popCurrentTag();
                super.onBackPressed();
            }
        } else {
            GlobalInfo.getInstance().popCurrentTag();
            super.onBackPressed();
        }
    }

    @Override
    public void onEvent(int eventType, View control, Object data) {
        switch (eventType) {
            case CONFIRM_EXIT_APP_OK:
//			sendBroadcast(ActionEventConstant.ACTION_FINISH_APP, new Bundle());
                TransactionProcessManager.getInstance().cancelTimer();
                //xu ly thoat ung dung
                processExitApp();
//			finish();
                break;
            case CONFIRM_EXIT_APP_CANCEL:
                break;
            case MENU_FINISH_VISIT:
                ActionLogDTO action = GlobalInfo.getInstance().getProfile().getActionLogVisitCustomer();
                if (action != null && StringUtil.isNullOrEmpty(action.endTime)) {
                    // kiem tra neu vao lai dung khach hang do thi khong
                    // insertlog
                    SpannableObject textConfirmed = new SpannableObject();
                    textConfirmed.addSpan(StringUtil
                                    .getString(R.string.TEXT_ALREADY_VISIT_CUSTOMER),
                            ImageUtil.getColor(R.color.WHITE),
                            android.graphics.Typeface.NORMAL);
                    textConfirmed.addSpan(" " + action.aCustomer.customerCode.substring(0, 3),
                            ImageUtil.getColor(R.color.WHITE),
                            android.graphics.Typeface.BOLD);
                    textConfirmed.addSpan(" - ",
                            ImageUtil.getColor(R.color.WHITE),
                            android.graphics.Typeface.BOLD);
                    textConfirmed.addSpan(action.aCustomer.customerName,
                            ImageUtil.getColor(R.color.WHITE),
                            android.graphics.Typeface.BOLD);
                    textConfirmed.addSpan(" trong ",
                            ImageUtil.getColor(R.color.WHITE),
                            android.graphics.Typeface.NORMAL);
                    textConfirmed.addSpan(DateUtils.getVisitTime(action.startTime),
                            ImageUtil.getColor(R.color.WHITE),
                            android.graphics.Typeface.BOLD);
                    textConfirmed.addSpan(StringUtil.getString(R.string.TEXT_ASK_END_VISIT_CUSTOMER),
                            ImageUtil.getColor(R.color.WHITE),
                            android.graphics.Typeface.NORMAL);

                    GlobalUtil.showDialogConfirmCanBackAndTouchOutSide(this, textConfirmed.getSpan(),
                            StringUtil.getString(R.string.TEXT_AGREE), ACTION_FINISH_VISIT_CUS_OK,
                            StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), ACTION_FINISH_VISIT_CUS_CANCEL, null, true,
                            true);
                }
                break;
            case ACTION_FINISH_VISIT_CUS_OK: {
                endVisitCustomer();
                break;
            }
            case ACTION_FINISH_VISIT_CUS_CANCEL: {
                break;
            }
            case MENU_FINISH_VISIT_CLOSE:
                this.showPopupConfirmCustomerClose();
                break;
            case CONFIRM_SHOW_CAMERA_WHEN_CLICK_CLOSE_DOOR:
                // show camera
                this.isTakePhotoFromMenuCloseCustomer = true;
                this.takenPhoto = GlobalUtil.takePhoto(this, GlobalBaseActivity.RQ_TAKE_PHOTO);
                break;
            default:
                super.onEvent(eventType, control, data);
                break;
        }
    }

    /**
     * ket thuc ghe tham
     * @author: dungdq3
     * @return: void
     */
    private void endVisitCustomer() {
        // TODO Auto-generated method stub
        requestUpdateActionLog("0", null, null, this);
        gotoCustomerListPG();
        GlobalInfo.getInstance().setPositionCustomerVisiting(null);
    }

    /**
     * Confirm chup hinh dong cua
     * @author:
     * @param: Tham số của hàm
     * @return: Kết qủa trả về
     * @throws: Ngoại lệ do hàm đưa ra (nếu có)
     */
    private void showPopupConfirmCustomerClose() {
        // TODO Auto-generated method stub
        GlobalUtil.showDialogConfirm(this,
                getString(R.string.TEXT_NOTIFY_DISPLAY_CAMERA_CLICK_BUTTON_CUSTOMER_CLOSE_THE_DOOR), StringUtil.getString(R.string.TEXT_AGREE),
                CONFIRM_SHOW_CAMERA_WHEN_CLICK_CLOSE_DOOR, "", 0, null);
    }

    private void gotoPGReportProgressDate() {
        ActionEvent e = new ActionEvent();
        e.sender = this;
        e.viewData = new Bundle();
        e.action = ActionEventConstant.GO_TO_GENERAL_STATISTICS;
        TNPGController.getInstance().handleSwitchFragment(e);
    }

    @Override
    public void handleModelViewEvent(ModelEvent modelEvent) {
        // TODO Auto-generated method stub
        ActionEvent e = modelEvent.getActionEvent();
        switch (e.action) {
            case ActionEventConstant.CHECK_VISIT_FROM_ACTION_LOG: {
                ActionLogDTO action = (ActionLogDTO) modelEvent.getModelData();
                if (action != null) {
                    GlobalInfo.getInstance().getProfile().setVisitingCustomer(true);
                    GlobalInfo.getInstance().getProfile().setActionLogVisitCustomer(action);
                    initMenuVisitCustomer(action.aCustomer.shortCode, action.aCustomer.customerName);
                    checkTNPGHaveActionFromActionLog(String.valueOf(action.aCustomer.customerId));
                }
                break;
            }
            case ActionEventConstant.UPLOAD_PHOTO_TO_SERVER: {
                if (this.isSaveActionLogAndCloseCustomerAfterTakePhoto) {
                    this.isSaveActionLogAndCloseCustomerAfterTakePhoto = false;
                    this.closeProgressDialog();
                } else {
                    super.handleModelViewEvent(modelEvent);
                }
                break;
            }
            case ActionEventConstant.CHECK_TNPG_HAVE_ACTION_FROM_ACTION_LOG: {
                ActionLogDTO action = (ActionLogDTO) modelEvent.getModelData();
                if (action != null) {
                    removeMenuCloseCustomer();
                }
                break;
            }
            default:
                super.handleModelViewEvent(modelEvent);
                break;
        }
    }

    @Override
    public void handleErrorModelViewEvent(ModelEvent modelEvent) {
        ActionEvent e = modelEvent.getActionEvent();
        switch (e.action) {
            case ActionEventConstant.UPLOAD_PHOTO_TO_SERVER: {
                if (this.isSaveActionLogAndCloseCustomerAfterTakePhoto) {
                    this.isSaveActionLogAndCloseCustomerAfterTakePhoto = false;
                    this.closeProgressDialog();
                } else {
                    super.handleErrorModelViewEvent(modelEvent);
                }
                break;
            }
            default:
                super.handleErrorModelViewEvent(modelEvent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            String filePath = "";
            switch (requestCode) {
                case RQ_TAKE_PHOTO: {
                    if (this.isTakePhotoFromMenuCloseCustomer) {
                        this.isTakePhotoFromMenuCloseCustomer = false;
                        this.isSaveActionLogAndCloseCustomerAfterTakePhoto = true;
                        if (resultCode == RESULT_OK) {
                            filePath = takenPhoto.getAbsolutePath();
                            ImageValidatorTakingPhoto validator = new ImageValidatorTakingPhoto(this, filePath,
                                    Constants.MAX_FULL_IMAGE_HEIGHT);
                            validator.setDataIntent(data);
                            if (validator.execute()) {
                                // imageFile = takingFile;
                                //GlobalInfo.getInstance().setBitmapData(validator.getBitmap());
                                this.saveCustomerCloseDoorAndGotoCustomerList();
                                updateTakenPhoto();
                            }
                        }
                    } else {
                        super.onActivityResult(requestCode, resultCode, data);
                    }
                    break;
                }
                default:
                    super.onActivityResult(requestCode, resultCode, data);
                    break;

            }
        }catch(RuntimeException ex){
            ServerLogger.sendLog("ActivityState", "GlobalBaseActivity : "
                            + VNMTraceUnexceptionLog.getReportFromThrowable(ex),
                    TabletActionLogDTO.LOG_EXCEPTION);
        }
    }

    /**
     *
     * @author: dungnt19
     * @since: 11:36:39 16-12-2013
     * @return: void
     * @throws:
     * @param customerId
     */
    private void checkTNPGHaveActionFromActionLog(String customerId) {
        ActionEvent e = new ActionEvent();
        Bundle bundle = new Bundle();
        bundle.putString(IntentConstants.INTENT_STAFF_ID, String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));
        bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, customerId);

        e.viewData = bundle;
        e.action = ActionEventConstant.CHECK_TNPG_HAVE_ACTION_FROM_ACTION_LOG;
        e.sender = this;
        TNPGController.getInstance().handleViewEvent(e);
    }
    /**
     *
     * thuc hien request luu action log - hien thi man hinh danh sach khach hang
     * va thiet lap ket thuc ghe tham khach hang hien tai
     *
     * @author: HaiTC3
     * @return: void
     * @throws:
     */
    private void saveCustomerCloseDoorAndGotoCustomerList() {
        requestUpdateActionLog("1", null, null, this);
        // ket thuc ghe tham
        GlobalInfo.getInstance().getProfile().setVisitingCustomer(false);
        gotoCustomerListPG();
        endVisitCustomerBar();
    }

    /**
     *
     * update image to db and to server
     *
     * @author: HaiTC3
     * @return: void
     * @throws:
     */
    private void updateTakenPhoto() {
        this.showProgressDialog(StringUtil.getString(R.string.loading));
        Vector<String> viewData = new Vector<String>();

//		viewData.add(IntentConstants.INTENT_STAFF_ID);
//		viewData.add(Integer.toString(GlobalInfo.getInstance().getProfile().getUserData().id));
//
//		viewData.add(IntentConstants.INTENT_SHOP_ID);
//		if (!StringUtil.isNullOrEmpty(GlobalInfo.getInstance().getProfile().getUserData().shopId)) {
//			viewData.add(GlobalInfo.getInstance().getProfile().getUserData().shopId);
//		} else {
//			viewData.add("1");
//		}

        if (this.takenPhoto != null) {
            viewData.add(IntentConstants.INTENT_FILE_NAME);
            viewData.add(this.takenPhoto.getName());

            viewData.add(IntentConstants.INTENT_TAKEN_PHOTO);
            viewData.add(this.takenPhoto.getAbsolutePath());
        }

        MediaItemDTO dto = new MediaItemDTO();

        try {
            dto.objectId = Long.parseLong(GlobalInfo.getInstance().getProfile().getActionLogVisitCustomer().aCustomer
                    .getCustomerId());
            dto.objectType = 0;
            dto.mediaType = 0;// loai hinh anh , 1 loai video
            dto.url = this.takenPhoto.getAbsolutePath();
            dto.thumbUrl = this.takenPhoto.getAbsolutePath();
            dto.createDate = DateUtils.now();
            dto.createUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;
            dto.lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude();
            dto.lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude();
            dto.type = 1;
            dto.status = 1;
            dto.fileSize = this.takenPhoto.length();
            dto.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
            if (!StringUtil.isNullOrEmpty(GlobalInfo.getInstance().getProfile().getUserData().shopId)) {
                dto.shopId = Integer.valueOf(GlobalInfo.getInstance().getProfile().getUserData().shopId);
            } else {
                dto.shopId = 1;
            }

            viewData.add(IntentConstants.INTENT_OBJECT_ID);
            viewData.add(GlobalInfo.getInstance().getProfile().getActionLogVisitCustomer().aCustomer.getCustomerId());
            viewData.add(IntentConstants.INTENT_OBJECT_TYPE_IMAGE);
            viewData.add(String.valueOf(dto.objectType));
            viewData.add(IntentConstants.INTENT_MEDIA_TYPE);
            viewData.add(String.valueOf(dto.mediaType));
            viewData.add(IntentConstants.INTENT_URL);
            viewData.add(String.valueOf(dto.url));
            viewData.add(IntentConstants.INTENT_THUMB_URL);
            viewData.add(String.valueOf(dto.thumbUrl));
            viewData.add(IntentConstants.INTENT_CREATE_DATE);
            viewData.add(String.valueOf(dto.createDate));
            viewData.add(IntentConstants.INTENT_CREATE_USER);
            viewData.add(String.valueOf(dto.createUser));
            viewData.add(IntentConstants.INTENT_LAT);
            viewData.add(String.valueOf(dto.lat));
            viewData.add(IntentConstants.INTENT_LNG);
            viewData.add(String.valueOf(dto.lng));
            viewData.add(IntentConstants.INTENT_FILE_SIZE);
            viewData.add(String.valueOf(dto.fileSize));
            viewData.add(IntentConstants.INTENT_CUSTOMER_CODE);
            viewData.add(GlobalInfo.getInstance().getProfile().getActionLogVisitCustomer().aCustomer
                    .getCustomerCode());
            viewData.add(IntentConstants.INTENT_STATUS);
            viewData.add("1");
            viewData.add(IntentConstants.INTENT_TYPE);
            viewData.add("1");
        } catch (NumberFormatException e1) {
            VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e1));
        }

        ActionEvent e = new ActionEvent();
        e.action = ActionEventConstant.UPLOAD_PHOTO_TO_SERVER;
        e.viewData = viewData;
        e.userData = dto;
        e.sender = this;
        UserController.getInstance().handleViewEvent(e);
    }
}
