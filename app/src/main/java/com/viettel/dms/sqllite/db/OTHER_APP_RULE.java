/**
 * Copyright 2015 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.sqllite.db;

import java.util.ArrayList;

import net.sqlcipher.database.SQLiteDatabase;
import android.database.Cursor;
import android.os.Bundle;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.AppRuleDTO;
import com.viettel.dms.global.GlobalInfo;

/**
 * Bang luu tru danh sach ung ung duoc su dung duoi MTB
 * @author: BangHN
 * @version: 1.0
 * @since: 1.0
 */
public class OTHER_APP_RULE extends ABSTRACT_TABLE {
	//danh sach ten cot
	public static final String ID = "ID";
	public static final String APP_TYPE = "APP_TYPE";
	public static final String APP_ID = "APP_ID";
	public static final String USER_TYPE = "USER_TYPE";
	public static final String USER_ID = "USER_ID";
	public static final String RULE_TYPE = "RULE_TYPE";
	public static final String STATUS = "STATUS";
	
	private static final String TABLE_APP_PARAM = "OTHER_APP_RULE";

	public OTHER_APP_RULE(SQLiteDatabase mDB) {
		this.tableName = TABLE_APP_PARAM;
		this.columns = new String[] {  };
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;

	}

	@Override
	protected long insert(AbstractTableDTO dto) {
		return 0;
	}

	@Override
	protected long update(AbstractTableDTO dto) {
		return 0;
	}

	@Override
	protected long delete(AbstractTableDTO dto) {
		return 0;
	}

	/**
	 * Khoi tao danh sach ung dung duoc phep su dung duoi Tablet
	 * @author: banghn
	 * @param ext
	 */
	public void initApplicationGuard(Bundle ext) {
		String shopId = ext.getString(IntentConstants.INTENT_SHOP_ID);
		String userId = ext.getLong(IntentConstants.INTENT_USER_ID) + "";
		String role = ext.getInt(IntentConstants.INTENT_ROLE_TYPE) + "";
		Cursor c = null;
		try {
			ArrayList<String> params = new ArrayList<String>();
			StringBuffer  sqlQuery = new StringBuffer();
			sqlQuery.append("SELECT DISTINCT tp.rule_type as rule_type, ");
			sqlQuery.append("                tp.app_code as app_code ");
			sqlQuery.append("FROM   (SELECT ur.user_id, ");
			sqlQuery.append("               ur.user_type, ");
			sqlQuery.append("               ur.rule_type, ");
			sqlQuery.append("               app.app_id, ");
			sqlQuery.append("               app.app_code ");
			sqlQuery.append("        FROM   (SELECT * ");
			sqlQuery.append("                FROM   other_app_rule ");
			sqlQuery.append("                WHERE  status = 1 ");
			sqlQuery.append("                       AND ( ( user_type = 3 ");
			sqlQuery.append("                               AND user_id = ? ) ");
			params.add(userId);
			sqlQuery.append("                              OR ( user_type = 2 ");
			sqlQuery.append("                                   AND user_id = ? ) ");
			params.add(role);
			sqlQuery.append("                              OR ( user_type = 1 ");
			sqlQuery.append("                                   AND user_id = ? ) )) ur ");
			params.add(shopId);
			sqlQuery.append("               JOIN (SELECT oagp.id AS group_id, ");
			sqlQuery.append("                            oap.id  AS app_id, ");
			sqlQuery.append("                            oap.app_code ");
			sqlQuery.append("                     FROM   other_app_group oagp ");
			sqlQuery.append("                            JOIN other_app_group_dtl oagdl ");
			sqlQuery.append("                              ON oagp.id = oagdl.other_app_group_id ");
			sqlQuery.append("                            JOIN other_app oap ");
			sqlQuery.append("                              ON oagdl.other_app_id = oap.id ");
			sqlQuery.append("                     WHERE  oagp.status = 1 ");
			sqlQuery.append("                            AND oagdl.status = 1 ");
			sqlQuery.append("                            AND oap.status = 1 ");
			sqlQuery.append("                     UNION ");
			sqlQuery.append("                     SELECT NULL   AS group_id, ");
			sqlQuery.append("                            oap.id AS app_id, ");
			sqlQuery.append("                            oap.app_code ");
			sqlQuery.append("                     FROM   other_app oap ");
			sqlQuery.append("                     WHERE  oap.status = 1) app ");
			sqlQuery.append("                 ON ( ( ur.app_type = 1 ");
			sqlQuery.append("                        AND ur.app_id = app.app_id ");
			sqlQuery.append("                        AND app.group_id IS NULL ) ");
			sqlQuery.append("                       OR ( ur.app_type = 2 ");
			sqlQuery.append("                            AND ur.app_id = app.group_id ))) tp ");
			sqlQuery.append("       JOIN (SELECT user_type, ");
			sqlQuery.append("                    app_id, ");
			sqlQuery.append("                    app_code, ");
			sqlQuery.append("                    Max(user_type) AS mx ");
			sqlQuery.append("             FROM   (SELECT ur.user_id, ");
			sqlQuery.append("                            ur.user_type, ");
			sqlQuery.append("                            ur.rule_type, ");
			sqlQuery.append("                            app.app_id, ");
			sqlQuery.append("                            app.app_code ");
			sqlQuery.append("                     FROM   (SELECT * ");
			sqlQuery.append("                             FROM   other_app_rule ");
			sqlQuery.append("                             WHERE  status = 1 ");
			sqlQuery.append("                                    AND ( ( user_type = 3 ");
			sqlQuery.append("                                            AND user_id = ? ) ");
			params.add(userId);
			sqlQuery.append("                                           OR ( user_type = 2 ");
			sqlQuery.append("                                                AND user_id = ? ) ");
			params.add(role);
			sqlQuery.append("                                           OR ( user_type = 1 ");
			sqlQuery.append("                                                AND user_id = ? ) )) ur ");
			params.add(shopId);
			sqlQuery.append("                            JOIN (SELECT oagp.id AS group_id, ");
			sqlQuery.append("                                         oap.id  AS app_id, ");
			sqlQuery.append("                                         oap.app_code ");
			sqlQuery.append("                                  FROM   other_app_group oagp ");
			sqlQuery.append("                                         JOIN other_app_group_dtl oagdl ");
			sqlQuery.append("                                           ON oagp.id = oagdl.other_app_group_id ");
			sqlQuery.append("                                         JOIN other_app oap ");
			sqlQuery.append("                                           ON oagdl.other_app_id = oap.id ");
			sqlQuery.append("                                  WHERE  oagp.status = 1 ");
			sqlQuery.append("                                         AND oagdl.status = 1 ");
			sqlQuery.append("                                         AND oap.status = 1 ");
			sqlQuery.append("                                  UNION ");
			sqlQuery.append("                                  SELECT NULL   AS group_id, ");
			sqlQuery.append("                                         oap.id AS app_id, ");
			sqlQuery.append("                                         oap.app_code ");
			sqlQuery.append("                                  FROM   other_app oap ");
			sqlQuery.append("                                  WHERE  oap.status = 1) app ");
			sqlQuery.append("                              ON ( ( ur.app_type = 1 ");
			sqlQuery.append("                                     AND ur.app_id = app.app_id ");
			sqlQuery.append("                                     AND app.group_id IS NULL ) ");
			sqlQuery.append("                                    OR ( ur.app_type = 2 ");
			sqlQuery.append("                                         AND ur.app_id = app.group_id ) )) ");
			sqlQuery.append("             GROUP  BY user_type, ");
			sqlQuery.append("                       app_id, ");
			sqlQuery.append("                       app_code) tpm ");
			sqlQuery.append("         ON tp.user_type = tpm.user_type ");
			sqlQuery.append("            AND tp.app_id = tpm.app_id ");
			sqlQuery.append("            AND tp.app_code = tpm.app_code ");
			sqlQuery.append("            AND tp.user_type = tpm.mx ");
			//sqlQuery.append("ORDER  BY tp.app_code");
			
			c = this.rawQueries(sqlQuery.toString(), params);
			ArrayList<String> whiteListAppInstall = new ArrayList<String>();
			ArrayList<String> blackListAppBlocked = new ArrayList<String>();
			
			if (c.moveToFirst()) {
				do {
					AppRuleDTO item = new AppRuleDTO();
					item.initObjectWithCursor(c);
					if(item.ruleType == 1){
						whiteListAppInstall.add(item.appCode);
					}else{
						blackListAppBlocked.add(item.appCode);
					}
				} while (c.moveToNext());
			}
			
			GlobalInfo.getInstance().setWhiteList(whiteListAppInstall);
			GlobalInfo.getInstance().setBlackListAppBlocked(blackListAppBlocked);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}

}
