/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseIntArray;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.download.ExternalStorage;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.ActionLogDTO;
import com.viettel.dms.dto.db.ApParamDTO;
import com.viettel.dms.dto.db.CustomerAvatarDTO;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.CustomerInputQuantityDTO;
import com.viettel.dms.dto.db.CustomerInputQuantityDetailDTO;
import com.viettel.dms.dto.db.CustomerListDoneProgrameDTO;
import com.viettel.dms.dto.db.CustomerStockHistoryDTO;
import com.viettel.dms.dto.db.DisplayProgrameDTO;
import com.viettel.dms.dto.db.DisplayProgrameLvDTO;
import com.viettel.dms.dto.db.FeedBackDTO;
import com.viettel.dms.dto.db.FeedBackDetailDTO;
import com.viettel.dms.dto.db.FeedBackTBHVDTO;
import com.viettel.dms.dto.db.LogDTO;
import com.viettel.dms.dto.db.MediaItemDTO;
import com.viettel.dms.dto.db.PayReceivedDTO;
import com.viettel.dms.dto.db.PaymentDetailDTO;
import com.viettel.dms.dto.db.ProCusAttendDTO;
import com.viettel.dms.dto.db.ProCusHistoryDTO;
import com.viettel.dms.dto.db.ProCusMapDTO;
import com.viettel.dms.dto.db.ProDisplayProgrameDTO;
import com.viettel.dms.dto.db.ProDisplayProgrameDetailDTO;
import com.viettel.dms.dto.db.ProInfoDTO;
import com.viettel.dms.dto.db.ProductCompetitorDTO;
import com.viettel.dms.dto.db.ProductCompetitorListDTO;
import com.viettel.dms.dto.db.ProductDTO;
import com.viettel.dms.dto.db.PromotionProDetailDTO;
import com.viettel.dms.dto.db.PromotionProgrameDTO;
import com.viettel.dms.dto.db.RoutingCustomerDTO;
import com.viettel.dms.dto.db.SaleOrderDTO;
import com.viettel.dms.dto.db.SaleOrderDetailDTO;
import com.viettel.dms.dto.db.ShopDTO;
import com.viettel.dms.dto.db.ShopParamDTO;
import com.viettel.dms.dto.db.StaffCustomerDTO;
import com.viettel.dms.dto.db.StaffPositionLogDTO;
import com.viettel.dms.dto.db.StockTotalDTO;
import com.viettel.dms.dto.db.SuggestedPriceDTO;
import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.dto.db.TimeKeeperDTO;
import com.viettel.dms.dto.db.TrainingShopManagerResultDTO;
import com.viettel.dms.dto.db.WorkLogDTO;
import com.viettel.dms.dto.me.NotifyOrderDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.photo.PhotoDTO;
import com.viettel.dms.dto.view.*;
import com.viettel.dms.dto.view.CreateCustomerInfoDTO.AreaItem;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.MeasuringTime;
import com.viettel.dms.util.ServerLogger;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.sale.customer.CustomerFeedBackDto;
import com.viettel.dms.view.sale.order.ListOrderView;
import com.viettel.map.dto.GeomDTO;
import com.viettel.map.dto.LatLng;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;

/**
 * Cac ham truy xuat contact trong SQLLite
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class SQLUtils {
	private static final String LOG_TAG = "SQLUtils";
	// private static DatabaseHelper mDbHelper;
	private static SQLiteDatabase mDB;
	private static SQLUtils instance = null;
	public boolean isProcessingTrans = false;

	private static final Object lockObject = new Object();

	public static SQLUtils getInstance() {
		if (instance == null) {
			synchronized (lockObject) {
				if (instance == null) {
					instance = new SQLUtils();
				}
			}
		}

		if (mDB == null || !mDB.isOpen()) {
			synchronized (lockObject) {
				if (mDB == null || !mDB.isOpen()) {
					try {
//						// int a =
//						// NativeCode.createFunction(ExternalStorage.getFileDBPath(GlobalInfo.getInstance().getAppContext())
//						// .getAbsolutePath() + "/" + Constants.DATABASE_NAME);
//						SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
//							@Override
//							public void preKey(SQLiteDatabase db) {
//								db.rawExecSQL(String.format("PRAGMA key = '%s'", Constants.CIPHER_KEY));
//								db.rawExecSQL("PRAGMA cipher = 'rc4'");
//							}
//
//							@Override
//							public void postKey(SQLiteDatabase db) {
//								db.rawExecSQL(String.format("PRAGMA key = '%s'", Constants.CIPHER_KEY));
//								db.rawExecSQL("PRAGMA cipher = 'rc4'");
//							}
//						};
//						mDB = SQLiteDatabase.openDatabase(ExternalStorage.getFileDBPath(GlobalInfo.getInstance().getAppContext()).getAbsolutePath()
//								+ "/" + Constants.DATABASE_NAME, Constants.CIPHER_KEY, null, SQLiteDatabase.OPEN_READWRITE, hook);
						SQLiteDatabase.loadLibs(GlobalInfo.getInstance().getApplicationContext());
						mDB = SQLiteDatabase.openDatabase(ExternalStorage.getFileDBPath(GlobalInfo.getInstance().getAppContext()).getAbsolutePath()
								+ "/" + Constants.DATABASE_NAME, Constants.CIPHER_KEY, null, SQLiteDatabase.OPEN_READWRITE, null);
						// String m = "";
						// mDB.execSQL("PRAMA journal_mode = OFF");
						mDB.execSQL("analyze;");
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
					}
				}
			}
		}
		return instance;
	}

	public SQLiteDatabase getmDB() {
		return mDB;
	}

	// ------------PRIVATE------------
	/*
	 * cai dat cay phan nhanh thuc thi cac nghiep vu DB cho table
	 */
	/**
	 * Insert dto (value of row) to table
	 * 
	 * @author: TruongHN
	 * @return: long
	 * @throws:
	 */
	private long insertDTO(AbstractTableDTO tableDTO) {
		long res = -1;
		if (AbstractTableDTO.TableType.CUSTOMER.equals(tableDTO.getType())) {
			CUSTOMER_TABLE table = new CUSTOMER_TABLE(mDB);
			res = table.insert(tableDTO);
		} else if (AbstractTableDTO.TableType.SALE_ORDER.equals(tableDTO.getType())) {
			SALE_ORDER_TABLE table = new SALE_ORDER_TABLE(mDB);
			res = table.insert(tableDTO);
		} else if (AbstractTableDTO.TableType.SALE_ORDER_DETAIL.equals(tableDTO.getType())) {
			SALES_ORDER_DETAIL_TABLE table = new SALES_ORDER_DETAIL_TABLE(mDB);
			res = table.insert(tableDTO);
		} else if (AbstractTableDTO.TableType.PRODUCT.equals(tableDTO.getType())) {
			PRODUCT_TABLE table = new PRODUCT_TABLE(mDB);
			res = table.insert(tableDTO);
		} else if (AbstractTableDTO.TableType.CUSTOMER_DISPLAY_PROGRAME_SCORE.equals(tableDTO.getType())) {
			CUSTOMER_DISPLAY_PROGRAME_SCORE_TABLE table = new CUSTOMER_DISPLAY_PROGRAME_SCORE_TABLE(mDB);
			res = table.insert(tableDTO);
		} else if (AbstractTableDTO.TableType.TRAINING_RESULT_TABLE.equals(tableDTO.getType())) {
			TRAINING_RESULT_TABLE table = new TRAINING_RESULT_TABLE(mDB);
			res = table.insert(tableDTO);
		} else if (AbstractTableDTO.TableType.FEEDBACK_TABLE.equals(tableDTO.getType())) {
			FEED_BACK_TABLE table = new FEED_BACK_TABLE(mDB);
			res = table.insert(tableDTO);
		} else if (AbstractTableDTO.TableType.FEEDBACK_DETAIL_TABLE.equals(tableDTO.getType())) {
			FEEDBACK_DETAIL_TABLE table = new FEEDBACK_DETAIL_TABLE(mDB);
			res = table.insert(tableDTO);
		} else if (AbstractTableDTO.TableType.LOG.equals(tableDTO.getType())) {
			LOG_TABLE table = new LOG_TABLE(mDB);
			res = table.insert(tableDTO);
		} else if (AbstractTableDTO.TableType.TRAINING_SHOP_MANAGER_RESULT_TABLE.equals(tableDTO.getType())) {
			TRAINING_SHOP_MANAGER_RESULT_TABLE table = new TRAINING_SHOP_MANAGER_RESULT_TABLE(mDB);
			res = table.insert(tableDTO);
		} else if (AbstractTableDTO.TableType.CUSTOMER_STOCK_HISTORY_TABLE.equals(tableDTO.getType())) {
			CUSTOMER_STOCK_HISTORY_TABLE table = new CUSTOMER_STOCK_HISTORY_TABLE(mDB);
			res = table.insert(tableDTO);
		}
		return res;
	}

	/**
	 * Update dto (value of row) to table
	 * 
	 * @author: TruongHN
	 * @param tableDTO
	 * @return: long (-1 neu that bai)
	 * @throws:
	 */
	private long updateDTO(AbstractTableDTO tableDTO) {
		long res = -1;
		if (AbstractTableDTO.TableType.CUSTOMER.equals(tableDTO.getType())) {
			CUSTOMER_TABLE table = new CUSTOMER_TABLE(mDB);
			res = table.update(tableDTO);
		} else if (AbstractTableDTO.TableType.SALE_ORDER.equals(tableDTO.getType())) {
			SALE_ORDER_TABLE table = new SALE_ORDER_TABLE(mDB);
			res = table.update(tableDTO);
		} else if (AbstractTableDTO.TableType.SALE_ORDER_DETAIL.equals(tableDTO.getType())) {
			SALES_ORDER_DETAIL_TABLE table = new SALES_ORDER_DETAIL_TABLE(mDB);
			res = table.update(tableDTO);
		} else if (AbstractTableDTO.TableType.PRODUCT.equals(tableDTO.getType())) {
			PRODUCT_TABLE table = new PRODUCT_TABLE(mDB);
			res = table.update(tableDTO);
		} else if (AbstractTableDTO.TableType.CUSTOMER_DISPLAY_PROGRAME_SCORE.equals(tableDTO.getType())) {
			CUSTOMER_DISPLAY_PROGRAME_SCORE_TABLE table = new CUSTOMER_DISPLAY_PROGRAME_SCORE_TABLE(mDB);
			res = table.update(tableDTO);
		} else if (AbstractTableDTO.TableType.FEEDBACK_TABLE.equals(tableDTO.getType())) {
			FEED_BACK_TABLE table = new FEED_BACK_TABLE(mDB);
			res = table.update(tableDTO);
		} else if (AbstractTableDTO.TableType.TRAINING_RESULT_TABLE.equals(tableDTO.getType())) {
			TRAINING_RESULT_TABLE table = new TRAINING_RESULT_TABLE(mDB);
			res = table.update(tableDTO);
		} else if (AbstractTableDTO.TableType.LOG.equals(tableDTO.getType())) {
			LOG_TABLE table = new LOG_TABLE(mDB);
			res = table.update(tableDTO);
		}
		return res;
	}

	/**
	 * Delete 1 DTO
	 * 
	 * @author: TruongHN
	 * @param tableDTO
	 * @return: long (-1 neu that bai)
	 * @throws:
	 */
	private long deleteDTO(AbstractTableDTO tableDTO) {
		long res = -1;
		if (AbstractTableDTO.TableType.CUSTOMER.equals(tableDTO.getType())) {
			CUSTOMER_TABLE table = new CUSTOMER_TABLE(mDB);
			res = table.delete(tableDTO);
		} else if (AbstractTableDTO.TableType.SALE_ORDER.equals(tableDTO.getType())) {
			SALE_ORDER_TABLE table = new SALE_ORDER_TABLE(mDB);
			res = table.delete(tableDTO);
		} else if (AbstractTableDTO.TableType.SALE_ORDER_DETAIL.equals(tableDTO.getType())) {
			SALES_ORDER_DETAIL_TABLE table = new SALES_ORDER_DETAIL_TABLE(mDB);
			res = table.delete(tableDTO);
		} else if (AbstractTableDTO.TableType.PRODUCT.equals(tableDTO.getType())) {
			PRODUCT_TABLE table = new PRODUCT_TABLE(mDB);
			res = table.delete(tableDTO);
		} else if (AbstractTableDTO.TableType.TRAINING_RESULT_TABLE.equals(tableDTO.getType())) {
			TRAINING_RESULT_TABLE table = new TRAINING_RESULT_TABLE(mDB);
			res = table.delete(tableDTO);
		} else if (AbstractTableDTO.TableType.FEEDBACK_TABLE.equals(tableDTO.getType())) {
			FEED_BACK_TABLE table = new FEED_BACK_TABLE(mDB);
			res = table.delete(tableDTO);
		} else if (AbstractTableDTO.TableType.FEEDBACK_DETAIL_TABLE.equals(tableDTO.getType())) {
			FEEDBACK_DETAIL_TABLE table = new FEEDBACK_DETAIL_TABLE(mDB);
			res = table.delete(tableDTO);
		} else if (AbstractTableDTO.TableType.LOG.equals(tableDTO.getType())) {
			LOG_TABLE table = new LOG_TABLE(mDB);
			res = table.delete(tableDTO);
		} else if (AbstractTableDTO.TableType.TRAINING_SHOP_MANAGER_RESULT_TABLE.equals(tableDTO.getType())) {
			TRAINING_SHOP_MANAGER_RESULT_TABLE table = new TRAINING_SHOP_MANAGER_RESULT_TABLE(mDB);
			res = table.delete(tableDTO);
		}
		return res;
	}

	/**
	 * Thuc thi mot mot hanh dong insert, update, delete mot doi tuong vao bang
	 * 
	 * @author: TruongHN
	 * @param dto
	 * @return: long (-1 neu that bai)
	 * @throws:
	 */
	private long execute(AbstractTableDTO dto) {
		long res = -1;
		if (AbstractTableDTO.TableAction.INSERT.equals(dto.getAction())) {
			res = insertDTO(dto);
		}
		if (AbstractTableDTO.TableAction.UPDATE.equals(dto.getAction())) {
			res = updateDTO(dto);
		}
		if (AbstractTableDTO.TableAction.DELETE.equals(dto.getAction())) {
			res = deleteDTO(dto);
		}
		return res;
	}

	// ------------PUBLIC-------------//
	/**
	 * Insert dto (value of row) to table
	 * 
	 * @author: BangHN
	 * @param tableDTO
	 * @return: void
	 * @throws:
	 */
	public synchronized void insert(AbstractTableDTO tableDTO) {
		if (mDB != null && tableDTO != null && mDB.isOpen()) {
			try {
				isProcessingTrans = true;
				mDB.beginTransaction();
				insertDTO(tableDTO);
				mDB.setTransactionSuccessful();
			} finally {
				if (mDB != null && mDB.inTransaction()) {
					mDB.endTransaction();
				}
				isProcessingTrans = false;
			}
			if (GlobalInfo.getInstance().isExitApp) {
				closeDB();
			}
		}
	}

	/**
	 * Insert dto (value of row) to table
	 * 
	 * @author : BangHN since : 11:52:29 AM
	 */
	public synchronized void update(AbstractTableDTO tableDTO) {
		if (mDB != null && tableDTO != null && mDB.isOpen() && !mDB.isReadOnly()) {
			try {
				isProcessingTrans = true;
				mDB.beginTransaction();
				updateDTO(tableDTO);
				mDB.setTransactionSuccessful();
			} finally {
				if (mDB != null && mDB.inTransaction()) {
					mDB.endTransaction();
				}
				isProcessingTrans = false;
			}
			if (GlobalInfo.getInstance().isExitApp) {
				closeDB();
			}
		}
	}

	/**
	 * Delelte 1 doi tuong
	 * 
	 * @author: TruongHN
	 * @param tableDTO
	 * @return: long (-1 neu that bai)
	 * @throws:
	 */
	public synchronized long delete(AbstractTableDTO tableDTO) {
		long res = -1;
		if (mDB != null && tableDTO != null && mDB.isOpen()) {
			try {
				isProcessingTrans = true;
				mDB.beginTransaction();
				res = deleteDTO(tableDTO);
				mDB.setTransactionSuccessful();
			} finally {
				if (mDB != null && mDB.inTransaction()) {
					mDB.endTransaction();
				}
				isProcessingTrans = false;
			}
			if (GlobalInfo.getInstance().isExitApp) {
				closeDB();
			}
		}
		return res;
	}

	/**
	 * Thuc thi mot mot hanh dong insert, update, delete mot doi tuong vao bang
	 * 
	 * @author: BangHN
	 * @param dto
	 * @return: long (-1 neu that bai)
	 * @throws:
	 */
	public synchronized long executeDTO(AbstractTableDTO dto) {
		long res = -1;
		if (mDB != null && dto != null && mDB.isOpen()) {
			try {
				isProcessingTrans = true;
				mDB.beginTransaction();
				res = execute(dto);
				mDB.setTransactionSuccessful();

			} finally {
				if (mDB != null && mDB.inTransaction()) {
					mDB.endTransaction();
				}
				isProcessingTrans = false;
			}
			if (GlobalInfo.getInstance().isExitApp) {
				closeDB();
			}
		}
		return res;
	}

	/**
	 * Thuc thi mot mot hanh dong insert, update, delete mot doi tuong vao bang
	 * 
	 * @author: TruongHN
	 * @param listDTO
	 * @return: void
	 * @throws:
	 */
	public synchronized void executeListDTO(ArrayList<AbstractTableDTO> listDTO) {
		if (mDB != null && listDTO != null && mDB.isOpen() && !mDB.isReadOnly()) {
			try {
				isProcessingTrans = true;
				mDB.beginTransaction();
				for (AbstractTableDTO dto : listDTO) {
					execute(dto);
				}
				mDB.setTransactionSuccessful();
			} finally {
				if (mDB != null && mDB.inTransaction()) {
					mDB.endTransaction();
				}
				isProcessingTrans = false;
			}
			if (GlobalInfo.getInstance().isExitApp) {
				closeDB();
			}
		}
	}

	/**
	 * Thuc hien 1 cau lenh sql tong quat
	 * 
	 * @author: TruongHN
	 * @param sqlQuery
	 * @param params
	 * @return
	 * @return: Cursor
	 * @throws:
	 */
	public Cursor rawQuery(String sqlQuery, String[] params) {
		Cursor cursor = null;
		if (mDB != null && mDB.isOpen()) {
			cursor = mDB.rawQuery(sqlQuery, params);
		}
		return cursor;
	}

	/**
	 * Xoa du lieu tu cau lenh sql
	 * 
	 * @author: TruongHN
	 * @param tableName
	 * @param whereClause
	 * @param whereArgs
	 * @return: int
	 * @throws:
	 */
	public int delete(String tableName, String whereClause, String[] whereArgs) {
		int delete = -1;
		if (mDB != null && mDB.isOpen()) {
			try {
				isProcessingTrans = true;
				mDB.beginTransaction();
				delete = mDB.delete(tableName, whereClause, whereArgs);
				VTLog.i(LOG_TAG, "delete: " + String.valueOf(delete));
				mDB.setTransactionSuccessful();
			} finally {
				if (mDB != null && mDB.inTransaction()) {
					mDB.endTransaction();
				}
				isProcessingTrans = false;
			}
			if (GlobalInfo.getInstance().isExitApp) {
				closeDB();
			}
		}
		return delete;
	}

	/**
	 * Xoa du lieu tu cau lenh sql, roi xoa luon bang con tham chieu
	 * 
	 * @author: PhucNT
	 * @param tableName
	 * @param whereClause
	 * @param whereArgs
	 * @return: int
	 * @throws:
	 */
	private boolean deleteCascade(String tableName, String whereClause, String[] whereArgs, String[] childTables) {
		boolean result = true;

		int delete = mDB.delete(tableName, whereClause, whereArgs);

		VTLog.i(LOG_TAG, "delete bang cha" + String.valueOf(delete));
		if (delete > 0) {
			for (int i = 0; i < childTables.length; i++) {
				String tbChild = childTables[i];
				int delete2 = mDB.delete(tbChild, whereClause, whereArgs);
				VTLog.i(LOG_TAG, "Delete bang con" + String.valueOf(delete2));

				if (delete2 <= 0) {
					result = false;
					break;
				}
			}
		} else {
			result = false;
		}

		return result;
	}

	/**
	 * Update table tu cau lenh sql
	 * 
	 * @author: TruongHN
	 * @param tableName
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @return: int
	 * @throws:
	 */
	public int update(String tableName, ContentValues values, String whereClause, String[] whereArgs) {
		int upda = -1;
		if (mDB != null && mDB.isOpen()) {
			try {
				isProcessingTrans = true;
				mDB.beginTransaction();
				upda = mDB.update(tableName, values, whereClause, whereArgs);
				VTLog.i(LOG_TAG, "Truong, update - " + String.valueOf(upda));
				mDB.setTransactionSuccessful();
			} finally {
				if (mDB != null && mDB.inTransaction()) {
					mDB.endTransaction();
				}
				isProcessingTrans = false;
			}
			if (GlobalInfo.getInstance().isExitApp) {
				closeDB();
			}
		}
		return upda;
	}

	/**
	 * Insert thong tin tu cau lenh sql
	 * 
	 * @author: TruongHN
	 * @param nameTable
	 * @param nullColumnHack
	 * @param values
	 * @return: long
	 * @throws:
	 */
	public long insert(String nameTable, String nullColumnHack, ContentValues values) {
		long insert = -1;
		if (mDB != null && mDB.isOpen()) {
			try {
				isProcessingTrans = true;
				mDB.beginTransaction();
				insert = mDB.insert(nameTable, nullColumnHack, values);
				VTLog.i(LOG_TAG, "Truong, insert - " + String.valueOf(insert));
				mDB.setTransactionSuccessful();
			} finally {
				if (mDB != null && mDB.inTransaction()) {
					mDB.endTransaction();
				}
				isProcessingTrans = false;
			}
			if (GlobalInfo.getInstance().isExitApp) {
				closeDB();
			}

		}
		return insert;
	}

	/**
	 * Close DB
	 * 
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	public void closeDB() {
		try {
			if (mDB != null) {
				if (mDB.inTransaction()) {
					mDB.endTransaction();
				}
				if (mDB.isOpen()) {
					mDB.close();
				}
				mDB = null;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * ------------------------------------------------------------------------
	 * ------------- CAI DAT CAC FUNCTION PHUC VU VIEC LAY DU LIEU TU NGHIEP VU
	 * -- ----------------------------------------------------------------------
	 * -- -----------
	 */

	/**
	 * Lay thong tin customer trong : CUSTOMER & CUSTOMER_TYPE
	 * 
	 * @author banghn
	 */
	public CustomerInfoDTO getCustomerInfo(String customerId, String shopId) {
		CUSTOMER_TABLE cusTable = new CUSTOMER_TABLE(mDB);
		CustomerInfoDTO customerInfo = null;
		try {
			customerInfo = cusTable.getCustomerInfo(customerId, shopId);
		} catch (Exception e) {
			return null;
		}
		return customerInfo;
	}

	/**
	 * lay thong tin cua khach hang
	 * 
	 * @author banghn
	 *            cua khach hang since : 1.0
	 */
	public CustomerDTO getCustomerById(String customerId) {
		CUSTOMER_TABLE cusTable = new CUSTOMER_TABLE(mDB);
		CustomerDTO customerDTO = cusTable.getCustomerById(customerId);
		return customerDTO;
	}

	/**
	 * get init cac textview de thuc hien autoComplete
	 * 
	 * @author: HieuNH
	 * @param ext
	 * @return
	 * @return: ListFindProductSaleOrderDetailViewDTO
	 * @throws:
	 */
	public AutoCompleteFindProductSaleOrderDetailViewDTO getInitListProductAddToOrderView(Bundle ext) {
		// sale order detail - lay ds order detail
		SALES_ORDER_DETAIL_TABLE saleOrderDetailTable = new SALES_ORDER_DETAIL_TABLE(mDB);

		try {
			AutoCompleteFindProductSaleOrderDetailViewDTO vt = saleOrderDetailTable.getInitListProductAddToOrderView(ext);
			return vt;

		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));

			return null;
		}
	}

	/**
	 * get list product to add order list
	 * 
	 * @author: HaiTC3
	 * @param ext
	 * @return
	 * @return: ListFindProductSaleOrderDetailViewDTO
	 * @throws:
	 */
	public ListFindProductSaleOrderDetailViewDTO getListProductAddToOrderView(Bundle ext) {
		// sale order detail - lay ds order detail
		SALES_ORDER_DETAIL_TABLE saleOrderDetailTable = new SALES_ORDER_DETAIL_TABLE(mDB);

		try {
			ListFindProductSaleOrderDetailViewDTO vt = saleOrderDetailTable.getListProductAddToOrderView(ext);
			return vt;

		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));

			return null;
		}
	}

	/**
	 * 
	 * get list display programe product
	 * 
	 * @author: HaiTC3
	 * @param ext
	 * @return
	 * @return: ListFindProductSaleOrderDetailViewDTO
	 * @throws:
	 */
	public List<DisplayPresentProductInfo> getListDisplayProgramProduct(Bundle ext) {
		// sale order detail - lay ds order detail
		DISPLAY_CUSTOMER_MAP_TABLE customerDisplayPrograme = new DISPLAY_CUSTOMER_MAP_TABLE(mDB);

		try {
			List<DisplayPresentProductInfo> vt = new ArrayList<DisplayPresentProductInfo>();
			vt = customerDisplayPrograme.getListDisplayProgramProduct(ext);
			return vt;

		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));

			return null;
		}
	}

	/**
	 * Lay danh sach chuong trinh trung bay cua 1 khach hang
	 * 
	 * @author: quangvt1
	 * @since: 14:09:53 09-05-2014
	 * @return: VoteDisplayPresentProductViewDTO
	 * @throws:
	 * @return
	 */
	public VoteDisplayPresentProductViewDTO getVoteDisplayProgrameProductViewData(String idStaff, String idCustomer) {
		DISPLAY_CUSTOMER_MAP_TABLE customerDisplayPrograme = new DISPLAY_CUSTOMER_MAP_TABLE(mDB);
		VoteDisplayPresentProductViewDTO vt = null;
		try {
			vt = customerDisplayPrograme.getVoteDisplayPresentProductView(idStaff, idCustomer);
		} catch (Exception e) {
			ServerLogger.sendLog("method getVoteDisplayProgrameProductViewData error: ", e.toString(), TabletActionLogDTO.LOG_CLIENT);
		}

		return vt;
	}

	/**
	 * Lay danh sach san pham cham trung bay cua 1 chuong trinh
	 * 
	 * @author: quangvt1
	 * @since: 16:07:56 09-05-2014
	 * @return: VoteDisplayPresentProductViewDTO
	 * @throws:
	 * @param programId
	 * @return
	 */
	public DisplayPresentProductInfo getListVoteDisplayProduct(String programId) {
		DISPLAY_CUSTOMER_MAP_TABLE customerDisplayPrograme = new DISPLAY_CUSTOMER_MAP_TABLE(mDB);
		DisplayPresentProductInfo vt = null;

		try {
			vt = customerDisplayPrograme.getListVoteDisplayProduct(programId);
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

		return vt;
	}

	/**
	 * 
	 * inset voted display programe product into DB
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: int
	 * @throws:
	 */
	public int insertVoteDisplayProgrameProduct(ListCustomerDisplayProgrameScoreDTO listDisplayProductVoted) {
		int kq = 0;
		if (listDisplayProductVoted != null) {
			try {
				mDB.beginTransaction();
				boolean checkSuccess = true;
				for (int i = 0, size = listDisplayProductVoted.getListCusDTO().size(); i < size; i++) {
					CustomerStockHistoryDTO displayProduct = listDisplayProductVoted.getListCusDTO().get(i);
					checkSuccess = this.insertDTO(displayProduct) >= 0 ? true
							: false;
					if (!checkSuccess) {
						break;
					}
				}
				// this.updateMaxIdForCustomerDisplayPrograme(listDisplayProductVoted.maxCustomerDisplayProgID);
				if (checkSuccess) {
					mDB.setTransactionSuccessful();
					kq = 1;
				}
			} catch (Exception e) {
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
				kq = 0;
			} finally {
				if (mDB != null && mDB.inTransaction()) {
					mDB.endTransaction();
				}
			}
		}
		return kq;
	}

	/**
	 * 
	 * update id for list customer display programe score
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: ListCustomerDisplayProgrameScoreDTO
	 * @throws:
	 */
	public ListCustomerDisplayProgrameScoreDTO updateCustomerDisplayProgrameScoreDTO(ListCustomerDisplayProgrameScoreDTO listDisplayProductVoted) {
		TABLE_ID tableId = new TABLE_ID(mDB);
		if (listDisplayProductVoted != null) {
			try {
				long cdpsMaxId = tableId.getMaxId(CUSTOMER_STOCK_HISTORY_TABLE.TABLE_NAME);
				for (int i = 0, size = listDisplayProductVoted.getListCusDTO().size(); i < size; i++) {
					listDisplayProductVoted.getListCusDTO().get(i).customerStockHistoryId = cdpsMaxId;
					cdpsMaxId++;
				}
			} catch (Exception e) {
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			}
		}
		return listDisplayProductVoted;
	}

	/**
	 * Luu thong tin cham chuong trinh TB
	 * 
	 * @author: quangvt1
	 * @since: 16:03:32 12-05-2014
	 * @return: ListCustomerDisplayProgrameScoreDTO
	 * @throws:
	 * @return
	 */
	public boolean voteCustomerDisplayProgrameScoreDTO(ProDisplayProgrameDTO programe) {
		boolean isSuccess = false;
		TABLE_ID tableID = new TABLE_ID(mDB);
		long programeId = tableID.getMaxId(PRO_DISPLAY_PROGRAM_TABLE.PRO_DISPLAY_PROGRAM_TABLE);
		long detailId = tableID.getMaxId(PRO_DISPLAY_PROGRAM_DETAIL_TABLE.PRO_DISPLAY_PROGRAM_DETAIL_TABLE);
		programe.displayProgrameId = programeId;
		for (ProDisplayProgrameDetailDTO detail : programe.listDetail) {
			detail.displayProgrameId = programeId;
			detail.displayProgrameDetailId = detailId++;
		}

		PRO_DISPLAY_PROGRAM_TABLE programeTable = new PRO_DISPLAY_PROGRAM_TABLE(mDB);
		isSuccess = programeTable.insertVotePrograme(programe);

		return isSuccess;
	}

	/**
	 * getListPromotionPrograme
	 * 
	 * @author: ThuatTQ
	 * @param ext
	 * @return: List<SaleOrderDetailDTO>
	 * @throws:
	 */
	public SaleSupportProgramModel getListSaleSupportProgram(String shopId, String ext, boolean checkLoadMore) {
		PROMOTION_PROGRAME_TABLE promotionProgrameTable = new PROMOTION_PROGRAME_TABLE(mDB);
		// SHOP_TABLE shopTable = new SHOP_TABLE(mDB);

		// ArrayList<String> shopIdArray = shopTable.getShopRecursive(shopId);
		return promotionProgrameTable.getListSaleSupportPrograme(shopId, ext, checkLoadMore);
	}

	/**
	 * 
	 * Lay ds chuong trinh KM cua GSNPP
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param ext
	 * @param checkLoadMore
	 * @return
	 * @return: PromotionProgrameModel
	 * @throws:
	 */
	public PromotionProgrameModel getSupervisorListPromotionPrograme(String shopId, String ext, boolean checkLoadMore) {
		PROMOTION_PROGRAME_TABLE promotionProgrameTable = new PROMOTION_PROGRAME_TABLE(mDB);
		SHOP_TABLE shopTable = new SHOP_TABLE(mDB);

		ArrayList<String> shopIdArray = shopTable.getShopRecursive(shopId);
		return promotionProgrameTable.getListSupervisorPromotionPrograme(shopIdArray, ext, checkLoadMore);
	}

	/**
	 * 
	 * Lay ds chuong trinh KM
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param shopId
	 * @param checkLoadMore
	 * @return
	 * @return: TBHVPromotionProgrameModel
	 * @throws:
	 */
	public TBHVPromotionProgrameDTO getTBHVListPromotionPrograme(String shopId, int page, boolean checkLoadMore) {
		PROMOTION_PROGRAME_TABLE promotionProgrameTable = new PROMOTION_PROGRAME_TABLE(mDB);

		return promotionProgrameTable.getListTBHVPromotionPrograme(shopId, page, checkLoadMore);
	}

	/**
	 * getListDisplayProgrameItem
	 * 
	 * @author: ThuatTQ
	 * @param ext
	 * @return: List<DisplayProgrameModel>
	 * @throws:
	 */
	public DisplayProgrameItemModel getListDisplayProgrameItem(Bundle ext) {

		DISPLAY_PROGRAME_PRODUCT_TABLE displayProductTable = new DISPLAY_PROGRAME_PRODUCT_TABLE(mDB);
		return displayProductTable.getListDisplayProgrameItem(ext);
	}

	/**
	 * getListDisplayProgrameItem
	 * 
	 * @author: SoaN
	 * @param ext
	 * @return: List<DisplayProgrameModel>
	 * @throws:
	 */
	public DisplayProgrameModel getListDisplayPrograme(Bundle ext) {
		SHOP_TABLE shopTable = new SHOP_TABLE(mDB);
		DISPLAY_PROGRAME_TABLE promotionProgrameTable = new DISPLAY_PROGRAME_TABLE(mDB);

		String shopId = ext.getString(IntentConstants.INTENT_SHOP_ID);
		ArrayList<String> shopIdArray = shopTable.getShopRecursive(shopId);

		DisplayProgrameModel model = promotionProgrameTable.getListDisplayPrograme(ext, shopIdArray);

		// List<DisplayProgrameDTO> modelData = model.getModelData();
		// int length = modelData.size();
		// for (int i = 0; i < length; i++) {
		// DisplayProgrameDTO dpDTO = modelData.get(i);
		// int quatity = customerDisplayProgrameTable
		// .getCountCustomerAttentPrograme(dpDTO.displayProgrameCode,
		// "" + staffId);
		// dpDTO.quantity = quatity;
		// }
		return model;
	}

	/**
	 * Lay ds sale order de chinh sua
	 * 
	 * @author: BangHN
	 * @param orderId
	 * @throws Exception
	 * @return: ListSaleOrderDTO
	 * @throws:
	 */
	public ListSaleOrderDTO getSaleOrderForEdit(long orderId) {
		ListSaleOrderDTO list = new ListSaleOrderDTO();

		SALE_ORDER_TABLE saleOrderTable = new SALE_ORDER_TABLE(mDB);

		try {
			SaleOrderDTO saleOrderDTO = saleOrderTable.getSaleById(orderId);
			if (saleOrderDTO != null) {
				list.saleOrderDTO = saleOrderDTO;
				SALES_ORDER_DETAIL_TABLE orderDetailTable = new SALES_ORDER_DETAIL_TABLE(mDB);

				List<OrderDetailViewDTO> listProduct = orderDetailTable.getListProductForEdit(Long.toString(orderId), saleOrderDTO.orderType);

				list.listData.addAll(listProduct);
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

		return list;
	}

	/**
	 * 
	 * Lay ds san pham ban de chuyen len server
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param orderId
	 * @return
	 * @throws Exception
	 * @return: ListSaleOrderDTO
	 * @throws:
	 */
	public ListSaleOrderDTO getSaleOrderForSend(long orderId) {
		ListSaleOrderDTO list = new ListSaleOrderDTO();

		SALE_ORDER_TABLE saleOrderTable = new SALE_ORDER_TABLE(SQLUtils.getInstance().getmDB());

		try {
			SaleOrderDTO saleOrderDTO = saleOrderTable.getSaleById(orderId);
			if (saleOrderDTO != null) {
				list.saleOrderDTO = saleOrderDTO;

				SALES_ORDER_DETAIL_TABLE orderDetailTable = new SALES_ORDER_DETAIL_TABLE(mDB);

				List<OrderDetailViewDTO> listProduct = orderDetailTable.getListProductForSend(Long.toString(orderId));
				list.listData.addAll(listProduct);
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

		return list;
	}

//	/**
//	 * Get MaxId trong table khi truyen vao tableName & ten column
//	 *
//	 * @author: BangHN
//	 * @param tableName
//	 * @param columnIdName
//	 * @throws Exception
//	 * @return: int
//	 * @throws:
//	 */
//	public int getMaxIdInTable(String tableName, String columnIdName)
//			throws Exception {
//		int maxId = -1;
//		Cursor cursor = null;
//		try {
//			StringBuilder sqlState = new StringBuilder();
//			sqlState.append("select ifnull(max(" + columnIdName + "), 0) as "
//					+ columnIdName + " from " + tableName);
//			cursor = rawQuery(sqlState.toString(), null);
//
//			if (cursor != null) {
//				if (cursor.moveToFirst()) {
//					maxId = cursor.getInt(cursor.getColumnIndex(columnIdName));
//				}
//			}
//		} catch (Exception ex) {
//			throw ex;
//		} finally {
//			if (cursor != null) {
//				cursor.close();
//			}
//		}
//		return maxId;
//	}

	/**
	 * Lay danh sach san pham NVBH, GSBH
	 * 
	 * @author: YenNTH
	 * @return: ListProductDTO
	 * @throws:
	 */
	public ListProductDTO getProductList(Bundle ext) {
		// sale order detail - lay ds order detail
		PRODUCT_TABLE productTable = new PRODUCT_TABLE(mDB);
		SHOP_TABLE shopTable = new SHOP_TABLE(mDB);
		ArrayList<String> shopIdArray = shopTable.getShopRecursive(ext.getString(IntentConstants.INTENT_SHOP_ID));
		ListProductDTO dto = null;
		try {
			dto = productTable.getProductList(ext, shopIdArray);
		} catch (Exception e) {
		} finally {
		}
		return dto;
	}

	/**
	 * get list product storage GSNPP
	 * 
	 * @author: HieuNH
	 * @return: ListProductDTO
	 * @throws:
	 */
	public ListProductDTO getSupervisorProductList(Bundle ext) {
		// sale order detail - lay ds order detail
		PRODUCT_TABLE productTable = new PRODUCT_TABLE(mDB);
		SHOP_TABLE shopTable = new SHOP_TABLE(mDB);
		ArrayList<String> shopIdArray = shopTable.getShopRecursive(ext.getString(IntentConstants.INTENT_SHOP_ID));
		ListProductDTO dto = null;
		try {
			dto = productTable.getSupervisorProductList(ext, shopIdArray);
		} catch (Exception e) {
		} finally {
		}
		return dto;
	}

	/**
	 * 
	 * Lay ds sp cua TBHV
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param ext
	 * @return
	 * @return: ListProductDTO
	 * @throws:
	 */
	public ListProductDTO getTBHVListProductStorage(Bundle ext) {
		// sale order detail - lay ds order detail
		PRODUCT_TABLE saleOrderDetailTable = new PRODUCT_TABLE(mDB);
		ListProductDTO dto = null;
		dto = saleOrderDetailTable.getTBHVProductList(ext);
		return dto;
	}

	/**
	 * get list nganh hang, nganh hang con
	 * 
	 * @author: HieuNH
	 * @return: CategoryCodeDTO
	 * @throws:
	 */
	public CategoryCodeDTO getListCategoryCodeProduct() {
		// sale order detail - lay ds order detail
		SALES_ORDER_DETAIL_TABLE saleOrderDetailTable = new SALES_ORDER_DETAIL_TABLE(mDB);

		try {
			CategoryCodeDTO vt = saleOrderDetailTable.getListCategoryCodeProduct();
			return vt;

		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));

			return null;
		}
	}

	/**
	 * 
	 * get Remain Product
	 * 
	 * @author: HieuNH
	 * @param ext
	 * @return
	 * @return: ListProductDTO
	 * @throws:
	 */
	public ListRemainProductDTO getRemainProduct(Bundle ext) {
		// sale order detail - lay ds order detail
		SALES_ORDER_DETAIL_TABLE saleOrderDetailTable = new SALES_ORDER_DETAIL_TABLE(mDB);

		try {
			return saleOrderDetailTable.getRemainProduct(ext);
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			ServerLogger.sendLog("method sqlUtil getRemainProduct exception: ", e.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			return null;
		}
	}

	/**
	 * Mo ta chuc nang cua ham
	 * 
	 * @author: SoaN
	 * @param ext
	 * @return
	 * @return: DisplayProgrameDTO
	 * @throws:
	 */

	public DisplayProgrameDTO getDisplayProgrameInfo(Bundle ext) {
		// sale order detail - lay ds order detail
		DISPLAY_PROGRAME_TABLE programe_TABLE = new DISPLAY_PROGRAME_TABLE(mDB);
		String code = ext.getString(IntentConstants.INTENT_PROMOTION_CODE);
		try {
			DisplayProgrameDTO vt = programe_TABLE.getRowById(code);
			return vt;

		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			return null;
		}
	}

	/**
	 * Mo ta chuc nang cua ham
	 * 
	 * @author: SoaN
	 * @param ext
	 * @return
	 * @return: DisplayProgrameLvDTO
	 * @throws:
	 */

	public List<DisplayProgrameLvDTO> getDisplayLvByProgrameCode(Bundle ext) {
		// sale order detail - lay ds order detail
		DISPLAY_PROGRAME_LEVEL_TABLE level_TABLE = new DISPLAY_PROGRAME_LEVEL_TABLE(mDB);
		String code = ext.getString(IntentConstants.INTENT_PROMOTION_CODE);
		try {
			return level_TABLE.getListRowById(code);
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			return null;
		}
	}

	/**
	 * Thuc hien lay nhung don hang gan day cua khach hang
	 * 
	 * @author : BangHN since : 1:50:41 PM
	 */
	public int getCountLastSaleOrders(String customerId, String shopId, String staffId) {
		int count = 0;
		SALE_ORDER_TABLE orderTable = new SALE_ORDER_TABLE(mDB);
		try {
			count = orderTable.getCountLastSaleOrders(customerId, shopId, staffId);
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return count;
	}

	/**
	 * 
	 * get promotion programe detail info
	 * 
	 * @author: HaiTC3
	 * @param ext
	 * @return
	 * @return: PromotionProgrameDTO
	 * @throws:
	 */
	public PromotionProgrameDTO getPromotionProgrameDetail(Bundle ext) {
		PROMOTION_PROGRAME_TABLE promotionProgrameTable = new PROMOTION_PROGRAME_TABLE(mDB);

		try {
			PromotionProgrameDTO vt = promotionProgrameTable.getPromotionProgrameDetail(ext);
			return vt;

		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));

			return null;
		}
	}

	/**
	 * Thuc hien lay nhung don hang gan day cua khach hang
	 * 
	 * @author : BangHN since : 1:50:41 PM
	 */
	public ArrayList<SaleOrderCustomerDTO> getLastSaleOrders(String customerId, String shopId, int page, int numTop) {
		SALE_ORDER_TABLE orderTable = new SALE_ORDER_TABLE(mDB);
		try {
			ArrayList<SaleOrderCustomerDTO> oders = orderTable.getLastSaleOrders(customerId, shopId, page, numTop);
			return oders;
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			return null;
		}
	}

	/**
	 * Lay chuong trinh khach hang dang tham gia
	 * 
	 * @param customerId
	 *            : id khach hang
	 * @return
	 */
	public ArrayList<CustomerProgrameDTO> getCustomerProgrames(String customerId) {
		CUSTOMER_DISPLAY_PROGRAME_TABLE customerPrograme = new CUSTOMER_DISPLAY_PROGRAME_TABLE(mDB);
		return customerPrograme.getCustomerProgrames(customerId);
	}

	/**
	 * Tinh doanh so chuong trinh khach hang dang tham gia
	 * 
	 * @author banghn
	 * @param programId
	 *            : chuong trinh tham gia
	 * @param isFino
	 *            co phai la fino hay ko
	 * @param hasFino
	 *            : chuong trinh nay co fino hay khong
	 * @return doanh so da dat
	 */
	public long caculateAmountAchievedOfPrograme(String customerId, String programId, boolean isFino, boolean hasFino) {
		CUSTOMER_DISPLAY_PROGRAME_TABLE customerPrograme = new CUSTOMER_DISPLAY_PROGRAME_TABLE(mDB);
		return customerPrograme.caculateAmountAchievedOfPrograme(customerId, ""
				+ programId, isFino, hasFino);
	}

	/**
	 * Insert order have detail into table sale_order & sales_order_detail
	 * 
	 * @author: TruongHN
	 * @param dto
	 * @throws Exception
	 * @return: SaleOrderDataResult
	 * @throws:
	 */
	public SaleOrderDataResult createOrder(OrderViewDTO dto) {
		String dateNow = DateUtils.now();
		SaleOrderDataResult result = new SaleOrderDataResult();
		long saleOrderId = -1;
		long saleOrderDetailId = -1;
		String createUser = dto.orderInfo.createUser;
		boolean insertSuccess = false;

		SALE_ORDER_TABLE orderTable = new SALE_ORDER_TABLE(mDB);
		SALES_ORDER_DETAIL_TABLE detailTable = new SALES_ORDER_DETAIL_TABLE(mDB);
		TABLE_ID tableId = new TABLE_ID(mDB);
		long staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
		long shopId = Integer.valueOf(GlobalInfo.getInstance().getProfile().getUserData().shopId);

		try {
			mDB.beginTransaction();

			// Lay danh sach id san pham trong order ma chua co stock_total
			SparseIntArray listProductId = getListProductIdNotInStockTotal(dto);
			dto.listProductIdNotHaveStock = listProductId;

			// cap nhat id order
			saleOrderId = tableId.getMaxId(SALE_ORDER_TABLE.TABLE_NAME);
			result.orderId = saleOrderId;

			saleOrderDetailId = tableId.getMaxId(SALES_ORDER_DETAIL_TABLE.TABLE_NAME);
			long suggestedPriceId = tableId.getMaxId(SUGGESTED_PRICE_TABLE.TABLE_NAME);

			// insert sale order
			dto.orderInfo.saleOrderId = result.orderId;
			// cap nhat order number
			// order number = chu cai dau tien (P, W) + 4 chu cuoi staff_id + 5
			// so cuoi id don hang
			String strOrderId = String.valueOf(result.orderId);
			StringBuilder orderNumber = new StringBuilder();
			String userId = ""
					+ GlobalInfo.getInstance().getProfile().getUserData().id;
			if (dto.orderInfo.isVisitPlan == 1) {
				// trong tuyen
				orderNumber.append("V");
			} else {
				// ngoai tuyen
				orderNumber.append("E");
			}
			if (userId.length() >= 5) {
				orderNumber.append(userId.substring(userId.length() - 5));
			} else {
				// 975
				String zero = "00000";
				int lengId = userId.length();
				orderNumber.append(zero.substring(lengId) + userId);
			}

			if (strOrderId.length() >= 8) {
				orderNumber.append(strOrderId.substring(strOrderId.length() - 8));
			} else {
				String zero = "00000000";
				int lengId = strOrderId.length();
				orderNumber.append(zero.substring(lengId) + strOrderId);
			}
			dto.orderInfo.orderNumber = orderNumber.toString();
			dto.orderInfo.totalDetail = dto.listBuyOrders.size()
					+ dto.listPromotionOrders.size();
			if (StringUtil.isNullOrEmpty(dto.orderInfo.shopCode)) {
				dto.orderInfo.shopCode = GlobalInfo.getInstance().getProfile().getUserData().shopCode;
			}
			if (orderTable.insert(dto.orderInfo) > 0) {
				// insert sale order detail
				List<OrderDetailViewDTO> listDetail = dto.listBuyOrders;
				STOCK_TOTAL_TABLE stockTable = new STOCK_TOTAL_TABLE(mDB);
				SUGGESTED_PRICE_TABLE suggestedPriceTable = new SUGGESTED_PRICE_TABLE(mDB);
				// boolean hasOrderDetail = false;
				boolean insertOrderDetail = true;
				// Check c2 customer
//				CUSTOMER_TABLE customer_table = new CUSTOMER_TABLE(mDB);
//				CustomerDTO customerDTO = customer_table.getCustomerById(String.valueOf(dto.orderInfo.customerId));
//				if(customerDTO.getParentCustomerId() == 0) {
//					dto.orderInfo.isNotUpdateStockTotal = false;
//				} else {
//					dto.orderInfo.isNotUpdateStockTotal = true;
//				}
				if (listDetail != null && listDetail.size() > 0) {
					for (OrderDetailViewDTO detailViewDTO : listDetail) {
						if (detailViewDTO.orderDetailDTO.quantity > 0) {
							// hasOrderDetail = true;

							// Luu cac mat hang ban
							createOrderDetailInfo(detailViewDTO, dto, result.orderId, saleOrderDetailId, createUser, dto.orderInfo.updateUser, staffId, shopId);

							// Check insert success
							insertOrderDetail = detailTable.insert(detailViewDTO.orderDetailDTO) > 0 ? true
									: false;

							// Update stock total if insert order success
							if (insertOrderDetail) {
								saleOrderDetailId += 1;
								// cap nhat ton kho tuong ung
								StockTotalDTO stockTotal = StockTotalDTO.createStockTotalInfo(dto, detailViewDTO.orderDetailDTO);

								//if(dto.orderInfo.isNotUpdateStockTotal == false) {
									if (dto.orderInfo.orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_PRESALE)) {// presale
										stockTable.descreaseStockTotalPresale(stockTotal);
									} else if (dto.orderInfo.orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_VANSALE)) {// vansale
										stockTable.descreaseStockTotalVansale(stockTotal);
									}
								//}

								// Cap nhat gia
								SuggestedPriceDTO oldPrice = suggestedPriceTable.getRowByCondition(String.valueOf(detailViewDTO.orderDetailDTO.productId), String.valueOf(shopId), String.valueOf(dto.orderInfo.customerId));
								if (oldPrice == null
										|| (oldPrice != null && oldPrice.price != detailViewDTO.orderDetailDTO.price)) {
									if (oldPrice != null) {
										oldPrice.status = 0;
										oldPrice.updateDate = dateNow;
										oldPrice.updateUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;

										suggestedPriceTable.updateRemoveRow(oldPrice);
									}
									suggestedPriceId ++;
									SuggestedPriceDTO newPrice = new SuggestedPriceDTO();
									newPrice.suggestedPriceId = suggestedPriceId;
									newPrice.customerId = dto.orderInfo.customerId;
									newPrice.shopId = shopId;
									newPrice.productId = detailViewDTO.orderDetailDTO.productId;
									newPrice.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
									newPrice.price = detailViewDTO.orderDetailDTO.price;
									newPrice.status = 1;
									newPrice.createDate = dateNow;
									newPrice.createUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;

									suggestedPriceTable.insert(newPrice);

									dto.listPrice.add(newPrice);
								}
							} else {
								break;
							}
							// add id vao ds insert xuong db server
							result.listOrderDetailId.add(detailViewDTO.orderDetailDTO.salesOrderDetailId);
						}
					}
				}
				// mat hang khuyen mai
				if (insertOrderDetail && dto.listPromotionOrders != null) {
					for (OrderDetailViewDTO detailViewDTO : dto.listPromotionOrders) {
						if (detailViewDTO.promotionType == OrderDetailViewDTO.PROMOTION_FOR_ORDER
								|| (detailViewDTO.promotionType == OrderDetailViewDTO.PROMOTION_FOR_PRODUCT && ((detailViewDTO.type != OrderDetailViewDTO.FREE_PRODUCT) || (detailViewDTO.type == OrderDetailViewDTO.FREE_PRODUCT && detailViewDTO.orderDetailDTO.quantity >= 0)))) {
							// hasOrderDetail = true;

							// Luu cac mat hang khuyen mai
							createOrderDetailInfo(detailViewDTO, dto, result.orderId, saleOrderDetailId, createUser, dto.orderInfo.updateUser, staffId, shopId);

							// Check insert success
							insertOrderDetail = detailTable.insert(detailViewDTO.orderDetailDTO) > 0 ? true
									: false;

							if (insertOrderDetail) {
								saleOrderDetailId += 1;

								StockTotalDTO stockTotal = StockTotalDTO.createStockTotalInfo(dto, detailViewDTO.orderDetailDTO);
								//if(dto.orderInfo.isNotUpdateStockTotal == false) {
									if (dto.orderInfo.orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_PRESALE)) {// presale
										stockTable.descreaseStockTotalPresale(stockTotal);
									} else if (dto.orderInfo.orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_VANSALE)) {// vansale
										stockTable.descreaseStockTotalVansale(stockTotal);
									}
								//}
							} else {
								break;
							}

							// add id vao ds insert xuong db server
							result.listOrderDetailId.add(detailViewDTO.orderDetailDTO.salesOrderDetailId);
						} else if (detailViewDTO.promotionType == OrderDetailViewDTO.PROMOTION_FOR_ZV21) {
							List<OrderDetailViewDTO> listPromotionForPromo21 = detailViewDTO.listPromotionForPromo21;
							for (OrderDetailViewDTO detailViewDTO1 : listPromotionForPromo21) {
								detailViewDTO = detailViewDTO1;
								// Luu cac mat hang khuyen mai
								createOrderDetailInfo(detailViewDTO, dto, result.orderId, saleOrderDetailId, createUser, dto.orderInfo.updateUser, staffId, shopId);

								// Check insert success
								insertOrderDetail = detailTable.insert(detailViewDTO.orderDetailDTO) > 0 ? true
										: false;

								if (insertOrderDetail) {
									saleOrderDetailId += 1;

									StockTotalDTO stockTotal = StockTotalDTO.createStockTotalInfo(dto, detailViewDTO.orderDetailDTO);
									//if(dto.orderInfo.isNotUpdateStockTotal == false) {
										if (dto.orderInfo.orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_PRESALE)) {// presale
											stockTable.descreaseStockTotalPresale(stockTotal);
										} else if (dto.orderInfo.orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_VANSALE)) {// vansale
											stockTable.descreaseStockTotalVansale(stockTotal);
										}
									//}
								} else {
									break;
								}

								// add id vao ds insert xuong db server
								result.listOrderDetailId.add(detailViewDTO.orderDetailDTO.salesOrderDetailId);
							}

							break;
						}
					}
				}

				if (insertOrderDetail) {
					// cap nhat customer
					CUSTOMER_TABLE cusTable = new CUSTOMER_TABLE(mDB);
					dto.customer.setLastOrder(dto.orderInfo.orderDate);
					if (dto.orderInfo.orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_VANSALE)) {// vansale
						dto.customer.setLastApproverder(dto.orderInfo.orderDate);
					}
					boolean updateStaff = cusTable.updateLastOrder(dto.customer) > 0 ? true
							: false;

					// cap nhat staff_customer
					STAFF_CUSTOMER_TABLE staffCusTable = new STAFF_CUSTOMER_TABLE(mDB);
					dto.staffCusDto.customerId = dto.customer.customerId;
					dto.staffCusDto.staffId = staffId;
					dto.staffCusDto.lastOrder = dto.orderInfo.orderDate;
					if (dto.orderInfo.orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_VANSALE)) {// vansale
						dto.staffCusDto.lastApproveOrder = dto.orderInfo.orderDate;
					}
					boolean insertUpdateStaffCus = staffCusTable.insertOrUpdate(dto.staffCusDto) > 0 ? true
							: false;

					boolean insertDebit = true;
					boolean insertDebitDetail = true;
					if (dto.orderInfo.orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_VANSALE)) {// vansale
						CustomerDTO cus = cusTable.getCustomerById(String.valueOf(dto.customer.customerId));

						DEBIT_TABLE debitTable = new DEBIT_TABLE(mDB);
						dto.debitDto.id = tableId.getMaxId(DEBIT_TABLE.TABLE_NAME);
						dto.debitDto.objectID = String.valueOf(dto.orderInfo.customerId);
						dto.debitDto.objectType = "3";
						dto.debitDto.totalAmount = dto.orderInfo.total;
						dto.debitDto.totalPay = 0;
						dto.debitDto.totalDebit = dto.orderInfo.total;
						dto.debitDto.maxDebitAmount = cus.getMaxDebitAmount();
						dto.debitDto.maxDebitDate = cus.getMaxDebitDate();

						dto.debitIdExist = debitTable.checkDebitExist(String.valueOf(dto.orderInfo.customerId));
						if (dto.debitIdExist > 0) {
							dto.debitDto.id = dto.debitIdExist;
							debitTable.increaseDebit(dto.debitDto);
						} else {
							insertDebit = debitTable.insert(dto.debitDto) > 0 ? true
									: false;
						}

						// Insert vao bang debit_detail
						DEBIT_DETAIL_TABLE debitDetailTable = new DEBIT_DETAIL_TABLE(mDB);
						dto.debitDetailDto.debitDetailID = tableId.getMaxId(DEBIT_DETAIL_TABLE.TABLE_NAME);
						dto.debitDetailDto.fromObjectID = dto.orderInfo.saleOrderId;
						dto.debitDetailDto.amount = dto.orderInfo.amount;
						dto.debitDetailDto.discount = dto.orderInfo.discount;
						dto.debitDetailDto.total = dto.orderInfo.total;
						dto.debitDetailDto.remain = dto.debitDetailDto.total;
						dto.debitDetailDto.debitID = dto.debitDto.id;
						dto.debitDetailDto.createUser = dto.orderInfo.createUser;
						dto.debitDetailDto.createDate = dateNow;

						insertDebitDetail = debitDetailTable.insert(dto.debitDetailDto) > 0 ? true
								: false;
					}

					if (updateStaff && insertUpdateStaffCus && insertDebit
							&& insertDebitDetail) {
						insertSuccess = true;
						mDB.setTransactionSuccessful();
					}
				}
			}
		} catch (Exception e) {
			insertSuccess = false;
			ServerLogger.sendLog(e.getMessage(), "createOrder", false, TabletActionLogDTO.LOG_EXCEPTION);
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
			result.isCreateSqlLiteSuccess = insertSuccess;
		}

		return result;
	}

	/**
	 * Lay danh sach id san pham co trong order nhung chua co stock_total
	 * 
	 * @author: quangvt1
	 * @since: 16:49:35 03-06-2014
	 * @return: List<Integer>
	 * @throws:
	 * @param dto
	 * @return
	 */
	private SparseIntArray getListProductIdNotInStockTotal(OrderViewDTO dto) {
		STOCK_TOTAL_TABLE table = new STOCK_TOTAL_TABLE(mDB);
		return table.getListProductIdNotInStockTotal(dto);
	}

	/**
	 * 
	 * @author: PhucNT
	 * @param dto
	 * @return
	 * @throws Exception
	 * @return: boolean
	 * @throws:
	 */
	public boolean deleteSaleOrder(SaleOrderViewDTO dto, List<SaleOrderDetailDTO> listSaleOrderDetail, ActionLogDTO actionLogDTO) {
		boolean result = false;

		try {

			mDB.beginTransaction();
			// xoa tat ca cac oder detail , cap nhat tang so luong cho cac san
			// pham ton kho
			// List<StockTotalDTO> listStockTotalDelete = new
			// ArrayList<StockTotalDTO>(); //Khong cap nhat ton kho nua -> de
			// thuc hien chuc nang hien thi mau` ton kho
			//
			// for (int i = 0, size = listSaleOrderDetail.size(); i < size; i++)
			// {
			// StockTotalDTO stockTotalDTO = new StockTotalDTO();
			// stockTotalDTO.ownerId = dto.saleOrder.shopId;
			// stockTotalDTO.ownerType = 1;
			// stockTotalDTO.productId = listSaleOrderDetail.get(i).productId;
			// stockTotalDTO.quantity = listSaleOrderDetail.get(i).quantity;
			// stockTotalDTO.availableQuantity =
			// listSaleOrderDetail.get(i).quantity;
			// listStockTotalDelete.add(stockTotalDTO);
			// }
			//
			// STOCK_TOTAL_TABLE stockTotalTable = new STOCK_TOTAL_TABLE(mDB);

			boolean isDeleted = deleteOrderNotCommit(dto);

			if (isDeleted) {
				// Update log of sale order delete
				LOG_TABLE logTable = new LOG_TABLE(mDB);
				logTable.updateState(String.valueOf(dto.saleOrder.saleOrderId), Integer.parseInt(LogDTO.STATE_ORDER_DELETED));

				// Tang ton kho //Khong cap nhat ton kho nua -> de thuc hien
				// chuc
				// nang hien thi mau` ton kho
				// for (StockTotalDTO stockTotalDTO : listStockTotalDelete) {
				// stockTotalTable.increaseStockTotal2(stockTotalDTO);
				// }

				boolean updateCustomer = true;
				boolean updateStaffCus = true;
				if (dto.isFinalOrder == 1) {
					// delete record cuoi cung thi cap nhat last_order
					CUSTOMER_TABLE customer = new CUSTOMER_TABLE(mDB);
					CustomerDTO cus = new CustomerDTO();
					cus.customerId = dto.getCustomerId();
					cus.setLastOrder(dto.lastOrder);
					updateCustomer = customer.updateLastOrder(cus) > 0 ? true
							: false;

					// cap nhat last order trong staff customer
					STAFF_CUSTOMER_TABLE staffCusTable = new STAFF_CUSTOMER_TABLE(mDB);
					StaffCustomerDTO staffDto = new StaffCustomerDTO();
					staffDto.customerId = dto.customer.customerId;
					staffDto.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
					staffDto.lastOrder = dto.lastOrder;
					updateStaffCus = staffCusTable.updateLastOrder(staffDto) > 0 ? true
							: false;

					// VISIT_PLAN_TABLE visitPlanTable = new
					// VISIT_PLAN_TABLE(mDB);
					// VisitPlanDTO visitPlanDTO = new VisitPlanDTO();
					// visitPlanDTO.active = 1;
					// visitPlanDTO.shopId = dto.saleOrder.shopId;
					// visitPlanDTO.staffId = dto.saleOrder.staffId;
					// visitPlanDTO.customerId = dto.saleOrder.customerId;
					// visitPlanDTO.lastOrder = dto.saleOrder.orderDate;
					// visitPlanTable.updateLastOrder(visitPlanDTO,
					// dto.lastOrder);
				}

				// Delete action log
				ACTION_LOG_TABLE action_log = new ACTION_LOG_TABLE(mDB);
				action_log.deleteActionLogWhenDeleteOrder(actionLogDTO);

				if (updateCustomer && updateStaffCus) {
					mDB.setTransactionSuccessful();
					result = true;
				}
			}

		} catch (Exception ex) {
			result = false;
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}
		return result;
	}

	/**
	 * update oder have detail not commit server
	 * 
	 * @author: PhucNT
	 * @param dto
	 * @throws Exception
	 * @return: boolean
	 * @throws: note:
	 */
	private boolean deleteOrderNotCommit(SaleOrderViewDTO dto) throws Exception {
		boolean result = false;
		try {
			String[] params = { "" + dto.getSaleOrderId() };
			String[] childTables = { "SALE_ORDER_DETAIL" };
			result = SQLUtils.getInstance().deleteCascade("SALE_ORDER", "SALE_ORDER_ID = ? ", params, childTables);

		} catch (Exception e) {
			result = false;
			ServerLogger.sendLog(e.getMessage() + " saleorderid: "
					+ dto.getSaleOrderId(), "deleteOrderNotCommit", false, TabletActionLogDTO.LOG_EXCEPTION);
		}
		return result;
	}

	/**
	 * Cap nhat sua don hang
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param dto
	 * @return
	 * @throws Exception
	 * @return: boolean
	 * @throws:
	 */
	public JSONArray generateSqlUpdateOrder(OrderViewDTO dto, List<SaleOrderDetailDTO> listSaleOrderDetail)
			throws Exception {
		long saleOrderId = dto.orderInfo.saleOrderId;
		// Contain sql
		JSONArray listSql = new JSONArray();

		try {
			// Generate sql to server
			listSql.put(dto.orderInfo.generateEditOrderSql());

			// Tinh toan de tang/giam ton kho //Khong cap nhat ton kho nua -> de
			// thuc hien chuc nang hien thi mau` ton kho
			// List<StockTotalDTO> listStockTotalDelete = new
			// ArrayList<StockTotalDTO>();
			//
			// if (listSaleOrderDetail != null) {
			// for (int i = 0, size = listSaleOrderDetail.size(); i < size; i++)
			// {
			// StockTotalDTO stockTotalDTO = new StockTotalDTO();
			// stockTotalDTO.ownerId = dto.orderInfo.shopId;
			// stockTotalDTO.ownerType = 1;
			// stockTotalDTO.productId = listSaleOrderDetail.get(i).productId;
			// stockTotalDTO.quantity = listSaleOrderDetail.get(i).quantity;
			// stockTotalDTO.availableQuantity =
			// listSaleOrderDetail.get(i).quantity;
			// listStockTotalDelete.add(stockTotalDTO);
			// }
			// }

			// Luu cac chi tiet don hang moi (san pham ban)
			SaleOrderDetailDTO saleDetailE = new SaleOrderDetailDTO();
			saleDetailE.salesOrderId = dto.orderInfo.saleOrderId;

			// Generate sql to server
			listSql.put(saleDetailE.generateDeleteOrderSql(saleOrderId));

			// Luu san pham & tang giam ton kho
			List<StockTotalDTO> listStockTotal = new ArrayList<StockTotalDTO>();
			if (dto.listBuyOrders != null) {
				for (OrderDetailViewDTO detailViewDTO : dto.listBuyOrders) {
					if (detailViewDTO.orderDetailDTO.quantity > 0) {
						// cap nhat ton kho tuong ung
						StockTotalDTO stockTotal = StockTotalDTO.createStockTotalInfo(dto, detailViewDTO.orderDetailDTO);
						listStockTotal.add(stockTotal);

						// Generate sql send to server
						listSql.put(detailViewDTO.orderDetailDTO.generateCreateSqlForUpdate());
					}
				}
			}

			// Luu san pham khuyen mai va tang giam ton kho
			if (dto.listPromotionOrders != null) {
				for (OrderDetailViewDTO detailViewDTO : dto.listPromotionOrders) {
					if (detailViewDTO.promotionType == OrderDetailViewDTO.PROMOTION_FOR_ORDER
							|| (detailViewDTO.promotionType == OrderDetailViewDTO.PROMOTION_FOR_PRODUCT && ((detailViewDTO.type != OrderDetailViewDTO.FREE_PRODUCT) || (detailViewDTO.type == OrderDetailViewDTO.FREE_PRODUCT && detailViewDTO.orderDetailDTO.quantity >= 0)))) {
						// cap nhat ton kho tuong ung
						StockTotalDTO stockTotal = StockTotalDTO.createStockTotalInfo(dto, detailViewDTO.orderDetailDTO);
						listStockTotal.add(stockTotal);

						// Generate sql send to server
						listSql.put(detailViewDTO.orderDetailDTO.generateCreateSqlForUpdate());
					} else if (detailViewDTO.promotionType == OrderDetailViewDTO.PROMOTION_FOR_ZV21) {
						List<OrderDetailViewDTO> listPromotionForPromo21 = detailViewDTO.listPromotionForPromo21;
						for (OrderDetailViewDTO detailViewDTO1 : listPromotionForPromo21) {
							// cap nhat ton kho tuong ung
							StockTotalDTO stockTotal = StockTotalDTO.createStockTotalInfo(dto, detailViewDTO1.orderDetailDTO);
							listStockTotal.add(stockTotal);

							// Generate sql send to server
							listSql.put(detailViewDTO1.orderDetailDTO.generateCreateSqlForUpdate());
						}
						break;
					}
				}
			}

			// Tang ton kho //Khong cap nhat ton kho nua -> de thuc hien chuc
			// nang hien thi mau` ton kho
			// Chi co presale moi sua duoc don hang
			//if(dto.orderInfo.isNotUpdateStockTotal == false) {
				for (StockTotalDTO stockTotal : listStockTotal) {
					// Neu san pham nay chua co stock_total thi insert
					if (dto.listProductIdNotHaveStock.indexOfKey(stockTotal.productId) > -1) {
						listSql.put(stockTotal.generateInsertOrUpdateSqlPresale());
						// Xoa san pham ra khoi list vi da insert roi
						dto.listProductIdNotHaveStock.removeAt(dto.listProductIdNotHaveStock.indexOfKey(stockTotal.productId));
					}
					// Neu da co thi update
					else {
						listSql.put(stockTotal.generateDescreaseSqlPresale());
					}
				}
			//}

			// cap nhat ban customer
			listSql.put(dto.customer.generateUpdateFromOrderSql());

			// cap nhat staff_customer
			listSql.put(dto.staffCusDto.generateInsertOrUpdateFromOrderSql());

			// Cap nhat gia tham khao
			for (SuggestedPriceDTO price : dto.listPrice) {
				listSql.put(price.generateInsertOrUpdateFromOrderSql());
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
		}

		return listSql;
	}

	/**
	 * 
	 * Update info for order detail to insert db
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param detailViewDTO
	 * @param dto
	 * @param saleOrderId
	 * @param saleOrderDetailId
	 * @param createUser
	 * @param updateUser
	 * @param staffId
	 * @param shopId
	 * @return: void
	 * @throws:
	 */
	private void createOrderDetailInfo(OrderDetailViewDTO detailViewDTO, OrderViewDTO dto, long saleOrderId, long saleOrderDetailId, String createUser, String updateUser, long staffId, long shopId) {
		detailViewDTO.orderDetailDTO.salesOrderId = saleOrderId;
		detailViewDTO.orderDetailDTO.salesOrderDetailId = saleOrderDetailId;
		detailViewDTO.orderDetailDTO.createUser = createUser;
		detailViewDTO.orderDetailDTO.updateUser = updateUser;
		detailViewDTO.orderDetailDTO.createDate = dto.orderInfo.createDate;
		detailViewDTO.orderDetailDTO.orderDate = dto.orderInfo.createDate;
		detailViewDTO.orderDetailDTO.updateDate = dto.orderInfo.updateDate;
		// detailViewDTO.orderDetailDTO.isFreeItem = 1;
		detailViewDTO.orderDetailDTO.setType(AbstractTableDTO.TableType.SALE_ORDER_DETAIL);
		detailViewDTO.orderDetailDTO.shopId = shopId;
		detailViewDTO.orderDetailDTO.staffId = staffId;

	}

	/**
	 * Generata sql sua don hang gui server
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param dto
	 * @return
	 * @throws Exception
	 * @return: boolean
	 * @throws:
	 */
	public boolean updateOrder(OrderViewDTO dto, List<SaleOrderDetailDTO> listSaleOrderDetail) {
		long saleOrderId = dto.orderInfo.saleOrderId;
		long saleOrderDetailId = -1;
		boolean insertSuccess = false;
		String createUser = dto.orderInfo.createUser;
		long staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
		long shopId = Integer.valueOf(GlobalInfo.getInstance().getProfile().getUserData().shopId);

		SALE_ORDER_TABLE orderTable = new SALE_ORDER_TABLE(mDB);
		SALES_ORDER_DETAIL_TABLE orderDetailTable = new SALES_ORDER_DETAIL_TABLE(mDB);
		TABLE_ID tableId = new TABLE_ID(mDB);
		SUGGESTED_PRICE_TABLE suggestedPriceTable = new SUGGESTED_PRICE_TABLE(mDB);
		long suggestedPriceId = tableId.getMaxId(SUGGESTED_PRICE_TABLE.TABLE_NAME);

		String dateNow = DateUtils.now();
		// dto.orderInfo.state = 1;
		dto.orderInfo.updateDate = dateNow;
		dto.orderInfo.updateUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;
		// dto.orderInfo.importCode = "";
		dto.orderInfo.totalDetail = dto.listBuyOrders.size()
				+ dto.listPromotionOrders.size();
		if (StringUtil.isNullOrEmpty(dto.orderInfo.shopCode)) {
			dto.orderInfo.shopCode = GlobalInfo.getInstance().getProfile().getUserData().shopCode;
		}
		try {
			mDB.beginTransaction();
			// Update sale order to sale order table
			if (orderTable.update(dto.orderInfo) > 0) {
				// Tinh toan de tang/giam ton kho
				// List<StockTotalDTO> listStockTotalDelete = new
				// ArrayList<StockTotalDTO>();
				//
				// for (SaleOrderDetailDTO orderDetailDTO : listSaleOrderDetail)
				// {
				// StockTotalDTO stockTotalDTO =
				// StockTotalDTO.createStockTotalInfo(dto, orderDetailDTO);
				// listStockTotalDelete.add(stockTotalDTO);
				// }

				// Lay danh sach id san pham trong order ma chua co stock_total
				SparseIntArray listProductId = getListProductIdNotInStockTotal(dto);
				dto.listProductIdNotHaveStock = listProductId;

				// Get max id
				saleOrderDetailId = tableId.getMaxId(SALES_ORDER_DETAIL_TABLE.TABLE_NAME);

				// Xoa cac order detail cu~
				boolean deleteDetailSuccess = orderDetailTable.deleteAllDetailOfOrder(saleOrderId) > 0 ? true
						: false;

				// Check c2 customer
//				CUSTOMER_TABLE customer_table = new CUSTOMER_TABLE(mDB);
//				CustomerDTO customerDTO = customer_table.getCustomerById(String.valueOf(dto.orderInfo.customerId));
//				if(customerDTO.getParentCustomerId() == 0) {
//					dto.orderInfo.isNotUpdateStockTotal = false;
//				} else {
//					dto.orderInfo.isNotUpdateStockTotal = true;
//				}
				if (deleteDetailSuccess) {
					// Luu san pham & tang giam ton kho
					List<StockTotalDTO> listStockTotal = new ArrayList<StockTotalDTO>();
					// boolean hasOrderDetail = false;
					boolean insertOrderDetail = true;
					if (dto.listBuyOrders != null) {
						for (OrderDetailViewDTO detailViewDTO : dto.listBuyOrders) {
							if (detailViewDTO.orderDetailDTO.quantity > 0) {
								// hasOrderDetail = true;
								// cap nhat ton kho tuong ung
								StockTotalDTO stockTotal = StockTotalDTO.createStockTotalInfo(dto, detailViewDTO.orderDetailDTO);

								listStockTotal.add(stockTotal);

								// Luu cac mat hang ban
								createOrderDetailInfo(detailViewDTO, dto, saleOrderId, saleOrderDetailId, createUser, GlobalInfo.getInstance().getProfile().getUserData().userCode, staffId, shopId);

								// Check insert success
								insertOrderDetail = orderDetailTable.insert(detailViewDTO.orderDetailDTO) > 0 ? true
										: false;

								if (insertOrderDetail) {
									saleOrderDetailId += 1;

									// Cap nhat gia
									SuggestedPriceDTO oldPrice = suggestedPriceTable.getRowByCondition(String.valueOf(detailViewDTO.orderDetailDTO.productId), String.valueOf(shopId), String.valueOf(dto.orderInfo.customerId));
									if (oldPrice == null
											|| (oldPrice != null && oldPrice.price != detailViewDTO.orderDetailDTO.price)) {
										if (oldPrice != null) {
											oldPrice.status = 0;
											oldPrice.updateDate = DateUtils.now();
											oldPrice.updateUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;

											suggestedPriceTable.updateRemoveRow(oldPrice);
										}

										SuggestedPriceDTO newPrice = new SuggestedPriceDTO();
										suggestedPriceId++;
										newPrice.suggestedPriceId = suggestedPriceId;
										newPrice.customerId = dto.orderInfo.customerId;
										newPrice.shopId = shopId;
										newPrice.productId = detailViewDTO.orderDetailDTO.productId;
										newPrice.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
										newPrice.price = detailViewDTO.orderDetailDTO.price;
										newPrice.status = 1;
										newPrice.createDate = dateNow;
										newPrice.createUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;

										suggestedPriceTable.insert(newPrice);

										dto.listPrice.add(newPrice);
									}
								} else {
									break;
								}
							}
						}
					}

					// Luu san pham khuyen mai va tang giam ton kho
					if (insertOrderDetail && dto.listPromotionOrders != null) {
						for (OrderDetailViewDTO detailViewDTO : dto.listPromotionOrders) {
							if (detailViewDTO.promotionType == OrderDetailViewDTO.PROMOTION_FOR_ORDER
									|| (detailViewDTO.promotionType == OrderDetailViewDTO.PROMOTION_FOR_PRODUCT && ((detailViewDTO.type != OrderDetailViewDTO.FREE_PRODUCT) || (detailViewDTO.type == OrderDetailViewDTO.FREE_PRODUCT && detailViewDTO.orderDetailDTO.quantity >= 0)))) {
								// hasOrderDetail = true;

								// cap nhat ton kho tuong ung
								StockTotalDTO stockTotal = StockTotalDTO.createStockTotalInfo(dto, detailViewDTO.orderDetailDTO);
								listStockTotal.add(stockTotal);

								// Luu cac mat hang khuyen mai
								createOrderDetailInfo(detailViewDTO, dto, saleOrderId, saleOrderDetailId, createUser, GlobalInfo.getInstance().getProfile().getUserData().userCode, staffId, shopId);

								// Check insert success
								insertOrderDetail = orderDetailTable.insert(detailViewDTO.orderDetailDTO) > 0 ? true
										: false;

								if (insertOrderDetail) {
									saleOrderDetailId += 1;
								} else {
									break;
								}
							} else if (detailViewDTO.promotionType == OrderDetailViewDTO.PROMOTION_FOR_ZV21) {
								List<OrderDetailViewDTO> listPromotionForPromo21 = detailViewDTO.listPromotionForPromo21;
								for (OrderDetailViewDTO detailViewDTO1 : listPromotionForPromo21) {
									detailViewDTO = detailViewDTO1;
									// cap nhat ton kho tuong ung
									StockTotalDTO stockTotal = StockTotalDTO.createStockTotalInfo(dto, detailViewDTO.orderDetailDTO);
									listStockTotal.add(stockTotal);

									// Luu cac mat hang khuyen mai
									createOrderDetailInfo(detailViewDTO, dto, saleOrderId, saleOrderDetailId, createUser, GlobalInfo.getInstance().getProfile().getUserData().userCode, staffId, shopId);

									// Check insert success
									insertOrderDetail = orderDetailTable.insert(detailViewDTO.orderDetailDTO) > 0 ? true
											: false;

									if (insertOrderDetail) {
										saleOrderDetailId += 1;
									} else {
										break;
									}
								}

								break;
							}
						}
					}

					if (insertOrderDetail) {
						// Khong cap nhat ton kho nua -> de thuc hien chuc nang
						// hien thi
						STOCK_TOTAL_TABLE stockTotalTable = new STOCK_TOTAL_TABLE(mDB);
						// Tang ton kho
						// for (StockTotalDTO stockTotalDTO :
						// listStockTotalDelete) {
						// stockTotalTable.increaseStockTotal2(stockTotalDTO);
						// }

						// Chi co presale moi sua don hang duoc
						//if(dto.orderInfo.isNotUpdateStockTotal == false) {
							for (StockTotalDTO stockTotalDTO : listStockTotal) {
								stockTotalTable.descreaseStockTotalPresale(stockTotalDTO);
							}
						//}

						// cap nhat customer
						CUSTOMER_TABLE cusTable = new CUSTOMER_TABLE(mDB);
						dto.customer.setLastOrder(dto.orderInfo.updateDate);
						if (dto.orderInfo.orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_VANSALE)) {// vansale
							dto.customer.setLastApproverder(dto.orderInfo.updateDate);
						}
						boolean updateStaff = cusTable.updateLastOrder(dto.customer) > 0 ? true
								: false;

						// cap nhat staff_customer
						STAFF_CUSTOMER_TABLE staffCusTable = new STAFF_CUSTOMER_TABLE(mDB);
						dto.staffCusDto.customerId = dto.customer.customerId;
						dto.staffCusDto.staffId = staffId;
						dto.staffCusDto.lastOrder = dto.orderInfo.updateDate;
						if (dto.orderInfo.orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_VANSALE)) {// vansale
							dto.staffCusDto.lastApproveOrder = dto.orderInfo.updateDate;
						}
						boolean insertUpdateStaffCus = staffCusTable.insertOrUpdate(dto.staffCusDto) > 0 ? true
								: false;

						if (updateStaff && insertUpdateStaffCus) {
							mDB.setTransactionSuccessful();
							insertSuccess = true;
						}
					}
				}
			}
		} catch (Exception e) {
			insertSuccess = false;
			ServerLogger.sendLog(e.getMessage(), "updateOrder", false, TabletActionLogDTO.LOG_EXCEPTION);
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}

		return insertSuccess;
	}

	/**
	 * Lay ds san pham khuyen mai tu san pham ban
	 * 
	 * @author: TruongHN
	 * @param orderView
	 * @return: ArrayList<OrderDetailViewDTO>
	 * @throws:
	 */

	public SortedMap<Long, List<OrderDetailViewDTO>> calculatePromotionProducts(OrderViewDTO orderView) {
		// ds khuyen mai tra ve
		SortedMap<Long, List<OrderDetailViewDTO>> sortListOutPut = new TreeMap<Long, List<OrderDetailViewDTO>>();

		// Sap xep cac san pham ban theo product id
		SortedMap<Long, OrderDetailViewDTO> sortListProductSale = new TreeMap<Long, OrderDetailViewDTO>();
		for (int i = 0, size = orderView.listBuyOrders.size(); i < size; i++) {
			OrderDetailViewDTO detail = orderView.listBuyOrders.get(i);
			OrderDetailViewDTO orderDetail = new OrderDetailViewDTO();
			SaleOrderDetailDTO orderDTO = new SaleOrderDetailDTO();
			orderDetail.orderDetailDTO = orderDTO;

			orderDTO.quantity = detail.orderDetailDTO.quantity;
			orderDTO.productId = detail.orderDetailDTO.productId;
			orderDTO.price = detail.orderDetailDTO.price;
			orderDTO.programeCode = detail.orderDetailDTO.programeCode;

			sortListProductSale.put(Long.valueOf(detail.orderDetailDTO.productId), orderDetail);
		}

		ArrayList<OrderDetailViewDTO> listProductPromotionsale = new ArrayList<OrderDetailViewDTO>();

		String shopId = GlobalInfo.getInstance().getProfile().getUserData().shopId;
		Long keyList = Long.valueOf(100);

		// Table for request
		PRODUCT_TABLE productDAO = new PRODUCT_TABLE(mDB);
		PROMOTION_PROGRAME_TABLE promotionProTable = new PROMOTION_PROGRAME_TABLE(mDB);
		PROMOTION_PROGRAME_DETAIL_TABLE proProgrameDetailTable = new PROMOTION_PROGRAME_DETAIL_TABLE(mDB);
		SHOP_TABLE shopTable = new SHOP_TABLE(mDB);

		// lay thong tin KM, loai KM
		String customerId = orderView.customer.getCustomerId();
		String customerTypeId = String.valueOf(orderView.customer.getCustomerTypeId());

		ArrayList<String> shopIdArray = shopTable.getShopRecursive(shopId);
		String idListString = TextUtils.join(",", shopIdArray);
		// Tinh KM cho tung sp
		while (sortListProductSale.size() > 0) {
			// Reset price & quantiy
			for (OrderDetailViewDTO sortDetail : sortListProductSale.values()) {
				for (int i = 0, size = orderView.listBuyOrders.size(); i < size; i++) {
					OrderDetailViewDTO detail = orderView.listBuyOrders.get(i);
					if (sortDetail.orderDetailDTO.productId == detail.orderDetailDTO.productId) {
						sortDetail.orderDetailDTO.quantity = detail.orderDetailDTO.quantity;
						sortDetail.orderDetailDTO.price = detail.orderDetailDTO.price;
						break;
					}
				}
			}

			Long key = sortListProductSale.firstKey();

			OrderDetailViewDTO buyProducts = sortListProductSale.get(key);
			String productId = String.valueOf(buyProducts.orderDetailDTO.productId);

			PromotionProgrameDTO promotionProgram = promotionProTable.getPromotionObjectWithCheckInvalid(productId, idListString, customerId, customerTypeId);

			// long promotionProgrameId =
			// promotionProTable.getPromotionProgramID(productId, idListString,
			// customerId,
			// customerTypeId);
			//
			// // Kiem tra CTKM co hop le voi cac thuoc tinh khai bao ko?
			// boolean checkProgrameValid = false;
			//
			// if (promotionProgrameId > 0) {
			// checkProgrameValid =
			// promotionProTable.checkPromotionProgrameValid("" +
			// promotionProgrameId,
			// customerId, customerTypeId);
			// }

			if (promotionProgram != null && promotionProgram.isInvalid) {
				// Luong: lay ds san pham ban, duyet tung san pham ban lay id
				// chuong trinh KM tuong ung voi SP ban.
				// Lay ds KM dua theo ID tren, vi mot sp ban co the KM nhieu mat
				// hang, cac mat hang duoc luu trong DB nhieu row du lieu
				// nen select se ra mot list cs KM cua sp do
				// lay id chuong trinh KM

				// PromotionProgrameDTO promotionProgram =
				// promotionProTable.getPromotionProgrameFromProgrameId(String
				// .valueOf(promotionProgrameId));

				// lay ds promotion_programe_detail tu promotion_programe_id
				ArrayList<PromotionProDetailDTO> listPromotionDetail = new ArrayList<PromotionProDetailDTO>();

				// Ds cac chi tiet khuyen mai cua chuong trinh khuyen mai
				listPromotionDetail = proProgrameDetailTable.getPromotionDetailByPromotionId((int) promotionProgram.PROMOTION_PROGRAM_ID);

				// Danh sach cac chi tiet khuyen mai duoc gom nhom theo product
				// id
				SortedMap<Long, List<PromotionProDetailDTO>> sortListPromotionProDetail = new TreeMap<Long, List<PromotionProDetailDTO>>();

				// Ds cac khuyen mai cua mot san pham
				List<PromotionProDetailDTO> listPromotionProDetailAdd = new ArrayList<PromotionProDetailDTO>();

				// Gom nhom cac chi tiet khuyen mai theo product id
				for (int i = 0, size = listPromotionDetail.size(); i < size; i++) {
					Long key2 = Long.valueOf(listPromotionDetail.get(i).productId);
					if (sortListPromotionProDetail.containsKey(key2)) {
						listPromotionProDetailAdd = sortListPromotionProDetail.get(key2);
						listPromotionProDetailAdd.add(listPromotionDetail.get(i));
					} else {
						listPromotionProDetailAdd = new ArrayList<PromotionProDetailDTO>();
						listPromotionProDetailAdd.add(listPromotionDetail.get(i));
						sortListPromotionProDetail.put(key2, listPromotionProDetailAdd);
					}
				}

				// tinh khuyen mai
				CalPromotions.calcPromotionForProduct(promotionProgram, sortListPromotionProDetail, sortListProductSale, listProductPromotionsale, keyList, sortListOutPut);

				// calcPromotion(listPromoProducts, orderView.orderInfo,
				// buyProducts);
				if (sortListOutPut.size() > 0) {
					keyList = sortListOutPut.lastKey();
				}
			}

			sortListProductSale.remove(key);
		}

		// tinh KM cho don hang
		calPromotionForOrder(orderView, sortListOutPut, listProductPromotionsale, idListString, keyList, promotionProTable, proProgrameDetailTable, customerId, customerTypeId);

		// Lay thong tin cho ds sp KM hien thi
		ArrayList<OrderDetailViewDTO> listProductSalePromotion = new ArrayList<OrderDetailViewDTO>();
		ArrayList<OrderDetailViewDTO> listOrderSalePromotion = new ArrayList<OrderDetailViewDTO>();
		ArrayList<OrderDetailViewDTO> listProductSalePromotionMissing = new ArrayList<OrderDetailViewDTO>();

		if (listProductPromotionsale != null
				&& listProductPromotionsale.size() > 0) {
			for (OrderDetailViewDTO promotionProduct : listProductPromotionsale) {
				promotionProduct.orderDetailDTO.productId = (int) promotionProduct.productPromoId;

				if (promotionProduct.orderDetailDTO.productId > 0) {
					// Chi lay sp KM co so luong KM > 0
					if (promotionProduct.orderDetailDTO.quantity > 0) {

						OrderDetailViewDTO productPromotionInfo = productDAO.getProductStockByID(String.valueOf(promotionProduct.productPromoId), orderView.orderInfo.orderType);

						if (productPromotionInfo != null) {
							promotionProduct.productCode = productPromotionInfo.productCode;
							promotionProduct.productName = productPromotionInfo.productName;
							promotionProduct.convfact = productPromotionInfo.convfact;
							promotionProduct.stock = productPromotionInfo.stock;
							promotionProduct.orderDetailDTO.price = productPromotionInfo.orderDetailDTO.price;
							promotionProduct.orderDetailDTO.priceId = productPromotionInfo.orderDetailDTO.priceId;
							promotionProduct.orderDetailDTO.vat = productPromotionInfo.orderDetailDTO.vat;
							promotionProduct.orderDetailDTO.priceNotVat = productPromotionInfo.orderDetailDTO.priceNotVat;
							promotionProduct.grossWeight = productPromotionInfo.grossWeight;
							promotionProduct.orderDetailDTO.totalWeight = promotionProduct.grossWeight
									* promotionProduct.orderDetailDTO.quantity;

							// Sp khong co gia' hieu luc hoac gia' <= 0
							if (promotionProduct.orderDetailDTO.price <= 0) {
								listProductSalePromotionMissing.add(promotionProduct);
							}
							// } else {// Loi dong bo
							// listProductSalePromotionMissing.add(promotionProduct);
						}
					} else {
						continue;
					}
				} else if (promotionProduct.listPromotionForPromo21 != null
						&& promotionProduct.listPromotionForPromo21.size() > 0) {
					for (OrderDetailViewDTO promotionProductZV21 : promotionProduct.listPromotionForPromo21) {
						promotionProductZV21.orderDetailDTO.productId = (int) promotionProductZV21.productPromoId;

						if (promotionProductZV21.orderDetailDTO.productId > 0) {
							// Chi lay sp KM co so luong KM > 0
							if (promotionProductZV21.orderDetailDTO.quantity > 0) {

								OrderDetailViewDTO productPromotionInfo = productDAO.getProductStockByID(String.valueOf(promotionProductZV21.productPromoId), shopId);

								if (productPromotionInfo != null) {
									promotionProductZV21.productCode = productPromotionInfo.productCode;
									promotionProductZV21.productName = productPromotionInfo.productName;
									promotionProductZV21.convfact = productPromotionInfo.convfact;
									promotionProductZV21.stock = productPromotionInfo.stock;
									promotionProductZV21.orderDetailDTO.price = productPromotionInfo.orderDetailDTO.price;
									promotionProductZV21.orderDetailDTO.priceId = productPromotionInfo.orderDetailDTO.priceId;
									promotionProductZV21.orderDetailDTO.vat = productPromotionInfo.orderDetailDTO.vat;
									promotionProductZV21.orderDetailDTO.priceNotVat = productPromotionInfo.orderDetailDTO.priceNotVat;

									// Sp khong co gia' hieu luc hoac gia' <= 0
									if (promotionProductZV21.orderDetailDTO.price <= 0) {
										listProductSalePromotionMissing.add(promotionProductZV21);
									}
									// } else {// Loi dong bo
									// listProductSalePromotionMissing.add(promotionProduct);
								}
							} else {
								continue;
							}
						} else {
							promotionProductZV21.productName = StringUtil.getString(R.string.TEXT_KM_CK_GET);
							promotionProductZV21.quantityEdit = promotionProductZV21.orderDetailDTO.quantity;
						}
					}
				} else {
					promotionProduct.productName = StringUtil.getString(R.string.TEXT_KM_CK_GET);
					promotionProduct.quantityEdit = promotionProduct.orderDetailDTO.quantity;
				}

				// Sap xep theo programe code
				// CTKM cho sp
				if (promotionProduct.promotionType == OrderDetailViewDTO.PROMOTION_FOR_PRODUCT) {
					int i = 0;
					int size = listProductSalePromotion.size();
					for (i = 0; i < size; i++) {
						OrderDetailViewDTO promotionTemp = listProductSalePromotion.get(i);
						// Lon hon
						if (promotionProduct.orderDetailDTO.programeCode.compareToIgnoreCase(promotionTemp.orderDetailDTO.programeCode) < 0) {
							break;
						}
					}

					listProductSalePromotion.add(i, promotionProduct);
				} else {// CTKM cho don hang
					int i = 0;
					int size = listOrderSalePromotion.size();
					for (i = 0; i < size; i++) {
						OrderDetailViewDTO promotionTemp = listOrderSalePromotion.get(i);
						// Lon hon
						if (promotionProduct.orderDetailDTO.programeCode.compareToIgnoreCase(promotionTemp.orderDetailDTO.programeCode) < 0) {
							break;
						}
					}

					listOrderSalePromotion.add(i, promotionProduct);
				}
			}

			// Add list promotion of order at the end of list promotion product
			listProductSalePromotion.addAll(listOrderSalePromotion);
		}

		// Ds san pham hien thi
		sortListOutPut.put(Long.valueOf(1), listProductSalePromotion);

		// Ds san pham ko co trong ton kho or ko co trong kho cua sp hien thi
		sortListOutPut.put(Long.valueOf(10), listProductSalePromotionMissing);

		// Ds sp ko co trong ton kho or ko co trong kho cua sp doi
		listProductSalePromotionMissing = new ArrayList<OrderDetailViewDTO>();

		if (sortListOutPut.size() > 2) {
			Iterator<Long> it = sortListOutPut.keySet().iterator();
			it.next();// sp hien thi
			it.next();// sp hien thi bi loi

			while (it.hasNext()) {
				Long md = it.next();
				List<OrderDetailViewDTO> listProductChange = sortListOutPut.get(md);
				listProductSalePromotion = new ArrayList<OrderDetailViewDTO>();

				for (OrderDetailViewDTO promotionProduct : listProductChange) {
					promotionProduct.orderDetailDTO.productId = (int) promotionProduct.productPromoId;

					if (promotionProduct.orderDetailDTO.productId > 0) {
						// Chi lay sp KM co so luong KM > 0
						if (promotionProduct.orderDetailDTO.quantity > 0) {

							OrderDetailViewDTO productPromotionInfo = productDAO.getProductStockByID(String.valueOf(promotionProduct.productPromoId), orderView.orderInfo.orderType);

							if (productPromotionInfo != null) {
								promotionProduct.productCode = productPromotionInfo.productCode;
								promotionProduct.productName = productPromotionInfo.productName;
								promotionProduct.convfact = productPromotionInfo.convfact;
								promotionProduct.stock = productPromotionInfo.stock;
								promotionProduct.orderDetailDTO.price = productPromotionInfo.orderDetailDTO.price;
								promotionProduct.orderDetailDTO.priceId = productPromotionInfo.orderDetailDTO.priceId;
								promotionProduct.orderDetailDTO.vat = productPromotionInfo.orderDetailDTO.vat;
								promotionProduct.orderDetailDTO.priceNotVat = productPromotionInfo.orderDetailDTO.priceNotVat;
								promotionProduct.grossWeight = productPromotionInfo.grossWeight;
								promotionProduct.orderDetailDTO.totalWeight = promotionProduct.grossWeight
										* promotionProduct.orderDetailDTO.quantity;

								// Sp khong co gia' hieu luc hoac gia' <= 0
								if (promotionProduct.orderDetailDTO.price <= 0) {
									listProductSalePromotionMissing.add(promotionProduct);
								}
								// } else {// Loi dong bo
								// listProductSalePromotionMissing.add(promotionProduct);
							}
						} else {
							continue;
						}
					} else {
						promotionProduct.productName = StringUtil.getString(R.string.TEXT_KM_CK_GET);
						promotionProduct.quantityEdit = promotionProduct.orderDetailDTO.quantity;
					}

					listProductSalePromotion.add(promotionProduct);
				}

				sortListOutPut.put(md, listProductSalePromotion);
			}

			// Ds san pham ko co trong ton kho or ko co trong kho cua sp doi
			// hang
			sortListOutPut.put(Long.valueOf(11), listProductSalePromotionMissing);
		}

		// Get information for product of zv21
		if (orderView.listPromotionForOrderChange != null
				&& orderView.listPromotionForOrderChange.size() > 0) {
			for (OrderDetailViewDTO promotionPrograme : orderView.listPromotionForOrderChange) {
				if (promotionPrograme.listPromotionForPromo21 != null
						&& promotionPrograme.listPromotionForPromo21.size() > 0) {
					for (OrderDetailViewDTO promotionProduct : promotionPrograme.listPromotionForPromo21) {
						promotionProduct.orderDetailDTO.productId = (int) promotionProduct.productPromoId;

						if (promotionProduct.orderDetailDTO.productId > 0) {
							// Chi lay sp KM co so luong KM > 0
							if (promotionProduct.orderDetailDTO.quantity > 0) {

								OrderDetailViewDTO productPromotionInfo = productDAO.getProductStockByID(String.valueOf(promotionProduct.productPromoId), orderView.orderInfo.orderType);

								if (productPromotionInfo != null) {
									promotionProduct.productCode = productPromotionInfo.productCode;
									promotionProduct.productName = productPromotionInfo.productName;
									promotionProduct.convfact = productPromotionInfo.convfact;
									promotionProduct.stock = productPromotionInfo.stock;
									promotionProduct.orderDetailDTO.price = productPromotionInfo.orderDetailDTO.price;
									promotionProduct.orderDetailDTO.priceId = productPromotionInfo.orderDetailDTO.priceId;
									promotionProduct.orderDetailDTO.vat = productPromotionInfo.orderDetailDTO.vat;
									promotionProduct.orderDetailDTO.priceNotVat = productPromotionInfo.orderDetailDTO.priceNotVat;
									promotionProduct.grossWeight = productPromotionInfo.grossWeight;
									promotionProduct.orderDetailDTO.totalWeight = promotionProduct.grossWeight
											* promotionProduct.orderDetailDTO.quantity;

									// Sp khong co gia' hieu luc hoac gia' <= 0
									if (promotionProduct.orderDetailDTO.price <= 0) {
										listProductSalePromotionMissing.add(promotionProduct);
									}
									// } else {// Loi dong bo
									// listProductSalePromotionMissing.add(promotionProduct);
								}
							} else {
								continue;
							}
						} else {
							promotionProduct.productName = StringUtil.getString(R.string.TEXT_KM_CK_GET);
							promotionProduct.quantityEdit = promotionProduct.orderDetailDTO.quantity;
						}
					}
				}
			}
		}

		// Lay ton kho cho sp ban tuong ung voi loai don hang
		for (OrderDetailViewDTO product : orderView.listBuyOrders) {
			OrderDetailViewDTO productPromotionInfo = productDAO.getProductStockByID(String.valueOf(product.orderDetailDTO.productId), orderView.orderInfo.orderType);
			if (productPromotionInfo != null) {
				product.stock = productPromotionInfo.stock;
			}
		}

		// Lay ton kho cho sp KM KHONG PHAI TU DONG tuong ung voi loai don hang
		for (OrderDetailViewDTO product : orderView.listPromotionOrders) {
			if (product.orderDetailDTO.programeType != 0) {
				OrderDetailViewDTO productPromotionInfo = productDAO.getProductStockByID(String.valueOf(product.orderDetailDTO.productId), orderView.orderInfo.orderType);
				if (productPromotionInfo != null) {
					product.stock = productPromotionInfo.stock;
				}
			}
		}

		return sortListOutPut;
	}

	/**
	 * Tinh KM cho don hang
	 * 
	 * @author: TruongHN
	 * @param orderView
	 * @param sortListOutPut
	 * @param listProductPromotionsale
	 * @param shopIdList
	 * @param keyList
	 * @param promotionProTable
	 * @param proProgrameDetailTable
	 * @return: void
	 * @throws:
	 */
	private void calPromotionForOrder(OrderViewDTO orderView, SortedMap<Long, List<OrderDetailViewDTO>> sortListOutPut, ArrayList<OrderDetailViewDTO> listProductPromotionsale, String shopIdList, Long keyList, PROMOTION_PROGRAME_TABLE promotionProTable, PROMOTION_PROGRAME_DETAIL_TABLE proProgrameDetailTable, String customerId, String customerTypeId) {
		// Calculate total (amount - discount) of order
		// long disCount = 0;
		long total = orderView.orderInfo.amount;
		// long amount = 0;
		// Calculate amount
		// for (int i = 0, size = orderView.listBuyOrders.size(); i < size; i++)
		// {
		// OrderDetailViewDTO dto = orderView.listBuyOrders.get(i);
		// amount += dto.orderDetailDTO.price * dto.orderDetailDTO.quantity;
		// }

		// Calculate discount
		// for (int i = 0, size = listProductPromotionsale.size(); i < size;
		// i++) {
		// OrderDetailViewDTO detailView = listProductPromotionsale.get(i);
		// if (detailView.promotionType ==
		// OrderDetailViewDTO.PROMOTION_FOR_PRODUCT) {
		// disCount += Math.round(detailView.orderDetailDTO.discountAmount);
		// }
		// }
		// total = amount - disCount;

		// B2: tinh KM cho don hang
		ArrayList<OrderDetailViewDTO> listPromotionForOrderChange = new ArrayList<OrderDetailViewDTO>();
		ArrayList<PromotionProgrameDTO> listPromotionOrder = promotionProTable.getListPromotionForOrder(shopIdList, customerId);
		for (int i = 0, size = listPromotionOrder.size(); i < size; i++) {
			// lay ds CTKM tuong ung
			PromotionProgrameDTO promotionProgram = listPromotionOrder.get(i);
			if (promotionProgram != null) {
				// kiem tra CTKM hop le

				// boolean checkProgrameValid =
				// promotionProTable.checkPromotionProgrameValid(""
				// + promotionProgram.PROMOTION_PROGRAM_ID, customerId,
				// customerTypeId);
				boolean checkProgrameValid = promotionProTable.checkPromotionInValid(String.valueOf(promotionProgram.PROMOTION_PROGRAM_ID), shopIdList, customerId, customerTypeId);
				if (checkProgrameValid) {
					ArrayList<PromotionProDetailDTO> listPromotionDetail = new ArrayList<PromotionProDetailDTO>();
					// Ds cac chi tiet khuyen mai cua chuong trinh khuyen mai
					listPromotionDetail = proProgrameDetailTable.getPromotionDetailByPromotionId((int) promotionProgram.PROMOTION_PROGRAM_ID);
					if (listPromotionDetail.size() > 0) {
						// tinh KM tuong ung
						OrderDetailViewDTO promotionOrder = CalPromotions.calcPromotionForOrder(total, promotionProgram, listPromotionDetail, listProductPromotionsale, keyList, sortListOutPut);
						if (promotionOrder != null) {
							// promotionOrder.promotionDescription =
							// promotionProgram.DESCRIPTION;
							listPromotionForOrderChange.add(promotionOrder);
						}
					}
				}
			}
		}

		if (listPromotionForOrderChange.size() > 0) {
			// neu co doi CTKM cho don hang
			listProductPromotionsale.add(listPromotionForOrderChange.get(0));
		}

		if (listPromotionForOrderChange.size() > 1) {
			// neu co doi CTKM cho don hang
			orderView.listPromotionForOrderChange = listPromotionForOrderChange;

		}
	}

	/**
	 * getFeedBackList NVBH
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	public CustomerFeedBackDto getFeedBackList(String staffId, String customerId, String type, String status, String doneDate, int page, boolean getTotalPage, BaseFragment sender, int from) {
		CustomerFeedBackDto dto = null;
		FEED_BACK_TABLE feedback = new FEED_BACK_TABLE(mDB);
		dto = feedback.getFeedBackList(staffId, customerId, type, status, doneDate, page, getTotalPage, sender, from);
		return dto;
	}

	/**
	 * postFeedBack
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	public long postFeedBack(FeedBackDTO dto) {
		long returnCode = -1;
		if (dto != null) {
			try {
				mDB.beginTransaction();
				FEED_BACK_TABLE feedbackTable = new FEED_BACK_TABLE(mDB);
				TABLE_ID tabId = new TABLE_ID(mDB);
				long id = tabId.getMaxId(FEED_BACK_TABLE.FEED_BACK_TABLE);
				dto.feedBackId = id;
				returnCode = feedbackTable.insert(dto);
				mDB.setTransactionSuccessful();
			} catch (Exception e) {
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			} finally {
				if (mDB != null && mDB.inTransaction()) {
					mDB.endTransaction();
				}
			}
		}
		return returnCode;
	}

	/**
	 * postListFeedBack
	 *
	 * @author: trungnt56
	 * @param feedBackDTOs
	 * @return: ArrayList<FeedBackDTO>
	 * @throws:
	 */
	public ArrayList<FeedBackDTO> postListFeedBack(ArrayList<FeedBackDTO> feedBackDTOs) {
		ArrayList<FeedBackDTO> dtos = new ArrayList<FeedBackDTO>();
		if (feedBackDTOs != null && feedBackDTOs.size()>0) {
			try {
				mDB.beginTransaction();
				FEED_BACK_TABLE feedbackTable = new FEED_BACK_TABLE(mDB);
				TABLE_ID tabId = new TABLE_ID(mDB);
				long id = tabId.getMaxId(FEED_BACK_TABLE.FEED_BACK_TABLE);
				boolean checkSuccess = true;
				for (FeedBackDTO feedBackDTO:
					 feedBackDTOs) {
					feedBackDTO.feedBackId = id;
					feedBackDTO.setType(AbstractTableDTO.TableType.FEEDBACK_TABLE);
					id++;
					checkSuccess = feedbackTable.insert(feedBackDTO) >= 0 ? true : false;

					if (!checkSuccess) {
						break;
					}
					dtos.add(feedBackDTO);
				}
				if (checkSuccess) {
					mDB.setTransactionSuccessful();
				}
			} catch (Exception e) {
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			} finally {
				if (mDB != null && mDB.inTransaction()) {
					mDB.endTransaction();
				}
			}
		}
		return dtos;
	}

	/**
	 * insert 1 khách hàng mới
	 * 
	 * @author: duongdt3
	 * @since: 09:46:28 7 Jan 2014
	 * @return: long
	 * @throws:
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@SuppressLint("DefaultLocale")
	public long insertCustomer(CustomerDTO dto, RoutingCustomerDTO routingCustomerDTO) throws Exception {
		long returnCode = -1;
		long routingId = 0;
		try {
			mDB.beginTransaction();

			CUSTOMER_TABLE cusTable = new CUSTOMER_TABLE(mDB);
			returnCode = cusTable.insertCustomer(dto);
			if(returnCode != -1) {
				routingId = getRoutingIdByStaffId(GlobalInfo.getInstance().getProfile().getUserData().id);
				if(routingId != 0) {
					routingCustomerDTO.customerId = returnCode;
					routingCustomerDTO.routingId = routingId;
					returnCode = insertRoutingCustomer(routingCustomerDTO);
				}
			}
			mDB.setTransactionSuccessful();
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}
		return returnCode;
	}

	/**
	 * lay tuyen cua nhan vien
	 *
	 * @author: trungnt56
	 * @since: 09:46:28 7 Jan 2014
	 * @return: long
	 * @throws:
	 * @return
	 * @throws Exception
	 */
	@SuppressLint("DefaultLocale")
	public long getRoutingIdByStaffId(long staffId) throws Exception {
		ROUTING_TABLE routing_table = new ROUTING_TABLE(mDB);
		return routing_table.getRoutingIdByStaffId(staffId);
	}

	/**
	 * insert 1 khach hang vao tuyen
	 *
	 * @author: trungnt56
	 * @since: 09:46:28 7 Jan 2014
	 * @return: long
	 * @throws:
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@SuppressLint("DefaultLocale")
	public long insertRoutingCustomer(RoutingCustomerDTO dto) throws Exception {
		ROUTING_CUSTOMER_TABLE routing_customer_table = new ROUTING_CUSTOMER_TABLE(mDB);
		return routing_customer_table.insertRoutingCustomer(dto);
	}

	/**
	 * cập nhât 1 KH do staff tạo
	 * 
	 * @author: duongdt3
	 * @since: 13:58:51 7 Jan 2014
	 * @return: long
	 * @throws:
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public long updateCustomer(CustomerDTO dto) throws Exception {
		CUSTOMER_TABLE cusTable = new CUSTOMER_TABLE(mDB);
		return cusTable.updateCustomer(dto);
	}

	/**
	 * Xóa khách hàng của staff tạo
	 * 
	 * @author: duongdt3
	 * @since: 14:11:17 7 Jan 2014
	 * @return: long
	 * @throws:
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public long deleteCustomer(CustomerDTO dto) throws Exception {
		CUSTOMER_TABLE cusTable = new CUSTOMER_TABLE(mDB);
		return cusTable.deleteCustomer(dto);
	}

	/**
	 * Lay action cho phep dat hang tu xa o ngay hien tai cau staff, customer
	 * tuong ung
	 * 
	 * @author: BangHN
	 * @return
	 * @return: ActionLogDTO
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */
	public ActionLogDTO getActionLogExceptionOrderDate(String staffId, String customerId) {
		ACTION_LOG_TABLE actionTable = new ACTION_LOG_TABLE(mDB);
		return actionTable.getActionLogExceptionOrderDate(staffId, customerId);
	}

	/**
	 * Insert mot action log to db local
	 * 
	 * @author : BangHN since : 1.0
	 */
	public long insertActionLog(ActionLogDTO dto) {
		long returnCode = -1;
		if (dto != null) {
			try {
				mDB.beginTransaction();
				TABLE_ID tableId = new TABLE_ID(mDB);
				dto.id = tableId.getMaxId(ACTION_LOG_TABLE.TABLE_NAME);
				ACTION_LOG_TABLE actionTable = new ACTION_LOG_TABLE(mDB);
				returnCode = actionTable.insert(dto);
				mDB.setTransactionSuccessful();
			} finally {
				if (mDB != null && mDB.inTransaction()) {
					mDB.endTransaction();
				}
			}
		}
		return returnCode;
	}

	/**
	 * delete actoin log khi remove exception order date
	 * 
	 * @author : BangHN since : 1.0
	 */
	public long deleteActionLogWhenRemoveExceptionOrderDate(ActionLogDTO dto) {
		long returnCode = -1;
		if (dto != null) {
			try {
				mDB.beginTransaction();
				ACTION_LOG_TABLE actionTable = new ACTION_LOG_TABLE(mDB);
				returnCode = actionTable.deleteActionLogWhenRemoveExceptionOrderDate(dto);
				mDB.setTransactionSuccessful();
			} finally {
				if (mDB != null && mDB.inTransaction()) {
					mDB.endTransaction();
				}
			}
		}
		return returnCode;
	}

	public long updateActionLogToDB(ActionLogDTO dto) {
		long returnCode = -1;
		ACTION_LOG_TABLE table = new ACTION_LOG_TABLE(mDB);
		try {
			mDB.beginTransaction();
			returnCode = table.updateVisited(dto);
			mDB.setTransactionSuccessful();
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}
		return returnCode;
	}

	/**
	 * 
	 * get general statistics report info
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: GeneralStatisticsInfoViewDTO
	 * @throws:
	 */
	public GeneralStatisticsInfoViewDTO getGeneralStatisticsReportInfo(String staffId, String shopId) {
		STAFF_TABLE staff = new STAFF_TABLE(mDB);
		try {
			GeneralStatisticsInfoViewDTO vt = staff.getGeneralStatisticsInfo(staffId, shopId);
			return vt;

		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));

			return null;
		}
	}

	/**
	 * get general statistics report info for SaleMan
	 * 
	 * @author: duongdt3
	 * @since: 10:02:05 29 Nov 2013
	 * @return: GeneralStatisticsViewDTO
	 * @throws:
	 * @param staffId
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	public GeneralStatisticsViewDTO getGeneralStatisticsReportForSaleman(String staffId, String shopId)
			throws Exception {
		STAFF_TABLE staff = new STAFF_TABLE(mDB);
		GeneralStatisticsViewDTO vt = null;
		try {
			vt = staff.getGeneralStatisticsForSaleman(staffId, shopId);
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			throw e;
		}
		return vt;
	}

	/**
	 * get general statistics report info for GST
	 * 
	 * @author: duongdt3
	 * @since: 10:02:13 29 Nov 2013
	 * @return: GeneralStatisticsViewDTO
	 * @throws:
	 * @param staffId
	 * @param shopId
	 * @return
	 */
	public GSTGeneralStatisticsViewDTO getGeneralStatisticsReportForGST(String staffId, String shopId) {
		STAFF_TABLE staff = new STAFF_TABLE(mDB);
		try {
			GSTGeneralStatisticsViewDTO vt = staff.getGeneralStatisticsForGST(staffId, shopId);
			return vt;
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			return null;
		}
	}

	/**
	 * get general statistics report info for TTTT
	 * 
	 * @author: duongdt3
	 * @since: 10:02:20 29 Nov 2013
	 * @return: GeneralStatisticsTTTTViewDTO
	 * @throws:
	 * @param staffId
	 * @param shopId
	 * @param idPG
	 * @param date
	 * @param isLoadStaffList
	 * @return
	 */
	public GeneralStatisticsTNPGViewDTO getGeneralStatisticsReportForTNPG(String staffId, String shopId, String idPG, String date, boolean isLoadStaffList) {
		STAFF_TABLE staff = new STAFF_TABLE(mDB);
		try {
			GeneralStatisticsTNPGViewDTO vt = staff.getGeneralStatisticsForTNPG(staffId, shopId, idPG, date, isLoadStaffList);
			return vt;
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			return null;
		}
	}

	/**
	 * get general statistics report info for Supervior (GSBH)
	 * 
	 * @author: duongdt3
	 * @since: 14:24:23 25 Nov 2013
	 * @return: GeneralStatisticsViewDTO
	 * @throws:
	 * @param staffId
	 * @param shopId
	 * @param isLoadListStaff
	 * @return
	 * @throws Exception
	 */
	public GSBHGeneralStatisticsViewDTO getGeneralStatisticsReportForSupervior(String staffId, String shopId, String parrentStaffId, boolean isLoadListStaff)
			throws Exception {
		STAFF_TABLE staff = new STAFF_TABLE(mDB);
		GSBHGeneralStatisticsViewDTO vt = null;
		try {
			vt = staff.getGeneralStatisticsForSupervior(staffId, shopId, parrentStaffId, isLoadListStaff);
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			throw e;
		}
		return vt;
	}

	/**
	 * 
	 * get list note for general statistics view
	 * 
	 * @author: HaiTC3
	 * @param staffId
	 * @param shopId
	 * @return
	 * @return: ListNoteInfoViewDTO
	 * @throws:
	 */
	public ListNoteInfoViewDTO getListNoteForGeneralStaticsView(String staffId, String shopId) {
		FEED_BACK_TABLE feedBack = new FEED_BACK_TABLE(mDB);
		ListNoteInfoViewDTO listNote = null;
		try {
			listNote = feedBack.getListNote(staffId, shopId, " limit 0,2");
			return listNote;
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			return null;
		}
	}

	/**
	 * 
	 * update note infor to database
	 * 
	 * @author: HaiTC3
	 * @param staffId
	 * @param shopId
	 * @param noteUpdate
	 * @return
	 * @return: ListNoteInfoViewDTO
	 * @throws:
	 */
	public ListNoteInfoViewDTO updateNoteInfoToDB(String staffId, String shopId, NoteInfoDTO noteUpdate) {
		ListNoteInfoViewDTO listNote = new ListNoteInfoViewDTO();
		FEED_BACK_TABLE feedBack = new FEED_BACK_TABLE(mDB);
		try {
			mDB.beginTransaction();
			listNote = feedBack.updateNoteInfo(staffId, shopId, noteUpdate);
			mDB.setTransactionSuccessful();
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			listNote = null;
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}
		return listNote;
	}

	/**
	 * getCustomerList
	 * 
	 * @author: TamPQ
	 * @return
	 * @return: CustomerListDTO
	 * @throws:
	 */
	public CustomerListDTO getCustomerList(long staffId, long shopId, String name, String code, String address, String visit_plan, boolean isGetWrongPlan, int page, boolean isGetTotalPage, int type) {
		CustomerListDTO dto = null;
		CUSTOMER_TABLE custommerTable = new CUSTOMER_TABLE(mDB);
		try {
			dto = custommerTable.getCustomerList(staffId, shopId, name, code, visit_plan, isGetWrongPlan, page, isGetTotalPage, type); // loại
																																	   // nhân
																																	   // viên.
		} catch (Exception e) {
		} finally {
		}
		return dto;
	}

	/**
	 * getCustomerList
	 * 
	 * @author: TamPQ
	 * @return
	 * @return: CustomerListDTO
	 * @throws:
	 */
	public CustomerListDTO getListCustomerOfC2(long staffId, long shopId, long c2CustomerId, String name, String code, int page, boolean isGetTotalPage, int type) {
		CustomerListDTO dto = null;
		CUSTOMER_TABLE custommerTable = new CUSTOMER_TABLE(mDB);
		try {
			dto = custommerTable.getCustomerListOfC2(staffId, shopId, c2CustomerId, name, code, page, isGetTotalPage, type); // loại
																															 // nhân
																															 // viên.
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
		}
		return dto;
	}

	/**
	 * Lay danh sach khach hang trong mot tuyen
	 * 
	 * @author : BangHN since : 1.0
	 */
	public CustomerListDTO getCustomerListForRoute(long staffId, long shopId, String visitPlan, boolean isGetWrongPlan, int type) {
		CustomerListDTO dto = null;
		CUSTOMER_TABLE custommerTable = new CUSTOMER_TABLE(mDB);
		try {
			dto = custommerTable.getCustomerList(staffId, shopId, null, null, visitPlan, isGetWrongPlan, -1, true, type);
		} catch (Exception e) {
		}
		return dto;
	}

	/**
	 * Lay danh sach khach hang trong mot tuyen cua ban do TBHV
	 * 
	 * @author : BangHN since : 1.0
	 */
	public CustomerListDTO getTBHVCustomerInVisitPlan(long staffId, long shopId, String visitPlan, boolean isGetWrongPlan) {
		CustomerListDTO dto = null;
		CUSTOMER_TABLE custommerTable = new CUSTOMER_TABLE(mDB);
		try {
			dto = custommerTable.getTBHVListCustomer(staffId, shopId, null, null, null, visitPlan, isGetWrongPlan, -1, 1);
		} catch (Exception e) {
		}
		return dto;
	}

	/**
	 * 
	 * cap nhat thuc hien van de TTTT, NVBH
	 * 
	 * @author: YenNTH
	 * @param dto
	 * @return
	 * @return: long
	 * @throws:
	 */
	public long updateDoneDateFeedBack(FeedBackDTO dto) {
		long returnCode = -1;
		if (dto != null) {
			mDB.beginTransaction();
			try {
				FEED_BACK_TABLE feedbackTable = new FEED_BACK_TABLE(mDB);
				returnCode = feedbackTable.updateDoneDateFeedBack(dto);
				mDB.setTransactionSuccessful();
			} finally {
				if (mDB != null && mDB.inTransaction()) {
					mDB.endTransaction();
				}
			}
		}
		return returnCode;
	}

	public long deleteFeedBack(FeedBackDTO dto) {
		long returnCode = -1;
		if (dto != null) {
			try {
				mDB.beginTransaction();
				FEED_BACK_TABLE feedbackTable = new FEED_BACK_TABLE(mDB);
				returnCode = feedbackTable.delete(dto);
				mDB.setTransactionSuccessful();
			} catch (Exception e) {
			} finally {
				if (mDB != null && mDB.inTransaction()) {
					mDB.endTransaction();
				}
			}
		}
		return returnCode;
	}

	/**
	 * 
	 * cap nhat feedback TTTT, NVBH
	 * 
	 * @author: YenNTH
	 * @param dto
	 * @return
	 * @return: long
	 * @throws:
	 */
	public long updateDeleteFeedBack(FeedBackDTO dto) {
		long returnCode = -1;
		if (dto != null) {
			try {
				mDB.beginTransaction();
				FEED_BACK_TABLE feedbackTable = new FEED_BACK_TABLE(mDB);
				returnCode = feedbackTable.updateDeleteFeedBack(dto);
				mDB.setTransactionSuccessful();
			} catch (Exception e) {
			} finally {
				if (mDB != null && mDB.inTransaction()) {
					mDB.endTransaction();
				}
			}
		}
		return returnCode;
	}

	/**
	 * 
	 * Cap nhat truong is send cua 1 order
	 * 
	 * @author: PhucNT
	 * @param order
	 * @return
	 * @return: boolean
	 * @throws:
	 */
	public boolean updateSentOrder(SaleOrderDTO order) {
		boolean result = false;
		try {
			mDB.beginTransaction();
			SALE_ORDER_TABLE saleOrderTable = new SALE_ORDER_TABLE(mDB);
			saleOrderTable.updateSentOrder(order);
			mDB.setTransactionSuccessful();
			result = true;
		} catch (Exception e) {
			result = false;
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}
		return result;
	}

	/**
	 * Lay ds san pham khuyen mai cua 1 don hang co san
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param orderId
	 *            : id cua don hang
	 * @return
	 * @return: ListSaleOrderDTO
	 * @throws:
	 */
	public ListSaleOrderDTO getPromotionProductsForEdit(long orderId, String orderType) {
		ListSaleOrderDTO list = new ListSaleOrderDTO();
		try {

			SALES_ORDER_DETAIL_TABLE orderDetailTable = new SALES_ORDER_DETAIL_TABLE(mDB);
			List<OrderDetailViewDTO> listProduct = orderDetailTable.getPromotionProductsForEdit(Long.toString(orderId), orderType);

			list.listData.addAll(listProduct);
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

		return list;
	}

	/**
	 * Lay danh sach khach hang tham gia chuong trinh khuyen mai
	 * 
	 * @author: ThanhNN8
	 * @param extPage
	 * @param displayProgrameCode
	 * @return
	 * @return: List<CustomerAttentProgrameDTO>
	 * @throws:
	 */
	public ListCustomerAttentProgrameDTO getListCustomerAttentPrograme(String extPage, String displayProgrameCode, long displayProgrameId, String customer_code, String customer_name, long staffId, boolean checkLoadMore) {
		// TODO Auto-generated method stub

		CUSTOMER_DISPLAY_PROGRAME_TABLE customerDisplayTable = new CUSTOMER_DISPLAY_PROGRAME_TABLE(mDB);
		ListCustomerAttentProgrameDTO result = customerDisplayTable.getListCustomerAttentPrograme(extPage, displayProgrameCode, displayProgrameId, customer_code, customer_name, staffId, checkLoadMore);
		return result;
	}

	/**
	 * Lay ds log can de thuc hien retry
	 * 
	 * @author: TruongHN
	 * @return: ArrayList<LogDTO>
	 * @throws:
	 */
	public ArrayList<LogDTO> getLogNeedToCheck() {
		ArrayList<LogDTO> result = new ArrayList<LogDTO>();
		if (mDB != null && mDB.isOpen()) {
			LOG_TABLE logTable = new LOG_TABLE(mDB);
			result = logTable.getLogNeedToCheck();
		}
		return result;
	}

	/**
	 * Lay ds log can de thuc hien retry su dung truoc khi sync du lieu tu
	 * server ve
	 * 
	 * @author: TruongHN
	 * @return: ArrayList<LogDTO>
	 * @throws:
	 */
	public ArrayList<LogDTO> getLogNeedToCheckBeforeSync() {
		ArrayList<LogDTO> result = new ArrayList<LogDTO>();
		if (mDB != null && mDB.isOpen()) {
			LOG_TABLE logTable = new LOG_TABLE(mDB);
			result = logTable.getLogNeedToCheckBeforeSync();
		}
		return result;
	}

	/**
	 * Lay ds log can de thuc hien retry su dung truoc khi sync du lieu tu
	 * server ve (khi login)
	 * 
	 * @author: TruongHN
	 * @return: ArrayList<LogDTO>
	 * @throws:
	 */
	public ArrayList<LogDTO> getLogNeedToCheckForLogin() {
		ArrayList<LogDTO> result = new ArrayList<LogDTO>();
		if (mDB != null && mDB.isOpen()) {
			LOG_TABLE logTable = new LOG_TABLE(mDB);
			result = logTable.getLogNeedToCheckForLogin();
		}
		return result;
	}

	/**
	 * Lay so factor default (dung cho cac truong hop generate id khi them moi)
	 * 
	 * @author: TruongHN
	 * @return: long
	 * @throws:
	 */
	public long getFactorValue() {
		// TABLE_ID table = new TABLE_ID(mDB);
		// return table.getFactorValue();
		long factor = 0;
		AP_PARAM_TABLE apTable = new AP_PARAM_TABLE(mDB);
		ArrayList<ApParamDTO> result = apTable.getConstantsApp();

		for (int i = 0, size = result.size(); i < size; i++) {
			if ("FACTOR".equals(result.get(i).type)) {
				factor = Long.valueOf(result.get(i).apParamCode);
				break;
			}
		}
		return factor;
	}

	/**
	 * Get nhung constant dinh nghia cho app "FACTOR", "TIME_TEST_ORDER",
	 * "RADIUS_OF_POSITION", "TIME_TRIG_POSITION"
	 * 
	 * @author banghn
	 */
	public ArrayList<ApParamDTO> getAppDefineConstant() {
		AP_PARAM_TABLE apTable = new AP_PARAM_TABLE(mDB);
		ArrayList<ApParamDTO> result = apTable.getConstantsApp();
		return result;
	}

	/**
	 * Lay so factor default (dung cho cac truong hop generate id khi them moi)
	 * 
	 * @author: TruongHN
	 * @return: long
	 * @throws:
	 */
	public long getFactorValue2() {
		TABLE_ID table = new TABLE_ID(mDB);
		long res = 1;
		try {
			mDB.beginTransaction();
			res = table.getFactorValue();
			mDB.setTransactionSuccessful();
		} catch (Exception ex) {
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}

		return res;
	}

	/**
	 * 
	 * lay danh sach chuong trinh lien quan toi san pham
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: List<ProgrameForProductDTO>
	 * @throws:
	 */
	public List<ProgrameForProductDTO> getListProgrameForProduct(String shop_id, String staff_id, String customer_id, String customerTypeId) {
		// sale order detail - lay ds order detail
		SALES_ORDER_DETAIL_TABLE saleOrderDetailTable = new SALES_ORDER_DETAIL_TABLE(mDB);

		try {
			List<ProgrameForProductDTO> vt = saleOrderDetailTable.getListProgrameForProduct(shop_id, staff_id, customer_id, customerTypeId);
			return vt;

		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			return null;
		}
	}

	/**
	 * Cáº­p nháº­t nhá»¯ng Ä‘Æ¡n hÃ ng sang tráº¡ng thÃ¡i chuyá»ƒn
	 * 
	 * @author: PhucNT
	 *            <SaleOrderDTO>
	 * @return
	 * @return: boolean
	 * @throws:
	 */
	private boolean updateNumberRemainProduct(ArrayList<CustomerStockHistoryDTO> order) {

		boolean checkSuccess = true;
		try {
			// mDB.beginTransaction();
			CUSTOMER_STOCK_HISTORY_TABLE cusStockHistory = new CUSTOMER_STOCK_HISTORY_TABLE(SQLUtils.getInstance().getmDB());
			for (int i = 0; i < order.size(); i++) {
				CustomerStockHistoryDTO s = order.get(i);
				checkSuccess = cusStockHistory.insertOrUpdate(s) >= 0 ? true
						: false;
				if (!checkSuccess) {
					break;
				}
			}
			// mDB.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			checkSuccess = false;
		} finally {
//			if (mDB != null && mDB.inTransaction()) {
//				mDB.endTransaction();
//			}
		}

		return checkSuccess;
	}

	/**
	 * Lay ds param by type
	 * 
	 * @author: TruongHN
	 * @param code
	 * @return: ArrayList<ApParamDTO>
	 * @throws:
	 */
	public ArrayList<ApParamDTO> getListParaByType(String code) {
		AP_PARAM_TABLE apTable = new AP_PARAM_TABLE(mDB);
		return apTable.getAllByType(code);
	}

	/**
	 * Tinh sku cua khach hang trong thang: So luong san pham khach hang mua
	 * trong thang
	 * 
	 * @author : BangHN since : 1.0
	 */
	public int getSKUOfCustomerInMonth(String customerId, String shopId) {
		int sku = 0;
		SALE_ORDER_TABLE saleOrder = new SALE_ORDER_TABLE(mDB);
		try {
			sku = saleOrder.getSKUOfCustomerInMonth(customerId, shopId);
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return sku;
	}

	/**
	 * So lan mua cua khach hang trong thang: So don hang trong thang
	 * 
	 * @author : BangHN since : 1.0
	 */
	public int getNumberOrdersInMonth(String customerId) {
		int sku = 0;
		SALE_ORDER_TABLE saleOrder = new SALE_ORDER_TABLE(mDB);
		try {
			sku = saleOrder.getNumberOrdersInMonth(customerId);
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return sku;
	}

	/**
	 * doanh so cua khach hang dat duoc trong thang
	 * 
	 * @author : BangHN since : 1.0
	 */
	public long getAverageSalesInMonth(String customerId, String shopId) {
		long amount = 0;
		SALE_ORDER_TABLE saleOrder = new SALE_ORDER_TABLE(mDB);
		try {
			amount = saleOrder.getAverageSalesInMonth2(customerId, shopId);
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return amount;
	}

	/**
	 * Lay doanh so cua khach hang trong 3 thang gan day
	 * 
	 * @author : BangHN since : 1.0
	 */
	public ArrayList<SaleInMonthDTO> getAverageSalesIn3MonthAgo(String customerId) {
		ArrayList<SaleInMonthDTO> saleIn3Month;
		SALE_ORDER_TABLE saleOrder = new SALE_ORDER_TABLE(mDB);
		try {
			saleIn3Month = saleOrder.getAverageSalesIn3MonthAgo(customerId);
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			return null;
		}
		return saleIn3Month;
	}

	/**
	 * Lay bai gioi thieu cua san pham
	 * 
	 * @author: ThanhNN8
	 * @param productId
	 * @return
	 * @return: IntroduceProductDTO
	 * @throws:
	 */
	public IntroduceProductDTO getIntroduceProduct(String productId) {
		// TODO Auto-generated method stub
		try {
			MEDIA_ITEM_TABLE mediaItemTable = new MEDIA_ITEM_TABLE(mDB);
			PRODUCT_INTRODUCTION_TABLE introTable = new PRODUCT_INTRODUCTION_TABLE(mDB);
			IntroduceProductDTO result = new IntroduceProductDTO();
			// get html content for introduce
			String htmlContent = introTable.getContentIntroduction(productId);
			result.setHtmlContextIntroduce(htmlContent);
			// get all image for product
			List<MediaItemDTO> listMedia = mediaItemTable.getListMediaOfProduct(productId);
			if (listMedia != null) {
				result.setListMedia(listMedia);
			}
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			return null;
		}
	}

	/**
	 * get List Sale Road map Supervisor
	 * 
	 * @author: TamPQ
	 * @throws Exception
	 * @return:
	 * @throws:
	 */
	public GsnppRouteSupervisionDTO getGsnppRouteSupervision(long staffId, String today, String shopId) {
		STAFF_TABLE staffTable = new STAFF_TABLE(mDB);
		GsnppRouteSupervisionDTO dto = null;
		try {
			dto = staffTable.getGsnppRouteSupervision(staffId, today, shopId);
		} catch (Exception ex) {
			VTLog.d("[Quang]", ex.getMessage());
		}
		return dto;
	}

	/**
	 * update thong tin vi tri cua khach hang
	 * 
	 * @author : BangHN since : 1.0
	 */
	public long updateCustomerLocation(CustomerDTO customer) {
		long update = -1;
		CUSTOMER_TABLE cusTab = new CUSTOMER_TABLE(mDB);
		try {
			mDB.beginTransaction();
			update = cusTab.updateLocation(customer);
			mDB.setTransactionSuccessful();
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}
		return update;
	}

	/**
	 * Lay danh sach loai chuong trinh cho chuong trinh trung bay
	 * 
	 * @author: ThanhNN8
	 * @return
	 * @return: List<ComboBoxDisplayProgrameItemDTO>
	 * @throws:
	 */
	public List<DisplayProgrameItemDTO> getListTypeDisplayPrograme() {
		// TODO Auto-generated method stub
		AP_PARAM_TABLE apParamTable = new AP_PARAM_TABLE(mDB);
		return apParamTable.getListTypeDisplayPrograme();
	}

	/**
	 * Lay danh sach nganh hang cho chuong trinh trung bay
	 * 
	 * @author: ThanhNN8
	 * @return
	 * @return: List<ComboBoxDisplayProgrameItemDTO>
	 * @throws:
	 */
	public List<DisplayProgrameItemDTO> getListDepartDisplayPrograme() {
		// TODO Auto-generated method stub
		PRODUCT_INFO_TABLE productInfoTable = new PRODUCT_INFO_TABLE(mDB);
		return productInfoTable.getListDepartDisplayPrograme();
	}

	public AccSaleProgReportDTO getAccSaleProgReport(long staffId, String shopId) {
		RPT_STAFF_SALE_TABLE staffTable = new RPT_STAFF_SALE_TABLE(mDB);
		return staffTable.getAccSaleProgReportDTO(staffId, shopId);
	}

	public DisProComProgReportDTO getDisProComProReport(long staffId, String shopId) {
		RPT_DISPLAY_PROGRAME_TABLE table = new RPT_DISPLAY_PROGRAME_TABLE(mDB);
		return table.getDisProComProReportDTO(staffId, shopId);
	}

	public ProgressDateReportDTO getProgressDateReport(long staffOwnerId, long shopId) {
		RPT_STAFF_SALE_TABLE table = new RPT_STAFF_SALE_TABLE(mDB);
		return table.getProgressDateReport(staffOwnerId, shopId);
	}

	public ProgressDateDetailReportDTO getProgressDateDetailReport(long staffId) {
		RPT_STAFF_SALE_TABLE table = new RPT_STAFF_SALE_TABLE(mDB);
		return table.getProgressDateDetailReport(staffId);
	}

	public TBHVProgressDateReportDTO getTBHVProgressDateReport(String staffOwnerId) {
		RPT_STAFF_SALE_TABLE table = new RPT_STAFF_SALE_TABLE(mDB);
		return table.getTBHVProgressDateReport(staffOwnerId);
	}

	public TBHVCustomerNotPSDSReportDTO getTBHVNotPSDSReport(String staffOwnerId) {
		RPT_STAFF_SALE_TABLE table = new RPT_STAFF_SALE_TABLE(mDB);
		return table.getTBHVNotPSDSReport(staffOwnerId);
	}

	public TBHVProgressDateReportDTO getTBHVProgressDateDetailReport(String shopId, String staffOwnerId) {
		RPT_STAFF_SALE_TABLE table = new RPT_STAFF_SALE_TABLE(mDB);
		return table.getTBHVProgressDateDetailReport(shopId, staffOwnerId);
	}

	/**
	 * Lay danh sach khach hang ghe tham cua NVBH
	 * 
	 * @author: QuangVT
	 * @since: 4:16:20 PM Dec 19, 2013
	 * @return: VisitDTO
	 * @throws:
	 * @param staffId
	 * @param customerId
	 * @return
	 */
	public VisitDTO getListVisit(long staffId, long customerId) {
		RPT_STAFF_SALE_TABLE table = new RPT_STAFF_SALE_TABLE(mDB);
		return table.getListVisitNotPSDS(staffId, customerId);
	}

	public ManagerEquipmentDTO getListEquipment(long staffId) {
		RPT_STAFF_SALE_TABLE table = new RPT_STAFF_SALE_TABLE(mDB);
		return table.getListEquipment(staffId);
	}

	public TBHVManagerEquipmentDTO getListEquipmentTBHV(long parentShopId) {
		RPT_STAFF_SALE_TABLE table = new RPT_STAFF_SALE_TABLE(mDB);
		return table.getListEquipmentTBHV(parentShopId);
	}

	public TBHVManagerEquipmentDTO getTBHVListEquipment(String shopId) {
		RPT_STAFF_SALE_TABLE table = new RPT_STAFF_SALE_TABLE(mDB);
		return table.getListEquipment(shopId);
	}

	// hoan
	public TBHVDisProComProgReportDTO getTBHVDisProComProReport(Bundle data) {
		RPT_DISPLAY_PROGRAME_TABLE table = new RPT_DISPLAY_PROGRAME_TABLE(mDB);
		return table.getTBHVDisProComProReportDTO(data);

	}

	public TBHVDisProComProgReportNPPDTO getTBHVDisProComProReportNPP(Bundle data) {
		RPT_DISPLAY_PROGRAME_TABLE table = new RPT_DISPLAY_PROGRAME_TABLE(mDB);
		return table.getTBHVDisProComProReportNPP(data);

	}

	public CabinetStaffDTO getCabinetStaff(long shopId, long staffId, int isAll, String page) {
		RPT_STAFF_SALE_TABLE table = new RPT_STAFF_SALE_TABLE(mDB);
		return table.getCabinetStaff(shopId, staffId, isAll, page);
	}

	public int getCountCabinetStaff(long shopId, long staffId, int isAll) {
		RPT_STAFF_SALE_TABLE table = new RPT_STAFF_SALE_TABLE(mDB);
		return table.getCountCabinetStaff(shopId, staffId, isAll);
	}

	public int getCountCustomerNotPsdsInMonthReport(long shopId, String staffOwnerId, String visit_plan, String staffId) {
		CUSTOMER_TABLE table = new CUSTOMER_TABLE(mDB);
		return table.getCountCustomerNotPsdsInMonthReport(shopId, staffOwnerId, visit_plan, staffId);
	}

	public ListStaffDTO getListNVBH(long shopId, String staffOwnerId) {
		STAFF_TABLE table = new STAFF_TABLE(mDB);
		return table.getListNVBH(shopId, staffOwnerId);
	}

	public ListStaffDTO getListNVBHNotPSDS(long shopId, String staffOwnerId) {
		STAFF_TABLE table = new STAFF_TABLE(mDB);
		return table.getListNVBHForGSNPP(shopId + "", staffOwnerId, false);
	}

	public ListStaffDTO getListStaffForGSBHOrderCode(long staffId, boolean isHaveAll) {
		RPT_STAFF_SALE_TABLE table = new RPT_STAFF_SALE_TABLE(mDB);
		return table.getListStaffForGSBHOrderCode(staffId, isHaveAll);
	}

	public ListStaffDTO getListNVBHOrderCode(long shopId, String staffOwnerId, boolean isHaveAll) {
		RPT_STAFF_SALE_TABLE table = new RPT_STAFF_SALE_TABLE(mDB);
		return table.getListNVBHOrderCode(shopId, staffOwnerId, isHaveAll);
	}

	/**
	 *
	 * Lay danh sach GSNPP thuoc GSBH
	 *
	 * @author: TrungNT56
	 * @param staffId
	 * @return
	 * @return: ArrayList<String>
	 * @throws:
	 */
	public ListStaffDTO getListGSNPPByGSBH(String staffId, boolean isHaveAll) {
		STAFF_TABLE staff_table = new STAFF_TABLE(mDB);
		return staff_table.getListGSNPPByGSBH(staffId, isHaveAll);
	}

	/**
	 *
	 * Lay danh sach nhan vien thuoc GSBH
	 *
	 * @author: TrungNT56
	 * @param staffId
	 * @return
	 * @return: ArrayList<String>
	 * @throws:
	 */
	public ListStaffDTO getListNVBHByGSBH(String staffId, boolean isHaveAll) {
		STAFF_TABLE staff_table = new STAFF_TABLE(mDB);
		return staff_table.getListNVBHByGSBH(staffId, isHaveAll);
	}

	/**
	 *
	 * Lay danh sach nhan vien thuoc GSNPP
	 *
	 * @author: TrungNT56
	 * @param staffId
	 * @return
	 * @return: ArrayList<String>
	 * @throws:
	 */
	public ListStaffDTO getListNVBHByGSNPP(String staffId, boolean isHaveAll, boolean isOrder) {
		STAFF_TABLE staff_table = new STAFF_TABLE(mDB);
		return staff_table.getListNVBHByGSNPP(staffId, isHaveAll, isOrder);
	}

	/**
	 * 
	 * bao cao tien do ban MHTT den ngay
	 * 
	 * @author: HaiTC3
	 * @param ext
	 * @return
	 * @return: ProgressReportSalesFocusDTO
	 * @throws:
	 * @since: Jan 29, 2013
	 */
	public ProgressReportSalesFocusDTO getProgReportSalesFocus(Bundle ext) {
		RPT_STAFF_SALE_TABLE focusTable = new RPT_STAFF_SALE_TABLE();
		return focusTable.getProgReportSalesFocus(ext);
	}

	/**
	 * Mo ta chuc nang cua ham
	 * 
	 * @author: ThanhNN8
	 * @param dto
	 * @return
	 * @return: int
	 * @throws:
	 */
	public int updateClientThumbnailUrl(MediaItemDTO dto) {
		// TODO Auto-generated method stub
		int result = 0;
		try {
			mDB.beginTransaction();
			MEDIA_ITEM_TABLE mediaTable = new MEDIA_ITEM_TABLE(mDB);
			result = mediaTable.updateClientThumbnailUrl(dto.sdCardPath, dto.id);
			mDB.setTransactionSuccessful();
		} catch (Exception e) {
			result = -1;
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}
		return result;
	}

	public DisProComProgReportDTO getStaffDisProComProReportDTO(long staffId, long shopId, int proId) {
		RPT_DISPLAY_PROGRAME_TABLE rptTable = new RPT_DISPLAY_PROGRAME_TABLE(mDB);
		return rptTable.getStaffDisProComProReportDTO(staffId, shopId, proId);
	}

	// bao cao tien do CTTB chi tiet NVBH ngay //
	public ProgReportProDispDetailSaleDTO getProgReportProDispDetailSaleDTO(long staffId, String displayProgrameCode, int checkAll, String page) {
		RPT_DISPLAY_PROGRAME_TABLE rptTable = new RPT_DISPLAY_PROGRAME_TABLE(mDB);
		return rptTable.getProgReportProDispDetailSaleDTO(staffId, displayProgrameCode, checkAll, page);
	}

	/**
	 * ds huan luyen tich luy ngay
	 * 
	 * @author: TampQ
	 * @return
	 * @return: int
	 * @throws:
	 */
	public GsnppTrainingResultAccReportDTO getAccTrainResultReportDTO(long staffId, String shopId) {
		return new TRAINING_PLAN_DETAIL_TABLE(mDB).getAccTrainResultReportDTO(staffId, shopId);
	}

	/**
	 * ds huan luyen ngay
	 * 
	 * @author: TampQ
	 * @return
	 * @return: int
	 * @throws:
	 */

	public GSNPPTrainingResultDayReportDTO getGsnppTrainingResultDayReport(long staffTrainId, long shopId) {
		return new RPT_SALE_RESULT_DETAIL_TABLE(mDB).getGsnppTrainingResultDayReport(staffTrainId, shopId);
	}

	/**
	 * Danh sach hinh anh khach hang NVBH
	 * 
	 * @author quangvt1
	 * @return: ImageListDTO
	 * @throws:
	 */
	public ImageListDTO getImageList(Bundle data) {
		ImageListDTO dto = null;
		CUSTOMER_TABLE custommerTable = new CUSTOMER_TABLE(mDB);
		Bundle bundle = new Bundle();
		bundle.putLong(IntentConstants.INTENT_STAFF_ID, data.getLong(IntentConstants.INTENT_STAFF_ID));
		bundle.putString(IntentConstants.INTENT_SHOP_ID, data.getString(IntentConstants.INTENT_SHOP_ID));
		try {
			dto = custommerTable.getImageList(data);
			return dto;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Danh sach hinh anh khach hang cua GSNPP
	 * 
	 * @author: QuangVT
	 * @since: 1:21:46 PM Dec 12, 2013
	 * @return: ImageListDTO
	 * @throws:
	 * @param ext
	 * @return
	 */
	public ImageListDTO getSupervisorImageList(Bundle ext) {
		ImageListDTO dto = null;
		CUSTOMER_TABLE custommerTable = new CUSTOMER_TABLE(mDB);
		try {
			dto = custommerTable.getSupervisorImageList(ext);
			return dto;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Danh sach hinh anh khach hang cua GSBH
	 *
	 * @author: trungnt56
	 * @since: 1:21:46 PM Dec 12, 2013
	 * @return: ImageListDTO
	 * @throws:
	 * @param ext
	 * @return
	 */
	public ImageListDTO getGSBHImageList(Bundle ext) {
		ImageListDTO dto = null;
		CUSTOMER_TABLE custommerTable = new CUSTOMER_TABLE(mDB);
		try {
			dto = custommerTable.getGSBHImageList(ext);
			return dto;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Danh sach hinh anh khach hang GSNPP
	 * 
	 * @author: HoanPD1
	 * @return: ImageListDTO
	 * @throws:
	 */
	public ImageListDTO getImageListGSNPP(Bundle ext) {
		// sale order detail - lay ds order detail
		ImageListDTO dto = null;
		CUSTOMER_TABLE custommerTable = new CUSTOMER_TABLE(mDB);
		try {

			dto = custommerTable.getImageList(ext);
			return dto;
		} catch (Exception e) {

			return null;
		}

	}

	// bao cao tien do CTTB chi tiet NVBH ngay //
	public TBHVProgReportProDispDetailSaleDTO getTBHVProgReportProDispDetailSaleDTO(long staffId, String displayProgrameCode, String displayProgrameLevel, int checkAll, int page) {
		RPT_DISPLAY_PROGRAME_TABLE rptTable = new RPT_DISPLAY_PROGRAME_TABLE(mDB);
		return rptTable.getTBHVProgReportProDispDetailSaleDTO(staffId, displayProgrameCode, displayProgrameLevel, checkAll, page);
	}

	/**
	 * 
	 * Danh sach chuong trinh trung bay danh cho nhan vien giam sat
	 * 
	 * @author: ThanhNN8
	 * @param ext
	 * @return
	 * @return: DisplayProgrameModel
	 * @throws:
	 */
	public DisplayProgrameModel getListSuperVisorDisplayPrograme(Bundle ext) {
		SHOP_TABLE shopTable = new SHOP_TABLE(mDB);
		DISPLAY_PROGRAME_TABLE displayProgrameTable = new DISPLAY_PROGRAME_TABLE(mDB);

		String shopId = ext.getString(IntentConstants.INTENT_SHOP_ID);
		ArrayList<String> shopIdArray = shopTable.getShopRecursive(shopId);
		DisplayProgrameModel model = displayProgrameTable.getListSuperDisplayPrograme(ext, shopIdArray);

		// List<DisplayProgrameDTO> modelData = model.getModelData();
		// int length = modelData.size();
		// for (int i = 0; i < length; i++) {
		// DisplayProgrameDTO dpDTO = modelData.get(i);
		// int quatity = rptDisplayProgrameTable.countCustomerNotComplete(
		// dpDTO.displayProgrameCode, false);
		// dpDTO.quantity = quatity;
		// dpDTO.countCustomerNotComplete = rptDisplayProgrameTable
		// .countCustomerNotComplete(dpDTO.displayProgrameCode, true);
		// }
		return model;
	}

	/**
	 * 
	 * Lay ds chuong tinh trung bay cua TBHV
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param ext
	 * @return
	 * @return: DisplayProgrameModel
	 * @throws:
	 */
	public TBHVDisplayProgrameModel getListTBHVDisplayPrograme(Bundle ext) {

		DISPLAY_PROGRAME_TABLE displayProgrameTable = new DISPLAY_PROGRAME_TABLE(mDB);
		TBHVDisplayProgrameModel model = displayProgrameTable.getListTBHVDisplayPrograme(ext);
		return model;
	}

	/**
	 * Lay danh sach theo doi khac phuc cua GSBH
	 * 
	 * @author: YenNTH
	 * @return
	 * @return: FollowProblemDTO
	 * @throws:
	 */
	public FollowProblemDTO getListFollowProblem(Bundle data) {
		// TODO Auto-generated method stub
		FEED_BACK_TABLE feedBackTable = new FEED_BACK_TABLE(mDB);
		STAFF_TABLE staffTable = new STAFF_TABLE(mDB);
		FollowProblemDTO model = feedBackTable.getListProblemOfSuperVisor(data);
		boolean checkCombobox = data.getBoolean(IntentConstants.INTENT_CHECK_COMBOBOX, false);
		if (checkCombobox) {
			ComboboxFollowProblemDTO comboboxDTO = new ComboboxFollowProblemDTO();
			comboboxDTO.listNVBH = staffTable.getStaffCodeComboboxProblemNVBHOfSuperVisor();
			comboboxDTO.listStatus = feedBackTable.getComboboxProblemStatusOfSuperVisor();
			comboboxDTO.listTypeProblem = staffTable.getComboboxProblemTypeProblemOfSuperVisor();
			model.comboboxDTO = comboboxDTO;
		} else {
			model.comboboxDTO = null;
		}
		return model;
	}

	/**
	 * 
	 * Lấy danh sách loại vấn đề role GST
	 * 
	 * @author: YenNTH
	 * @param data
	 * @return
	 * @return: FollowProblemDTO
	 * @throws:
	 */
	public TBHVFollowProblemDTO getTBHVListFollowProblem(Bundle data) {
		// TODO Auto-generated method stub
		TRAINING_SHOP_MANAGER_RESULT_TABLE feedBackTable = new TRAINING_SHOP_MANAGER_RESULT_TABLE(mDB);
		STAFF_TABLE staffTable = new STAFF_TABLE(mDB);
		TBHVFollowProblemDTO model = feedBackTable.getListProblemOfTBHV(data);

		boolean checkCombobox = data.getBoolean(IntentConstants.INTENT_CHECK_COMBOBOX);
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		if (checkCombobox) {
			ComboboxFollowProblemDTO comboboxDTO = new ComboboxFollowProblemDTO();
			comboboxDTO.listNVBH = staffTable.getStaffCodeComboboxProblemSuperVisorOfTBHV(shopId);
			comboboxDTO.listStatus = feedBackTable.getComboboxProblemStatusOfTBHV();
			comboboxDTO.listTypeProblem = staffTable.getComboboxProblemTypeProblem(false);
			model.comboboxDTO = comboboxDTO;
		} else {
			model.comboboxDTO = null;
		}
		return model;
	}

	/**
	 * Lay danh sach combobox cho man hinh theo doi khac phuc
	 * 
	 * @author: ThanhNN8
	 * @return
	 * @return: ComboboxFollowProblemDTO
	 * @throws:
	 */
	public ComboboxFollowProblemDTO getComboboxListFollowProblem() {
		// TODO Auto-generated method stub
		FEED_BACK_TABLE feedBackTable = new FEED_BACK_TABLE(mDB);
		STAFF_TABLE staffTable = new STAFF_TABLE(mDB);
		ComboboxFollowProblemDTO model = new ComboboxFollowProblemDTO();
		model.listNVBH = staffTable.getStaffCodeComboboxProblemNVBHOfSuperVisor();
		model.listStatus = feedBackTable.getComboboxProblemStatusOfSuperVisor();
		return model;
	}

	/**
	 * getPlanTrainResultReportDTO
	 * 
	 * @author: HieuNQ
	 * @throws Exception
	 * @return:
	 * @throws:
	 */
	public GSNPPTrainingPlanDTO getGsnppTrainingPlan(long staffId, String shopId) {
		TRAINING_PLAN_DETAIL_TABLE table = new TRAINING_PLAN_DETAIL_TABLE(mDB);
		GSNPPTrainingPlanDTO dto = null;
		try {
			dto = table.getGsnppTrainingPlan(staffId, shopId);
		} catch (Exception e) {
		} finally {
		}
		return dto;
	}

	/**
	 * 
	 * lay thong tin danh gia cua NVBH
	 * 
	 * @author: HaiTC3
	 * @param ext
	 * @return
	 * @return: ReviewsStaffViewDTO
	 * @throws:
	 */
	public ReviewsStaffViewDTO getReviewsInfoOfStaff(Bundle ext) {
		ReviewsStaffViewDTO reviewsData = new ReviewsStaffViewDTO();
		FEED_BACK_TABLE feedBackTable = new FEED_BACK_TABLE(mDB);
		try {
			reviewsData = feedBackTable.getReviewStaffView(ext);
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			return null;
		}
		return reviewsData;
	}

	/**
	 * 
	 * cap nhat cac thong tin can bo xung cho training result va feedback (id va
	 * training_plan_detail_id)
	 * 
	 * @author: HaiTC3
	 * @param data
	 * @return
	 * @return: ReviewsStaffViewDTO
	 * @throws:
	 */
	public ReviewsStaffViewDTO updateIdForTrainingResult(String shopId, String supperStaffId, String staffId, ReviewsStaffViewDTO data) {
		TABLE_ID tableId = new TABLE_ID(mDB);

		if (data != null) {
			long feedBackMaxId = tableId.getMaxId(FEED_BACK_TABLE.FEED_BACK_TABLE);
			long feedBackDetailMaxId = tableId.getMaxId(FEEDBACK_DETAIL_TABLE.FEEDBACK_DETAIL_TABLE);
			try {
				// cap nhat thong tin feedback dto cho SKU vao bang FeedBack
				// table
				long feedbackSKUId = -1;
				if (data.feedBackSKU.feedBack != null) {
					if (data.feedBackSKU.currentStateOfFeedback == FeedBackDetailDTO.STATE_NEW_INSERT) {
						data.feedBackSKU.feedBack.feedBackId = feedBackMaxId;
						// data.feedBackSKU.feedBack.trainingPlanDetailId =
						// trainingPlanDetailId;
						feedbackSKUId = feedBackMaxId;
					} else if (data.feedBackSKU.currentStateOfFeedback == FeedBackDetailDTO.STATE_NEW_UPDATE) {
						feedbackSKUId = data.feedBackSKU.feedBack.feedBackId;
					}
				}
				if (feedbackSKUId > 0) {
					// cap nhat thong tin sku (feedback detail)
					for (int i = 0, size = data.listSKU.size(); i < size; i++) {
						// tao moi training result
						if (data.listSKU.get(i).feedbackDetailId == -1
								&& data.listSKU.get(i).currentState == FeedBackDetailDTO.STATE_NEW_INSERT) {
							data.listSKU.get(i).feedbackDetailId = feedBackDetailMaxId;
							data.listSKU.get(i).feedbackId = feedbackSKUId;
							feedBackDetailMaxId++;
						}
					}
				}

				feedBackMaxId++;
				// cap nhat thong tin danh gia (feedback)
				for (int i = 0, size = data.listReviewsObject.size(); i < size; i++) {
					// tao moi record training result
					if (data.listReviewsObject.get(i).currentStateOfFeedback == FeedBackDetailDTO.STATE_NEW_INSERT) {
						data.listReviewsObject.get(i).feedBack.feedBackId = feedBackMaxId;
						// data.listReviewsObject.get(i).feedBack.trainingPlanDetailId
						// = trainingPlanDetailId;
						feedBackMaxId++;
					}
				}
			} catch (Exception e) {
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));

			}
		}

		return data;
	}

	/**
	 * 
	 * update maxid for training shop manager result
	 * 
	 * @author: HaiTC3
	 * @param listTrainingReviews
	 * @return
	 * @return: List<TrainingShopManagerResultDTO>
	 * @throws:
	 */
	public List<FeedBackTBHVDTO> tbhvUpdateIdForTrainingResult(List<FeedBackTBHVDTO> listTrainingReviews) {
		TABLE_ID tableId = new TABLE_ID(mDB);
		long feedbackMaxId = tableId.getMaxId(FEED_BACK_TABLE.FEED_BACK_TABLE);

		for (int i = 0, size = listTrainingReviews.size(); i < size; i++) {
			if (listTrainingReviews.get(i).currentState == FeedBackTBHVDTO.STATE_NEW_INSERT) {
				listTrainingReviews.get(i).feedBackBasic.feedBackId = feedbackMaxId;
				feedbackMaxId++;
			}
		}
		return listTrainingReviews;
	}

	/**
	 * Lay thong tin album cua user
	 * 
	 * @author: QuangVT1
	 * @return
	 * @return: ListAlbumUserDTO
	 * @throws:
	 */
	public ListAlbumUserDTO getAlbumUserInfo(Bundle data) {
		MEDIA_ITEM_TABLE mediaItemTable = new MEDIA_ITEM_TABLE(mDB);
		String customerId = data.getString(IntentConstants.INTENT_CUSTOMER_ID);
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		String staffId = GlobalInfo.getInstance().getCurrentStaffId();

		// request lay danh sach hinh anh cua user
		ListAlbumUserDTO albumInfo = null;
		try {
			albumInfo = mediaItemTable.getAlbumUserInfo(customerId, shopId);

			if (albumInfo != null) {
				CUSTOMER_TABLE cusTable = new CUSTOMER_TABLE(mDB);
				albumInfo.shopDistance = cusTable.getDistanceOrder(customerId, shopId, staffId);
			}

			return albumInfo;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Mo ta chuc nang cua ham
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param customerId
	 * @param type
	 * @param numTop
	 * @return
	 * @return: ArrayList<PhotoDTO>
	 * @throws:
	 */
	public PhotoThumbnailListDto getAlbumDetailUser(String customerId, String type, String numTop, String page, String shopId, boolean isGetTotalImage) {
		MEDIA_ITEM_TABLE mediaItemTable = new MEDIA_ITEM_TABLE(mDB);
		PhotoThumbnailListDto dto = mediaItemTable.getAlbumDetailUser(customerId, type, numTop, page, shopId, isGetTotalImage);
		return dto;
	}

	/**
	 * Mo ta chuc nang cua ham
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param customerId
	 * @param type
	 * @param numTop
	 * @return
	 * @return: ArrayList<PhotoDTO>
	 * @throws:
	 */
	public ArrayList<PhotoDTO> getSupervisorAlbumDetailUser(String customerId, String type, String numTop, String page, String shopId) {
		MEDIA_ITEM_TABLE mediaItemTable = new MEDIA_ITEM_TABLE(mDB);
		ArrayList<PhotoDTO> listPhoto = mediaItemTable.getSupervisorAlbumDetailUser(customerId, type, numTop, page, shopId);
		return listPhoto;
	}

	/**
	 * get Staff Information
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws Exception
	 * @throws:
	 */
	public StaffInfoDTO getStaffInformation(String staffId, int page, int isGetTotalPage, int isLoadSaleInMonth)
			throws Exception {
		RPT_SALE_HISTORY rptSaleHistory = new RPT_SALE_HISTORY(mDB);
		StaffInfoDTO dto = rptSaleHistory.getStaffInformation(staffId, page, isGetTotalPage, isLoadSaleInMonth);
		return dto;
	}

	/**
	 * Cap nhat cac log co thoi gian khong hop le ve trang thai close
	 * 
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	public void updateLogWithStateClose(ArrayList<LogDTO> listLog) {
		if (mDB != null && mDB.isOpen()) {
			try {
				mDB.beginTransaction();
				LOG_TABLE log = new LOG_TABLE(mDB);
				SALE_ORDER_TABLE orderTable = new SALE_ORDER_TABLE(mDB);
				for (int i = 0, size = listLog.size(); i < size; i++) {
					LogDTO dto = (LogDTO) listLog.get(i);
					log.updateLogWithStateClose(dto);
					// neu la don hang thi cap nhat trang thai don hang la co
					// thoi gian khong hop le
					if (dto.tableType == LogDTO.TYPE_ORDER
							&& !StringUtil.isNullOrEmpty(dto.tableId)) {
						orderTable.updateState(dto.tableId, Integer.parseInt(LogDTO.STATE_INVALID_TIME));
					}
				}
				mDB.setTransactionSuccessful();
			} catch (Exception e) {
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			} finally {
				if (mDB != null && mDB.inTransaction()) {
					mDB.endTransaction();
				}
			}
		}
	}

	/**
	 * 
	 * cap nhat thong tin danh gia NVBH toi DB Note: Nhung ham insert, delete,
	 * update da co transaction
	 * 
	 * @author: HaiTC3
	 * @param reviewsInfoDTO
	 * @return
	 * @return: int
	 * @throws:
	 */
	public int insertAndUpdateReviewsStaffDTO(ReviewsStaffViewDTO reviewsInfoDTO) {
		int kq = 0;
		try {
			mDB.beginTransaction();
			FEED_BACK_TABLE feedback = new FEED_BACK_TABLE(mDB);
			boolean checkSuccess = true;
			// insert feedback of SKU to DB
			if (reviewsInfoDTO.feedBackSKU != null) {
				if (reviewsInfoDTO.feedBackSKU.currentStateOfFeedback == FeedBackDetailDTO.STATE_NEW_INSERT) {
					// tao moi feedback SKU trong feedback
					checkSuccess = this.insertDTO(reviewsInfoDTO.feedBackSKU.feedBack) >= 0 ? true
							: false;
				} else if (reviewsInfoDTO.feedBackSKU.currentStateOfFeedback == FeedBackDetailDTO.STATE_NEW_UPDATE) {
					// update feedback SKU trong feedback neu co it nhat 1 sku
					// trong feedback detail
					checkSuccess = feedback.updateContentFeedBack(reviewsInfoDTO.feedBackSKU.feedBack) > 0 ? true
							: false;
				} else if (reviewsInfoDTO.feedBackSKU.currentStateOfFeedback == FeedBackDetailDTO.STATE_DELETED) {
					// bo xung xoa di feedback SKU trong feedback neu nhu ko con
					// SKU nao
					checkSuccess = this.deleteDTO(reviewsInfoDTO.feedBackSKU.feedBack) > 0 ? true
							: false;
				}
			}
			if (checkSuccess) {
				// insert list SKU to DB
				for (int i = 0, size = reviewsInfoDTO.listSKU.size(); i < size; i++) {
					FeedBackDetailDTO feedbackInfo = reviewsInfoDTO.listSKU.get(i);
					// chi them moi cac recode, nhung record da co thi khong cap
					// nhat gi ca
					if (feedbackInfo.currentState == FeedBackDetailDTO.STATE_NEW_INSERT) {
						checkSuccess = this.insertDTO(feedbackInfo) >= 0 ? true
								: false;
					} else if (feedbackInfo.currentState == FeedBackDetailDTO.STATE_DELETED) {
						// xoa record ra khoi bang feeback detail neu yeu cau
						// xoa
						// SKU nay
						checkSuccess = this.deleteDTO(feedbackInfo) > 0 ? true
								: false;
					}
				}
			}

			if (checkSuccess) {
				// insert reviews info to DB (3 loai feedback co type = 6-7-8)
				for (int i = 0, size = reviewsInfoDTO.listReviewsObject.size(); i < size; i++) {
					ReviewsObjectDTO reviewDTO = reviewsInfoDTO.listReviewsObject.get(i);
					// them moi cac record tao moi
					if (reviewDTO.currentStateOfFeedback == FeedBackDetailDTO.STATE_NEW_INSERT) {
						checkSuccess = this.insertDTO(reviewDTO.feedBack) >= 0 ? true
								: false;
						if (!checkSuccess) {
							break;
						}
					}
					// nhung record da co thi chi bo xung noi dung trong
					// feedback
					else if (reviewDTO.currentStateOfFeedback == FeedBackDetailDTO.STATE_NEW_UPDATE) {
						checkSuccess = feedback.updateContentFeedBack(reviewDTO.feedBack) > 0 ? true
								: false;
						if (!checkSuccess) {
							break;
						}
					} else if (reviewDTO.currentStateOfFeedback == FeedBackDetailDTO.STATE_DELETED) {
						checkSuccess = this.deleteDTO(reviewDTO.feedBack) > 0 ? true
								: false;
						if (!checkSuccess) {
							break;
						}
					}
				}
			}
			if (checkSuccess) {
				kq = 1;
				mDB.setTransactionSuccessful();
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			kq = 0;
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}
		return kq;
	}

	/**
	 * 
	 * insert and update training reviews info to db local
	 * 
	 * @author: HaiTC3
	 * @param listTrainingReviews
	 * @return
	 * @return: int
	 * @throws:
	 */
	public int tbhvInsertAndUpdateReviewsStaffDTO(List<FeedBackTBHVDTO> listTrainingReviews) {
		int kq = 0;
		try {
			mDB.beginTransaction();
			boolean checkSuccess = true;
			FEED_BACK_TABLE tableTraining = new FEED_BACK_TABLE(mDB);
			for (int i = 0, size = listTrainingReviews.size(); i < size; i++) {
				FeedBackTBHVDTO feedBackInfo = listTrainingReviews.get(i);
				// them moi, delete, update record , nhung record khong thay doi
				// thi khong lam gi ca
				if (feedBackInfo.currentState == TrainingShopManagerResultDTO.STATE_NEW_INSERT) {
					checkSuccess = this.insertDTO(feedBackInfo.feedBackBasic) >= 0 ? true
							: false;
					if (!checkSuccess) {
						break;
					}
				} else if (feedBackInfo.currentState == TrainingShopManagerResultDTO.STATE_DELETED) {
					checkSuccess = this.deleteDTO(feedBackInfo.feedBackBasic) > 0 ? true
							: false;
					if (!checkSuccess) {
						break;
					}
				} else if (feedBackInfo.currentState == TrainingShopManagerResultDTO.STATE_NEW_UPDATE) {
					checkSuccess = tableTraining.update(feedBackInfo.feedBackBasic) > 0 ? true
							: false;
					if (!checkSuccess) {
						break;
					}
				}
			}
			if (checkSuccess) {
				kq = 1;
				mDB.setTransactionSuccessful();
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			kq = 0;
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}
		return kq;
	}

	/**
	 * 
	 * cap nhat maxId vao bang table_id
	 * 
	 * @author: HaiTC3
	 * @param maxFeedbackId
	 * @param maxTrainingResultId
	 * @return: void
	 * @throws:
	 */
	public void updateMaxIdForFeedBackAndTrainingResult(long maxFeedbackId, long maxTrainingResultId) {
		try {
			mDB.beginTransaction();
			TABLE_ID tableId = new TABLE_ID(mDB);
			tableId.updateMaxId(FEED_BACK_TABLE.FEED_BACK_TABLE, maxFeedbackId);
			tableId.updateMaxId(TRAINING_RESULT_TABLE.TABLE_NAME, maxTrainingResultId);
			mDB.setTransactionSuccessful();
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}
	}

	public ActionLogDTO checkVisitFromActionLog(long staffId) {
		ACTION_LOG_TABLE table = new ACTION_LOG_TABLE(mDB);
		ActionLogDTO dto = table.checkVisitFromActionLog(staffId);
		return dto;
	}

	/**
	 * 
	 * cap nhat van de cua nhan vien ban hang (thuc hien boi GSNPP)
	 * 
	 * @author: ThanhNN8
	 * @param dto
	 * @return
	 * @return: long
	 * @throws:
	 */
	public long updateGSNPPFollowProblemDone(FollowProblemItemDTO dto) {
		long returnCode = -1;
		if (dto != null) {
			mDB.beginTransaction();
			try {
				FEED_BACK_TABLE feedbackTable = new FEED_BACK_TABLE(mDB);
				returnCode = feedbackTable.updateGSNPPFollowProblemDone(dto);
				mDB.setTransactionSuccessful();
			} finally {
				if (mDB != null && mDB.inTransaction()) {
					mDB.endTransaction();
				}
			}
		}
		return returnCode;
	}

	/**
	 * 
	 * Cap nhat van de cua GSNPP/TTTT (thuc hien boi GST)
	 * 
	 * @author: YenNTH
	 * @param dto
	 * @return
	 * @return: long
	 * @throws:
	 */
	public long updateTBHVFollowProblemDone(TBHVFollowProblemItemDTO dto) {
		long returnCode = -1;
		if (dto != null) {
			try {
				mDB.beginTransaction();
				FEED_BACK_TABLE feedbackTable = new FEED_BACK_TABLE(mDB);
				returnCode = feedbackTable.updateTBHVFollowProblemDone(dto);
				mDB.setTransactionSuccessful();
			} finally {
				if (mDB != null && mDB.inTransaction()) {
					mDB.endTransaction();
				}
			}
		}
		return returnCode;
	}

	/**
	 * HieuNH delete van de cua TBHV
	 * 
	 * @param dto
	 * @return
	 */
	public long deleteTBHVFollowProblemDone(TBHVFollowProblemItemDTO dto) {
		long returnCode = -1;
		if (dto != null) {
			try {
				mDB.beginTransaction();
				FEED_BACK_TABLE feedbackTable = new FEED_BACK_TABLE(mDB);
				returnCode = feedbackTable.deleteTBHVFollowProblemDone(dto);
				mDB.setTransactionSuccessful();
			} finally {
				if (mDB != null && mDB.inTransaction()) {
					mDB.endTransaction();
				}
			}
		}
		return returnCode;
	}

	/**
	 * 
	 * lay d/s san pham de them vao danh gia NVBH
	 * 
	 * @author: HaiTC3
	 * @param ext
	 * @return
	 * @return: ListFindProductSaleOrderDetailViewDTO
	 * @throws:
	 */
	public ListFindProductSaleOrderDetailViewDTO getListProductToAddReviewsStaff(Bundle ext) {
		SALES_ORDER_DETAIL_TABLE saleOrderDetailTable = new SALES_ORDER_DETAIL_TABLE(mDB);
		try {
			ListFindProductSaleOrderDetailViewDTO vt = saleOrderDetailTable.getListProductToAddReviewsStaff(ext);
			return vt;

		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));

			return null;
		}
	}

	/**
	 * 
	 * lay thong tin bao cao cham cong ngay cua TBHV
	 * 
	 * @param ext
	 * @return
	 * @return: ArrayList<ReportTakeAttendOfDateDTO>
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 22, 2013
	 */
	public TBHVAttendanceDTO getTBHVAttendance(Bundle ext) {
		STAFF_POSITION_LOG_TABLE staffPosition = new STAFF_POSITION_LOG_TABLE(mDB);
		try {
			TBHVAttendanceDTO dto = staffPosition.getTBHVAttendance(ext);
			return dto;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Request upload photo
	 * 
	 * @author: PhucNT
	 * @param mediaDTO
	 * @return
	 * @return: boolean
	 * @throws:
	 */
	public long insertMediaItem(MediaItemDTO mediaDTO) {
		long result = -1;
		try {
			mDB.beginTransaction();
			// <HaiTC> - update use new resolution get maxid
			// TABLE_ID tableId = new TABLE_ID(SQLUtils.getInstance().getmDB());
			// // test
			// long offValue =
			// GlobalInfo.getInstance().getProfile().getUserData().id
			// * GlobalInfo.getInstance().getFactorDefault();
			// // cap nhat id order
			// long cshId = tableId
			// .getMaxIdFromTableName(MEDIA_ITEM_TABLE.TABLE_MEDIA_ITEM) + 1;
			// MEDIA_ITEM_TABLE table = new MEDIA_ITEM_TABLE(mDB);
			// mediaDTO.id = offValue + cshId;
			// result = mediaDTO.id;
			// result = table.insert(mediaDTO);
			//
			// tableId.updateMaxId(MEDIA_ITEM_TABLE.TABLE_MEDIA_ITEM, cshId);

			TABLE_ID tableId = new TABLE_ID(SQLUtils.getInstance().getmDB());
			// cap nhat id order
			long cshId = tableId.getMaxId(MEDIA_ITEM_TABLE.TABLE_MEDIA_ITEM);
			MEDIA_ITEM_TABLE table = new MEDIA_ITEM_TABLE(mDB);
			mediaDTO.id = cshId;
			result = mediaDTO.id;
			result = table.insert(mediaDTO);
			// </HaiTC> - end

			mDB.setTransactionSuccessful();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			result = -1;
			ServerLogger.sendLog(e.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}
		return result;
	}

	/**
	 * cap nhat du lieu o db local
	 * 
	 * @author: PhucNT
	 * @return: void
	 * @throws:
	 */
	public ArrayList<CustomerStockHistoryDTO> saveRemainProduct(ArrayList<RemainProductViewDTO> listRemain) {
		// TODO Auto-generated method stub
		MeasuringTime.getStartTimeParser();
		ArrayList<CustomerStockHistoryDTO> listCSTDTO = new ArrayList<CustomerStockHistoryDTO>();
		try {
			mDB.beginTransaction();
			TABLE_ID tableId = new TABLE_ID(SQLUtils.getInstance().getmDB());
			long cshId = tableId.getMaxId(CUSTOMER_STOCK_HISTORY_TABLE.TABLE_NAME);

			for (int i = 0; i < listRemain.size(); i++) {
				CustomerStockHistoryDTO cus = new CustomerStockHistoryDTO();
				cus.convertFromRemainProductDTO(listRemain.get(i));
				cus.customerStockHistoryId = cshId + i;
				cus.createDate = DateUtils.now();
				listCSTDTO.add(cus);
			}
			boolean checkSuccess = false;
			checkSuccess = updateNumberRemainProduct(listCSTDTO);
			if (checkSuccess) {
				// truong hop insert/update thanh cong
				mDB.setTransactionSuccessful();
			} else {
				//
				listCSTDTO = new ArrayList<CustomerStockHistoryDTO>();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			listCSTDTO = null;
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}
		MeasuringTime.getEndTimeParser();
		VTLog.i("INSERT", "Insert voi thuong: " + MeasuringTime.getTimeParser());
		return listCSTDTO;

	}

	/**
	 * lay thong tin chi tiet san pham
	 * 
	 * @author: ThanhNN8
	 * @param viewData
	 * @return
	 * @return: ProductDTO
	 * @throws:
	 */
	public ProductDTO getProductInfoDetail(Bundle viewData) {
		// TODO Auto-generated method stub
		PRODUCT_TABLE productTable = new PRODUCT_TABLE(mDB);
		try {
			String productId = viewData.getString(IntentConstants.INTENT_PRODUCT_ID);
			ProductDTO dto = productTable.getProductInfoDetail(productId);
			return dto;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	/**
	 * Request upload photo
	 * 
	 * @author: PhucNT
	 * @param mediaDTO
	 * @return
	 * @return: long
	 * @throws:
	 */
	public int updateNewLinkPhoto(MediaItemDTO mediaDTO) {
		// TODO Auto-generated method stub
		int result = -1;
		if (mDB != null && mDB.isOpen()) {
			try {
				mDB.beginTransaction();

				MEDIA_ITEM_TABLE table = new MEDIA_ITEM_TABLE(mDB);
				result = table.updateNewLink(mediaDTO);

				mDB.setTransactionSuccessful();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				result = -1;
			} finally {
				if (mDB != null && mDB.inTransaction()) {
					mDB.endTransaction();
				}
			}
		}
		return result;
	}

	public WrongPlanCustomerDTO getWrongCustomerList(long staffId) {
		WrongPlanCustomerDTO dto = null;
		if (mDB != null && mDB.isOpen()) {
			try {
				CUSTOMER_TABLE table = new CUSTOMER_TABLE(mDB);
				dto = table.getWrongCustomerList(staffId);
			} catch (Exception ex) {
			}
		}
		return dto;
	}

	/**
	 * generate sql delete order
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param dto
	 * @return
	 * @return: JSONArray
	 * @throws:
	 */

	public JSONArray generateSqlDeleteOrder(SaleOrderViewDTO dto, List<SaleOrderDetailDTO> listSaleOrderDetail, ActionLogDTO actionLogDTO) {
		// tao cau sql de xoa sale order tren server
		JSONArray listSql = new JSONArray();
		try {
			listSql = dto.generateDeleteSaleOrderSql();

			// Sql increase stock total //Khong cap nhat ton kho nua -> de thuc
			// hien chuc nang hien thi mau` ton kho
			// for (int i = 0, size = listSaleOrderDetail.size(); i < size; i++)
			// {
			// StockTotalDTO stockTotalDTO = new StockTotalDTO();
			// stockTotalDTO.ownerId = dto.saleOrder.shopId;
			// stockTotalDTO.ownerType = 1;
			// stockTotalDTO.productId = listSaleOrderDetail.get(i).productId;
			// stockTotalDTO.quantity = listSaleOrderDetail.get(i).quantity;
			// stockTotalDTO.availableQuantity =
			// listSaleOrderDetail.get(i).quantity;
			//
			// listSql.put(stockTotalDTO.generateIncreaseSql());
			// }

			if (dto.isFinalOrder == 1) {
				// delete record cuoi cung thi cap nhat last_order
				CustomerDTO cus = new CustomerDTO();
				cus.customerId = dto.getCustomerId();
				// cus.setLastOrder(null);
				listSql.put(cus.generateUpdateLastOrder(dto.lastOrder));

				// cap nhat staff customer
				StaffCustomerDTO staffDto = new StaffCustomerDTO();
				staffDto.customerId = dto.customer.customerId;
				staffDto.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
				listSql.put(staffDto.generateUpdateFromOrderSql());

				// cap nhat trong visit plan
				// VisitPlanDTO visitPlanDTO = new VisitPlanDTO();
				// visitPlanDTO.active = 1;
				// visitPlanDTO.shopId = dto.saleOrder.shopId;
				// visitPlanDTO.staffId = dto.saleOrder.staffId;
				// visitPlanDTO.customerId = dto.saleOrder.customerId;
				// visitPlanDTO.lastOrder = dto.saleOrder.orderDate;
				//
				// listSql.put(visitPlanDTO.generateUpdateLastOrder(dto.lastOrder));
			}

			listSql.put(actionLogDTO.generateDeleteActionWhenDeleteOrder());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

		return listSql;
	}

	/**
	 * Xoa nhung log cu
	 * 
	 * @author banghn
	 * @return
	 */
	public long deleteOldLogTable() {
		long returnCode = 1;
		if (mDB != null && mDB.isOpen()) {
			try {
				// delete log table truoc 15 ngay
				LOG_TABLE logTable = new LOG_TABLE(mDB);
				if (logTable.deleteOldLog() < 0) {
					returnCode = -1;
				}
				// delete action log truoc 3 ngay
				ACTION_LOG_TABLE actionLog = new ACTION_LOG_TABLE(mDB);
				if (actionLog.deleteOldActionLog() < 0) {
					returnCode = -1;
				}

				// delete sale_in_month (giu lai 3 thang)
				String startOfMonth = DateUtils.getFirstDateOfNumberPreviousMonthWithFormat(
						DateUtils.DATE_FORMAT_SQL_DEFAULT, -3);
				String[] params = {startOfMonth};
				StringBuffer sql = new StringBuffer();
				sql.append("DELETE FROM rpt_sale_in_month ");
				sql.append("WHERE  ? > substr(month,0,11) ");
				mDB.execSQL(sql.toString(), params);
				
				// xoa nhung feeback sai
				sql = new StringBuffer();
				sql.append("DELETE FROM FEEDBACK ");
				sql.append("WHERE  CREATE_USER_ID <= 0 ");
				mDB.execSQL(sql.toString());
				
				// delete position-log
				STAFF_POSITION_LOG_TABLE positionLog = new STAFF_POSITION_LOG_TABLE(
						mDB);
				returnCode = positionLog.deleteOldPositionLog();
				//delete tablet action log
//				TABLET_ACTION_LOG_TABLE tabletLog = new TABLET_ACTION_LOG_TABLE(mDB);
//				tabletLog.deleteOldTabletActionLog();

				// delete SO & SOD
				SALES_ORDER_DETAIL_TABLE sod = new SALES_ORDER_DETAIL_TABLE(mDB);
				if (sod.deleteOldSaleOrderDetail() < 0) {
					returnCode = -1;
				}
				SALE_ORDER_TABLE so = new SALE_ORDER_TABLE(mDB);
				if (so.deleteOldSaleOrder() < 0) {
					returnCode = -1;
				}

				//xoa du lieu media cu hon 2 thang, giu lai hinh anh san pham
				MEDIA_ITEM_TABLE mediaItem = new MEDIA_ITEM_TABLE(mDB);
				returnCode = mediaItem.deleteOldMediaItem();
				
			} catch (Throwable e) {
				returnCode = -1;
			}
		}
		return returnCode;
	}

	/**
	 * Lay ds don hang co trong log
	 * 
	 * @author: TruongHN
	 * @return: ArrayList<String>
	 * @throws:
	 */
	public ArrayList<LogDTO> getOrderInLog() {
		ArrayList<LogDTO> listOrder = new ArrayList<LogDTO>();
		try {
			if (mDB != null && mDB.isOpen()) {
				// lay ds don hang co trong log
				LOG_TABLE logTable = new LOG_TABLE(mDB);
				listOrder = logTable.getOrderInLog();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return listOrder;
	}

	/**
	 * Update log va sale order
	 * 
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	public void updateLogWithOrder(LogDTO log, String saleOrderId) {
		if (mDB != null && log != null && mDB.isOpen() && !mDB.isReadOnly()) {
			try {
				isProcessingTrans = true;
				mDB.beginTransaction();
				// update log
				updateDTO(log);
				// update saleOrder
				SALE_ORDER_TABLE saleOrderTable = new SALE_ORDER_TABLE(mDB);
				saleOrderTable.updateState(saleOrderId, Integer.parseInt(log.state));
				mDB.setTransactionSuccessful();
			} finally {
				if (mDB != null && mDB.inTransaction()) {
					mDB.endTransaction();
				}
				isProcessingTrans = false;
			}
			if (GlobalInfo.getInstance().isExitApp) {
				closeDB();
			}
		}

	}

	/**
	 * Lay ds don hang chua send thanh cong
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	public NoSuccessSaleOrderDto getNoSuccessOrderList(ArrayList<String> idList) {
		NoSuccessSaleOrderDto dto = null;
		if (mDB != null && mDB.isOpen()) {
			try {
				SALE_ORDER_TABLE saleOrderTable = new SALE_ORDER_TABLE(mDB);
				dto = saleOrderTable.getNoSuccessOrderList(idList);
			} catch (Exception e) {
			}
		}
		return dto;
	}

	/**
	 * Lay lich su mua hang cua nhan vien ban hang ban cho khach hang
	 * 
	 * @author: HieuNH
	 *            , customerCode
	 * @return: void
	 * @throws:
	 */
	public Vector<HistoryItemDTO> getListHistory(String staffId, String customerId, String productId) {
		RPT_STAFF_SALE_TABLE table = new RPT_STAFF_SALE_TABLE(mDB);
		return table.getListHistory(staffId, customerId, productId);
	}

	/**
	 * Lay danh sach NPP
	 * 
	 * @author: HieuNH
	 * @return: void
	 * @throws:
	 */
	public ArrayList<ShopDTO> getListNPP(String shopId) {
		RPT_STAFF_SALE_TABLE table = new RPT_STAFF_SALE_TABLE(mDB);
		return table.getListNPP(shopId);
	}

	/**
	 * Lay danh sach NVGS report date
	 * 
	 * @author: HieuNH
	 * @param shopId
	 * @return: void
	 * @throws:
	 */
	public ListStaffDTO getListNVGSOfTBHVReportDate(String shopId) {
		RPT_STAFF_SALE_TABLE table = new RPT_STAFF_SALE_TABLE(mDB);
		return table.getListNVGSOfTBHVReportDate(shopId);
	}

	/**
	 * Lay danh sach NVGS
	 * 
	 * @author: HieuNH
	 * @param shopId
	 * @return: void
	 * @throws:
	 */
	public ListStaffDTO getListNVGSOfTBHVReportPSDS(String shopId) {
		RPT_STAFF_SALE_TABLE table = new RPT_STAFF_SALE_TABLE(mDB);
		return table.getListNVGSOfTBHVReportPSDS(shopId);
	}

	/**
	 * kiem tra db co phai cua user dang dang nhap hay kg
	 * 
	 * @author: TamPQ
	 * @param staffCode
	 *            , customerCode
	 * @return: void
	 * @throws:
	 */
	public boolean checkUserDB(String staffCode, int roleType) {
		STAFF_TABLE table = new STAFF_TABLE(mDB);
		return table.checkUserDB(staffCode, roleType);
	}

	/**
	 * 
	 * get report progress in month of TBHV
	 * 
	 * @author: HaiTC3
	 * @param ext
	 * @return: ReportProgressMonthViewDTO
	 * @throws:
	 */
	public ReportProgressMonthViewDTO getReportProgressInMonthTBHV(Bundle ext) {
		ReportProgressMonthViewDTO reviewsData = new ReportProgressMonthViewDTO();
		RPT_STAFF_SALE_TABLE reportStaffSale = new RPT_STAFF_SALE_TABLE(mDB);
		try {
			reviewsData = reportStaffSale.getReportProgressInMonthOfTBHV(ext);
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			return null;
		}
		return reviewsData;
	}

	/**
	 * 
	 * get report progress in month of TBHV in detail NPP screen
	 * 
	 * @author: HaiTC3
	 * @param ext
	 * @return
	 * @return: ReportProgressMonthViewDTO
	 * @throws:
	 */
	public ReportProgressMonthViewDTO getReportProgressInMonthTBHVDetailNPP(Bundle ext) {
		ReportProgressMonthViewDTO reviewsData = new ReportProgressMonthViewDTO();
		RPT_STAFF_SALE_TABLE reportStaffSale = new RPT_STAFF_SALE_TABLE(mDB);
		try {
			reviewsData = reportStaffSale.getReportProgressInMonthOfTBHVDetailNPP(ext);
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			return null;
		}
		return reviewsData;
	}

	/**
	 * 
	 * get report progress TBHV sales focus info
	 * 
	 * @author: HaiTC3
	 * @param ext
	 * @return
	 * @return: ReportProgressMonthViewDTO
	 * @throws:
	 */
	public TBHVProgressReportSalesFocusViewDTO getReportProgressTBHVSalesFocusInfo(Bundle ext) {
		TBHVProgressReportSalesFocusViewDTO reviewsData = new TBHVProgressReportSalesFocusViewDTO();
		RPT_STAFF_SALE_TABLE reportStaffSale = new RPT_STAFF_SALE_TABLE(mDB);
		try {
			reviewsData = reportStaffSale.getReportProgressSalesFocusInfo(ext);
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			return null;
		}
		return reviewsData;
	}

	/**
	 * 
	 * get report progess tbhv sales focus detail info
	 * 
	 * @author: HaiTC3
	 * @param ext
	 * @return
	 * @return: TBHVProgressReportSalesFocusViewDTO
	 * @throws:
	 */
	public TBHVProgressReportSalesFocusViewDTO getReportProgressTBHVSalesFocusDetailInfo(Bundle ext) {
		TBHVProgressReportSalesFocusViewDTO reviewsData = new TBHVProgressReportSalesFocusViewDTO();
		RPT_STAFF_SALE_TABLE reportStaffSale = new RPT_STAFF_SALE_TABLE(mDB);
		try {
			reviewsData = reportStaffSale.getReportProgressSalesFocusDetailInfo(ext);
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			return null;
		}
		return reviewsData;
	}

	/**
	 * 
	 * 
	 * @author: HaiTC3
	 * @param ext
	 * @return
	 * @return: List<TrainingShopManagerResultDTO>
	 * @throws:
	 */
	public List<FeedBackTBHVDTO> getTrainingReviewsGSNPPOfTBHVInfo(Bundle ext) {
		List<FeedBackTBHVDTO> listTrainingReviewsTBHV = new ArrayList<FeedBackTBHVDTO>();
		FEED_BACK_TABLE trainingReviewsTable = new FEED_BACK_TABLE(mDB);
		try {
			listTrainingReviewsTBHV = trainingReviewsTable.getListReviewsOfTBHV(ext);
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			return null;
		}
		return listTrainingReviewsTBHV;
	}

	/**
	 * getTBHVListRouteSupervision
	 * 
	 * @author: TamPQ
	 * @return: TBHVRouteSupervisionDTO
	 * @throws:
	 */
	public TBHVRouteSupervisionDTO getTBHVListRouteSupervision(long staff_id, String shopId, int day) {
		STAFF_TABLE table = new STAFF_TABLE(mDB);
		return table.getTbhvRouteSupervision(staff_id, shopId, day);
	}

	/**
	 * Get danh sach khach hang cua nhan vien ban hang trong tuyen
	 * 
	 * @author banghn
	 * @return
	 */
	public CustomerListDTO requestGetCustomerSaleList(String ownerId, String shopId, String staffId, String code, String nameAddress, String visitPlan, int page) {
		CustomerListDTO dto = null;
		CUSTOMER_TABLE custommerTable = new CUSTOMER_TABLE(mDB);
		try {
			dto = custommerTable.requestGetCustomerSaleList(ownerId, shopId, staffId, code, nameAddress, visitPlan, page);
		} catch (Exception e) {
		} finally {
		}
		return dto;
	}

	/**
	 * get Gsnpp Training Plan
	 * 
	 * @author: TamPQ
	 * @return: TBHVGsnppTrainingPlanDTO
	 * @throws:
	 */
	public TBHVTrainingPlanDTO getTbhvTrainingPlan(long staffId, long shopId) {
		TRAINING_PLAN_DETAIL_TABLE table = new TRAINING_PLAN_DETAIL_TABLE(mDB);
		return table.getTbhvTrainingPlan(staffId, shopId);
	}

	/**
	 * Lay danh sach lich su cap nhat vi tri khach hang
	 * 
	 * @author banghn
	 * @param customerId
	 * @return
	 */
	public ArrayList<CustomerUpdateLocationDTO> getCustomerHistoryUpdateLocation(String customerId) {
		ArrayList<CustomerUpdateLocationDTO> listHistory = null;
		CUSTOMER_POSITION_LOG_TABLE logPosition = new CUSTOMER_POSITION_LOG_TABLE(mDB);
		try {
			listHistory = logPosition.getCustomerHistoryUpdateLocation(customerId);
		} catch (Exception e) {
		} finally {
		}
		return listHistory;
	}

	/**
	 * Lay ds vi tri NVBH cua NVGS
	 * 
	 * @author: TruongHN
	 * @throws Exception
	 */
	public GsnppRouteSupervisionDTO getListPositionSalePersons(long staffId, String shopId) {
		GsnppRouteSupervisionDTO dto = null;
		try {
			STAFF_TABLE staffTable = new STAFF_TABLE(mDB);
			dto = staffTable.getListPositionSalePersons(staffId, shopId);
		} catch (Exception ex) {
		}
		return dto;
	}

	/**
	 * getDayTrainingSupervision
	 * 
	 * @author: TamPQ
	 * @return: TBHVDayTrainingSupervisionDTO
	 * @throws:
	 */
	public TBHVTrainingPlanDayResultReportDTO getDayTrainingSupervision(long trainDetailId, long shopId) {
		return new RPT_SALE_RESULT_DETAIL_TABLE(mDB).getDayTrainingSupervision(trainDetailId, shopId);
	}

	/**
	 * getHistoryPlanTraining
	 * 
	 * @author: TamPQ
	 * @return: TBHVDayTrainingSupervisionDTO
	 * @throws:
	 */
	public TBHVTrainingPlanHistoryAccDTO getPlanTrainingHistoryAcc(long staffId, long gsnppStaffId, String shopId) {
		return new TRAINING_PLAN_DETAIL_TABLE(mDB).getPlanTrainingHistoryAcc(staffId, gsnppStaffId, shopId);
	}

	/**
	 * Lay ds don hang loi: bao gom don hang chua goi, don hang loi va don hang
	 * tra ve
	 * 
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	public NoSuccessSaleOrderDto getAllOrderFail() {
		NoSuccessSaleOrderDto dto = null;
		if (mDB != null && mDB.isOpen()) {
			try {
				SALE_ORDER_TABLE saleOrderTable = new SALE_ORDER_TABLE(mDB);
				dto = saleOrderTable.getAllOrderFail(GlobalInfo.getInstance().getProfile().getUserData().id);
			} catch (Exception ex) {
				// mDB.endTransaction();
			}
		}
		return dto;
	}

	/**
	 * Lay thong tin don hang can canh bao
	 * 
	 * @author: TruongHN
	 * @return: NotifyOrderDTO
	 * @throws:
	 */
	public NotifyOrderDTO getOrderNeedNotify() {
		NotifyOrderDTO notifyDTO = new NotifyOrderDTO();
		try {
			if (mDB != null && mDB.isOpen()) {
				// lay ds don hang co trong log
				LOG_TABLE logTable = new LOG_TABLE(mDB);
				notifyDTO.listOrderInLog = logTable.getOrderInLog();
				// lay so luong don hang tra ve trong ngay tu NPP
				SALE_ORDER_TABLE orderTable = new SALE_ORDER_TABLE(mDB);
				notifyDTO.numOrderReturnNPP = orderTable.getOrderReturnNPPInDay(GlobalInfo.getInstance().getProfile().getUserData().id);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return notifyDTO;
	}

	/**
	 * update ExeptionOrderDate
	 * 
	 * @author: TamPQ
	 * @param dto
	 * @return
	 * @return: longvoid
	 * @throws:
	 */
	public long updateExceptionOrderDate(StaffCustomerDTO dto) {
		long returnCode = -1;
		if (dto != null) {
			mDB.beginTransaction();
			try {
				STAFF_CUSTOMER_TABLE staffCusTable = new STAFF_CUSTOMER_TABLE(mDB);
				returnCode = staffCusTable.insertOrUpdate(dto);
				if(returnCode > 0
						&& GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSNPP){
					STAFF_CUSTOMER_DETAIL_TABLE staffCustomerDetailTable = new STAFF_CUSTOMER_DETAIL_TABLE(mDB);
					returnCode = staffCustomerDetailTable.insertOrUpdate(dto);
				}
				mDB.setTransactionSuccessful();
			} finally {
				if (mDB != null && mDB.inTransaction()) {
					mDB.endTransaction();
				}
			}
		}
		return returnCode;
	}

	/**
	 * 
	 * get list industry product and list product
	 * 
	 * @param ext
	 * @return
	 * @return: SaleStatisticsProductInDayInfoViewDTO
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 20, 2012
	 */
	public SaleStatisticsProductInDayInfoViewDTO getListIndustryProductAndListProduct(Bundle ext) {
		SaleStatisticsProductInDayInfoViewDTO result = new SaleStatisticsProductInDayInfoViewDTO();
		PRODUCT_TABLE productTable = new PRODUCT_TABLE(mDB);
		try {
			result = productTable.getListIndustryProductAndListProduct(ext);
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			return null;
		}
		return result;
	}

	/**
	 * 
	 * get list product pre sale sold
	 * 
	 * @param ext
	 * @return
	 * @return: ArrayList<SaleProductInfoDTO>
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 20, 2012
	 */
	public ArrayList<SaleProductInfoDTO> getListProductPreSaleSold(Bundle ext) {
		ArrayList<SaleProductInfoDTO> result = new ArrayList<SaleProductInfoDTO>();
		PRODUCT_TABLE productTable = new PRODUCT_TABLE(mDB);
		try {
			result = productTable.getListProductPreSaleSold(ext);
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			return null;
		}
		return result;
	}

	/**
	 * 
	 * get list product van sale sold
	 * 
	 * @param ext
	 * @return
	 * @return: ArrayList<SaleProductInfoDTO>
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 17, 2013
	 */
	public ArrayList<SaleProductInfoDTO> getListProductVanSaleSold(Bundle ext) {
		ArrayList<SaleProductInfoDTO> result = new ArrayList<SaleProductInfoDTO>();
		PRODUCT_TABLE productTable = new PRODUCT_TABLE(mDB);
		try {
			result = productTable.getListProductVanSaleSold(ext);
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			return null;
		}
		return result;
	}

	public ArrayList<String> getSaleStatisticsAccumulateDay(String shopId, String staffId, String productCode, String productName, String industry, String page) {
		PRODUCT_TABLE table = new PRODUCT_TABLE(mDB);
		return table.getSaleStatisticsAccumulateDay(shopId, staffId, productCode, productName, industry, page);
	}

	public SaleStatisticsAccumulateDayDTO getSaleStatisticsAccumulateDayListProduct(String shopId, String staffId, String productCode, String productName, String industry, String page) {
		PRODUCT_TABLE table = new PRODUCT_TABLE(mDB);
		return table.getSaleStatisticsAccumulateDayListProduct(shopId, staffId, productCode, productName, industry, page);
	}

	public int getCountSaleStatisticsAccumulateDayListProduct(String shopId, String staffId, String productCode, String productName, String industry) {
		PRODUCT_TABLE table = new PRODUCT_TABLE(mDB);
		return table.getCountSaleStatisticsAccumulateDayListProduct(shopId, staffId, productCode, productName, industry);
	}

	/**
	 * Lay thong tin so luong ctkm & cttb dang chay
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param shopId
	 * @return
	 * @return: Bundle
	 * @throws:
	 */

	public Bundle getBusinessSupportProgrameInfo(long shopId) {
		Bundle dto = new Bundle();
		PROMOTION_PROGRAME_TABLE promotionTable = new PROMOTION_PROGRAME_TABLE(mDB);
		int numPromotionPrograme = promotionTable.getNumPromotionProgrameRunning(String.valueOf(shopId));

		DISPLAY_PROGRAME_TABLE displayTable = new DISPLAY_PROGRAME_TABLE(mDB);
		int numDisplayPrograme = displayTable.getNumDisplayProgrameRunning();

		dto.putInt("CTKM", numPromotionPrograme);
		dto.putInt("CTTB", numDisplayPrograme);

		return dto;
	}

	/**
	 * 
	 * get list track and fix problem of gsnpp
	 * 
	 * @param ext
	 * @return
	 * @return: SuperviorTrackAndFixProblemOfGSNPPViewDTO
	 * @throws:
	 * @author: HaiTC3
	 * @date: Nov 6, 2012
	 */
	public SuperviorTrackAndFixProblemOfGSNPPViewDTO getListTrackAndFixProblemOfGSNPP(Bundle ext) {
		SuperviorTrackAndFixProblemOfGSNPPViewDTO reviewsData = new SuperviorTrackAndFixProblemOfGSNPPViewDTO();
		FEED_BACK_TABLE feedBack = new FEED_BACK_TABLE(mDB);
		try {
			boolean isGetTotalitem = ext.getBoolean(IntentConstants.INTENT_IS_ALL);
			SuperviorTrackAndFixProblemOfGSNPPViewDTO reviewsDataTMP = feedBack.getListTrackAndFixProblemOfGSNPP(ext);
			reviewsData.listProblemsOfGSNPP = reviewsDataTMP.listProblemsOfGSNPP;
			if (isGetTotalitem) {
				reviewsData.totalItem = reviewsDataTMP.totalItem;
			}

		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			return null;
		}
		return reviewsData;
	}

	/**
	 * Get list time define for header
	 * 
	 * @author: QuangVT
	 * @since: 4:22:05 PM Dec 4, 2013
	 * @return: ArrayList<ShopParamDTO>
	 * @throws:
	 * @param ext
	 * @return
	 */
	public ArrayList<ShopParamDTO> getListDefineHeaderTable(Bundle ext) {
		ArrayList<ShopParamDTO> listDefineTimeHeader = new ArrayList<ShopParamDTO>();
		SHOP_PARAM_TABLE shopPram = new SHOP_PARAM_TABLE(mDB);
		try {
			listDefineTimeHeader = shopPram.getListTimeHeader(ext);
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			return null;
		}
		return listDefineTimeHeader;
	}

	/**
	 * 
	 * lay d/s lich su bao cao cua NVBH da ghe tham khach hang trong ngay
	 * 
	 * @param ext
	 * @return
	 * @return: ArrayList<ReportNVBHVisitCustomerDTO>
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 21, 2013
	 */
	public ArrayList<ReportNVBHVisitCustomerDTO> getListReportVisitCustomerInDay(Bundle ext) {
		ArrayList<ReportNVBHVisitCustomerDTO> listReportNVBH = new ArrayList<ReportNVBHVisitCustomerDTO>();
		ACTION_LOG_TABLE actionLog = new ACTION_LOG_TABLE(mDB);
		try {
			listReportNVBH = actionLog.getListReportNVBHInDay(ext);
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			return null;
		}
		return listReportNVBH;
	}

	/**
	 * 
	 * request update feedback status
	 * 
	 * @return
	 * @return: int
	 * @throws:
	 * @author: HaiTC3
	 * @param updateUser
	 * @param updateDate
	 * @date: Nov 7, 2012
	 */
	public int requestUpdateFeedbackStatus(String feedBackId, String feedBackStatus, String doneDate, String updateDate, String updateUser) {
		FEED_BACK_TABLE feedBack = new FEED_BACK_TABLE(mDB);
		int result = 0;
		try {
			mDB.beginTransaction();
			feedBack.updateFeedBackStatus(feedBackStatus, doneDate, feedBackId, updateDate, updateUser);
			result = 1;
			mDB.setTransactionSuccessful();
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			result = 0;
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}
		return result;
	}

	/**
	 * Lấy danh sách khách hàng Sử dụng: MH thêm ghi chú NVBH, TTTT, GSBH, GST;
	 * MH theo dõi khắc phục GSBH
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return
	 * @return: TBHVAddRequirementViewDTO
	 * @throws:
	 */
	public TBHVCustomerListDTO getCustomerListForPostFeedback(Bundle ext) {
		// sale order detail - lay ds order detail
		TBHVCustomerListDTO dto = null;
		STAFF_TABLE staffTable = new STAFF_TABLE(mDB);
		try {
			dto = staffTable.getCustomerListForPostFeedback(ext);
			return dto;
		} catch (Exception e) {

			return null;
		}
	}

	/**
	 * Lay thong tin GSNNP & loai van de
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param shopId
	 * @return
	 * @return: TBHVAddRequirementViewDTO
	 * @throws:
	 */
	public TBHVAddRequirementViewDTO getAddRequirementInfo(long shopId) {
		// TODO Auto-generated method stub
		STAFF_TABLE staffTable = new STAFF_TABLE(mDB);
		AP_PARAM_TABLE apParamTable = new AP_PARAM_TABLE(mDB);
		TBHVAddRequirementViewDTO model = new TBHVAddRequirementViewDTO();

		model.listNVBH = staffTable.getStaffCodeComboboxProblemSuperVisorOfTBHV(String.valueOf(shopId));
		model.listTypeProblem = apParamTable.getListTBHVProblemType();
		return model;
	}

	/**
	 * Luu thong tin feedback (problem) do TBHV tao ra
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return
	 * @return: TBHVAddRequirementViewDTO
	 * @throws:
	 */
	public boolean createTBHVFeedback(FeedBackDTO feedBack) {
		long feedBackId = -1;
		boolean insertSuccess = false;

		FEED_BACK_TABLE orderTable = new FEED_BACK_TABLE(mDB);
		TABLE_ID tableId = new TABLE_ID(mDB);
		try {
			mDB.beginTransaction();
			// cap nhat id order
			feedBackId = tableId.getMaxId(FEED_BACK_TABLE.FEED_BACK_TABLE);
			feedBack.feedBackId = feedBackId;

			if (orderTable.insert(feedBack) > 0) {
				insertSuccess = true;
				mDB.setTransactionSuccessful();
			}
		} catch (Exception e) {
			insertSuccess = false;
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}

		return insertSuccess;
	}

	/**
	 * Lay danh sach GSBH - MH theo doi vi tri NVBH, GSBH, TTTT role GST
	 * 
	 * @author: YenNTH
	 * @return
	 * @return: TBHVRouteSupervisionDTOvoid
	 * @throws:
	 */
	public TBHVRouteSupervisionDTO getGsnppPosition(String shopId) {
		STAFF_TABLE staffTable = new STAFF_TABLE(mDB);
		TBHVRouteSupervisionDTO dto = null;
		try {
			dto = staffTable.getGsnppTtttPosition(shopId);
		} catch (Exception ex) {
		}
		return dto;
	}

	/**
	 * Lay danh sach NVBH - MH theo doi vi tri NVBH, GSBH, TTTT role GST
	 * 
	 * @author: YenNTH
	 * @param staffId
	 * @return
	 * @return: SaleRoadMapSupervisorDTOvoid
	 * @throws:
	 */
	public TBHVRouteSupervisionDTO getNvbhPosition(String staffId, String shopId) {
		STAFF_TABLE staffTable = new STAFF_TABLE(mDB);
		TBHVRouteSupervisionDTO dto = null;
		try {
			dto = staffTable.getNvbhPositionOfGsnpp(staffId, shopId);
		} catch (Exception ex) {
		}
		return dto;
	}

	/**
	 * Lay DS KH < 2 phut
	 * 
	 * @author: TamPQ
	 * @param lessThan2MinList
	 * @return
	 * @return: LessThan2MinsDTOvoid
	 * @throws:
	 */
	public GsnppLessThan2MinsDTO requestGetLessThan2Mins(long staffId, String lessThan2MinList) {
		GsnppLessThan2MinsDTO dto = null;

		if (mDB != null && mDB.isOpen()) {
			try {
				CUSTOMER_TABLE table = new CUSTOMER_TABLE(mDB);
				dto = table.requestGetLessThan2Mins(staffId, lessThan2MinList);
			} catch (Exception ex) {
			}
		}
		return dto;
	}

	/**
	 * 
	 * get forcus product infor view
	 * 
	 * @param data
	 * @return
	 * @return: NVBHReportForcusProductInfoViewDTO
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 2, 2013
	 */
	public NVBHReportForcusProductInfoViewDTO getForcusProductInfoView(Bundle data) {
		PRODUCT_TABLE productTable = new PRODUCT_TABLE(mDB);

		try {
			NVBHReportForcusProductInfoViewDTO vt = productTable.getForcusProductInfoView(data);
			return vt;

		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));

			return null;
		}
	}

	/**
	 * K tra da co log ghe tham trong ngay hay chua cua 1 KH
	 * 
	 * @author: TamPQ
	 * @param actionLog
	 * @return
	 * @return: booleanvoid
	 * @throws:
	 */
	public boolean alreadyHaveVisitLog(ActionLogDTO actionLog) {
		boolean alreadyHaveVisitLog = false;
		STAFF_TABLE staffTable = new STAFF_TABLE(mDB);
		try {
			alreadyHaveVisitLog = staffTable.alreadyHaveVisitLog(actionLog);
		} catch (Exception e) {
		}
		return alreadyHaveVisitLog;
	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @param list
	 * @return
	 * @return: ArrayList<StaffItem>void
	 * @throws:
	 */
	public ListStaffDTO getPositionOfGsnppAndNvbh(String[] list) {
		STAFF_TABLE staffTable = new STAFF_TABLE(mDB);
		ListStaffDTO staffList = staffTable.getPositionOfGsnppAndNvbh(list);
		return staffList;
	}

	/**
	 * danh sach van de NVBH
	 * 
	 * @author: YenNTH
	 * @return
	 * @return: ArrayList<StaffItem>void
	 * @throws:
	 */
	public Vector<ApParamDTO> getListTypeProblemNVBHGSNPP() {
		AP_PARAM_TABLE apParamTable = new AP_PARAM_TABLE(mDB);
		return apParamTable.getListTypeProblemNVBHGSNPP();
	}

	/**
	 * chi tiet cong no
	 * 
	 * @author: TamPQ
	 * @return
	 * @return: CustomerListDTOvoid
	 * @throws:
	 */
	public CusDebitDetailDTO requestDebitDetail(long debitId) {
		DEBIT_DETAIL_TABLE debit = new DEBIT_DETAIL_TABLE(mDB);
		CusDebitDetailDTO debitDetail = debit.requestDebitDetail(debitId);
		return debitDetail;
	}

	/**
	 * lay danh sach van de (GSBH) MH them van de GSBH
	 * 
	 * @author: TamPQ
	 * @param from
	 * @return
	 * @return: ArrayList<String>void
	 * @throws:
	 */
	public ArrayList<ApParamDTO> requestGetTypeFeedback(int from) {
		AP_PARAM_TABLE table = new AP_PARAM_TABLE(mDB);
		ArrayList<ApParamDTO> list = table.requestGetTypeFeedback(from);
		return list;
	}

	/**
	 * Request get danh sach cong no khach hang
	 * 
	 * @author: BangHN
	 * @param cusCode
	 * @param cusNameAdd
	 * @return
	 * @return: ArrayList<CustomerDebtDTO>
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */

	public ArrayList<CustomerDebtDTO> requestGetCustomerDebt(String cusCode, String cusNameAdd) {
		DEBIT_TABLE debit = new DEBIT_TABLE(mDB);
		ArrayList<CustomerDebtDTO> listData = null;
		try {
			listData = debit.getCustomerDebt(cusCode, cusNameAdd);
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return listData;
	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @param staffID
	 * @return
	 * @return: TBHVVisitCustomerNotificationDTOvoid
	 * @throws Exception
	 * @throws:
	 */
	public TBHVVisitCustomerNotificationDTO getVisitCusNotification(String shopId, String staffID)
			throws Exception {
		STAFF_TABLE staffTable = new STAFF_TABLE(mDB);
		TBHVVisitCustomerNotificationDTO dto = staffTable.getVisitCusNotification(shopId, staffID);
		return dto;
	}

	/**
	 * get list product storage
	 * 
	 * @author: HieuNH
	 * @return: ListProductDTO
	 * @throws:
	 */
	public GSNPPTakeAttendaceDTO getSupervisorAttendanceList(Bundle ext) {
		STAFF_TABLE staffTable = new STAFF_TABLE(mDB);
		SHOP_PARAM_TABLE apParamTable = new SHOP_PARAM_TABLE(mDB);
		SHOP_TABLE shopTable = new SHOP_TABLE(mDB);
		GSNPPTakeAttendaceDTO dto = new GSNPPTakeAttendaceDTO();
		try {
			List<ShopParamDTO> paramList = apParamTable.getListParamForTakeAttendance(ext.getString(IntentConstants.INTENT_SHOP_ID));
			dto.listInfo = paramList;
			LatLng shopPosition = shopTable.getPositionOfShop(ext.getString(IntentConstants.INTENT_SHOP_ID));
			dto.shopPosition = shopPosition;
			List<AttendanceDTO> attendanceList = staffTable.getSupervisorAttendanceList(ext, shopPosition);
			dto.listStaff = attendanceList;
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
		}
		return dto;
	}

	/**
	 * Lay thoi gian deffine cac tham so thoi gian cham cong
	 * 
	 * @author banghn
	 * @param ext
	 *            : bundle chua shop id
	 * @return: cac tham so define
	 */
	public List<ShopParamDTO> getAttendaceTimeDefine(Bundle ext) {
		SHOP_PARAM_TABLE apParamTable = new SHOP_PARAM_TABLE(mDB);
		List<ShopParamDTO> paramList = null;
		try {
			paramList = apParamTable.getListParamForTakeAttendance(ext.getString(IntentConstants.INTENT_PARENT_SHOP_ID));
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
		}
		return paramList;
	}

	/**
	 * Lay ds cham cong cua TBHV
	 * 
	 * @author: HieuNH
	 * @return: ListProductDTO
	 * @throws:
	 */
	public GSNPPTakeAttendaceDTO getTBHVListAttendance(Bundle ext) {
		STAFF_TABLE staffTable = new STAFF_TABLE(mDB);
		SHOP_TABLE shopTable = new SHOP_TABLE(mDB);
		SHOP_PARAM_TABLE apParamTable = new SHOP_PARAM_TABLE(mDB);

		boolean isRefressCombox = ext.getBoolean(IntentConstants.INTENT_CHECK_COMBOBOX);
		GSNPPTakeAttendaceDTO dto = new GSNPPTakeAttendaceDTO();
		try {
			List<ShopParamDTO> paramList = apParamTable.getListParamForTakeAttendance(ext.getString(IntentConstants.INTENT_SHOP_ID));
			dto.listInfo = paramList;
			if (isRefressCombox) {
				List<ShopSupervisorDTO> listCombo = staffTable.getTBHListSupervisorShopManaged(ext.getString(IntentConstants.INTENT_PARENT_SHOP_ID));
				dto.listCombox = listCombo;
			}
			LatLng shopPosition = shopTable.getPositionOfShop(ext.getString(IntentConstants.INTENT_SHOP_ID));
			dto.shopPosition = shopPosition;
			List<AttendanceDTO> attendanceList = staffTable.getSupervisorAttendanceList(ext, shopPosition);
			dto.listStaff = attendanceList;
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
		}
		return dto;
	}

	/**
	 * Tao phieu thu
	 * 
	 * @author: TamPQ
	 * @param cusDebitDetailDto
	 * @return
	 * @return: booleanvoid
	 * @throws:
	 */
	public boolean createPayReceived(CusDebitDetailDTO cusDebitDetailDto) {
		PAY_RECEIVED_TABLE payReceivedTable = new PAY_RECEIVED_TABLE(mDB);
		PAYMENT_DETAIL_TABLE payDetailTable = new PAYMENT_DETAIL_TABLE(mDB);
		TABLE_ID tableId = new TABLE_ID(mDB);
		try {
			mDB.beginTransaction();
			// generate PAY_RECEIVED_NUMBER
			// PAY_RECEIVED_NUMBER = chu cai dau tien (CT) + 5 chu cuoi staff_id
			// +
			// so cuoi cua pay_received_Id
			long payReceivedID = tableId.getMaxId(PAY_RECEIVED_TABLE.TABLE_PAY_RECEIVED);
			String strpayReceivedID = String.valueOf(payReceivedID);
			String userId = ""
					+ GlobalInfo.getInstance().getProfile().getUserData().id;
			StringBuilder payReceivedNumber = new StringBuilder();
			payReceivedNumber.append("CT");
			if (userId.length() >= 5) {
				payReceivedNumber.append(userId.substring(userId.length() - 5));
			} else {
				String zero = "00000";
				int lengId = userId.length();
				payReceivedNumber.append(zero.substring(lengId) + userId);
			}

			if (strpayReceivedID.length() >= 8) {
				payReceivedNumber.append(strpayReceivedID.substring(strpayReceivedID.length() - 8));
			} else {
				String zero = "00000000";
				int lengId = strpayReceivedID.length();
				payReceivedNumber.append(zero.substring(lengId)
						+ strpayReceivedID);
			}

			// update DEBIT
			DEBIT_TABLE debit = new DEBIT_TABLE(mDB);
			long debitSucc = debit.updateDebt(cusDebitDetailDto);
			// send to server
			if (debitSucc <= 0) {
				return false;
			}
			String dateNow = DateUtils.now();
			// insert vao PayReceived
			PayReceivedDTO dto = new PayReceivedDTO();
			dto.payReceivedID = payReceivedID;
			dto.amount = cusDebitDetailDto.payNowTotal;
			dto.customerId = cusDebitDetailDto.customerId;
			dto.paymentType = PayReceivedDTO.PAYMENT_TYPE_CASH;
			dto.shopId = GlobalInfo.getInstance().getProfile().getUserData().shopId;
			dto.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
			dto.receiptType = 0;
			dto.payReceivedNumber = payReceivedNumber.toString();
			dto.createDate = dateNow;
			dto.createUser = ""
					+ GlobalInfo.getInstance().getProfile().getUserData().userName;
			cusDebitDetailDto.payReceivedDto = dto;
			long paySuccess = payReceivedTable.insert(dto);
			// send to server
			if (paySuccess == -1) {
				return false;
			}

			long tempPaymentDetailID = tableId.getMaxId(PAYMENT_DETAIL_TABLE.TABLE_PAYMENT_DETAIL);
			cusDebitDetailDto.arrPaymentDetailDto = new ArrayList<PaymentDetailDTO>();
			for (int i = 0; i < cusDebitDetailDto.arrList.size(); i++) {
				if (cusDebitDetailDto.arrList.get(i).isWouldPay) {
					// update DEBIT_DETAIL
					DEBIT_DETAIL_TABLE debitDetail = new DEBIT_DETAIL_TABLE(mDB);
					long detaiSucc = debitDetail.updateDebt(cusDebitDetailDto.arrList.get(i));
					// send to server
					if (detaiSucc <= 0) {
						return false;
					}

					// insert PAYMENT_DETAIL
					PaymentDetailDTO paymentDetailDto = new PaymentDetailDTO();
					paymentDetailDto.paymentDetailID = tempPaymentDetailID;
					paymentDetailDto.payReceivedID = payReceivedID;
					paymentDetailDto.debitDetailID = cusDebitDetailDto.arrList.get(i).debitDetailId;
					paymentDetailDto.amount = cusDebitDetailDto.arrList.get(i).paidAmount;
					paymentDetailDto.paymentType = "0";
					paymentDetailDto.status = 1;
					paymentDetailDto.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
					paymentDetailDto.payDate = dateNow;
					paymentDetailDto.createUser = ""
							+ GlobalInfo.getInstance().getProfile().getUserData().userName;
					paymentDetailDto.createDate = dateNow;
					long paymentSucc = payDetailTable.insert(paymentDetailDto);
					tempPaymentDetailID = paymentSucc + 1;
					cusDebitDetailDto.arrPaymentDetailDto.add(paymentDetailDto);

					// send to server
					if (paymentSucc <= 0) {
						return false;
					}
				} else {
					cusDebitDetailDto.arrPaymentDetailDto.add(new PaymentDetailDTO());
				}
			}
			mDB.setTransactionSuccessful();
		} catch (Exception e) {
			try {
				throw e;
			} catch (Exception e1) {
			}
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}

		return true;
	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @param staffTypeId
	 * @return
	 * @return: Stringvoid
	 * @throws:
	 */
	public String requestStaffType(int staffTypeId) {
		AP_PARAM_TABLE apParamTable = new AP_PARAM_TABLE(mDB);
		String staffType = null;
		try {
			staffType = apParamTable.requestStaffType(staffTypeId);
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
		}
		return staffType;
	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @return
	 * @return: ArrayList<ActionLogDTO>void
	 * @throws:
	 */
	public ArrayList<ActionLogDTO> getVisitActionLogWithEndTimeIsNull(long staffId, long cusId) {
		ArrayList<ActionLogDTO> listActLog = null;
		STAFF_TABLE staffTable = new STAFF_TABLE(mDB);
		try {
			listActLog = staffTable.getVisitActionLogWithEndTimeIsNull(staffId, cusId);
		} catch (Exception e) {
		}
		return listActLog;
	}

	/**
	 * 
	 * update list ActionLog To DB
	 * 
	 * @author: TamPQ
	 * @param
	 * @return
	 * @return:
	 * @throws:
	 */
	public long updateEndtimeListActionLogToDB(ArrayList<ActionLogDTO> listActLog) {
		long returnCode = -1;
		ACTION_LOG_TABLE table = new ACTION_LOG_TABLE(mDB);
		try {
			mDB.beginTransaction();
			for (int i = 0; i < listActLog.size(); i++) {
				returnCode = table.updateVisitEndtime(listActLog.get(i));
//				if(returnCode > 0){
//					ActionLogDTO dto = new ActionLogDTO();
//					dto = listActLog.get(i);
//					TABLE_ID tableId = new TABLE_ID(mDB);
//					dto.id = tableId.getMaxId(ACTION_LOG_TABLE.TABLE_NAME);
//					dto.objectType = "9";
//					ACTION_LOG_TABLE actionTable = new ACTION_LOG_TABLE(mDB);
//					returnCode = actionTable.insert(dto);
//				}
			}
			mDB.setTransactionSuccessful();
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}
		return returnCode;
	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @param isGetTotalPage
	 * @param page
	 * @return
	 * @return: CustomerListDTOvoid
	 * @throws:
	 */
	public CustomerListDTO requestGetCusNoPSDS(int rptSaleHisId, boolean isGetTotalPage, int page) {
		CUSTOMER_TABLE table = new CUSTOMER_TABLE(mDB);
		return table.requestGetCusNoPSDS(rptSaleHisId, isGetTotalPage, page);
	}

	/**
	 * lay danh sach hinh anh cua chuong trinh
	 * 
	 * @author thanhnn
	 * @param customerId
	 * @param type
	 * @param numTop
	 * @return
	 */
	public ArrayList<PhotoDTO> getAlbumDetailPrograme(String customerId, String type, String numTop, String page, String programeCode, String fromDate, String toDate, String shopId) {
		MEDIA_ITEM_TABLE mediaItemTable = new MEDIA_ITEM_TABLE(mDB);
		ArrayList<PhotoDTO> listPhoto = mediaItemTable.getAlbumDetailPrograme(customerId, type, numTop, page, programeCode, fromDate, toDate, shopId);
		return listPhoto;
	}

	/**
	 * lay danh sach hinh anh o che do search
	 * 
	 * @author: QuangVT
	 * @since: 6:01:39 PM Dec 12, 2013
	 * @return: ArrayList<PhotoDTO>
	 * @throws:
	 * @param customerId
	 * @param numTop
	 * @param page
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public ArrayList<PhotoDTO> getAlbumDetailForSearch(String customerId, String numTop, String page, String fromDate, String toDate) {
		MEDIA_ITEM_TABLE mediaItemTable = new MEDIA_ITEM_TABLE(mDB);
		ArrayList<PhotoDTO> listPhoto = mediaItemTable.getAlbumDetailForSearch(customerId, numTop, page, fromDate, toDate);
		return listPhoto;
	}

	/**
	 * lay danh sach hinh anh cua chuong trinh (gsnpp)
	 * 
	 * @author thanhnn
	 * @param customerId
	 * @param type
	 * @param numTop
	 * @return
	 */
	public ArrayList<PhotoDTO> getSuperVisorAlbumDetailPrograme(String customerId, String type, String numTop, String page, String programeCode, String fromDate, String toDate) {
		MEDIA_ITEM_TABLE mediaItemTable = new MEDIA_ITEM_TABLE(mDB);
		ArrayList<PhotoDTO> listPhoto = mediaItemTable.getSuperVisorAlbumDetailPrograme(customerId, type, numTop, page, programeCode, fromDate, toDate);
		return listPhoto;
	}

	/**
	 * lay danh sach album theo chuong trinh cua khach hang
	 * 
	 * @author thanhnn
	 * @param data
	 * @return
	 */
	public ArrayList<AlbumDTO> getAlbumProgrameInfo(Bundle data) {
		MEDIA_ITEM_TABLE mediaItemTable = new MEDIA_ITEM_TABLE(mDB);
		String customerId = data.getString(IntentConstants.INTENT_CUSTOMER_ID);
		String programeCode = data.getString(IntentConstants.INTENT_DISPLAY_PROGRAM_CODE);
		String fromDate = data.getString(IntentConstants.INTENT_FIND_ORDER_FROM_DATE);
		String toDate = data.getString(IntentConstants.INTENT_FIND_ORDER_TO_DATE);
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		ArrayList<AlbumDTO> arrayListAlbum = mediaItemTable.getAlbumProgrameInfo(programeCode, customerId, fromDate, toDate, shopId);
		return arrayListAlbum;

	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @param staffId
	 * @return
	 * @return: StaffDTOvoid
	 * @throws:
	 */
	public GeomDTO getPosition(long staffId) {
		STAFF_POSITION_LOG_TABLE table = new STAFF_POSITION_LOG_TABLE(mDB);
		return table.getPosition(staffId);
	}

	public CustomerNotPsdsInMonthReportViewDTO getReportCustomerNotPSDSForSaleMan(long shopId, int dayInWeek, String staffId, int page, int numItemInPage) {
		CUSTOMER_TABLE table = new CUSTOMER_TABLE(mDB);
		return table.getReportCustomerNotPSDSForSaleMan(shopId, dayInWeek, staffId, page, numItemInPage);
	}

	public CustomerNotPsdsInMonthReportViewDTO getReportCustomerNotPSDSForSupervisor(long shopId, int dayInWeek, String staffId, int page, int numItemInPage) {
		CUSTOMER_TABLE table = new CUSTOMER_TABLE(mDB);
		return table.getReportCustomerNotPSDSForSaleMan(shopId, dayInWeek, staffId, page, numItemInPage);
	}

	/**
	 * Lay ds PG de cham cong
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return
	 * @throws:
	 */
	public PGTakeAttendanceViewDTO getListPGForTakeAttendance(Bundle data) {
		PG_VISIT_PLAN_TABLE table = new PG_VISIT_PLAN_TABLE(mDB);
		return table.getListPGForTakeAttendance(data);
	}

	/**
	 * danh sach van de role TTTT
	 * 
	 * @author: YenNTH
	 * @return
	 * @return: ArrayList<StaffItem>void
	 * @throws:
	 */
	public Vector<ApParamDTO> getListTypeProblemTTTT() {
		AP_PARAM_TABLE apParamTable = new AP_PARAM_TABLE(mDB);
		return apParamTable.getListTypeProblemTTTT();
	}

	/**
	 * Lay thong tin doi thu canh tranh
	 * 
	 * @author: dungdq3
	 * @param:
	 * @return: void
	 */
	public ProductCompetitorListDTO getInformationCompetitor(String staffId, String cusId, String date) {
		OPPONENT_TABLE oppo = new OPPONENT_TABLE(mDB);
		return oppo.getInformationCompetitor(staffId, cusId, date);
	}

	/**
	 * Lay thong tin doi thu canh tranh
	 * 
	 * @author: dungdq3
	 * @param:
	 * @return: void
	 */
	public ProductCompetitorListDTO getInformationCompetitorPG() {
		OPPONENT_TABLE oppo = new OPPONENT_TABLE(mDB);
		return oppo.getInformationCompetitorPG();
	}

	/**
	 * Lay thong tin BSG
	 * 
	 * @author: dungdq3
	 * @param:
	 * @return: void
	 */
	public ProductCompetitorListDTO getInformationBSG(String staffId, String cusId, String date) {
		OPPONENT_TABLE oppo = new OPPONENT_TABLE(mDB);
		return oppo.getInformationBSG(staffId, cusId, date);
	}

	/**
	 * Luu thong tin cham cong
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param:
	 * @return: ArrayList<TimeKeeperDTO>
	 */
	public ArrayList<TimeKeeperDTO> savePGTakeAttendance(Bundle data) {
		ArrayList<TimeKeeperDTO> listCSTDTO = new ArrayList<TimeKeeperDTO>();
		PGTakeAttendanceViewDTO dto = (PGTakeAttendanceViewDTO) data.getSerializable(IntentConstants.INTENT_TTTT_TAKE_ATTENDANCE_DTO);
		long staffOwnerId = Long.parseLong(data.getString(IntentConstants.INTENT_STAFF_ID));
		long customerId = Long.parseLong(data.getString(IntentConstants.INTENT_CUSTOMER_ID));
		try {
			mDB.beginTransaction();
			TABLE_ID tableId = new TABLE_ID(mDB);
			TIME_KEEPER_TABLE timeKeeperTable = new TIME_KEEPER_TABLE(mDB);
			long cshId = tableId.getMaxId(TIME_KEEPER_TABLE.TABLE_NAME);

			boolean checkSuccess = true;
			int k = 0;
			for (int i = 0; i < dto.listPG.size(); i++) {
				PGTakeAttendanceDTO item = dto.listPG.get(i);
				TimeKeeperDTO cus = new TimeKeeperDTO();
				if(item.id == 0) {
					cus.timeKeeperId = cshId + k;
					k++;
					cus.createDate = DateUtils.now();
					cus.createUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;
					cus.isEdit = false;
					//cus.startTime = null;
				} else {
					cus.timeKeeperId = item.id;
					cus.isEdit = true;
					cus.updateDate = DateUtils.now();
					cus.updateUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;
					cus.createDate = item.createDate;
					cus.createUser = item.createUser;
					//cus.startTime = item.timeOnWork;
				}
				cus.startTime = item.timeOnWork;
				cus.staffOwnerId = staffOwnerId;
				cus.shopId = Long.parseLong(GlobalInfo.getInstance().getProfile().getUserData().shopId);
				cus.customerId = customerId;
				cus.staffId = Long.valueOf(item.staffId);
				cus.isAbsent = item.isOnWork ? 0 : 1;
//				if(cus.isAbsent == 1) {
//					cus.startTime = null;
//				}
				cus.dresses = item.isDress ? 1 : 0;
				cus.rule = item.isFollowRule ? 1 : 0;
				cus.type = 1;
				listCSTDTO.add(cus);

				checkSuccess = timeKeeperTable.insertOrUpdate(cus) >= 0 ? true : false;

				if (!checkSuccess) {
					break;
				}
			}
			if (checkSuccess) {
				mDB.setTransactionSuccessful();
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}
		return listCSTDTO;
	}

	/**
	 * Luu danh sach kiem ton
	 * 
	 * @author: dungdq3
	 * @param: ActionEvent e
	 * @return: void
	 */
	public long saveRemainCompetitor(long staffID, long cusID, ProductCompetitorListDTO listProductCompetitor, String staffCode) {
		// TODO Auto-generated method stub
		OP_STOCK_TOTAL_TABLE opStockTotal = new OP_STOCK_TOTAL_TABLE(mDB);
		long success = -1;
		TABLE_ID tableId = new TABLE_ID(mDB);
		mDB.beginTransaction();
		try {
			long idMax = tableId.getMaxId(OP_STOCK_TOTAL_TABLE.OP_STOCK_TOTAL_TABLE);
			for (ProductCompetitorDTO product : listProductCompetitor.getArrProductCompetitor()) {
				idMax = opStockTotal.insertStockTotal(product, staffID, cusID,staffCode, idMax);
				success = idMax;
				if (success == -1) { // insert ko thanh cong
					break;
				}
			}
			if (success > -1) { // insert thanh cong thi moi set successful
				mDB.setTransactionSuccessful();
			}
		} catch (Exception ex) {
			success = -1;
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}
		return success;
	}

	/**
	 * Kiem tra xem co can kiem hang ton ko!?
	 * 
	 * @author: dungdq3
	 * @param: Bundle data
	 * @return: int
	 */
	public long checkRemainedCompetitor(Bundle data) {
		// TODO Auto-generated method stub
		OP_STOCK_TOTAL_TABLE opStockTotal = new OP_STOCK_TOTAL_TABLE(mDB);
		return opStockTotal.checkRemained(data);
	}

	/**
	 * Kiem tra xem co can kiem ban hang ko!?
	 * 
	 * @author: dungdq3
	 * @param: Bundle data
	 * @return: int
	 */
	public long checkSaledCompetitor(Bundle data) {
		// TODO Auto-generated method stub
		OP_SALE_VOLUME_TABLE opSaleVolume = new OP_SALE_VOLUME_TABLE(mDB);
		return opSaleVolume.checkSaled(data);
	}

	/**
	 * Lưu danh sách kiểm bán
	 * 
	 * @author: quangvt1
	 * @since: 13:15:35 08-05-2014
	 * @return: long
	 * @throws:
	 * @param staffID
	 * @param cusID
	 * @param date
	 * @param type
	 *            : 1 - BSG, 0 - Bia doi thu
	 * @return
	 */
	public long saveSaleCompetitor(long staffID, String staffCode, long cusID, String date, int type, ProductCompetitorListDTO listProductCompetitor) {
		// TODO Auto-generated method stub
		OP_SALE_VOLUME_TABLE opSaleVolume = new OP_SALE_VOLUME_TABLE(mDB);
		OP_PRICE_TABLE opPrice = new OP_PRICE_TABLE(mDB);
		long success = -1;
		TABLE_ID tableId = new TABLE_ID(mDB);
		mDB.beginTransaction();
		try {
			long maxID = tableId.getMaxId(OP_SALE_VOLUME_TABLE.OP_SALE_VOLUME_TABLE);
			long maxPriceID = tableId.getMaxId(OP_PRICE_TABLE.OP_PRICE_TABLE);
			for (ProductCompetitorDTO product : listProductCompetitor.getArrProductCompetitor()) {
				maxID = maxID + 1;
				maxID = opSaleVolume.saveSaleVolume(product, staffID, staffCode, cusID, date, type, maxID);
				//success = maxID;
				maxPriceID = maxPriceID + 1;
				maxPriceID = opPrice.savePrice(product, staffID, staffCode, cusID, date, type, maxPriceID);
				//success = maxPriceID;
				if (maxID == -1 && maxPriceID == -1) {
					success = -1;
					break;
				} else {
					success = 1;
				}
			}
			if (success > -1) {
				mDB.setTransactionSuccessful();
			}
		} catch (Exception ex) {
			success = -1;
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}

		return success;
	}

	/**
	 * Lưu danh sách kiểm bán đối thủ
	 * 
	 * @author: dungdq3
	 * @return: long
	 */
	public long saveSaleCompetitorPG(long staffID, long cusID, ProductCompetitorListDTO listProductCompetitor, int type) {
		// TODO Auto-generated method stub
		OP_SALE_VOLUME_TABLE opSaleVolume = new OP_SALE_VOLUME_TABLE(mDB);
		long success = -1;
		String date = null;
		TABLE_ID tableId = new TABLE_ID(mDB);
		mDB.beginTransaction();
		try {
			long maxID = tableId.getMaxId(OP_SALE_VOLUME_TABLE.OP_SALE_VOLUME_TABLE);
			for (ProductCompetitorDTO product : listProductCompetitor.getArrProductCompetitor()) {
				maxID = opSaleVolume.insertSaleVolumePG(product, staffID, cusID, type, date, maxID);
				success = maxID;
				if (success == -1) {
					break;
				}
			}
			if (success > -1) {
				mDB.setTransactionSuccessful();
			}
		} catch (Exception ex) {
			success = -1;
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}
		return success;
	}

	/**
	 * 
	 * get list product for C2 sale or buy
	 * 
	 * @param ext
	 * @return
	 * @return: C2SaleOrderlViewDTO
	 * @throws:
	 * @author: Nguyen Thanh Dung
	 * @date: 06/12/2013
	 */
	public C2SaleOrderlViewDTO getListProductForC2Order(Bundle ext) {
		PRODUCT_TABLE table = new PRODUCT_TABLE(mDB);
		C2SaleOrderlViewDTO orderInfo = table.getListProductForC2Order(ext);
		AP_PARAM_TABLE paramTable = new AP_PARAM_TABLE(mDB);
		orderInfo.c2ReportTime = paramTable.getC2ReportTime();
		return orderInfo;
	}

	/**
	 * tao don hang cho C2
	 * @return
	 * @return: C2SaleOrderlViewDTO
	 * @throws:
	 * @author: Nguyen Thanh Dung
	 * @date: 06/12/2013
	 */
	public boolean createOrderForC2(C2SaleOrderlViewDTO order) {
		long saleOrderId = -1;
		long saleOrderDetailId = -1;
		boolean insertSuccess = false;
		order.orderInfo.createDate = DateUtils.now();
		order.orderInfo.createUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;

		C2_SALE_ORDER_TABLE orderTable = new C2_SALE_ORDER_TABLE(mDB);
		C2_SALE_ORDER_DETAIL_TABLE detailTable = new C2_SALE_ORDER_DETAIL_TABLE(mDB);
		TABLE_ID tableId = new TABLE_ID(mDB);
		// int staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
		// int shopId =
		// Integer.valueOf(GlobalInfo.getInstance().getProfile().getUserData().shopId);

		try {
			mDB.beginTransaction();
			saleOrderId = tableId.getMaxId(C2_SALE_ORDER_TABLE.TABLE_NAME);
			saleOrderDetailId = tableId.getMaxId(C2_SALE_ORDER_DETAIL_TABLE.TABLE_NAME);
			order.orderInfo.c2SaleOrderId = saleOrderId;

			if (orderTable.insert(order.orderInfo) > 0) {
				// insert sale order detail
				List<C2SaleOrderDetailViewDTO> listDetail = order.listProduct;
				boolean insertOrderDetail = true;
				if (listDetail != null && listDetail.size() > 0) {
					for (C2SaleOrderDetailViewDTO detailViewDTO : listDetail) {
						if (detailViewDTO.orderDetail.amount > 0) {
							detailViewDTO.orderDetail.c2SaleOrderDetailId = saleOrderDetailId;
							detailViewDTO.orderDetail.shopId = order.orderInfo.shopId;
							detailViewDTO.orderDetail.c2Id = order.orderInfo.c2Id;
							detailViewDTO.orderDetail.staffId = order.orderInfo.staffId;
							detailViewDTO.orderDetail.orderDate = order.orderInfo.orderDate;
							detailViewDTO.orderDetail.c2SaleOrderId = order.orderInfo.c2SaleOrderId;
							detailViewDTO.orderDetail.createUser = order.orderInfo.createUser;
							detailViewDTO.orderDetail.createDate = order.orderInfo.createDate;
							detailViewDTO.orderDetail.status = order.orderInfo.status;
							// Check insert success
							insertOrderDetail = detailTable.insert(detailViewDTO.orderDetail) > 0 ? true
									: false;

							// Update stock total if insert order success
							if (insertOrderDetail) {
								saleOrderDetailId += 1;
							} else {
								break;
							}
						}
					}
				}

				if (insertOrderDetail) {
					insertSuccess = true;
					mDB.setTransactionSuccessful();
				}
			}
		} catch (Exception e) {
			insertSuccess = false;
			ServerLogger.sendLog(e.getMessage(), "createOrder", false, TabletActionLogDTO.LOG_EXCEPTION);
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}

		return insertSuccess;
	}

	/**
	 * Lay ds KH la C2
	 * @return
	 * @return: C2SaleOrderlViewDTO
	 * @throws:
	 * @author: Nguyen Thanh Dung
	 * @date: 06/12/2013
	 */
	public CustomerListDTO getListC2Customer(long staffId, long shopId, int page, boolean isGetTotalPage) {
		CustomerListDTO dto = null;
		CUSTOMER_TABLE custommerTable = new CUSTOMER_TABLE(mDB);
		try {
			dto = custommerTable.getListC2Customer(staffId, shopId, page, isGetTotalPage);
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
		}
		return dto;
	}

	/**
	 * Lay danh sach khach hang C2 trong tuyen cua nhan vien ban hang
	 * 
	 * @author: quangvt1
	 * @since: 17:42:03 05-05-2014
	 * @return: CustomerListDTO
	 * @throws:
	 * @param staffId
	 * @param shopId
	 * @return
	 */
	public CustomerListDTO getListC2CustomerOnPlan(long staffId, long shopId, int page, boolean isGetTotalPage) {
		CustomerListDTO dto = null;
		CUSTOMER_TABLE custommerTable = new CUSTOMER_TABLE(mDB);
		try {
			dto = custommerTable.getListC2CustomerOnPlan(staffId, shopId, page, isGetTotalPage);
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
		}
		return dto;
	}

	/**
	 * 
	 * get list product for C2 sale or buy
	 * 
	 * @param ext
	 * @return
	 * @return: C2SaleOrderlViewDTO
	 * @throws:
	 * @author: Nguyen Thanh Dung
	 * @date: 06/12/2013
	 */
	public C2PurchaselViewDTO getListProductForC2Purchase(Bundle ext) {
		PRODUCT_TABLE table = new PRODUCT_TABLE(mDB);
		C2PurchaselViewDTO orderInfo = table.getListProductForC2Purchase(ext);
		AP_PARAM_TABLE paramTable = new AP_PARAM_TABLE(mDB);
		orderInfo.c2ReportTime = paramTable.getC2ReportTime();
		return orderInfo;
	}

	/**
	 * tao don mua hang cho C2
	 * @return
	 * @return: C2SaleOrderlViewDTO
	 * @throws:
	 * @author: Nguyen Thanh Dung
	 * @date: 06/12/2013
	 */
	public boolean createPurchaseForC2(C2PurchaselViewDTO order) {
		long saleOrderId = -1;
		long saleOrderDetailId = -1;
		boolean insertSuccess = false;
		order.orderInfo.createDate = DateUtils.now();
		order.orderInfo.createUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;

		C2_PURCHASE_TABLE orderTable = new C2_PURCHASE_TABLE(mDB);
		C2_PURCHASE_DETAIL_TABLE detailTable = new C2_PURCHASE_DETAIL_TABLE(mDB);
		TABLE_ID tableId = new TABLE_ID(mDB);
		// int staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
		// int shopId =
		// Integer.valueOf(GlobalInfo.getInstance().getProfile().getUserData().shopId);

		try {
			mDB.beginTransaction();
			saleOrderId = tableId.getMaxId(C2_PURCHASE_TABLE.TABLE_NAME);
			saleOrderDetailId = tableId.getMaxId(C2_PURCHASE_DETAIL_TABLE.TABLE_NAME);
			order.orderInfo.c2PurchaseId = saleOrderId;

			if (orderTable.insert(order.orderInfo) > 0) {
				// insert sale order detail
				List<C2PurchaseDetailViewDTO> listDetail = order.listProduct;
				boolean insertOrderDetail = true;
				if (listDetail != null && listDetail.size() > 0) {
					for (C2PurchaseDetailViewDTO detailViewDTO : listDetail) {
						if (detailViewDTO.orderDetail.quantity > 0) {
							detailViewDTO.orderDetail.c2PurchaseDetailId = saleOrderDetailId;
							detailViewDTO.orderDetail.staffId = order.orderInfo.staffId;
							detailViewDTO.orderDetail.purchaseDate = order.orderInfo.c2PurchaseDate;
							detailViewDTO.orderDetail.c2PurchaseId = order.orderInfo.c2PurchaseId;
							detailViewDTO.orderDetail.createUser = order.orderInfo.createUser;
							detailViewDTO.orderDetail.createDate = order.orderInfo.createDate;
							detailViewDTO.orderDetail.status = order.orderInfo.status;
							// Check insert success
							insertOrderDetail = detailTable.insert(detailViewDTO.orderDetail) > 0 ? true
									: false;

							// Update stock total if insert order success
							if (insertOrderDetail) {
								saleOrderDetailId += 1;
							} else {
								break;
							}
						}
					}
				}

				if (insertOrderDetail) {
					insertSuccess = true;
					mDB.setTransactionSuccessful();
				}
			}
		} catch (Exception e) {
			insertSuccess = false;
			ServerLogger.sendLog(e.getMessage(), "createOrder", false, TabletActionLogDTO.LOG_EXCEPTION);
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}

		return insertSuccess;
	}

	/**
	 * Lay thong tin action log xem da ghe tham KH hay chua
	 * 
	 * @author: dungnt19
	 * @since: 11:43:37 16-12-2013
	 * @return: ActionLogDTO
	 * @throws:
	 * @param staffId
	 * @param customerId
	 * @return
	 */
	public ActionLogDTO checkTNPGHaveActionFromActionLog(String staffId, String customerId) {
		ACTION_LOG_TABLE table = new ACTION_LOG_TABLE(mDB);
		ActionLogDTO dto = table.checkTNPGHaveActionFromActionLog(staffId, customerId);
		return dto;
	}

	/**
	 * Lay danh sach SHOP, GSBH, NVHB cua GST
	 * 
	 * @author: QuangVT
	 * @since: 6:15:25 PM Dec 17, 2013
	 * @return: TBHVNoPSDSDetailDTO
	 * @throws:
	 * @param staffId
	 * @return
	 */
	public TBHVNoPSDSDetailDTO getListShopSupervisorAndStaff(String staffId) {
		TBHVNoPSDSDetailDTO dto = new TBHVNoPSDSDetailDTO();

		SHOP_TABLE shopTable = new SHOP_TABLE(mDB);
		dto.listNPP = shopTable.getListShopOfTBHV(staffId);

		STAFF_TABLE staffTable = new STAFF_TABLE(mDB);
		dto.listGSBH = staffTable.getListGSBHForTBHV(staffId, true);
		dto.listNVBH = staffTable.getListNVBHForTBHV(staffId, true);

		return dto;
	}

	/**
	 * lấy danh sách khách hàng chưa duyệt
	 * 
	 * @author: duongdt3
	 * @since: 09:35:03 6 Jan 2014
	 * @return: NewCustomerListDTO
	 * @throws:
	 * @param staffId
	 * @param shopId
	 * @param customerName
	 * @param numItemInPage
	 * @param currentIndexSpinnerState
	 * @param currentPage
	 * @return
	 * @throws Exception
	 */
	public NewCustomerListDTO getListNewCustomer(String staffId, String shopId, String customerName, int numItemInPage, int currentIndexSpinnerState, int currentPage)
			throws Exception {
		CUSTOMER_TABLE cusTable = new CUSTOMER_TABLE(mDB);
		return cusTable.getListNewCustomer(staffId, shopId, customerName, numItemInPage, currentIndexSpinnerState, currentPage);
	}

	/**
	 * xoa avatar
	 * 
	 * @author: dungdq3
	 * @param: Tham số của hàm
	 * @return: long
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 * @date: Jan 4, 2014
	 */
	public long deleteAvatar(CustomerAvatarDTO customerAvatarDTO) {
		// TODO Auto-generated method stub
		CUSTOMER_AVATAR_TABLE cusAvatar = new CUSTOMER_AVATAR_TABLE(mDB);
		return cusAvatar.update(customerAvatarDTO);
	}

	/**
	 * cap nhat trang thai avatar
	 * 
	 * @author: dungdq3
	 * @param: CustomerAvatarDTO customerAvatarDTO
	 * @return: long
	 * @date: Jan 6, 2014
	 */
	public long updateAvatar(CustomerAvatarDTO customerAvatarDTO) {
		// TODO Auto-generated method stub
		CUSTOMER_AVATAR_TABLE cusAvatar = new CUSTOMER_AVATAR_TABLE(mDB);
		return cusAvatar.updateURL(customerAvatarDTO);
	}

	/**
	 * them avatar
	 * 
	 * @author: dungdq3
	 * @param: CustomerAvatarDTO customerAvatarDTO
	 * @return: long
	 * @date: Jan 6, 2014
	 */
	public long insertAvatar(CustomerAvatarDTO customerAvatarDTO) {
		// TODO Auto-generated method stub
		long returnCode = -1;
		try {
			if (customerAvatarDTO != null) {
				mDB.beginTransaction();
				CUSTOMER_AVATAR_TABLE customerAvatarTable = new CUSTOMER_AVATAR_TABLE(mDB);
				returnCode = customerAvatarTable.insertAvatar(customerAvatarDTO);
				if (returnCode > 0)
					mDB.setTransactionSuccessful();
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}
		return returnCode;
	}

	/**
	 * lay gia tri toi da avatar cua khach hang
	 * 
	 * @author: dungdq3
	 * @param: Bundle data
	 * @return: long
	 * @date: Jan 6, 2014
	 */
	public long getMaxCustomerAvatarID(Bundle data) {
		// TODO Auto-generated method stub
		long returnCode = -1;
		try {
			CUSTOMER_AVATAR_TABLE customerAvatarTable = new CUSTOMER_AVATAR_TABLE(mDB);
			returnCode = customerAvatarTable.getMaxCustomerAvatarID();
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return returnCode;
	}

	/**
	 * lấy thông tin khách hàng cho việc tạo khách hàng
	 * 
	 * @author: duongdt3
	 * @since: 14:23:56 7 Jan 2014
	 * @return: CustomerDTO
	 * @throws:
	 * @param customerId
	 * @return
	 * @throws Exception
	 */
	public CustomerDTO getCustomerByIdForCreateCustomer(String customerId)
			throws Exception {
		CUSTOMER_TABLE cusTable = new CUSTOMER_TABLE(mDB);
		return cusTable.getCustomerByIdForCreateCustomer(customerId);
	}

	/**
	 * lấy thông tin màn hình tạo mới khách hàng /**
	 * 
	 * @author: duongdt3
	 * @since: 09:34:47 6 Jan 2014
	 * @return: CreateCustomerInfoDTO
	 * @throws:
	 * @param staffId
	 * @param shopId
	 * @param customerId
	 * @return
	 * @throws Exception
	 */
	public CreateCustomerInfoDTO getCreateCustomerInfo(String staffId, String shopId, String customerId)
			throws Exception {
		CUSTOMER_TABLE cusTable = new CUSTOMER_TABLE(mDB);
		CreateCustomerInfoDTO dto = new CreateCustomerInfoDTO();
		dto.listC2 = cusTable.getListC2Customer(staffId, shopId, 0, false);
		// thêm 1 giá trị mặc định
		CustomerListItem item = new CustomerListItem();
		item.aCustomer.customerName = "";
		item.aCustomer.customerId = 0;
		dto.listC2.cusList.add(0, item);

		dto.listCusType = cusTable.getListCustomerType();

		long idDistrict = 0;
		long idProvine = 0;
		long idPrecinct = 0;

		if (!StringUtil.isNullOrEmpty(customerId)) {
			dto.cusInfo = cusTable.getCustomerByIdForCreateCustomer(customerId);
			if (dto.cusInfo != null) {
				// có thông tin khách hàng
				idPrecinct = dto.cusInfo.getAreaId();
				// lấy thông tin huyện, tỉnh của xã hiện tại
				// tìm 2 id này từ xã hiện tại
				idDistrict = cusTable.getParrentAreaId(idPrecinct);
				idProvine = cusTable.getParrentAreaId(idDistrict);

				// tìm id, vị trí hiện tại của C2
				dto.curentIdC2 = dto.cusInfo.getParentCustomerId();
				int currentIndexC2 = -1;
				for (int i = 0, size = dto.listC2.cusList.size(); i < size; i++) {
					if (dto.curentIdC2 == dto.listC2.cusList.get(i).aCustomer.customerId) {
						currentIndexC2 = i;
						break;
					}
				}
				dto.currentIndexC2 = currentIndexC2;

				// tìm id, vị trí hiện tại của C2
				dto.curentIdType = dto.cusInfo.getCustomerTypeId();
				int currentIndexType = -1;
				for (int i = 0, size = dto.listCusType.size(); i < size; i++) {
					if (dto.curentIdType == dto.listCusType.get(i).typeId) {
						currentIndexType = i;
						break;
					}
				}
				dto.currentIndexType = currentIndexType;
			}
		}

		// lấy danh sách Tỉnh
		dto.listProvine = cusTable.getListArea(CUSTOMER_TABLE.AREA_TYPE_PROVINE, 0);
		// lấy danh sách huyện của tỉnh index 0, hoặc index X (từ id lấy ra
		// index) nếu có
		// chưa lấy ra được id tỉnh
		if (idProvine <= 0) {
			// se lấy id đầu tiên
			if (dto.listProvine.size() > 0) {
				idProvine = dto.listProvine.get(0).areaId;
				dto.currentIndexProvince = 0;
			}
		} else {
			for (int i = 0, size = dto.listProvine.size(); i < size; i++) {
				if (idProvine == dto.listProvine.get(i).areaId) {
					dto.currentIndexProvince = i;
					break;
				}
			}
		}
		dto.listDistrict = cusTable.getListArea(CUSTOMER_TABLE.AREA_TYPE_DISTRICT, idProvine);
		// lấy danh sách phường xã của huyện index 0, hoặc index X (từ id lấy ra
		// index) nếu có
		// chưa lấy ra được id huyện
		if (idDistrict <= 0) {
			// se lấy id đầu tiên
			if (dto.listDistrict.size() > 0) {
				idDistrict = dto.listDistrict.get(0).areaId;
				dto.currentIndexDistrict = 0;
			}
		} else {
			for (int i = 0, size = dto.listDistrict.size(); i < size; i++) {
				if (idDistrict == dto.listDistrict.get(i).areaId) {
					dto.currentIndexDistrict = i;
					break;
				}
			}
		}
		dto.listPrecinct = cusTable.getListArea(CUSTOMER_TABLE.AREA_TYPE_PRECINCT, idDistrict);
		if (idPrecinct <= 0) {
			// se lấy id đầu tiên
			if (dto.listPrecinct.size() > 0) {
				idPrecinct = dto.listPrecinct.get(0).areaId;
				dto.currentIndexPrecinct = 0;
			}
		} else {
			for (int i = 0, size = dto.listPrecinct.size(); i < size; i++) {
				if (idPrecinct == dto.listPrecinct.get(i).areaId) {
					dto.currentIndexPrecinct = i;
					break;
				}
			}
		}
		// lưu các id 3 địa bàn
		dto.curentIdDistrict = idDistrict;
		dto.curentIdPrecinct = idPrecinct;
		dto.curentIdProvine = idProvine;

		return dto;
	}

	public CreateCustomerInfoDTO getAreaCustomerInfo(String areaCode)
			throws Exception {
		CUSTOMER_TABLE cusTable = new CUSTOMER_TABLE(mDB);
		CreateCustomerInfoDTO dto = new CreateCustomerInfoDTO();
		long idDistrict = 0;
		long idProvine = 0;
		long idPrecinct = 0;

		if (!StringUtil.isNullOrEmpty(areaCode)) {
			int areaId = cusTable.getGetAreaIdFromCode(areaCode);
			if (areaId != 0){
				// có thông tin khách hàng
				idPrecinct = areaId;
				// lấy thông tin huyện, tỉnh của xã hiện tại
				// tìm 2 id này từ xã hiện tại
				idDistrict = cusTable.getParrentAreaId(idPrecinct);
				idProvine = cusTable.getParrentAreaId(idDistrict);
			}
		}

		// lấy danh sách Tỉnh
		dto.listProvine = cusTable.getListArea(CUSTOMER_TABLE.AREA_TYPE_PROVINE, 0);
		// lấy danh sách huyện của tỉnh index 0, hoặc index X (từ id lấy ra
		// index) nếu có
		// chưa lấy ra được id tỉnh
		if (idProvine <= 0) {
			// se lấy id đầu tiên
			if (dto.listProvine.size() > 0) {
				idProvine = dto.listProvine.get(0).areaId;
				dto.currentIndexProvince = 0;
			}
		} else {
			for (int i = 0, size = dto.listProvine.size(); i < size; i++) {
				if (idProvine == dto.listProvine.get(i).areaId) {
					dto.currentIndexProvince = i;
					break;
				}
			}
		}
		dto.listDistrict = cusTable.getListArea(CUSTOMER_TABLE.AREA_TYPE_DISTRICT, idProvine);
		// lấy danh sách phường xã của huyện index 0, hoặc index X (từ id lấy ra
		// index) nếu có
		// chưa lấy ra được id huyện
		if (idDistrict <= 0) {
			// se lấy id đầu tiên
			if (dto.listDistrict.size() > 0) {
				idDistrict = dto.listDistrict.get(0).areaId;
				dto.currentIndexDistrict = 0;
			}
		} else {
			for (int i = 0, size = dto.listDistrict.size(); i < size; i++) {
				if (idDistrict == dto.listDistrict.get(i).areaId) {
					dto.currentIndexDistrict = i;
					break;
				}
			}
		}
		dto.listPrecinct = cusTable.getListArea(CUSTOMER_TABLE.AREA_TYPE_PRECINCT, idDistrict);
		if (idPrecinct <= 0) {
			// se lấy id đầu tiên
			if (dto.listPrecinct.size() > 0) {
				idPrecinct = dto.listPrecinct.get(0).areaId;
				dto.currentIndexPrecinct = 0;
			}
		} else {
			for (int i = 0, size = dto.listPrecinct.size(); i < size; i++) {
				if (idPrecinct == dto.listPrecinct.get(i).areaId) {
					dto.currentIndexPrecinct = i;
					break;
				}
			}
		}
		// lưu các id 3 địa bàn
		dto.curentIdDistrict = idDistrict;
		dto.curentIdPrecinct = idPrecinct;
		dto.curentIdProvine = idProvine;

		return dto;
	}

	/**
	 * Get list area from parrentAreaId
	 * 
	 * @author: duongdt3
	 * @since: 15:44:06 6 Jan 2014
	 * @return: ArrayList<AreaItem>
	 * @throws:
	 * @param parentAreaId
	 * @param typeArea
	 *            {AREA_TYPE_PROVINE, AREA_TYPE_DISTRICT, AREA_TYPE_PRECINCT}
	 * @return
	 * @throws Exception
	 */
	public ArrayList<AreaItem> getCreateCustomerAreaInfo(long parentAreaId, int typeArea)
			throws Exception {
		CUSTOMER_TABLE cusTable = new CUSTOMER_TABLE(mDB);
		ArrayList<AreaItem> dto = new ArrayList<AreaItem>();
		dto = cusTable.getListArea(typeArea, parentAreaId);
		return dto;
	}

	/**
	 * @author: dungdq3
	 * @since: 10:25:55 AM Mar 7, 2014
	 * @return: CustomerListDTO
	 * @throws:
	 * @param staffId
	 * @param shopId
	 * @param visit_plan
	 * @param isGetWrongPlan
	 * @param page
	 * @param isGetTotalPage
	 * @param type
	 * @return
	 */
	public CustomerListDTO getListCustomerForOpponentSale(long staffId, long shopId, long cusId, String dataSearch, String visit_plan, boolean isGetWrongPlan, int page, boolean isGetTotalPage, int type, int typeRequest) {
		// TODO Auto-generated method stub
		CustomerListDTO dto = null;
		CUSTOMER_TABLE custommerTable = new CUSTOMER_TABLE(mDB);
		try {

			if (typeRequest == 0) {
				// Lay ca 2: danh sach khach hang trong tuyen va khach hang cua
				// c2
				dto = custommerTable.getListCustomerOnPlanAndOfC2(staffId, shopId, dataSearch, visit_plan, isGetWrongPlan, page, isGetTotalPage, type);
			} else if (typeRequest == 1) {
				// Lay danh sach khach hang trong tuyen
				dto = custommerTable.getListCustomerForOpponentSale(staffId, shopId, dataSearch, visit_plan, isGetWrongPlan, page, isGetTotalPage, type);
			} else if (typeRequest == 2) {
				// Lay danh sach khach hang cua c2
				dto = custommerTable.getListCustomerOfC2(staffId, shopId, cusId, dataSearch, page, isGetTotalPage, type);
			}
		} catch (Exception e) {
		} finally {
		}
		return dto;
	}

	/**
	 * Lay thong tin dinh nghia co' hien thi gia hay khong
	 * 
	 * @author: dungnt19
	 * @since: 14:24:49 10-03-2014
	 * @return: int
	 * @throws:
	 * @param bundle
	 * @return
	 */
	public int getIsShowPriceDefine(Bundle bundle) {
		int result = 1;
		SHOP_TABLE shopTable = new SHOP_TABLE(mDB);

		try {
			result = shopTable.getIsShowPriceDefince(bundle.getString(IntentConstants.INTENT_SHOP_ID));
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
		}
		return result;
	}

	/**
	 * Lay thong tin chi tiet CT HTBH
	 * 
	 * @author: dungnt19
	 * @since: 09:26:28 08-05-2014
	 * @return: SaleSupportProgramDetailModel
	 * @throws:
	 * @param programId
	 * @return
	 */
	public SaleSupportProgramDetailModel getSaleSupportProgramDetail(long programId) {
		PRO_INFO_TABLE proInfoTable = new PRO_INFO_TABLE(mDB);
		SaleSupportProgramDetailModel model = proInfoTable.getSaleSupportProgramDetail(programId);
		return model;
	}

	/**
	 * 
	 * @author: dungnt19
	 * @since: 1.0
	 * @return: SaleSupportProgramDetailModel
	 * @throws:
	 * @param programId
	 * @return
	 */
	public int getSaleSupportProgramLevel(long programId) {
		PRO_INFO_TABLE proInfoTable = new PRO_INFO_TABLE(mDB);
		int numLevel = proInfoTable.getSaleSupportProgramLevel(programId);
		return numLevel;
	}

	/**
	 * @author: dungnt19
	 * @since: 1.0
	 * @return: SaleSupportProgramDetailModel
	 * @throws:
	 * @param bundle
	 * @return
	 */
	public CustomerAttendProgramListDTO getCustomerListAttendProgram(Bundle bundle) {
		PRO_INFO_TABLE proInfoTable = new PRO_INFO_TABLE(mDB);
		CustomerAttendProgramListDTO dto = proInfoTable.getCustomerListAttendProgram(bundle);
		return dto;
	}

	/**
	 * Danh sach khach hang can add vao chuong trinh htbh
	 * 
	 * @author: hoanpd1
	 * @since: 1.0
	 * @return: CustomerAttendProgramListDTO
	 * @throws:
	 * @param bundle
	 * @return
	 */
	public CustomerAttendProgramListDTO getAddCustomerList(Bundle bundle) {
		PRO_INFO_TABLE proInfoTable = new PRO_INFO_TABLE(mDB);
		CustomerAttendProgramListDTO dto = proInfoTable.getAddCustomerList(bundle);
		return dto;
	}

	/**
	 * Luu danh sach khach hang duoc chon de tham gia chuong trinh HTBH
	 * 
	 * @author: hoanpd1
	 * @since: 1.0
	 * @return: long
	 * @throws:
	 * @return
	 */

	public ProCusAttendDTO saveCustomerAttend(CustomerAttendProgramListDTO dto) {
		ProCusAttendDTO cusAttendto = new ProCusAttendDTO();
		ProCusMapDTO tempProCusMapDTO = null;
		ProCusHistoryDTO tempProCusHisDTO = null;
		TABLE_ID tableId = new TABLE_ID(mDB);
		long maxProCusMapId = tableId.getMaxId(PRO_CUS_MAP_TABLE.PRO_CUS_MAP_TABLE);
		long maxProHistoryId = tableId.getMaxId(PRO_CUS_HISTORY_TABLE.PRO_CUS_HISTORY_TABLE);

		PRO_CUS_MAP_TABLE cusMap = new PRO_CUS_MAP_TABLE(mDB);
		PRO_CUS_HISTORY_TABLE cusHistory = new PRO_CUS_HISTORY_TABLE(mDB);
		mDB.beginTransaction();
		try {
			for (int i = 0; i < dto.cusList.size(); i++) {
				long customerId = dto.cusList.get(i).customerId;
				tempProCusMapDTO = cusMap.saveCustomerAttend(maxProCusMapId, dto.programID, dto.staffId, dto.staffCode, dto.day, customerId, dto.level);
				if (tempProCusMapDTO == null) {
					break;
				} else {
					cusAttendto.lisCusMapDto.add(tempProCusMapDTO);
				}
				tempProCusHisDTO = cusHistory.saveCustomerAttendHistory(maxProHistoryId, maxProCusMapId, dto.staffId, dto.staffCode, dto.day, dto.type);
				if (tempProCusHisDTO == null) {
					break;
				} else {
					cusAttendto.listCusHisDto.add(tempProCusHisDTO);
				}
				maxProCusMapId++;
				maxProHistoryId++;
			}
			if (tempProCusMapDTO != null && tempProCusHisDTO != null) {
				mDB.setTransactionSuccessful();
			}
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}

		return cusAttendto;
	}

	/**
	 * Xoa thong tin tham gia chuong trinh HTBH cua 1 KH
	 * 
	 * @author: quangvt1
	 * @since: 15:12:50 19-05-2014
	 * @return: boolean
	 * @throws:
	 * @param pro_cus_map_id
	 * @return
	 */
	public boolean deleteCustomerJoinHTBH(String pro_cus_map_id) {
		PRO_CUS_MAP_TABLE table = new PRO_CUS_MAP_TABLE(mDB);
		boolean isSuccess = table.deleteCustomerJoinHTBH(pro_cus_map_id);
		return isSuccess;
	}

	/**
	 * Lay danh sach san pham de dang ki san luong tham gia
	 * 
	 * @author: quangvt1
	 * @since: 17:01:37 20-05-2014
	 * @return: ListProductQuantityJoin
	 * @throws:
	 * @param pro_info_id
	 * @param customer_id
	 * @param level
	 * @return
	 */
	public ListProductQuantityJoin getListProductForJoin(long pro_info_id, long customer_id, String level) {
		PRO_INFO_TABLE table = new PRO_INFO_TABLE(mDB);
		ListProductQuantityJoin result = table.getListProductForJoin(pro_info_id, customer_id, level);
		return result;
	}

	/**
	 * Luu thong tin san luong ban
	 * 
	 * @author: quangvt1
	 * @since: 16:25:22 21-05-2014
	 * @return: long
	 * @throws:
	 * @return
	 */
	public boolean saveDoneProduct(CustomerInputQuantityDTO customer, String staffId, String staffCode) {
		PRO_CUS_PROCESS_TABLE table = new PRO_CUS_PROCESS_TABLE(mDB);
		boolean isSuccess = true;

		mDB.beginTransaction();
		try {
			// Luu thong tin san luong ban
			isSuccess = table.saveDoneProduct(customer, staffCode);

			// Kiem tra chu ki co dung 1 thang khong
			if (isSuccess && customer.isFullMonth) {
				OP_SALE_VOLUME_TABLE opSaleVolume = new OP_SALE_VOLUME_TABLE(mDB);
				isSuccess = opSaleVolume.saveSaleVolumeFromHTBH(customer, staffId, staffCode, 1);
			}

			if (isSuccess) {
				// Tao hashmap tung san pham vs san luong thuc hien
				Map<Long, Long> listProductInput = getListValueProductInputDone(customer);
				CustomerInputQuantityDTO cusSamePeriod = table.getListProductSamePreriod(customer, listProductInput);

				if (cusSamePeriod.details.size() > 0) {
					customer.cusSamePeriod = cusSamePeriod;
					isSuccess = table.saveDoneProduct(cusSamePeriod, staffCode);
				}
			}

			if (isSuccess) {
				mDB.setTransactionSuccessful();
			}

		} catch (Exception ex) {
			isSuccess = false;
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}

		return isSuccess;
	}

	/**
	 * Tao HashMap san pham vs san luong thuc hien
	 * 
	 * @author: quangvt1
	 * @since: 09:48:45 25-06-2014
	 * @return: HashMap<Long,Integer>
	 * @throws:
	 * @param customer
	 * @return
	 */
	private Map<Long, Long> getListValueProductInputDone(CustomerInputQuantityDTO customer) {
		Map<Long, Long> listProductInput = new HashMap<Long, Long>();
		for (CustomerInputQuantityDetailDTO detail : customer.details) {
			listProductInput.put(detail.PRODUCT_ID, detail.NEWQUANTITY);
		}

		return listProductInput;
	}

	/**
	 * Luu thong tin dang ki san luong tham gia
	 * 
	 * @author: quangvt1
	 * @since: 16:25:22 21-05-2014
	 * @return: long
	 * @throws:
	 * @param data
	 * @return
	 */
	public boolean saveJoinProduct(Bundle data) {
		boolean isChangeLevel = data.getBoolean(IntentConstants.INTENT_CHANGE_LEVEL);
		ListProductQuantityJoin productList = (ListProductQuantityJoin) data.get(IntentConstants.INTENT_PRODUCT_LIST);
		int newLevel = data.getInt(IntentConstants.INTENT_NEW_LEVEL);
		String staffId = data.getString(IntentConstants.INTENT_STAFF_ID);
		String staffCode = data.getString(IntentConstants.INTENT_STAFF_CODE);

		PRO_CUS_MAP_TABLE table = new PRO_CUS_MAP_TABLE(mDB);
		PRO_CUS_MAP_DETAIL_TABLE tableDetail = new PRO_CUS_MAP_DETAIL_TABLE(mDB);
		boolean isSuccess = true;

		mDB.beginTransaction();
		try {
			// Cap nhat lai level
			if (isChangeLevel) {
				isSuccess = table.updateLevelOfCustomerJoinHTBH(productList.pro_cus_map_id, newLevel, staffCode);

				if (isSuccess) {
					// Xoa cac thong tin tham gia cu~
					isSuccess = tableDetail.deleteDetailProCusMap(productList.pro_cus_map_id);
				}
			}

			if (isSuccess) {
				isSuccess = tableDetail.saveChangeDetail(productList.pro_cus_map_id, productList, staffId, staffCode);
			}

			if (isSuccess) {
				mDB.setTransactionSuccessful();
			}

		} catch (Exception ex) {
			isSuccess = false;
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}

		return isSuccess;
	}

	/**
	 * Lay thong tin chi tiet cua chuong trinh
	 * 
	 * @author: quangvt1
	 * @since: 14:13:48 23-05-2014
	 * @return: ProductDTO
	 * @throws:
	 * @param pro_info_id
	 * @return
	 */
	public ProInfoDTO getDetailPrograme(long pro_info_id) {
		PROMOTION_PROGRAME_TABLE table = new PROMOTION_PROGRAME_TABLE(mDB);
		PRO_STRUCTURE_TABLE tbProStructure = new PRO_STRUCTURE_TABLE(mDB);
		ProInfoDTO proInfo = table.getDetailPrograme(pro_info_id + "");
		proInfo.listLevel = tbProStructure.getListLevel(pro_info_id + "");
		return proInfo;
	}

	/**
	 * Lay thong tin chi tiet cua chuong trinh
	 * 
	 * @author: quangvt1
	 * @since: 14:13:48 23-05-2014
	 * @return: ProductDTO
	 * @throws:
	 * @param pro_info_id
	 * @return
	 */
	public ProInfoDTO getDetailProgrameDone(long pro_info_id) {
		PROMOTION_PROGRAME_TABLE table = new PROMOTION_PROGRAME_TABLE(mDB);
		PRO_STRUCTURE_TABLE tbProStructure = new PRO_STRUCTURE_TABLE(mDB);
		ProInfoDTO proInfo = table.getDetailPrograme(pro_info_id + "");
		proInfo.listPeriod = table.getListProPeriodDone(pro_info_id + "");
		proInfo.listLevel = tbProStructure.getListLevel(pro_info_id + "");

		return proInfo;
	}

	/**
	 * Lay danh sach chuong trinh cap nhap san luong ban
	 * 
	 * @author: quangvt1
	 * @since: 08:18:09 12-06-2014
	 * @return: ProInfoDTO
	 * @throws:
	 * @return
	 */
	public SaleSupportProgramModel getAllProgrameNeedTypeQuantity(String shopid) {
		PROMOTION_PROGRAME_TABLE table = new PROMOTION_PROGRAME_TABLE(mDB);
		return table.getAllProgrameNeedTypeQuantity(shopid);
	}

	/**
	 * Lay danh sach khach hang xong 1 chu ki tinh thuong Lay ve de tien hanh
	 * nhap san luong ban cho chu ki do.
	 * 
	 * @author: quangvt1
	 * @since: 15:12:12 13-06-2014
	 * @return: SaleSupportProgramModel
	 * @throws:
	 * @param data
	 * @return
	 */
	public CustomerListDoneProgrameDTO getListCustomerDonePrograme(Bundle data) {
		PROMOTION_PROGRAME_TABLE table = new PROMOTION_PROGRAME_TABLE(mDB);
		return table.getListCustomerDonePrograme(data);
	}

	/**
	 * Lay danh sach san pham can nhap thuc hien cua khach hang
	 * 
	 * @author: hoanpd1
	 * @since: 19:52:20 07-08-2014
	 * @return: CustomerInputQuantityDTO
	 * @throws:
	 * @param data
	 * @return
	 */
	public CustomerInputQuantityDTO getListProductDonePrograme(Bundle data) {
		PROMOTION_PROGRAME_TABLE table = new PROMOTION_PROGRAME_TABLE(mDB);
		return table.getListProductDonePrograme(data);
	}
	/**
	 * Khoi tao danh sach ung dung duoc su dung Tablet
	 * @author: banghn
	 * @param ext
	 */
	public void initApplicationGuard(Bundle ext) {
		OTHER_APP_RULE appRule = new OTHER_APP_RULE(mDB);
		STAFF_TABLE staffTable = new STAFF_TABLE(mDB);
		try {
			GlobalInfo.getInstance().getProfile().getUserData().checkAccessApp = staffTable.checkAccessApp();
			appRule.initApplicationGuard(ext);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {}
	}

	/**
	 * Lay log vi tri cuoi cung dong bo len server
	 * @author:
	 * @return
	 */
	public StaffPositionLogDTO getAttendancePosition() {
		STAFF_POSITION_LOG_TABLE posLog = new STAFF_POSITION_LOG_TABLE(mDB);
		WORK_LOG_TABLE workLogTable = new WORK_LOG_TABLE(mDB);
		WorkLogDTO workLogDTO = null;
		workLogDTO = workLogTable.getWorkLog();
		StaffPositionLogDTO result = new StaffPositionLogDTO();
		if(workLogDTO != null){
			result.createDate = workLogDTO.workDate;
			result.workLogId = workLogDTO.workLogId;
			result.workLogDTO = null;
		}else{
			result = posLog.getAttendancePosition();
		}
		return result;
	}
	/**
	 * Them du lieu cham cong
	 * @param workLog
	 * @return
     */
	public boolean insertAttendancePosition(WorkLogDTO workLog) {
		boolean result = false;
		WORK_LOG_TABLE workLogTable = new WORK_LOG_TABLE(mDB);
		WorkLogDTO workLogDTO = null;
		workLogDTO = workLogTable.getWorkLog();
		if(workLogDTO == null){
			insertWorkLog(workLog);
			result = true;
		}
		return result;
	}
	/**
	 * Insert work log
	 * @param dto
	 * @return
     */
	public long insertWorkLog(WorkLogDTO dto) {
		long returnCode = -1;
		if (dto != null) {
			try {
				mDB.beginTransaction();
				TABLE_ID tableId = new TABLE_ID(mDB);
				dto.workLogId = tableId.getMaxId(WORK_LOG_TABLE.WORK_LOG_TABLE);
				WORK_LOG_TABLE actionTable = new WORK_LOG_TABLE(mDB);
				returnCode = actionTable.insert(dto);
				mDB.setTransactionSuccessful();
			} finally {
				if (mDB != null && mDB.inTransaction()) {
					mDB.endTransaction();
				}
			}
		}
		return returnCode;
	}
	/**
	 * Lay log vi tri cuoi cung dong bo len server
	 * @author:
	 * @return
	 */
	public StaffPositionLogDTO getLastPosition() {
		STAFF_POSITION_LOG_TABLE posLog = new STAFF_POSITION_LOG_TABLE(mDB);
		StaffPositionLogDTO result = posLog.getLastPosition();
		return result;
	}

	public void getAreaCustomerInfo(CreateCustomerInfoDTO dto, String areaCode) throws Exception {
		CUSTOMER_TABLE cusTable = new CUSTOMER_TABLE(mDB);
		long idDistrict = 0;
		long idProvine = 0;
		long idPrecinct = 0;

		if (!StringUtil.isNullOrEmpty(areaCode)) {
			int areaId = cusTable.getGetAreaIdFromCode(areaCode);
			if (areaId != 0){
				// có thông tin khách hàng
				idPrecinct = areaId;
				// lấy thông tin huyện, tỉnh của xã hiện tại
				// tìm 2 id này từ xã hiện tại
				idDistrict = cusTable.getParrentAreaId(idPrecinct);
				idProvine = cusTable.getParrentAreaId(idDistrict);
			}
		}

		// lấy danh sách Tỉnh
		dto.listProvine = cusTable.getListArea(CUSTOMER_TABLE.AREA_TYPE_PROVINE, 0);
		// lấy danh sách huyện của tỉnh index 0, hoặc index X (từ id lấy ra
		// index) nếu có
		// chưa lấy ra được id tỉnh
		if (idProvine <= 0) {
			// se lấy id đầu tiên
			if (dto.listProvine.size() > 0) {
				idProvine = dto.listProvine.get(0).areaId;
				dto.currentIndexProvince = 0;
			}
		} else {
			for (int i = 0, size = dto.listProvine.size(); i < size; i++) {
				if (idProvine == dto.listProvine.get(i).areaId) {
					dto.currentIndexProvince = i;
					break;
				}
			}
		}
		dto.listDistrict = cusTable.getListArea(CUSTOMER_TABLE.AREA_TYPE_DISTRICT, idProvine);
		// lấy danh sách phường xã của huyện index 0, hoặc index X (từ id lấy ra
		// index) nếu có
		// chưa lấy ra được id huyện
		if (idDistrict <= 0) {
			// se lấy id đầu tiên
			if (dto.listDistrict.size() > 0) {
				idDistrict = dto.listDistrict.get(0).areaId;
				dto.currentIndexDistrict = 0;
			}
		} else {
			for (int i = 0, size = dto.listDistrict.size(); i < size; i++) {
				if (idDistrict == dto.listDistrict.get(i).areaId) {
					dto.currentIndexDistrict = i;
					break;
				}
			}
		}
		dto.listPrecinct = cusTable.getListArea(CUSTOMER_TABLE.AREA_TYPE_PRECINCT, idDistrict);
		if (idPrecinct <= 0) {
			// se lấy id đầu tiên
			if (dto.listPrecinct.size() > 0) {
				idPrecinct = dto.listPrecinct.get(0).areaId;
				dto.currentIndexPrecinct = 0;
			}
		} else {
			for (int i = 0, size = dto.listPrecinct.size(); i < size; i++) {
				if (idPrecinct == dto.listPrecinct.get(i).areaId) {
					dto.currentIndexPrecinct = i;
					break;
				}
			}
		}
		// lưu các id 3 địa bàn
		dto.curentIdDistrict = idDistrict;
		dto.curentIdPrecinct = idPrecinct;
		dto.curentIdProvine = idProvine;
	}

	/**
	 * Lay danh sach don hang, san pham cho PG
	 * @param ext
	 * @return
     */
	public ListOrderViewDTO requestGetOrderView(Bundle ext) {
		PG_SALE_ORDER_DETAIL_TABLE saleOrderDetailTable = new PG_SALE_ORDER_DETAIL_TABLE(mDB);
		ListOrderViewDTO dto = new ListOrderViewDTO();
		try {
			CustomerDTO customerDTO = saleOrderDetailTable.requestCustomerVisited(ext);
			dto = saleOrderDetailTable.requestGetOrderView(ext, customerDTO.customerId);
			dto.customerDTO = customerDTO;
			dto.customerDTOVisiting = saleOrderDetailTable.requestCustomerVisiting(ext);
			dto.isCheckPlanLine = saleOrderDetailTable.checkPlanLine(ext);
			dto.pgCustomerViewDTO = saleOrderDetailTable.requestPGSaleOrderView(ext, customerDTO.customerId);
			boolean isFirstLoadProduct = ext.getBoolean(IntentConstants.INTENT_IS_FIRST_LOAD_PRODUCT_PG);
			if(isFirstLoadProduct) {
				dto.listAllOrder = saleOrderDetailTable.requestALLPGSaleOrderView(ext, customerDTO.customerId);
			}else{
				dto.listAllOrder = new ArrayList<PGOrderViewDTO>();
			}
			return dto;
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			return null;
		}
	}

	/**
	 * Tao don hang pg
	 * @param dto
	 * @return
     */
	public ListOrderViewDTO createPGSaleOrder(ListOrderViewDTO dto) {
		ListOrderViewDTO orderViewDTO = new ListOrderViewDTO();
		orderViewDTO = dto;
		long pgSaleOrderId = -1;
		long pgSaleOrderDetailId = -1;
		long result = -1;
		PG_SALE_ORDER_TABLE pgSaleOrderTable = new PG_SALE_ORDER_TABLE(mDB);
		PG_SALE_ORDER_DETAIL_TABLE pgSaleOrderDetailTable = new PG_SALE_ORDER_DETAIL_TABLE(mDB);
		TABLE_ID tableId = new TABLE_ID(mDB);
		try {
			mDB.beginTransaction();
			long updatePGSaleOrder = 0;
			if(orderViewDTO.pgOrderUpdateDTO.pgSaleOrderId > 0) {
				updatePGSaleOrder = pgSaleOrderTable.update(dto.pgOrderUpdateDTO);
			}else{
				updatePGSaleOrder = 1;
			}
			if(updatePGSaleOrder > 0 && orderViewDTO.saleOrderDTO != null) {
				pgSaleOrderId = tableId.getMaxId(PG_SALE_ORDER_TABLE.PG_SALE_ORDER_TABLE);
				pgSaleOrderDetailId = tableId.getMaxId(PG_SALE_ORDER_DETAIL_TABLE.PG_SALE_ORDER_DETAIL_TABLE);
				orderViewDTO.saleOrderDTO.pgSaleOrderId = pgSaleOrderId;
				long insertPGSaleOrder = pgSaleOrderTable.insert(orderViewDTO.saleOrderDTO);
				if (insertPGSaleOrder > 0) {
					for (int i = 0; i < orderViewDTO.listSaleOrderDetailDTO.size(); i++) {
						orderViewDTO.listSaleOrderDetailDTO.get(i).pgSaleOrderDetailId = pgSaleOrderDetailId++;
						orderViewDTO.listSaleOrderDetailDTO.get(i).pgSaleOrderId = pgSaleOrderId;
						long insertPGOrderDetail = pgSaleOrderDetailTable.insert(orderViewDTO.listSaleOrderDetailDTO.get(i));
						if (insertPGOrderDetail > 0) {
							result = i + 1;
						}
					}
					if (result == orderViewDTO.listSaleOrderDetailDTO.size()) {
						dto.insertSuccess = true;
					}
				}
			}else{
				dto.insertSuccess = true;
			}
			mDB.setTransactionSuccessful();
		} catch (Exception e) {
			dto.insertSuccess = false;
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}
		return orderViewDTO;
	}
	/**
	 * Lay danh sach bao cao don hang, san pham cho PG
	 * @param ext
	 * @return
	 */
	public ListOrderViewDTO requestGetReportOrderView(Bundle ext) {
		PG_SALE_ORDER_DETAIL_TABLE saleOrderDetailTable = new PG_SALE_ORDER_DETAIL_TABLE(mDB);
		ListOrderViewDTO dto = new ListOrderViewDTO();
		try {
			dto = saleOrderDetailTable.requestGetReportOrderView(ext);
			dto.customerDTOVisiting = saleOrderDetailTable.requestCustomerReportVisiting(ext);
			return dto;
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			return null;
		}
	}

	/**
	 * Lấy danh sách khách hàng màn hình huấn luyện GSNPP, GSBH
	 * @param ownerId
	 * @param shopId
	 * @param staffId
	 * @param code
	 * @param nameAddress
	 * @param visitPlan
     * @param page
     * @return
     */
	public CustomerListDTO requestGetCustomerSaleListTraing(String id, String shopIdParent, String ownerId, String shopId, String staffId, String code,
															String nameAddress, String visitPlan, int page, boolean isAll) {
		CustomerListDTO dto = null;
		CUSTOMER_TABLE custommerTable = new CUSTOMER_TABLE(mDB);
		try {
			dto = custommerTable.requestGetCustomerSaleListTraining(id, shopIdParent, ownerId, shopId, staffId, code, nameAddress, visitPlan, page, isAll);
		} catch (Exception e) {
		} finally {
		}
		return dto;
	}

	/**
	 * Lấy danh sách nhân viên bán hàng màn hình huấn luyện
	 * @param shopId
	 * @param staffOwnerId
     * @return
     */
	public ListStaffDTO getTrainingListNVBH(long shopId, String staffOwnerId) {
		STAFF_TABLE table = new STAFF_TABLE(mDB);
		return table.getTrainingListNVBH(shopId, staffOwnerId);
	}

	/**
	 * Xoa du lieu khong thoa dieu kien cham cong
	 * @return
     */
	public ArrayList<WorkLogDTO> deleteWorkLog(long workLogIdNotDelete) {
		ArrayList<WorkLogDTO> result = new ArrayList<>();
		ArrayList<WorkLogDTO> listWorkLog = new ArrayList<>();
		try {
			mDB.beginTransaction();
			WORK_LOG_TABLE workLogTable = new WORK_LOG_TABLE(mDB);
			listWorkLog = workLogTable.getWorkLogDelete(workLogIdNotDelete);
			if (listWorkLog.size() > 0) {
				int value = 0;
				for (WorkLogDTO workLogDTO : listWorkLog) {
					long workLogId = workLogTable.deleteWorkLog(workLogDTO);
					if (workLogId > 0) {
						value = value + 1;
					}
				}
				if (value == listWorkLog.size()) {
					result = listWorkLog;
				}
			}
			mDB.setTransactionSuccessful();
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
		}
		return result;
	}
}
