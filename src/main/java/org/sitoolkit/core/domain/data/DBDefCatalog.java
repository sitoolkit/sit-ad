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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.sitoolkit.core.infra.repository.DocumentMapper;
import org.sitoolkit.core.infra.repository.DocumentRepository;
import org.sitoolkit.core.infra.repository.RowData;
import org.sitoolkit.core.infra.repository.TableData;
import org.sitoolkit.core.infra.repository.TableDataCatalog;
import org.sitoolkit.core.infra.srccd.SourceCodeCatalog;
import org.sitoolkit.core.infra.util.PropertyManager;

/**
 *
 * @author yuichi.kuwahara
 */
public class DBDefCatalog implements SourceCodeCatalog<TableDef> {

	private Map<String, TableDef> tables = new HashMap<String, TableDef>();
	@Resource
	PropertyManager pm;
	@Resource
	DocumentRepository repo;
	@Resource
	DocumentMapper dm;

	@PostConstruct
	public void load() {
		TableDataCatalog catalog = repo.readAll(pm.getDbDef());
		TableData tableDefCatalog = catalog.get("テーブル一覧");

		for (RowData tableDefRowData : tableDefCatalog.getRows()) {
			TableDef table = dm.map("tableDef", tableDefRowData, TableDef.class);
			tables.put(table.getPname(), table);

			for (RowData columnDefRowData : catalog.get(table.getName()).getRows()) {
				ColumnDef column = dm.map("columnDef", columnDefRowData, ColumnDef.class);
				table.addColumn(column);
			}
		}
	}

	@Override
	public Collection<TableDef> getAll() {
		return tables.values();
	}
}
