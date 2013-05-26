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
package org.sitoolkit.core.domain.view.nav;

import org.sitoolkit.core.domain.view.nav.NodeDef;
import org.sitoolkit.core.domain.view.nav.NodeType;
import javax.annotation.Resource;
import org.sitoolkit.core.infra.repository.DocumentMapper;
import org.sitoolkit.core.infra.repository.RowData;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sitoolkit.core.app.SourceCodeGenerator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author yuichi.kuwahara
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:" + SourceCodeGenerator.APP_CTX_CONFIG_LOCATION)
public class NodeDefTest {
	
	@Resource
	DocumentMapper dm;
	
	public NodeDefTest() {
	}

	/**
	 * ケース：designDoc.xmlを使用したマッピング
	 */
	@Test
	public void testDesignDoc() {
		RowData rowData = new RowData();
		rowData.setCellValue("No.", "1");
		rowData.setCellValue("画面/階層", "画面");
		rowData.setCellValue("画面名/階層名", "テスト画面");
		rowData.setCellValue("物理名", "testPage");
		rowData.setCellValue("ドメイン", "test");
		rowData.setCellValue("階層_1", "a");
		rowData.setCellValue("階層_2", "b");
		rowData.setCellValue("階層_3", "c");
		
		
		NodeDef node = dm.map("nodeDef", rowData, NodeDef.class);
		
		assertEquals(1, node.getNo());
		assertEquals("テスト画面", node.getName());
		assertEquals(NodeType.Page, node.getType());
		assertEquals("testPage", node.getPname());
		assertEquals("test", node.getDomain());
		assertArrayEquals(
				new String[]{"a", "b", "c"},
				node.getDirs());
	}
}
