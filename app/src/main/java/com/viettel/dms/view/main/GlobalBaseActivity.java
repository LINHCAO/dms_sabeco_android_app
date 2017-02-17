/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.main;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.commonsware.cwac.cache.HashMapManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.code.microlog4android.appender.LogCatAppender;
import com.google.code.microlog4android.config.PropertyConfigurator;
import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.PGController;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.SynDataController;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.download.ExternalStorage;
import com.viettel.dms.dto.db.ActionLogDTO;
import com.viettel.dms.dto.db.ApParamDTO;
import com.viettel.dms.dto.db.LogDTO;
import com.viettel.dms.dto.db.ProInfoDTO;
import com.viettel.dms.dto.db.SaleOrderDTO;
import com.viettel.dms.dto.db.ShopParamDTO;
import com.viettel.dms.dto.db.StaffPositionLogDTO;
import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.dto.db.WorkLogDTO;
import com.viettel.dms.dto.me.NotifyOrderDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.syndata.SynDataDTO;
import com.viettel.dms.dto.view.CustomerListItem;
import com.viettel.dms.dto.view.NoSuccessSaleOrderDto;
import com.viettel.dms.dto.view.SaleSupportProgramModel;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ErrorConstants;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.GlobalInfo.RightTimeInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.locating.PositionManager;
import com.viettel.dms.sqllite.db.SQLUtils;
import com.viettel.dms.util.AsyncTaskUtil;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.LocationService;
import com.viettel.dms.util.LogFile;
import com.viettel.dms.util.ServerLogger;
import com.viettel.dms.util.ServiceUtil;
import com.viettel.dms.util.StatusNotificationHandler;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.TransactionProcessManager;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.util.guard.AccessInternetService;
import com.viettel.dms.util.guard.ApplicationReceiver;
import com.viettel.dms.util.guard.MockLocationProvider;
import com.viettel.dms.view.control.RightHeaderMenuBar;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.listener.OnDialogControlListener;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.pg.PGOrderView;
import com.viettel.dms.view.pg.PGReportOrderView;
import com.viettel.dms.view.sale.customer.PostFeedbackView;
import com.viettel.dms.view.sale.image.ImageListView;
import com.viettel.dms.view.sale.image.ListAlbumUserView;
import com.viettel.dms.view.sale.order.ListOrderView;
import com.viettel.dms.view.sale.order.OrderView;
import com.viettel.dms.view.sale.promotion.CustomerAttendProgramListView;
import com.viettel.dms.view.sale.promotion.CustomerProgrameDoneView;
import com.viettel.dms.view.sale.promotion.PromotionProgramView;
import com.viettel.dms.view.sale.promotion.SaleSupportProgramDetailView;
import com.viettel.dms.view.supervisor.FollowProblemView;
import com.viettel.dms.view.supervisor.GSNPPPostFeedbackView;
import com.viettel.dms.view.supervisor.SupervisorListAlbumUserView;
import com.viettel.dms.view.supervisor.TrackAndFixProblemsOfGSNPPView;
import com.viettel.dms.view.supervisor.collectinformation.CustomerC2ListView;
import com.viettel.dms.view.supervisor.collectinformation.CustomerOfC2ListView;
import com.viettel.dms.view.supervisor.image.SupervisorImageListView;
import com.viettel.dms.view.supervisor.training.ReviewsStaffView;
import com.viettel.dms.view.tbhv.TBHVAddRequirementView;
import com.viettel.dms.view.tbhv.TBHVFollowProblemView;
import com.viettel.dms.view.tbhv.TBHVReviewsGSNPPView;
import com.viettel.dms.view.tnpg.statistic.TNPGGeneralStatisticView;
import com.viettel.sabeco.R;
import com.viettel.utils.ImageValidatorTakingPhoto;
import com.viettel.utils.VTLog;
import com.viettel.utils.VTStringUtils;
import com.viettel.utils.locating.VTLocating;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.network.http.HTTPClient;
import com.viettel.viettellib.network.http.HTTPRequest;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * Base activity
 * GlobalBaseActivity.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:02:08 14 Jan 2014
 */
public class GlobalBaseActivity extends Activity implements
		OnCompletionListener, OnDialogControlListener, OnEventControlListener,
		OnClickListener, OnCancelListener {
	public static final String LAST_LOG_ID = "com.viettel.dms.lastLogId";
	public static final String DATE_CLEAR_OLD_DATA = "com.viettel.sabeco.dateClearData";
	// chuoi action cua cac broadcast message
	public static final String DMS_ACTION = "com.viettel.sabeco.BROADCAST";
	// sync du lieu khi ung dung dang inactive
	public static final int REQUEST_SYNC_FROM_INACTIVE_APP = 0;
	// sync du lieu khi nguoi dung thao tac
	public static final int REQUEST_SYNC_FROM_USER = 1;

	// thoi gian rung khi co notification, chat
	private static final int VIBRATION_DURATION = 500;
	// take photo
	public static final int RQ_TAKE_PHOTO = 999;
	public static final int RQ_TAKE_PHOTO_VOTE_DP = 998;
	public static final int RQ_SUPERVISOR_TAKE_PHOTO = 997;

	// before re-login
	public static final int MENU_FINISH_VISIT = 1001;
	public static final int MENU_FINISH_VISIT_CLOSE = 1002;
	// action dong y ra lai man hinh login
	public final int ACTION_SHOW_LOGIN = 1003;
	// action dong y dang nhap lai de tai du lieu
	final int ACTION_OK_RESET_DB = 1004;
	final int ACTION_CANCEL_RESET_DB = 1005;

	// check mock location
	public final static int ACTION_ALLOW_MOCK_LOCATION_OK = 1007;
	public final static int ACTION_ALLOW_MOCK_LOCATION_CANCEL = 1008;
	public final int ACTION_SELECTED_APP_DELETE = 1009;
	// xoa du lieu ung dung
	private final int ACTION_CLEAR_DATA_OK = 1010;
	private final int ACTION_CLEAR_DATA_CANCEL = 1011;

	// kiem tra activity da finish hay chua
	public boolean isFinished = false;
	private boolean blReLogin = false; // check re-login one time
	public ActionEvent actionEventBeforReLogin = null; // save actionevent
	// mang cac request dang xu ly do activity request
	private ArrayList<HTTPRequest> unblockReqs = new ArrayList<HTTPRequest>();
	private ArrayList<HTTPRequest> blockReqs = new ArrayList<HTTPRequest>();
	protected final int ICON_RIGHT_PARENT = 0;
	protected final int ICON_NEXT_1 = 1;
	protected final int ICON_BUTTON = -1;
	public static final int RESEND_TYPE = -1;
	private LinearLayout rootView;
	protected LinearLayout mainContent;
	protected LinearLayout llShowHideMenu;// layout chua icon an hien menu
	protected LinearLayout llMenubar;// layout menu icon ben phai
	protected LinearLayout llMenuUpdate;// menu update du lieu tu server
	protected LinearLayout llStatus;// layout chua trang thai
	protected LinearLayout llWarningInputQuantity;// canh bao co chuong trinh can nhap san luong ban
	protected LinearLayout llShopSelected; // hien thi shop quan ly cho giam sat
	protected TextView tvStatus;// text trang thai
	protected ImageView ivClearData;// clear data app
	// dialog hien thi khi request
	private static ProgressDialog progressDlg;
	// progressBar tren header
	private ProgressBar progressBar;
	// header kunkun
	private View rl_header;
	private LinearLayout llMenuWarning;// logo Warning
	ArrayList<Long> listOrderId = new ArrayList<Long>();// ds orderId fail
	// private EditText tvLocation;
	// co nhan broadcast hay khong
	// private boolean broadcast;
	// menu icon goc phai trong activity
	public RightHeaderMenuBar menuBar;
	// thuc hien hieu ung rung khi co notification
	private Vibrator vibrator;
	// am thanh khi co notification, chat
	private MediaPlayer soundPlayer;
	private int synType;
	// cancel request udate du lieu giua chung hay khong
	private boolean isCancelUpdateData = false;
	// dang insert action log?
	public boolean isInsertingActionLogVisit = false;
	// control date picker
	public DatePickerDialog datePickerDialog;
	// day for dialog date picker
	public int day = 0;
	// month for dialog date picker
	public int month = 0;
	// year for dialog date picker
	public int year = 0;

	public String fragmentTag = "";
	public static final int DATE_DIALOG_ID = 0;
	public static final int TIME_DIALOG_ID = 1;

	private Bundle trainingReviewStaff;
	//dung tinh % hoan thanh dong bo
	long beginLogId;
	//relogin khi mat session
	int numRelogin = 0;
	private boolean isLoadedNativeLib = false;// load lib dong
	// ds cac ung dung can phai xoa
	ArrayList<ApplicationInfo> blackApps = new ArrayList<ApplicationInfo>();
	PackageManager packageManager = null;
	// alert cac ung dung can xoa
	private AlertDialog alertBackListAppDialog;
	// view hien thi ds cac ung dung can xoa
	BlackListAppView blackListView;
	Dialog dialogCheckGooglePlay = null;
	// Danh sach chuong trinh can cap nhat san luong ban
	protected SaleSupportProgramModel listProgrameTypeQuantity = null;
	private long startTimeSynDataFromBoot = 0;
	protected LinearLayout llMenuGPS;
	CountDownTimer timerGPSRequest;
	private boolean isFirstCallDialogGPS = true;

	public void setTrainingReviewStaffBundle(Bundle bundle) {
		this.trainingReviewStaff = bundle;
	}

	public Bundle getTrainingReviewStaffBundle() {
		return this.trainingReviewStaff;
	}

	// broadcast receiver, nhan broadcast
	BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int action = intent.getExtras().getInt(Constants.ACTION_BROADCAST);
			int hasCode = intent.getExtras().getInt(
					Constants.HASHCODE_BROADCAST);
			if (hasCode != GlobalBaseActivity.this.hashCode()) {
				receiveBroadcast(action, intent.getExtras());
			}
		}
	};
	public File takenPhoto;
	private AlertDialog alertRemindDialog;
	private TextView tvTitle;
	private ImageView ivLogo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isFirstCallDialogGPS = true;
		SQLiteDatabase.loadLibs(this);
//		 checkUnCaughtExeptionFile(this);
		isLoadedNativeLib = false;
		loadNativeLib();
		PropertyConfigurator.getConfigurator(this).configure();
		if (VTLog.logger.getNumberOfAppenders() == 0) {
			VTLog.logger.addAppender(new LogCatAppender());
		}
		GlobalInfo.getInstance().setActivityContext(this);
//		GlobalInfo.getInstance().setAppContext(this);
		if (savedInstanceState != null) {
			if (savedInstanceState
					.containsKey(IntentConstants.INTENT_GLOBAL_BUNDLE)) {
				getIntent()
						.putExtras(
								savedInstanceState
										.getBundle(IntentConstants.INTENT_GLOBAL_BUNDLE));
			}
			if (savedInstanceState != null) {
				if (savedInstanceState
						.containsKey(IntentConstants.INTENT_GLOBAL_BUNDLE)) {
					getIntent()
							.putExtras(
									savedInstanceState
											.getBundle(IntentConstants.INTENT_GLOBAL_BUNDLE));
				}
				if (StringUtil.isNullOrEmpty(HTTPClient.sessionID)
						&& savedInstanceState
								.containsKey(IntentConstants.INTENT_SESSION_ID)) {
					HTTPClient.sessionID = savedInstanceState
							.getString(IntentConstants.INTENT_SESSION_ID);
				}
			}
		}
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		try {
			IntentFilter filter = new IntentFilter(DMS_ACTION);
			registerReceiver(receiver, filter);
			// this.broadcast = true;
			Thread.setDefaultUncaughtExceptionHandler(new VNMTraceUnexceptionLog(
					this));
		} catch (Exception e) {
		}
		packageManager = getPackageManager();

		//hien thi thong bao dinh vi
		if (!(this instanceof LoginView)){
			new NotifyLocation().execute();
		}
	}

	/**
	 * 
	 * ham onCreate voi tham so co dang ky lang nghe broadcast khong
	 * 
	 * @author: Unknown
	 * @param savedInstanceState
	 * @param broadcast
	 * @return: void
	 * @throws:
	 */
	protected void onCreate(Bundle savedInstanceState, boolean broadcast) {
		super.onCreate(savedInstanceState);
		isLoadedNativeLib = false;
		loadNativeLib();
		// PropertyConfigurator.getConfigurator(this).configure();
		// if (VTLog.logger.getNumberOfAppenders() == 0) {
		// VTLog.logger.addAppender(new LogCatAppender());
		// }
//		GlobalInfo.getInstance().setAppContext(getApplicationContext());
		GlobalInfo.getInstance().setActivityContext(this);
		if (savedInstanceState != null) {
			if (savedInstanceState
					.containsKey(IntentConstants.INTENT_GLOBAL_BUNDLE)) {
				getIntent()
						.putExtras(
								savedInstanceState
										.getBundle(IntentConstants.INTENT_GLOBAL_BUNDLE));
			}
			if (savedInstanceState != null) {
				if (savedInstanceState
						.containsKey(IntentConstants.INTENT_GLOBAL_BUNDLE)) {
					getIntent()
							.putExtras(
									savedInstanceState
											.getBundle(IntentConstants.INTENT_GLOBAL_BUNDLE));
				}
				if (StringUtil.isNullOrEmpty(HTTPClient.sessionID)
						&& savedInstanceState
								.containsKey(IntentConstants.INTENT_SESSION_ID)) {
					HTTPClient.sessionID = savedInstanceState
							.getString(IntentConstants.INTENT_SESSION_ID);
					// KunKunUtils.reloginXMPP(this);
				}
			}
		}

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		// this.broadcast = broadcast;
		if (broadcast) {
			try {
				IntentFilter filter = new IntentFilter(DMS_ACTION);
				registerReceiver(receiver, filter);

				Thread.setDefaultUncaughtExceptionHandler(new VNMTraceUnexceptionLog(
						this));
			} catch (Exception e) {
			}
		}
		packageManager = getPackageManager();
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(R.layout.layout_global_main);
		rootView = (LinearLayout) findViewById(R.id.ll_global_main);
		mainContent = (LinearLayout) findViewById(R.id.ll_content);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(layoutResID, mainContent);

		initialize();
		// setTitleName("Vinamilk");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (this == GlobalInfo.getInstance().lastActivity) {
			GlobalInfo.getInstance().lastActivity = null;
		}
		try {
			unregisterReceiver(receiver);
			if(progressDlg != null) { progressDlg.dismiss();}
			if(progressDlgGPSLocating != null) { progressDlgGPSLocating.dismiss();}
			if (dialogCheckGooglePlay != null && dialogCheckGooglePlay.isShowing()) {
				dialogCheckGooglePlay.dismiss();
			}
		} catch (Exception e) {
		}
		GlobalUtil.nullViewDrawablesRecursive(rootView);
		HashMapManager.getInstance().clearHashMapById(this.toString());
		System.gc();
		System.runFinalization();
		super.onDestroy();
	}

	@Override
	public void finish() {
		isFinished = true;
		super.finish();
	}

	@Override
	public void finishActivity(int requestCode) {
		isFinished = true;
		super.finishActivity(requestCode);
	}

	

	/**
	 * Init view base activity
	 * @author : BangHN since : 1.0
	 */
	private void initialize() {
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rl_header = inflater.inflate(R.layout.global_header, null);
		rootView.addView(rl_header, 0);

		// Cap nhat hien thi logo loading
		progressBar = (ProgressBar) rl_header.findViewById(R.id.pb_global);
		llShowHideMenu = (LinearLayout) rl_header
				.findViewById(R.id.llShowHideMenu);
		llMenubar = (LinearLayout) rl_header.findViewById(R.id.llMenubar);
		llMenuUpdate = (LinearLayout) rl_header.findViewById(R.id.llMenuUpdate);
		llMenuGPS = (LinearLayout) rl_header.findViewById(R.id.llMenuGPS);
		llStatus = (LinearLayout) rl_header.findViewById(R.id.llStatus);
		tvStatus = (TextView) rl_header.findViewById(R.id.tvStatus);
		tvStatus.setOnClickListener(this);
		ivClearData =(ImageView) rl_header.findViewById(R.id.ivClearData);
		ivClearData.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clearData();
			}
		});
		tvTitle = (TextView) rl_header.findViewById(R.id.tvTitle);
		if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType != UserDTO.TYPE_PG) {
			tvTitle.setOnClickListener(this);
		}
		ivLogo = (ImageView) rl_header.findViewById(R.id.ivLogo);
		ivLogo.setOnClickListener(this);
		llMenuWarning = (LinearLayout) rl_header
				.findViewById(R.id.llMenuWarning);
		// tvLocation = (EditText) rl_header.findViewById(R.id.tvLocation);

		llMenuGPS.setVisibility(View.GONE);
		llMenuGPS.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isFirstCallDialogGPS = false;
				restartLocating(true);
			}
		});

		llMenuUpdate.setVisibility(View.GONE);
		llMenuUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (GlobalInfo.getInstance().getStateConnectionMode() == Constants.CONNECTION_OFFLINE) {
					showToastMessage(StringUtil.getString(R.string.TEXT_INFORM_USING_LOGIN_OFFLINE));
				} else if (progressDlg == null
						|| (progressDlg != null && !progressDlg.isShowing())) {
					//nếu số lượng thread ko cho thực thi nữa
					if (!AsyncTaskUtil.checkAllowDoTask()) {
						showToastMessage(StringUtil.getString(R.string.TEXT_APP_BUSY_WAIT_A_BIT), Toast.LENGTH_LONG);
						//ghi log
						ServerLogger.sendLog(StringUtil.getString(R.string.TEXT_OVER_LOAD_THREAD),
								AsyncTaskUtil.getThreadInfo() + " " + DateUtils.getRightTimeNow(), TabletActionLogDTO.LOG_CLIENT);
					} else {
						//nếu còn cho phép AsynTask thì cho phép request syndata
						showProgressDialog(StringUtil.getString(R.string.updating));
						if (GlobalUtil.checkNetworkAccess()) {
							TransactionProcessManager.getInstance().resetChecking(
									TransactionProcessManager.SYNC_FROM_UPDATE);
							synType = REQUEST_SYNC_FROM_USER;

						} else {
							startTimeSynDataFromBoot = SystemClock.elapsedRealtime();
							requestSynData(REQUEST_SYNC_FROM_USER);
						}
					}
				}
			}
		});

		llMenuWarning.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// danh sach order_id : listOrderId
				if (!AsyncTaskUtil.checkAllowDoTask() ||
						GlobalInfo.getInstance().stateSynData == GlobalInfo.SYNDATA_EXECUTING) {
					showToastMessage(StringUtil.getString(R.string.TEXT_APP_BUSY_WAIT_A_BIT), Toast.LENGTH_LONG);
					//ghi log
					ServerLogger.sendLog(StringUtil.getString(R.string.TEXT_OVER_LOAD_THREAD),
							AsyncTaskUtil.getThreadInfo() + " " + DateUtils.getRightTimeNow(), TabletActionLogDTO.LOG_CLIENT);
				} else {
					if (progressDlg == null
							|| (progressDlg != null && !progressDlg.isShowing())) {
						if (GlobalInfo.getInstance().notifyOrderReturnInfo.isSyncDataFromServer) {
							// neu can update du lieu tu server thi update truoc khi
							// hien thi popup
							showProgressDialog(StringUtil
									.getString(R.string.updating));
							if (GlobalUtil.checkNetworkAccess()) {
								TransactionProcessManager
										.getInstance()
										.resetChecking(
												TransactionProcessManager.SYNC_FROM_UPDATE);
								synType = REQUEST_SYNC_FROM_INACTIVE_APP;
							} else {
								startTimeSynDataFromBoot = SystemClock.elapsedRealtime();
								requestSynData(REQUEST_SYNC_FROM_INACTIVE_APP);
							}
						} else {
							showProgressDialog(StringUtil
									.getString(R.string.loading));
							requestGetAllOrderFail();
						}
					}
				}
			}
		});

		llShopSelected = (LinearLayout) rl_header.findViewById(R.id.llShopSelected);
		
		// Icon canh bao co chuong trinh can nhap san luong ban
		llWarningInputQuantity = (LinearLayout) rl_header.findViewById(R.id.llWarningInputQuantity);
		llWarningInputQuantity.setVisibility(View.GONE);
		llWarningInputQuantity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { 
				if (progressDlg == null
						|| (progressDlg != null && !progressDlg.isShowing())) {
					showDialogAllProgrameNeedTypeQuantity();
				}
			}
		});
	}


	public void addView(View v) {
		mainContent.addView(v);
		mainContent.forceLayout();
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * xoa view
	 * 
	 * @author: DoanDM
	 * @param view
	 * @return: void
	 * @throws:
	 */
	public void removeView(View view) {
		mainContent.removeView(view);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			String filePath = "";
			switch (requestCode) {
			case RQ_TAKE_PHOTO: {
				if (resultCode == RESULT_OK && takenPhoto != null) {
					filePath = takenPhoto.getAbsolutePath();
					ImageValidatorTakingPhoto validator = new ImageValidatorTakingPhoto(
							this, filePath, Constants.MAX_FULL_IMAGE_HEIGHT);
					validator.setDataIntent(data);
					if (validator.execute()) {
						ListAlbumUserView orderFragment = (ListAlbumUserView) this
								.getFragmentManager().findFragmentByTag(
										ListAlbumUserView.TAG);
						if (orderFragment != null) {
							orderFragment.updateTakenPhoto();
						}
					}
				}
				break;
			}
			case RQ_SUPERVISOR_TAKE_PHOTO: {
				if (resultCode == RESULT_OK && takenPhoto != null) {
					filePath = takenPhoto.getAbsolutePath();
					ImageValidatorTakingPhoto validator = new ImageValidatorTakingPhoto(
							this, filePath, Constants.MAX_FULL_IMAGE_HEIGHT);
					validator.setDataIntent(data);
					if (validator.execute()) {
						SupervisorListAlbumUserView orderFragment = (SupervisorListAlbumUserView) this
								.getFragmentManager().findFragmentByTag(
										SupervisorListAlbumUserView.TAG);
						if (orderFragment != null) {
							orderFragment.updateTakenPhoto();
						}
					}
				}
			}
			}
		} catch (Throwable ex) {
			ServerLogger.sendLog("ActivityState", "GlobalBaseActivity : "
					+ VNMTraceUnexceptionLog.getReportFromThrowable(ex),
					TabletActionLogDTO.LOG_EXCEPTION);
		}
	}

	/**
	 * Return dialog object
	 * @author banghn
	 * @return
	 */
	public ProgressDialog getProgressDialog() {
		return progressDlg;
	}

	/**
	 * show progress dialog
	 * @author: AnhND
	 * @param content
	 * @param cancleable
	 * @return: void
	 * @throws:
	 */
	public void showProgressDialog(String content, boolean cancleable) {
		if (progressDlg != null && progressDlg.isShowing()) {
			closeProgressDialog();
		}
		if (!isFinishing()) {
			SpannableObject title = new SpannableObject();
			title.addSpan(content, ImageUtil.getColor(R.color.WHITE),
					android.graphics.Typeface.NORMAL);
			progressDlg = ProgressDialog
					.show(this, "", title.getSpan(), true, true);
			// progressDlg = new ProgressDialog(this, R.style.DialogText);
			progressDlg.setTitle(content);
			progressDlg.setIndeterminate(true);
			progressDlg.setOnCancelListener(this);
			progressDlg.setCancelable(cancleable);
			progressDlg.setCanceledOnTouchOutside(false);
			// progressDlg.show();
		}
	}

	public void closeProgressDialog() {
		if (progressDlg != null) {
			try {
				progressDlg.dismiss();
				progressDlg = null;
			} catch (Exception e) {
				VTLog.i("Exception", e.toString());
			}
		}
	}

	public boolean isShowProgressDialog() {
		if (progressDlg != null && progressDlg.isShowing()) {
			return true;
		}
		return false;
	}

	public void showProgressPercentDialog(String content) {
		if (progressDlg != null && progressDlg.isShowing()) {
			closeProgressDialog();
		}
		if (!isFinishing()) {
			progressDlg = new ProgressDialog(this);
			progressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDlg.setMessage(content);
			progressDlg.setCancelable(false);
			progressDlg.show();
		}
	}
	
	public void showProgressPercentDialog(String content, boolean isCancelable) {
		if (progressDlg != null && progressDlg.isShowing()) {
			closeProgressDialog();
		}
		if (!isFinishing()) {
			progressDlg = new ProgressDialog(this);
			progressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDlg.setMessage(content);
			progressDlg.setOnCancelListener(this);
			progressDlg.setCancelable(isCancelable);
			progressDlg.setCanceledOnTouchOutside(false);
			progressDlg.show();
		}
	}

	public void updateProgressPercentDialog(int percent) {
		if (progressDlg != null) {
			progressDlg.setProgress(percent);
		}
	}

	public void closeProgressPercentDialog() {
		if (progressDlg != null) {
			progressDlg.dismiss();
			progressDlg = null;
		}
	}

	public View getLogoHeader() {
		return rl_header;
	}

	public void setLogoLoading(boolean visible) {
		if (progressBar != null) {
			if (visible) {
				progressBar.setVisibility(View.VISIBLE);
			} else {
				progressBar.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * Enable menu bar icon goc phai o action bar cua activity
	 * 
	 * @author : BangHN since : 1.0
	 */
	public void enableMenuActionBar(OnEventControlListener listener) {
		menuBar = new RightHeaderMenuBar(this);
		menuBar.setOnEventControlListener(listener);
		llMenubar.addView(menuBar);
	}

	public void disableMenuActionBar() {
		llMenubar.removeAllViews();
	}

	/**
	 * Add icon menu
	 * @author : TamPQ since : 4:30:39 PM
	 */
	public void addMenuActionBarItem(int drawableResId, int action) {
		menuBar.addMenuItem(drawableResId, action, View.VISIBLE);
	}

	public void addMenuActionBarItem(String text, int drawableResId, int action) {
		menuBar.addMenuItem(text, drawableResId, action, View.VISIBLE);
	}

	/**
	 * Add menu icon kem theo co hien thi dau ngan cach hay khong (GONE ||
	 * VISIBLE)
	 * @author : BangHN since : 9:20:11 AM
	 */
	public void addMenuActionBarItem(int drawableResId, int action,
			int separateVisible) {
		menuBar.addMenuItem(drawableResId, action, separateVisible);
	}

	public void addMenuActionBarItem(String text, int drawableResId,
			int action, int separateVisible) {
		menuBar.addMenuItem(text, drawableResId, action, separateVisible);
	}

	/**
	 * Init menu bar khi ghe tham khach hang
	 * @author : BangHN since : 1.0
	 */
	public void initMenuVisitCustomer(String cusCode, String cusName) {
		llMenubar.removeAllViews();
		enableMenuActionBar(this);
		addMenuActionBarItem(StringUtil.getString(R.string.TEXT_FINISH_VISIT),
				R.drawable.icon_exit, MENU_FINISH_VISIT);
		addMenuActionBarItem(StringUtil.getString(R.string.TEXT_CLOSE_DOOR),
				R.drawable.icon_door, MENU_FINISH_VISIT_CLOSE);
		if(!StringUtil.isNullOrEmpty(cusCode)) {
			setStatusVisible(StringUtil.getString(R.string.TEXT_VISITTING)
					+ cusName + " - " + cusCode + " ", View.VISIBLE);
		}else{
			setStatusVisible(StringUtil.getString(R.string.TEXT_VISITTING)
					+ cusName + " ", View.VISIBLE);
		}
	}

	/**
	 * Init menu bar khi ghe tham khach hang
	 * @author : BangHN since : 1.0
	 */
	public void initMenuVisitCustomerForNVBH(String cusCode, String cusName,
			String objectype, int isOr, int haveAction) {
		llMenubar.removeAllViews();
		enableMenuActionBar(this);
		if (isOr == 0) {
			addMenuActionBarItem(
					StringUtil.getString(R.string.TEXT_FINISH_VISIT),
					R.drawable.icon_exit, MENU_FINISH_VISIT);
			// if(!objectype.equals("0") && !objectype.equals("1")){
			if (haveAction==0) {
				//GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude()
				addMenuActionBarItem(
						StringUtil.getString(R.string.TEXT_CLOSE_DOOR),
						R.drawable.icon_door, MENU_FINISH_VISIT_CLOSE);
			}
		}
		// }
		if(!StringUtil.isNullOrEmpty(cusCode)) {
			setStatusVisible(StringUtil.getString(R.string.TEXT_VISITTING)
					+ cusName + " - " + cusCode + " ", View.VISIBLE);
		}else{
			setStatusVisible(StringUtil.getString(R.string.TEXT_VISITTING)
					+ cusName + " ", View.VISIBLE);
		}
	}

	/**
	 * Init menu bar khi ghe tham khach hang, chi init Ten khach hang
	 * @author : TamPQ since : 1.0
	 */
	public void initCustomerNameOnMenuVisit(String cusCode, String cusName) {
		llMenubar.removeAllViews();
		enableMenuActionBar(this);
		if(!StringUtil.isNullOrEmpty(cusCode)) {
			setStatusVisible(cusName + " - " + cusCode + " ",
					View.VISIBLE);
		}else{
			setStatusVisible(cusName + " ",
					View.VISIBLE);
		}
	}

	/**
	 * remove menu khach hang dong cua
	 * @author : BangHN since : 1.0
	 */
	public void removeMenuCloseCustomer() {
		if (menuBar != null && menuBar.view != null) {
			try {
				menuBar.view.removeViewAt(2);
			} catch (Exception e) {
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			}
		}
	}

	/**
	 * remove menu khach hang dong cua
	 * @author : TamPQ since : 1.0
	 */
	public void removeMenuFinishCustomer() {
		if (menuBar != null && menuBar.view != null) {
			try {
				menuBar.view.removeViewAt(1);
			} catch (Exception e) {
				VTLog.e("Un exception Log", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			}
		}
	}

	/**
	 * An status & menu khi ket thuc ghe tham ket hang
	 * @author : BangHN since : 1.0
	 */
	public void endVisitCustomerBar() {
		if (menuBar != null) {
			menuBar.removeAllViews();
		}
		setStatusVisible("", View.GONE);
	}

	/**
	 * Set trang thai
	 * @author : BangHN since : 1.0
	 */
	public void setStatusVisible(String status, int visible) {
		if (status != null) {
			tvStatus.setText(status);
		}
		llStatus.setVisibility(visible);
	}

	/**
	 * set drawable cho icon header
	 * @author: DoanDM
	 * @param id
	 * @return: void
	 * @throws:
	 */
	protected void setDrawableIconHeader(int pos, int id, String text) {
		switch (pos) {
		case ICON_RIGHT_PARENT:
			RelativeLayout rl1 = (RelativeLayout) rl_header
					.findViewById(R.id.icon1);
			ImageView iv1 = (ImageView) rl1.findViewById(R.id.ivIcon1);
			iv1.setImageDrawable(getResources().getDrawable(id));
			rl1.setVisibility(View.VISIBLE);
			break;
		case ICON_NEXT_1:
			RelativeLayout rl2 = (RelativeLayout) rl_header
					.findViewById(R.id.icon2);
			ImageView iv2 = (ImageView) rl2.findViewById(R.id.ivIcon2);
			iv2.setImageDrawable(getResources().getDrawable(id));
			rl2.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	/**
	 * Set icon header
	 * @author banghn
	 * @param isShowWarning
	 */
	protected void showWarning(boolean isShowWarning) {
		if (llMenuWarning != null && isShowWarning) {
			llMenuWarning.setVisibility(View.VISIBLE);
		} else if (llMenuWarning != null) {
			llMenuWarning.setVisibility(View.GONE);
		}
	}
	 
	 /**
	  * An/hien icon canh bao co chuong trinh can nhap san luong ban
	  * @author: quangvt1
	  * @since: 16:49:20 10-06-2014
	  * @return: void
	  * @throws:  
	  * @param isShowWarning
	  */
	protected void showWarningInputQuantity(boolean isShowWarning) {
		if (llWarningInputQuantity != null && isShowWarning) {
			llWarningInputQuantity.setVisibility(View.VISIBLE);
		} else if (llWarningInputQuantity != null) {
			llWarningInputQuantity.setVisibility(View.GONE);
		}
	}

	/**
	 * Lay icon header view
	 * @author: DoanDM
	 * @param pos
	 * @return
	 * @return: View
	 * @throws:
	 */
	protected View getIconHeader(int pos) {
		View v = null;
		switch (pos) {
		case ICON_RIGHT_PARENT:
			RelativeLayout rl1 = (RelativeLayout) rl_header
					.findViewById(R.id.icon1);
			v = rl1.findViewById(R.id.ivIcon1);
			break;
		case ICON_NEXT_1:
			RelativeLayout rl2 = (RelativeLayout) rl_header
					.findViewById(R.id.icon2);
			v = rl2.findViewById(R.id.ivIcon2);
			break;
		default:
			break;
		}
		return v;
	}

	public void sendBroadcast(int action, Bundle bundle) {
		Intent intent = new Intent(DMS_ACTION);
		bundle.putInt(Constants.ACTION_BROADCAST, action);
		bundle.putInt(Constants.HASHCODE_BROADCAST, intent.getClass()
				.hashCode());
		intent.putExtras(bundle);
		sendBroadcast(intent, "com.viettel.sabeco.permission.BROADCAST");
	}

	/**
	 * Nhan cac broadcast
	 * @author : BangHN since : 1.0
	 */
	public void receiveBroadcast(int action, Bundle bundle) {
		switch (action) {
		case ActionEventConstant.ACTION_FINISH_APP:
			//stop service dinh vi location client
			stopFusedLocationService();
			// clear profile
			GlobalUtil.clearAllData();
			finish();
			break;
		case ActionEventConstant.ACTION_STOP_GOOGLE_PLAY_SERVICE:
			//stop service dinh vi location client
			stopFusedLocationService();
			break;
		case ActionEventConstant.ACTION_FINISH_AND_SHOW_LOGIN:
			//stop service dinh vi location client
			stopFusedLocationService();
			// clear profile
			GlobalUtil.clearAllData();
			finish();
			break;
		case ActionEventConstant.ACTION_UPDATE_POSITION:
			//co vi tri thi tat timer dinh vi
			if (timerGPSRequest != null) {
				timerGPSRequest.cancel();
			}
			//dong gps dialog
			closeGPSProgressDialog();
			break;
		case ActionEventConstant.ACTION_UPDATE_POSITION_SERVICE:
//			VTLog.i("yen", "Global" + DateUtils.now()+ "pos" + GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude()
//					+ "," + GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude());
			if (this == GlobalInfo.getInstance().getActivityContext()
					&& GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType != UserDTO.TYPE_PG){
				VTLog.d("ACTION_UPDATE_POSITION_SERVICE", String.valueOf(this));
				Location location = bundle.getParcelable(IntentConstants.INTENT_DATA);
				updatePosition(location.getLongitude(), location.getLatitude(), location);
			} else{
				VTLog.d("ACTION_UPDATE_POSITION_SERVICE not except", String.valueOf(this));
			}
			break;
		case ActionEventConstant.NOTIFY_ORDER_STATE:
			if (GlobalInfo.getInstance().notifyOrderReturnInfo.hasOrderFail) {
				showWarning(true);
			} else {
				showWarning(false);
			}
			break;
		case ActionEventConstant.NOTIFY_DELETE_SALE_ORDER:
			if (GlobalInfo.getInstance().lastActivity != null
					&& GlobalInfo.getInstance().lastActivity.equals(this)) {
				long saleOrderIdDelete = bundle
						.getLong(IntentConstants.INTENT_SALE_ORDER_ID);
				if (saleOrderIdDelete >= 0 && listOrderId != null) {
					listOrderId.remove(saleOrderIdDelete);
				}
				// xoa don hang da luu
				String orderId = String.valueOf(saleOrderIdDelete);
				if (!GlobalInfo.getInstance().notifyOrderReturnInfo.listOrderProcessedId
						.contains(orderId)) {
					GlobalInfo.getInstance().notifyOrderReturnInfo.listOrderProcessedId
							.add(orderId);
				}
				// cap nhat icon notify
				if (listOrderId != null && listOrderId.size() > 0) {
					showWarning(true);
				} else {
					showWarning(false);
				}
			}
			break;
		case ActionEventConstant.REQUEST_TO_SERVER_SUCCESS:
			closeProgressDialog();
			if (GlobalInfo.getInstance().lastActivity != null
					&& GlobalInfo.getInstance().lastActivity.equals(this)
					&& GlobalInfo.getInstance().getProfile().getUserData().getLoginState() == UserDTO.LOGIN_SUCCESS) {
				showProgressPercentDialog(
						StringUtil.getString(R.string.updating), false);
				//luu lai moc de tinh % hoan thanh dong bo
				SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
				beginLogId = Long.parseLong(sharedPreferences.getString(LAST_LOG_ID, "0"));
				//đặt log KPI đồng bộ ở đây
				startTimeSynDataFromBoot = SystemClock.elapsedRealtime();
				requestSynData(this.synType);
			}
			break;
		case ActionEventConstant.UPGRADE_NOTIFY: {
			if(!(this instanceof LoginView)){
				closeProgressDialog();
				GlobalUtil.showDialogConfirm(this, StringUtil.getString(R.string.ERR_UPGRADE_RELOGIN), 
						StringUtil.getString(R.string.TEXT_AGREE), ACTION_SHOW_LOGIN, null, -1, null);
			}
			break;
		}
		case ActionEventConstant.NOTIFY_DATA_NOT_SYN:{
			//thong bao dinh vi
		new NotifyLocation().execute();
		break;
	}
		default:
			break;
		}
	}

	/**
	 * show dialog
	 * @param mes
	 * @return
	 */
	public AlertDialog showDialog(final CharSequence mes) {
		if (isFinishing()) {
			return null;
		}
		AlertDialog alertDialog = null;
		try {
			alertDialog = new AlertDialog.Builder(this).create();
			// alertDialog.setTitle("Thông báo");
			alertDialog.setMessage(mes);
			alertDialog.setButton(
					StringUtil.getString(R.string.TEXT_BUTTON_CLOSE),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							// TamPQ: neu truong hop ko tim thay doi tuong thi
							// quay tro ve man hinh truoc
							return;

						}
					});
			alertDialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return alertDialog;
	}
	
	/**
	 * Dialog tu dong tat
	 * @author: quangvt
	 * @since: 17:25:10 28-05-2014
	 * @return: AlertDialog
	 * @throws:  
	 * @param mes
	 * @param time : thoi gian tat (mili)
	 * @return
	 */
	public void showDialogAutoClose(final CharSequence mes, final long time) {
		if (isFinishing()) {
			return;
		} 
		
		 final Timer t = new Timer();
		 
		try {
			final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			// alertDialog.setTitle("Thông báo");
			alertDialog.setMessage(mes);
			alertDialog.setButton(
					StringUtil.getString(R.string.TEXT_BUTTON_CLOSE),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							t.cancel();
							// TamPQ: neu truong hop ko tim thay doi tuong thi
							// quay tro ve man hinh truoc
							return;

						}
					});
			alertDialog.show();  
			
            t.schedule(new TimerTask() {
				public void run() {
					alertDialog.dismiss();
					t.cancel();
				}
			}, time);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}  
	}
	 
	/**
	 * Dialog hien cac chuong trinh can nhap san luong ban
	 * @author: quangvt1
	 * @since: 17:26:53 10-06-2014
	 * @return: void
	 * @throws:
	 */
	public void showDialogAllProgrameNeedTypeQuantity() {
		if (isFinishing()) {
			return;
		}  
		
		try {  
			final Dialog dialog = new Dialog(this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.layout_dialog_all_programe_need_type_quantity); 
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
  
			TextView text = (TextView) dialog.findViewById(R.id.tvNotify);
			String str = StringUtil.getString(R.string.TEXT_TITLE_NEED_INPUT_QUANTIY) + "\n";
			for (ProInfoDTO p : listProgrameTypeQuantity.listPrograme) {
				str += "- " + p.PRO_INFO_NAME + "\n";
			}
			text.setText(str); 
 
			Button btnClose = (Button) dialog.findViewById(R.id.btnClose); 
			btnClose.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			
			Button btnAccept = (Button) dialog.findViewById(R.id.btnAccept); 
			btnAccept.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();

					// Popstack neu dang o 1 trong 3 màn hinh Thuc hien, tham gia, Thong tin
					String currTag = GlobalInfo.getInstance().getCurrentTag();
					if (currTag.equals(CustomerProgrameDoneView.TAG)
							|| currTag.equals(CustomerAttendProgramListView.TAG)
							|| currTag.equals(SaleSupportProgramDetailView.TAG)) {
						GlobalUtil.popBackStack(GlobalBaseActivity.this);
					}

					ActionEvent e = new ActionEvent();
					e.sender = GlobalBaseActivity.this;
					Bundle bundle = new Bundle();
					bundle.putSerializable(IntentConstants.INTENT_PROMOTION, listProgrameTypeQuantity.listPrograme.get(0));
					e.viewData = bundle;
					e.action = ActionEventConstant.GO_TO_PROMOTION_PROGRAM_DONE;
					SaleController.getInstance().handleSwitchFragment(e);
				}
			});
 
			dialog.show();
		} catch (Exception e) { 
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}  
	}

	/**
	 * show dialog ForAddRequirementView
	 * @param mes
	 * @return
	 */
	public AlertDialog showDialogForAddRequirementView(final CharSequence mes,
			final Object sender) {
		if (isFinishing()) {
			return null;
		}
		AlertDialog alertDialog = null;
		try {
			alertDialog = new AlertDialog.Builder(this).create();
			// alertDialog.setTitle("Thông báo");
			alertDialog.setMessage(mes);
			alertDialog.setButton(
					StringUtil.getString(R.string.TEXT_BUTTON_CLOSE),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							// TamPQ: neu truong hop ko tim thay doi tuong thi
							// quay tro ve man hinh truoc
							ActionEvent e = new ActionEvent();
							e.action = ActionEventConstant.GO_TO_TBHV_FOLLOW_LIST_PROBLEM;
							e.sender = sender;
							TBHVController.getInstance()
									.handleSwitchFragment(e);
							return;

						}
					});
			alertDialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return alertDialog;
	}

	/**
	 * set cac constant define in app params "FACTOR", "TIME_TEST_ORDER",
	 * "RADIUS_OF_POSITION", "TIME_TRIG_POSITION", "ALLOW_EDIT_PROMOTION"
	 * 
	 * @author banghn
	 */
	protected void setAppDefineConstant() {
		ArrayList<ApParamDTO> result = SQLUtils.getInstance().getAppDefineConstant();
		if (result != null && result.size() > 0) {
			long beginInterval = GlobalInfo.getInstance().getIntervalFusedPosition();
			long beginFastInterval = GlobalInfo.getInstance().getFastIntervalFusedPosition();
			long beginPriority = GlobalInfo.getInstance().getFusedPositionPriority();
			long beginRadius = GlobalInfo.getInstance().getRadiusFusedPosition();
			int size = result.size();
			for (int i = 0; i < size; i++) {
				VTLog.d("setAppDefineConstant", result.get(i).type + " " + result.get(i).value);
				try {
					if ("TIME_TEST_ORDER".equals(result.get(i).type)) {
                        GlobalInfo.getInstance().setTimeTestOrder(
								Long.valueOf(result.get(i).value));
                    } else if ("RADIUS_OF_POSITION".equals(result.get(i).type)) {
                        GlobalInfo.getInstance().setRadiusOfPosition(
								Long.valueOf(result.get(i).value));
                    } else if ("TIME_TRIG_POSITION".equals(result.get(i).type)) {
                        GlobalInfo.getInstance().setTimeTrigPosition(
								Long.valueOf(result.get(i).value));
                    } else if ("TIMEOUT_WHEN_IDLE".equals(result.get(i).type)) {
                        GlobalInfo.getInstance().setTimeAllowIdleStatus(
								Long.valueOf(result.get(i).value));
                    } else if ("TIME_ALLOW_SYN_DATA_START".equals(result.get(i).type)) {
                        GlobalInfo.getInstance().setBeginTimeAllowSynData(
								Integer.valueOf(result.get(i).value));
                    } else if ("TIME_ALLOW_SYN_DATA_END".equals(result.get(i).type)) {
						GlobalInfo.getInstance().setEndTimeAllowSynData(
								Integer.valueOf(result.get(i).value));
                    } else if ("ALLOW_EDIT_PROMOTION".equals(result.get(i).type)) {
                        GlobalInfo.getInstance().setAllowEditPromotion(
								Integer.valueOf(result.get(i).value));
                    } else if ("SERVER_IMAGE_PRO_VNM".equals(result.get(i).type)) {
                        GlobalInfo.getInstance().setServerImageProductVNM(
								result.get(i).value);
                    } else if ("TIME_SYNC_TO_SERVER".equals(result.get(i).type)) {
                        GlobalInfo.getInstance().setTimeSyncToServer(
								Integer.valueOf(result.get(i).value));
                    } else if ("TIME_TRIG_POS_ATTEND".equals(result.get(i).type)) {
                        GlobalInfo.getInstance().setTimeTrigPositionAttendance(
								Long.valueOf(result.get(i).value) * 60 * 1000);
                    } else if ("MAX_LOGKPI_ALLOW".equals(result.get(i).type)) {
                        GlobalInfo.getInstance().setAllowRequestLogKPINumber(Integer.valueOf(result.get(i).value));
                    }else if ("MAX_TIME_WRONG".equals(result.get(i).type)) {
                        GlobalInfo.getInstance().setMaxIimeWrong(Long.valueOf(result.get(i).value));
                    }else if("INTERVAL_FUSED_POSITION".equals(result.get(i).type)) {
                        GlobalInfo.getInstance().setIntervalFusedPosition(
								Long.valueOf(result.get(i).value));
                    } else if("FAST_INTERVAL_FUSED_POSITION".equals(result.get(i).type)) {
                        GlobalInfo.getInstance().setFastIntervalFusedPosition(
								Long.valueOf(result.get(i).value));
                    } else if("RADIUS_OF_POSITION_FUSED".equals(result.get(i).type)) {
                        GlobalInfo.getInstance().setRadiusFusedPosition(
								Long.valueOf(result.get(i).value));
                    } else if("FUSED_POSTION_PRIORITY".equals(result.get(i).type)) {
                        GlobalInfo.getInstance().setFusedPositionPriority(
                                Long.valueOf(result.get(i).value));
                    }else if ("TIME_REQUEST_TO_SERVER".equals(result.get(i).type)) {
                        GlobalInfo.getInstance().setTimeCheckToServer(
                                Integer.valueOf(result.get(i).value));
                    } else if("FLAG_VALIDATE_MOCK_LOCATION".equals(result.get(i).type)) {
						GlobalInfo.getInstance().setFlagValidateMockLocation(Long.valueOf(result.get(i).value));
					} else if("VT_MAP_SET_SERVER".equals(result.get(i).type)) {
						GlobalInfo.getInstance().setVtMapSetServer(Integer.valueOf(result.get(i).value) == 1 ? true : false);
					} else if("VT_MAP_PROTOCOL".equals(result.get(i).type)) {
						GlobalInfo.getInstance().setVtMapProtocol(result.get(i).value);
					} else if("VT_MAP_IP".equals(result.get(i).type)) {
						GlobalInfo.getInstance().setVtMapIP(result.get(i).value);
					} else if("VT_MAP_PORT".equals(result.get(i).type)) {
						GlobalInfo.getInstance().setVtMapPort(Integer.valueOf(result.get(i).value));
					} else if ("LOCATION_SPEED".equals(result.get(i).type)) {
						GlobalInfo.getInstance().setLocationSpeed(
								Integer.valueOf(result.get(i).value));
					} else if ("CF_NUM_FLOAT".equals(result.get(i).type)) {
						GlobalInfo.getInstance().setCfNumFloat(result.get(i).value);
					} else if ("PG_SEND_ORDER".equals(result.get(i).type)) {
						GlobalInfo.getInstance().setPGNumberSendOrder(
								Integer.valueOf(result.get(i).value));
					} else if ("EMAIL_SEND_DATA".equals(result.get(i).type)) {
						GlobalInfo.getInstance().setEmailSendData((result.get(i).value));
					} else if ("TYPE_HASH_PASS".equals(result.get(i).type)) {
						GlobalInfo.getInstance().setTypeHashPass((result.get(i).value));
					}
				} catch (Exception e) {
					VTLog.e("Set App constanst", result.get(i).type, e);
				}
			}
			long endInterval = GlobalInfo.getInstance().getIntervalFusedPosition();
			long endFastInterval = GlobalInfo.getInstance().getFastIntervalFusedPosition();
			long endPriority = GlobalInfo.getInstance().getFusedPositionPriority();
			long endRadius = GlobalInfo.getInstance().getRadiusFusedPosition();
			//kiểm tra tham số định vị fused thay đổi
			if ((beginInterval != endInterval
					|| beginFastInterval != endFastInterval 
					|| beginPriority != endPriority
					|| beginRadius != endRadius) && GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType != UserDTO.TYPE_PG) {
				//start lại luồng đồng bộ fused với tham số mới
				startFusedLocationService(false, false, false);
			}
		}
		// doc thoi gian cham cong
		readAttendaceTime();
		
		//Doc thong tin co' hien thi gia hay khong?
		readShowPriceOfShop();
		
		// khoi tao danh sach ung dung chan network, cai dat
		initListApplication();
	}

	/**
	 * Doc cau hinh thoi gian cham cong luu vao global-info
	 * @author: BANGHN
	 */
	private void readAttendaceTime() {
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_PARENT_SHOP_ID, GlobalInfo
				.getInstance().getProfile().getUserData().shopIdProfile);
		List<ShopParamDTO> paramList = SQLUtils.getInstance()
				.getAttendaceTimeDefine(b);
		if (paramList != null && paramList.size() >= 3
				&& paramList.get(0).code != null
				&& paramList.get(1).code != null
				&& paramList.get(2).code != null) {
			// starttime
			int hour = 7, minute = 30;
			if (paramList.get(0).code != null) {
				hour = Integer.valueOf(paramList.get(0).code.split(":")[0]);
				minute = Integer.valueOf(paramList.get(0).code.split(":")[1]);
			}
			if (minute > 5)
				minute -= 5;
			String ccStartTime = hour + ":" + minute;
			GlobalInfo.getInstance().setCcStartTime(ccStartTime);

			// endtime
			hour = 8;
			minute = 15;
			hour = Integer.valueOf(paramList.get(1).code.split(":")[0]);
			minute = Integer.valueOf(paramList.get(1).code.split(":")[1]);
			String ccEtartTime = hour + ":" + minute;
			GlobalInfo.getInstance().setCcEndTime(ccEtartTime);

			// cc_distance
			GlobalInfo.getInstance().setCcDistance(
					Integer.parseInt(paramList.get(2).code));
		}
	}
	
	/**
	 * Doc thong tin co' hien thi gia khi dat hang or xem don hang hay khong
	 * @author: dungnt19
	 * @since: 14:12:24 10-03-2014
	 * @return: void
	 * @throws:
	 */
	private void readShowPriceOfShop() {
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
		int showPrice = SQLUtils.getInstance().getIsShowPriceDefine(bundle);
		GlobalInfo.getInstance().setIsShowPrice(showPrice);
	}

	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.RE_LOGIN:
			UserDTO userDTO = (UserDTO) modelEvent.getModelData();
			GlobalInfo.getInstance().getProfile().setUserData(userDTO);
			// kiem tra thoi gian hop le? sau khi relogin
			if (userDTO != null && (userDTO.appVersion != null || userDTO.dbVersion != null) 
					&& !(this instanceof LoginView)) {
				GlobalUtil.showDialogConfirm(this,
						StringUtil.getString(R.string.ERR_UPGRADE_RELOGIN),
						StringUtil.getString(R.string.TEXT_AGREE),
						ACTION_SHOW_LOGIN, null, -1, null);
			}else if (userDTO != null
					&& !DateUtils.checkTimeToShowLogin(userDTO.serverDate)) {
				//no_log
				GlobalUtil.showDialogConfirm(this,
						StringUtil.getString(R.string.TEXT_CONFIRM_RELOGIN),
						StringUtil.getString(R.string.TEXT_AGREE),
						ACTION_SHOW_LOGIN, null, -1, null);
				//cap nhat thoi gian dung sau khi relogin
				updateTimeAfterRelogin(userDTO.serverDate);
			} else if (actionEventBeforReLogin != null 
					&& actionEventBeforReLogin.action != ActionEventConstant.RE_LOGIN) {
				actionEventBeforReLogin.controller
						.handleViewEvent(actionEventBeforReLogin);
			}
			blReLogin = false;
			break;
		case ActionEventConstant.ACTION_SYN_SYNDATA: {
			SynDataDTO synDataDTO = (SynDataDTO) modelEvent.getModelData();
			if (SynDataDTO.UPDATE_TO_DATE.equals(synDataDTO.getState())) {
				if (!TransactionProcessManager.getInstance().isStarting()) {
					TransactionProcessManager.getInstance().startChecking(
							TransactionProcessManager.SYNC_NORMAL);
				}
				if (modelEvent.getActionEvent().tag == REQUEST_SYNC_FROM_USER) {
					requestGetAllOrderFailForWarning();
					updateProgressPercentDialog(98);
					updateProgressPercentDialog(99);
					updateProgressPercentDialog(100);
					closeProgressDialog();
				} else if (modelEvent.getActionEvent().tag == REQUEST_SYNC_FROM_INACTIVE_APP) {
					// hien thi ds don hang loi
					requestGetAllOrderFail();
				} else{
					closeProgressDialog();
				}
				// update cac tham so dinh nghia tren server
				setAppDefineConstant();
				GlobalInfo.getInstance().notifyOrderReturnInfo.isSyncDataFromServer = false;
				// refresh view
				sendBroadcast(ActionEventConstant.NOTIFY_REFRESH_VIEW,
						new Bundle());
				//thêm KPI
				requestInsertLogKPI(HashMapKPI.GLOBAL_SYN_DATA , startTimeSynDataFromBoot);
				//tránh trùng
				startTimeSynDataFromBoot = 0;

				VTLog.d("SYN UPDATE_TO_DATE", "getMaxDBLogId: " + synDataDTO.getMaxDBLogId()
						+ " getLastLogId_update: " + synDataDTO.getLastLogId_update() + " beginLogId: " + beginLogId);
			} else if (SynDataDTO.CONTINUE.equals(synDataDTO.getState())) {
				// fixed truong hop nhan phim back nhung van con dong bo
				// tiep
				if (!isCancelUpdateData) {
					requestSynData(modelEvent.getActionEvent().tag);
					int percent = (int)(100 - ((float)(synDataDTO.getMaxDBLogId() - synDataDTO
							.getLastLogId_update())/(float)(synDataDTO.getMaxDBLogId() - beginLogId))* 100);
					if (percent > 98) {
						percent = 98;
					}
					updateProgressPercentDialog(percent);
					VTLog.d("SYN CONTINUE", "getMaxDBLogId: " + synDataDTO.getMaxDBLogId()
							+ " getLastLogId_update: " + synDataDTO.getLastLogId_update() + " beginLogId: " + beginLogId);
				}
			} else if (SynDataDTO.RESET.equals(synDataDTO.getState())) {
				closeProgressDialog();
				GlobalUtil.showDialogConfirm(this, StringUtil
						.getString(R.string.TEXT_CONFIRM_RELOGIN_TO_RESET_DB),
						StringUtil.getString(R.string.TEXT_AGREE),
						ACTION_OK_RESET_DB, StringUtil
								.getString(R.string.TEXT_DENY),
						ACTION_CANCEL_RESET_DB, null);
			}
			break;
		}
		case ActionEventConstant.INSERT_ACTION_LOG:
			isInsertingActionLogVisit = false;
			break;
		case ActionEventConstant.START_INSERT_ACTION_LOG:
			// update last customer visit
			isInsertingActionLogVisit = false;
			GlobalInfo.getInstance().getProfile().setVisitingCustomer(true);
			GlobalInfo.getInstance().getProfile()
					.setActionLogVisitCustomer((ActionLogDTO) e.viewData);
			break;
		case ActionEventConstant.ACTION_GET_ALL_ORDER_FAIL_FOR_WARNING:
			// closeProgressDialog();
			NoSuccessSaleOrderDto dto = (NoSuccessSaleOrderDto) modelEvent
					.getModelData();
			// luu ds don hang
			if (dto != null && dto.itemList != null) {
				if (listOrderId != null) {
					listOrderId.clear();
				}
				// GlobalInfo.getInstance().notifyOrderReturnInfo.listOrderUpdateId.clear();
				for (int i = 0, size = dto.itemList.size(); i < size; i++) {
					SaleOrderDTO saleDTO = dto.itemList.get(i).saleOrder;
					listOrderId.add(saleDTO.saleOrderId);
					// if (saleDTO.isSend == 0 && saleDTO.synStatus == 0 &&
					// saleDTO.synState == 2){
					// GlobalInfo.getInstance().notifyOrderReturnInfo.listOrderUpdateId.add(String.valueOf(saleDTO.saleOrderId));
					// }

				}
			}
			if (dto != null && dto.itemList.size() > 0) {
				showWarning(true);
			} else {
				if (listOrderId != null && listOrderId.size() <= 0) {
					showWarning(false);
				}
			}
			break;
		case ActionEventConstant.NO_SUCCESS_ORDER_LIST:
		case ActionEventConstant.ACTION_GET_ALL_ORDER_FAIL:
			closeProgressDialog();
			dto = (NoSuccessSaleOrderDto) modelEvent.getModelData();
			// luu ds don hang
			if (dto != null && dto.itemList != null) {
				if (listOrderId != null) {
					listOrderId.clear();
				}
				// GlobalInfo.getInstance().notifyOrderReturnInfo.listOrderUpdateId.clear();
				for (int i = 0, size = dto.itemList.size(); i < size; i++) {
					SaleOrderDTO saleDTO = dto.itemList.get(i).saleOrder;
					listOrderId.add(saleDTO.saleOrderId);
					// if (saleDTO.isSend == 0 && saleDTO.synStatus == 0 &&
					// saleDTO.synState == 2){
					// GlobalInfo.getInstance().notifyOrderReturnInfo.listOrderUpdateId.add(String.valueOf(saleDTO.saleOrderId));
					// }

				}
			}
			if (dto != null && dto.itemList.size() > 0) {
				showNoSuccessOrderDialog(dto);
			} else {
				if (listOrderId != null && listOrderId.size() <= 0) {
					showWarning(false);
				}
			}

			break;
		case ActionEventConstant.GET_ORDER_IN_LOG: {
			NotifyOrderDTO notifyDTO = (NotifyOrderDTO) modelEvent
					.getModelData();
			// ArrayList<LogDTO> list = (ArrayList<LogDTO>)
			// modelEvent.getModelData();
			sendBroadcaseNotifyOrder(notifyDTO);
			break;
		}
		case ActionEventConstant.ACTION_UPDATE_POSITION:
			int errCode = modelEvent.getModelCode();
			StaffPositionLogDTO staffPos = (StaffPositionLogDTO) e.userData;
			if (errCode == ErrorConstants.ERROR_CODE_SUCCESS) {
				// thanh cong thi insert db hien thi thong bao
				staffPos.id = System.currentTimeMillis();
				SQLUtils.getInstance().insert(staffPos);
				sendBroadcast(ActionEventConstant.NOTIFY_DATA_NOT_SYN, new Bundle());
			} else {
				// ghi vao log de thuc hien lai
				if (e.userData != null){
					if (staffPos != null){
						GlobalInfo.getInstance().addPosition(staffPos);
					}
				}
			}
			break;
		case ActionEventConstant.ACTION_DELETE_OLD_LOG_TABLE: {
			SharedPreferences sharedPreferences = getSharedPreferences(LoginView.PREFS_PRIVATE, Context.MODE_PRIVATE);
			Editor prefsPrivateEditor = sharedPreferences.edit();
			prefsPrivateEditor.putString(DATE_CLEAR_OLD_DATA, DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT));
			prefsPrivateEditor.commit();

			closeProgressDialog();
			LogFile.logToFile(DateUtils.now()
					+ " || Hoàn thành thực hiện xóa dữ liệu cũ");
			sendBroadcast(ActionEventConstant.ACTION_FINISH_APP, new Bundle());
			break;
		}
		default:
			break;
		}
	}

	/**
	 * show dialog DS don hang chua chuyen thanh cong
	 * 
	 * @author : TamPQ since : 1.0
	 */
	private void showNoSuccessOrderDialog(NoSuccessSaleOrderDto dto) {
		if (alertRemindDialog != null && alertRemindDialog.isShowing()) {
			alertRemindDialog.dismiss();
		}
		Builder build = new AlertDialog.Builder(this, R.style.CustomDialogTheme);

		NoSuccessSaleOrderView view = new NoSuccessSaleOrderView(this, dto);
		build.setView(view.viewLayout);
		alertRemindDialog = build.create();
		Window window = alertRemindDialog.getWindow();
		window.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 255, 255,
				255)));
		window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.CENTER);
		if (!alertRemindDialog.isShowing()) {
			alertRemindDialog.show();
		}
	}

	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		closeProgressDialog();
		VTLog.logToFile("BaseAcitivty", DateUtils.now() + "Action		: "
				+ modelEvent.getActionEvent().action);
		VTLog.logToFile("BaseAcitivty", DateUtils.now() + "Error code	: "
				+ modelEvent.getModelCode());
		VTLog.logToFile("BaseAcitivty", DateUtils.now() + "Message		: "
				+ modelEvent.getModelMessage());
		if (modelEvent.getActionEvent().action == ActionEventConstant.START_INSERT_ACTION_LOG
				|| modelEvent.getActionEvent().action == ActionEventConstant.INSERT_ACTION_LOG) {
			isInsertingActionLogVisit = false;
		}

		// neu luong dong bo loi thi kick lai luong dong bo dinh ky 3 phut
		if (modelEvent.getActionEvent().action == ActionEventConstant.ACTION_SYN_SYNDATA) {
			if (!TransactionProcessManager.getInstance().isStarting()) {
				TransactionProcessManager.getInstance().startChecking(
						TransactionProcessManager.SYNC_NORMAL);
			}
			closeProgressPercentDialog();
		}
		
		// thoat app sau khi clear data old (o hanh dong thoat ung dung)
		if (modelEvent.getActionEvent().action == ActionEventConstant.ACTION_DELETE_OLD_LOG_TABLE) {
			closeProgressDialog();
			LogFile.logToFile(DateUtils.now()
					+ " || Hoàn thành thực hiện xóa dữ liệu cũ");
			sendBroadcast(ActionEventConstant.ACTION_FINISH_APP, new Bundle());
		}
		
		//gap loi (network, offline,...) luu vao log de gui len lai
		if (modelEvent.getActionEvent().action == ActionEventConstant.ACTION_UPDATE_POSITION) {
			// ghi vao log de thuc hien lai
			if (modelEvent.getActionEvent().userData != null) {
				StaffPositionLogDTO staffPos = (StaffPositionLogDTO) modelEvent
						.getActionEvent().userData;
				if (staffPos != null) {
					GlobalInfo.getInstance().addPosition(staffPos);
				}
			}
		}

		switch (modelEvent.getModelCode()) {
		case ErrorConstants.ERROR_WAITING:{
			showToastMessage(StringUtil.getString(R.string.TEXT_SYSTEM_WAITING));
			break;
		}
		case ErrorConstants.ERROR_COMMON:
			if (modelEvent.getActionEvent().action == ActionEventConstant.ACTION_UPDATE_POSITION) {
				if ((modelEvent.getActionEvent().userData != null)) {
					StaffPositionLogDTO staffPos = (StaffPositionLogDTO) (modelEvent
							.getActionEvent()).userData;
					if (staffPos != null) {
						GlobalInfo.getInstance().addPosition(staffPos);
					}
				}
			} else {
				if(!isFinishing()){
					showDialog(VTStringUtils.getString(getApplicationContext(), R.string.MESSAGE_ERROR_COMMON));
					ServerLogger.sendLogLogin("LOGIN ACTION_SYN_SYNDATA"
							, "modelEvent 1: " + modelEvent.getModelMessage()
							+ " - action: " + modelEvent.getActionEvent().action
							, TabletActionLogDTO.LOG_CLIENT);
				}
			}
			break;
		case ErrorConstants.ERROR_NO_CONNECTION:
			showDialog(modelEvent.getModelMessage());
			break;
		case ErrorConstants.ERROR_EXPIRED_TIMESTAMP:
			/*
			 * BangHN: Loi chung thuc thoi gian do tomcat bao Tam thoi bo vi da
			 * check bang system auto-time
			 */
			// GlobalUtil.showDialogSettingTime();
			break;
		case ErrorConstants.ERROR_SESSION_RESET: // error when session time out.
			// re-login and re-request
			// kiem tra da dang nhap thanh cong chua
			if (GlobalInfo.getInstance().getProfile().getUserData()
					.getLoginState() == UserDTO.LOGIN_SUCCESS) {
				ServerLogger.sendLog("RELOGIN",
						"ERROR_SESSION_RESET - State Success",
						TabletActionLogDTO.LOG_CLIENT);
				// da login & mat session
				if (!blReLogin) {
					if (modelEvent != null && modelEvent.getActionEvent() != null) {
						if (modelEvent.getActionEvent().action != ActionEventConstant.RE_LOGIN) {
							actionEventBeforReLogin = modelEvent.getActionEvent();
						} else {
							ServerLogger.sendLog("RELOGIN", "Loop Non-Stop", TabletActionLogDTO.LOG_CLIENT);
						}
					}
//					actionEvent_befor_re_login = modelEvent.getActionEvent();
					blReLogin = true;
					// get Oauth thanh cong
					if (GlobalUtil.checkNetworkAccess()) {
						blReLogin = false;
						if (actionEventBeforReLogin != null) {
							actionEventBeforReLogin.controller.handleViewEvent(actionEventBeforReLogin);
						}
					} else {// get Oauth fail
						blReLogin = false;
						modelEvent
								.setModelCode(ErrorConstants.ERROR_NO_CONNECTION);
						modelEvent
								.setModelMessage(StringUtil
										.getString(R.string.MESSAGE_ERROR_NO_CONNECTION));
						handleErrorModelViewEvent(modelEvent);
					}
				} else {
					blReLogin = false;
					modelEvent.setModelCode(ErrorConstants.ERROR_NO_CONNECTION);
					handleErrorModelViewEvent(modelEvent);
				}
			} else {
				// chua login thanh cong
				if (modelEvent != null && modelEvent.getActionEvent() != null) {
					if (modelEvent.getActionEvent().action != ActionEventConstant.RE_LOGIN) {
						numRelogin = 0;
						actionEventBeforReLogin = modelEvent.getActionEvent();
					} else {
						numRelogin++;
						ServerLogger.sendLog("RELOGIN", "Loop Non-Stop", TabletActionLogDTO.LOG_CLIENT);
					}
				} else {
					numRelogin++;
				}
				
				if(numRelogin < 3){
					VTLog.d("Relogin", "Relogin ...... " + numRelogin);

					Vector<String> vt = new Vector<String>();
					vt.add(IntentConstants.INTENT_USER_NAME);
					vt.add(GlobalInfo.getInstance().getProfile().getUserData().userName);

					vt.add(IntentConstants.INTENT_LOGIN_PASSWORD);
					vt.add(GlobalInfo.getInstance().getProfile().getUserData().pass);
					vt.add(IntentConstants.INTENT_LOGIN_IS_REMEMBER);
					vt.add("true");

					vt.add(IntentConstants.INTENT_LOGIN_PHONE_MODEL);
					vt.add(GlobalInfo.getInstance().PHONE_MODEL);

					vt.add(IntentConstants.INTENT_IMEI);
					vt.add(GlobalInfo.getInstance().getDeviceIMEI());

					if (!StringUtil.isNullOrEmpty(StringUtil.getSimSerialNumber())) {
						vt.add(IntentConstants.INTENT_SIM_SERIAL);
						vt.add(StringUtil.getSimSerialNumber());
					}

					vt.add(IntentConstants.INTENT_VERSION_APP);
					vt.add(GlobalInfo.getInstance().getProfile().getVersionApp());

					vt.add(IntentConstants.INTENT_VERSION_DB);
					vt.add(GlobalInfo.getInstance().getProfile().getVersionDB());

					vt.add(IntentConstants.INTENT_LOGIN_PLATFORM);
					vt.add(GlobalInfo.getInstance().PLATFORM_SDK_STRING);

					ActionEvent e = new ActionEvent();
					e.action = ActionEventConstant.RE_LOGIN;
					e.viewData = vt;
					e.sender = this;
					UserController.getInstance().handleViewEvent(e);
				}else{
					modelEvent.setModelCode(ErrorConstants.ERROR_NO_CONNECTION);
					modelEvent.setModelMessage(StringUtil.getString(R.string.ERR_LOGIN_CONNET_FAIL));
					handleErrorModelViewEvent(modelEvent);
				}
			}

			break;
		}

	}

	/**
	 * Thuc hien lay tat ca don hang chua thuc hien thanh cong: bi loi, chua
	 * goi, tra ve
	 * 
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	public void requestGetAllOrderFail() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.action = ActionEventConstant.ACTION_GET_ALL_ORDER_FAIL;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * Thuc hien lay tat ca don hang chua thuc hien thanh cong: bi loi, chua
	 * goi, tra ve de show warning
	 * 
	 * @author: trungnt56
	 * @return: void
	 */
	public void requestGetAllOrderFailForWarning() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.action = ActionEventConstant.ACTION_GET_ALL_ORDER_FAIL_FOR_WARNING;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * request bat dau ghe tham khach hang
	 * 
	 * @author : BangHN since : 1.0
	 */
	public void requestInsertActionLog(ActionLogDTO action) {
		if (!isInsertingActionLogVisit) {
			isInsertingActionLogVisit = true;
			ActionEvent e = new ActionEvent();
			action.createUser = GlobalInfo.getInstance().getProfile()
					.getUserData().userCode;
			action.staffId = GlobalInfo.getInstance().getProfile()
					.getUserData().id;
			action.lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo()
					.getLastLatitude();
			action.lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo()
					.getLastLongtitude();
			action.endTime = DateUtils.now();
			
			if (!StringUtil.isNullOrEmpty(action.startTime)) {
				long second = DateUtils.getDistanceSecondsFrom2Date(action.startTime, action.endTime);
				action.interval_time = String.valueOf(second);
			}
			
			VTLog.i("VTLog", "insert action: startTime - endTime : "
					+ action.startTime + " - " + action.endTime);
			e.viewData = action;
			e.sender = this;
			e.action = ActionEventConstant.INSERT_ACTION_LOG;
			e.isNeedCheckTimeServer = false;
			SaleController.getInstance().handleViewEvent(e);
		}
	}

	/**
	 * request bat dau ghe tham khach hang
	 * 
	 * @author : BangHN since : 1.0
	 */
	public void requestDeleteActionLog(ActionLogDTO action) {
		ActionEvent e = new ActionEvent();
		VTLog.i("VTLog", "delete action: startTime - endTime : "
				+ action.startTime + " - " + action.endTime);
		e.viewData = action;
		e.sender = this;
		e.action = ActionEventConstant.DELETE_ACTION_LOG;
		e.isNeedCheckTimeServer = false;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * request bat dau ghe tham khach hang
	 * 
	 * @author : TamPQ since : 1.0
	 */
	public void requestStartInsertVisitActionLog(CustomerListItem item) {
		if (!isInsertingActionLogVisit) {
			isInsertingActionLogVisit = true;
			ActionLogDTO visitCustomer = new ActionLogDTO();
			visitCustomer.aCustomer.customerId = item.aCustomer.customerId;
			visitCustomer.aCustomer.customerName = item.aCustomer.customerName;
			visitCustomer.aCustomer.customerCode = item.aCustomer.customerCode;
			visitCustomer.startTime = DateUtils.now();
			visitCustomer.isOr = item.isOr;
			visitCustomer.createUser = GlobalInfo.getInstance().getProfile()
					.getUserData().userCode;
			visitCustomer.staffId = GlobalInfo.getInstance().getProfile()
					.getUserData().id;
			visitCustomer.lat = GlobalInfo.getInstance().getProfile()
					.getMyGPSInfo().getLastLatitude();
			visitCustomer.lng = GlobalInfo.getInstance().getProfile()
					.getMyGPSInfo().getLastLongtitude();

			visitCustomer.createUser = GlobalInfo.getInstance().getProfile()
					.getUserData().userCode;
			visitCustomer.staffId = GlobalInfo.getInstance().getProfile()
					.getUserData().id;
			visitCustomer.lat = GlobalInfo.getInstance().getProfile()
					.getMyGPSInfo().getLastLatitude();
			visitCustomer.lng = GlobalInfo.getInstance().getProfile()
					.getMyGPSInfo().getLastLongtitude();
			// visitCustomer.objectId = "0";
			visitCustomer.objectType = "0";
			visitCustomer.distance = item.cusDistance;
			// visitCustomer.endTime = DateUtils.now();

			GlobalInfo.getInstance().getProfile()
					.setActionLogVisitCustomer(visitCustomer);

			ActionEvent e = new ActionEvent();
			e.viewData = visitCustomer;
			e.sender = this;
			e.action = ActionEventConstant.START_INSERT_ACTION_LOG;
			SaleController.getInstance().handleViewEvent(e);
		}
	}

	/**
	 * request ket thuc ghe tham
	 * 
	 * @author : TamPQ since : 1.0
	 */
	public void requestUpdateActionLog(String objType, String objId,
			CustomerListItem customer, Object sender) {
		endVisitCustomerBar();
		ActionLogDTO al = GlobalInfo.getInstance().getProfile()
				.getActionLogVisitCustomer();
		if (al != null && StringUtil.isNullOrEmpty(al.endTime)) {
			if(StringUtil.isNullOrEmpty(al.startTime)){
				al.startTime=customer.visitStartTime;
			}
			al.endTime = DateUtils.now();
			long second = DateUtils.getDistanceSecondsFrom2Date(al.startTime,
					al.endTime);
			al.interval_time = String.valueOf(second);
			al.objectType = objType;
			al.objectId = objId;

			GlobalInfo.getInstance().getProfile().setActionLogVisitCustomer(al);

			ActionEvent e = new ActionEvent();
			e.viewData = al;
			e.sender = sender;
			e.userData = customer;
			e.action = ActionEventConstant.UPDATE_ACTION_LOG;
			e.isNeedCheckTimeServer = false;
			SaleController.getInstance().handleViewEvent(e);
		}
	}

	/**
	 * ghi du lieu ghe tham vao action log
	 * 
	 * @author: TruongHN
	 * @param startTime
	 * @param objectType
	 * @param objectId
	 * @param customerId
	 * @return: void
	 * @throws:
	 */
	public void requestInsertActionLog(String startTime, String objectType,
			String objectId, long customerId, String isVisitPlan) {
		if (!isInsertingActionLogVisit) {
			isInsertingActionLogVisit = true;
			ActionEvent e = new ActionEvent();
			ActionLogDTO log = new ActionLogDTO();
			log.createUser = GlobalInfo.getInstance().getProfile()
					.getUserData().userCode;
			log.aCustomer.customerId = customerId;
			log.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
			log.endTime = DateUtils.now();
			log.startTime = startTime;
			log.lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo()
					.getLastLatitude();
			log.lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo()
					.getLastLongtitude();
			log.objectId = objectId;
			log.objectType = objectType;
			log.isOr = Integer.parseInt(isVisitPlan);
			long second = DateUtils.getDistanceSecondsFrom2Date(log.startTime,
					log.endTime);
			log.interval_time = String.valueOf(second);

			e.viewData = log;
			e.sender = this;
			e.action = ActionEventConstant.INSERT_ACTION_LOG;
			e.isNeedCheckTimeServer = false;
			SaleController.getInstance().handleViewEvent(e);
		}
	}

	/**
	 * Lay cac don hang trong log
	 * 
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	public void getOrderInLogForNotify() {
		ActionEvent e = new ActionEvent();
		Bundle bundle = new Bundle();

		e.viewData = bundle;
		e.sender = this;
		e.action = ActionEventConstant.GET_ORDER_IN_LOG;

		SaleController.getInstance().handleViewEvent(e);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// bat dau tinh thoi gian idle
		GlobalInfo.getInstance().setTimeStartIdleStatus(
				System.currentTimeMillis());
		// van hoat dong gps khi home, pause chuong trinh
		GlobalInfo.getInstance().setAppActive(false);
		// PositionManager.getInstance().stop();
		System.gc();
	}

	@Override
	public void onLowMemory() {
		System.gc();
		super.onLowMemory();
	}

	@Override
	protected void onResume() {
		// neu hien thi cac dialog khac thi ko hien thi xoa app
		boolean isExistedDialog = false;
		AccessInternetService.unlockAppPrevent(false);
		GlobalInfo.getInstance().setActivityContext(this);

		// check datetime auto update
		// check datetime auto update
		if(GlobalInfo.getInstance().getStateConnectionMode() == Constants.CONNECTION_ONLINE){
			//no_log begin
			if (!GlobalUtil.isRightSettingTime()) {
				GlobalUtil.showDialogSettingTimeAutomatic();
			} else {
				// ngoai tru login view ko check time.
				if (!(this instanceof LoginView)) {
					int wrongTime = DateUtils.checkTabletRightTimeWorking();
					if (wrongTime != DateUtils.RIGHT_TIME) {
						// neu chua tung online trong ngay
						GlobalUtil.showDialogCheckWrongTime(wrongTime);
					}
				}
			}
			//no_log end
		}
		// kiem tra thoi gian cham cong & luong dinh vi(Neu khong trong thoi
		// gian cham cong
		// va dinh ki = 2 phut thi set lai la 5 phut
		if (!DateUtils.isInAttendaceTime()
				&& PositionManager.currentTimePeriod == GlobalInfo
						.getInstance().getTimeTrigPositionAttendance()) {
			PositionManager.getInstance().stop();
			PositionManager.getInstance().start();
		}
		//start service dinh vi location client
		if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType != UserDTO.TYPE_PG) {
			startFusedLocationService(false, this instanceof LoginView, true);
		}

		GlobalInfo.getInstance().lastActivity = this;
		GlobalInfo.getInstance().setAppActive(true);
		if (!PositionManager.getInstance().getIsStart()) {
			if (GlobalInfo.getInstance().getLoc() != null
					&& GlobalInfo.getInstance().getLoc().getTime() < System
							.currentTimeMillis() - VTLocating.MAX_TIME_RESET) {
				GlobalInfo.getInstance().setLoc(null);
				GlobalInfo.getInstance().getProfile().getMyGPSInfo()
						.setLongtitude(-1);
				GlobalInfo.getInstance().getProfile().getMyGPSInfo()
						.setLattitude(-1);
				GlobalInfo.getInstance().getProfile().save();
			}
			PositionManager.getInstance().start();
//			VTLog.i("yen", "PositionManager.getInstance().start();" + DateUtils.now()+ "pos" + GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude()
//					+ "," + GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude());
		}

		// check allow mock location neu truong hop tu setting quay ve ma khong
		// tick bo chon
		if (GlobalUtil.isMockLocation() && GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType != UserDTO.TYPE_PG) {
			GlobalUtil.checkAllowMockLocation();
			isExistedDialog = true;
		}
		//kiem showtra co ung dung fake location
		if (GlobalInfo.getInstance().isVaildateMockLocation() && ApplicationReceiver.isHasAppFakeLocation()
				&& GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType != UserDTO.TYPE_PG) {
			GlobalInfo.getInstance().isHasAppFakeLocation = true;
			showToastMessageLoop(StringUtil.getString(R.string.TEXT_APP_MOCK_LOCATION), 30000);
		} else {
			GlobalInfo.getInstance().isHasAppFakeLocation = false;
		}
		// kiem tra co always Finish activities enable?
		GlobalUtil.checkStatusAlwaysFinishActivities();
		// hien thi dialog ds ung dung can xoa
		// cac dialog khac chua hien thi thi moi hien thi dialog xoa ung dung
		int checkAccessApp = GlobalInfo.getInstance().getProfile().getUserData().checkAccessApp;
		if (!isExistedDialog && (checkAccessApp == UserDTO.CHECK_INSTALL 
						|| checkAccessApp == UserDTO.CHECK_ALL_ACCESS)) {
			if (!(this instanceof LoginView)) {
				initBlackListApp();
				showDialogAppUninstall(blackApps);
			}
		}
		// kiem tra neu services chua start thi start service kiem tra chan network
		if (checkAccessApp == UserDTO.CHECK_NETWORK 
						|| checkAccessApp == UserDTO.CHECK_ALL_ACCESS
						|| checkAccessApp == -1) {
			ServiceUtil.startServiceIfNotRunning(AccessInternetService.class);
		}else{
			ServiceUtil.stopService(AccessInternetService.class);
		}

		super.onResume();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		boolean isNotifyInactive = intent.getExtras().getBoolean(
				IntentConstants.INTENT_NOTIFY_INACTIVE);
		if (isNotifyInactive) {
			// request dong bo du lieu
			showProgressDialog(StringUtil.getString(R.string.updating));
			TransactionProcessManager.getInstance().startChecking(
					TransactionProcessManager.SYNC_FROM_UPDATE);
			this.synType = REQUEST_SYNC_FROM_INACTIVE_APP;
			// requestSynData(REQUEST_SYNC_FROM_INACTIVE_APP);
		}
	}

	@Override
	public void onDialogListener(int eventType, int eventCode, Object data) {
		// TODO Auto-generated method stub
		if (eventType == RESEND_TYPE) {
			ActionEvent eOld = (ActionEvent) data;
			UserController.getInstance().handleViewEvent(eOld);
		}
	}

	public void showProgressDialog(String content) {
		showProgressDialog(content, true);
	}

	public void showLoadingDialog() {
		showProgressDialog(StringUtil.getString(R.string.loading), true);
	}

	public void showToastMessage(String message) {
		GlobalUtil.showToastMessage(this, message, Toast.LENGTH_SHORT);
	}

	public void showToastMessage(String message, int time) {
		GlobalUtil.showToastMessage(this, message, time);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
		case ACTION_ALLOW_MOCK_LOCATION_OK:
			startActivity(new Intent(
					android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
			createMockLocation();
			restartLocating();
			//khong cam setting neu bi chan
			AccessInternetService.unlockAppPrevent(true);
			break;
		case ACTION_ALLOW_MOCK_LOCATION_CANCEL:
			showToastMessage(StringUtil.getString(R.string.TEXT_NO_TICE), Toast.LENGTH_LONG);
			sendBroadcast(ActionEventConstant.ACTION_FINISH_APP, new Bundle());
			finish();
			break;
		case ACTION_SHOW_LOGIN:
		case ACTION_OK_RESET_DB:
			closeProgressDialog();
			// send broadcast ra login
			sendBroadcast(ActionEventConstant.ACTION_FINISH_AND_SHOW_LOGIN,
					new Bundle());
			break;
		case ACTION_SELECTED_APP_DELETE:
			ApplicationInfo app = (ApplicationInfo)data;
			Uri packageURI = Uri.parse("package:" + app.packageName);
			Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
			startActivity(uninstallIntent);
			//neu app la fake location thuc hien tao vi tri gia
			if(ApplicationReceiver.isHasPermissionFakeLocation(app.packageName)){
				createMockLocation();
			}
			break;
		case ActionEventConstant.ACTION_REFRESH_APP_UNINSTALL:
			sendBroadcast(ActionEventConstant.ACTION_FINISH_AND_SHOW_LOGIN,
					new Bundle());
			break;
		case ACTION_CLEAR_DATA_OK:
			GlobalUtil.clearApplicationData();
			break;
		default:
			break;
		}
	}

	/*
	 * @author: BanghN Request sync data
	 */
	private void requestSynData(int notifyInactive) {
		try {
			isCancelUpdateData = false;
			Vector<String> vt = new Vector<String>();
			SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
			String lastLogId = sharedPreferences.getString(LAST_LOG_ID, "0");

			vt.add(IntentConstants.INTENT_LAST_LOG_ID);
			vt.add(lastLogId);// "21246863");

			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.ACTION_SYN_SYNDATA;
			e.isBlockRequest = true;
			e.tag = notifyInactive;
			e.viewData = vt;
			e.userData = lastLogId;
			e.sender = this;

			SynDataController.getInstance().handleViewEvent(e);
			VTLog.d("requestSynData", "requestSynData type: " + notifyInactive + " lastLogId: " + lastLogId);
		} catch (Exception ex) {
			VTLog.e("requestSynData", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
	}

	/**
	 * Lay thoi gian hien tai tren server
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	public void getCurrentDateTimeServer(ActionEvent actionEventBeforeGetTime) {
		ActionEvent e = new ActionEvent();
		Bundle bundle = new Bundle();
		bundle.putBoolean(IntentConstants.INTENT_IS_SYNC, false);
		e.viewData = bundle;
		e.sender = this;
		e.userData = actionEventBeforeGetTime;
		e.action = ActionEventConstant.ACTION_GET_CURRENT_DATE_TIME_SERVER;
		UserController.getInstance().handleViewEvent(e);
	}

	/**
	 * Notify cho ds don hang & icon notify cac don hang bi loi
	 * @author: Nguyen Thanh Dung
	 * @param notifyDTO
	 * @return: void
	 * @throws:
	 */
	public void sendBroadcaseNotifyOrder(NotifyOrderDTO notifyDTO) {
		if (notifyDTO != null) {
			// hien thi icon don hang chua goi
			ArrayList<String> listOrder = new ArrayList<String>();
			for (int i = 0, size = notifyDTO.listOrderInLog.size(); i < size; i++) {
				if (LogDTO.STATE_NEW
						.equals(notifyDTO.listOrderInLog.get(i).state)
						|| LogDTO.STATE_FAIL.equals(notifyDTO.listOrderInLog
								.get(i).state)
						|| LogDTO.STATE_INVALID_TIME
								.equals(notifyDTO.listOrderInLog.get(i).state)
						|| LogDTO.STATE_UNIQUE_CONTRAINTS
								.equals(notifyDTO.listOrderInLog.get(i).state)) {
					listOrder.add(notifyDTO.listOrderInLog.get(i).tableId);
				}
			}

			boolean hasOrderFail = listOrder.size() > 0 ? true : false;
			if (!hasOrderFail) {
				hasOrderFail = notifyDTO.numOrderReturnNPP > 0 ? true : false;
			}
			GlobalInfo.getInstance().notifyOrderReturnInfo.hasOrderFail = hasOrderFail;
			sendBroadcast(ActionEventConstant.NOTIFY_ORDER_STATE, new Bundle());
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btClose) {
			if (alertRemindDialog != null && alertRemindDialog.isShowing()) {
				alertRemindDialog.dismiss();
			}
		} else if (v == tvTitle) {
			if(!GlobalInfo.getInstance().getCurrentTag().equals(ChangePasswordView.TAG))
				gotoChangePassView();
		} else if (v == ivLogo && GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_PG) {
			if(!GlobalInfo.getInstance().getCurrentTag().equals(PGOrderView.TAG)
					&& !GlobalInfo.getInstance().getCurrentTag().equals(LoginView.class.getName())
					&& GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_PG)
				gotoPGOrderView();
		} else if(v == tvStatus && GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_PG
				&& !GlobalInfo.getInstance().getCurrentTag().equals(PGChangePasswordView.TAG)){
			gotoPGChangePassView();
		}
	}

	/**
	 * go to ChangePassView
	 * @author: TamPQ
	 * @return: voidvoid
	 * @throws:
	 */
	private void gotoChangePassView() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = new Bundle();
		e.action = ActionEventConstant.CHANGE_PASS;
		UserController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * add cac request dang xu ly vao mang request
	 * @author: BangHN
	 * @param req
	 *            , isBlock
	 * @return: void
	 * @throws:
	 */
	public void addProcessingRequest(HTTPRequest req, boolean isBlock) {
		if (isBlock) {
			blockReqs.add(req);
		} else {
			unblockReqs.add(req);
		}
	}

	/**
	 * remove all processing request
	 * @author: banghn
	 * @return: void
	 * @throws:
	 */
	public void removeAllProcessingRequest() {
		cancelRequest(blockReqs);
		cancelRequest(unblockReqs);
	}

	/**
	 * remove processing request
	 * @author: banghn
	 * @param req
	 * @return: void
	 * @throws:
	 */
	public void removeProcessingRequest(HTTPRequest req, boolean isBlock) {
		if (isBlock) {
			cancelRequest(blockReqs, req);
		} else {
			cancelRequest(unblockReqs, req);
		}
	}

	/**
	 * Cancle request mot array request
	 * @author banghn
	 * @param arrReq
	 */
	private void cancelRequest(ArrayList<HTTPRequest> arrReq) {
		HTTPRequest req = null;
		for (int i = 0, n = arrReq.size(); i < n; i++) {
			req = arrReq.get(i);
			req.setAlive(false);
		}
		arrReq.clear();
	}

	/**
	 * cancle mot request
	 * @author banghn
	 * @param arrReq
	 * @param req
	 */
	private void cancelRequest(ArrayList<HTTPRequest> arrReq, HTTPRequest req) {
		HTTPRequest curReq = null;
		for (int i = 0, n = arrReq.size(); i < n; i++) {
			curReq = arrReq.get(i);
			if (curReq == req) {
				arrReq.remove(i);
				req.setAlive(false);
				break;
			}
		}
		arrReq.clear();
	}

	/**
	 * Kiem tra co ton tai request dang xu ly hay khong
	 * @author: TruongHN
	 * @param reqAction
	 * @return: boolean
	 * @throws:
	 */
	public boolean checkExistRequestProcessing(int reqAction) {
		boolean res = false;
		HTTPRequest curReq = null;
		for (int i = 0, n = blockReqs.size(); i < n; i++) {
			curReq = blockReqs.get(i);
			if (curReq.isAlive() && curReq.getAction() == reqAction) {
				res = true;
				break;
			}
		}
		return res;
	}

	public void onCancel(DialogInterface dialog) {
		// dang hien thi progressDialog => bam nut back
		if (dialog == progressDlg) {
			cancelRequest(this.blockReqs);
			isCancelUpdateData = true;
		}
	}

	/**
	 * Hien thi thong bao khi chuong trinh Home
	 * @author: TruongHN
	 * @param action
	 * @param bundle
	 * @return: void
	 * @throws:
	 */
	public void handleNotifyInActiveView(int action, Bundle bundle) {
		// neu co setting thong bao ra man hinh chinh thi moi hien thi
		if (GlobalInfo.getInstance().getProfile() != null) {
			// rung
			vibrate();
			playSound();
			StatusNotificationHandler handler = GlobalInfo.getInstance()
					.getStatusNotifier();
			if (handler != null) {
				handler.handleAction(action, bundle, this);
			}
		}
	}

	/**
	 * Bat hieu ung rung
	 * @author: AnhND
	 * @return: void
	 * @throws:
	 */
	public void vibrate() {
		try {
			stopVibrating();
			vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATION_DURATION);
		} catch (Throwable ex) {
		}
	}

	/**
	 * Ngung hieu ung rung
	 * @author: AnhND
	 * @return: void
	 * @throws:
	 */
	protected void stopVibrating() {
		if (vibrator != null) {
			vibrator.cancel();
		}
	}

	/**
	 * bat am thanh
	 * @author: AnhND
	 * @return: void
	 * @throws:
	 */
	public void playSound() {
		try {
			Uri alert = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			if (alert != null) {
				stopPlayingSound();
				soundPlayer = new MediaPlayer();
				soundPlayer.setOnCompletionListener(this);

				soundPlayer.setDataSource(this, alert);
				final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
				if (audioManager
						.getStreamVolume(AudioManager.STREAM_NOTIFICATION) != 0) {
					soundPlayer
							.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
					soundPlayer.setLooping(false);
					soundPlayer.prepare();
					soundPlayer.start();
				}
			}
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			if (soundPlayer != null) {
				soundPlayer.release();
				soundPlayer = null;
			}
		}
	}

	/**
	 * tat am thanh
	 * @author: AnhND
	 * @return: void
	 * @throws:
	 */
	protected void stopPlayingSound() {
		if (soundPlayer != null && soundPlayer.isPlaying()) {
			soundPlayer.stop();
			soundPlayer.release();
			soundPlayer = null;
		}
	}

	@Override
	public void onCompletion(MediaPlayer paramMediaPlayer) {
		// TODO Auto-generated method stub
		if (soundPlayer != null) {
			soundPlayer.release();
		}
		soundPlayer = null;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// get the current date
			if (day == 0 || month == 0 || year == 0) {
				final Calendar c = Calendar.getInstance();
				day = c.get(Calendar.DAY_OF_MONTH);
				month = c.get(Calendar.MONTH);
				year = c.get(Calendar.YEAR);
			}
			datePickerDialog = new DatePickerDialog(this, mDateSetListener,
					year, month, day);
			return datePickerDialog;
		case TIME_DIALOG_ID:
			// get the current date
			final Calendar time = Calendar.getInstance();
			int hour = time.get(Calendar.HOUR_OF_DAY);
			int minute = time.get(Calendar.MINUTE);
			return new TimePickerDialog(this, mTimeSetListener, hour, minute,
					true);
		}
		return null;
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle bundle) {
		int mHour;
		int mMinute;
		switch (id) {
		case DATE_DIALOG_ID:
			// get the current date
			if (day == 0 || month == 0 || year == 0) {
				final Calendar c = Calendar.getInstance();
				day = c.get(Calendar.DAY_OF_MONTH);
				month = c.get(Calendar.MONTH);
				year = c.get(Calendar.YEAR);
			}
			datePickerDialog = new DatePickerDialog(this, mDateSetListener,
					year, month, day);
			return datePickerDialog;
		case TIME_DIALOG_ID:
			// get the current date
			if (bundle != null) {
				mHour = bundle.getInt(IntentConstants.INTENT_HOUR);
				mMinute = bundle.getInt(IntentConstants.INTENT_MINUTE);
			} else {
				final Calendar time = Calendar.getInstance();
				mHour = time.get(Calendar.HOUR_OF_DAY);
				mMinute = time.get(Calendar.MINUTE);
			}
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
					true);
		}
		return null;
	}

	/**
	 * DatePickerDialog
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			if (fragmentTag.equals(PromotionProgramView.TAG)) {
				PromotionProgramView pro = (PromotionProgramView) getFragmentManager()
						.findFragmentByTag(PromotionProgramView.TAG);
				pro.updateBirtday(dayOfMonth, monthOfYear, year);
			} else if (fragmentTag.equals(PostFeedbackView.TAG)) {
				PostFeedbackView v = (PostFeedbackView) getFragmentManager()
						.findFragmentByTag(PostFeedbackView.TAG);
				v.updateDate(dayOfMonth, monthOfYear, year);
			} else if (fragmentTag.equals(OrderView.TAG)) {
				OrderView v = (OrderView) getFragmentManager()
						.findFragmentByTag(OrderView.TAG);
				v.updateDate(dayOfMonth, monthOfYear, year);
			} else if (fragmentTag.equals(ListOrderView.TAG)) {
				ListOrderView v = (ListOrderView) getFragmentManager()
						.findFragmentByTag(ListOrderView.TAG);
				v.updateDate(dayOfMonth, monthOfYear, year);
			} else if (fragmentTag.equals(ImageListView.TAG)) {
				ImageListView v = (ImageListView) getFragmentManager()
						.findFragmentByTag(ImageListView.TAG);
				v.updateDate(dayOfMonth, monthOfYear, year);
			} else if (fragmentTag.equals(ListAlbumUserView.TAG)) {
				// ListAlbumUserView v = (ListAlbumUserView)
				// getFragmentManager().findFragmentByTag(ListAlbumUserView.TAG);
				// v.updateDate(dayOfMonth, monthOfYear, year);
			} else if (fragmentTag.equals(FollowProblemView.TAG)) {
				FollowProblemView pro = (FollowProblemView) getFragmentManager()
						.findFragmentByTag(FollowProblemView.TAG);
				pro.update(dayOfMonth, monthOfYear, year);
			} else if (fragmentTag.equals(TrackAndFixProblemsOfGSNPPView.TAG)) {
				TrackAndFixProblemsOfGSNPPView pro = (TrackAndFixProblemsOfGSNPPView) getFragmentManager()
						.findFragmentByTag(TrackAndFixProblemsOfGSNPPView.TAG);
				pro.updateFromDateAndEndDate(dayOfMonth, monthOfYear, year);
			} else if (fragmentTag.equals(GSNPPPostFeedbackView.TAG)) {
				GSNPPPostFeedbackView pro = (GSNPPPostFeedbackView) getFragmentManager()
						.findFragmentByTag(GSNPPPostFeedbackView.TAG);
				pro.updateDate(dayOfMonth, monthOfYear, year);
			} else if (fragmentTag.equals(ReviewsStaffView.TAG)) {
				ReviewsStaffView pro = (ReviewsStaffView) getFragmentManager()
						.findFragmentByTag(ReviewsStaffView.TAG);
				pro.updateFromDateAndEndDate(dayOfMonth, monthOfYear, year);
			} else if (fragmentTag.equals(SupervisorImageListView.TAG)) {
				SupervisorImageListView v = (SupervisorImageListView) getFragmentManager()
						.findFragmentByTag(SupervisorImageListView.TAG);
				v.updateDate(dayOfMonth, monthOfYear, year);
			} else if (fragmentTag.equals(TBHVFollowProblemView.TAG)) {
				TBHVFollowProblemView pro = (TBHVFollowProblemView) getFragmentManager()
						.findFragmentByTag(TBHVFollowProblemView.TAG);
				pro.update(dayOfMonth, monthOfYear, year);
			}
			if (fragmentTag.equals(TBHVAddRequirementView.TAG)) {
				TBHVAddRequirementView pro = (TBHVAddRequirementView) getFragmentManager()
						.findFragmentByTag(TBHVAddRequirementView.TAG);
				pro.update(dayOfMonth, monthOfYear, year);
			} else if (fragmentTag.equals(TBHVReviewsGSNPPView.TAG)) {
				TBHVReviewsGSNPPView pro = (TBHVReviewsGSNPPView) getFragmentManager()
						.findFragmentByTag(TBHVReviewsGSNPPView.TAG);
				pro.updateFromDateAndEndDate(dayOfMonth, monthOfYear, year);
			} else if (fragmentTag.equals(SupervisorImageListView.TAG)) {
				SupervisorImageListView v = (SupervisorImageListView) getFragmentManager()
						.findFragmentByTag(SupervisorImageListView.TAG);
				v.updateDate(dayOfMonth, monthOfYear, year);
			} else if (fragmentTag.equals(TNPGGeneralStatisticView.TAG)) {
				TNPGGeneralStatisticView v = (TNPGGeneralStatisticView) getFragmentManager()
						.findFragmentByTag(TNPGGeneralStatisticView.TAG);
				v.updateDate(dayOfMonth, monthOfYear, year);
			} else if (fragmentTag.equals(CustomerOfC2ListView.TAG)) {
				CustomerOfC2ListView v = (CustomerOfC2ListView) getFragmentManager()
						.findFragmentByTag(CustomerOfC2ListView.TAG);
				v.updateDate(dayOfMonth, monthOfYear, year);
			} else if (fragmentTag.equals(CustomerC2ListView.TAG)) {
				CustomerC2ListView v = (CustomerC2ListView) getFragmentManager()
						.findFragmentByTag(CustomerC2ListView.TAG);
				v.updateDate(dayOfMonth, monthOfYear, year);
			} else if (fragmentTag.equals(PGOrderView.TAG)) {
				PGOrderView pro = (PGOrderView) getFragmentManager()
						.findFragmentByTag(PGOrderView.TAG);
				pro.updateDate(dayOfMonth, monthOfYear, year);
			} else if (fragmentTag.equals(PGReportOrderView.TAG)) {
				PGReportOrderView pro = (PGReportOrderView) getFragmentManager()
						.findFragmentByTag(PGReportOrderView.TAG);
				pro.updateDate(dayOfMonth, monthOfYear, year);
			}
		}
	};

	/**
	 * DatePickerDialog
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			if (fragmentTag.equals(OrderView.TAG)) {
				OrderView v = (OrderView) getFragmentManager()
						.findFragmentByTag(OrderView.TAG);
				v.updateTime(hourOfDay, minute);

			}
		}
	};

	/**
	 * reset gia tri ngay tren date picker
	 * @author: Nguyen Thanh Dung
	 * @return: void
	 * @throws:
	 */
	public void resetDatePickerDialog() {
		int mYear;
		int mMonth;
		int mDay;
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		if (datePickerDialog != null) {
			datePickerDialog.updateDate(mYear, mMonth, mDay);
		}
	}

	/**
	 * hien thi popup date picker voi ngay tuong ung date truyen vao theo
	 * format: dd/mm/yyyy || dd-mm-yyyy || dd:mm:yyyy bien isSetDefaultDate chi
	 * co tac dung khi date = null or date = ""
	 * 
	 * @author: HaiTC3
	 * @param dialogId
	 * @param date
	 * @param isSetDefaultDate
	 * @return: void
	 * @throws:
	 * @since: Feb 20, 2013
	 */
	public void showDatePickerWithDate(int dialogId, String date,
			boolean isSetDefaultDate) {
		int numDate = 0;
		int numMonth = 0;
		int numYear = 0;
		if (!StringUtil.isNullOrEmpty(date)) {
			String[] strDate = new String[] {};
			if (date.indexOf("/") >= 0) {
				strDate = date.split("/");
			} else if (date.indexOf("-") >= 0) {
				strDate = date.split("-");
			} else if (date.indexOf(":") >= 0) {
				strDate = date.split(":");
			}
			if (strDate.length == 3) {
				numDate = Integer.parseInt(strDate[0].trim());
				numMonth = Integer.parseInt(strDate[1].trim()) - 1;
				numYear = Integer.parseInt(strDate[2].trim());
			} else {
				final Calendar c = Calendar.getInstance();
				numYear = c.get(Calendar.YEAR);
				numMonth = c.get(Calendar.MONTH);
				numDate = c.get(Calendar.DAY_OF_MONTH);
			}
			if (datePickerDialog != null) {
				datePickerDialog.updateDate(numYear, numMonth, numDate);
			} else {
				day = numDate;
				month = numMonth;
				year = numYear;
			}
		} else {
			if (isSetDefaultDate) {
				final Calendar c = Calendar.getInstance();
				day = c.get(Calendar.DAY_OF_MONTH);
				month = c.get(Calendar.MONTH);
				year = c.get(Calendar.YEAR);
				if (datePickerDialog != null) {
					datePickerDialog.updateDate(year, month, day);
				}
			}
		}
		showDialog(dialogId);
	}
	
	/**
	 * Kiem tra service dinh vi hoat dong?
	 * @author: banghn
	 * @return
	 */
	private boolean isLocationServiceRunning() {
		boolean flag=false;
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (LocationService.class.getName().equals(service.service.getClassName())) {
	            flag = true;
	            break;
	        }
	    }
	    return flag;
	}
	
	
	/**
	 * Kiem tra GooglePlayService co ton tai hay khong
	 * @author: banghn
	 * @return
	 */
	private boolean isGooglePlayServiceAvailable(boolean isShowDialog, boolean isShowToast){
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
		if(status == ConnectionResult.SUCCESS) {
		    return true;
		}
		//no_log
		else{
			String errorStr = "Kích hoạt luồng định vị Google Play Serrvices không thành công: " + GooglePlayServicesUtil.getErrorString(status);
			if (isShowDialog) {
				if(dialogCheckGooglePlay != null && dialogCheckGooglePlay.isShowing()){
					dialogCheckGooglePlay.dismiss();
				}

				dialogCheckGooglePlay = GooglePlayServicesUtil.getErrorDialog(status, this , -1);
				if (dialogCheckGooglePlay != null) {
					dialogCheckGooglePlay.show();
				}
				ServerLogger.sendLog(errorStr, TabletActionLogDTO.LOG_CLIENT);
			} else if(isShowToast){
				showToastMessage(errorStr);
			}
		}
		return false;
	}
	
	
	/**
	 * Update vi tri vao profile
	 * @author: banghn
	 * @param lng
	 * @param lat
	 * @param loc
	 */
	public void updatePosition(double lng, double lat, Location loc) {
		VTLog.logToFile("Location", DateUtils.now() + " updatePosition: " + String.valueOf(loc));
		//kiem tra toa do nam trong lanh tho VN
		if (lat < 8.45 || lng < 102.0 
				|| lat > 23.5 || lng > 110) {
			VTLog.d("Location", "updatePosition, lng = " + lng + " lat = " + lat);
			return;
		}
		// toa do fake truoc do neu co
		double fakeLat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getFakeLatitude();
		double fakeLng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getFakeLongitude();
		// toa do luu truoc do
		double lastLat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude();
		double lastLng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude();
		VTLog.d("Location","Lat-Lng : " + lat + "," + lng + "," + loc.getAccuracy()
						+ " || FakeLat-FakeLng: " + fakeLat + "," + fakeLng);
		// kiem tra ghi log vi tri len server
		if (GlobalInfo.getInstance().isVaildateMockLocation() && GlobalInfo.getInstance().isHasAppFakeLocation()
				&& lat != lastLat && lng != lastLng) {
			GlobalInfo.getInstance().getProfile().getMyGPSInfo().setFakeLongitude(lng);
			GlobalInfo.getInstance().getProfile().getMyGPSInfo().setFakeLatitude(lat);
			if(GlobalInfo.getInstance().isSendLogPosition()) {
				ServerLogger.sendLogLogin("Log có vị trí nhưng sử dụng hackgps hoặc mockloaction nên không thể lưu staff_position_log",
						"Ngày:" + DateUtils.now() +
								" Lat,lng cũ:" + lastLat + "," + lastLng +
								" Lat,lng mới:" + lat + "," + lng +
								" isVaildateMockLocation:" + GlobalInfo.getInstance().isVaildateMockLocation() +
								" isHasAppFakeLocation:" + GlobalInfo.getInstance().isHasAppFakeLocation(),
						TabletActionLogDTO.LOG_LOCATION_FAIL);
			}
		} else if (lat > 0	&& lng > 0 && lat != fakeLat && lng != fakeLng
			&& GlobalInfo.getInstance().getProfile() != null
			&& GlobalInfo.getInstance().getProfile().getUserData() != null
			&& GlobalInfo.getInstance().getProfile().getUserData().id > 0) {
			if (lat != lastLat || lng != lastLng) {
				VTLog.d("updatePosition", String.valueOf(loc));
				//showToastMessage("Vị trí mới " + loc.getProvider() + " sai số " + ((int) loc.getAccuracy()) + "m.");
				String logLocation = StringUtil.getString(R.string.TEXT_LOCATING_SUCCESS);
				if (GlobalInfo.getInstance().isDebugMode){
					logLocation += " " + loc.getProvider() + " sai số " + ((int) loc.getAccuracy()) + "m.";
				}
				showToastMessage(logLocation);
				// khoi tao staffPosition
				StaffPositionLogDTO staffPos = new StaffPositionLogDTO();
				staffPos.createDate = DateUtils.now();
				staffPos.lat = lat;
				staffPos.lng = lng;
				staffPos.accuracy = loc.getAccuracy();
				staffPos.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
				staffPos.status = GlobalUtil.locationCorrect(DateUtils.now(),lat, lng);

				
				//luu vi tri de thuc hien ket thuc tu dong
				//GlobalInfo.getInstance().addStaffPosition(staffPos);
				if (GlobalUtil.checkNetworkAccess() &&
						GlobalInfo.getInstance().getProfile().getUserData().loginState == UserDTO.LOGIN_SUCCESS
						&& GlobalInfo.getInstance().getStateConnectionMode() == Constants.CONNECTION_ONLINE
						&& AsyncTaskUtil.checkAllowDoTask()
						){
					// Neu hard code thi comment dong duoi
					//no_log
					sendPositionToServer(staffPos);
					VTLog.logToFile("Location", DateUtils.now() + " updatePosition: " + String.valueOf(loc) + " send staffPos: " + staffPos);
				}else{
					// h hien tai
					int currentHour = DateUtils.getCurrentTimeByTimeType(Calendar.HOUR_OF_DAY);
					//check thoi gian day vi tri trong thoi gian cho phep
					if (currentHour >= GlobalInfo.getInstance().getBeginTimeAllowSynData()
							&& currentHour < GlobalInfo.getInstance().getEndTimeAllowSynData()) {
						//luu vi tri de dong bo offline
						GlobalInfo.getInstance().addPosition(staffPos);
						VTLog.logToFile("Location",  DateUtils.now() + " updatePosition: " + String.valueOf(loc) + " save staffPos: " + staffPos);
					}
					if(GlobalInfo.getInstance().isSendLogPosition()) {
						ServerLogger.sendLogLogin("Log có vị trí lưu offline staff_position_log",
								"Ngày:" + DateUtils.now() +
										" Lat,lng cũ:" + lastLat + "," + lastLng +
										" Lat,lng mới:" + lat + "," + lng +
										" isVaildateMockLocation:" + GlobalInfo.getInstance().isVaildateMockLocation() +
										" isHasAppFakeLocation:" + GlobalInfo.getInstance().isHasAppFakeLocation() +
										" currentHour:" + currentHour +
										" Có network hay ko:" + GlobalUtil.checkNetworkAccess() +
										" State login:" + GlobalInfo.getInstance().getProfile().getUserData().loginState +
										" Chọn loại kết nối:" + GlobalInfo.getInstance().getStateConnectionMode() +
										" Allowdotask:" + AsyncTaskUtil.checkAllowDoTask(),
								TabletActionLogDTO.LOG_LOCATION_FAIL);
					}
				}
				// no_log
				if(staffPos.status == 1) {
					GlobalInfo.getInstance().setLoc(loc);
					GlobalInfo.getInstance().getProfile().getMyGPSInfo().setLongtitude(lng);
					GlobalInfo.getInstance().getProfile().getMyGPSInfo().setLattitude(lat);
					GlobalInfo.getInstance().getProfile().getMyGPSInfo().setAccuracy(loc.getAccuracy());
					GlobalInfo.getInstance().getProfile().save();
					if (GlobalInfo.getInstance().isSendLogPosition()) {
						ServerLogger.sendLogLogin("Log ghi thời gian định vị - GlobalBaseActivity - updatePosition", "Vị trí hợp lệ, lưu vào GPSInfo - ngày: "
								+ DateUtils.now() + "-lat,lng: " + GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude()
								+ "," + GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude(), TabletActionLogDTO.LOG_LOCATION);
					}
				}
				Bundle bd = new Bundle();
				if (GlobalInfo.getInstance().getActivityContext() instanceof GlobalBaseActivity) {
					((GlobalBaseActivity) GlobalInfo.getInstance().getActivityContext())
							.sendBroadcast(ActionEventConstant.ACTION_UPDATE_POSITION, bd);
					if (GlobalInfo.getInstance().isSendLogPosition()) {
						ServerLogger.sendLogLogin("Log ghi thời gian định vị - GlobalBaseActivity - updatePosition", "Vị trí hợp lệ, sendBroadcast cập nhật vị trí - ngày: "
								+ DateUtils.now() + "-lat,lng: " + GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude()
								+ "," + GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude(), TabletActionLogDTO.LOG_LOCATION);
					}
				}
			} else{
				VTLog.d("updatePosition fail", String.valueOf(loc));
				VTLog.logToFile("Location", DateUtils.now() + " old updatePosition: " + String.valueOf(loc));
			}
		}else{
			if(GlobalInfo.getInstance().isSendLogPosition()) {
				ServerLogger.sendLogLogin("Log có vị trí nhưng không thỏa điều kiện nên không thể lưu staff_position_log",
						"Ngày:" + DateUtils.now() +
								" Lat,lng cũ:" + lastLat + "," + lastLng +
								" Lat,lng mới:" + lat + "," + lng +
								" Lat,lng fake:" + fakeLat + "," + fakeLng +
								" isVaildateMockLocation:" + GlobalInfo.getInstance().isVaildateMockLocation() +
								" isHasAppFakeLocation:" + GlobalInfo.getInstance().isHasAppFakeLocation() +
								" profile:" + GlobalInfo.getInstance().getProfile().getUserData() +
								" userData:" + GlobalInfo.getInstance().getProfile().getUserData() +
								" id:" + GlobalInfo.getInstance().getProfile().getUserData().id,
						TabletActionLogDTO.LOG_LOCATION_FAIL);
			}
		}
	}
	
	
	/**
	 * Tao request position de goi len server
	 * @author: BangHN
	 * @param staffPos
	 * @return: void
	 * @throws:
	 */
	public void sendPositionToServer(StaffPositionLogDTO staffPos) {
		// h hien tai
		int currentHour = DateUtils
				.getCurrentTimeByTimeType(Calendar.HOUR_OF_DAY);
		// cho phep request day toa do tu beginTime - > endTime h
		if (currentHour >= GlobalInfo.getInstance().getBeginTimeAllowSynData()
				&& currentHour < GlobalInfo.getInstance()
						.getEndTimeAllowSynData()) {
			// id
			String id = GlobalUtil.generateLogId();
			// khoi tao sql
			JSONArray json = new JSONArray();
			json.put(staffPos.generateCreateSql());
			Vector<Object> para = new Vector<Object>();
			para.add(IntentConstants.INTENT_LIST_SQL);
			para.add(json);
			para.add(IntentConstants.INTENT_MD5);
			para.add(StringUtil.md5(json.toString()));
			para.add(IntentConstants.INTENT_LOG_ID);
			para.add(id);
			para.add(IntentConstants.INTENT_IMEI_PARA);
			para.add(GlobalInfo.getInstance().getDeviceIMEI());

			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.ACTION_UPDATE_POSITION;
			e.sender = this;
			e.userData = staffPos;
			e.viewData = para;
			if (GlobalInfo.IS_SEND_DATA_VERSION) {
				UserController.getInstance().handleViewEvent(e);// comment dong nay de khong day vi tri
			}
		}
	}
	
	/**
	 * request ghi log KPI
	 * 
	 * @author: duongdt3
	 * @since: 1.0
	 * @time: 16:50:36 23 Jun 2014
	 * @return: void
	 * @throws:
	 * @param type
	 * @param startTimeFromBoot
	 */
	public void requestInsertLogKPI(HashMapKPI type, long startTimeFromBoot) {
		GlobalUtil.requestInsertLogKPI(type, startTimeFromBoot);
	}
	
	/**
	 * Xu ly thoat ung dung
	 * 
	 * @author: banghn
	 */
	public void processExitApp() {
		SharedPreferences sharedPreferences = getSharedPreferences(LoginView.PREFS_PRIVATE, Context.MODE_PRIVATE);
		String dateClearData = sharedPreferences.getString(DATE_CLEAR_OLD_DATA, "");
		String dateNow = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		if (dateClearData.equals(dateNow)) {
			sendBroadcast(ActionEventConstant.ACTION_FINISH_APP, new Bundle());
		} else {
			requestDeleteOldLogTable();
		}
	}

	/**
	 * Request xoa bo nhung record cu trong bang log_table, action log
	 * 
	 * @author banghn
	 */
	public void requestDeleteOldLogTable() {
		showProgressDialog(StringUtil.getString(R.string.TEXT_EXITTING));
		LogFile.logToFile(DateUtils.now()
				+ " || Bắt đầu thực hiện xóa dữ liệu cũ");
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.ACTION_DELETE_OLD_LOG_TABLE;
		e.viewData = new Bundle();
		e.sender = GlobalBaseActivity.this;
		UserController.getInstance().handleViewEvent(e);
	}
	
	
	/**
	 * Load native lib
	 * @author: duongdt3
	 * @since: 09:32:59 4 Dec 2014
	 * @return: boolean
	 * @throws:  
	 * @return
	 */
	public boolean loadNativeLib(){
//		boolean isHaveNativeLib = isLoadedNativeLib ;
//		if (!isHaveNativeLib) {
//			 isHaveNativeLib = ExternalStorage.checkNativeLib(this);
//			// nếu đã có thư viện trong Internal Storage thì check load ICU sqlcipher
//			if (isHaveNativeLib) {
//				isHaveNativeLib = ExternalStorage.checkIcuLibVaild(this);
//			}
//		}
//		//set isLoadedNativeLib from load libs
//		isLoadedNativeLib = isHaveNativeLib;
//		return isHaveNativeLib;
		return true;
	}
	
	/**
	 * showBeginProgressPercentDialog
	 * @author: yennth16
	 * @since: 17:32:17 09-04-2015
	 * @return: void
	 * @throws:  
	 * @param content
	 */
	public void showBeginProgressPercentDialog(String content) {
		VTLog.d("showBeginProgressPercentDialog", "begin");
		updateProgressPercentDialog(0);
		showProgressPercentDialog(content, true);
	}
	
	
	/**
	 * Cap nhat lai thoi gian dung sau khi re-login
	 * tranh truong hop sai thoi gian login offline
	 * @author: banghn
	 * @param dateTime
	 */
	protected void updateTimeAfterRelogin(String dateTime){
		// Check thoi gian
		RightTimeInfo info = new RightTimeInfo();
		if (!StringUtil.isNullOrEmpty(dateTime)) {
			// luu lai thoi gian cuoi cung login online server
			info.lastTimeOnlineLogin = dateTime;
		} else {
			// luu lai thoi gian cuoi cung login online local
			info.lastTimeOnlineLogin = DateUtils.now();
		}
		// cap nhat thoi gian dung cuoi cung
		info.lastRightTime = GlobalInfo.getInstance().lastTimeOnlineLogin;
		// cap nhat thoi gian tu khi boot
		info.lastRightTimeSinceBoot = SystemClock.elapsedRealtime();

		// luu lai profile de auto login lan sau
		SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
		Editor prefsPrivateEditor = sharedPreferences.edit();

		// them luu thong tin last login online
		prefsPrivateEditor.putString(LoginView.DMS_LAST_LOGIN_ONLINE, info.lastTimeOnlineLogin);
		prefsPrivateEditor.putString(LoginView.DMS_LAST_RIGHT_TIME, info.lastRightTime);
		prefsPrivateEditor.putLong(LoginView.DMS_LAST_LOGIN_ONLINE_FROM_BOOT, info.lastRightTimeSinceBoot);
		prefsPrivateEditor.putString(LoginView.DMS_SV_DATE, DateUtils.now());
		prefsPrivateEditor.commit();
	}
	
	/**
	 * showToastMessage
	 * @author: 
	 * @since: 14:18:05 15-04-2015
	 * @return: void
	 * @throws:  
	 * @param message
	 * @param duration
	 */
	public void showToastMessageLoop(String message, int duration){
		final Toast tag = Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT);
        tag.show();
        //Create the count down timer here
		new CountDownTimer(duration, 1000) {
			public void onTick(long millisUntilFinished) {
				tag.show();
			}
			public void onFinish() {
				tag.show();
			}
		}.start();
	}
	
	/**
	 * Tao vi tri gia sau khi phat hien mock location
	 * @author: banghn
	 */
	private void createMockLocation(){
		final MockLocationProvider mockGPS = new MockLocationProvider(
				MockLocationProvider.VNM_GPS, this);
	    //Set test location
		mockGPS.pushLocation(-1, -1);
		// Create the count down timer here
		new CountDownTimer(15000, 1000) {
			public void onTick(long millisUntilFinished) {
				if(mockGPS != null){
					mockGPS.pushLocation(-1, -1);
				}
			}
			public void onFinish() {
				if(ApplicationReceiver.isHasAppFakeLocation()){
					createMockLocation();
				}
			}
		}.start();

	    //kick lai dinh vi
	    PositionManager.getInstance().reStart();
	}
	
	
	/**
	 * Hien thi dialog app can xoa
	 * @author: Tuanlt11
	 * @param listApp
	 * @return: void
	 * @throws:
	 */
	public void showDialogAppUninstall(List<ApplicationInfo> listApp) {
		if (listApp != null && !listApp.isEmpty()) {
			if (alertBackListAppDialog == null) {
				Builder build = new AlertDialog.Builder(this,
						R.style.CustomDialogTheme);
				blackListView = new BlackListAppView(this,
						ACTION_SELECTED_APP_DELETE);
				build.setView(blackListView.viewLayout);
				alertBackListAppDialog = build.create();
				alertBackListAppDialog.setCancelable(false);
				Window window = alertBackListAppDialog.getWindow();
				window.setBackgroundDrawable(new ColorDrawable(Color.argb(0,
						255, 255, 255)));
				window.setLayout(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				window.setGravity(Gravity.CENTER);
			}
			blackListView.renderLayout(listApp);
			if (!alertBackListAppDialog.isShowing())
				alertBackListAppDialog.show();
		} else {
			if (alertBackListAppDialog != null
					&& alertBackListAppDialog.isShowing()) {
				alertBackListAppDialog.dismiss();
			}
		}
	}
	
	/**
	 * Khoi tao danh sach ung dung white-list cai dat, 
	 * black-list ung dung chan network
	 * @author: banghn
	 */
	private void initListApplication(){
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance()
				.getProfile().getUserData().shopId);
		b.putInt(IntentConstants.INTENT_ROLE_TYPE, GlobalInfo.getInstance()
				.getProfile().getUserData().chanelObjectType);
		b.putLong(IntentConstants.INTENT_USER_ID, GlobalInfo.getInstance()
				.getProfile().getUserData().id);
		SQLUtils.getInstance().initApplicationGuard(b);
	}
	
	/**
	 * Khoi tao mang blacklistcu
	 * @author: Tuanlt11
	 * @return: void
	 * @throws:
	 */
	public void initBlackListApp() {
		blackApps.clear();
		List<ApplicationInfo> packages = packageManager
				.getInstalledApplications(PackageManager.GET_META_DATA);
		StringBuffer strApp = new StringBuffer();
		//StringBuffer strAppSystem = new StringBuffer();
		int sWhiteList = GlobalInfo.getInstance().getWhiteList().size();
		if (sWhiteList > 0) {
			String myPackage = this.getPackageName();
			for (ApplicationInfo packageInfo : packages) {
				if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 1) {
					boolean isExisted = false;
					for (int i = 0; i < sWhiteList; i++) {
						// kiem tra ung dung co trong whitelist ko, neu co thi
						// bo qua
						if (packageInfo.packageName.contains(GlobalInfo
								.getInstance().getWhiteList().get(i))
								|| packageInfo.packageName.contains(myPackage)) {
							isExisted = true;
							break;
						}
					}
					if (!isExisted) {
						blackApps.add(packageInfo);
						strApp.append(packageInfo.loadLabel(packageManager)
								+ ":" + packageInfo.packageName + "\n");
					}
				} 
			}
		}
		String strLogApp = "";
		// bo di dau phay cuoi cung
		if (strApp.length() > 1) {
			strApp.setLength(strApp.length() - 1);
			strLogApp += "App Install: " + strApp.toString() + "\n";
			if (!GlobalInfo.getInstance().isFirstTimeSendApp) {
				ServerLogger.sendLog("AppPrevent", strLogApp, true,
						TabletActionLogDTO.LOG_CLIENT);
				GlobalInfo.getInstance().isFirstTimeSendApp = true;
			}
		}
	}
	boolean isUserApp(ApplicationInfo ai)
	{
		int mask = ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP;
		return (ai.flags & mask) == 0;
	}
	public enum AppType
	{
		ALL {
			@Override
			public String toString() {
				return "ALL";
			}
		},
		USER {
			@Override
			public String toString() {
				return "USER";
			}
		},
		SYSTEM {
			@Override
			public String toString() {
				return "SYSTEM";
			}
		}
	}
	/**
	 * Hien thi thong bao vui long dang nhap lai
	 * @author: dungnt19
	 * @since: 1.0
	 * @return: AlertDialog
	 * @throws:
	 * @param mes
	 * @return
	 */
	public AlertDialog showDialogMissingDeviceId(final CharSequence mes) {
		if (isFinishing()) {
			return null;
		}
		AlertDialog alertDialog = null;
		try {
			alertDialog = new AlertDialog.Builder(this).create();
			// alertDialog.setTitle("Thông báo");
			alertDialog.setMessage(mes);
			alertDialog.setButton(
					StringUtil.getString(R.string.TEXT_BUTTON_CLOSE),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

							return;

						}
					});
			alertDialog.show();
		} catch (Exception e) {
			VTLog.w("", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return alertDialog;
	}
	
	/**
	 * start service định vị fused, 
	 * nếu lần thứ 2 gọi sẽ truyền data tới service đang chạy, không khởi chạy service cái mới
	 * @author: duongdt3
	 * @since: 13:40:26 5 Nov 2014
	 * @return: void
	 * @throws:  
	 * @param isRestart
	 * @param isShowDialog
	 * @param isShowToast
	 */
	public void startFusedLocationService(boolean isRestart, boolean isShowDialog, boolean isShowToast) {
		if (isGooglePlayServiceAvailable(isShowDialog, isShowToast)) {
			Intent service = new Intent(GlobalInfo.getInstance()
					.getAppContext(), LocationService.class);
			service.putExtra(IntentConstants.RADIUS_OF_POSITION_FUSED, GlobalInfo.getInstance().getRadiusFusedPosition());
			service.putExtra(IntentConstants.FAST_INTERVAL_FUSED_POSITION, GlobalInfo.getInstance().getFastIntervalFusedPosition());
			service.putExtra(IntentConstants.INTERVAL_FUSED_POSITION, GlobalInfo.getInstance().getIntervalFusedPosition());
			service.putExtra(IntentConstants.FUSED_POSTION_PRIORITY, GlobalInfo.getInstance().getFusedPositionPriority());
			service.putExtra(IntentConstants.FUSED_POSTION_RESTART, isRestart);
			startService(service);
		}
	}
	
	/**
	 * stop service dinh vi
	 * @author: yennth16
	 * @since: 10:13:58 12-03-2015
	 * @return: void
	 * @throws:
	 */
	public void stopFusedLocationService() {
		if(isLocationServiceRunning()){
			Intent service = new Intent(GlobalInfo.getInstance()
					.getAppContext(), LocationService.class);
		    stopService(service);
		}
	}
	
	//Thong bao location
	protected class NotifyLocation extends AsyncTask<Void, Void, Exception> {
		StaffPositionLogDTO lastLog;
		StaffPositionLogDTO lastLogPosition;
		@Override
		protected Exception doInBackground(Void... params) {
				// vi tri cuoi cung
			if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_PRESALES) {
				lastLog = SQLUtils.getInstance().getAttendancePosition();
				lastLogPosition = SQLUtils.getInstance().getLastPosition();
			} else {
				lastLog = SQLUtils.getInstance().getLastPosition();
				lastLogPosition = null;
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Exception result) {
			if (result == null && (lastLog != null || lastLogPosition != null)) {
				notifyLastLogPosition(lastLog, lastLogPosition);
				boolean isInsertAttendancePosition = false;
				if(lastLog != null && GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_PRESALES) {
					isInsertAttendancePosition = SQLUtils.getInstance().insertAttendancePosition(lastLog.workLogDTO);
					if(lastLog.workLogDTO != null) {
						lastLog.workLogId = lastLog.workLogDTO.workLogId;
					}
					if(isInsertAttendancePosition) {
						sendWorkLog(lastLog.workLogDTO);
					}
				}
				ArrayList<WorkLogDTO> list = new ArrayList<>();
				if(lastLog != null) {
					list = SQLUtils.getInstance().deleteWorkLog(lastLog.workLogId);
				}else{
					list = SQLUtils.getInstance().deleteWorkLog(0);
				}
				if(list.size() > 0){
					deleteWorkLog(list);
				}
			}else{
				ViewGroup viewGroup = (ViewGroup) rl_header.findViewById(R.id.llGPSInfo);
				viewGroup.setVisibility(View.GONE);
			}
		}
	}
	
	/**
	 * Thong bao vi tri gan day day len server
	 * @author: 
	 * @param log
	 */
	protected void notifyLastLogPosition(StaffPositionLogDTO log, StaffPositionLogDTO lastLogPosition) {
		if (log != null && lastLogPosition != null) {
			setGPSRecentInfo(log.createDate, lastLogPosition.createDate);
		} else if (log != null) {
			setGPSRecentInfo(log.createDate, null);
		} else if (lastLogPosition != null) {
			setGPSRecentInfo(null, lastLogPosition.createDate);
		}
	}
	
	/**
	 * Set title bar
	 * @author: yennth16
	 * @since: 15:56:27 17-04-2015
	 * @return: void
	 * @throws:  
	 * @param val
	 */
	public void setTitleName(CharSequence val) {
		if (val == null) {
			val = "Bia SG -" + GlobalInfo.getInstance().getProfile().getUserData().userCode;
		}
		TextView title = (TextView) rl_header.findViewById(R.id.tvTitle);
		title.setText(val);
		title.setVisibility(View.VISIBLE);
	}

	public void setGPSRecentInfo(CharSequence val, CharSequence log) {
		ViewGroup viewGroup = (ViewGroup) rl_header.findViewById(R.id.llGPSInfo);
		TextView title = (TextView) rl_header.findViewById(R.id.tvGPSRecent);
		if (val != null) {
			if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_PRESALES) {
				if(log != null){
					title.setText(StringUtil.getString(R.string.UPDATE) + ": " + log +
							" - " + StringUtil.getString(R.string.TEXT_TAKE_ATTENDANCE) + ": " + val);
				}else{
					title.setText(StringUtil.getString(R.string.TEXT_TAKE_ATTENDANCE) + ": " + val);
				}
			} else {
				title.setText(StringUtil.getString(R.string.UPDATE) + ": " + val);
			}
			viewGroup.setVisibility(View.VISIBLE);
		}else if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_PRESALES
				&& log != null){
			title.setText(StringUtil.getString(R.string.UPDATE) + ": " + log);
			viewGroup.setVisibility(View.VISIBLE);
		}
	}
	public void setReGPSRecentInfo() {
		ViewGroup viewGroup = (ViewGroup) rl_header.findViewById(R.id.llGPSInfo);
		TextView title = (TextView) rl_header.findViewById(R.id.tvGPSRecent);
		title.setText("");
		viewGroup.setVisibility(View.GONE);
	}
	public void setShopSelected(CharSequence val) {
		if (val != null) {
			ViewGroup viewGroup = (ViewGroup) rl_header.findViewById(R.id.llShopSelected);
			TextView tvShopSelected = (TextView) rl_header.findViewById(R.id.tvShopSelected);
			tvShopSelected.setText(val);
			viewGroup.setVisibility(View.VISIBLE);
		}
	}

	public void restartLocating(boolean isShowDialog){
		if (timerGPSRequest != null){
			timerGPSRequest.cancel();
		}
		restartLocating();
		if (isShowDialog) {
			showGPSProgressDialog();
			//khoi tao gps request timer de thong bao loi
			timerGPSRequest =  new CountDownTimer(PositionManager.TIME_OUT_SHOW_LOCATING, 1000) {

				public void onTick(long millisUntilFinished) {
					double lat = GlobalInfo.getInstance().getProfile()
							.getMyGPSInfo().getLatitude();
					double lng = GlobalInfo.getInstance().getProfile()
							.getMyGPSInfo().getLongtitude();
					if (lat > 0 && lng > 0) {
						cancel();
						closeGPSProgressDialog();
					} else {
						restartLocating();
					}
				}

				public void onFinish() {
					//chua co vi tri, dong progress dialog
					closeGPSProgressDialog();
					//hien thi thong bao dinh vi chua duoc
					showToastMessage(StringUtil.getString(R.string.TEXT_REQUEST_LOCATING_ERROR), Toast.LENGTH_LONG);
				}
			}.start();
		}
	}

	private void restartLocating(){
		//restart PositionManager
		PositionManager.getInstance().reStart();

		//restart FusedLocationService
		startFusedLocationService(true, false, false);

	}

	ProgressDialog progressDlgGPSLocating = null;

	public void closeGPSProgressDialog() {
		if (progressDlgGPSLocating != null && progressDlgGPSLocating.isShowing()) {
			progressDlgGPSLocating.dismiss();
		}
	}

	public void showGPSProgressDialog() {
		if (isFirstCallDialogGPS){
			isFirstCallDialogGPS = false;
		} else {
			closeGPSProgressDialog();

			if (!isFinishing()) {
				if (progressDlgGPSLocating == null) {
					progressDlgGPSLocating = new ProgressDialog(this);
					progressDlgGPSLocating.setMessage(StringUtil.getString(R.string.TEXT_REQUEST_LOCATING));
					progressDlgGPSLocating.setCancelable(true);
					progressDlgGPSLocating.setCanceledOnTouchOutside(false);
				}
				if (progressDlgGPSLocating != null) {
					progressDlgGPSLocating.show();
				}
			}
		}
	}

	/**
	 * sendWorkLog
	 * @param staffPos
     */
	public void sendWorkLog(WorkLogDTO staffPos) {
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.ACTION_INSERT_WORK_LOG;
		e.viewData =  staffPos;
		e.sender = GlobalBaseActivity.this;
		UserController.getInstance().handleViewEvent(e);
	}
	/**
	 * xoa WorkLog
	 */
	public void deleteWorkLog(ArrayList<WorkLogDTO> list) {
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.ACTION_DELETE_WORK_LOG;
		e.viewData =  list;
		e.sender = GlobalBaseActivity.this;
		UserController.getInstance().handleViewEvent(e);
	}
	/**
	 * Xoa du lieu ung dung
	 */
	public void clearData(){
		GlobalUtil.showDialogConfirm(this, StringUtil.getString(R.string.TEXT_CONFIRM_CLEAR_DATA),
				StringUtil.getString(R.string.TEXT_AGREE), ACTION_CLEAR_DATA_OK,
				StringUtil.getString(R.string.TEXT_DENY), ACTION_CLEAR_DATA_CANCEL, null);
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

	/**
	 * go to ChangePassView
	 * @author: TamPQ
	 * @return: voidvoid
	 * @throws:
	 */
	private void gotoPGChangePassView() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = new Bundle();
		e.action = ActionEventConstant.PG_CHANGE_PASS;
		UserController.getInstance().handleSwitchFragment(e);
	}

}
