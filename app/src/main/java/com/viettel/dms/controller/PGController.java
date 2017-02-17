package com.viettel.dms.controller;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ErrorConstants;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.model.PGModel;
import com.viettel.dms.model.SaleModel;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.pg.PGGeneralStatisticView;
import com.viettel.dms.view.pg.PGOrderView;
import com.viettel.dms.view.pg.PGReportOrderView;
import com.viettel.viettellib.network.http.HTTPRequest;

/**
 * Created by yennth16 on 8/11/2016.
 */
public class PGController extends AbstractController {

    static PGController instance;

    protected PGController() {
    }

    public static PGController getInstance() {
        if (instance == null) {
            instance = new PGController();
        }
        return instance;
    }

    /**
     * Xu ly cac request tu view
     * @author: BangHN
     * @param e
     * @return: void
     * @throws:
     */
    public void handleViewEvent(final ActionEvent e) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                switch (e.action) {
                    case ActionEventConstant.GET_PG_ORDER_VIEW:
                        PGModel.getInstance().requestGetOrderView(e);
                        break;
                    case ActionEventConstant.CREATE_NEW_ORDER:
                        PGModel.getInstance().requestCreateOrder(e);
                        break;
                    case ActionEventConstant.GET_PG_REPORT_ORDER_VIEW:
                        PGModel.getInstance().requestGetReportOrderView(e);
                        break;
                    default:
                        break;
                }
                return null;
            }
        };
        task.execute();
    }

    /**
     * Xu ly du lieu tra ve tu model
     * @author: BangHN
     * @param modelEvent
     * @return: void
     * @throws:
     */
    public void handleModelEvent(final ModelEvent modelEvent) {
        if (modelEvent.getModelCode() == ErrorConstants.ERROR_CODE_SUCCESS) {
            final ActionEvent e = modelEvent.getActionEvent();
            HTTPRequest request = e.request;
            if (e.sender != null && (request == null || (request != null && request.isAlive()))) {
                if (e.sender instanceof GlobalBaseActivity) {
                    final GlobalBaseActivity sender = (GlobalBaseActivity) e.sender;
                    if (sender.isFinished)
                        return;
                    sender.runOnUiThread(new Runnable() {
                        public void run() {
                            // TODO Auto-generated method stub
                            sender.handleModelViewEvent(modelEvent);
                        }
                    });
                } else if (e.sender instanceof BaseFragment) {
                    final BaseFragment sender = (BaseFragment) e.sender;
                    if (sender.getActivity() == null || sender.isFinished) {
                        return;
                    }
                    sender.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            sender.handleModelViewEvent(modelEvent);
                        }
                    });
                }
            } else {
                modelEvent.setIsSendLog(false);
                handleErrorModelEvent(modelEvent);
            }
        } else {
            handleErrorModelEvent(modelEvent);
        }
    }

    @Override
    public void handleSwitchActivity(ActionEvent e) {
    }

    @Override
    public void handleSwitchFragment(ActionEvent e) {
        Activity base = null;
        if (e.sender instanceof Activity) {
            base = (Activity) e.sender;
        } else if (e.sender instanceof Fragment) {
            base = ((Fragment) e.sender).getActivity();
        }
        if (base != null)
            GlobalUtil.forceHideKeyboard(base);

        FragmentManager fm = base.getFragmentManager();
        switch (e.action) {
            case ActionEventConstant.GO_TO_PG_REPORT_ORDER_VIEW: {
                PGReportOrderView frg = PGReportOrderView.getInstance((Bundle)e.viewData);
                switchFragment(fm, frg, PGReportOrderView.TAG, true);
                break;
            }
            case ActionEventConstant.GO_TO_PG_ORDER_VIEW: {
                PGOrderView genSta = PGOrderView.getInstance((Bundle)e.viewData);
                switchFragment(fm, genSta, PGOrderView.TAG, true);
                break;
            }
        }
    }

}
