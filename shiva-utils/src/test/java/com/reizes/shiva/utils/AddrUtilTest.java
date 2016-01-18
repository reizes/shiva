/*
 * @(#)AddrUtilTest.java $version 2012. 3. 14.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.reizes.shiva.utils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author reizes
 */
public class AddrUtilTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testNormalizeAddr() {
		assertEquals("서울 중구 을지로6가 18-185", AddrUtil.normalizeAddr("서울 중구 을지로6가 18-185 밀리오레5층 118"));
		assertEquals("서울 중구 을지로6가 18-185", AddrUtil.normalizeAddr("서울 중구 을지로6가 18-185밀리오레5층 118"));
		assertEquals("경기 광주시 오포읍 능평리 386", AddrUtil.normalizeAddr("경기 광주시 오포읍 능평리 1~199 386"));
		assertEquals("서울 중구 충무로1가 22-13", AddrUtil.normalizeAddr("서울 중구 충무로1가 22-13 대흥빌딩3층 303"));
		assertEquals("서울 중구 충무로1가 1772-7", AddrUtil.normalizeAddr("서울 중구 충무로1가 1772-7번지 201호"));
		assertEquals("경기도 성남시 분당구 정자동 178-3", AddrUtil.normalizeAddr("경기도 성남시 분당구 정자동 178-3 그린팩토리 3층"));
		assertEquals("경기도 성남시 분당구 정자동 178-3", AddrUtil.normalizeAddr("경기도 성남시 분당구 정자동 178-3그린팩토리 3층"));
		assertEquals("경기도 성남시 분당구 정자동 178-3", AddrUtil.normalizeAddr("경기도 성남시 분당구 정자동 178번지 3호"));
		assertEquals("경기도 성남시 분당구 정자동 178-3", AddrUtil.normalizeAddr("경기도 성남시 분당구 정자동 178번지 3호 그린팩토리 3층"));
		assertEquals("경기도 성남시 분당구 정자동 178-3", AddrUtil.normalizeAddr("경기도 성남시 분당구 정자동 178번지 3호그린팩토리 3층"));

		//assertEquals("서울 중구 을지로6가 18-185", AddrUtil.normalizeAddr("서울 중구 을지로6가 18-185밀리오레2층163"));
	}

	/**
	 * Test method for {@link com.reizes.cocoa.merge.MergeUtil#normalizeAddr1(java.lang.String)}.
	 */
	@Test
	public void testNormalizeAddr1() {
		assertNull(AddrUtil.normalizeAddr1(null));
		assertNull(AddrUtil.normalizeAddr1(""));
		assertEquals("성남시분당구정자동", AddrUtil.normalizeAddr1(" 성남시분당구정자동"));
		assertEquals("성남시분당구정자동", AddrUtil.normalizeAddr1(" 성남시분당구정자동   "));
		assertEquals("성남시 분당구 정자동", AddrUtil.normalizeAddr1(" 성남시 분당구 정자동   "));
		assertEquals("성남시 분당구 정자동", AddrUtil.normalizeAddr1(" 성남시    분당구    정자동   "));
	}

	/**
	 * Test method for {@link com.reizes.cocoa.merge.MergeUtil#normalizeTitle(java.lang.String)}.
	 * @throws URISyntaxException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	/*	@Test
		public void testNormalizeTitle() throws IOException, InterruptedException, URISyntaxException {
			assertNull(MergeUtil.normalizeTitle(null));
			assertNull(MergeUtil.normalizeTitle(""));
			assertEquals("엔에이치엔주식회사", MergeUtil.normalizeTitle(" 엔에이치엔 주식회사"));
			assertEquals("엔에이치엔주식회사", MergeUtil.normalizeTitle(" 엔에이치엔주식회사"));
			assertEquals("엔에이치엔", MergeUtil.normalizeTitle(" 엔에이치엔(주)"));
			assertEquals("엔에이치엔", MergeUtil.normalizeTitle(" 엔에이치엔(사회복지법인)"));
			assertEquals("nhn", MergeUtil.normalizeTitle(" NHN(사회복지법인)"));
		}*/

	/**
	 * Test method for {@link com.reizes.cocoa.merge.MergeUtil#normalizeAlias(java.lang.String, java.lang.String)}.
	 * @throws URISyntaxException 
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	/*	@Test
		public void testNormalizeAlias() throws IOException, InterruptedException, URISyntaxException {
			assertEquals("", MergeUtil.normalizeAlias(null, null));
			assertEquals("", MergeUtil.normalizeAlias("", ""));
			assertEquals("가나다;abc;def;마바사", MergeUtil.normalizeAlias("", "가나다;abc;def;마바사"));
			assertEquals("가나다;abc;def;마바사", MergeUtil.normalizeAlias("", "가나다;ABC;DEF;마바사"));
			assertEquals("가나다;abc;def;마바사", MergeUtil.normalizeAlias("XXX", "가나다;ABC;DEF;마바사"));
			assertEquals("abc;def;마바사", MergeUtil.normalizeAlias("가나다", "가나다;ABC;DEF;마바사"));
			assertEquals("가나다;abc;마바사", MergeUtil.normalizeAlias("def", "가나다;ABC;DEF;마바사"));
		}*/

	/**
	 * Test method for {@link com.reizes.cocoa.merge.MergeUtil#getStreetAddr(java.lang.String)}.
	 */
	@Test
	public void testGetStreetAddr() {
		assertEquals("1", AddrUtil.getStreetAddr("1"));
		assertEquals("1", AddrUtil.getStreetAddr("1번지"));
		assertEquals("1-2", AddrUtil.getStreetAddr("1-2 번지"));
		assertEquals("1-2", AddrUtil.getStreetAddr("01-2"));
		assertEquals("1-2", AddrUtil.getStreetAddr("1-02"));
		assertEquals("1-2", AddrUtil.getStreetAddr("01-02"));
		assertEquals("1", AddrUtil.getStreetAddr("01-"));
		assertEquals("1", AddrUtil.getStreetAddr("-01"));
		assertEquals("1", AddrUtil.getStreetAddr("1-"));
		assertEquals("1", AddrUtil.getStreetAddr("-1"));
		assertEquals("1-2", AddrUtil.getStreetAddr("01-2 번지"));
		assertEquals("1-2", AddrUtil.getStreetAddr("1-02 번지"));
		assertEquals("1-2", AddrUtil.getStreetAddr("01-02번지"));
		assertEquals("1", AddrUtil.getStreetAddr("01-번지"));
		assertEquals("1", AddrUtil.getStreetAddr("-01 번지"));
		assertEquals("1", AddrUtil.getStreetAddr("1-번지"));
		assertEquals("1", AddrUtil.getStreetAddr("-1 번지"));
		assertEquals("산1", AddrUtil.getStreetAddr("산1번지"));
		assertEquals("산1-1", AddrUtil.getStreetAddr("산1-1 번지"));
		assertEquals("1-2", AddrUtil.getStreetAddr("1-2 번지 아파트 5층"));
		assertEquals("680-2", AddrUtil.getStreetAddr("680-2 씨파크빌딩 3층 26호, 27호"));
		assertEquals("1699-2", AddrUtil.getStreetAddr("1699-2 승원빌딩 4, 5층"));
		assertEquals("190-8", AddrUtil.getStreetAddr("190-8,9"));
		assertEquals("178-3", AddrUtil.getStreetAddr("178번지 3호"));
		assertEquals("178-3", AddrUtil.getStreetAddr("178번지 3호 그린팩토리 4층"));
		assertEquals("1772-7", AddrUtil.getStreetAddr("1772-7번지 201호"));
		assertEquals("12-3456", AddrUtil.getStreetAddr("12다시3456"));
		assertEquals("190-8", AddrUtil.getStreetAddr(" 190-8,9"));
	}

	/**
	 * Test method for {@link com.reizes.cocoa.merge.MergeUtil#getNormalizedAddr1(java.lang.String)}.
	 */
	@Test
	public void testGetNormalizedAddr1() {
		assertNull(AddrUtil.getNormalizedAddr1(null));
		assertEquals("", AddrUtil.getNormalizedAddr1(""));
		assertEquals("경기도 성남시 정자동", AddrUtil.getNormalizedAddr1("경기도 성남시 정자동"));
		assertEquals("경기도 성남시 정자동", AddrUtil.getNormalizedAddr1("경기도 성남시 정자1동"));
		assertEquals("경기도 성남시 정자리", AddrUtil.getNormalizedAddr1("경기도 성남시 정자2리"));
	}

	/**
	 * Test method for {@link com.reizes.cocoa.merge.MergeUtil#splitAddr(java.lang.String)}.
	 */
	@Test
	public void testSplitAddr() {
		assertArrayEquals(new String[] {"강원도 속초시 중앙동", "472-23", null}, AddrUtil.splitAddr("강원도 속초시 중앙동 472-23"));
		assertArrayEquals(new String[] {"강원도 속초시 중앙동", "산472-23", null}, AddrUtil.splitAddr("강원도 속초시 중앙동 산472-23"));
		assertArrayEquals(new String[] {"강원도 속초시 중앙동", "472-23", null}, AddrUtil.splitAddr("강원도 속초시 중앙동 472-23번지"));
		assertArrayEquals(new String[] {"강원도 속초시 중앙동", "산472-23", null}, AddrUtil.splitAddr("강원도 속초시 중앙동 산472-23번지"));
		assertArrayEquals(new String[] {"강원도 속초시 중앙동", "472-23", null}, AddrUtil.splitAddr("강원도 속초시 중앙동 472-23 번지"));
		assertArrayEquals(new String[] {"강원도 속초시 중앙동", "산472-23", null}, AddrUtil.splitAddr("강원도 속초시 중앙동 산472-23 번지"));
		assertArrayEquals(new String[] {"강원도 속초시 중앙동", null, null}, AddrUtil.splitAddr("강원도 속초시 중앙동"));
		assertArrayEquals(new String[] {"강원도 양양군 손양면 오산리", "23-4", null}, AddrUtil.splitAddr("강원도 양양군 손양면 오산리 23-4"));
		assertArrayEquals(new String[] {"강원도 양양군 손양면 오산리", "산23-4", null}, AddrUtil.splitAddr("강원도 양양군 손양면 오산리 산23-4"));
		assertArrayEquals(new String[] {"강원도 강릉시 교1동", "1068", null}, AddrUtil.splitAddr("강원도 강릉시 교1동 1068"));
		assertArrayEquals(new String[] {"강원도 강릉시 교1동", "산1068", null}, AddrUtil.splitAddr("강원도 강릉시 교1동 산1068"));
		assertArrayEquals(new String[] {"전라북도 완주군 삼례읍", "1068", null}, AddrUtil.splitAddr("전라북도 완주군 삼례읍 1068"));
		assertArrayEquals(new String[] {"서울시 용산구", null, null}, AddrUtil.splitAddr("서울시 용산구"));
		assertArrayEquals(new String[] {"서울 용산구", null, null}, AddrUtil.splitAddr("서울 용산구"));
		assertArrayEquals(new String[] {"서울시 용산구", "1068", null}, AddrUtil.splitAddr("서울시 용산구 1068"));
		assertArrayEquals(new String[] {"서울시 용산구", "산1068", null}, AddrUtil.splitAddr("서울시 용산구 산1068"));
		assertArrayEquals(new String[] {"경기도 성남시 분당구 정자동", "178-3", null}, AddrUtil.splitAddr("경기도 성남시 분당구 정자동 178번지 3호"));
		assertArrayEquals(new String[] {"경기도 성남시 분당구 정자동", "178-3", "그린팩토리 3층"}, AddrUtil.splitAddr("경기도 성남시 분당구 정자동 178번지 3호 그린팩토리 3층"));
		assertArrayEquals(new String[] {"경기도 성남시 분당구 정자동", "178-3", "그린팩토리 3층"}, AddrUtil.splitAddr("경기도 성남시 분당구 정자동 178번지 3호그린팩토리 3층"));
		assertArrayEquals(new String[] {"경기도 성남시 분당구 정자동", "산178-3", "그린팩토리 3층"}, AddrUtil.splitAddr("경기도 성남시 분당구 정자동 산178번지 3호  그린팩토리 3층"));
		assertArrayEquals(new String[] {"경기도 성남시 분당구 정자동", "산178-3", "그린팩토리 3층"}, AddrUtil.splitAddr("경기도 성남시 분당구 정자동 산178번지 3호그린팩토리 3층"));
		assertArrayEquals(new String[] {"경기도 성남시 분당구 정자동", "178", "3호"}, AddrUtil.splitAddr("경기도 성남시 분당구 정자동 178 3호"));
		assertArrayEquals(new String[] {"경기도 성남시 분당구 정자동", "178-3", null}, AddrUtil.splitAddr("경기도 성남시 분당구 정자동 178-3"));
		assertArrayEquals(new String[] {"경기도 성남시 분당구 정자동", "178-3", "그린팩토리 3층"}, AddrUtil.splitAddr("경기도 성남시 분당구 정자동 178-3 그린팩토리 3층"));
		assertArrayEquals(new String[] {"경기도 성남시 분당구 정자동", "178-3", "그린팩토리 3층"}, AddrUtil.splitAddr("경기도 성남시 분당구 정자동 178-3그린팩토리 3층"));
		assertArrayEquals(new String[] {"경기도 성남시 분당구 판교동", "746", "외 3 필지"}, AddrUtil.splitAddr("경기도 성남시 분당구 판교동 746 외 3 필지"));
		assertArrayEquals(new String[] {"경기도 성남시 분당구 정자동", "22-13", "대흥빌딩3층 303"}, AddrUtil.splitAddr("경기도 성남시 분당구 정자동 22-13 대흥빌딩3층 303"));
		assertArrayEquals(new String[] {"경기도 성남시 분당구 정자동", "22-13", "대흥빌딩3층 303"}, AddrUtil.splitAddr("경기도 성남시 분당구 정자동 22 - 13 대흥빌딩3층 303"));
		assertArrayEquals(new String[] {"경기도 성남시 분당구 정자동", "123", null}, AddrUtil.splitAddr("경기도 성남시 분당구 정자동 123-"));
		assertArrayEquals(new String[] {"경기도 성남시 분당구 정자동", "123", null}, AddrUtil.splitAddr("경기도 성남시 분당구 정자동 123-번지"));
		assertArrayEquals(new String[] {"경기도 성남시 분당구 정자동", "31", null}, AddrUtil.splitAddr("경기도 성남시 분당구 정자동 -31"));
		assertArrayEquals(new String[] {"경기도 성남시 분당구 정자동", "31", null}, AddrUtil.splitAddr("경기도 성남시 분당구 정자동 31호"));
		assertArrayEquals(new String[] {"경기도 성남시 분당구 정자동", "245", null}, AddrUtil.splitAddr("경기도 성남시 분당구 정자동 -245번지"));
		assertArrayEquals(new String[] {"경기도 성남시 분당구 정자동", "123", null}, AddrUtil.splitAddr("경기도 성남시 분당구 정자동 0123-"));
		assertArrayEquals(new String[] {"경기도 성남시 분당구 정자동", "123", null}, AddrUtil.splitAddr("경기도 성남시 분당구 정자동 0123-번지"));
		assertArrayEquals(new String[] {"경기도 성남시 분당구 정자동", "31", null}, AddrUtil.splitAddr("경기도 성남시 분당구 정자동 -031"));
		assertArrayEquals(new String[] {"경기도 성남시 분당구 정자동", "245", null}, AddrUtil.splitAddr("경기도 성남시 분당구 정자동 -0245번지"));
		assertArrayEquals(new String[] {"경기도 성남시 분당구 정자동", "12-3456", null}, AddrUtil.splitAddr("경기도 성남시 분당구 정자동 12다시3456"));
	}

	@Test
	public void testSplitRoadAddr() {
		assertArrayEquals(new String[] {"서울특별시 종로구", null, "혜화로11길", "24", null}, AddrUtil.splitRoadAddr("서울특별시 종로구 혜화로11길 24 (명륜1가)"));
		assertArrayEquals(new String[] {"제주특별자치도 제주시", "영평동", "516로", "2810-31", null}, AddrUtil.splitRoadAddr("제주특별자치도 제주시 영평동 516로 2810-31"));
		assertArrayEquals(new String[] {"대전광역시 서구", null, "계백로1249번길", "174-82", null}, AddrUtil.splitRoadAddr("대전광역시 서구 계백로 1249번길 174-82"));
		assertArrayEquals(new String[] {"부산 기장군", "장안읍", "좌동길", null, null}, AddrUtil.splitRoadAddr("부산 기장군 장안읍 좌동길 591번지"));
		assertArrayEquals(new String[] {"전남 영암군", "영암읍", "낭주로", "49-1", null}, AddrUtil.splitRoadAddr("전남 영암군 영암읍 낭주로 49-1"));
		assertArrayEquals(new String[] {"경기도 수원시 영통구", null, "영통로217번길", "12", null}, AddrUtil.splitRoadAddr("경기도 수원시 영통구 영통로217번길 12 (영통동)"));
		assertArrayEquals(new String[] {"충남 아산시", "배방읍", "배방로173번길", "14-9", "1층  102호  ,  상가"}, AddrUtil.splitRoadAddr("충남 아산시 배방읍  배방로173번길  14-9,1층  102호  ,  상가"));
		assertArrayEquals(new String[] {"경기도 수원시 영통구", null, "영통로217번길", "12", "어문인문학부행정실"}, AddrUtil.splitRoadAddr("경기도 수원시 영통구 영통로217번길 12 어문인문학부행정실(퇴계인문관3층)"));
		assertArrayEquals(new String[] {"경기도 수원시 영통구", null, "영통로217번길", "12", "어문인문학부행정실"}, AddrUtil.splitRoadAddr("경기도 수원시 영통구 영통로217번길 12 어문인문학부행정실{퇴계인문관3층}"));
		assertArrayEquals(new String[] {"경기도 수원시 영통구", null, "영통로217번길", "12", "어문인문학부행정실"}, AddrUtil.splitRoadAddr("경기도 수원시 영통구 영통로217번길 12 어문인문학부행정실[퇴계인문관3층]"));
	}

	@Test
	public void testIsRoadAddr() {
		assertFalse(AddrUtil.isRoadAddr(null));
		assertFalse(AddrUtil.isRoadAddr(""));
		assertFalse(AddrUtil.isRoadAddr("강원도 양양군 손양면 오산리 23-4"));
		assertFalse(AddrUtil.isRoadAddr("서울시 용산구"));
		assertFalse(AddrUtil.isRoadAddr("서울특별시 종로구 종로1가 24"));
		assertFalse(AddrUtil.isRoadAddr("서울특별시 종로구 구로1동 24"));
		assertFalse(AddrUtil.isRoadAddr("경기도 수원시 영통구 영통동 977-3 영통2동주민센터"));
		assertTrue(AddrUtil.isRoadAddr("서울특별시 종로구 혜화로11길 24 (명륜1가)"));
		assertTrue(AddrUtil.isRoadAddr("제주특별자치도 제주시 영평동 516로 2810-31"));
		assertTrue(AddrUtil.isRoadAddr(" 대전광역시 서구 계백로 1249번길 174-82"));
		assertFalse(AddrUtil.isRoadAddr(" 부산 기장군 장안읍 좌동길 591번지"));
		assertTrue(AddrUtil.isRoadAddr(" 전남 영암군 영암읍 낭주로 49-1"));
		assertTrue(AddrUtil.isRoadAddr("경기도 수원시 영통구 영통로217번길 12 (영통동) "));
	}

	@Test
	public void testGetAddr3FromAddr2() {
		assertEquals("밀리오레5층 118", AddrUtil.getAddr3FromAddr2("18-185 밀리오레5층 118"));
		assertEquals("밀리오레5층 118", AddrUtil.getAddr3FromAddr2("18-185밀리오레5층 118"));
		assertEquals("밀리오레5층 118", AddrUtil.getAddr3FromAddr2("18-185번지 밀리오레5층 118"));
		assertEquals("밀리오레5층 118", AddrUtil.getAddr3FromAddr2("18-185번지밀리오레5층 118"));
		assertEquals("밀리오레5층 118", AddrUtil.getAddr3FromAddr2("18-185 번지 밀리오레5층 118"));
		assertEquals("밀리오레5층 118", AddrUtil.getAddr3FromAddr2("18-185 번지밀리오레5층 118"));
		assertNull(AddrUtil.getAddr3FromAddr2("386"));
		assertNull(AddrUtil.getAddr3FromAddr2("386-12"));
		assertNull(AddrUtil.getAddr3FromAddr2("386번지"));
		assertNull(AddrUtil.getAddr3FromAddr2("386-12번지"));
		assertNull(AddrUtil.getAddr3FromAddr2("386 번지"));
		assertNull(AddrUtil.getAddr3FromAddr2("386-12 번지"));
		assertEquals("대흥빌딩3층 303", AddrUtil.getAddr3FromAddr2("22-13 대흥빌딩3층 303"));
		assertEquals("평화시장2층 가257", AddrUtil.getAddr3FromAddr2("평화시장2층 가257"));
		assertEquals(null, AddrUtil.getAddr3FromAddr2("178번지 3호"));
		assertEquals("그린팩토리 4층", AddrUtil.getAddr3FromAddr2("178번지 3호 그린팩토리 4층"));
		assertEquals("죽전리 172-1 동부아파트 상가 106", AddrUtil.getAddr3FromAddr2("죽전리 172-1 동부아파트 상가 106"));
		assertEquals("201호", AddrUtil.getAddr3FromAddr2("1772-7번지 201호"));
		assertEquals("201호", AddrUtil.getAddr3FromAddr2("1772 - 7번지 201호"));
	}

	@Test
	public void testSplitAddr2() {
		assertArrayEquals(new String[] {"18-185", "밀리오레5층 118"}, AddrUtil.splitAddr2("18-185 밀리오레5층 118"));
		assertArrayEquals(new String[] {"18-185", "밀리오레5층 118"}, AddrUtil.splitAddr2("18-185밀리오레5층 118"));
		assertArrayEquals(new String[] {"18-185", "밀리오레5층 118"}, AddrUtil.splitAddr2("18-185번지 밀리오레5층 118"));
		assertArrayEquals(new String[] {"18-185", "밀리오레5층 118"}, AddrUtil.splitAddr2("18-185번지밀리오레5층 118"));
		assertArrayEquals(new String[] {"18-185", "밀리오레5층 118"}, AddrUtil.splitAddr2("18-185 번지 밀리오레5층 118"));
		assertArrayEquals(new String[] {"18-185", "밀리오레5층 118"}, AddrUtil.splitAddr2("18-185 번지밀리오레5층 118"));
		assertArrayEquals(new String[] {"18-185", "밀리오레5층 118"}, AddrUtil.splitAddr2("18번지 185호 밀리오레5층 118"));
		assertArrayEquals(new String[] {"18-185", "밀리오레5층 118"}, AddrUtil.splitAddr2("18번지 185호밀리오레5층 118"));
		assertArrayEquals(new String[] {"386", null}, AddrUtil.splitAddr2("386"));
		assertArrayEquals(new String[] {"386-12", null}, AddrUtil.splitAddr2("386-12"));
		assertArrayEquals(new String[] {"386", null}, AddrUtil.splitAddr2("386번지"));
		assertArrayEquals(new String[] {"386-12", null}, AddrUtil.splitAddr2("386-12번지"));
		assertArrayEquals(new String[] {"386", null}, AddrUtil.splitAddr2("386 번지"));
		assertArrayEquals(new String[] {"386-12", null}, AddrUtil.splitAddr2("386-12 번지"));
		assertArrayEquals(new String[] {"22-13", "대흥빌딩3층 303"}, AddrUtil.splitAddr2("22-13 대흥빌딩3층 303"));
		assertArrayEquals(new String[] {null, "평화시장2층 가257"}, AddrUtil.splitAddr2("평화시장2층 가257"));
		assertArrayEquals(new String[] {"178-3", null}, AddrUtil.splitAddr2("178번지 3호"));
		assertArrayEquals(new String[] {"178-3", "그린팩토리 4층"}, AddrUtil.splitAddr2("178번지 3호 그린팩토리 4층"));
		assertArrayEquals(new String[] {"1772-7", "201호"}, AddrUtil.splitAddr2("1772-7번지 201호"));
		assertArrayEquals(new String[] {"1772-7", "201호"}, AddrUtil.splitAddr2("1772 - 7번지 201호"));
		assertArrayEquals(new String[] {null, "삼호아파트"}, AddrUtil.splitAddr2("삼호아파트"));
		assertArrayEquals(new String[] {null, "성균관대학교 어문인문학부행정실(퇴계인문관3층)"}, AddrUtil.splitAddr2("성균관대학교 어문인문학부행정실(퇴계인문관3층)"));
	}

	@Test
	public void testMergeAddrs() {
		assertEquals("경기도 성남시 분당구 정자동 178-3 그린팩토리 3층", AddrUtil.mergeAddrs("경기도 성남시 분당구 정자동", "178-3", "그린팩토리 3층"));
		assertEquals("178-3 그린팩토리 3층", AddrUtil.mergeAddrs(null, "178-3", "그린팩토리 3층"));
		assertEquals("경기도 성남시 분당구 정자동 그린팩토리 3층", AddrUtil.mergeAddrs("경기도 성남시 분당구 정자동", null, "그린팩토리 3층"));
		assertEquals("경기도 성남시 분당구 정자동 178-3", AddrUtil.mergeAddrs("경기도 성남시 분당구 정자동", "178-3", null));

		assertEquals("경기도 성남시 분당구 정자동 그린팩토리 3층", AddrUtil.mergeAddrs("경기도 성남시 분당구 정자동", "	\t\n", "그린팩토리 3층"));
		assertEquals("경기도 성남시 분당구 정자동 178-3 그린팩토리 3층", AddrUtil.mergeAddrs("경기도 성남시 분당구 정자동", "\n178-3\t", "그린팩토리 3층"));
	}
}
