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
package org.sitoolkit.core.infra.doc;

import org.sitoolkit.core.infra.repository.StringLoadable;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Yuichi Kuwahara
 */
public class KeyValuePair implements StringLoadable {

	private static final String DELIMITER_PATTERN = "[:|：]";
	private static final String DELIMITER = "：";

	private String key;
	private String value;

	public KeyValuePair() {
	}

	public KeyValuePair(String str) {
		this.load(str);
	}
	
	public KeyValuePair(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		if(key == null) {
			key = "";
		}
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		if(value == null) {
			value = "";
		}
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public final void load(String str) {
		if(StringUtils.isEmpty(str)) {
			return ;
		}
		String[] pair = str.split(DELIMITER_PATTERN);
		if(pair.length > 0) {
			setKey(StringUtils.trim(pair[0]));
			if(pair.length > 1) {
				setValue(StringUtils.trim(pair[1]));
			}
		}
	}

	@Override
	public String toString() {
		if(StringUtils.isEmpty(getValue())) {
			return getKey();
		}
		return getKey() + DELIMITER + getValue();
	}
	
	public static KeyValuePair deserialize(String line) {
		KeyValuePair kvp = new KeyValuePair();
		kvp.load(line);
		return kvp;
	}
}
