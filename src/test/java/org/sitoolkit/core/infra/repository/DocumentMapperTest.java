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
import java.util.Map;
import javax.annotation.Resource;
import org.sitoolkit.core.app.SourceCodeGenerator;
import org.sitoolkit.core.infra.doc.KeyValuePairMap;
import org.sitoolkit.core.infra.repository.schema.Column;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sitoolkit.core.infra.repository.schema.ReplacePattern;
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

	/**
	 * <dl>
	 * <dt>ケース
	 * <dd>BeanのString型プロパティにマップ
	 * <dl>
	 */
	@Test
	public void testMapString() {
		RowData rowData = new RowData("文字列", "str(3文字)");
		Column column = new Column();
		column.setProperty("strVal");
		column.setName("文字列");

		TestBean bean = new TestBean();
		dm.map(bean, column, rowData);

		assertEquals("str(3文字)", bean.getStrVal());

		ReplacePattern replace = new ReplacePattern();
		replace.setPattern("\\([0-9]*文字\\)");
		replace.setReplacement("");
		column.getReplace().add(replace);

		dm.map(bean, column, rowData);

		assertEquals("str", bean.getStrVal());
	}


	/**
	 * <dl>
	 * <dt>ケース
	 * <dd>Beanのint型プロパティにマップ
	 * <dl>
	 */
	@Test
	public void testMapInt() {
		RowData rowData = new RowData("整数", 100);
		Column column = new Column();
		column.setProperty("intVal");
		column.setName("整数");

		TestBean bean = new TestBean();
		dm.map(bean, column, rowData);

		assertEquals(100, bean.getIntVal());
	}

	/**
	 * <dl>
	 * <dt>ケース
	 * <dd>BeanのList型プロパティにマップ
	 * <dl>
	 */
	@Test
	public void testMapList() {
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

	/**
	 * <dl>
	 * <dt>ケース
	 * <dd>Beanの{@link KeyValuePairMap}型プロパティにマップ
	 * <dl>
	 */
	@Test
	public void testMapKvm() {
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

	/**
	 * <dl>
	 * <dt>ケース
	 * <dd>BeanのMap型プロパティにマップ
	 * <dl>
	 */
	@Test
	public void testMapMap() {
		RowData rowData = new RowData();
		rowData.setCellValue("ケース_001", "a(1文字)");
		rowData.setCellValue("ケース_002", "b");
		rowData.setCellValue("ケース_003", "c");
		Column column = new Column();
		column.setProperty("mapVal");
		column.setPattern("ケース_([0-9]*)");

		TestBean bean = new TestBean();
		dm.map(bean, column, rowData);

		assertEquals("a(1文字)", bean.getMapVal().get("001"));
		assertEquals("b", bean.getMapVal().get("002"));
		assertEquals("c", bean.getMapVal().get("003"));

		ReplacePattern replace = new ReplacePattern();
		replace.setPattern("\\([0-9]*文字\\)");
		replace.setReplacement("");
		column.getReplace().add(replace);

		dm.map(bean, column, rowData);

		assertEquals("a", bean.getMapVal().get("001"));
	}

	/**
	 * <dl>
	 * <dt>ケース
	 * <dd>セル値の「はい」、「いいえ」をtrue, falseにマップ
	 * <dl>
	 */
	@Test
	public void testBooleanMap() {
		Column column = new Column();
		column.setProperty("booleanVal");
		column.setName("はい/いいえ");
		column.setTrueStr("はい");
		column.setFalseStr("いいえ");

		RowData rowData = new RowData(column.getName(), "はい");

		TestBean bean = new TestBean();
		dm.map(bean, column, rowData);
		assertEquals(true, bean.isBooleanVal());

		rowData.setCellValue(column.getName(), "いいえ");

		dm.map(bean, column, rowData);
		assertEquals(false, bean.isBooleanVal());
	}

	/**
	 * <dl>
	 * <dt>ケース
	 * <dd>セル値の「はい」、「いいえ」を1, 0に置換してマップ
	 * <dl>
	 */
	@Test
	public void testReplaceMap() {
		Column column = new Column();
		column.setProperty("intVal");
		column.setName("はい/いいえ");

		ReplacePattern replaceYes = new ReplacePattern();
		replaceYes.setPattern("はい");
		replaceYes.setReplacement("1");
		column.getReplace().add(replaceYes);

		ReplacePattern replaceNo = new ReplacePattern();
		replaceNo.setPattern("いいえ");
		replaceNo.setReplacement("0");
		column.getReplace().add(replaceNo);

		RowData rowData = new RowData(column.getName(), "はい");

		TestBean bean = new TestBean();
		dm.map(bean, column, rowData);
		assertEquals(1, bean.getIntVal());

		rowData.setCellValue(column.getName(), "いいえ");
		dm.map(bean, column, rowData);
		assertEquals(0, bean.getIntVal());

	}

	public class TestBean {
		private String strVal;
		private int intVal;
		private boolean booleanVal;
		private List<String> listVal;
		private KeyValuePairMap kvmVal;
		private Map<String, String> mapVal;

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

		public Map<String, String> getMapVal() {
			return mapVal;
		}

		public void setMapVal(Map<String, String> mapVal) {
			this.mapVal = mapVal;
		}

		public boolean isBooleanVal() {
			return booleanVal;
		}

		public void setBooleanVal(boolean booleanVal) {
			this.booleanVal = booleanVal;
		}


	}
}
