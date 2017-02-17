package com.viettel.dms.view.main;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.download.DownloadFile;
import com.viettel.dms.download.ExternalStorage;
import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ErrorConstants;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ServerLogger;
import com.viettel.dms.util.SqlCipherUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;
import com.viettel.utils.VTStringUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yennth16 on 8/19/2016.
 */
public class PGChangePasswordView extends BaseFragment implements View.OnFocusChangeListener {
    public static final String TAG = PGChangePasswordView.class.getName();
    private static final int ACTION_OK = 0;
    private static final int ACTION_CANCEL = -1;
    private GlobalBaseActivity parent; // parent
    private VNMEditTextClearable edMKcu;// edMKcu
    private VNMEditTextClearable edMKmoi;// edMKmoi
    private VNMEditTextClearable edMKxacnhan;// edMKxacnhan
    private TextView tvMaNV;// tvMaNV
    private TextView tvTenNV;// tvTenNV
    private TextView tvLoaiNV;// tvLoaiNV
    private Button btLuu;// btLuu
    private String newPass;
    private Button btSenDB;// gui db

    public static PGChangePasswordView newInstance() {
        PGChangePasswordView f = new PGChangePasswordView();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = (GlobalBaseActivity) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_pg_change_password_view, container, false);
        View v = super.onCreateView(inflater, view, savedInstanceState);
        tvMaNV = (TextView) v.findViewById(R.id.tvMaNV);
        tvTenNV = (TextView) v.findViewById(R.id.tvTenNV);
        tvLoaiNV = (TextView) v.findViewById(R.id.tvLoaiNV);
        edMKcu = (VNMEditTextClearable) v.findViewById(R.id.edMKcu);
        edMKmoi = (VNMEditTextClearable) v.findViewById(R.id.edMKmoi);
        edMKmoi.setOnFocusChangeListener(this);
        edMKxacnhan = (VNMEditTextClearable) v.findViewById(R.id.edMKxacnhan);
        edMKxacnhan.setOnFocusChangeListener(this);
        btLuu = (Button) v.findViewById(R.id.btLuu);
        btLuu.setOnClickListener(this);

        setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_USER_INFO));

        tvMaNV.setText(GlobalInfo.getInstance().getProfile().getUserData().userCode);
        tvTenNV.setText(GlobalInfo.getInstance().getProfile().getUserData().displayName);
        tvLoaiNV.setText(GlobalInfo.getInstance().getProfile().getUserData().channelTypeCode);
        btSenDB = (Button) v.findViewById(R.id.btSenDB);
        btSenDB.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        if (v == btLuu) {
            if (GlobalInfo.getInstance().getStateConnectionMode() == Constants.CONNECTION_ONLINE && isPassValid()) {
                if (GlobalUtil.checkNetworkAccess()) {
                    String textConfirm = StringUtil.getString(R.string.TEXT_CHANGE_PASS_CONFIRM);
                    GlobalUtil.showDialogConfirmCanBackAndTouchOutSide(this, parent, textConfirm,
                            StringUtil.getString(R.string.TEXT_AGREE), ACTION_OK,
                            StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), ACTION_CANCEL, -1, true, true);
                } else {
                    String mess = StringUtil.getString(R.string.TEXT_CHANGE_PASS_NO_CONNECTION);
                    GlobalUtil.showDialogConfirm(this, parent, mess, StringUtil.getString(R.string.TEXT_BUTTON_CLOSE),
                            -1, -1, false);
                }
            } else if(GlobalInfo.getInstance().getStateConnectionMode() == Constants.CONNECTION_OFFLINE ){
                parent.showDialog(StringUtil.getString(R.string.TEXT_PLEASE_LOGIN_ONLINE_TO_USING_THIS_FUNCTION));
            }
        } else if(v == btSenDB){
            String dbPath = ExternalStorage
                    .getDirDBPath(GlobalInfo.getInstance().getAppContext()).getAbsolutePath() + "/" + Constants.DATABASE_NAME;
            String decryptDbPath = ExternalStorage
                    .getDirDBPath(GlobalInfo.getInstance().getAppContext()).getAbsolutePath() + "/DMSDatabase"
                    + "_"
                    + GlobalInfo.getInstance().getProfile().getUserData().userCode
                    + "_" + DateUtils.nowStr(DateUtils.DATE_FORMAT_DATE_TIME_FILE);
            String zipFileDB = decryptDbPath + ".zip";
            new CipherTask().execute(dbPath, decryptDbPath, zipFileDB);
        } else {
            super.onClick(v);
        }
    }

    @Override
    public void onEvent(int eventType, View control, Object data) {
        switch (eventType) {
            case ACTION_OK:
                parent.showLoadingDialog();
                GlobalInfo.getInstance().getProfile().getUserData().saltChangePass = StringUtil.getRandomString(StringUtil.LENGTH_HASH);
                Vector<String> vt = new Vector<String>();
                vt.add(IntentConstants.INTENT_USER_NAME);
                vt.add(GlobalInfo.getInstance().getProfile().getUserData().userCode);
                vt.add(IntentConstants.NEW_PASS);
                vt.add(newPass);
                vt.add(IntentConstants.OLD_PASS);
                vt.add(edMKcu.getText().toString().trim());
                vt.add(IntentConstants.SALT_PASS);
                vt.add(GlobalInfo.getInstance().getProfile().getUserData().saltChangePass);
                ActionEvent e = new ActionEvent();
                e.viewData = vt;
                e.action = ActionEventConstant.CHANGE_PASS;
                e.sender = this;
                UserController.getInstance().handleViewEvent(e);
                break;

            default:
                super.onEvent(eventType, control, data);
                break;
        }
    }

    @Override
    public void handleModelViewEvent(ModelEvent modelEvent) {
        parent.closeProgressDialog();
        ActionEvent e = modelEvent.getActionEvent();
        switch (e.action) {
            case ActionEventConstant.GET_STAFF_TYPE:
                if (modelEvent.getModelData() != null) {
                    tvLoaiNV.setText((String) modelEvent.getModelData());
                } else {
                    tvLoaiNV.setText("");
                }
                break;
            case ActionEventConstant.CHANGE_PASS:
                parent.showDialog(StringUtil.getString(R.string.TEXT_CHANGE_PASS_SUCCEEDED));
                try {
                    GlobalInfo.getInstance().getProfile().getUserData().salt = GlobalInfo.getInstance().getProfile().getUserData().saltChangePass;
                    GlobalInfo.getInstance().getProfile().getUserData().saltChangePass = "";
                    GlobalInfo.getInstance().getProfile().getUserData().pass = StringUtil.generateHashSHA256(newPass,
                            GlobalInfo.getInstance().getProfile().getUserData().salt.toLowerCase(),
                            GlobalInfo.getInstance().getTypeHashPass());
                    SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
                    SharedPreferences.Editor prefsPrivateEditor = sharedPreferences.edit();
                    prefsPrivateEditor.putString(LoginView.DMS_PASSWORD, GlobalInfo.getInstance().getProfile()
                            .getUserData().pass);
                    prefsPrivateEditor.putString(LoginView.DMS_SALT, GlobalInfo.getInstance().getProfile()
                            .getUserData().salt);
                    prefsPrivateEditor.commit();
                } catch (NoSuchAlgorithmException e1) {
                } catch (UnsupportedEncodingException e1) {
                }
                resetValues();
                break;

            default:
                super.handleModelViewEvent(modelEvent);
                break;
        }
    }

    @Override
    public void handleErrorModelViewEvent(ModelEvent modelEvent) {
        ActionEvent e = modelEvent.getActionEvent();
        switch (e.action) {
            case ActionEventConstant.CHANGE_PASS:
                parent.closeProgressDialog();
                GlobalInfo.getInstance().getProfile().getUserData().saltChangePass = "";
                switch (modelEvent.getModelCode()) {
                    case ErrorConstants.ERROR_SESSION_RESET:
                        parent.showDialog("Vui lòng đăng nhập lại trước khi thực hiện chức năng này!");
                        break;
                    default:
                        if(modelEvent != null && !StringUtil.isNullOrEmpty(modelEvent.getModelMessage())){
                            parent.showDialog(modelEvent.getModelMessage());
                        }else{
                            parent.showDialog("Lỗi trong quá trình xử lý, vui lòng thử lại");
                        }
                        break;
                }
                break;
            default:
                super.handleErrorModelViewEvent(modelEvent);
                break;
        }

    }

    /**
     * Mo ta muc dich cua ham
     *
     * @author: TamPQ
     * @return: voidvoid
     * @throws:
     */
    private void resetValues() {
        edMKcu.setText("");
        edMKmoi.setText("");
        edMKxacnhan.setText("");
        edMKcu.requestFocus();
    }

    /**
     * Mo ta muc dich cua ham
     *
     * @author: TamPQ
     * @return
     * @return: booleanvoid
     * @throws:
     */
    private boolean isPassValid() {
        boolean isValid = true;
        String pass = GlobalInfo.getInstance().getProfile().getUserData().pass;

        String oldPass = null;
        try {
            oldPass = StringUtil.generateHashSHA256(edMKcu.getText().toString().trim(),
                    GlobalInfo.getInstance().getProfile().getUserData().salt.toLowerCase(),
                    GlobalInfo.getInstance().getTypeHashPass());
        } catch (NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException e) {
        }

        newPass = edMKmoi.getText().toString().trim();
        String confirmPass = edMKxacnhan.getText().toString().trim();
        if (StringUtil.isNullOrEmpty(edMKcu.getText().toString().trim())) {
            isValid = false;
            parent.showDialog(StringUtil.getString(R.string.TEXT_PLEASE_INPUT) + " "
                    + StringUtil.getString(R.string.TEXT_OLD_PASS));
            edMKcu.requestFocus();
        } else if (StringUtil.isNullOrEmpty(newPass)) {
            isValid = false;
            parent.showDialog(StringUtil.getString(R.string.TEXT_PLEASE_INPUT) + " "
                    + StringUtil.getString(R.string.TEXT_NEW_PASS));
            edMKmoi.requestFocus();
        } else if (StringUtil.isNullOrEmpty(confirmPass)) {
            isValid = false;
            parent.showDialog(StringUtil.getString(R.string.TEXT_PLEASE_INPUT) + " "
                    + StringUtil.getString(R.string.TEXT_CONFIRMED_PASS));
            edMKxacnhan.requestFocus();
        } else if (!oldPass.equals(pass)) {
            isValid = false;
            parent.showDialog(StringUtil.getString(R.string.TEXT_WRONG_OLD_PASS));
            resetValues();
        } else if (!newPass.equals(confirmPass)) {
            isValid = false;
            parent.showDialog(StringUtil.getString(R.string.TEXT_WRONG_CONFIRMED_PASS));
            resetValues();
            // resetValues();
        } else if (newPass.length() < 6 || newPass.length() > 16) {
            isValid = false;
            parent.showDialog(StringUtil.getString(R.string.TEXT_PASS_6_16));
            resetValues();
        }

        return isValid;
    }

    @Override
    public void onFocusChange(View arg0, boolean arg1) {
        if (arg0 == edMKmoi && !arg1) {
            checkStrongPassword(edMKmoi);
        } else if (arg0 == edMKxacnhan && !arg1) {
            checkStrongPassword(edMKxacnhan);
        }

    }

    /**
     * kiem tra mat khau manh
     * @author: hoanpd1
     * @since: 09:34:06 28-08-2014
     * @return: void
     * @throws:
     */
    private void checkStrongPassword(VNMEditTextClearable edMK) {
        String dateTimePattern = VTStringUtils.getString(parent,
                R.string.TEXT_STRONG_PASSWORD_PATTERN);
        Pattern pattern = Pattern.compile(dateTimePattern);
        if (!VTStringUtils.isNullOrEmpty(edMK.getText().toString())) {
            String strTN = edMK.getText().toString().trim();
            Matcher matcher = pattern.matcher(strTN);
            if (!matcher.matches()) {
                parent.showDialog(VTStringUtils.getString(parent,
                        R.string.TEXT_STRONG_PASSWORD_SYNTAX_ERROR));
                return;
            }
        }

    }

    private class CipherTask extends AsyncTask<String, Void, Exception> {
        //file zip db gui ve he thong
        String zipFileDB;

        @Override
        protected void onPreExecute() {
            parent.showProgressDialog(StringUtil.getString(R.string.TEXT_REPARE_SEND_DATA_WAIT_A_BIT));
        }

        @Override
        protected Exception doInBackground(String... params) {
            File bkFile = null;
            String dbPath = params[0];
            String dbPathBK = dbPath + "_" + System.currentTimeMillis();
            String decryptDbPath = params[1];
            zipFileDB = params[2];
            try {
                bkFile = new File(dbPathBK);
                //copy file db hiện tại ra, tránh tranh chấp
                DownloadFile.copy(new File(dbPath), bkFile);
                //giải mã db backup
                SqlCipherUtil.decryptCiphertextDB(bkFile, decryptDbPath);
                //nén file db mới giải mã
                DownloadFile.zipFileWithPass(decryptDbPath, zipFileDB, Constants.CIPHER_KEY);
            } catch (Exception e) {
                return e;
            } finally{
                if (bkFile != null) {
                    try {
                        bkFile.delete();
                    } catch (Exception e2) {}
                }
                try {
                    if (!StringUtil.isNullOrEmpty(decryptDbPath)) {
                        File filedecrypt = new File(decryptDbPath);
                        if (filedecrypt.exists()) {
                            filedecrypt.delete();
                        }
                    }
                } catch (Exception e2) {}
            }
            return null;
        }

        @Override
        protected void onPostExecute(Exception result) {
            parent.closeProgressDialog();
            if (result != null) {
                parent.showDialog(StringUtil.getString(R.string.TEXT_DATA_SEND_FAIL_TRY_AGAIN));
                ServerLogger.sendLog("SQLiteCipher","Giai nen db: "
                                + VNMTraceUnexceptionLog.getReportFromThrowable(result), true,
                        TabletActionLogDTO.LOG_EXCEPTION);
                VTLog.e("SQLiteCipher", "Giai nen db: " + VNMTraceUnexceptionLog.getReportFromThrowable(result));
                return;
            } else{
                if(zipFileDB != null){
                    parent.showToastMessage(StringUtil.getString(R.string.TEXT_DATA_SEND_MAIL));
                    String userCode = GlobalInfo.getInstance().getProfile().getUserData().userCode;
                    String subject = "SABECO_" + userCode + "_DB_"
                            + DateUtils.getCurrentDateTimeWithFormat("dd_MM_yyyy");
                    StringBuffer content = new StringBuffer();
                    content.append("Thân gửi \n");
                    content.append("Nhân viên: " + userCode);
                    content.append(" gửi dữ liệu phân tích " + DateUtils.nowVN());
                    content.append("\nThân chào!");
                    try {
                        String email = "dmsviettel@gmail.com";
                        if(!StringUtil.isNullOrEmpty(GlobalInfo.getInstance().getEmailSendData())){
                            email = GlobalInfo.getInstance().getEmailSendData();
                        }
                        Intent mail = GlobalUtil.getIntentSendMail(subject,
                                new String[] {email}, content.toString(), zipFileDB);
                        parent.startActivity(mail);
                    } catch (ActivityNotFoundException e) {
                        parent.showDialog(StringUtil.getString(R.string.TEXT_SEND_MAIL_FAIL_NO_APP));
                        ServerLogger.sendLog("SQLiteCipher",
                                "Gửi mail db, không có ứng dụng: "+ VNMTraceUnexceptionLog.getReportFromThrowable(result), true, TabletActionLogDTO.LOG_EXCEPTION);
                        VTLog.e("SQLiteCipher", "Gửi mail db, không có ứng dụng: " + VNMTraceUnexceptionLog.getReportFromThrowable(result));
                    } catch (Exception e) {
                        ServerLogger.sendLog("SQLiteCipher",
                                "Gửi mail db: "+ VNMTraceUnexceptionLog.getReportFromThrowable(result), true, TabletActionLogDTO.LOG_EXCEPTION);
                        VTLog.e("SQLiteCipher", "Gửi mail db: " + VNMTraceUnexceptionLog.getReportFromThrowable(result));
                        parent.showDialog(StringUtil.getString(R.string.TEXT_SEND_MAIL_FAIL_TRY_AGAIN));
                    }
                }
            }

        }
    }
}
