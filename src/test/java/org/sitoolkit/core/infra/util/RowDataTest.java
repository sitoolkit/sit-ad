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

import org.sitoolkit.core.infra.repository.RowData;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author yuichi.kuwahara
 */
public class RowDataTest {
	
	public RowDataTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

	@Test
	public void testToString() {
		RowData rowData = new RowData();
		rowData.setCellValue("c", 1);
		rowData.setCellValue("b", 2);
		rowData.setCellValue("a", 3);
		
		assertEquals("1,2,3", rowData.toString());
	}
}
