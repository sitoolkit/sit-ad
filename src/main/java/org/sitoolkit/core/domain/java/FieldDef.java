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

import javax.annotation.Resource;
import org.sitoolkit.core.infra.util.SitStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.sitoolkit.core.domain.data.ColumnDef;
import org.sitoolkit.core.domain.format.FormatDef;
import org.sitoolkit.core.domain.format.FormatDefCatalog;

/**
 * このクラスは、クラスのフィールドを表すエンティティです。
 * @author Yuichi Kuwahara
 *
 */
public class FieldDef {

	@Resource
	protected FormatDefCatalog formatDefCatalog;
	/**
	 * フィールド名
	 */
	private String name;
	/**
	 * フィールド物理名
	 */
	private String pname;
	/**
	 * データ型(単純名)
	 */
	private String type;

	/**
	 * データ型(厳密名)
	 */
	private String fullType;

	private ColumnDef column;
	private String codeSpec;
	private boolean isId;
	private boolean isEmbeddedId;
	private boolean isBooled;
	private EntityDef entity;
	private boolean required;
	private int maxLength = -1;
	private int minLength = -1;

	public FormatDef getFormat() {
		if (getColumn() == null) {
			return null;
		}
		return formatDefCatalog.getByName(getColumn().getFormat());
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getType() {
		return type;
	}
	public ColumnDef getColumn() {
		return column;
	}
	public void setColumn(ColumnDef column) {
		this.column = column;
	}
	/**
	 * 当該フィールドの型がプリミティブ型であるか否かを判定する。
	 * @return
	 */
	public boolean isPrimitiveType() {
		return StringUtils.isAllLowerCase(getType());
	}
	public boolean isId() {
		return isId;
	}
	public void setId(boolean isId) {
		this.isId = isId;
	}
	public EntityDef getEntity() {
		return entity;
	}
	public void setEntity(EntityDef entity) {
		this.entity = entity;
	}
	public boolean isEmbeddedId() {
		return isEmbeddedId;
	}
	public void setEmbeddedId(boolean isEmbeddedId) {
		this.isEmbeddedId = isEmbeddedId;
	}
	public String getCodeSpec() {
		return codeSpec;
	}
	public void setCodeSpec(String codeSpec) {
		this.codeSpec = codeSpec;
	}
	public boolean isBooled() {
		return isBooled;
	}

	public void setBooled(boolean isBooled) {
		this.isBooled = isBooled;
	}

	public String getPnamePascal() {
		return SitStringUtils.toPascal(getPname());
	}

	public boolean isArrayType() {
		return StringUtils.contains(getType(), "[]");
	}

	public boolean isCoded() {
		return StringUtils.isNotEmpty(getCodeSpec());
	}

	public String getFullType() {
		return fullType;
	}

	public void setFullType(String fullType) {
		this.fullType = fullType;
		this.type = fullTypeToSimpleType(fullType);
	}

	String fullTypeToSimpleType(String fullType) {
		String simpleType = "";
		if (fullType.contains(".")) {
			simpleType = StringUtils.substringAfterLast(fullType, ".");
		} else {
			simpleType = fullType;
		}
		return simpleType;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public int getMinLength() {
		return minLength;
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}
}
