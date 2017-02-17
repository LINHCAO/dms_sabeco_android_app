/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.controller;

/**
 * Controller giao tiep cua User
 *
 * @author: DoanDM
 * @version: 1.1
 * @since: 1.0
 */

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ErrorConstants;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.model.UserModel;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.ChangePasswordView;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.main.PGActivity;
import com.viettel.dms.view.main.PGChangePasswordView;
import com.viettel.dms.view.main.SalesPersonActivity;
import com.viettel.dms.view.main.SupervisorActivity;
import com.viettel.dms.view.main.TBHVActivity;
import com.viettel.dms.view.main.TNPGActivity;
import com.viettel.dms.view.sale.customer.CustomerInfoView;
import com.viettel.dms.view.sale.customer.CustomerLocationUpdateView;
import com.viettel.dms.view.sale.customer.CustomerRouteView;
import com.viettel.dms.view.sale.customer.NewCustomerListView;
import com.viettel.dms.view.sale.depot.ProductListView;
import com.viettel.dms.view.sale.depot.StatisticsTotalProductsView;
import com.viettel.dms.view.sale.image.FullImageView;
import com.viettel.dms.view.sale.indebtedness.IndebtednessListView;
import com.viettel.dms.view.sale.order.FindProductAddOrderListView;
import com.viettel.dms.view.sale.order.ListOrderView;
import com.viettel.dms.view.sale.order.OrderView;
import com.viettel.dms.view.sale.order.PaymentOrderBillingView;
import com.viettel.dms.view.sale.order.RemainProductView;
import com.viettel.dms.view.sale.promotion.DisplayProgramView;
import com.viettel.dms.view.sale.promotion.ListCustomerAttentProgram;
import com.viettel.dms.view.sale.salestatistics.SaleStatisticsAccumulateDayView;
import com.viettel.dms.view.sale.salestatistics.SaleStatisticsInDayPreSalesView;
import com.viettel.dms.view.sale.salestatistics.SaleStatisticsInDayVanSalesView;
import com.viettel.dms.view.sale.statistic.NVBHReportForcusProductView;
import com.viettel.dms.view.supervisor.GSBHRouteSupervisionView;
import com.viettel.dms.view.tnpg.order.TNPGTakeAttendanceView;
import com.viettel.viettellib.network.http.HTTPRequest;

/**
 * User Controller
 * UserController.java
 * @version: 1.0
 * @since: 08:22:47 20 Jan 2014
 */
public class UserController extends AbstractController {

    static UserController instance;

    protected UserController() {
    }

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    /**
     *
     * Xu ly cac request tu view
     *
     * @author: DoanDM
     * @param e
     * @return: void
     * @throws:
     */
    public void handleViewEvent(final ActionEvent e) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                HTTPRequest request = null;
                GlobalBaseActivity base = null;
                if (e.sender instanceof Activity) {
                    base = (GlobalBaseActivity) e.sender;
                } else if (e.sender instanceof Fragment) {
                    base = (GlobalBaseActivity) ((Fragment) e.sender).getActivity();
                }
                switch (e.action) {
                    case ActionEventConstant.ACTION_LOGIN:
                        request = UserModel.getInstance().requestLoginHTTP(e);
                        break;
                    case ActionEventConstant.RE_LOGIN:
                        request = UserModel.getInstance().requestLoginHTTP(e);
                        break;
                    case ActionEventConstant.ACTION_RETRY_REQUEST:
                    case ActionEventConstant.REQUEST_UPDATE_POSITION:
                        UserModel.getInstance().requestRetry(e);
                        break;
                    case ActionEventConstant.ACTION_GET_CURRENT_DATE_TIME_SERVER:
                        UserModel.getInstance().getCurrentDateTimeServer(e);
                        break;
                    case ActionEventConstant.UPDATE_NEW_URL_TO_DB:
                        UserModel.getInstance().requestUdateNewLink(e);
                        break;
                    case ActionEventConstant.UPLOAD_PHOTO_TO_SERVER:
                        UserModel.getInstance().requestUploadPhoto(e);
                        break;
                    case ActionEventConstant.RETRY_UPLOAD_PHOTO:
                        UserModel.getInstance().requestRetryUploadPhoto(e);
                        break;
                    case ActionEventConstant.ACTION_DELETE_OLD_LOG_TABLE:
                        UserModel.getInstance().requestDeleteOldLogTable(e);
                        break;
                    case ActionEventConstant.UPLOAD_DB_FILE: {
                        UserModel.getInstance().uploadDBFile(e);
                        break;
                    }
                    case ActionEventConstant.CHECK_USER_DB: {
                        UserModel.getInstance().requestCheckUserDB(e);
                        break;
                    }
                    case ActionEventConstant.ACTION_UPDATE_DELETED_DB: {
                        UserModel.getInstance().requestUpdateDeletedDB(e);
                        break;
                    }
                    case ActionEventConstant.NVBH_GET_REPORT_FORCUS_PRODUCT_INFO_VIEW: {
                        UserModel.getInstance().requestGetForcusProductInfo(e);
                        break;
                    }
                    case ActionEventConstant.CHANGE_PASS: {
                        UserModel.getInstance().requestChangePass(e);
                        break;
                    }
                    case ActionEventConstant.GET_STAFF_TYPE: {
                        UserModel.getInstance().requestStaffType(e);
                        break;
                    }
                    case ActionEventConstant.ACTION_UPDATE_POSITION: {
                        UserModel.getInstance().requestSendPositionLog(e);
                        break;
                    }
                    case ActionEventConstant.ACTION_INSERT_WORK_LOG: {
                        UserModel.getInstance().inserWorkLogTo(e);
                        break;
                    }
                    case ActionEventConstant.ACTION_DELETE_WORK_LOG: {
                        UserModel.getInstance().deleteWorkLog(e);
                        break;
                    }
                    default:// test
                        break;
                }
                if (request != null && base != null) {
                    base.addProcessingRequest(request, e.isBlockRequest);
                    e.request = request;
                }
                return null;
            }
        };
        task.execute();
    }

    /**
     *
     * Xu ly du lieu tra ve tu model
     *
     * @author: DoanDM
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
                            // TODO Auto-generated method stub
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
        Intent intent;
        Bundle extras;
        GlobalBaseActivity sender;

        switch (e.action) {
            case ActionEventConstant.GO_TO_SALE_PERSON_VIEW:
                sender = (GlobalBaseActivity) e.sender;
                extras = (Bundle) e.viewData;

                intent = new Intent(sender, SalesPersonActivity.class);
                if (extras != null) {
                    intent.putExtras(extras);
                }
                sender.startActivity(intent);
                break;
            case ActionEventConstant.GO_TO_SUPERVISOR_VIEW:
                sender = (GlobalBaseActivity) e.sender;
                extras = (Bundle) e.viewData;
                intent = new Intent(sender, SupervisorActivity.class);
                if (extras != null) {
                    intent.putExtras(extras);
                }
                sender.startActivity(intent);
                break;
            case ActionEventConstant.GO_TO_TBHV_VIEW:
                sender = (GlobalBaseActivity) e.sender;
                extras = (Bundle) e.viewData;
                intent = new Intent(sender, TBHVActivity.class);
                if (extras != null) {
                    intent.putExtras(extras);
                }
                sender.startActivity(intent);
                break;
            case ActionEventConstant.GO_TO_TNPG:
                sender = (GlobalBaseActivity) e.sender;
                extras = (Bundle) e.viewData;
                intent = new Intent(sender, TNPGActivity.class);
                if (extras != null) {
                    intent.putExtras(extras);
                }
                sender.startActivity(intent);
                break;
            case ActionEventConstant.GO_TO_PG:
                sender = (GlobalBaseActivity) e.sender;
                extras = (Bundle) e.viewData;
                intent = new Intent(sender, PGActivity.class);
                if (extras != null) {
                    intent.putExtras(extras);
                }
                sender.startActivity(intent);
                break;
        }
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
            case ActionEventConstant.GET_NEW_CUSTOMER_LIST: {
                NewCustomerListView frag = NewCustomerListView.getInstance((Bundle) e.viewData);
                switchFragment(fm, frag, NewCustomerListView.TAG, true);
                break;
            }
            case ActionEventConstant.GO_TO_LIST_ORDER: {
                ListOrderView listOrder = ListOrderView.newInstance(1);
                switchFragment(fm, listOrder, ListOrderView.TAG, true);
                break;
            }
            case ActionEventConstant.GO_TO_CUSTOMER_INFO: {
                CustomerInfoView cus = CustomerInfoView.newInstance((Bundle) e.viewData);
                switchFragment(fm, cus, CustomerInfoView.TAG, false);
                break;
            }
            case ActionEventConstant.GO_TO_REMAIN_PRODUCT_VIEW: {
                RemainProductView cus = RemainProductView.newInstance((Bundle) e.viewData);
                switchFragment(fm, cus, RemainProductView.TAG, false);
                break;
            }
            case ActionEventConstant.GOTO_CUSTOMER_LOCATION: {
                CustomerLocationUpdateView cus = CustomerLocationUpdateView.newInstance((Bundle) e.viewData);
                switchFragment(fm, cus, CustomerLocationUpdateView.TAG, false);
                break;
            }
            case ActionEventConstant.GO_TO_LIST_PRODUCTS_ADD_ORDER_LIST: {
                Bundle data = (Bundle) e.viewData;
                FindProductAddOrderListView findPro = FindProductAddOrderListView.getInstance(data);
                switchFragment(fm, findPro, FindProductAddOrderListView.TAG, false);
                break;
            }
            case ActionEventConstant.GO_TO_PAYMENT_ORDER_BILLING: {
                PaymentOrderBillingView paymentOrder = PaymentOrderBillingView.getInstance(1);
                switchFragment(fm, paymentOrder, PaymentOrderBillingView.TAG, false);
                break;
            }
            case ActionEventConstant.GO_TO_DISPLAY_PROGRAM: {
                DisplayProgramView paymentOrder = DisplayProgramView.getInstance(1);
                switchFragment(fm, paymentOrder, DisplayProgramView.TAG, true);
                break;
            }
            case ActionEventConstant.GO_TO_LIST_CUSTOMER_ATTEND_PROGRAM: {
                ListCustomerAttentProgram promotionDetail = ListCustomerAttentProgram.getInstance((Bundle) e.viewData);
                switchFragment(fm, promotionDetail, ListCustomerAttentProgram.TAG, false);
                break;
            }
            case ActionEventConstant.GO_TO_PRODUCT_LIST: {
                ProductListView listProduct = ProductListView.newInstance();
                switchFragment(fm, listProduct, ProductListView.TAG, true);
                break;
            }
            case ActionEventConstant.GO_TO_ORDER_VIEW: {
                OrderView genSta = OrderView.newInstance((Bundle) e.viewData);
                switchFragment(fm, genSta, OrderView.TAG, false);
                break;
            }
            case ActionEventConstant.ACTION_LOAD_IMAGE_FULL: {
                FullImageView genSta = FullImageView.newInstance((Bundle) e.viewData);
                switchFragment(fm, genSta, FullImageView.TAG, false);
                break;
            }
            case ActionEventConstant.GO_TO_STATISTICS_TOTAL_PRODUCT: {
                StatisticsTotalProductsView statisticsProduct = StatisticsTotalProductsView.getInstance();
                switchFragment(fm, statisticsProduct, StatisticsTotalProductsView.TAG, false);
                break;
            }
            case ActionEventConstant.GO_TO_INDEBTEDNESS_LIST_VIEW: {
                IndebtednessListView indebtednessList = IndebtednessListView.getInstance();
                switchFragment(fm, indebtednessList, IndebtednessListView.TAG, false);
                break;
            }

            case ActionEventConstant.GO_TO_CUSTOMER_ROUTE: {
                CustomerRouteView addCusFrag = CustomerRouteView.getInstance((Bundle) e.viewData);
                switchFragment(fm, addCusFrag, CustomerRouteView.TAG, true);
                break;
            }
            case ActionEventConstant.GET_GSNPP_ROUTE_SUPERVISION: {
                GSBHRouteSupervisionView saleRoad = GSBHRouteSupervisionView.newInstance();
                switchFragment(fm, saleRoad, GSBHRouteSupervisionView.TAG, true);
                break;
            }
            case ActionEventConstant.GO_TO_SALE_STATISTICS_PRODUCT_VIEW_IN_DAY: {
                if (GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_PRESALES) {
                    SaleStatisticsInDayPreSalesView genSta = SaleStatisticsInDayPreSalesView.getInstance(new Bundle());
                    switchFragment(fm, genSta, SaleStatisticsInDayPreSalesView.TAG, true);
                } else if (GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_VALSALES) {
                    SaleStatisticsInDayVanSalesView genSta = SaleStatisticsInDayVanSalesView.getInstance(new Bundle());
                    switchFragment(fm, genSta, SaleStatisticsInDayVanSalesView.TAG, true);
                }
                break;
            }
            case ActionEventConstant.GO_TO_SALE_STATISTICS_PRODUCT_VIEW_IN_MONTH: {
                SaleStatisticsAccumulateDayView genSta = SaleStatisticsAccumulateDayView.getInstance(new Bundle());
                switchFragment(fm, genSta, SaleStatisticsAccumulateDayView.TAG, true);
                break;
            }
            case ActionEventConstant.GO_TO_NVBH_REPORT_FORCUS_PRODUCT_VIEW: {
                NVBHReportForcusProductView cus = NVBHReportForcusProductView.getInstance((Bundle) e.viewData);
                switchFragment(fm, cus, NVBHReportForcusProductView.TAG, true);
                break;
            }
            case ActionEventConstant.CHANGE_PASS: {
                ChangePasswordView view = ChangePasswordView.newInstance();
                switchFragment(fm, view, ChangePasswordView.TAG, false);
                break;
            }
            case ActionEventConstant.GO_TO_TTTT_TAKE_ATTENDANCE: {
                TNPGTakeAttendanceView view = TNPGTakeAttendanceView.getInstance((Bundle) e.viewData);
                switchFragment(fm, view, TNPGTakeAttendanceView.TAG, false);
                break;
            }
            case ActionEventConstant.PG_CHANGE_PASS: {
                PGChangePasswordView view = PGChangePasswordView.newInstance();
                switchFragment(fm, view, PGChangePasswordView.TAG, false);
                break;
            }
        }
    }

}
