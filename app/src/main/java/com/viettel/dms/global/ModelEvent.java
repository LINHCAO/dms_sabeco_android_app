/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.global;

/**
 *  Chua model event
 *  @author: DoanDM
 *  @version: 1.0
 *  @since: 1.1
 */
public class ModelEvent {
	// thanh cong
	public static final int MODEL_RESULT_SUCCESS = 0;
	// thoi gian sai khi request online
	public static final int MODEL_RESULT_FAIL_TIME_ONLINE = 1;
	// thoi gian sai khi request offline
	public static final int MODEL_RESULT_FAIL_TIME_OFFLINE = 2;
	// that bai chung (do exception code....)
	public static final int MODEL_RESULT_COMMON_FAIL = 3;
	
	private ActionEvent actionEvent;
	Object modelData;
	int modelCode;
	int code;//dung trong timeout cua Oauth
	String modelMessage;
	// luu giu chuoi datatext tra ve tu server, phuc vu trace log
	private String dataText = "";
	// luu giu param cua request khi truyen len server
	private String param = "";
	private boolean isSendLog = true;
	
	public void setActionEvent(ActionEvent actionEvent) {
		this.actionEvent = actionEvent;
	}
	public ActionEvent getActionEvent() {
		return actionEvent;
	}
	public Object getModelData() {
		return modelData;
	}
	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}
	public void setModelData(Object modelData) {
		this.modelData = modelData;
	}
	public int getModelCode() {
		return modelCode;
	}
	public void setModelCode(int modelCode) {
		this.modelCode = modelCode;
	}
	public String getModelMessage() {
		return modelMessage;
	}
	public void setModelMessage(String modelMessage) {
		this.modelMessage = modelMessage;
	}
	public void setDataText(String dataText) {
		this.dataText = dataText;
	}
	public String getDataText() {
		return dataText;
	}
	public void setParams(String para) {
		this.param = para;
	}
	public String getParams() {
		return param;
	}
	
	public boolean getIsSendLog(){
		return this.isSendLog;
	}
	
	public void setIsSendLog(boolean isSendLog){
		this.isSendLog = isSendLog;
	}
}
