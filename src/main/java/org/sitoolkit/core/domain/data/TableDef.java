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
package org.sitoolkit.core.domain.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Resource;
import org.sitoolkit.core.infra.srccd.SourceCode;
import org.sitoolkit.core.infra.util.PropertyManager;
import org.apache.commons.lang3.StringUtils;

/**
 * このクラスは、テーブル定義を表すVOです。
 * @author Yuichi Kuwahara
 *
 */
public class TableDef extends SourceCode {

	@Resource
	PropertyManager pm;

	/**
	 * ドメイン
	 */
	private String domain;

	/**
	 * ドメイン物理名
	 */
	private String domainPname;

	/**
	 * テーブルに定義されている全てのカラム定義
	 */
	private List<ColumnDef> columns = new ArrayList<>();

	/**
	 * テーブルに定義されている主キーカラム
	 */
	private List<ColumnDef> pks = new ArrayList<>();

	/**
	 * テーブルに定義されている外部キーのマップ
	 * キー：参照先のテーブル物理名、値：外部キー定義
	 */
	private Map<String, ForeignKeyDef> fkMap = new HashMap<>();

	private String masterTableNameSuffix = "マスター";

	private String relationTableNameSuffix = "関連";

	/**
	 * 親テーブルの物理名
	 */
	private String parentPname = StringUtils.EMPTY;
	/**
	 * テーブルに定義されている全てのカラム定義をリストとして取得します。
	 * このリストは不可変です。
	 * @return テーブルに定義されている全てのカラム定義のリスト
	 */
	public List<ColumnDef> getColumns() {
		return Collections.unmodifiableList(columns);
	}

	/**
	 * カラム定義を追加します。
	 * @param column カラム定義
	 */
	public void addColumn(ColumnDef column) {
		column.setTable(this);
		columns.add(column);
		if (column.isPrimaryKey()) {
			pks.add(column);
		}

		addFk(column);
	}

	/**
	 * カラムに定義された外部キー定義を追加します。
	 * @param column カラム
	 */
	protected void addFk(ColumnDef column) {
		Map<String, String> fkdef = column.getForeignKey();
		for (Entry<String, String> entry : fkdef.entrySet()) {
			if (StringUtils.isEmpty(entry.getValue())) {
				continue;
			}
			String key = entry.getKey();
			ForeignKeyDef fk = fkMap.get(key);
			if (fk == null) {
				fk = ForeignKeyDef.build(
						getPname(),
						key,
						column.getPname(),
						entry.getValue());
				if (fk == null) {
					continue;
				}
				fkMap.put(key, fk);
			} else {
				fk.add(column.getPname(), entry.getValue());
			}
			if (column.getPk() == 1) {
				parentPname = fk.getDstTable();
			}
		}
	}
	/**
	 * テーブルに定義されている主キーカラムのリストを取得します。
	 * @return テーブルに定義されている主キーカラムのリスト
	 */
	public List<ColumnDef> getPks() {
		return Collections.unmodifiableList(pks);
	}
	public Map<String, ForeignKeyDef> getFkMap() {
		return Collections.unmodifiableMap(fkMap);
	}

	/**
	 * テーブルがマスタテーブル(リソース系)である場合にtrueを返します。
	 * @return テーブルがマスタテーブル(リソース系)である場合にtrue
	 */
	public boolean isMaster() {
		return getPname().toLowerCase().startsWith("m_");
	}

	/**
	 * 完全従属テーブルであるか否かを判定します。
	 * 以下の条件を満たす場合に、当該テーブルは「完全従属である」と判定されます。
	 * <pre>
	 *	主キーの1番目の列に外部キーが設定されている。
	 * </pre>
	 * @return true:完全従属テーブルである。
	 */
	public boolean isDependent() {
		return getPks().isEmpty() ? false : getPks().get(0).hasFk();
	}

	/**
	 * 当該テーブルが依存するテーブルの物理名を返します。
	 * 当該テーブルが独立テーブルである場合は空文字を返します。
	 *
	 * @return 依存するテーブルの物理名
	 */
	public String getParentPname() {
		return parentPname;
	}

	/**
	 *
	 * @return テーブル名の接頭辞を除いた名前
	 */
	public String getSimpleName() {
		if(getName().endsWith(getMasterTableNameSuffix())) {
			return StringUtils.removeEnd(getName(), getMasterTableNameSuffix());
		} else if(getName().endsWith(getRelationTableNameSuffix())) {
			return StringUtils.removeEnd(getName(), getRelationTableNameSuffix());
		}
		return getName();
	}

	/**
	 * テーブルが関連テーブルである場合にtrueを返します。
	 * @return true:関連テーブルである
	 */
	public boolean isRelation() {
		return getName().endsWith(getRelationTableNameSuffix());
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Override
	public String getFileName() {
		return String.format(
					"tb%s%02d_create_%s.%s",
					isMaster() ? "m" : "t",
					getNo(),
					getPname(),
					getFileExt());
	}

	@Override
	public String getOutDir() {
		return pm.getSysProp("outdir.db", "out\\db\\script");
	}

	public String getDomainPname() {
		return domainPname;
	}

	public void setDomainPname(String domainPname) {
		this.domainPname = domainPname;
	}

	public String getMasterTableNameSuffix() {
		return masterTableNameSuffix;
	}

	public void setMasterTableNameSuffix(String masterTableNameSuffix) {
		this.masterTableNameSuffix = masterTableNameSuffix;
	}

	public String getRelationTableNameSuffix() {
		return relationTableNameSuffix;
	}

	public void setRelationTableNameSuffix(String relationTableNameSuffix) {
		this.relationTableNameSuffix = relationTableNameSuffix;
	}
}
