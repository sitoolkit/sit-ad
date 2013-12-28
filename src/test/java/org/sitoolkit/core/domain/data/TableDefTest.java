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
package org.sitoolkit.core.domain.data;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kuwahara
 */
public class TableDefTest {

	public TableDefTest() {
	}

	@Test
	public void testIsIndependent() {
		TableDef table = new TableDef();

		// 空の状態で実行可能であることを確認
		assertFalse(table.isDependent());

		ColumnDef col0 = new ColumnDef();
		table.addColumn(col0);

		// 主キーが無い状態で実行可能であることを確認
		assertFalse(table.isDependent());

		ColumnDef col1 = new ColumnDef();
		col1.setPk(1);

		table.addColumn(col1);

		// 「独立テーブルである」という判定が行われることを確認
		assertFalse(table.isDependent());


		table = new TableDef();
		ColumnDef col2 = new ColumnDef();
		col2.setPk(1);
		Map<String, String> map = new HashMap<>();
		map.put("外部キー_1\nt_table", "col1");
		col2.setForeignKey(map);
		table.addColumn(col2);

		// 「依存テーブルである」という判定が行われることを確認
		assertTrue(table.isDependent());

	}

	@Test
	public void testGetParentPname() {
		TableDef table = new TableDef();

		assertEquals("", table.getParentPname());

		ColumnDef col1 = new ColumnDef();
		col1.setPk(1);
		Map<String, String> map = new HashMap<>();
		map.put("1t_table", "col1");
		col1.setForeignKey(map);
		table.addColumn(col1);

		assertEquals("t_table", table.getParentPname());
	}

	public void testGetFileName() {
		TableDef table = new TableDef();
		table.setNo(1);
		table.setPname("m_user");

		assertEquals("tbm01_create_m_user.sql", table.getFileName());
	}
}
