/*
 * CommonUtil.java 
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.reizes.shiva.utils;

import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.net.IDN;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.UrlValidator;

/**
 * 공용 유틸리티
 * @author reizes
 * @since 2010.1.21
 */
public class CommonUtil {
	/**
	 * Date에서 yyyy-mm-dd 형태의 스트링 생성
	 * @param date - Date
	 * @return - yyyy-mm-dd
	 */
	public static String getDateString(Date date) {
		if (date != null) {
			return String.format("%1$tY.%1$tm.%1$td", date);
		}

		return null;
	}

	/**
	 * Date에서 yyyy-mm-dd 형태의 스트링 생성
	 * date 가 null 인경우 null 리턴
	 * @param date - Date
	 * @return - yyyy-mm-dd
	 */
	public static String getDateStringDash(Date date) {
		if (date != null) {
			return String.format("%1$tY-%1$tm-%1$td", date);
		}

		return null;
	}

	/**
	 * Date에서 yyyy-mm-dd 형태의 스트링 생성
	 * date 가 null 인경우 ""(빈문자열)로 리턴
	 * @param date
	 * @return
	 */
	public static String getDateStringDashNullToEmpty(Date date) {
		String dateString = getDateStringDash(date);
		return dateString == null ? "" : dateString;
	}

	/**
	 * Date에서 yyyy.mm.dd HH:MM 형태의 스트링 생성
	 * @param date - Date
	 * @return - yyyy.mm.dd HH:MM
	 */
	public static String getDateTimeString(Date date) {
		if (date != null) {
			return String.format("%1$tY.%1$tm.%1$td %1$tH:%1$tM:%1$tS", date);
		}

		return null;
	}

	/**
	 * Date에서 yyyy-mm-dd HH:MM:SS 형태의 스트링 생성(24시간 표시)
	 * @param date - Date
	 * @return - yyyy-mm-dd HH:MM:SS
	 */
	public static String getDateTimeStringDash(Date date) {
		if (date != null) {
			return String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS", date);
		}
		return null;
	}

	/**
	 * Date에서 yyyymmdd-HHMMSS 형태의 스트링 생성(24시간 표시)
	 * @param date - Date
	 * @return - yyyymmdd-HHMMSS
	 */
	public static String getDateTimeStringNoBlank(Date date) {
		if (date != null) {
			return String.format("%1$tY%1$tm%1$td-%1$tH%1$tM%1$tS", date);
		}
		return null;
	}

	/**
	 * 입력된 날짜를 기준으로 날짜를 더하거나 뺀 날을 돌려줌. 시간은 00:00:00 으로 리셋
	 * adjustAmount 값이 -이면 빼기 + 이면 더하기, 0 이면 현재 날짜에 시간만 리셋
	 */
	public static Date getAdjustDayResetTime(Date date, int adjustAmount) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.add(Calendar.DATE, adjustAmount);
		return cal.getTime();
	}

	/**
	 * 주어진 날짜의 하루 하루전 날짜를 구한다.
	 */
	public static Date getMinus1Day(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	/**
	 * 주어진 날짜의 하루 하루이후 날짜를 구한다.
	 */
	public static Date getPlus1Day(Date date) {
		if (date == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);
		return cal.getTime();
	}

	/**
	 * inputFormatStr 형식으로 받은 날짜를 Date 반환(yyyy-MM-dd) (yyyyMM)
	 */
	public static Date getDateFromStr(String inputDate, String inputFormatStr) {
		java.text.SimpleDateFormat formatterInput = new java.text.SimpleDateFormat(inputFormatStr);
		try {
			return formatterInput.parse(inputDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * HTML 표시를 위해 말줄임 및 escape 수행
	 * @param str -
	 * @param length - 말줄임표 포함 길이
	 * @return - 변환된 String
	 */
	public static String getAbbrHTMLString(String str, int length) {
		return StringEscapeUtils.escapeHtml(StringUtils.abbreviate(str, length));
	}

	/**
	 * StringEscapeUtils.escapeHtml wrapping
	 * @param str -
	 * @return -
	 */
	public static String escapeHtml(String str) {
		if (str == null) {
			return null;
		}

		return StringUtils.replace(StringEscapeUtils.escapeHtml(str), "\n", "<br/>");
	}

	/**
	 * StringEscapeUtils.escapeJavaScript wrapping
	 * @param str -
	 * @return -
	 */
	public static String escapeJavaScript(String str) {
		if (str == null) {
			return null;
		}

		return StringEscapeUtils.escapeJavaScript(str);
	}

	/**
	 * URLEncoder.encode wrapping
	 * @param uri -
	 * @return -
	 * @throws UnsupportedEncodingException -
	 */
	public static String encodeURI(String uri) throws UnsupportedEncodingException {
		if (uri == null) {
			return null;
		}

		return URLEncoder.encode(uri, "UTF-8");
	}

	/**
	 * URLDecoder.decode wrapping
	 * @param uri -
	 * @return -
	 * @throws UnsupportedEncodingException -
	 */
	public static String decodeURI(String uri) throws UnsupportedEncodingException {
		if (uri == null) {
			return null;
		}

		return URLDecoder.decode(uri, "UTF-8");
	}

	/**
	 * apache codec Base64 encoding
	 * @param str -
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encodeBase64(String str) throws UnsupportedEncodingException {
		return Base64.encodeBase64URLSafeString(str.getBytes("UTF-8"));
	}

	/**
	 * apache codec Base64 decoding
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String decodeBase64(String str) throws UnsupportedEncodingException {
		return new String(Base64.decodeBase64(str), "UTF-8");
	}

	public static String md5(String data) {
		return DigestUtils.md5Hex(data);
	}

	/**
	 * @param str 입력 String
	 * @return null 인경우 빈문자열
	 */
	public static String enforceNotNull(String str) {
		return str == null ? "" : str;
	}

	/**
	 * @param num 입력 숫자
	 * @return null 인경우 0
	 */
	public static String enforceNotNull(Integer num) {
		return num == null ? "0" : Integer.toString(num);
	}

	/**
	 * @param num 입력 숫자
	 * @return null 인경우 0
	 */
	public static String enforceNotNull(Long num) {
		return num == null ? "0" : Long.toString(num);
	}

	/**
	 * @param num 입력 숫자
	 * @return null 인경우 0.0
	 */
	public static String enforceNotNull(Double num) {
		return num == null ? "0.0" : Double.toString(num);
	}

	/**
	 * @param num 입력 날짜
	 * @return null 인경우 빈문자열
	 */
	public static String enforceNotNull(Date date) {
		return getDateStringDashNullToEmpty(date);
	}

	/**
	 * 배열의 마지막 요소를 리턴한다. 배열이 null 이거나 size 가 0이면 null 리턴
	 * @param list 대상 배열
	 */
	public static Object popFromList(List<?> list) {
		Object lastItem = null;
		if (list != null && list.size() > 0) {
			lastItem = list.get(list.size() - 1);
		}
		return lastItem;
	}

	/**
	 * API 에서 사용하는 path 형식으로 변환
	 * path 가 null 인경우 null 반환
	 * @param path 전체 경로
	 * @return "||" -> ">", 처음 존재하는 "^>" -> "" 으로 replace
	 */
	public static String getDirPathForApi(String path) {
		if (path == null) {
			return null;
		}
		String dispPath = path.replace("||", ">");
		dispPath = dispPath.replaceFirst("^>", "");
		return dispPath;
	}

	/**
	 * 숫자데이터를 100으로 나누고 소수점 두째자리까지를 문자열로 반환(반올림 하지 않음)
	 * 입력 숫자가 null 이거나 0 일경우 0.0 반환(기존 API 구조를 따르기 때문)
	 * @param input 입력 숫자
	 * @return 소수점 두째짜리까지 표현된 문자열
	 */
	public static String div100(Long input) {
		if (input == null || input == 0) {
			return "0.0";
		}
		Double fl = input / 100d;
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.FLOOR);
		return df.format(fl);
	}

	/**
	 * end 값이 더 큰 값이어야 한다.
	 * @param start
	 * @param end
	 * @return
	 */
	public static long daysBetweenTime(long start, long end) {
		if (start > end) {
			return -1;
		}
		long term = end - start;
		return term / (1000 * 60 * 60 * 24);
	}

	/**
	 * 소수점 둘째짜리 까지 표현(반올림 하지 않음)
	 * @param input
	 * @return
	 */
	public static String decimalPointTwo(Float input) {
		if (input == null || input == 0) {
			return "0.0";
		}
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.FLOOR);
		return df.format(input);
	}

	/**
	 * 소수점 둘째짜리 까지 표현(반올림 하지 않음)
	 * @param input
	 * @return
	 */
	public static String decimalPointTwo(Double input) {
		if (input == null || input == 0) {
			return "0.0";
		}
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.FLOOR);
		return df.format(input);
	}

	/**
	 * http 아 https URL 형식에 맞는지 검사
	 * @param url
	 * @return
	 * @throws MalformedURLException 
	 */
	public static boolean isValidHttpUrl(String url) {
		if (url.length() > 255) { // 255자 넘어가는 url 은 취급하지 않음
			return false;
		}

		String[] schemes = {"http", "https"};
		UrlValidator urlValidator = new UrlValidator(schemes);
		if (urlValidator.isValid(url)) {
			return true;
		}

		// 이 아래로는 호스트 이름에 대해서만 검사
		URL urlTemp;
		try {
			urlTemp = new URL(url);
		} catch (MalformedURLException e) {
			return false;
		}
		String forUnicodeUrl = urlTemp.getProtocol() + "://" + IDN.toASCII(urlTemp.getHost());
		if (urlValidator.isValid(forUnicodeUrl)) {
			// 한글도메인의 경우 http://안녕.com 처럼 www 가 없는경우 실패로 나와서 호스트 이름만 가져옴
			return true;
		}

		String regex = "([a-zA-Z0-9가-힣.\\-&/%=?:#$(),.+;~\\_]+)"; // 프로토콜을 제외한 영역에 대한 검사
		if (urlTemp.getHost().startsWith("\"")) {
			// 도메인 첫문자가 이상한걸로 시작하면 잘못된 URL
			return false;
		} else if (urlTemp.getHost().startsWith(".")) {
			// 도메인 첫문자가 이상한걸로 시작하면 잘못된 URL
			return false;
		} else if (urlTemp.getProtocol().startsWith("http") && urlTemp.getHost().matches(regex)) {
			return true;
		}

		return false;
	}

	/**
	 * url 호스트 정보를 추출해낸다. www 제거
	 * 입력받은 url 이 잘못된 형식이거나 해서 오류가 발생했을경우 "" 빈 문자열 리턴
	 * @param url
	 * @return
	 */
	public static String getUrlHost(String url) {
		String hostName = "";
		try {
			hostName = new URL(url).getHost();
			if (hostName.startsWith("www.")) {
				hostName = hostName.substring(4);
			}
		} catch (Exception e) {
		}
		return hostName;
	}

	/**
	 * 입력받은 두개의 URL 의 호스트 이름이 동일한지 비교. www. 는 제거하고 비교한다.
	 * @param url1
	 * @param url2
	 * @return
	 */
	public static boolean isEqualHost(String url1, String url2) {
		return CommonUtil.getUrlHost(url1).equals(CommonUtil.getUrlHost(url2));
	}

	public static String newlineToBR(String str) {
		return StringUtils.replace(str, "\n", "<BR/>");
	}
}
