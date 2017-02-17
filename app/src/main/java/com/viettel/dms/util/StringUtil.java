/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;
import com.viettel.utils.VTStringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Chua cac ham util ve xu ly chuoi
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
@SuppressLint({ "SimpleDateFormat", "DefaultLocale" })
public class StringUtil{
	// public static final String TAG_PATERN =
	public static String EMPTY_STRING = "";
	public static String SPACE_STRING = " ";
	public static char CHAR_SPLIT = '�';
	public static String[] arrSpecialChar = { "‘", "’", "“", "”", "„", "†", "‡", "‰", "‹", "›", "♠", "♣", "♥", "♦",
			"‾", "←", "↑", "→", "↓", "™", "!", "“", "#", "$", "%", "&", "‘", "(", ")", "*", "+", ",", "-", ".", "/",
			":", ";", "<", "=", ">", "?", "@", "[", "\\", "]", "_", "`", "{", "|", "}", "~", "–", "—", "¡", "¢", "£",
			"¤", "¥", "¦", "§", "¨", "©", "ª", "«", "¬", "¬", "®", "¯", "°", "±", "²", "³", "´", "µ", "¶", "•", "¸",
			"¹", "º", "¿", "Ä", "Å", "Æ", "Ç", "Ë", "Î", "Ï", "Ñ", "Ö", "×", "Ø", "Û", "Ü", "Þ", "ß", "ã", "ä", "å",
			"æ", "ç", "ë", "î", "ï", "ð", "ñ", "õ", "ö", "÷", "ø", "û", "ü", "þ", "ÿ" };

	/** The codau. */
	static char codau[] = { 'à', 'á', 'ả', 'ã', 'ạ', 'ă', 'ằ', 'ắ', 'ẳ', 'ẵ', 'ặ', 'â', 'ầ', 'ấ', 'ẩ', 'ẫ', 'ậ', 'À',
			'Á', 'Ả', 'Ã', 'Ạ', 'Ă', 'Ằ', 'Ắ', 'Ẳ', 'Ẵ', 'Ặ', 'Â', 'Ầ', 'Ấ', 'Ẩ', 'Ẫ', 'Ậ', 'è', 'é', 'ẻ', 'ẽ', 'ẹ',
			'ê', 'ề', 'ế', 'ể', 'ễ', 'ệ', 'È', 'É', 'Ẻ', 'Ẽ', 'Ẹ', 'Ê', 'Ề', 'Ế', 'Ể', 'Ễ', 'Ệ', 'ì', 'í', 'ỉ', 'ĩ',
			'ị', 'Ì', 'Í', 'Ỉ', 'Ĩ', 'Ị', 'ò', 'ó', 'ỏ', 'õ', 'ọ', 'ô', 'ồ', 'ố', 'ổ', 'ỗ', 'ộ', 'ơ', 'ờ', 'ớ', 'ở',
			'ỡ', 'ợ', 'Ò', 'Ó', 'Ỏ', 'Õ', 'Ọ', 'Ô', 'Ồ', 'Ố', 'Ổ', 'Ỗ', 'Ộ', 'Ơ', 'Ờ', 'Ớ', 'Ở', 'Ỡ', 'Ợ', 'ù', 'ú',
			'ủ', 'ũ', 'ụ', 'ư', 'ừ', 'ứ', 'ử', 'ữ', 'ự', 'Ù', 'Ú', 'Ủ', 'Ũ', 'Ụ', 'ỳ', 'ý', 'ỷ', 'ỹ', 'ỵ', 'Ỳ', 'Ý',
			'Ỷ', 'Ỹ', 'Ỵ', 'đ', 'Đ', 'Ư', 'Ừ', 'Ử', 'Ữ', 'Ứ', 'Ự' };
	/** The khongdau. */
	static char khongdau[] = { 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a', 'a',
			'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'e', 'e', 'e', 'e',
			'e', 'e', 'e', 'e', 'e', 'e', 'e', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'i', 'i', 'i',
			'i', 'i', 'I', 'I', 'I', 'I', 'I', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o',
			'o', 'o', 'o', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'O', 'u',
			'u', 'u', 'u', 'u', 'u', 'u', 'u', 'u', 'u', 'u', 'U', 'U', 'U', 'U', 'U', 'y', 'y', 'y', 'y', 'y', 'Y',
			'Y', 'Y', 'Y', 'Y', 'd', 'D', 'U', 'U', 'U', 'U', 'U', 'U' };

	static char charsInName[] = { 'à', 'á', 'ả', 'ã', 'ạ', 'ă', 'ằ', 'ắ', 'ẳ', 'ẵ', 'ặ', 'â', 'ầ', 'ấ', 'ẩ', 'ẫ', 'ậ',
			'À', 'Á', 'Ả', 'Ã', 'Ạ', 'Ă', 'Ằ', 'Ắ', 'Ẳ', 'Ẵ', 'Ặ', 'Â', 'Ầ', 'Ấ', 'Ẩ', 'Ẫ', 'Ậ', 'è', 'é', 'ẻ', 'ẽ',
			'ẹ', 'ê', 'ề', 'ế', 'ể', 'ễ', 'ệ', 'È', 'É', 'Ẻ', 'Ẽ', 'Ẹ', 'Ê', 'Ề', 'Ế', 'Ể', 'Ễ', 'Ệ', 'ì', 'í', 'ỉ',
			'ĩ', 'ị', 'Ì', 'Í', 'Ỉ', 'Ĩ', 'Ị', 'ò', 'ó', 'ỏ', 'õ', 'ọ', 'ô', 'ồ', 'ố', 'ổ', 'ỗ', 'ộ', 'ơ', 'ờ', 'ớ',
			'ở', 'ỡ', 'ợ', 'Ò', 'Ó', 'Ỏ', 'Õ', 'Ọ', 'Ô', 'Ồ', 'Ố', 'Ổ', 'Ỗ', 'Ộ', 'Ơ', 'Ờ', 'Ớ', 'Ở', 'Ỡ', 'Ợ', 'ù',
			'ú', 'ủ', 'ũ', 'ụ', 'ư', 'ừ', 'ứ', 'ử', 'ữ', 'ự', 'Ù', 'Ú', 'Ủ', 'Ũ', 'Ụ', 'Ư', 'Ừ', 'Ứ', 'Ử', 'Ữ', 'Ự',
			'ỳ', 'ý', 'ỷ', 'ỹ', 'ỵ', 'Ỳ', 'Ý', 'Ỷ', 'Ỹ', 'Ỵ', 'đ', 'Đ', 'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e',
			'E', 'f', 'F', 'g', 'G', 'h', 'H', 'i', 'I', 'j', 'J', 'k', 'K', 'l', 'L', 'm', 'M', 'n', 'N', 'o', 'O',
			'p', 'P', 'q', 'Q', 'r', 'R', 's', 'S', 't', 'T', 'u', 'U', 'v', 'V', 'w', 'W', 'x', 'X', 'y', 'Y', 'z',
			'Z', ' ' };
	
	static char charsInNumber[] = { '0','1', '2','3','4','5','6','7','8','9' };
	static char charsInStreet[] = { '-', ',', '/', '0','1', '2','3','4','5','6','7','8','9' };
	static char charsInHouseNumber[] = { '-', ',', '/', '0','1', '2','3','4','5','6','7','8','9'};

	/** STRING_SEARCH_LIKE_ALL. */
	public static final String STRING_SEARCH_LIKE_ALL = "%%";

	private static final String[] oracleTextKeywords = new String[] { "ACCUM", "ABOUT", "NOT", "OR", "AND", "BT",
			"BTG", "BTP", "BTI", "NT", "NTG", "NTP", "NTI", "PT", "RT", "RT", "SQE", "SYN", "TR", "TRSYN", "TT",
			"FUZZY", "HASPATH", "INPATH", "MINUS", "NEAR", "WITHIN", "84%", "8%", "MDATA" };

	public static final String STRING_SPECIAL_CHAR = "<>./!@#$%^*'\"-_():|[]~+{}?\\\n";

	private static final byte[] HEX_CHAR_TABLE = { (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4',
			(byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd',
			(byte) 'e', (byte) 'f' };
	private static final String charset = "!0123456789abcdefghijklmnopqrstuvwxyz";
	public static final int LENGTH_HASH = 50;

	/**
	 * 
	 * kiem tra ten co chua cac ki tu hop le
	 * 
	 * @author: AnhND
	 * @return
	 * @return: String
	 * @throws:
	 */
	public static boolean isNameContainValidChars(String name) {
		boolean isContain = false;
		for (int j = 0, m = name.length(); j < m; j++) {
			isContain = false;
			for (int i = 0, n = charsInName.length; i < n; i++) {
				if (name.charAt(j) == charsInName[i]) {
					isContain = true;
					break;
				}
			}
			//duongdt, nếu không nằm trong tập cho phép, => inVaild
			if (isContain == false) {
				break;
			}
		}
		return isContain;
	}
	
	public static boolean isCustomerNameContainValidChars(String name) {
		return isStringContainValidChars(name, charsInNumber, charsInName);
	}
	/**
	 * Kiểm tra chuỗi nhập vào có là street
	 * @author: duongdt3
	 * @since: 07:59:07 9 Jan 2014
	 * @return: boolean
	 * @throws:  
	 * @param street
	 * @return
	 */
	public static boolean isStreetContainValidChars(String street) {
		return isStringContainValidChars(street, charsInStreet, charsInName);
	}
	
	/**
	 * Kiểm tra chuỗi nhập có là house number hợp lệ
	 * @author: duongdt3
	 * @since: 08:26:44 9 Jan 2014
	 * @return: boolean
	 * @throws:  
	 * @param street
	 * @return
	 */
	public static boolean isHouseNumberContainValidChars(String street) {
		return isStringContainValidChars(street, charsInHouseNumber, charsInName);
	}
	
	/**
	 * Kiểm tra string có nằm trong 2 mảng char cho phép hay không
	 * @author: duongdt3
	 * @since: 07:49:27 9 Jan 2014
	 * @return: boolean
	 * @throws:  
	 * @param text
	 * @param arChar1
	 * @param arChar2
	 * @return
	 */
	public static boolean isStringContainValidChars(String text, char[]  arChar1, char[]  arChar2) {
		boolean isContain = false;
		for (int j = 0, m = text.length(); j < m; j++) {
			isContain = false;			
			//check mảng 1
			for (int i = 0, n = arChar1.length; i < n; i++) {
				if (text.charAt(j) == arChar1[i]) {
					isContain = true;
					break;
				}
			}
			//nếu nằm trong 1 mảng thì tiếp tục, không cần xét mảng dưới
			if (isContain == true) {
				continue;
			}
			
			//check mảng 2
			for (int i = 0, n = arChar2.length; i < n; i++) {
				if (text.charAt(j) == arChar2[i]) {
					isContain = true;
					break;
				}
			}
			//nếu không chứa trong mảng nào -> không hợp lệ
			if (isContain == false) {
				break;
			}
		}
		
		return isContain;
	}

	/**
	 * 
	 * bo dau tieng viet
	 * 
	 * @author: AnhND
	 * @param input
	 * @return: String
	 * @throws:
	 */
	public static String getEngStringFromUnicodeString(String input) {
		if (isNullOrEmpty(input)) {
			return "";
		}
		input = input.trim();

		for (int i = 0; i < codau.length; i++) {
			input = input.replace(codau[i], khongdau[i]);
		}
		return input;
	}

	public static String replace(String _text, String _searchStr, String _replacementStr) {
		// String buffer to store str
		StringBuffer sb = new StringBuffer();

		// Search for search
		int searchStringPos = _text.indexOf(_searchStr);
		int startPos = 0;
		int searchStringLength = _searchStr.length();

		// Iterate to add string
		while (searchStringPos != -1) {
			sb.append(_text.substring(startPos, searchStringPos)).append(_replacementStr);
			startPos = searchStringPos + searchStringLength;
			searchStringPos = _text.indexOf(_searchStr, startPos);
		}

		// Create string
		sb.append(_text.substring(startPos, _text.length()));

		return sb.toString();
	}

	/**
	 * Kiem tra nick name hop le
	 * 
	 * @author: TruongHN3
	 * @param userName
	 * @return: boolean
	 * @throws:
	 */
	public static boolean isValidateUserName(String userName) {
		// UserName khong the toan so
		// UserName chi chua cac ky tu a-z, A-Z, 0-9
		Boolean isValid = false;
		int length = userName.length();
		for (int i = 0; i < length; i++) {
			char ch = userName.charAt(i);
			if (((ch >= 0x30 && ch <= 0x39) || (ch >= 0x41 && ch <= 0x5a) || (ch >= 0x61 && ch <= 0x7a))) {
				isValid = true;
			} else {
				isValid = false;
				break;
			}
		}
		return isValid;
	}

	/**
	 * Kiem tra noi dung hop le
	 * 
	 * @author: TruongHN3
	 * @param name
	 * @return: boolean
	 * @throws:
	 */
	public static boolean isValidatePaymentContent(String name) {
		// Noi dung chi chua cac ky tu a-z, A-Z, 0-9, khoang trang
		Boolean isValid = false;
		int length = name.length();
		for (int i = 0; i < length; i++) {
			char ch = name.charAt(i);
			if (((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9') || (ch == ' '))) {
				isValid = true;
			} else {
				isValid = false;
				break;
			}
		}
		return isValid;
	}

	/**
	 * Kiem tra noi dung hop le
	 * 
	 * @author: TruongHN3
	 * @param name
	 * @return: boolean
	 * @throws:
	 */
	public static boolean isValidatePaymentContentWithoutSpace(String name) {
		// Noi dung chi chua cac ky tu a-z, A-Z, 0 - 9
		Boolean isValid = false;
		int length = name.length();
		for (int i = 0; i < length; i++) {
			char ch = name.charAt(i);
			if (((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9'))) {
				isValid = true;
			} else {
				isValid = false;
				break;
			}
		}
		return isValid;
	}

	/**
	 * 
	 * validate num product input to order
	 * 
	 * @author: HaiTC3
	 * @param inputData
	 * @return
	 * @return: boolean
	 * @throws:
	 */
	public static boolean isValidateNumProductInput(String inputData) {
		for (int i = 0, size = inputData.length(); i < size; i++) {
			if ((inputData.charAt(i) < '0' || inputData.charAt(i) > '9') && inputData.charAt(i) != '/') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * validate vote number input for product
	 * 
	 * @author: HaiTC3
	 * @param inputData
	 * @return
	 * @return: boolean
	 * @throws:
	 */
	public static boolean isValidateIntergeNonNegativeInput(String inputData) {
		for (int i = 0, size = inputData.length(); i < size; i++) {
			if (inputData.charAt(i) < '0' || inputData.charAt(i) > '9') {
				return false;
			}
		}
		return true;
	}

	/**
	 * validate phone
	 * 
	 * @param phone
	 * @return
	 * @Author : DoanDM Since Dec 9, 2010
	 */
	public static boolean isValidPhone(String phone) {
		int length = phone.length();
		for (int i = 0; i < length; i++) {
			if (phone.charAt(i) < '0' || phone.charAt(i) > '9') {
				return false;
			}
		}
		return true;
	}

	/**
	 * validate email
	 * @return
	 * @Author : DoanDM Since Dec 9, 2010
	 */
	public static boolean isValidateEmail(String email) {
		if (email == null || email.length() == 0 || email.indexOf("@") == -1 || email.indexOf(" ") != -1) {
			return false;
		}
		int emailLenght = email.length();
		int atPosition = email.indexOf("@");

		String beforeAt = email.substring(0, atPosition);
		String afterAt = email.substring(atPosition + 1, emailLenght);

		if (beforeAt.length() == 0 || afterAt.length() == 0) {
			// #ifdef DEBUG
			// # //System.out.println("only @ is");
			// #endif
			return false;
		}

		// CHECK for .(dot) before @(at) = aaa.@domain.com
		if (email.charAt(atPosition - 1) == '.') {
			// #ifdef DEBUG
			// # //System.out.println(".(dot) before @(at)");
			// #endif
			return false;
		}

		// CHECK for .(dot) before @(at) = aaa@.domain.com
		if (email.charAt(atPosition + 1) == '.') {
			// #ifdef DEBUG
			// # //System.out.println("@.");
			// #endif
			return false;
		}

		// CHECK for .(dot) = email@domaincom
		if (afterAt.indexOf(".") == -1) {
			// #ifdef DEBUG
			// # //System.out.println("no dot(.)");
			// #endif
			return false;
		}

		// CHECK for ..(2 dots) = email@domain..com
		char dotCh = 0;
		for (int i = 0; i < afterAt.length(); i++) {
			char ch = afterAt.charAt(i);
			// soan validate
			if (!((ch >= 0x30 && ch <= 0x39) || (ch >= 0x41 && ch <= 0x5a) || (ch >= 0x61 && ch <= 0x7a)
					|| (ch == 0x2e) || (ch == 0x2d) || (ch == 0x5f))) {
				// #ifdef DEBUG
				// # //System.out.println("not valid chars");
				// #endif
				return false;
			}
			// end soan
			if ((ch == 0x2e) && (ch == dotCh)) {
				// #ifdef DEBUG
				// # // System.out.println("find .. (2 dots) in @>");
				// #endif
				return false;
			}
			dotCh = ch;
		}

		// CHECK for double '@' = example@@domain.com
		if (afterAt.indexOf("@") != -1) {
			// #ifdef DEBUG
			// # //System.out.println("find 2 @");
			// #endif
			return false;
		}
		// check domain name 2-5 chars
		int ind = 0;
		do {
			int newInd = afterAt.indexOf(".", ind + 1);

			if (newInd == ind || newInd == -1) {
				String prefix = afterAt.substring(ind + 1);
				if (prefix.length() > 1 && prefix.length() < 6) {
					break;
				} else {
					// #ifdef DEBUG
					// # //System.out.println("prefix not 2-5 chars");
					// #endif
					return false;
				}
			} else {
				ind = newInd;
			}
		} while (true);

		// CHECK for valid chars[a-z][A-Z][0-9][. - _]
		// CHECK for ..(2 dots)
		dotCh = 0;
		for (int i = 0; i < beforeAt.length(); i++) {
			char ch = beforeAt.charAt(i);
			if (!((ch >= 0x30 && ch <= 0x39) || (ch >= 0x41 && ch <= 0x5a) || (ch >= 0x61 && ch <= 0x7a)
					|| (ch == 0x2e) || (ch == 0x2d) || (ch == 0x5f))) {
				// #ifdef DEBUG
				// # //System.out.println("not valid chars");
				// #endif
				return false;
			}
			if ((ch == 0x2e) && (ch == dotCh)) {
				// #ifdef DEBUG
				// # //System.out.println("find .. (2 dots)");
				// #endif
				return false;
			}
			dotCh = ch;
		}
		return true;
	}

	public final static void supplementCodePointToSurrogatePair(int codePoint, int[] surrogatePair) {
		int high4 = ((codePoint >> 16) & 0x1F) - 1;
		int mid6 = ((codePoint >> 10) & 0x3F);
		int low10 = codePoint & 0x3FF;

		surrogatePair[0] = (0xD800 | (high4 << 6) | (mid6));
		surrogatePair[1] = (0xDC00 | (low10));
	}

	public static boolean isNullOrEmpty(String aString) {
		return (aString == null) || (aString.trim().length()==0);
	}

	public static String getString(int id) {
		return GlobalInfo.getInstance().getAppContext().getResources().getString(id);
	}
	
	/***
	 * Lay String[] tu resource
	 * @author quangvt1
	 * @param id
	 * @return
	 */
	public static String[] getStringArray(int id) {
		return GlobalInfo.getInstance().getAppContext().getResources().getStringArray(id);
	}
	
	/***
	 * Lay mang Int[] tu resource
	 * @author quangvt1
	 * @param id
	 * @return
	 */
	public static int[] getIntegerArray(int id) {
		return GlobalInfo.getInstance().getAppContext().getResources().getIntArray(id);
	}

	/**
	 * lay string co dang "... xxx..." tu string.xml va thay the chuoi xxx
	 * 
	 * @author: PhucNT
	 * @param
	 * @return: String
	 * @throws:
	 */
	public static String getStringAndReplace(int id, String replace) {
		String str = getString(id);
		if (str.contains(Constants.REPLACED_STRING))
			str = str.replace(Constants.REPLACED_STRING, replace);
		return str;
	}

	/**
	 * chuyen phone ve dang 01, 09
	 * 
	 * @author: BangHN
	 * @param phoneString
	 * @return
	 * @return: String
	 * @throws:
	 */
	public static String converPhoneStringToHeader0109(String phoneString) {
		if (!isNullOrEmpty(phoneString)) {
			if (phoneString.startsWith("84")) {
				phoneString = phoneString.substring(2);
				phoneString = "0" + phoneString;
			} else if (phoneString.startsWith("+84")) {
				phoneString = phoneString.substring(3);
				phoneString = "0" + phoneString;
			}
		} else {
			phoneString = "";
		}
		return phoneString;
	}

	/**
	 * 
	 * Thay the khoang trang thanh %20
	 * 
	 * @author: DoanDM
	 * @return: void
	 * @throws:
	 */
	public static String replaceSpaceByHtmlCode(String ori) {
		if (isNullOrEmpty(ori))
			return "";
		return ori.replace(" ", "%20");
	}

	/**
	 * validate va chuyen phone ve dang 01, 09
	 * 
	 * @author: BangHN
	 * @param phoneString
	 * @return
	 * @return: String
	 * @throws:
	 */
	public static String validateAndConverPhoneStringToHeader0109(String phoneString) {
		String res = phoneString;
		if (validateMobileNumber(phoneString)) {
			res = converPhoneStringToHeader0109(phoneString);
		}
		return res;
	}

	/**
	 * Co the la so dien thoai hay khong
	 * 
	 * @author: BangHN
	 * @param phonenumber
	 * @return: boolean
	 */
	public static boolean canBePhoneNumber(String phonenumber) {
		if (StringUtil.isNullOrEmpty(phonenumber)) {
			return false;
		}
		Pattern p = Pattern.compile("^[09|01|849|841|+849|+841][0-9]+$");
		Matcher m = p.matcher(phonenumber);

		boolean matchFound = m.matches();
		return matchFound;
	}

	/**
	 * Kiem tra mot chuoi co phai la so dien thoai hop le hay khong
	 * 
	 * @author: BangHN
	 * @param mobileNumber
	 * @return: boolean
	 */
	public static boolean validateMobileNumber(String mobileNumber) {
		mobileNumber = mobileNumber.trim();
		final String prefix849 = "849";
		final String prefix849plus = "+849";
		final String prefix841 = "841";
		final String prefix841plus = "+841";
		final String prefix09 = "09";
		final String prefix01 = "01";
		boolean result = false;

		if (!StringUtil.isNullOrEmpty(mobileNumber) && canBePhoneNumber(mobileNumber)) {
			int length = mobileNumber.length();
			switch (length) {
			case 10:
				if (mobileNumber.startsWith(prefix09)) {
					result = true;
				}
				break;
			case 11:
				if (mobileNumber.startsWith(prefix01) || mobileNumber.startsWith(prefix849)) {
					result = true;
				}
				break;
			case 12:
				if (mobileNumber.startsWith(prefix841) || mobileNumber.startsWith(prefix849plus)) {
					result = true;
				}
				break;
			case 13:
				if (mobileNumber.startsWith(prefix841plus)) {
					result = true;
				}
				break;
			}
		}

		return result;
	}

	/**
	 * Them ma code quoc gia vietnam vao so dien thoai
	 * 
	 * @author: BangHN
	 * @param mobileNumber
	 * @return: String
	 */
	public static String parseMobileNumber(String mobileNumber) {
		final String countryCode = "84";
		final String countryCodePlus = "+84";
		final String _9x = "9";
		final String _09x = "09";
		final String _1x = "1";
		final String _01x = "01";

		if (mobileNumber == null) {
			return null;
		}
		mobileNumber = mobileNumber.trim();

		if (mobileNumber.startsWith(countryCodePlus)) {
			mobileNumber = mobileNumber.substring(1);
		}

		String validatePhone = null;

		// phone start with "84" or "+84"
		if (mobileNumber.startsWith(countryCode)) {
			boolean isMobileNumber = (mobileNumber.length() == 11 && mobileNumber.substring(2).startsWith(_9x))
					|| (mobileNumber.length() == 12 && mobileNumber.substring(2).startsWith(_1x));
			if (isMobileNumber) {
				validatePhone = mobileNumber;
			}
		}
		// phone = "09xxxxxxxx" (like: 0987868686)
		else if (mobileNumber.length() == 10 && mobileNumber.startsWith(_09x)) {
			validatePhone = countryCode + mobileNumber.substring(1);
		}
		// phone = "1xxxxxxxx" (like: 01696999999)
		else if (mobileNumber.length() == 11 && mobileNumber.startsWith(_01x)) {
			validatePhone = countryCode + mobileNumber.substring(1);
		}

		return validatePhone;
	}

	public static String md5(String s) {
		byte[] defaultBytes = s.getBytes();
		try {
			MessageDigest algorithm;
			algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(defaultBytes);
			byte messageDigest[] = algorithm.digest();

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String hex = Integer.toHexString(0xFF & messageDigest[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return "";
	}

	/**
	 * Sinh md5 (code mobile server)
	 * 
	 * @author : BangHN Input: mat khau. Salt: ten dang nhap da lower. since :
	 *         1.0
	 */
	public static String generateHash(String input, String salt) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		// SHA or MD5
		MessageDigest md = MessageDigest.getInstance("MD5");
		String hash = "";
		if (null == salt) {
			salt = "";
		}
		input += salt;
		byte[] data = input.getBytes("US-ASCII");
		md.update(data);
		byte[] digest = md.digest();
		for (int i = 0; i < digest.length; i++) {
			String hex = Integer.toHexString(digest[i]);
			if (hex.length() == 1)
				hex = "0" + hex;
			hex = hex.substring(hex.length() - 2);
			hash += hex;
		}
		return hash;
	}

	/**
	 * Ma hoa pass word SHA-256
	 * yennth16
	 * @param input
	 * @param salt
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
     */
	public static String generateHashSHA256(String input, String salt, String type) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance(type);
		String hash = "";
		if (null == salt  || "".equals(salt)) {
			salt = "";
		}
		input += salt;
		byte[] data = input.getBytes("US-ASCII");
		md.update(data);
		byte[] digest = md.digest();
		for (int i = 0; i < digest.length; i++) {
			String hex = Integer.toHexString(digest[i]);
			if (hex.length() == 1)
				hex = "0" + hex;
			hex = hex.substring(hex.length() - 2);
			hash += hex;
		}
		return hash;
	}
	/**
	 * Gets the random string.
	 *
	 * @param length
	 *            the length
	 *
	 * @return the random string
	 */
	public static String getRandomString(int length) {
		Random rand = new Random(System.currentTimeMillis());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int pos = rand.nextInt(charset.length());
			sb.append(charset.charAt(pos));
		}
		return sb.toString();
	}
	/**
	 * Tao mot string tien duoc ngan cach boi dau cham vi du :1.600.000
	 * 
	 * @author: PhucNT6
	 * @modified: TamPQ - ThanhNN chuyen '.' -> ','
	 * @return: String
	 */
	public static String parseAmountMoney(String money) {
		String result = "";
		if (!isNullOrEmpty(money)) {
			int index = money.indexOf(".");
			if(index >= 0) {
				result = money.substring(index, money.length());
				money =  money.substring(0, index);
			}
			for (int i = money.length() - 1; i >= 0; i--) {
				int offsetLast = money.length() - 1 - i;
				if ((offsetLast > 0) && (offsetLast % 3) == 0 && (money.charAt(i) != '+') && (money.charAt(i) != '-'))
					result = "," + result;
				result = money.charAt(i) + result;
			}

			if (result.substring(0, 1).equals(",")) {
				result.substring(1, result.length() - 1);
			}
		}
		return result;
	}
//	public synchronized String encrypt(String plaintext) throws Exception {
//		MessageDigest md = null;
//		String hash = null;
//		try {
//			md = MessageDigest.getInstance("SHA-1"); //step 2
//			md.update(plaintext.getBytes("UTF-8")); //step 3
//			byte raw[] = md.digest(); //step 4
//			hash = (new BASE64Encoder()).encode(raw); //step 5
//		} catch (NoSuchAlgorithmException e) {
//		} catch (UnsupportedEncodingException e) {
//		} catch (Exception e) {
//		}
//		return hash; //step 6
//	}


	/**
	 * 
	 * Mo ta chuc nang cua ham
	 * 
	 * @author: ThanhNN8
	 * @param money
	 *            - string tien can chuyen fomat
	 * @param unit
	 *            - chuoi cac so 0 cua don vi format tuong ung, vi du 1000VND
	 *            thi unit = "000"
	 * @return
	 * @return: String
	 * @throws:
	 */
	public static String parseAmountMoney(String money, String unit) {
		String result = "";
		int lengthUnit = unit.length();
		boolean checkNumberNotIsZero = false;
		if (!isNullOrEmpty(money)) {
			for (int i = money.length() - 1; i >= 0; i--) {
				int offsetLast = money.length() - 1 - i;
				if (offsetLast < lengthUnit) {// check truong hop co unit
					if (money.charAt(i) != '0') {
						checkNumberNotIsZero = true;
					}
				}
				if (offsetLast < lengthUnit + 1) {
					if ((offsetLast > 0) && (offsetLast % lengthUnit) == 0 && (money.charAt(i) != '+')
							&& (money.charAt(i) != '-')) {
						if (checkNumberNotIsZero == true) {
							result = "." + result;
							checkNumberNotIsZero = false;
						} else {
							result = "";
						}
					}
				} else {
					if ((offsetLast > 0) && (offsetLast % 3) == 0 && (money.charAt(i) != '+')
							&& (money.charAt(i) != '-')) {
						result = "," + result;
					}
				}
				result = money.charAt(i) + result;
			}
		}
		return result;
	}

	public static String parseAmountMoney(double money) {
		String amount = decimalFormatSymbols("#.##", money);
		return parseAmountMoney(amount);
	}

	public static String parseAmountMoney(float money) {

		String amount = decimalFormatSymbols("#.##", money);
		return parseAmountMoney(amount);
	}

	public static String parseAmountMoney(long money) {
		String amount = decimalFormatSymbols("#.##", money);
		return parseAmountMoney(amount);
	}

	/**
	 * 
	 * hien thi 2 chu so sau ki tu thap phan su dung DecimalFormat("#.##")
	 * 
	 * @author: HaiTC3
	 * @param percent
	 * @return
	 * @return: String
	 * @throws:
	 */
	public static String parseTwoDigitsDecimalCharacter(float percent) {
		String strPercent = decimalFormatSymbols("#.##", percent);
		return strPercent;
	}

	/**
	 * 
	 * hien thi 2 chu so sau ki tu thap phan su dung DecimalFormat("#.##")
	 * 
	 * @author: HaiTC3
	 * @param percent
	 * @return
	 * @return: String
	 * @throws:
	 */
	public static String parseTwoDigitsDecimalCharacter(double percent) {
		String strPercent = decimalFormatSymbols("#.##", percent);
		return strPercent;
	}

	/**
	 * Cat khoang trang o giua String, giua 2 ky tu chi co duy nhat 1 khoang
	 * trang
	 * 
	 * @author: TruongHN
	 * @param source
	 * @return
	 * @return: String
	 * @throws:
	 */
	public static String itrim(String source) {
		if (!isNullOrEmpty(source)) {
			return source.replaceAll("\\b\\s{2,}\\b", " ");
		}
		return source;
	}

	/**
	 * Kiem tra co phai la so hay khong
	 * 
	 * @author: TruongHN
	 * @param in
	 * @return: boolean
	 * @throws:
	 */
	public static boolean checkIfNumber(String in) {
		boolean res = false;
		try {
			Integer.parseInt(in);
			res = true;
		} catch (NumberFormatException ex) {
			res = false;
		}
		return res;
	}

	/**
	 * Checks if is viettel phone number.
	 * 
	 * @author: PhucNT
	 * @param phoneNumber
	 *            the phone number
	 * @return true, if is viettel phone number
	 */
	public static boolean isViettelPhoneNumber(String phoneNumber) {
		boolean match = false;
		if (canBePhoneNumber(phoneNumber)) {
			String formatedPhone = parseMobileNumber(phoneNumber);
			if (formatedPhone != null) {
				Pattern p = Pattern.compile("^84(98|97|163|164|165|166|167|168|169)[0-9]*$");
				Matcher m = p.matcher(formatedPhone);
				match = m.matches();
			}
		}
		return match;
	}

	public static String dateToString(Date input, String format) throws Exception {
		try {
			String expectedPattern = "".equals(format) ? "dd/MM/yyyy" : format;
			SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);

			return formatter.format(input);

		} catch (Exception ex) {
			throw ex;
		}
	}

	public static String stringDateToString(String input, String format) throws Exception {
		try {
			String expectedPattern = "".equals(format) ? "dd/MM/yyyy" : format;
			SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);
			return formatter.format(stringToDate(input, format));

		} catch (Exception ex) {
			throw ex;
		}
	}

	public static Date stringToDate(String input, String format) throws Exception {
		try {
			String expectedPattern = "".equals(format) ? "dd/MM/yyyy" : format;
			SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);

			return formatter.parse(input);

		} catch (Exception ex) {
			throw ex;
		}
	}

	public static Date addDate(Date input, int number) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(input);

		cal.add(Calendar.DATE, number);

		return cal.getTime();
	}

	public static String changeString(String src, String change, String replace) {
		String result = "";
		result = src.replaceAll(change, replace);
		return result;
	}

	/**
	 * 
	 * Remove cac ky tu dat biet cua sql
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param searchText
	 * @param isAutocomplete
	 * @return
	 * @return: String
	 * @throws:
	 */
	public static String toOracleSearchText(String searchText, boolean isAutocomplete) {
		String[] splitString;
		StringBuilder text = new StringBuilder();
		String OpPat = ",;&"; // search operator pattern
		// String SpPat = "<>./!@#$%^*'\"-_():|[]~+{}?\\\n"; // special char
		// pattern
		char[] searchTextArr;
		boolean preCheck = true;

		// [DungNTM commented on Jan 10 - 2011: remove all special characters
		// later]
		// searchText = clearAllHTMLTags(searchText);
		// [end]

		if (!StringUtil.isNullOrEmpty(searchText)) {
			searchTextArr = searchText.toCharArray();

			// remove special char, keep operator char
			for (int i = 0; i < searchTextArr.length; i++) {
				if (STRING_SPECIAL_CHAR.indexOf(searchTextArr[i]) >= 0) {
					searchTextArr[i] = ' ';
				} else if (OpPat.indexOf(searchTextArr[i]) >= 0) {
					if (preCheck) {
						searchTextArr[i] = ' ';
					}
					preCheck = true;
				} else
					preCheck = false;
			}

			searchText = String.valueOf(searchTextArr).trim();
			if (StringUtil.isNullOrEmpty(searchText)) {
				return STRING_SEARCH_LIKE_ALL;
			}

			if (isAutocomplete)
				// searchText = "%" + text.toString().trim() + "%";
				searchText = searchText.trim() + "%";
			else
				searchText = searchText.trim();

			splitString = searchText.split(" ");
			// if (splitString.length > 1) {
			for (int i = 0; i < splitString.length; i++) {
				if (!"".equals(splitString[i])) {

					// if (!isAutocomplete) {
					for (int j = 0; j < oracleTextKeywords.length; j++) {
						if (oracleTextKeywords[j].equals(splitString[i].toUpperCase())) {
							splitString[i] = "{" + splitString[i] + "}";
							break;
						}
					}
					// }

					text.append(splitString[i] + " ");
				}
			}

			searchText = text.toString();

			// remove last operator if exist
			if (OpPat.indexOf(searchText.charAt(searchText.length() - 1)) >= 0) {
				searchText = searchText.substring(0, searchText.length() - 1);
			}
		} else
			return STRING_SEARCH_LIKE_ALL;

		// System.out.println("searchString:" + searchText);
		return searchText;
	}

	/**
	 * Get chuoi HEXA tu mang byte
	 * 
	 * @author banghn
	 * @param b
	 *            : mang byte
	 * @return
	 * @throws Exception
	 */
	public static String getHexString(byte[] b) throws Exception {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}

	/**
	 * Get chuoi HEXA tu mang byte nhanh hon
	 * 
	 * @author banghn
	 * @param raw
	 *            : mang byte
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getHexStringFaster(byte[] raw) throws UnsupportedEncodingException {
		byte[] hex = new byte[2 * raw.length];
		int index = 0;
		for (byte b : raw) {
			int v = b & 0xFF;
			hex[index++] = HEX_CHAR_TABLE[v >>> 4];
			hex[index++] = HEX_CHAR_TABLE[v & 0xF];
		}
		return new String(hex, "ASCII");
	}

	/**
	 * Get phone number khi mang la CDMA
	 * 
	 * @author banghn
	 * @return : phone number
	 */
	public static String getPhoneNumber() {
		TelephonyManager tm = (TelephonyManager) GlobalInfo.getInstance().getAppContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		String number = tm.getLine1Number();
		return number;
	}

	/**
	 * Get so serial number SIM
	 * 
	 * @author banghn
	 * @return: serial number SIM
	 */
	public static String getSimSerialNumber() {
		TelephonyManager tm = (TelephonyManager) GlobalInfo.getInstance().getAppContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		String number = tm.getSimSerialNumber();
		return number;
	}

	/**
	 * tinh % lam tron len
	 * 
	 * @author: TamPQ
	 * @param temp2
	 * @param temp3
	 * @return
	 * @return: intvoid
	 * @throws:
	 */
	public static int calcularPercent(double temp2, double temp3) {
		if (temp2 >= temp3) {
			return 100;
		} else {
			return (int) ((temp2 / temp3) * 100);
		}
	}

	/**
	 * escape special character of sql
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param sqlValue
	 * @return
	 * @return: String
	 * @throws:
	 */

	public static String escapeSqlString(String sqlValue) {
		sqlValue = sqlValue.replace("^", "^^");// escape char
		sqlValue = sqlValue.replace("_", "^_");// search 1 character
		sqlValue = sqlValue.replace("%", "^%");// search all

		return sqlValue;
	}

	/**
	 * Lay ten file tu chuoi duong dan URL Vi du:
	 * https://www.192.168.1.171/data/file.zip tra ve chuoi: "file"
	 * 
	 * @author: BANGHN
	 * @param url
	 *            Duong dan URL co chua file name
	 * @return
	 */
	public static String getFileNameRromURLString(String url) {
		String fileNameWithoutExtn = null;
		if (!isNullOrEmpty(url)) {
			String fileName = url.substring(url.lastIndexOf('/') + 1, url.length());
			fileNameWithoutExtn = fileName.substring(0, fileName.lastIndexOf('.'));
		}
		return fileNameWithoutExtn;
	}

	/**
	 * tao chuoi username tu nhung ky tu ban dau
	 * 
	 * @author: BangHN
	 * @param userName
	 * @return
	 * @return: String
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */
	public static String generateFullUserName(String userName) {
//		StringBuilder fullName = new StringBuilder();
//		if (StringUtil.isNullOrEmpty(userName)) {
//		} else if (userName.length() == 10) {
//			fullName.append(userName);
//		} else {
//			String zero = "0000000000";
//			int lengh = userName.length();
//			if (lengh > 2 && userName.substring(0, 2).toUpperCase().equals("GS")) {
//				fullName.append("GS" + zero.substring(0, 10 - lengh) + userName.substring(2));
//			} else if (lengh > 4 && userName.substring(0, 4).toUpperCase().equals("TBHV")) {
//				fullName.append("TBHV" + zero.substring(0, 10 - lengh) + userName.substring(4));
//			} else if (userName.charAt(0) <= '9' && userName.charAt(0) >= '0') {
//				fullName.append(zero.substring(0, 10 - lengh) + userName);
//			} else {
//				fullName.append(userName);
//			}
//		}
//		return fullName.toString();
		
		return userName;
	}

	/**
	 * count Char In String
	 * 
	 * @author: TamPQ
	 * @param action
	 * @return
	 * @return: intvoid
	 * @throws:
	 */
	public static int countCharInString(Character ch, String action) {
		int counter = 0;
		for (int i = 0; i < action.length(); i++) {
			if (action.charAt(i) == ch) {
				counter++;
			}
		}
		return counter;
	}

	/**
	 * chuyen format string kieu double neu .0 thi bo di
	 * 
	 * @author thanhnguyen
	 * @param doubleString
	 * @return
	 */
	public static String formatDoubleString(String doubleString) {
		String result = "";
		if (doubleString.length() > 2) {
			// check cho truong hop ".00"
			String subString2 = doubleString.substring(doubleString.length() - 3, doubleString.length());
			if (subString2.equals(".00")) {
				result = doubleString.substring(0, doubleString.length() - 3);
				return result;
			} else {
				result = doubleString;
			}
			String subString = doubleString.substring(doubleString.length() - 2, doubleString.length());
			if (subString.equals(".0")) {
				result = doubleString.substring(0, doubleString.length() - 2);
				return result;
			} else {
				result = doubleString;
			}
		} else {
			result = doubleString;
		}

		return result;
	}

	/**
	 * Tao mot string tien duoc ngan cach boi dau cham phay voi dang double vi
	 * du :1,600,000.05
	 * 
	 * @author thanhnguyen
	 * @return
	 */
	public static String parseAmountMoneyWithDoubleString(String money) {
		String result = "";
		if (!isNullOrEmpty(money)) {
			// check co dau "." hay khong
			int indexDot = 0;
			String tempMoney = money;
			for (int j = money.length() - 1; j >= 0; j--) {
				Character c = money.charAt(j);
				if (c.equals('.')) {
					indexDot = j;
					break;
				}
			}
			if (indexDot > 0) {
				tempMoney = money.substring(0, indexDot);
				tempMoney = StringUtil.parseAmountMoney(tempMoney);
				result = tempMoney + money.substring(indexDot, money.length());
			} else {
				result = StringUtil.parseAmountMoney(tempMoney);
			}
		}
		return result;
	}

	/**
	 * parse dau thap phan va dau gom nhom du :1,600,000.05
	 * 
	 * @author tampq
	 * @return
	 */
	public static String decimalFormatSymbols(String formatString, Object str) {
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(',');
		DecimalFormat df = new DecimalFormat(formatString, otherSymbols);
		return df.format(str);
	}
	
	/**
     * cal Progress from quota (Chi tieu) và attain (thuc hien)
     * 
     * @author: duongdt3
     * @since: 09:53:51 1 Nov 2013
     * @return: int
     * @throws:
     * @param quota
     *            chi tieu
     * @param attain
     *            thuc hien
     * @param isMax100 Neu muon > 100 -> 100           
     *            
     * @return
     */
	public static float calProgress(long quota, long attain, boolean isMax100) {
		float progress = 0;
		progress = (float) (quota > 0 ? attain * 1.0 * 100 / quota
			: attain > 0 ? 100 : 0);
		//Neu muon > 100
		if (progress > 100 && isMax100) {
			progress = 100;
		}
		//lam tron 2 chu so thap phan
		return (int)progress;
	}
	
	/**
	 * Tính số lượng tồn
	 * @author: duongdt3
	 * @since: 21:12:50 6 Nov 2013
	 * @return: long
	 * @throws:  
	 * @param quota chi tieu
	 * @param attain thuc hien
	 * @return 
	 */
	public static long calRemain(long quota, long attain) {
		long remain = quota - attain;
		if (remain < 0) {
		    remain = 0;
		}
		return remain;
	}
	
	/**
	 * Get sql paging from itemOnPage & page
	 * @author: duongdt3
	 * @since: 21:12:26 6 Nov 2013
	 * @return: String
	 * @throws:  
	 * @param itemOnPage
	 * @param page
	 * @return
	 */
	public static String getPagingSql(int itemOnPage, int page){
		int offset = (page - 1) * itemOnPage;
		int limit = itemOnPage;
		String result =  " limit "+limit +" offset "+offset ;
		
		return result;
	}
	/**
	 * Get customer code display for user view
	 * @return
	 */
	public static int getSTT(int page, int numberInPage) {
		int firstStt = ((page - 1) * numberInPage) + 1;
		return firstStt;
	}
	
	/**
	 * Get count row from sql
	 * @author: duongdt3
	 * @since: 21:12:09 6 Nov 2013
	 * @return: String
	 * @throws:  
	 * @param sql
	 * @return
	 */
	public static String getCountSql(String sql) {
		String result =  "SELECT count(*) FROM ( " + sql + " )";
		return result;
	}
	
	/**
	 * Get số tiền / 1000
	 * @author: duongdt3
	 * @since: 21:11:56 6 Nov 2013
	 * @return: long
	 * @throws:  
	 * @param number
	 * @return
	 */
	public static long getTienNhan1000(long number) {
		return number / 1000;
	}
	
	/**
	 * Convert QuantityAndConvfact to string
	 * @author: duongdt3
	 * @since: 10:19:43 8 Nov 2013
	 * @return: void
	 * @throws:  
	 * @param quantity
	 * @param convfact
	 */
	public static String getQuantityAndConvfactConvert(long quantity, long convfact) {
		String quantityStr = "";
		if (quantity == 0) {
			quantityStr = "0/0";
		}else if (convfact == 0){
			quantityStr = String.valueOf(quantity);
		}else{
			long thung = quantity / convfact;
			long le = quantity % convfact;
			quantityStr = thung +"/"+ le;
		}
		return quantityStr;
	}
	
	/**
	 * Get string from cursor sqlite, if not exists, null => defaultValue
	 * @param c
	 * @param colName
	 * @param defaultValue
	 * @return
	 */
	public static String getStringFromSQliteCursor(Cursor c, String colName, String defaultValue) {
		String result = defaultValue;
		if (c.getColumnIndex(colName) >= 0) {
			result = c.getString(c.getColumnIndex(colName));
			if (result == null) {
				result = defaultValue;
			}
		}
		return result;
	}

	/**
	 * Get int from cursor sqlite, if not exists, null => defaultValue
	 * @param c
	 * @param colName
	 * @param defaultValue
	 * @return
	 */
	public static int getIntFromSQliteCursor(Cursor c, String colName, int defaultValue) {
		int result = defaultValue;
		if (c.getColumnIndex(colName) >= 0) {
			result = c.getInt(c.getColumnIndex(colName));
		}
		return result;
	}

	/**
	 * Get int from cursor sqlite, if not exists, null => ""
	 * @author: trungnt56
	 * @since: 09:28:05 13 Nov 2013
	 * @return: String
	 * @throws:
	 * @param c
	 * @param colName
	 * @return
	 */
	public static int getIntFromSQliteCursor(Cursor c, String colName) {
		int result = getIntFromSQliteCursor(c, colName, 0);
		return result;
	}
	
	/**
	 * Get string from cursor sqlite, if not exists, null => ""
	 * @author: duongdt3
	 * @since: 09:28:05 13 Nov 2013
	 * @return: String
	 * @throws:  
	 * @param c
	 * @param colName
	 * @return
	 */
	public static String getStringFromSQliteCursor(Cursor c, String colName) {
		String result = getStringFromSQliteCursor(c, colName, "");
		return result;
	}
	
	/**
	 * Get long from cursor sqlite, if not exists => 0
	 * @author: duongdt3
	 * @since: 10:11:00 21 Nov 2013
	 * @param c
	 * @param colName
	 * @return
	 */
	public static long getLongFromSQliteCursor(Cursor c, String colName) {
		long result = getLongFromSQliteCursor(c, colName, 0);
		return result;
	}
	
	/**
	 * Get long from cursor sqlite, if  not exists => defaultValue
	 * @author: duongdt3
	 * @since: 10:11:00 21 Nov 2013
	 * @param c
	 * @param colName
	 * @param defaultValue
	 * @return
	 */
	public static long getLongFromSQliteCursor(Cursor c, String colName, long defaultValue) {
		long result = defaultValue;
		if (c.getColumnIndex(colName) >= 0) {
			result = c.getLong(c.getColumnIndex(colName));
		}
		return result;
	}
	
	/**
	 * Get float from cursor sqlite, if  not exists => 0
	 * @author: duongdt3
	 * @since: 10:11:00 21 Nov 2013
	 * @param c
	 * @param colName
	 * @return
	 */
	public static float getFloatFromSQliteCursor(Cursor c, String colName) {
		float result = getFloatFromSQliteCursor(c, colName, 0);
		return result;
	}
	
	public static double getDoubleFromSQliteCursor(Cursor c, String colName) {
		double result = getDoubleFromSQliteCursor(c, colName, 0);
		return result;
	}
	
	public static double getDoubleFromSQliteCursor(Cursor c, String colName, double defaultValue) {
		double result = defaultValue;
		if (c.getColumnIndex(colName) >= 0) {
			result = c.getDouble(c.getColumnIndex(colName));
		}
		return result;
	}
	
	/**
	 * Get float from cursor sqlite, if  not exists => defaultValue
	 * @author: duongdt3
	 * @since: 10:11:00 21 Nov 2013
	 * @param c
	 * @param colName
	 * @param defaultValue
	 * @return
	 */
	public static float getFloatFromSQliteCursor(Cursor c, String colName, float defaultValue) {
		float result = defaultValue;
		if (c.getColumnIndex(colName) >= 0) {
			result = c.getFloat(c.getColumnIndex(colName));
		}
		return result;
	}
	
	/**
	 * get percent string from float percent
	 * @author: duongdt3
	 * @since: 10:11:00 21 Nov 2013
	 * @return: String
	 * @throws:  
	 * @return
	 */
	public static String parsePercent(float percent) {
		String result = ((int) percent) + "%";
		return result;
	}

	/**
	 * Parse progress string 
	 * @author: duongdt3
	 * @since: 10:15:41 21 Nov 2013
	 * @return: String
	 * @throws:  
	 * @param progressSold
	 * @return
	 */
	public static String parseProgressSold(float progressSold) { 
		String result = parseTwoDigitsDecimalCharacter(progressSold) + "%";
		return result;
	}

	/**
	 * Bo dau phay dau tien neu co trong list ,T2,T4,T6
	 * @author: duongdt3
	 * @since: 11:25:38 26 Nov 2013
	 * @return: String
	 * @throws:  
	 * @return
	 */
	public static String removeFirstComma(String stringWantRemoveComma) {
		if (stringWantRemoveComma!=null) {
			stringWantRemoveComma =  stringWantRemoveComma.trim();
			if(stringWantRemoveComma.startsWith(",")){
				stringWantRemoveComma = stringWantRemoveComma.substring(1);
				stringWantRemoveComma = stringWantRemoveComma.trim();
			}
		}
		return stringWantRemoveComma;
	}

	/**
	 * get Underline text, using StringUtil.getHTMLText(text) get HTML Span
	 * @author: duongdt3
	 * @since: 12:38:52 26 Nov 2013
	 * @return: Spanned
	 * @throws:  
	 * @param text
	 * @return
	 */
	public static String getUnderlineText(String text) {
		if (text == null) {
			text = "";
		}
		String str="<u>" +text +"</u>";
		return str;
	}
	
	/**
	 * get text color, using StringUtil.getHTMLText(text) get HTML Span
	 * @author: duongdt3
	 * @since: 14:46:12 6 Jan 2014
	 * @return: String
	 * @throws:  
	 * @param text
	 * @param color = getResources().getColor(R.color.x_color);
	 * @return
	 */
	public static String getColorText(String text, int color){
		if (text == null) {
			text = "";
		}
		String сolorString = String.format("%X", color).substring(2); // !!strip alpha value!!
		String result = String.format("<font color=\"#%s\">%s</font>", сolorString, text);
		return result;
	}
	/**
	 * get Bold text, using StringUtil.getHTMLText(text) get HTML Span
	 * @author: duongdt3
	 * @since: 14:40:32 26 Nov 2013
	 * @return: String
	 * @throws:  
	 * @param text
	 * @return
	 */
	public static String getBoldText(String text) {
		if (text == null) {
			text = "";
		}
		String str="<b>" +text +"</b>";
		return str;
	}
	
	/**
	 * get HTML text
	 * @author: duongdt3
	 * @since: 14:36:35 26 Nov 2013
	 * @return: Spanned
	 * @throws:  
	 * @param text
	 * @return
	 */
	public static Spanned getHTMLText(String text) {
		return Html.fromHtml(text);
	}
	/**
	 * display double data in TextView
	 * @author: duongdt3
	 * @since: 14:39:50 2 Apr 2015
	 * @return: void
	 * @throws:
	 * @param tv
	 * @param number
	 */
	public static void display(TextView tv, double number){
		display(tv, StringUtil.formatNumber(number));
	}
	/**
	 * format double number with format  #,##0.00 (cấu hình dấu , . + số lượng số sau thập phân)
	 * @author: duongdt3
	 * @since: 10:53:22 31 Mar 2015
	 * @return: String
	 * @throws:
	 * @param number
	 * @return
	 */
	public static String formatNumber(double number) {
		String result = formatNumber(number, true);
		return result;
	}
	private static String formatNumber(Object number, boolean isUsingDigitDecimal) {
		int sysDecimalPoint = GlobalInfo.getInstance().getSysDecimalPoint();
		//2 la dung dau cham ngan cach phan thap phan
		char numberSeparator = (sysDecimalPoint == 2) ? '.' : ',';
		//se dung dau con lai ngan cach nhom 3 chu so
		char groupingSeparator = (sysDecimalPoint == 2) ? ',' : '.';
		String format = "#,##0";
		if (isUsingDigitDecimal) {
			//so luong chu so phan thap phan
			int numDot = GlobalInfo.getInstance().getSysDigitDecimal();
			if (numDot > 0) {
				format += ".";
				for (int i = 0; i < numDot; i++) {
					format += "0";
				}
			}
		}

		return formatNumber(number, format, numberSeparator, groupingSeparator);
	}
	/**
	 * format object number with format pattern
	 * @author: duongdt3
	 * @since: 11:03:23 31 Mar 2015
	 * @return: String
	 * @throws:
	 * @param number
	 * @param format
	 * @param numberSeparator
	 * @param groupingSeparator
	 * @return
	 */
	private static String formatNumber(Object number, String format, char numberSeparator, char groupingSeparator) {
		String result = EMPTY_STRING;
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
		otherSymbols.setDecimalSeparator(numberSeparator);
		otherSymbols.setGroupingSeparator(groupingSeparator);
		DecimalFormat df = new DecimalFormat(format, otherSymbols);
		result = df.format(number);
		return result;
	}
	/**
	 * display percent
	 * @author: duongdt3
	 * @since: 11:10:40 3 Apr 2015
	 * @return: void
	 * @throws:
	 * @param tv
	 */
	public static void displayPercent(TextView tv, double percent){
		display(tv, StringUtil.formatNumber(percent) + "%");
	}
	/**
	 * display CharSequence data in TextView
	 * @author: duongdt3
	 * @since: 14:40:37 2 Apr 2015
	 * @return: void
	 * @throws:
	 * @param tv
	 * @param str
	 */
	public static void display(TextView tv, CharSequence str){
		if (tv != null) {
			tv.setText(str);
		} else{
			VTLog.e("display TextView", "TextView null with data: " + str);
		}
	}

	/**
	 * Dinh dang so luong san pham theo dang thung / hop
	 * @param numberProduct
	 * @param convfact
     * @return
     */
	public static String formatNumberProductFlowConvfact(long numberProduct,
														 int convfact) {
		StringBuilder availableProductFormat = new StringBuilder();
		availableProductFormat.append("0/0");

		if (numberProduct != 0 && convfact != 0) {
			availableProductFormat.setLength(0);
			availableProductFormat
					.append(parseAmountMoney(numberProduct / convfact))
					.append("/")
					.append(parseAmountMoney(numberProduct % convfact));
		}
		return availableProductFormat.toString();
	}

	// loai tag
	public static final String TYPE_TAG = "TYPE_TAG";
	// du lieu tag
	public static final String DATA_TAG = "DATA_TAG";
	public static final String DATA_CONVFACT = "DATA_CONVFACT";
	// tag nhap thay doi so luong thuc dat
	public static final String TAG_INPUT_QUANTITY = "QUANTITY";
	// tag nhap thay doi gia
	public static final String TAG_INPUT_PRICE = "PRICE";
	// input money
	public static final String TAG_INPUT_MONEY = "MONEY";
	// input convfact contain "/"
	public static final String TAG_INPUT_CONVFACT = "CONVFACT";
	// input percent contain "."
	public static final String TAG_INPUT_PERCENT = "PERCENT";
	// lam tron tu nhien
	public static final int MATH_ROUND = 1;
	//lam tron len
	public static final int MATH_CEIL = 2;
	//lam tron xuong
	public static final int MATH_FLOOR = 3;
	// DATA
	public static final String DATA = "DATA";
	/**
	 * show dialog nhập dữ liệu
	 * @param ed
	 * @param message
     */
	public static void initInputDialog(EditText ed, final String message) {
		ed.setFocusable(false);
		ed.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				final EditText input = new EditText(v.getContext());
				String strContent = ((EditText) v).getText().toString();
				input.setText(strContent.replaceAll(",", ""));
				input.setSingleLine(true);
				input.setTextColor(ImageUtil.getColor(R.color.WHITE));
				input.setImeOptions(EditorInfo.IME_ACTION_NEXT);
				String titleDialog = message;
				Bundle bun = (Bundle) v.getTag();
				String type = bun.getString(StringUtil.TYPE_TAG);
				String data = bun.getString(StringUtil.DATA_TAG);
				int convfact = bun.getInt(DATA_CONVFACT);
				if (type.equals(TAG_INPUT_MONEY)) {
					GlobalUtil.setFilterInputMoneyFloat(input,
							Constants.MAX_LENGHT_DISCOUNT_MONEY);
				}
				else if (type.equals(TAG_INPUT_PERCENT)) {
					GlobalUtil.setFilterInputPercentFloat(input,
							Constants.MAX_LENGHT_DISCOUNT_PERCENT);
				} else {
					if (type.equals(TAG_INPUT_PRICE)) {
						GlobalUtil.setFilterInputPrice(input,
								Constants.MAX_LENGHT_PRICE);
					} else if (type.equals(TAG_INPUT_QUANTITY)) {
						GlobalUtil.setFilterInputConvfact(input,
								Constants.MAX_LENGHT_QUANTITY);
					}
					titleDialog += Constants.STR_SPACE + data;
				}
				final AlertDialog alertDialog = new AlertDialog.Builder(v.getContext())
						.setTitle(titleDialog)
						.setView(input)
						.setPositiveButton(getString(R.string.TEXT_AGREE), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								Editable value = input.getText();
								if (!VTStringUtils.isNullOrEmpty(value.toString())) {
									String type = ((Bundle)v.getTag()).getString(StringUtil.TYPE_TAG);
									int convfact = ((Bundle)v.getTag()).getInt(DATA_CONVFACT);
									if (type.equals(TAG_INPUT_MONEY) || type.equals(TAG_INPUT_PERCENT)) {
										if (type.equals(TAG_INPUT_PERCENT)) {
											if (checkInputFloatValid(value.toString())) {
												if (Double.valueOf(value.toString()) > 100) {
													((EditText)v).setText("100");
												} else {
													((EditText)v).setText(value.toString());
												}
											}
										} else {
											((EditText)v).setText(formatViewValue(Double.valueOf(value.toString()), Constants.TYPE_FORMAT_FLOAT));
										}
									} else if (checkInputPatternValid(value.toString(), " ") && checkInputPatternValid(value.toString(), ".")) {
										if (!StringUtil.isNullOrEmpty(value.toString()) && !value.toString().equals("0")) {
											if (!StringUtil.isStringContainValidChars(value.toString(), '/')) {
												if(Long.valueOf(value.toString()) > 0){
													((EditText)v).setText(GlobalUtil.formatNumberProductFlowConvfact(Long.valueOf(value.toString()), convfact));
												}else{
													((EditText)v).setText(GlobalUtil.formatQuantity("0"));
												}
											} else{
												((EditText)v).setText(GlobalUtil.formatQuantity(value.toString()));
											}
										}else{
											((EditText)v).setText(GlobalUtil.formatQuantity(value.toString()));
										}
									}
								} else {
									((EditText)v).setText(value.toString());
								}
								GlobalUtil.forceHideKeyboardInput((Activity)GlobalInfo.getInstance().getActivityContext(), input);
							}
						}).setNegativeButton(getString(R.string.TEXT_DENY), new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								GlobalUtil.forceHideKeyboardInput((Activity)GlobalInfo.getInstance().getActivityContext(), input);
							}
						}).show();
				//cho ban phim luon hien thi
				alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
				input.requestFocus();
				input.setOnKeyListener(new View.OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						// TODO Auto-generated method stub
						if (keyCode == KeyEvent.KEYCODE_ENTER) {
							alertDialog.getButton(Dialog.BUTTON_POSITIVE).performClick();
						}
						return false;
					}
				});
			}
		});
	}

	/**
	 * checkInputFloatValid
	 * @param value
	 * @return
     */
	public static boolean checkInputFloatValid(String value) {
		if (value.contains(".")) {
			String[] arr = value.split(Pattern.quote("."));
			if (arr.length <= 2 && arr.length > 1) {
				if (!VTStringUtils.isNullOrEmpty(arr[0])) {
					return true;
				}
			}
		} else {
			return true;
		}
		return false;
	}
	/**
	 * dinh dang hien thi tren view VD: 16,780,000.45
	 * @return: String
	 * @throws:
	 * @param value
	 * @param typeFormat
	 * @return
	 */
	public static String formatViewValue(Object value, String typeFormat) {
		String val = "0";
		if (typeFormat.equals(Constants.TYPE_FORMAT_FLOAT)) {
			val = String.format(Locale.US, "%,." + GlobalInfo.getInstance().cfNumFloat + typeFormat, value);
		} else if (typeFormat.equals(Constants.TYPE_FORMAT_INTEGER)) {
			val = String.format(Locale.US, "%," + typeFormat, value);
		}
		return val;
	}
	/**
	 * kiem tra input gia tri co cham dong hop le truoc khi parse du lieu
	 * @return: int
	 * @throws:
	 * @param value
	 * @return
	 */
	public static boolean checkInputPatternValid(String value, String pattern) {
		if (value.contains(pattern)) {
			String[] arr = value.split(Pattern.quote(pattern));
			if (arr.length > 0 && arr.length <= 2) {
				return true;
			} else if (arr.length > 0 && arr.length <= 3) {
				if (arr[1].contains("/") || arr[1].contains(" ")) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			return true;
		}
		return false;
	}
	/**
	 * set du lieu cho hien thi nhap thong tin trong dialog
	 * @return: Bundle
	 * @throws:
	 * @param type
	 * @param data
	 * @return
	 */
	public static Bundle setDataInputDialog(String type, String data, int convfact) {
		Bundle bun = new Bundle();
		bun.putString(TYPE_TAG, type);
		bun.putString(DATA_TAG, data);
		bun.putInt(DATA_CONVFACT, convfact);
		return bun;
	}

	/**
	 * Kiem tra chuoi co ky tu trong mang quy dinh hay ko
	 * @param text
     * @return
     */
	public static boolean isStringContainValidChars(String text, char character) {
		boolean isContain = false;
		char arr[]= text.toCharArray();
		for(int i=0;i<arr.length;i++){
			if (character == (arr[i])) {
				isContain = true;
				break;
			}
		}
		return isContain;
	}
}
