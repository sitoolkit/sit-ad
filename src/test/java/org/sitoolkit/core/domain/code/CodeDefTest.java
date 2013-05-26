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
package org.sitoolkit.core.domain.code;

import org.sitoolkit.core.domain.code.CodeItemDef;
import org.sitoolkit.core.domain.code.CodeDef;
import java.util.List;
import org.sitoolkit.core.infra.util.SitFileUtils;
import org.sitoolkit.core.infra.repository.RowData;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kuwahara
 */
public class CodeDefTest {

	private static final Logger log = LoggerFactory.getLogger(CodeDefTest.class);
	
	public CodeDefTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Test
	public void testDeserialize() {
		List<CodeItemDef> codeItems = CodeDef.deserialize("コード：ラベル");
		assertEquals(1, codeItems.size());
		assertEquals("コード", codeItems.get(0).getCode());
		assertEquals("ラベル", codeItems.get(0).getLabel());
		
		codeItems = CodeDef.deserialize("コード1：ラベル1\nコード2：ラベル2\nコード3：ラベル3");
		assertEquals(3, codeItems.size());
		for(int i = 0; i < 3; i++) {
			assertEquals("コード" + (i + 1), codeItems.get(i).getCode());
			assertEquals("ラベル" + (i + 1), codeItems.get(i).getLabel());
		}
		
		codeItems = CodeDef.deserialize(null);
		assertTrue(codeItems.isEmpty());

		codeItems = CodeDef.deserialize("");
		assertTrue(codeItems.isEmpty());

	}
	
}
