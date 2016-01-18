/*
 * @(#)AddrUtil.java $version 2012. 3. 14.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.reizes.shiva.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.reizes.shiva.utils.StringUtil;

/**
 * MergeUtil에서 주소 관련 메소드를 이동하고 새주소 관련 기능 추가
 * @author reizes
 */
public class AddrUtil {
	private static final Pattern PATTERN_NORMALIZE_ADDR = Pattern.compile("^([가-힣 ]+\\d*[시군구읍면동리가로])(?:\\s*\\d+\\s*~\\s*\\d+\\s*)?(?:\\s*\\d+[반통])?\\s*(.*)$");
	//private static final Pattern PATTERN_NORMALIZE_ADDR = Pattern.compile("^(.+[시군구읍면동리가로])(?: ?(?:\\d+~\\d+)? ?)(?: \\d+[반통])? ?((?:산\\s*)?[\\d-]*)(?:번지)?(?:[ [^\\d]].+)?$");
	private static final Pattern PATTERN_ADDR2_1 = Pattern.compile("^\\s*(산)?\\s*0*(\\d*)(?:\\s*(?:-|다시)\\s*0*(\\d*))?\\s*(?:번지|호)? *");
	private static final Pattern PATTERN_ADDR2_2 = Pattern.compile("^\\s*(산)?\\s*0*(\\d+)\\s*(?:번지\\s*0*(\\d+)\\s*호)\\s*");
	private static final Pattern PATTERN_NORMALIZE_ADDR1 = Pattern.compile("(?:\\d+([동리가로]))");
	private static final Pattern PATTERN_INVALID_ADDR3 = Pattern.compile("(?:[\\(\\[{].+[\\)\\]}])");
	
	// 2012.3.14 도로명 새주소 지원
	private static final Pattern PATTERN_NORMALIZE_ROAD_ADDR = 
		Pattern.compile("^(.+[시군구])\\s*(\\S+[읍면동리가])?\\s*(\\S+대?[로길](?:\\s*\\d+\\s*번?길)?)\\s+(\\d+)(?:(?:-|다시)(\\d+))?(번지)?[,\\s]?(\\s*.+)?$");

	private static String organizeAddr2(String san, String addr21, String addr22) {
		StringBuilder sb = new StringBuilder();
		if (san != null) {
			sb.append(san);
		}
		if (addr21 != null) {
			sb.append(addr21);
		}

		if (addr22 != null) { // 2012.9.3 178번지 3호 와 같은 스타일 대응
			if (sb.length() > 0) {
				sb.append('-');
			}
			sb.append(addr22);
		}

		return StringUtil.normalize(sb.toString());
	}

	/** 
	 * 상세주소(addr2)에서 번지정보만 얻어오는 함수
	 * 기존 방법
	 * - 첫 공백 앞부분 정보가 번지 정보라고 가정. 예) 238-1 000아파트 0동  0호
	 * - '번지'라는 글자가 있는 경우 제거
	 * 변경된 방법
	 * - 문자열 젤 앞에서 숫자 또는 숫자-숫자 형태로 되어 있는 값을 가져옴
	 * - 산으로 시작하는 경우 특별 처리 예) 산5-1번지
	 * 
	 * @param subaddr 번지 및 상세 주소
	 * @return 번지까지만의 주소문자열을 반환
	 */
	public static String getStreetAddr(String addr2) {
		if (addr2 != null) {
			Matcher matcher = PATTERN_ADDR2_2.matcher(addr2);

			boolean find = matcher.find();
			if (!find) {
				matcher = PATTERN_ADDR2_1.matcher(addr2);
				find = matcher.find();
			}

			if (find) {
				String san = matcher.group(1);
				String retaddr21 = StringUtil.normalize(matcher.group(2));
				String retaddr22 = StringUtil.normalize(matcher.group(3));

				return organizeAddr2(san, retaddr21, retaddr22);
			}
		}

		return null;
	}

	/**
	 * 정자1동 -> 정자동 형태로 일반화한 주소 반환
	 * @param addr1
	 * @return
	 */
	public static String getNormalizedAddr1(String addr1) {
		if (addr1 != null) {
			return PATTERN_NORMALIZE_ADDR1.matcher(addr1).replaceAll("$1");
		}

		return addr1;
	}

	public static String normalizeAddr(String addr) {
		if (addr != null) {
			Matcher matcher = PATTERN_NORMALIZE_ADDR.matcher(addr);
			if (matcher.find()) {
				String addr1 = StringUtil.normalize(matcher.group(1));
				String tmpAddr2 = StringUtil.normalize(matcher.group(2));
				String addr2 = getStreetAddr(tmpAddr2);

				return (addr1 != null ? addr1 : "") + (addr2 != null ? " " + addr2 : "");
			}
		}
		return null;
	}

	/**
	 * 주소 1,2를 분리
	 * 2012.9.4 - 주소3 분리 추가
	 * @param addr
	 * @return
	 */
	public static String[] splitAddr(String addr) {
		String[] addrs = new String[3];
		if (addr != null) {
			Matcher matcher = PATTERN_NORMALIZE_ADDR.matcher(addr);
			if (matcher.find()) {
				addrs[0] = StringUtil.normalize(matcher.group(1));
				String tmpAddr2 = StringUtil.normalize(matcher.group(2));
				addrs[1] = getStreetAddr(tmpAddr2);
				addrs[2] = getAddr3FromAddr2(tmpAddr2);
			}
		}
		return addrs;
	}

	/**
	 * 공백 두 개 등을 제거하고 정리
	 * @param addr1
	 * @return
	 */
	public static String normalizeAddr1(String addr1) {
		addr1 = StringUtil.normalize(addr1);

		if (addr1 != null) {
			return StringUtils.join(StringUtils.split(addr1, ' '), ' ');
		}

		return addr1;
	}

	/**
	 * 입력된 주소가 도로명 주소인지 검사
	 * @param addr
	 * @return
	 */
	public static boolean isRoadAddr(String addr) {
		if (addr != null) {
			Matcher matcher = PATTERN_NORMALIZE_ROAD_ADDR.matcher(addr);
			boolean ret = matcher.find();

			return ret && StringUtil.normalize(matcher.group(6)) == null; // 번지/가 .. 이것들이 있으면 도로명 주소가 아니다.
		}

		return false;
	}

	/**
	 * 입력된 도로면 주소를 주소 0, 1,2로 분리 (common은 시/군/구 까지로 구주소와 같음)
	 * @param addr
	 * @return
	 */
	public static String[] splitRoadAddr(String addr) {
		String[] addrs = new String[5];
		if (addr != null) {
			Matcher matcher = PATTERN_NORMALIZE_ROAD_ADDR.matcher(addr);
			if (matcher.find()) {
				addrs[0] = StringUtil.normalize(matcher.group(1)); // 시/군/구
				addrs[1] = StringUtil.normalize(matcher.group(2)); // 읍/면/동 - 새주소도 읍/면/동이 있을 수 있다.
				addrs[2] = StringUtil.normalize(StringUtils.join(StringUtils.split(matcher.group(3), ' '))); // 도로명 - 도로명은 공백이 없어야 한다.
				addrs[3] = StringUtil.normalize(matcher.group(4) + (matcher.group(5) != null ? "-" + matcher.group(5) : "")); // 지번

				String invalid = StringUtil.normalize(matcher.group(6)); // 번지/가 .. 이것들이 있으면 도로명 주소가 아니다.

				if (invalid != null) {
					addrs[3] = null;
				}
				// 2012.11.8 addr3 추가
				String addr3 = StringUtil.normalize(matcher.group(7));

				if (addr3 != null) {
					Matcher matcher2 = PATTERN_INVALID_ADDR3.matcher(addr3); // addr3에 필요없는 패턴 제거
					if (matcher2.find()) {
						addr3 = StringUtil.normalize(matcher2.replaceAll(""));
					}
					addrs[4] = StringUtil.normalize(addr3); // addr3
				}
			}
		}
		return addrs;
	}

	/**
	 * addr2에서 지번을 제외한 건물명 등을 추출한다.
	 * @param addr2
	 * @return
	 */
	public static String getAddr3FromAddr2(String addr2) {
		if (addr2 != null) {
			Matcher matcher2 = PATTERN_ADDR2_2.matcher(addr2);
			if (matcher2.find()) {
				return StringUtil.normalize(StringUtils.strip(matcher2.replaceAll(""), " ,/.\t-_"));
			} else {
				Matcher matcher = PATTERN_ADDR2_1.matcher(addr2);
				if (matcher.find()) {
					return StringUtil.normalize(StringUtils.strip(matcher.replaceAll(""), " ,/.\t-_"));
				}
				return StringUtil.normalize(addr2);
			}
		}

		return null;
	}

	/**
	 * addr2를 addr2, addr3 로 split한다.
	 * @param addr2
	 * @return
	 */
	public static String[] splitAddr2(String addr2) {
		return new String[] {getStreetAddr(addr2), getAddr3FromAddr2(addr2)};
	}

	/**
	 * 3개로 분리되어 있는 주소를 하나의 문자열로 합친다. 
	 * 각 주소를 합칠때는 공백을 추가
	 * 각 주소별로 앞뒤 trim
	 * @param addr1 주소1
	 * @param addr2 주소2
	 * @param addr3 주소3
	 * @return
	 */
	public static String mergeAddrs(String addr1, String addr2, String addr3) {
		StringBuffer address = new StringBuffer();
		address.append(addr1 == null ? "" : StringUtils.trim(addr1));
		if (addr2 != null) {
			String newAddr2 = StringUtils.trim(addr2);
			if (newAddr2.length() > 0) {
				address.append(" ");
				address.append(newAddr2);
			}
		}
		if (addr3 != null) {
			String newAddr3 = StringUtils.trim(addr3);
			if (newAddr3.length() > 0) {
				address.append(" ");
				address.append(newAddr3);
			}
		}
		return StringUtils.trim(address.toString());
	}

}
