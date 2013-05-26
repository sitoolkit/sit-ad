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
package org.sitoolkit.core.domain.java;

import java.util.Collection;
import java.util.List;

/**
 * このクラスは、エンティティの組み込みIDを表すVOです。
 * 組み込みIDとは、主キーが2カラム以上で構成されるテーブルに対応する
 * エンティティのIDとして使用するクラスです。
 * 
 * @author Yuichi Kuwahara
 * @since 1.0
 * @version 1.0
 */
public class EmbeddedIdDef extends ClassDef {

	public EmbeddedIdDef() {
		super();
	}
	
	public void load(EntityDef entity, List<FieldDef> ids) {
		setName(entity.getName());
		setPname(entity.getPname() + "PK");
		addIds(ids);
		setPkg(entity.getPkg());
		setOutDir(entity.getOutDir());
	}
		
	@Override
	public void addFields(Collection<FieldDef> fields) {
		super.addFields(fields);
		
		for (FieldDef field : fields) {
			field.setId(false);
		}
	}
}
