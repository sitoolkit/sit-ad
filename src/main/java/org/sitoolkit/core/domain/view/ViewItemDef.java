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

import javax.annotation.Resource;
import org.sitoolkit.core.domain.format.FormatDef;
import org.sitoolkit.core.domain.format.FormatDefCatalog;
import org.sitoolkit.core.domain.view.itemcmp.CommonAction;
import org.sitoolkit.core.domain.view.itemcmp.DataBind;
import org.sitoolkit.core.domain.view.itemcmp.DesignInfoType;
import org.sitoolkit.core.domain.view.itemcmp.ItemControl;
import org.sitoolkit.core.infra.util.SitStringUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Yuichi Kuwahara
 */
public class ViewItemDef extends ItemDef {

	@Resource
	private FormatDefCatalog formatDefCatalog;
	/**
	 * 繰り返し変数
	 */
	private String repeatVar;
	
	private String contextVar;
	/**
	 * モデルを基準とした検索条件項目へのパス
	 */
	private String searchConditionPath;

	/**
	 * コード型フィールドのサフィックス
	 */
	private String codedFieldSuffix = "Vo";

	/**
	 * 繰り返し変数を返します。
	 * @return 繰り返し変数
	 */
	public String getRepeatVar() {
		return repeatVar;
	}

	/**
	 * 繰り返し変数を設定します。
	 * @param repeatVar 繰り返し変数
	 */
	public void setRepeatVar(String repeatVar) {
		this.repeatVar = repeatVar;
	}
	
	/**
	 * 入出力書式VOを返します。
	 * @return 入出力書式VO
	 */
	public FormatDef getIoformatObj() {
		return formatDefCatalog.getByName(getIoformat());
	}

	/**
	 * アクションボタンVOを返します。
	 * @return アクションボタンVO
	 */
	public CommonAction getActionButton() {
		return CommonAction.valueOf(getName());
	}
	
	/**
	 * データバインド列の文字列を、JSFコンポーネントのvalue属性に指定可能なEL式に変換します。
	 * 
	 * <pre>{@code 
	 *  dataBind = eq t_table_name.col_name contextVar = bean
	 *  -> colName_eq
	 * 
	 *  dataBind = t_table_name.col_name, contextVar = null or ""
	 *   -> tableName.entity.colName
	 * 
	 *  dataBind = t_table_name.col_name, repeatVar = r
	 *   -> r.colName
	 * }</pre>
	 * 
	 * @return データバインドを表すEL式文字列
	 */
	public String getDataBind2El() {
		if (getDataBindList().isEmpty()) {
			return "";
		}
		DataBind dataBind = getDataBindList().get(0);
		String fieldName = dataBind.getField();
		if (isCodedField() && !getControl().isInput()) {
			fieldName += getCodedFieldSuffix();
		}
		if (StringUtils.isNotEmpty(dataBind.getOperator())) {
			return getContextVar() + "."
					+ getSearchConditionPath() + "."
					+ dataBind.getOperator() + "_" + fieldName;
		} else if (StringUtils.isNotEmpty(getRepeatVar())){ 
			return getRepeatVar() + "."
					+ fieldName;
		} else {
			return getContextVar() + "."
					+ "entity." 
					+ fieldName;
		}
	}
	
	public String getDataBindColumn2El() {
		if (getDataBindList().isEmpty()) {
			return "";
		}
		DataBind dataBind = getDataBindList().get(0);
		return SitStringUtils.toCamel(dataBind.getColumn());
	}

	public String getSearchConditionPath() {
		return searchConditionPath;
	}

	public void setSearchConditionPath(String searchConditionPath) {
		this.searchConditionPath = searchConditionPath;
	}

	public String getContextVar() {
		return contextVar;
	}

	public void setContextVar(String contextVar) {
		this.contextVar = contextVar;
	}

	public String getCodedFieldSuffix() {
		return codedFieldSuffix;
	}

	public void setCodedFieldSuffix(String codedFieldSuffix) {
		this.codedFieldSuffix = codedFieldSuffix;
	}

}
