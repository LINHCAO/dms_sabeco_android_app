package com.viettel.dms.view.main;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.viettel.dms.controller.PGController;
import com.viettel.dms.dto.control.MenuItemDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.TransactionProcessManager;
import com.viettel.dms.view.pg.PGOrderView;
import com.viettel.dms.view.pg.PGReportOrderView;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yennth16 on 8/11/2016.
 */
public class PGActivity extends GlobalBaseActivity implements OnClickListener, OnItemClickListener {
    boolean isShowMenu = false;
    ImageView iconLeft;
    TextView tvTitleMenu;
    LinearLayout llMenu;// layout header icon

    ArrayList<MenuItemDTO> listMenu;
    ListView lvMenu;
    MenuAdapter menuAdapter;
    boolean isShowTextInMenu = false;
    public static final int CONFIRM_EXIT_APP_OK = 1;
    public static final int CONFIRM_EXIT_APP_CANCEL = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
//        if (GlobalInfo.getInstance().isDebugMode) {
//            setTitleName(StringUtil.getString(R.string.TEXT_HEPL));
//        } else {
//            setTitleName(StringUtil.getString(R.string.TEXT_HEPL));
//        }
        llMenuUpdate.setVisibility(View.VISIBLE);
        llMenuGPS.setVisibility(View.GONE);
//        llShowHideMenu.setOnClickListener(this);
        iconLeft = (ImageView) llShowHideMenu.findViewById(R.id.ivLeftIcon);
        iconLeft.setVisibility(View.GONE);
        tvTitleMenu = (TextView) findViewById(R.id.tvTitleMenu);
        llMenu = (LinearLayout) findViewById(R.id.llMenu);
        lvMenu = (ListView) findViewById(R.id.lvMenu);
        lvMenu.setVisibility(View.GONE);
        initMenu();
        showHideMenuText(isShowMenu);
        // check va gui toan bo log dang con ton dong
        TransactionProcessManager.getInstance().startChecking(TransactionProcessManager.SYNC_NORMAL);
    }
    /**
     * Khoi tao menu chuong trinh
     *
     * @author : BangHN since : 4:41:56 PM
     */
    private void initMenu() {
        listMenu = new ArrayList<MenuItemDTO>();
        MenuItemDTO itemOrder = new MenuItemDTO(StringUtil.getString(R.string.TEXT_MENU_SALES),R.drawable.menu_customer_icon);
        listMenu.add(itemOrder);
        menuAdapter = new MenuAdapter(this, 0, listMenu);
        lvMenu.setAdapter(menuAdapter);
        lvMenu.setOnItemClickListener(this);
        listMenu.get(0).setSelected(true);
        lvMenu.setSelection(0);
        gotoPGOrderView();
        menuAdapter.notifyDataSetChanged();
//        GlobalUtil.forceHideKeyboardUseToggle(this);
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

    /**
     * Show cac manhinh detail
     *
     * @author : BangHN since : 4:42:12 PM
     */
    void showDetails(int index) {
        switch (index) {
            case 0:
                gotoPGOrderView();
                break;
            case 1:
                gotoPGReportOrderView();
                break;
            default:
                break;
        }
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
                    fg = (BaseFragment) fm.findFragmentByTag(PGOrderView.TAG);
                    if (fg != null && fg.isVisible()) {
                        isOk = true;
                    }
                    break;
                case 1:
                    fg = (BaseFragment) fm.findFragmentByTag(PGReportOrderView.TAG);
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
                    ActionBar.LayoutParams.FILL_PARENT);
            RelativeLayout.LayoutParams llMenuParam = new RelativeLayout.LayoutParams(GlobalUtil.dip2Pixel(200),
                    ActionBar.LayoutParams.WRAP_CONTENT);
            lvMenu.setLayoutParams(layoutParams);
            llMenu.setLayoutParams(llMenuParam);
            iconLeft.setVisibility(View.VISIBLE);
            tvTitleMenu.setVisibility(View.VISIBLE);
        } else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(GlobalUtil.dip2Pixel(48),
                    ActionBar.LayoutParams.FILL_PARENT);
            RelativeLayout.LayoutParams llMenuParam = new RelativeLayout.LayoutParams(GlobalUtil.dip2Pixel(50),
                    ActionBar.LayoutParams.WRAP_CONTENT);
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
                cell = new MenuItemRow(PGActivity.this, row);
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
            GlobalUtil.showDialogConfirm(this,
                    StringUtil.getString(R.string.TEXT_CONFIRM_EXIT), "Đồng ý",
                    CONFIRM_EXIT_APP_OK, "Hủy bỏ", CONFIRM_EXIT_APP_CANCEL,
                    null);
        }else{
            gotoPGOrderView();
        }
    }

    @Override
    public void onEvent(int eventType, View control, Object data) {
        switch (eventType) {
            case CONFIRM_EXIT_APP_OK:
                TransactionProcessManager.getInstance().cancelTimer();
                processExitApp();
                break;
            case CONFIRM_EXIT_APP_CANCEL:
                break;
            default:
                super.onEvent(eventType, control, data);
                break;
        }
    }

    private void gotoPGReportOrderView() {
        ActionEvent e = new ActionEvent();
        e.sender = this;
        e.viewData = new Bundle();
        e.action = ActionEventConstant.GO_TO_PG_REPORT_ORDER_VIEW;
        PGController.getInstance().handleSwitchFragment(e);
    }

    /**
     * Den man hinh ban hang PG
     */
    private void gotoPGOrderView() {
        ActionEvent e = new ActionEvent();
        e.sender = this;
        e.viewData = new Bundle();
        e.action = ActionEventConstant.GO_TO_PG_ORDER_VIEW;
        PGController.getInstance().handleSwitchFragment(e);
    }

    @Override
    public void handleModelViewEvent(ModelEvent modelEvent) {
        // TODO Auto-generated method stub
        ActionEvent e = modelEvent.getActionEvent();
        switch (e.action) {
            default:
                super.handleModelViewEvent(modelEvent);
                break;
        }
    }

    @Override
    public void handleErrorModelViewEvent(ModelEvent modelEvent) {
        ActionEvent e = modelEvent.getActionEvent();
        switch (e.action) {
            default:
                super.handleErrorModelViewEvent(modelEvent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
