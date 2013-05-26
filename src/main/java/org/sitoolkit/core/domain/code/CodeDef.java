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
package org.sitoolkit.core.domain.code;

import java.util.ArrayList;
import java.util.List;
import org.sitoolkit.core.domain.java.ClassDef;
import org.sitoolkit.core.infra.util.SitStringUtils;
import org.apache.commons.lang3.StringUtils;

public class CodeDef extends ClassDef {

	private String codeDef;
	
	/**
	 * 物理名のサフィックス
	 */
	private String pnameSuffix;
	
	private List<CodeItemDef> items;
	
	public CodeDef() {
	}
	
	public List<CodeItemDef> getItems() {
		if(items == null) {
			items = deserialize(getCodeDef());
		}
		return items;
	}

	@Override
	public String getPkg() {
		return getAppRootPkg() + ".domain.code";
	}
	
	/**
	 * 
	 * @param str
	 * @return 
	 */
	public static List<CodeItemDef> deserialize(String str) {
		List<CodeItemDef> items = new ArrayList<CodeItemDef>();
		
		if(StringUtils.isEmpty(str)) {
			return items;
		}
		
		for(String itemStr :SitStringUtils.splitLine(str)) {
			items.add(new CodeItemDef(itemStr));
		}
		
		return items;
	}

	public String getCodeDef() {
		return codeDef;
	}

	public void setCodeDef(String codeDef) {
		this.codeDef = codeDef;
	}

	@Override
	public String getOutDir() {
		return getLibOutDirPath();
	}

	@Override
	public String getPname() {
		return getName() + getPnameSuffix();
	}
	
	public String getPnameSuffix() {
		return pnameSuffix;
	}

	public void setPnameSuffix(String pnameSuffix) {
		this.pnameSuffix = pnameSuffix;
	}
}
