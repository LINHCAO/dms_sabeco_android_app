/**
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Map.Entry;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.download.ExternalStorage;
import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.view.main.LoginView;

public class SqlCipherUtil {
	/**
	 * Ma hoa File DB
	 * 
	 * @author: ThangNV31
	 * @return: void
	 * @throws:
	 * @param fileName
	 */
	public static void encryptPlaintextDB(String fileName, Context context) throws Exception {
		File databaseFile = new File(fileName);
		if (databaseFile.exists()) {
			SQLiteDatabase.loadLibs(context);
			SQLiteDatabase mDB;
			mDB = SQLiteDatabase.openDatabase(fileName, "", null, SQLiteDatabase.OPEN_READWRITE);
			if (mDB == null) {
				ServerLogger.sendLogLogin("encryptPlaintextDB", "Cannot open database" , TabletActionLogDTO.LOG_LOGIN);
				throw new Exception("Cannot open database: " + fileName);
			}
            String tempPathDb = ExternalStorage.getFileDBPath(context).getPath() + "/" + System.currentTimeMillis();
            String realPathDb = ExternalStorage.getFileDBPath(context).getPath() + "/" + Constants.DATABASE_NAME;
            File tempEncryptedDB = new File(tempPathDb);
            File realEncryptedDB = new File(realPathDb);
            if (realEncryptedDB.exists()) {
                  realEncryptedDB.delete();
            }
			if (!tempEncryptedDB.exists()) {
				tempEncryptedDB.createNewFile();
			}
			String firstCommand = String.format("ATTACH DATABASE '%s' as encrypted KEY '%s'", tempEncryptedDB.getAbsoluteFile(), Constants.CIPHER_KEY);
			mDB.rawExecSQL(firstCommand);
			mDB.rawExecSQL("SELECT sqlcipher_export('encrypted')");
			mDB.rawExecSQL("DETACH DATABASE encrypted");
//			mDB.rawExecSQL(String.format("ATTACH DATABASE '%s' AS encrypted KEY '%s'",tempPathDb, Constants.CIPHER_KEY));
//			mDB.rawExecSQL("PRAGMA encrypted.cipher = 'rc4'");
//			mDB.rawExecSQL("SELECT sqlcipher_export('encrypted')");
//			mDB.rawExecSQL("DETACH DATABASE encrypted");
			mDB.close();
			databaseFile.delete();
			boolean isRenameSusscess = tempEncryptedDB.renameTo(realEncryptedDB);
			if (!isRenameSusscess) {
				ServerLogger.sendLogLogin("encryptPlaintextDB", "Encrypted DB fail" , TabletActionLogDTO.LOG_LOGIN);
				throw new Exception("Encrypted DB fail");
			}
		} else {
			throw new FileNotFoundException(fileName + " does not exists");
		}
	}

	/**
	 * bat buoc dung SecurePreference
	 * 
	 * @author: ThangNV31
	 * @return: void
	 * @throws:
	 * @param context
	 */
	public static void ensureSecurePreference(Context context) {
		SharedPreferences sharedpreferences = context.getSharedPreferences(LoginView.PREFS_PRIVATE, Context.MODE_PRIVATE);
		if (sharedpreferences.contains(LoginView.DMS_ID)) {
			Map<String, ?> map = sharedpreferences.getAll();
			sharedpreferences.edit().clear().commit();
			SharedPreferences securePreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
			Editor editor = securePreferences.edit();
			for (Entry<String, ?> entry : map.entrySet()) {
				editor.putString(entry.getKey(), "" + entry.getValue());
			}
			editor.commit();
			return;
		}
		GlobalInfo.getInstance().getDmsPrivateSharePreference();
		GlobalInfo.getInstance().getTimeSharePreference();
	}
	/**
	 * mã hoá file db
	 * @author: duongdt3
	 * @since: 09:19:00 29 Jan 2015
	 * @return: void
	 * @throws:  
	 * @param filePath
	 * @param decrytFilePath
	 * @throws Exception
	 */
	public static void decryptCiphertextDB(File databaseFile, String decrytFilePath)
			throws Exception {
		String filePath = databaseFile != null ? databaseFile.getPath() : "unknown";
		if (databaseFile.exists()) {
			SQLiteDatabase mDB;
//			SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
//				@Override
//				public void preKey(SQLiteDatabase db) {
//					db.rawExecSQL(String.format("PRAGMA key = '%s'",
//							Constants.CIPHER_KEY));
//					db.rawExecSQL("PRAGMA cipher = 'rc4'");
//				}
//
//				@Override
//				public void postKey(SQLiteDatabase db) {
//					db.rawExecSQL(String.format("PRAGMA key = '%s'",
//							Constants.CIPHER_KEY));
//					db.rawExecSQL("PRAGMA cipher = 'rc4'");
//				}
//			};
//			mDB = SQLiteDatabase.openOrCreateDatabase(databaseFile,
//					Constants.CIPHER_KEY, null, hook);
			mDB = SQLiteDatabase.openOrCreateDatabase(databaseFile,
					Constants.CIPHER_KEY, null, null);

			if (mDB == null) {
				throw new Exception("Cannot open database: " + filePath);
			}
			File decryptDB = new File(decrytFilePath);
			if (decryptDB.exists()) {
				decryptDB.delete();
			}
			mDB.rawExecSQL(String.format("PRAGMA key = '%s'",
					Constants.CIPHER_KEY));
//			mDB.rawExecSQL(String.format("PRAGMA cipher = '%s'", "rc4"));

			mDB.rawExecSQL(String.format(
					"ATTACH DATABASE '%s' AS plaintext KEY ''",
					decryptDB.getAbsolutePath()));
			mDB.rawExecSQL("SELECT sqlcipher_export('plaintext')");
			mDB.rawExecSQL("DETACH DATABASE plaintext");
			mDB.close();
		} else {
			throw new FileNotFoundException("Decrypt " + filePath + " does not exists");
		}
	}
}
