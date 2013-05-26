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

public enum CommonAction {
		作成("create", ItemControl.ボタン, "create", false),
		更新("update", ItemControl.ボタン, "update", false),
		削除("delete", ItemControl.ボタン, "update", false),
		一覧へ戻る("cancel", ItemControl.リンク, "any", true),
		検索("search", ItemControl.ボタン, "any", false),
		クリア("clear", ItemControl.ボタン, "any", true)
	;
	private String id;
	private ItemControl control;
	private String mode;
	private boolean immediate;
	
	/**
	 * 
	 * @param id
	 * @param mode
	 * @param immediate
	 */
	private CommonAction(String id, ItemControl control, String mode, boolean immediate) {
		this.id = id;
		this.control = control;
		this.mode = mode;
		this.immediate = immediate;
	}
	public String id(String domain) {
		return domain + "-" + id;
	}
	public ItemControl getControl() {
		return control;
	}
	public String action(String domain) {
		return domain + "." + id;
	}
	
	public String rendered(String domain) {
		return domain + ".mode." + mode;
	}
	
	public static boolean is(String name) {
		for(CommonAction btn : CommonAction.values()) {
			if(btn.name().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public String getId() {
		return id;
	}

	public boolean isImmediate() {
		return immediate;
	}

	public String getMode() {
		return mode;
	}

}
