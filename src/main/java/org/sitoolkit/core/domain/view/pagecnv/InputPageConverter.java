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
import org.sitoolkit.core.domain.format.FormatDef;
import org.sitoolkit.core.domain.format.FormatDefCatalog;
import org.sitoolkit.core.domain.view.AreaDef.AreaType;
import org.sitoolkit.core.domain.view.ItemDef;
import org.sitoolkit.core.domain.view.PageDef;
import org.sitoolkit.core.domain.view.itemcmp.CommonAction;
import org.sitoolkit.core.domain.view.itemcmp.DesignInfoType;
import org.sitoolkit.core.domain.view.itemcmp.ItemControl;
import org.apache.commons.lang3.StringUtils;

/**
 * このクラスは、入力画面用の{@link PageDefConverter}です。
 *
 * @author Yuichi Kuwahara
 */
public class InputPageConverter extends PageDefConverter {

	@Resource
	FormatDefCatalog formatDefCatalog;

	private static CommonAction[] actions = new CommonAction[]{
		CommonAction.作成,
		CommonAction.更新,
		CommonAction.削除,
		CommonAction.一覧へ戻る,
	};

	/**
	 * アクション項目を配置する領域名
	 */
	private String actionItemAreaName = "フッター";

	/**
	 * 一覧ページの物理名サフィックス
	 */
	private String listPagePnameSuffix = "List";
	/**
	 *
	 * @param page
	 */
	@Override
	protected void convertAfter(PageDef page) {
		for (CommonAction action : actions) {
			ItemDef actionItem = new ItemDef();
			actionItem.setName(action.name());
			actionItem.setLabel(action.name());
			actionItem.setControl(action.getControl());
			actionItem.setArea(getAreaName(page), getActionItemAreaName());
			if (CommonAction.一覧へ戻る.equals(action)) {
				actionItem.addDesignInfo(DesignInfoType.NavigateTo, page.getDomain() + getListPagePnameSuffix());
			}
			page.addItemWithNo(actionItem);
		}
	}

	@Override
	protected void convertItem(int no, ColumnDef column, ItemDef item) {
		item.setControl(getControl(column));
		item.setInputLengthStr(column.getLength());
		item.setRequired(column.isNotNull() || column.isPrimaryKey());
		if (column.isPrimaryKey()) {
			item.addDesignInfo(DesignInfoType.InactiveOnUpdate, "");
		}
		if (no == 1) {
			item.addDesignInfo(DesignInfoType.AreaType, AreaType.フォーム);
		}

		String formatName = column.getFormat();
		if (StringUtils.isNotEmpty(formatName)) {
			FormatDef ioformat = formatDefCatalog.getByName(formatName);
			if (ioformat == null) {
				log.warn("書式[{}]は未定義です。", formatName);
			} else {
				item.setIoformat(ioformat.getName());
				item.addDesignInfo(DesignInfoType.CSSClass, ioformat.getCssClass());
			}
		}
	}

	/**
	 * 列定義に応じたフォームコントロールを取得する。
	 * @param column
	 * @return
	 */
	protected ItemControl getControl(ColumnDef column) {
		// TODO 変換定義を外部ファイル化
		if ("text".equals(column.getType())) {
			return ItemControl.テキストエリア;
		} else if ("boolean".equalsIgnoreCase(column.getType())) {
			return ItemControl.単一チェックボックス;
		} else if (StringUtils.isNotEmpty(column.getCodeSpec())) {
			return ItemControl.セレクトボックス;
		} else if (column.isPassword()) {
			return ItemControl.パスワード;
		}
		return ItemControl.テキストボックス;
	}

	@Override
	protected void convertBefore(PageDef page) {
		// NOP
	}

	@Override
	protected String getPageNameSuffix() {
		return "登録";
	}

	public String getActionItemAreaName() {
		return actionItemAreaName;
	}

	public void setActionItemAreaName(String actionItemAreaName) {
		this.actionItemAreaName = actionItemAreaName;
	}

	public String getListPagePnameSuffix() {
		return listPagePnameSuffix;
	}

	public void setListPagePnameSuffix(String listPagePnameSuffix) {
		this.listPagePnameSuffix = listPagePnameSuffix;
	}

}
