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
package org.sitoolkit.core.domain.view.pagecnv;

import javax.annotation.Resource;
import org.sitoolkit.core.domain.data.ColumnDef;
import org.sitoolkit.core.domain.data.TableDef;
import org.sitoolkit.core.domain.view.AreaDef;
import org.sitoolkit.core.domain.view.ItemDef;
import org.sitoolkit.core.domain.view.PageDef;
import org.sitoolkit.core.domain.view.itemcmp.DataBind;
import org.sitoolkit.core.domain.view.itemcmp.DataBindList;
import org.sitoolkit.core.domain.view.itemcmp.DesignInfoType;
import org.sitoolkit.core.domain.view.itemcmp.ItemControl;
import org.sitoolkit.core.domain.view.itemcmp.NestedNamePair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * テーブル定義VOを画面定義VOに変換するクラス
 * @author Yuichi Kuwahara
 *
 */
public abstract class  PageDefConverter {

	@Resource
	protected ApplicationContext appCtx;

	protected final Logger log = LoggerFactory.getLogger(PageDefConverter.class);
	
	/**
	 * テーブル定義をフォーム領域を持った画面定義へ変換する。
	 * @param table
	 * @return
	 */
	public PageDef convert(TableDef table) {
		PageDef page = appCtx.getBean("pageDef", PageDef.class);
		page.setTable(table);
		page.setDomain(table.getDomainPname());
		page.setName(table.getSimpleName() + getPageNameSuffix());

		convertBefore(page);
		
		int no = 1;
		for (ColumnDef column : table.getColumns()) {
			ItemDef item = convert(table, column);
			if (item == null) {
				continue;
			}
			item.setAreaStr(new NestedNamePair(getAreaName(page)));
			convertItem(no++, column, item);
			log.debug("カラム定義[{}]を項目定義[{}]に変換しました。", column.getName(), item.getName());
			page.addItemWithNo(item);
		}
		
		convertAfter(page);
		
		return page;
	}

	protected abstract void convertBefore(PageDef page);

	protected abstract void convertAfter(PageDef page);
	
	protected abstract void convertItem(int no, ColumnDef column, ItemDef item);
	
	protected abstract String getPageNameSuffix();
	
	protected String getAreaName(PageDef page) {
		return page.getName();
	}
	
	/**
	 * 列定義を項目定義へ変換する。
	 * @param table
	 * @param column
	 * @param areaStr
	 * @return
	 */
	protected ItemDef convert(TableDef table, ColumnDef column) {
		ItemDef item = new ItemDef();
		item.setName(column.getName());
		item.setLabel(column.getName());
		item.setOptionAsCodeSpec(column.getCodeSpec());
		item.getDataBindList().add(new DataBind(table.getPname(), column.getPname()));
		return item;
	}
	
	protected ItemDef buildPageNumItem() {
		ItemDef pageNumItem = new ItemDef();
		pageNumItem.setName("ページ番号");
		pageNumItem.setLabel("cpn");
		pageNumItem.setControl(ItemControl.リクエストパラメータ);
		pageNumItem.setArea(AreaDef.AreaType.メタ情報.name(), "");
		pageNumItem.addDesignInfo(DesignInfoType.AreaType, AreaDef.AreaType.メタ情報.name());
		pageNumItem.setDataBindList(new DataBindList("user.page_ctrl.current_page_num"));
		return pageNumItem;
	}
}
