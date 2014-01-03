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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import org.sitoolkit.core.domain.code.CodeDef;
import org.sitoolkit.core.domain.code.CodeDefCatalog;
import org.sitoolkit.core.domain.code.CodeItemDef;
import org.sitoolkit.core.domain.view.itemcmp.DataBindList;
import org.sitoolkit.core.domain.view.itemcmp.DesignInfoType;
import org.sitoolkit.core.domain.view.itemcmp.ItemControl;
import org.sitoolkit.core.domain.view.itemcmp.ItemPosition;
import org.sitoolkit.core.domain.view.itemcmp.NestedNamePair;
import org.sitoolkit.core.infra.doc.DocumentElement;
import org.sitoolkit.core.infra.doc.KeyValuePair;
import org.sitoolkit.core.infra.doc.KeyValuePairMap;
import org.sitoolkit.core.infra.util.SitStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * このクラスは、1つの画面項目の定義を表すエンティティです。
 * インスタンスは画面項目定義の1行に相当し、
 *
 * @author Yuichi Kuwahara
 *
 */
public class ItemDef extends DocumentElement {

	private static final String CODE_SPEC_PREFIX = "コード仕様：";

	/**
	 * 領域欄の文字列
	 */
	private NestedNamePair areaStr;
	/**
	 * 項目欄の文字列
	 */
	private NestedNamePair itemStr;
	/**
	 * ラベル
	 */
	private String label;
	/**
	 * ガイダンス
	 */
	private String guidance;
	/**
	 * 選択肢
	 */
	private String choice;
	/**
	 * 表示桁数
	 */
	private int displayLength;
	/**
	 * 入力桁数
	 */
	private int inputLength;
	/**
	 * その他設計情報
	 */
	private KeyValuePairMap designInfoMap;
	/**
	 * コントロール
	 */
	private ItemControl control;
	/**
	 * 入力必須
	 */
	private boolean required;
	/**
	 * 入出力書式
	 */
	private String ioformat;
	/**
	 * CSSクラス
	 */
	private String cssClass;
	/**
	 * 右配置のサブ項目
	 */
	private List<ItemDef> rightSubItems;
	/**
	 * 下配置のサブ項目
	 */
	private List<ItemDef> bottomSubItems;
	/**
	 * 選択肢(デコード後)
	 */
	private List<CodeItemDef> options;
	/**
	 * 位置
	 */
	private ItemPosition position;
	/**
	 * ドメイン
	 */
	private String domain;
	/**
	 * データバインド
	 */
	private DataBindList dataBindList;

	@Resource
	private CodeDefCatalog codeDefCatalog;

	/**
	 * 領域欄の文字列を取得します。
	 * @return 領域欄の文字列
	 */
	public NestedNamePair getAreaStr() {
		return areaStr;
	}

	/**
	 * 領域欄の文字列を設定します。
	 * @param areaStr 領域欄の文字列
	 */
	public void setAreaStr(NestedNamePair areaStr) {
		this.areaStr = areaStr;
	}

	/**
	 * 項目が配置される領域名を取得します。
	 * 領域欄の文字列が読み込まれていない場合は、空文字を返します。
	 * @return 項目が配置される領域名
	 */
	public String getAreaName() {
		return getAreaStr() == null ? "" : getAreaStr().getSelfName();
	}

	/**
	 * 項目が配置される領域の親領域の名前を取得します。
	 * 領域欄の文字列が読み込まれていない場合、
	 * または項目の配置される領域がメイン領域である場合は、空文字を返します。
	 * @return 親領域の名前
	 */
	public String getParentAreaName() {
		return getAreaStr() == null ? "" : getAreaStr().getParentName();
	}

	/**
	 * 項目がメイン領域に配置される場合にtrueを返します。
	 * @return 項目がメイン領域に配置されている場合はtrue
	 */
	public boolean isLocatedMainArea() {
		return StringUtils.isEmpty(getParentAreaName());
	}
	/**
	 * 項目欄の文字列を取得します。
	 * @return 項目欄の文字列
	 */
	public NestedNamePair getItemStr() {
		if (itemStr == null) {
			itemStr = new NestedNamePair();
		}
		return itemStr;
	}

	/**
	 * 項目欄の文字列を設定します。
	 * @param itemStr 項目欄の文字列
	 */
	public void setItemStr(NestedNamePair itemStr) {
		this.itemStr = itemStr;
	}

	/**
	 * 項目名を取得します。
	 */
	@Override
	public String getName() {
		return getItemStr().getSelfName();
	}

	/**
	 * 項目名を設定します。
	 */
	@Override
	public void setName(String name) {
		getItemStr().setSelfName(name);
	}

	/**
	 * 親項目の項目名を取得します。
	 * @return 親項目の項目名
	 */
	public String getParentName() {
		return getItemStr().getParentName();
	}

//	/**
//	 * 項目列の入力値を親項目名、位置、子項目名に分割する。
//	 * {親項目名}\n{子項目名}
//	 */
//	private void splitNameStr() {
//		NameSplitter splitter = NameSplitter.split(getName());
//		if(splitter.hasParent()) {
//			setName(splitter.getName());
//			setParentName(splitter.getParentName());
//		} else {
//			setName(splitter.getName());
//			setParentName("");
//		}
//	}

	/**
	 * コントロールを取得します。
	 * @return コントロール
	 */
	public ItemControl getControl() {
		if(control == null) {
			control = ItemControl.ブランク;
		}
		return control;
	}

	/**
	 * 項目IDを取得する。項目IDは以下の優先順位で決まる。
	 *
	 * <ol>
	 * <li>項目物理名<br/>
	 *		項目物理名が設定されている場合は、
	 *		項目物理名を項目IDとする。</li>
	 * <li>データバインド<br/>
	 *		項目物理名が設定されておらず、データバインドが設定されている場合は、
	 *		項目物理名、項目名のいずえれも設定されていない場合は、
	 *		データバインドの文字列をハイフン区切り、Camel形式に変換した文字列を項目IDとする。</li>
	 * <li>項目名<br/>
	 *		項目物理名、データバインドいずれも設定されていない場合は、
	 *		項目名を項目IDとする。
	 * </li>
	 * </ol>
	 *
	 * @return 項目ID
	 */
	public String getId() {
		if(StringUtils.isEmpty(getPname())) {
			String id = getDataBindList().toItemId();
			return StringUtils.defaultIfEmpty(id, getName());
		} else {
			return getPname();
		}
	}

	/**
	 * 右配置のサブ項目を取得します。
	 * @return 右配置のサブ項目
	 */
	public List<ItemDef> getRightSubItems() {
		if (rightSubItems == null) {
			rightSubItems = new ArrayList<ItemDef>();
		}
		return rightSubItems;
	}

	/**
	 * 項目がサブ項目である場合にtrueを返します。
	 * @return 項目がサブ項目である場合はtrue
	 */
	public boolean isSubItem() {
		return StringUtils.isNotEmpty(getParentName());
	}

	/**
	 * 選択肢(デコード後)を取得します。
	 * @return 選択肢(デコード後)
	 */
	public List<CodeItemDef> getOptions() {
		if(options == null) {
			String codeName = getCodeName();
			if(StringUtils.isEmpty(codeName)) {
				options = CodeDef.deserialize(getChoice());
			} else {
				CodeDef codeDef = codeDefCatalog.get(codeName);
				if(codeDef == null) {
					options = Collections.emptyList();
				} else {
					options = codeDef.getItems();
				}
			}
		}
		return options;
	}

	/**
	 * 当該項目の選択肢に指定されるコード名を取得する。
	 *
	 * @return コード名
	 */
	public String getCodeName() {
		String str = getChoice();

		if(StringUtils.isEmpty(str)) {
			return "";
		}

		if(str.startsWith(CODE_SPEC_PREFIX)) {
			return str.replace(CODE_SPEC_PREFIX, "");
		} else {
			return "";
		}
	}

	public void setOptionAsCodeSpec(String str) {
		if(!StringUtils.isEmpty(str)) {
			setChoice(CODE_SPEC_PREFIX + str);
		}
	}

	/**
	 * 下配置のサブ項目を取得します。
	 * @return 下配置のサブ項目
	 */
	public List<ItemDef> getBottomSubItems() {
		if (bottomSubItems == null) {
			bottomSubItems = new ArrayList<ItemDef>();
		}
		return bottomSubItems;
	}

	public void addSubItem(ItemDef item) {
		switch(item.getPosition()) {
			case 下:
				getBottomSubItems().add(item);
				break;
			case 右:
				getRightSubItems().add(item);
				break;
			default:

		}
	}

	/**
	 * 親項目の項目名を設定します。
	 * @param parentName 親項目の項目名
	 */
	public void setParentName(String parentName) {
		getItemStr().setParentName(parentName);
	}

	/**
	 * 位置を取得します。
	 * @return 位置
	 */
	public ItemPosition getPosition() {
		if(position == null) {
			position = ItemPosition.decode(getDesignInfo(DesignInfoType.Position));
		}
		return position;
	}

	/**
	 * 入力必須を取得します。
	 * @return 入力必須
	 */
	public boolean isRequired() {
		return this.required;
	}

	/**
	 * 入力必須を設定します。
	 * @param required 入力必須
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}

	public void setRequiredStr(String required) {
		this.required = "○".equals(required);
	}

	/**
	 * ドメインを取得します。
	 * @return ドメイン
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * ドメインを設定します。
	 * @param domain ドメイン
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void addDesignInfo(DesignInfoType key, Object value) {
		if(value == null) {
			return ;
		}
		getDesignInfoMap().add(new KeyValuePair(key.name(), value.toString()));
	}

	public void setArea(String name, String part) {
		setAreaStr(new NestedNamePair(name, part));
	}

	public String getDesignInfo(String key) {
		return getDesignInfoMap().getValue(key);
	}

	public String getDesignInfo(DesignInfoType type) {
		return getDesignInfoMap().getValue(type.name());
	}

	public boolean hasDesignInfo(String key) {
		return getDesignInfoMap().containsKey(key);
	}
	/**
	 * 選択肢を取得します。
	 * @return 選択肢
	 */
	public String getChoice() {
		return choice;
	}

	/**
	 * 選択肢を設定します。
	 * @param choice 選択肢
	 */
	public void setChoice(String choice) {
		this.choice = choice;
	}

	/**
	 * 表示桁数を取得します。
	 * @return 表示桁数
	 */
	public int getDisplayLength() {
		return displayLength;
	}

	/**
	 * 表示桁数を設定します。
	 * @param displayLength 表示桁数
	 */
	public void setDisplayLength(int displayLength) {
		this.displayLength = displayLength;
	}

	/**
	 * 入力桁数を取得します。
	 * @return 入力桁数
	 */
	public int getInputLength() {
		return inputLength;
	}

	/**
	 * 入力桁数を設定します。
	 * @param inputLength 入力桁数
	 */
	public void setInputLength(int inputLength) {
		this.inputLength = inputLength;
	}

		/**
	 * 入力桁数を設定します。
	 * @param inputLength 入力桁数
	 */
	public void setInputLengthStr(String inputLength) {
		this.inputLength = NumberUtils.toInt(inputLength, 10);
	}


	/**
	 * ラベルを取得します。
	 * @return ラベル
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * ラベルを設定します。
	 * @param label ラベル
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * コントロールを設定します。
	 * @param control コントロール
	 */
	public void setControl(ItemControl control) {
		this.control = control;
	}

	/**
	 * コントロールを設定します。
	 * @param control コントロールに変換可能な文字列
	 * @see ItemControl#deserialize(String)
	 */
	public void setControlStr(String control) {
		setControl(ItemControl.deserialize(control));
	}

	public String getControlStr() {
		return getControl().toString();
	}

	/**
	 * ガイダンスを取得します。
	 * @return ガイダンス
	 */
	public String getGuidance() {
		return guidance;
	}

	/**
	 * ガイダンスを設定します。
	 * @param guidance ガイダンス
	 */
	public void setGuidance(String guidance) {
		this.guidance = guidance;
	}

	/**
	 * データバインドを取得します。
	 * @return データバインド
	 */
	public DataBindList getDataBindList() {
		if(dataBindList == null) {
			dataBindList = new DataBindList();
		}
		return dataBindList;
	}

	/**
	 * データバインドを設定します。
	 * @param dataBindList データバインド
	 */
	public void setDataBindList(DataBindList dataBindList) {
		this.dataBindList = dataBindList;
	}

	/**
	 * その他設計情報を取得します。
	 * @return その他設計情報
	 */
	public KeyValuePairMap getDesignInfoMap() {
		if(designInfoMap == null) {
			designInfoMap = new KeyValuePairMap();
		}
		return designInfoMap;
	}

	/**
	 * その他設計情報を設定します。
	 * @param designInfoMap その他設計情報
	 */
	public void setDesignInfoMap(KeyValuePairMap designInfoMap) {
		this.designInfoMap = designInfoMap;
	}

	/**
	 * 入出力書式を取得します。
	 * @return 入出力書式
	 */
	public String getIoformat() {
		return ioformat;
	}

	/**
	 * 入出力書式を設定します。
	 * @param ioformat 入出力書式
	 */
	public void setIoformat(String ioformat) {
		this.ioformat = ioformat;
	}

	/**
	 * CSSクラスを取得します。
	 * @return CSSクラス
	 */
	public String getCssClass() {
		if(cssClass == null) {
			cssClass = getDesignInfo(DesignInfoType.CSSClass);
		}
		return cssClass;
	}

	/**
	 * CSSクラスを設定します。
	 * @param cssClass CSSクラス
	 */
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public boolean isCodedField() {
		return StringUtils.isNotEmpty(getCodeName());
	}
}
