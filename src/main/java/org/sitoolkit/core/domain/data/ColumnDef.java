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

import java.util.Collections;
import java.util.Set;
import org.sitoolkit.core.infra.doc.DocumentElement;
import org.apache.commons.lang3.StringUtils;

/**
 * このクラスは、DBの列の定義を表すエンティティです。
 * @author Yuichi Kuwahara
 *
 */
public class ColumnDef extends DocumentElement {

	/**
	 * この列が所属するテーブル定義
	 */
	private TableDef table;
	
	private ForeignKeyDef fk;
	/**
	 * データ型
	 */
	private String type;

	/**
	 * 書式
	 */
	private String format;
	/**
	 * 初期値
	 */
	private String defaultValue;
	/**
	 * 長さ
	 */
	private int length;
	/**
	 * 主キー
	 */
	private int pk;
	/**
	 * 外部キー
	 */
	private String fkStr;
	/**
	 * 説明
	 */
	private String remark;
	/**
	 * コード仕様
	 */
	private String codeSpec;
	/**
	 * NOT NULL
	 */
	private boolean notnull;
	/**
	 * 長さの指定が不要なデータ型のセット
	 */
	private Set<String> dataTypeSetWithoutLength;
	
	public boolean isNotNull() {
		return this.notnull;
	}
	public int getPk() {
		return this.pk;
	}
	public int getLength() {
		return this.length;
	}
	
	public boolean isPrimaryKey() {
		return getPk() > 0;
	}
	
	public boolean hasFk() {
		return StringUtils.isNotEmpty(getFkStr());
	}
	public TableDef getTable() {
		return table;
	}
	public void setTable(TableDef table) {
		this.table = table;
	}

	public ForeignKeyDef getFk() {
		if(fk == null && hasFk()) {
			fk = new ForeignKeyDef(this);
		}
		return fk;
	}

	public void setFk(ForeignKeyDef fk) {
		this.fk = fk;
	}
	
	public boolean isPassword() {
		return "password".equalsIgnoreCase(getPname());
	}

	public String getCodeSpec() {
		return codeSpec;
	}

	public void setCodeSpec(String codeSpec) {
		this.codeSpec = codeSpec;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setPk(int pk) {
		this.pk = pk;
	}

	public String getFkStr() {
		return fkStr;
	}

	public void setFkStr(String fkStr) {
		this.fkStr = fkStr;
	}

	public boolean isNotnull() {
		return notnull;
	}

	public void setNotnull(boolean notnull) {
		this.notnull = notnull;
	}

	public void setLength(int length) {
		this.length = length;
	}

	@Override
	public String toString() {
		return String.format(
				"\t%s %s %s %s, %n",
				getPname(),
				getType(),
				getDeclareLength(),
				!isPrimaryKey() && isNotNull() ? "NOT NULL" : ""
				);
	}

	public String getDeclareLength() {
		if(getDataTypeSetWithoutLength().contains(getType()) ||
			getLength() < 1) {
			return "";
		} else {
			return "(" + getLength() + ")";
		}
	}
	
	public String getDelareNotNull() {
		return !isPrimaryKey() && isNotNull() ? "NOT NULL" : "";
	}

	public Set<String> getDataTypeSetWithoutLength() {
		return Collections.unmodifiableSet(dataTypeSetWithoutLength);
	}

	public void setDataTypeSetWithoutLength(Set<String> dataTypeSetWithoutLength) {
		this.dataTypeSetWithoutLength = dataTypeSetWithoutLength;
	}
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
}
