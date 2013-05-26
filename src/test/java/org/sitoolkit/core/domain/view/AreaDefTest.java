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

import org.sitoolkit.core.domain.view.AreaDef;
import org.sitoolkit.core.domain.view.ItemDef;
import static org.junit.Assert.assertEquals;
import org.sitoolkit.core.domain.view.AreaDef.AreaType;
import org.sitoolkit.core.domain.view.itemcmp.DesignInfoType;
import org.sitoolkit.core.domain.view.itemcmp.NestedNamePair;

import org.junit.Test;

/**
 *
 * @author kuwahara
 */
public class AreaDefTest {
	
	@Test
	public void testInit_1() {
		AreaDef area = new AreaDef();
		ItemDef item = new ItemDef();
		item.setAreaStr(new NestedNamePair("area"));
		item.addDesignInfo(DesignInfoType.AreaPName, "areap");
		item.addDesignInfo(DesignInfoType.AreaType, AreaType.フォーム);
		item.addDesignInfo(DesignInfoType.FormLaneCount, 2);
		area.init("domain", item);
		
		assertEquals("domain", area.getDomain());
		assertEquals("area", area.getName());
		assertEquals("areap", area.getPname());
		assertEquals(AreaDef.AreaType.フォーム, area.getType());
		assertEquals(2, area.getFormLane());
	}
	
	@Test
	public void testInit_2() {
		AreaDef area = new AreaDef();
		ItemDef item = new ItemDef();
		item.setAreaStr(new NestedNamePair("area"));
		item.addDesignInfo(DesignInfoType.AreaType, "フォーム");
		area.init("domain", item);
		
		assertEquals("area", area.getName());
		assertEquals("", area.getPname());
		assertEquals(AreaDef.AreaType.フォーム, area.getType());
		assertEquals(1, area.getFormLane());
	}

}
