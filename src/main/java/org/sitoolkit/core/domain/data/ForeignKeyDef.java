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

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;
import org.sitoolkit.core.infra.util.FormatException;

/**
 *
 * @author yuichi.kuwahara
 */
public class ForeignKeyDef {

	private ColumnDef column;
	/**
	 * 参照先のテーブル物理名
	 */
	private String dstTable;
	private boolean cascade;
	/**
	 * 参照元のカラム物理名リスト
	 */
	private List<String> srcColList = new ArrayList<String>();
	/**
	 * 参照先のカラム物理名リスト
	 */
	private List<String> dstColList = new ArrayList<String>();

	ForeignKeyDef(ColumnDef column) {
		this.column = column;
		srcColList.add(column.getPname());
		String fkStr = column.getFkStr();
		String[] fk = fkStr.split("\\.");
		if (fk.length != 2) {
			throw new FormatException("外部キーの書式が不正です。{0}　「テーブル物理名.カラム物理名」で記載してください。", fkStr);
		}
		setDstTable(fk[0]);
		dstColList.add(fk[1]);
	}

	public List<String> getDstColList() {
		return Collections.unmodifiableList(dstColList);
	}

	public String getDstTable() {
		return dstTable;
	}

	public void setDstTable(String dstTable) {
		this.dstTable = dstTable;
	}
	
	public boolean isCascade() {
		return cascade;
	}

	public void setCascade(boolean cascade) {
		this.cascade = cascade;
	}
	
	public List<String> getSrcColList() {
		return Collections.unmodifiableList(srcColList);
	}
	
	public String getSrcCols() {
		return StringUtils.join(getSrcColList(), ", ");
	}

	public String getDstCols() {
		return StringUtils.join(getDstColList(), ", ");
	}

	public void merge(ColumnDef column) {
		ForeignKeyDef other = column.getFk();
		dstColList.addAll(other.getDstColList());
		srcColList.addAll(other.getSrcColList());

		// TODO 実装は不完全です。
		int maxPk = Math.max(this.column.getPk(), column.getPk());
		cascade = dstColList.size() == maxPk;
	}
}
