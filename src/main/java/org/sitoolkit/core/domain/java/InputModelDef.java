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

/**
 *
 * @author yuichi.kuwahara
 */
public class InputModelDef extends LayerDef {

	/**
	 * 派生元エンティティのID
	 */
	private FieldDef entityId;

	@Override
	public void load(EntityDef entity) {
		super.load(entity);
		if (entity.hasEmbeddedId()) {
			FieldDef id = new FieldDef();
			EmbeddedIdDef embeddedId = entity.getEmbeddedId();
			
			id.setFullType(embeddedId.getPname());
			id.setPname("id");
			id.setId(true);
			id.setEmbeddedId(true);
			id.setName(embeddedId.getName());
			addId(id);
			addFields(embeddedId.getFields());
			getIds().addAll(embeddedId.getIds());
			
			setEntityId(id);
		} else {
			addFields(entity.getIds());
			setEntityId(entity.getIds().get(0));
		}
	}

	public FieldDef getEntityId() {
		return entityId;
	}

	public void setEntityId(FieldDef entityId) {
		this.entityId = entityId;
	}
	
}
