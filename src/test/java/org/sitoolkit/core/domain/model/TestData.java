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

import org.sitoolkit.core.domain.data.ColumnDef;
import org.sitoolkit.core.domain.data.TableDef;

/**
 *
 * @author yuichi.kuwahara
 */
public class TestData {
	
	public static TableDef getTableDef1() {
		TableDef table = new TableDef();
		table.setPname( "m_table");
		
		ColumnDef col1 = new ColumnDef();
		col1.setPname("col1");
		col1.setPk(1);
		col1.setType("int");
		table.addColumn(col1);

   		ColumnDef col2 = new ColumnDef();
		col2.setPname("col2");
		col2.setPk(2);
		col2.setType("Date");
		table.addColumn(col2);

   		ColumnDef col3 = new ColumnDef();
		col3.setPname("col3");
		col3.setType("String");
		table.addColumn(col3);
		
		return table;
	}
}
