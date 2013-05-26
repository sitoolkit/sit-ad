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
package org.sitoolkit.core.domain.format;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.sitoolkit.core.infra.repository.DocumentMapper;
import org.sitoolkit.core.infra.repository.DocumentRepository;
import org.sitoolkit.core.infra.repository.RowData;
import org.sitoolkit.core.infra.repository.TableData;
import org.sitoolkit.core.infra.util.PropertyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * このクラスは、書式定義書の内容を保持するカタログです。
 *
 * @author yuichi.kuwahara
 */
public class FormatDefCatalog {

	private static final Logger LOG = LoggerFactory.getLogger(FormatDefCatalog.class);

	/**
	 * キー：データ型(DB)、値：書式定義
	 */
	private Map<String, FormatDef> dbTypeMap = new HashMap<String, FormatDef>();
	/**
	 * キー：書式名、値：書式定義
	 */
	private Map<String, FormatDef> nameMap = new HashMap<String, FormatDef>();
	/**
	 * 書式定義書のシート名
	 */
	private String sheetName = "入出力書式";

	/**
	 * 設計書マッピング定義の
	 */
	private String designDocBeanId;
	@Resource
	DocumentRepository repo;
	@Resource
	PropertyManager pm;
	@Resource
	DocumentMapper dm;

	/**
	 * カタログに書式定義を追加します。
	 *
	 * @param format 書式定義
	 */
	public void add(FormatDef format) {
		dbTypeMap.put(format.getDbType().toLowerCase(), format);
		nameMap.put(format.getName(), format);
	}

	/**
	 * 書式名をキーに書式定義を取得します。
	 *
	 * @param name 書式名
	 * @return 書式名に一致する書式定義 書式名がnull、または一致する書式名が無い場合はnullを返します。
	 */
	public FormatDef getByName(String name) {
		if (name == null) {
			return null;
		}
		FormatDef format = nameMap.get(name);
		if (format == null) {
			LOG.warn("定義されていない書式名が指定されました。{}", name);
		}
		return format;
	}

	/**
	 * 書式定義書を読み込み内部保持します。
	 */
	@PostConstruct
	public void load() {
		TableData tableData = repo.read(pm.getFormatDefPath(), getSheetName());
		for (RowData rowData : tableData.getRows()) {
			add(dm.map("formatDef", rowData, FormatDef.class));
		}
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
}
