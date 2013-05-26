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
import org.sitoolkit.core.infra.repository.TableData;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author yuichi.kuwahara
 */
public class TableDataTest {
	
	public TableDataTest() {
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
	public void testSomeMethod() {
		TableData tableData = new TableData();
		tableData.add(new RowData("a", "1"));
		tableData.add(new RowData("b", "2"));
		tableData.add(new RowData("c", "3"));
		
		assertEquals("1\r\n2\r\n3", tableData.toString());
	}
}
