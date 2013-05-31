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

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import org.sitoolkit.core.infra.util.SitStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yuichi.kuwahara
 */
public class RowData {
	
	private static final Logger LOG = LoggerFactory.getLogger(RowData.class);
	
	private Map<String, String> data = new LinkedHashMap<String, String>();

	public RowData() {
		super();
	}
	
	public RowData(String columnName, Object value) {
		setCellValue(columnName, value);
	}
	
	/**
	 * 当該行の列名に該当するセルの値を返します。
	 * @param columnName 列名
	 * @return 列名に該当するセルの値
	 */
	public String getCellValue(Object columnName) {
		if (SitStringUtils.isEmpty(columnName)) {
			LOG.warn("列名が空であるセル値は格納できません。");
			return "";
		}
		
		return getData().get(SitStringUtils.cleansing(columnName.toString()));
	}

	/**
	 * 正規表現に一致する列に該当するセルの値を返します。
	 * @param regex 正規表現
	 * @return 正規表現に一致する列に該当するセルの値
	 */
	public List<String> getCellValues(String regex) {
		List<String> valueList = new ArrayList<String>();

		if (StringUtils.isEmpty(regex)) {
			LOG.warn("列名が空であるセル値は格納できません。");
			return valueList;
		}
		Pattern pattern = Pattern.compile(regex);
		for (Entry<String, String> entry : getData().entrySet()) {
			if (pattern.matcher(entry.getKey()).matches()) {
				if (StringUtils.isEmpty(entry.getValue())) {
					continue;
				}
				if (LOG.isTraceEnabled()) {
					LOG.trace("key:{}, value:{}", entry.getKey(), entry.getValue());
				}
				valueList.add(entry.getValue());
			}
		}
		
		return valueList;
	}
	
	public int getInt(Object columnName) {
		return NumberUtils.toInt(getCellValue(columnName), 0);
	}
	
	public boolean getBoolean(Object columnName, Object trueStr) {
		return getCellValue(columnName).equals(trueStr);
	}
	
	public void setCellValue(Object columnName, Object value) {
		if (SitStringUtils.isEmpty(columnName)) {
			LOG.warn("列名が空であるセル値は格納できません。");
			return;
		}
		getData().put(
				SitStringUtils.cleansing(columnName.toString()),
				value == null ? "" : value.toString());
	}
	
	public void setInt(Object columnName, int value, int limit) {
		if(value <= limit) {
			setCellValue(columnName, "");
		} else {
			setCellValue(columnName, value);
		}
	}

	public Map<String, String> getData() {
		return data;
	}

	@Override
	public String toString() {
		return StringUtils.join(getData().values(), ",");
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final RowData other = (RowData) obj;
		if (this.data != other.data && (this.data == null || !this.data.equals(other.data))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 73 * hash + (this.data != null ? this.data.hashCode() : 0);
		return hash;
	}
	
}