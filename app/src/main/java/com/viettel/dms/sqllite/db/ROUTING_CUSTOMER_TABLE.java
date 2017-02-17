/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import android.content.ContentValues;

import net.sqlcipher.database.SQLiteDatabase;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.RoutingCustomerDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;

import java.util.Calendar;

/**
 * Mo ta muc dich cua class
 * 
 * @author: TamPQ
 * @version: 1.0
 * @since: 1.0
 */
public class ROUTING_CUSTOMER_TABLE extends ABSTRACT_TABLE {
	// ID bảng
	public static final String ROUTING_CUSTOMER_ID = "ROUTING_CUSTOMER_ID";
	// id khach hang
	public static final String CUSTOMER_ID = "CUSTOMER_ID";
	// Khoang delay giua cac tuan (chua dung)
	public static final String WEEK_INTERVAL = "WEEK_INTERVAL";
	// 0: ko di , 1: di
	public static final String MONDAY = "MONDAY";
	// 0: ko di , 1: di
	public static final String TUESDAY = "TUESDAY";
	// 0: ko di , 1: di
	public static final String WEDNESDAY = "WEDNESDAY";
	// 0: ko di , 1: di
	public static final String THURSDAY = "THURSDAY";
	// 0: ko di , 1: di
	public static final String FRIDAY = "FRIDAY";
	// 0: ko di , 1: di
	public static final String SATURDAY = "SATURDAY";
	// 0: ko di , 1: di
	public static final String SUNDAY = "SUNDAY";
	// 0: khong hoat dong, 1: hoat dong
	public static final String STATUS = "STATUS";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// ngay update
	public static final String UPDATE_DATE = "UPDATE_DATE";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// tan suat
	public static final String SEQ = "SEQ";
	// id tuyen
	public static final String ROUTING_ID = "ROUTING_ID";
	// thu tu ghe tham thu 2
	public static final String SEQ2 = "SEQ2";
	// thu tu ghe tham thu 3
	public static final String SEQ3 = "SEQ3";
	// thu tu ghe tham thu 4
	public static final String SEQ4 = "SEQ4";
	// thu tu ghe tham thu 5
	public static final String SEQ5 = "SEQ5";
	// thu tu ghe tham thu 6
	public static final String SEQ6 = "SEQ6";
	// thu tu ghe tham thu 7
	public static final String SEQ7 = "SEQ7";
	// thu tu ghe tham thu cn
	public static final String SEQ8 = "SEQ8";
	// START_WEEK
	public static final String START_WEEK = "START_WEEK";
	// START_DATE
	public static final String START_DATE = "START_DATE";
	// END_DATE
	public static final String END_DATE = "END_DATE";
	public static final String WEEK1 = "WEEK1";
	public static final String WEEK2 = "WEEK2";
	public static final String WEEK3 = "WEEK3";
	public static final String WEEK4 = "WEEK4";

	public static final String ROUTING_CUSTOMER_TABLE = "ROUTING_CUSTOMER";

	public ROUTING_CUSTOMER_TABLE() {
		this.tableName = ROUTING_CUSTOMER_TABLE;
		this.columns = new String[] { ROUTING_CUSTOMER_ID, CUSTOMER_ID, WEEK_INTERVAL, MONDAY, TUESDAY, WEDNESDAY,
				THURSDAY, FRIDAY, SATURDAY, SUNDAY, STATUS, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER, SEQ,
				SEQ2, SEQ3, SEQ4, SEQ5, SEQ6, SEQ7, SEQ8, START_WEEK, SYN_STATE };

		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = SQLUtils.getInstance().getmDB();
	}

	public ROUTING_CUSTOMER_TABLE(SQLiteDatabase mDB) {
		this.tableName = ROUTING_CUSTOMER_TABLE;
		this.columns = new String[] { ROUTING_CUSTOMER_ID, CUSTOMER_ID, WEEK_INTERVAL, MONDAY, TUESDAY, WEDNESDAY,
				THURSDAY, FRIDAY, SATURDAY, SUNDAY, STATUS, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER, SEQ,
				SEQ2, SEQ3, SEQ4, SEQ5, SEQ6, SEQ7, SEQ8, START_WEEK, SYN_STATE };

		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;
	}

	/**
	 * Khi táº¡o khĂ¡ch hĂ ng
	 * @author: duongdt3
	 * @since: 14:13:47 7 Jan 2014
	 * @return: ContentValues
	 * @throws:
	 * @param dto
	 * @return
	 */
	public ContentValues initDataRow(RoutingCustomerDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(ROUTING_CUSTOMER_ID, dto.routingCustomerId);
		editedValues.put(CUSTOMER_ID, dto.customerId);
		editedValues.put(MONDAY, dto.monday);
		editedValues.put(TUESDAY, dto.tuesday);
		editedValues.put(WEDNESDAY, dto.wednesday);
		editedValues.put(THURSDAY, dto.thursday);
		editedValues.put(FRIDAY, dto.friday);
		editedValues.put(SATURDAY, dto.saturday);
		editedValues.put(STATUS, dto.status);
		editedValues.put(CREATE_DATE, dto.createDate);
		editedValues.put(CREATE_USER, dto.createUser);
		editedValues.put(ROUTING_ID, dto.routingId);
		editedValues.put(START_DATE, dto.startDate);
		editedValues.put(END_DATE, dto.endDate);
		editedValues.put(WEEK1, dto.week1);
		editedValues.put(WEEK2, dto.week2);
		editedValues.put(WEEK3, dto.week3);
		editedValues.put(WEEK4, dto.week4);

		return editedValues;
	}

	/**
	 * them khach hang vao tuyen
	 * @author: trungnt56
	 * @since: 14:05:01 7 Jan 2014
	 * @return: void
	 * @throws:
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public long insertRoutingCustomer(RoutingCustomerDTO dto) throws Exception {
		TABLE_ID idTable = new TABLE_ID(mDB);
		// táº¡o id
		dto.routingCustomerId = idTable.getMaxId(ROUTING_CUSTOMER_TABLE);
		addInfoForNewRoutingCustomer(dto);

		return insert(dto);
	}

	public void addInfoForNewRoutingCustomer(RoutingCustomerDTO routingCustomerDTO) throws Exception {
		Calendar calendar = Calendar.getInstance();
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if(dayOfWeek == 1) {
			routingCustomerDTO.sunday = 1;
		} else if(dayOfWeek == 2) {
			routingCustomerDTO.monday = 1;
		} else if(dayOfWeek == 3) {
			routingCustomerDTO.tuesday = 1;
		} else if(dayOfWeek == 4) {
			routingCustomerDTO.wednesday = 1;
		} else if(dayOfWeek == 5) {
			routingCustomerDTO.thursday = 1;
		} else if(dayOfWeek == 6) {
			routingCustomerDTO.friday = 1;
		} else if(dayOfWeek == 7) {
			routingCustomerDTO.saturday = 1;
		}
		routingCustomerDTO.status = 1;
		routingCustomerDTO.createDate = DateUtils.now();
		routingCustomerDTO.createUser = GlobalInfo.getInstance().getProfile().getUserData().userName;
		routingCustomerDTO.startDate = DateUtils.now();
		routingCustomerDTO.endDate = null;
		routingCustomerDTO.week1 = 1;
		routingCustomerDTO.week2 = 1;
		routingCustomerDTO.week3 = 1;
		routingCustomerDTO.week4 = 1;
	}

	@Override
	protected long insert(AbstractTableDTO dto) {
		ContentValues value = initDataRow((RoutingCustomerDTO) dto);
		return insert(null, value);
	}

	@Override
	protected long update(AbstractTableDTO dto) {
		return 0;
	}

	@Override
	protected long delete(AbstractTableDTO dto) {
		return 0;
	}

}
