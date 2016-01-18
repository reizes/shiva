/*
 * @(#)CharUtilTest.java $version 2012. 4. 24.
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
public class CharUtilTest {

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

	/**
	 * Test method for {@link com.reizes.shiva.utils.CharUtil#containsHanja(java.lang.String)}.
	 */
	@Test
	public void testContainsHanja() {
		assertFalse(CharUtil.containsHanja("해물야"));
		assertFalse(CharUtil.containsHanja("가천대학교메디컬캠퍼스"));
		assertFalse(CharUtil.containsHanja("금강바다마을"));
	}

	/**
	 * Test method for {@link com.reizes.shiva.utils.CharUtil#containsHangul(java.lang.String)}.
	 */
	@Test
	public void testContainsHangul() {
		assertTrue(CharUtil.containsHangul("해물야"));
		assertTrue(CharUtil.containsHangul("가천대학교메디컬캠퍼스"));
		assertTrue(CharUtil.containsHangul("금강바다마을"));
	}

	/**
	 * Test method for {@link com.reizes.shiva.utils.CharUtil#isHanja(char)}.
	 */
	@Test
	public void testIsHanja() {
	}

	/**
	 * Test method for {@link com.reizes.shiva.utils.CharUtil#isHangul(char)}.
	 */
	@Test
	public void testIsHangul() {
	}

}
