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
package org.sitoolkit.core.domain.view.itemcmp;

/**
 * 項目のGUIコントロールの種類を定義する列挙型
 *
 * @author Yuichi Kuwahara
 */
public enum ItemControl {
	テキストボックス			(true, 0, "textbox"),
	テキストエリア			(true, 0, "textarea"),
	ラジオボタン			(true, 0, "radioButton"),
	単一チェックボックス		(true, 1, "singleCheck"),
	複数チェックボックス		(true, 2, "multiCheck"),
	セレクトボックス			(true, 1, "selectBox"),
	リストボックス			(true, 1, "listBox"),
	テキスト					(false, 0, "text"),
	ボタン					(false, 0, "button"),
	リンク					(false, 0, "link"),
	イメージ				(false, 0, "image"),
	ブランク					(false, 0, ""),
	パスワード				(true, 0, "password"),
	非表示テキスト			(true, 0, "hidden"),
	リクエストパラメータ		(true, 0, "requestParameter")
	;
	/**
	 * 入力項目であるか否か
	 */
	private boolean input;
	/**
	 * 選択のタイプ
	 * 0:選択無し、1:単一選択、2:複数選択
	 */
	private int selectType;
	/**
	 * テンプレートキー
	 */
	private String template;

	private ItemControl(boolean input, int selectType, String template) {
		this.input = input;
		this.selectType = selectType;
		this.template = template;
	}

	public boolean isInput() {
		return input;
	}

	public String getTemplate() {
		return template;
	}
	
	public static ItemControl deserialize(String name) {
		for(ItemControl c : ItemControl.values()) {
			if(c.name().equals(name)) {
				return c;
			}
		}
		return ブランク;
	}
	
	/**
	 * 選択タイプが「複数」の場合にtrueを返します。
	 * @return 選択タイプが「複数」の場合にtrue 
	 */
	public boolean isMultiSelect() {
		return selectType == 2;
	}
	
	/**
	 * 選択タイプが「選択無し」でない場合にtrueを返します。
	 * @return 選択タイプが「選択無し」でない場合にtrue
	 */
	public boolean isSelect() {
		return selectType != 0;
	}
}
