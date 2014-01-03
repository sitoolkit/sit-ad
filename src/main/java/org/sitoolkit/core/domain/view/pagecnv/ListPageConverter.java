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
import org.sitoolkit.core.domain.format.FormatDef;
import org.sitoolkit.core.domain.format.FormatDefCatalog;
import org.sitoolkit.core.domain.view.AreaDef.AreaType;
import org.sitoolkit.core.domain.view.ItemDef;
import org.sitoolkit.core.domain.view.PageDef;
import org.sitoolkit.core.domain.view.itemcmp.CommonAction;
import org.sitoolkit.core.domain.view.itemcmp.DataBind;
import org.sitoolkit.core.domain.view.itemcmp.DataBindList;
import org.sitoolkit.core.domain.view.itemcmp.DesignInfoType;
import org.sitoolkit.core.domain.view.itemcmp.ItemControl;
import org.sitoolkit.core.domain.view.itemcmp.NestedNamePair;
import org.apache.commons.lang3.StringUtils;

public class ListPageConverter extends PageDefConverter {

	@Resource
	protected FormatDefCatalog formatDefCatalog;

	private CommonAction[] actions = new CommonAction[] {
		CommonAction.検索,
		CommonAction.クリア
	};

	/**
	 * 検索条件領域名
	 */
	private String searchAreaName = "検索条件";

	/**
	 * アクション項目を配置する領域名
	 */
	private String actionItemAreaName = "フッター";

	/**
	 * 入力ページの物理名サフィックス
	 */
	private String inputPagePnameSuffix = "Input";

	@Override
	protected void convertAfter(PageDef page) {
		// NOP
	}

	@Override
	protected void convertItem(int no, ColumnDef column, ItemDef item) {
		item.setControl(ItemControl.テキスト);
		FormatDef format = formatDefCatalog.getByName(column.getFormat());
		if (format != null) {
			if (!format.isNoEdit()) {
				item.setIoformat(format.getName());
				item.addDesignInfo(DesignInfoType.CSSClass, format.getCssClass());
			}
		}
	}

	@Override
	protected void convertBefore(PageDef page) {
		int itemCnt = 0;
		for (ColumnDef column : page.getTable().getPks()) {
			ItemDef searchItem = new ItemDef();
			searchItem.setArea(getSearchAreaName(), "");
			searchItem.setName(column.getName());
			searchItem.setLabel(column.getName());
			searchItem.setInputLengthStr(column.getLength());
			searchItem.setIoformat(column.getFormat());
			searchItem.setControl(getControl(column));
			searchItem.setDataBindList(new DataBindList(getSearchItemDataBind(column)));

			if (itemCnt++ == 0) {
				searchItem.addDesignInfo(DesignInfoType.AreaType, AreaType.フォーム);
			}

			page.addItem(searchItem);
		}

		for (CommonAction action : actions) {
			ItemDef actionItem = new ItemDef();
			actionItem.setArea(getSearchAreaName(), getActionItemAreaName());
			actionItem.setLabel(action.name());
			actionItem.setName(action.name());
			actionItem.setControl(action.getControl());
			actionItem.addDesignInfo(DesignInfoType.Ajax, "");

			page.addItem(actionItem);
		}

		ItemDef detailLink = new ItemDef();
		detailLink.setAreaStr(new NestedNamePair(getAreaName(page)));
		detailLink.setName("詳細表示");
		detailLink.setLabel("表示");
		detailLink.setControl(ItemControl.リンク);
		for (ColumnDef column : page.getTable().getPks()) {
			detailLink.getDataBindList().add(new DataBind(page.getTable().getPname(), column.getPname()));
		}
		detailLink.addDesignInfo(DesignInfoType.AreaType, AreaType.テーブル);
		detailLink.addDesignInfo(DesignInfoType.NavigateTo, page.getDomain() + getInputPagePnameSuffix());
		page.addItemWithNo(detailLink);
	}

	@Override
	protected ItemDef convert(TableDef table, ColumnDef column) {
		// TODO 暫定処置
		if (column.isPassword() || "text".equals(column.getType())) {
			return null;
		}
		ItemDef item = super.convert(table, column);
		item.addDesignInfo(DesignInfoType.Sortable, "");
		return item;
	}

	@Override
	protected String getPageNameSuffix() {
		return "一覧";
	}

	/**
	 * 列定義に応じたフォームコントロールを取得する。
	 * @param column
	 * @return
	 */
	protected ItemControl getControl(ColumnDef column) {
		// TODO 変換定義を外部ファイル化
		if ("boolean".equalsIgnoreCase(column.getType())) {
			return ItemControl.単一チェックボックス;
		} else if (StringUtils.isNotEmpty(column.getCodeSpec())) {
			return ItemControl.複数チェックボックス;
		}
		return ItemControl.テキストボックス;
	}

	protected String getSearchItemDataBind(ColumnDef column) {
		String operator = "sw";
		if (StringUtils.isNotEmpty(column.getCodeSpec())) {
			operator = "in";
		}
		return operator + " " + column.getTable().getPname() + "." + column.getPname();
	}

	public String getSearchAreaName() {
		return searchAreaName;
	}

	public void setSearchAreaName(String searchAreaName) {
		this.searchAreaName = searchAreaName;
	}

	public String getActionItemAreaName() {
		return actionItemAreaName;
	}

	public void setActionItemAreaName(String actionItemAreaName) {
		this.actionItemAreaName = actionItemAreaName;
	}

	public String getInputPagePnameSuffix() {
		return inputPagePnameSuffix;
	}

	public void setInputPagePnameSuffix(String inputPagePnameSuffix) {
		this.inputPagePnameSuffix = inputPagePnameSuffix;
	}
}
