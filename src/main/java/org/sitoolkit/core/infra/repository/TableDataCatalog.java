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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author yuichi.kuwahara
 */
public class TableDataCatalog {

	private Map<String, TableData> data = new HashMap<String, TableData>();
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void add(TableData table) {
		data().put(table.getName(), table);
	}

	public TableData get(String name) {
		TableData tableData = data().get(name);
		if(tableData == null) {
			// TODO 「Excelファイル」はRepostoryの実装によって変わる。
			throw new IllegalArgumentException("Excelファイル：" + getName() + "にシート：" + name + "がありません。");
		}
		return tableData;
	}
	
	private Map<String, TableData> data() {
		return data;
	}

	public Collection<TableData> tables() {
		return data().values();
	}

	public int size() {
		return data().size();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final TableDataCatalog other = (TableDataCatalog) obj;
		if (this.data != other.data && (this.data == null || !this.data.equals(other.data))) {
			return false;
		}
		if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 37 * hash + (this.data != null ? this.data.hashCode() : 0);
		hash = 37 * hash + (this.name != null ? this.name.hashCode() : 0);
		return hash;
	}
	
	
}
