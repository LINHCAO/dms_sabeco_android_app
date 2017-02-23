package com.viettel.dms.view.admin.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.admin.report.ReportView;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.sabeco.R;

/**
 * AdminNotificationList
 *
 * @author tuanlt
 * @since 11:43 AM 2/23/17
 */
public class AdminNotificationList extends BaseFragment {

    public static final String TAG = AdminNotificationList.class.getName(); //Tag
    public static AdminNotificationList newInstance() {
        AdminNotificationList f = new AdminNotificationList();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_report_view, container, false);
        View v = super.onCreateView(inflater, view, savedInstanceState);
        setTitleHeaderView(StringUtil.getString(R.string.TEXT_REPORT));
        return v;
    }
}
