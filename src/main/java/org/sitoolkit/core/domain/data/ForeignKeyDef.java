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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * このクラスは外部キー定義を表すエンティティです。
 * このクラスのインスタンス1つが、1つの外部キー制約を表します。
 *
 * @author yuichi.kuwahara
 */
public class ForeignKeyDef {

	/**
	 * 外部キー物理名
	 */
	private String pname;
	/**
	 * 参照先のテーブル物理名
	 */
	private String dstTable;
	private boolean cascade;
	/**
	 * 参照元のカラム物理名リスト
	 */
	private List<String> srcColList = new ArrayList();
	/**
	 * 参照先のカラム物理名リスト
	 */
	private List<String> dstColList = new ArrayList();

	/**
	 * 外部キー列のヘッダー名からテーブル物理名を取得するための正規表現
	 */
	private static final Pattern PNAME_PATTERN = Pattern.compile("([0-9]*)(.*)");

	/**
	 * 外部キーエンティティのインスタンスを構築します。
	 *
	 * @param srcTable 外部キーを持つテーブル
	 * @param fkStr テーブル定義書の外部キーに記載された文字列
	 * @param srcCol 参照元のカラム
	 * @param dstCol 参照先のカラム
	 * @return 外部キーのインスタンス
	 */
	public static ForeignKeyDef build(String srcTable, String fkStr, String srcCol, String dstCol) {
		Matcher m = PNAME_PATTERN.matcher(fkStr);
		if (!m.matches()) {
			return null;
		}
		ForeignKeyDef fk = new ForeignKeyDef();
		fk.setPname(srcTable + "_fk" + m.group(1));
		fk.setDstTable(m.group(2));
		fk.srcColList.add(srcCol);
		fk.dstColList.add(dstCol);
		return fk;
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

	/**
	 * 外部キー制約にカラムを追加します。
	 * @param srcCol 参照元カラム
	 * @param dstCol 参照先カラム
	 */
	public void add(String srcCol, String dstCol) {
		srcColList.add(srcCol);
		dstColList.add(dstCol);
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

}
