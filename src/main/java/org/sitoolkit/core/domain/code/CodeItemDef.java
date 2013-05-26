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

import org.sitoolkit.core.infra.doc.KeyValuePair;
import org.sitoolkit.core.infra.util.SitStringUtils;

/**
 * コード値を表すVO
 * 
 * @author Yuichi Kuwahara
 */
public class CodeItemDef extends KeyValuePair {

	CodeItemDef(String itemStr) {
		super.load(itemStr);
	}
	public String getCode() {
		return super.getKey();
	}
	public String getLabel() {
		return super.getValue();
	}
	
	public String getEscapedLabel() {
		return SitStringUtils.escapeForJavaIdentifer(getLabel());
	}
}
