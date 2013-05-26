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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.sitoolkit.core.infra.srccd.SourceCode;
import org.sitoolkit.core.infra.util.PropertyManager;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author Yuichi Kuwahara
 */
public abstract class ClassDef extends SourceCode {

	@Resource
	PropertyManager pm;

	/**
	 * パッケージ物理名
	 */
	private String pkg;

	/**
	 * クラスが持つ全フィールドのリスト
	 */
	private List<FieldDef> fields = new ArrayList<FieldDef>();

	/**
	 * クラスのIDとなるフィールドのリスト
	 */
	private List<FieldDef> ids = new ArrayList<FieldDef>();
	
	/**
	 * クラスのインポートのセット
	 */
	private Set<String> imports = new HashSet<String>();

	/**
	 * パッケージのルートディレクトリのパス
	 */
	private String pkgRootDir;
	
	public String getPkg() {
		return pkg;
	}
	public void setPkg(String pkg) {
		this.pkg = pkg;
	}

	public boolean hasDateField() {
		if(getIds().size() == 1 && "Date".equals(getIds().get(0).getType())) {
			return true;
		}
		
		for(FieldDef field : getFields()) {
			if("Date".equals(field.getType())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasCodedField() {
		for(FieldDef field : getFields()) {
			if(!StringUtils.isEmpty(field.getCodeSpec())) {
				return true;
			}
		}
		for(FieldDef field : getIds()) {
			if(!StringUtils.isEmpty(field.getCodeSpec())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * クラスが持つ全フィールドのリストを取得します。
	 * リストは不可変です。
	 * @return クラスが持つ全フィールドのリスト
	 */
	public List<FieldDef> getFields() {
		return Collections.unmodifiableList(fields);
	}

	/**
	 * クラスが持つID
	 * @return 
	 */
	public List<FieldDef> getIds() {
		return Collections.unmodifiableList(ids);
	}

	/**
	 * クラスにフィールドを追加します。
	 * @param fields 追加するフィールド
	 * @see #addField(jp.monocrea.sitoolkit.domain.java.FieldDef)
	 */
	public void addFields(Collection<FieldDef> fields) {
		for (FieldDef field : fields) {
			addField(field);
		}
	}
	
	/**
	 * クラスにフィールドを追加します。
	 * @param field 追加するフィールド
	 */
	public void addField(FieldDef field) {
		fields.add(field);
		addImport(field);
	}

	public void addId(FieldDef id) {
		ids.add(id);
		addImport(id);
	}

	public void addIds(Collection<FieldDef> ids) {
		for (FieldDef id : ids) {
			addId(id);
		}
	}

	public String getFullName() {
		return getPkg() + "." + getPname();
	}

	/**
	 * @return アプリケーションルートパッケージ
	 */
	public String getAppRootPkg() {
		return pm.getAppRootPkg();
	}
	
	/**
	 * パッケージ名を、そのパッケージ内のクラス(Javaソースファイル)を出力するフォルダのパスに変換します。
	 * 
	 * @return クラスを出力するフォルダのパス
	 */
	protected String getLibOutDirPath() {
		return pm.getLibSrcDir() + File.separator + getPkg().replace(".", File.separator);
	}

	@Override
	public String getOutDir() {
		if (StringUtils.isEmpty(getPkgRootDir())) {
			throw new IllegalArgumentException();
		}
		return getPkgRootDir() + File.separator + getPkg().replace(".", File.separator);
	}
	
	@Override
	public String getFileExt() {
		return "java";
	}

	public String getPkgRootDir() {
		return pkgRootDir;
	}

	public void setPkgRootDir(String pkgRootDir) {
		this.pkgRootDir = pkgRootDir;
	}

	public Set<String> getImports() {
		return Collections.unmodifiableSet(imports);
	}

	public void addImport(FieldDef field) {
		String[] array = field.getFullType().split(".");
		
		if (array.length == 1) {
			return;
		} else if (array.length == 3 && "java".equals(array[0]) && "lang".equals(array[0])) {
			return;
		}
		
		imports.add(field.getFullType());
	}
}
