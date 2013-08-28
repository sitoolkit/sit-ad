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
package org.sitoolkit.core.domain.view;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.sitoolkit.core.domain.data.TableDef;
import org.sitoolkit.core.infra.repository.DocumentMapper;
import org.sitoolkit.core.infra.repository.RowData;
import org.sitoolkit.core.infra.repository.TableData;
import org.sitoolkit.core.infra.srccd.SourceCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

/**
 * このクラスは、画面定義を表すエンティティです。
 * @author Yuichi Kuwahara
 *
 */
public class PageDef extends SourceCode {

	private String id;
	/**
	 * ドメイン
	 */
	private String domain;
	private Map<String, AreaDef> areas = new LinkedHashMap<String, AreaDef>();
	private TableDef table;
	/**
	 * 領域定義を表すBeanId
	 */
	private String areDefId;
	/**
	 * 項目定義を表すBeanId
	 */
	private String itemDefId;
	/**
	 * パス
	 */
	private String parentPath;

	@Resource
	ApplicationContext appCtx;
	@Resource
	DocumentMapper dm;

	private Map<String, AreaDef> areas() {
		return areas;
	}

	public Collection<AreaDef> getAreas() {
		return areas().values();
	}

	/**
	 * 画面項目定義を画面定義に追加します。
	 * 追加の際に項目番号を採番します。
	 * @param item 画面項目定義
	 */
	public void addItemWithNo(ItemDef item) {
		item.setNo(getItemCount() + 1);
		addItem(item);
	}

	/**
	 * 画面項目定義を画面定義に追加します。
	 * @param item 画面項目定義
	 */
	public void addItem(ItemDef item) {
		log.debug("項目[{}]を画面[{}]に追加します。", item.getName(), getName());
		final String areaName = item.isLocatedMainArea()
				? item.getAreaName()
				: item.getParentAreaName();
		AreaDef area = areas().get(areaName);
		if (area == null) {
			area = appCtx.getBean(getAreDefId(), AreaDef.class);
			area.init(getDomain(), item);
			log.debug("領域[{}]を画面[{}]に追加します。", areaName, getName());
			areas().put(areaName, area);
		}
		area.addItem(item);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public TableDef getTable() {
		return table;
	}

	public void setTable(TableDef table) {
		this.table = table;
	}

	public TableData toTableData() {
		TableData tableData = new TableData();
		int no = 1;
		for(AreaDef area : getAreas()) {
			for(ItemDef item : area.getAllItems()) {
				tableData.add(dm.map("itemDef", item));
			}
		}

		return tableData;
	}

	public void load(TableData tableData) {
		for(RowData rowData : tableData.getRows()) {
			addItem(dm.map(getItemDefId(), rowData, ItemDef.class));
		}
	}

	public int getItemCount() {
		int cnt = 0;
		for(AreaDef area : getAreas()) {
			cnt += area.getItemCount();
		}
		return cnt;
	}

	public String getAreDefId() {
		return areDefId;
	}

	public void setAreDefId(String areDefId) {
		this.areDefId = areDefId;
	}

	public String getItemDefId() {
		return itemDefId;
	}

	public void setItemDefId(String itemDefId) {
		this.itemDefId = itemDefId;
	}

	@Override
	public String getOutDir() {
		return super.getOutDir() + "/" + getParentPath();
	}

	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	public String getBase() {
		return StringUtils.repeat("../", StringUtils.countMatches(getParentPath(), "/") + 1);
	}
}
