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
package org.sitoolkit.core.domain.view.itemcmp;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.sitoolkit.core.infra.util.SitStringUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Yuichi Kuwahara
 */
public class DataBind {
	
	private static final Logger logger = Logger.getLogger(DataBind.class.getName());
	/**
	 * テーブル物理名
	 */
	private String table;
	/**
	 * カラム物理名
	 */
	private String column;
	/**
	 * 演算子
	 */
	private String operator;

	public DataBind() {
	}
	
	public DataBind(String table, String column) {
		this.table = table;
		this.column = column;
	}

	public DataBind(String operator, String table, String column) {
		this.table = table;
		this.column = column;
		this.operator = operator;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	@Override
	public String toString() {
		return serialize();
	}

	public static DataBind deserialize(String str) {
		if (StringUtils.isEmpty(str)) {
			return null;
		}
		DataBind dataBind = null;

		String[] split = str.split("\\s|\\.");
		
		if (split.length == 2) {
			dataBind = new DataBind(split[0], split[1]);
		} else if (split.length == 3) {
			dataBind = new DataBind(split[0], split[1], split[2]);
		} else {
			logger.log(Level.WARNING, "データバインドに不正な値が設定されています。{0}", str);
		}
		return dataBind;
	}
	
	/**
	 * 当該インスタンスの文字列表現を返します。
	 * <dl>
	 * <dt>operatorが空の場合
	 * <dd>table + "." + column
	 * 
	 * <dt>operatorが空でない場合
	 * <dd>operator + " " + table + "." + column
	 * </dl>
	 * @return 当該インスタンスの文字列表現
	 */
	public String serialize() {
		String ret = "";
		if (StringUtils.isNotEmpty(getTable()) && StringUtils.isNotEmpty(getColumn())) {
			ret = getTable() + "." + getColumn();

			if (StringUtils.isNotEmpty(getOperator())) {
				ret = getOperator() + " " + ret;
			}
		}
		return ret;
	}
	
	public String getField() {
		return SitStringUtils.toCamel(getColumn());
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	public String toItemId() {
		return SitStringUtils.table2camel(getTable()) + "-"
				+ SitStringUtils.toCamel(getColumn());
	}
}
