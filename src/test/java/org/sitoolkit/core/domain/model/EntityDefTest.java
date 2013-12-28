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
package org.sitoolkit.core.domain.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.sitoolkit.core.app.SourceCodeGenerator;
import org.sitoolkit.core.domain.data.ColumnDef;
import org.sitoolkit.core.domain.data.TableDef;
import org.sitoolkit.core.domain.java.EmbeddedIdDef;
import org.sitoolkit.core.domain.java.EntityDef;
import org.sitoolkit.core.domain.java.FieldDef;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author kuwahara
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:" + SourceCodeGenerator.APP_CTX_CONFIG_LOCATION)
public class EntityDefTest {

	private static final Logger log = LoggerFactory.getLogger(EntityDefTest.class);

	@Autowired
	private ApplicationContext appCtx;

	public EntityDefTest() {
	}

	/**
	 * 独立エンティティの場合
	 */
	@Test
	public void testNew_01() {
		TableDef table = new TableDef();
		table.setPname("m_domain");

		ColumnDef col1 = new ColumnDef();
		col1.setPname("col1");
		col1.setPk(1);
		table.addColumn(col1);

		ColumnDef col2 = new ColumnDef();
		col2.setPname("col2");
		table.addColumn(col2);

		EntityDef entity = appCtx.getBean("entityDef", EntityDef.class);
		entity.load(table);

		assertFalse(entity.isDependent());
		assertEquals("DomainEntity", entity.getPname());
		FieldDef id1 = entity.getIds().get(0);
		assertEquals("col1", id1.getPname());

		FieldDef field1 = entity.getFields().get(0);
		assertEquals("col2", field1.getPname());
		assertEquals("String", field1.getType());
	}

	/**
	 * 依存エンティティの場合
	 */
	@Test
	public void testNew_02() {
		TableDef table = new TableDef();
		table.setPname("m_sub_domain");

		ColumnDef col1 = new ColumnDef();
		col1.setPname("col1");
		col1.setPk(1);
		Map<String, String> map = new HashMap<>();
		map.put("1m_domain", "col1");
		col1.setForeignKey(map);
		table.addColumn(col1);

		EntityDef entity = appCtx.getBean("entityDef", EntityDef.class);
		entity.load(table);

		assertTrue(entity.isDependent());
		assertEquals("SubDomainEntity", entity.getPname());
		assertEquals("Domain", entity.getDomain());
	}

	/**
	 * カラム定義からフィールド定義が生成される確認 1カラム主キーの場合
	 */
	@Test
	public void testNew_03() {
		TableDef table = new TableDef();
		table.setPname("m_table");

		ColumnDef col1 = new ColumnDef();
		col1.setPname("col1");
		col1.setPk(1);
		col1.setType("char");
		table.addColumn(col1);

		ColumnDef col2 = new ColumnDef();
		col2.setPname("col2");
		col2.setType("varchar");
		table.addColumn(col2);

		EntityDef entity = appCtx.getBean("entityDef", EntityDef.class);
		entity.load(table);

		List<FieldDef> ids = entity.getIds();
		assertEquals(1, ids.size());
		assertEquals("String", entity.getIdType());
		assertEquals("String", ids.get(0).getType());
		assertEquals("col1", ids.get(0).getPname());

		List<FieldDef> fields = entity.getFields();
		assertEquals(1, fields.size());
		assertEquals("String", fields.get(0).getType());
		assertEquals("col2", fields.get(0).getPname());
	}

	/**
	 * カラム定義からフィールド定義が生成される確認 2カラム主キーの場合
	 */
	@Test
	public void testNew_04() {
		TableDef table = new TableDef();
		table.setPname("m_table");

		ColumnDef col1 = new ColumnDef();
		col1.setPname("col1");
		col1.setPk(1);
		col1.setType("int");
		table.addColumn(col1);

		ColumnDef col2 = new ColumnDef();
		col2.setPname("col2");
		col2.setPk(2);
		col2.setType("int");
		table.addColumn(col2);

		EntityDef entity = appCtx.getBean("entityDef", EntityDef.class);
		entity.load(table);

		List<FieldDef> ids = entity.getIds();
		assertEquals(1, ids.size());

		EmbeddedIdDef embeddedId = entity.getEmbeddedId();
		assertEquals("TableEntityPK", embeddedId.getPname());

		List<FieldDef> fields = embeddedId.getIds();
		assertEquals(2, fields.size());

	}
}
