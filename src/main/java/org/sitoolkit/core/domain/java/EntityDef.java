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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import org.sitoolkit.core.domain.data.ColumnDef;
import org.sitoolkit.core.domain.data.TableDef;
import org.sitoolkit.core.domain.format.FormatDef;
import org.sitoolkit.core.domain.format.FormatDefCatalog;
import org.sitoolkit.core.infra.srccd.SourceCode;
import org.sitoolkit.core.infra.util.SitStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.context.ApplicationContext;

/**
 * このクラスは、エンティティクラスの定義情報を表すVOです。
 *
 * @author Yuichi Kuwahara
 *
 */
public class EntityDef extends ClassDef {

	/**
	 * 派生元となるテーブル定義
	 */
	private TableDef table;
	/**
	 * 組み込みID定義
	 */
	private EmbeddedIdDef embeddedId;
	/**
	 *
	 */
	private String parentPname;
	/**
	 * ドメイン
	 */
	private String domain;
	/**
	 * エンティティから派生するレイヤのリスト
	 */
	private List<LayerDef> layerList;

	/**
	 * VOの物理名のサフィックス
	 */
	private String voPnameSuffix = "Vo";

	/**
	 * 複合主キーフィールドの物理名
	 */
	private String embeddedIdPname = "pk";

	@Resource
	FormatDefCatalog formatDefCatalog;

	@Resource
	ApplicationContext appCtx;

	public EntityDef() {
		super();
	}

	public EntityDef(TableDef table) {
		setTable(table);
	}

	public String getTablePname() {
		return getTable().getPname();
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getDomainPkg() {
		return getDomain().toLowerCase();
	}

	public TableDef getTable() {
		return table;
	}

	public final void setTable(TableDef table) {
		this.table = table;
	}

	public boolean isDependent() {
		return getTable().isDependent();
	}

	@Override
	public String getPkg() {
		return getAppRootPkg() + ".domain." + getDomainPkg();
	}

	/**
	 * エンティティクラスが埋め込みIDを必要とするか否かを判定する。
	 * この判定は、対応するテーブルの主キーが2列以上であるか否か、に一致する。
	 * @return
	 */
	public boolean hasEmbeddedId() {
		return getTable() == null ? false : getTable().getPks().size() >= 2;
	}

	public EmbeddedIdDef getEmbeddedId() {
		return embeddedId;
	}

	public void setEmbeddedId(EmbeddedIdDef embeddedId) {
		this.embeddedId = embeddedId;
	}

	public String getIdType() {
		if(getIds().isEmpty()) {
			return "";
		} else {
			if(hasEmbeddedId()) {
				return getEmbeddedId().getPname();
			} else {
				return getIds().get(0).getType();
			}
		}
	}

	/**
	 *
	 * @return 親テーブル物理名
	 */
	public String getParentPname() {
		return parentPname;
	}

	/**
	 * 親テーブル物理名を設定します。
	 * @param parentPname 親テーブル物理名
	 */
	public void setParentPname(String parentPname) {
		this.parentPname = parentPname;
	}

	/**
	 * テーブル定義情報からエンティティの定義を初期化します。
	 *
	 * @param table
	 */
	public void load(TableDef table) {
		setTable(table);
		setName(table.getName());
		setDomain(isDependent() ?
					SitStringUtils.table2entity(getTable().getParentPname()) :
					SitStringUtils.table2entity(getTable().getPname()));
		setPname((table.isDependent() ? SitStringUtils.table2entity(table.getPname()) : getDomain()) + "Entity");

		List<FieldDef> ids = new ArrayList<FieldDef>();
        for (ColumnDef column : table.getColumns()) {
            FieldDef field = column2field(column);
			if (column.isPrimaryKey()) {
				ids.add(field);
			} else {
	            addField(field);
			}
        }

		if(ids.size() == 1) {
			addId(ids.get(0));
		} else {
            getEmbeddedId().load(this, ids);

			FieldDef embeddedIdField = new FieldDef();
			embeddedIdField.setFullType(getEmbeddedId().getPname());
			embeddedIdField.setPname(getEmbeddedIdPname());
			embeddedIdField.setName(table.getName() + "の主キー");
			embeddedIdField.setEmbeddedId(true);
			embeddedIdField.setEntity(this);
			embeddedIdField.setRequired(true);

			addId(embeddedIdField);
		}
	}

	/**
	 * カラム定義情報をフィールド定義情報に変換します。
	 * @param column カラム定義情報
	 * @return フィールド定義情報
	 */
	protected FieldDef column2field(ColumnDef column) {
		FieldDef field = appCtx.getBean(FieldDef.class);
		field.setEntity(this);
		field.setPname(SitStringUtils.toCamel(column.getPname()));
		field.setName(column.getName());
		field.setFullType(toJavaType(column));
		field.setColumn(column);
		field.setId(column.isPrimaryKey());
		field.setCodeSpec(column.getCodeSpec());
		field.setRequired(column.isNotnull());
		if ("String".equals(field.getType())) {
			field.setMaxLength(NumberUtils.toInt(column.getLength()));
			if ("char".equalsIgnoreCase(column.getType())) {
				field.setMinLength(field.getMaxLength());
			}
		}
		if ("boolean".equalsIgnoreCase(column.getType())) {
			field.setBooled(true);
		}
		return field;
	}

	public List<SourceCode> create() {
		List<SourceCode> list = new ArrayList<SourceCode>();
		list.add(this);

		if (hasEmbeddedId()) {
			list.add(getEmbeddedId());
		}

		if (!isDependent()) {
			for (LayerDef layer : getLayerList()) {
				layer.load(this);
				list.add(layer);
			}
		}
		return list;
	}

	public List<LayerDef> getLayerList() {
		return layerList;
	}

	public void setLayerList(List<LayerDef> layerList) {
		this.layerList = layerList;
	}

	public String getVoPnameSuffix() {
		return voPnameSuffix;
	}

	public void setVoPnameSuffix(String voPnameSuffix) {
		this.voPnameSuffix = voPnameSuffix;
	}

	public String getEmbeddedIdPname() {
		return embeddedIdPname;
	}

	public void setEmbeddedIdPname(String embeddedIdPname) {
		this.embeddedIdPname = embeddedIdPname;
	}

	String toJavaType(ColumnDef column) {
		String javaType = "";
		if (StringUtils.isNotEmpty(column.getCodeSpec())) {
			javaType = "java.lang.String";
		} else {
			FormatDef format = formatDefCatalog.getByName(column.getFormat());

			if (format == null) {
				javaType = "java.lang.String";
			} else {
				javaType = format.getPgType();
			}
		}
		return javaType;
	}
}
