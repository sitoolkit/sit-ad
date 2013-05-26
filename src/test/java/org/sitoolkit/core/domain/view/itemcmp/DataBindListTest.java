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
package org.sitoolkit.core.domain.view.itemcmp;

import org.sitoolkit.core.domain.view.itemcmp.DataBind;
import org.sitoolkit.core.domain.view.itemcmp.DataBindList;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 *
 * @author kuwahara
 */
public class DataBindListTest {

	@Test
	public void testLoad_101() {
		DataBindList list = new DataBindList();
		list.load("m_table.col_1");
		assertEquals(1, list.size());
		assertEquals("m_table", list.get(0).getTable());
		assertEquals("col_1", list.get(0).getColumn());

		list = new DataBindList();
		list.load("m_table_1.col_11\nm_table_2.col_21");
		assertEquals(2, list.size());
		assertEquals("m_table_1", list.get(0).getTable());
		assertEquals("col_11", list.get(0).getColumn());
		assertEquals("m_table_2", list.get(1).getTable());
		assertEquals("col_21", list.get(1).getColumn());

		list = new DataBindList();
		list.load(null);
		assertEquals(0, list.size());
		
		list = new DataBindList();
		list.load("");
		assertEquals(0, list.size());
	}
	
	@Test
	public void testToString_101() {
		DataBindList dataBinds = new DataBindList();
		
		assertEquals("", dataBinds.toString());
		
		DataBind dataBind1 = new DataBind();
		dataBinds.add(dataBind1);
		
		assertEquals("", dataBinds.toString());
		
		dataBind1.setTable("m_table");
		dataBind1.setColumn("col_1");
		
		assertEquals("m_table.col_1", dataBinds.toString());
		
		DataBind dataBind2 = new DataBind();
		dataBinds.add(dataBind2);
		
		assertEquals("m_table.col_1\n", dataBinds.toString());
		
		dataBind2.setTable("m_table");
		dataBind2.setColumn("col_2");
		
		assertEquals("m_table.col_1\nm_table.col_2", dataBinds.toString());
	}
}
