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
import java.util.regex.Matcher;
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
	 *
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

	public String getCellValue(Object columnName, String excludePattern) {
		if (SitStringUtils.isEmpty(columnName)) {
			LOG.warn("列名が空であるセル値は格納できません。");
			return StringUtils.EMPTY;
		}
		String columnNameStr = SitStringUtils.cleansing(columnName.toString());
		return value(getData().get(columnNameStr), excludePattern);
	}

	private String value(String value, String excludePattern) {
		return StringUtils.isEmpty(excludePattern)
				? value
				: value.replaceAll(excludePattern, StringUtils.EMPTY);
	}

	/**
	 * 正規表現に一致する列に該当するセルの値を返します。
	 *
	 * @param regex 正規表現
	 * @return 正規表現に一致する列に該当するセルの値
	 */
	public List<String> getCellValues(String regex, String excludePattern, boolean excludeEmptyValue) {
		List<String> valueList = new ArrayList<String>();

		if (StringUtils.isEmpty(regex)) {
			LOG.warn("列名が空であるセル値は格納できません。");
			return valueList;
		}
		Pattern pattern = Pattern.compile(regex);
		for (Entry<String, String> entry : getData().entrySet()) {
			if (pattern.matcher(entry.getKey()).matches()) {
				if (excludeEmptyValue &&
					StringUtils.isEmpty(entry.getValue())) {
					continue;
				}
				String value = value(entry.getValue(), excludePattern);
				LOG.trace("key:{}, value:{}", entry.getKey(), value);
				valueList.add(value);
			}
		}

		return valueList;
	}

	/**
	 * 正規表現に一致する列名のセルの値をMapとして取得します。
	 *
	 * @param regex 列名に一致させる正規表現
	 * @param groupIdx 列名の中から抜き出しMapのキーとするグループの番号
	 * @return 正規表現に一致する列名のセルの値
	 * @see Matcher#group(int)
	 */
	public Map<String, String> getCellValuesAsMap(String regex, int groupIdx, String excludePattern) {
		Map<String, String> map = new TreeMap<String, String>();

		Pattern p = Pattern.compile(regex);
		for (Entry<String, String> entry : getData().entrySet()) {
			Matcher m = p.matcher(entry.getKey());

			if (m.matches()) {
				String value = value(entry.getValue(), excludePattern);
				map.put(m.group(groupIdx), value);
			}
		}
		return map;
	}

	public int getInt(Object columnName, String excludePattern) {
		return NumberUtils.toInt(getCellValue(columnName), 0);
	}

	public boolean getBoolean(Object columnName, Object trueStr, String excludePattern) {
		return getCellValue(columnName, excludePattern).equals(trueStr);
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
		if (value <= limit) {
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
