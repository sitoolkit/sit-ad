/*
 * Copyright 2013 yuichi.kuwahara.
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
package org.sitoolkit.core.domain.java;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author yuichi.kuwahara
 */
public class LayerDef extends ClassDef {

	/**
	 * 派生元のエンティティ
	 */
	private EntityDef entity;

	/**
	 * 物理名の接尾辞
	 */
	private String pnameSuffix;

	public EntityDef getEntity() {
		return entity;
	}

	public void setEntity(EntityDef entity) {
		this.entity = entity;
	}

	public String getPnameSuffix() {
		return pnameSuffix;
	}

	public void setPnameSuffix(String pnameSuffix) {
		this.pnameSuffix = pnameSuffix;
	}
	
	public void load(EntityDef entity) {
		setEntity(entity);
		if (StringUtils.isEmpty(getPkg())) {
			setPkg(entity.getPkg());
		}
		setPname(entity.getDomain() + getPnameSuffix());
	}
}
