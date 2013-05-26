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
package org.sitoolkit.core.infra.repository;

import org.sitoolkit.core.infra.repository.RowData;
import org.sitoolkit.core.infra.repository.DocumentMapper;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.annotation.Resource;
import org.sitoolkit.core.app.SourceCodeGenerator;
import org.sitoolkit.core.infra.doc.KeyValuePairMap;
import org.sitoolkit.core.infra.repository.schema.Column;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author yuichi.kuwahara
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:" + SourceCodeGenerator.APP_CTX_CONFIG_LOCATION)
public class DocumentMapperTest {
	
	@Resource
	private DocumentMapper dm;
	
	@Test
	public void testMap_101() {
		RowData rowData = new RowData("文字列", "str");
		Column column = new Column();
		column.setProperty("strVal");
		column.setName("文字列");

		TestBean bean = new TestBean();
		dm.map(bean, column, rowData);
		
		assertEquals("str", bean.getStrVal());
	}
	

	@Test
	public void testMap_102() {
		RowData rowData = new RowData("整数", 100);
		Column column = new Column();
		column.setProperty("intVal");
		column.setName("整数");

		TestBean bean = new TestBean();
		dm.map(bean, column, rowData);
		
		assertEquals(100, bean.getIntVal());
	}

	@Test
	public void testMap_103() {
		RowData rowData = new RowData();
		rowData.setCellValue("階層1", "a");
		rowData.setCellValue("階層2", "b");
		rowData.setCellValue("階層3", "c");
		Column column = new Column();
		column.setProperty("listVal");
		column.setPattern("階層[0-9]");

		TestBean bean = new TestBean();
		dm.map(bean, column, rowData);
		
		assertEquals("a", bean.getListVal().get(0));
		assertEquals("b", bean.getListVal().get(1));
		assertEquals("c", bean.getListVal().get(2));
	}

	@Test
	public void testMap_104() throws IllegalAccessException, InvocationTargetException {
		RowData rowData = new RowData("マップ", "key1:val1\nkey2:val2");
		Column column = new Column();
		column.setProperty("kvmVal");
		column.setName("マップ");

		TestBean bean = new TestBean();
		dm.map(bean, column, rowData);
		
		assertEquals("key1", bean.getKvmVal().get("key1").getKey());
		assertEquals("val1", bean.getKvmVal().get("key1").getValue());
		assertEquals("key2", bean.getKvmVal().get("key2").getKey());
		assertEquals("val2", bean.getKvmVal().get("key2").getValue());
	}
	

	public class TestBean {
		private String strVal;
		private int intVal;
		private List<String> listVal;
		private KeyValuePairMap kvmVal;

		public int getIntVal() {
			return intVal;
		}

		public void setIntVal(int intVal) {
			this.intVal = intVal;
		}

		public String getStrVal() {
			return strVal;
		}

		public void setStrVal(String strVal) {
			this.strVal = strVal;
		}

		public List<String> getListVal() {
			return listVal;
		}

		public void setListVal(List<String> listVal) {
			this.listVal = listVal;
		}

		public KeyValuePairMap getKvmVal() {
			return kvmVal;
		}

		public void setKvmVal(KeyValuePairMap kvmVal) {
			this.kvmVal = kvmVal;
		}

		
	}
}
