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
 *
 * @author Yuichi Kuwahara
 */
public enum DesignInfoType {
	InactiveOnUpdate("更新時非活性"),
	Sortable("ソート可"),
	Ajax("非同期"),
	AreaType("領域タイプ"),
	Position("位置"),
	NavigateTo("遷移先"),
	DefaultValue("初期値"),
	AreaPName("領域物理名"),
	FormLaneCount("フォームレーン数"),
	SubAreaPName("サブ領域物理名"),
	File("ファイル"),
	CSSClass("CSSクラス"),
	Blank("ブランク"),
	;
	
	private String label = "";
	
	private DesignInfoType() {
	}
	
	private DesignInfoType(String label) {
		this.label = label;
	}

	public static DesignInfoType desirialize(String str) {
		for (DesignInfoType type : values()) {
			if (type.name().equals(str) || type.getLabel().equals(str)) {
				return type;
			}
		}
		return Blank;
	}

	public String getLabel() {
		return label;
	}
	
}
