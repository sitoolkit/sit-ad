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
import java.util.Map.Entry;
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

	/**
	 * 外部キー定義
	 * キー：定義の列名上のサフィックス(外部キー_XXX)、値：定義値
	 */
	private Map<String, String> foreignKey = new HashMap<>();

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
	private String length;
	/**
	 * バイト数
	 */
	private int byteLength;
	/**
	 * 主キー
	 */
	private int pk;
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

	public boolean isNotNull() {
		return this.notnull;
	}
	public int getPk() {
		return this.pk;
	}
	public String getLength() {
		return this.length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public boolean isPrimaryKey() {
		return getPk() > 0;
	}

	public boolean hasFk() {
		for (Entry<String, String> entry : getForeignKey().entrySet()) {
			if (StringUtils.isNotEmpty(entry.getValue())) {
				return true;
			}
		}
		return false;
	}

	public TableDef getTable() {
		return table;
	}
	public void setTable(TableDef table) {
		this.table = table;
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

	public boolean isNotnull() {
		return notnull;
	}

	public void setNotnull(boolean notnull) {
		this.notnull = notnull;
	}

	public String getDelareNotNull() {
		return !isPrimaryKey() && isNotNull() ? "NOT NULL" : "";
	}
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public int getByteLength() {
		return byteLength;
	}

	public void setByteLength(int byteLength) {
		this.byteLength = byteLength;
	}

	public Map<String, String> getForeignKey() {
		return foreignKey;
	}

	public void setForeignKey(Map<String, String> foreignKey) {
		this.foreignKey = foreignKey;
	}
}
