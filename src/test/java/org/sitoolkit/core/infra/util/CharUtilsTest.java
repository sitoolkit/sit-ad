/*
 * Copyright 2013 Monocrea Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sitoolkit.core.infra.util;

import org.sitoolkit.core.infra.util.CharUtils;
import java.util.Arrays;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author kuwahara
 */
public class CharUtilsTest {
	
	public CharUtilsTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Test
	public void testHex2bytes() {
		assertEqualBytes(new byte[0], CharUtils.hex2bytes(null));
		assertEqualBytes(new byte[0], CharUtils.hex2bytes(""));
		assertEqualBytes(new byte[0], CharUtils.hex2bytes("1"));
		assertEqualBytes(new byte[]{0, 65}, CharUtils.hex2bytes("0041"));
		assertEqualBytes(new byte[]{0, 65}, CharUtils.hex2bytes("00412"));
	}

	@Test
	public void testHex2str() {
		assertEquals("", CharUtils.hex2str(null));
		assertEquals("", CharUtils.hex2str(""));
		assertEquals(" ", CharUtils.hex2str("20"));
		assertEquals("ABC", CharUtils.hex2str("414243"));
	}
	
	@Test
	public void tesetDecs2hex() {
		assertEquals("01", CharUtils.decs2hex((byte)1));
		assertEquals("00010E0F", CharUtils.decs2hex((byte)0, (byte)1, (byte)14, (byte)15));
	}
	
	private void assertEqualBytes(byte[] expected, byte[] actual) {
		assertTrue("expected is " + Arrays.toString(expected) + " but actual was " + Arrays.toString(actual), Arrays.equals(expected, actual));
	}
}
