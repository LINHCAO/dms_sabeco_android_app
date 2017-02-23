package com.viettel.dms.view.admin.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.sabeco.R;

/**
 * AdminNotificationListView
 *
 * @author tuanlt
 * @since 11:43 AM 2/23/17
 */
public class AdminNotificationListView extends BaseFragment {

    public static final String TAG = AdminNotificationListView.class.getName(); //Tag
    public static AdminNotificationListView newInstance() {
        AdminNotificationListView f = new AdminNotificationListView();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_admin_notification_list_view, container, false);
        View v = super.onCreateView(inflater, view, savedInstanceState);
        setTitleHeaderView(StringUtil.getString(R.string.TEXT_REPORT));
        return v;
    }
}
