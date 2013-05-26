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
package org.sitoolkit.core.domain.view;

import org.sitoolkit.core.domain.view.ItemDef;
import static org.junit.Assert.assertEquals;
import org.sitoolkit.core.domain.view.itemcmp.DataBindList;

import org.junit.Test;

/**
 *
 * @author kuwahara
 */
public class ItemDefTest {

	@Test
	public void testGetId_1() {
		ItemDef item = new ItemDef();
		item.setPname("item1");
		
		assertEquals("item1", item.getId());
	}
	
	@Test
	public void testGetId_2() {
		ItemDef item = new ItemDef();
		item.setDataBindList(new DataBindList("m_user.user_id"));
		
		assertEquals("user-userId", item.getId());
	}

	@Test
	public void testGetId_3() {
		ItemDef item = new ItemDef();
		item.setName("項目１");
		
		assertEquals("項目１", item.getId());
	}
}
