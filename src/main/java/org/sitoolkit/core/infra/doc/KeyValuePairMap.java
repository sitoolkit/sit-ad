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

import java.util.HashMap;

import org.sitoolkit.core.infra.repository.StringLoadable;
import org.sitoolkit.core.infra.util.SitStringUtils;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Yuichi Kuwahara
 */
public class KeyValuePairMap extends HashMap<String, KeyValuePair> implements StringLoadable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public KeyValuePair add(KeyValuePair kvp) {
		return kvp == null ? null : put(kvp.getKey(), kvp);
	}
	
	public static KeyValuePairMap deserialize(String str) {
		KeyValuePairMap map = new KeyValuePairMap();
		for(String line : SitStringUtils.splitLine(str)) {
			map.add(KeyValuePair.deserialize(line));
		}
		return map;
	}
	
	public String getValue(String key) {
		KeyValuePair kvp = get(key);
		return kvp == null ? "" : kvp.getValue();
	}
	
	public boolean hasValue(String type, String value) {
		return getValue(type).equals(value);
	}
	
	public String serialize() {
		return StringUtils.join(this.values(), "\n");
	}

	@Override
	public String toString() {
		return serialize();
	}

	@Override
	public void load(String str) {
		for(String line : SitStringUtils.splitLine(str)) {
			add(KeyValuePair.deserialize(line));
		}
	}

}
