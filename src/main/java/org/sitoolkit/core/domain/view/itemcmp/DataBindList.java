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

import java.util.ArrayList;

import org.sitoolkit.core.infra.repository.StringLoadable;
import org.sitoolkit.core.infra.util.SitStringUtils;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Yuichi Kuwahara
 */
public class DataBindList extends ArrayList<DataBind> implements StringLoadable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DataBindList() {
		super();
	}
	
	public DataBindList(String str) {
		this();
		load(str);

	}
	
	public static DataBindList deserialise(String str) {
		DataBindList list = new DataBindList();
		if(StringUtils.isEmpty(str)) {
			return list;
		}
		for(String line : str.split("[\n|\r\n]")) {
			list.add(DataBind.deserialize(line));
		}
		return list;
	}

	@Override
	public boolean add(DataBind e) {
		return e == null ? false : super.add(e);
	}

	@Override
	public String toString() {
		return StringUtils.join(this, "\n");
	}

	@Override
	public void load(String str) {
		if(StringUtils.isEmpty(str)) {
			return;
		}
		for(String line : SitStringUtils.splitLine(str)) {
			add(DataBind.deserialize(line));
		}
	}
	
	public String toItemId() {
		if (isEmpty()) {
			return "";
		}
		return get(0).toItemId();
		
	}
}
