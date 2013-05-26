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

import org.sitoolkit.core.infra.repository.StringLoadable;
import org.apache.commons.lang3.StringUtils;

/**
 * このクラスは、1つの文字列で親と子の対を表すためのVOです。
 * @author Yuichi Kuwahara
 */
public class NestedNamePair implements StringLoadable {

	/**
	 * 親要素の名前
	 */
	private String parentName;
	/**
	 * 自身の名前
	 */
	private String selfName;

	/**
	 * 特に何も処理しません。
	 */
	public NestedNamePair() {
		super();
	}
	
	public NestedNamePair(String str) {
		this();
		load(str);
	}

	/**
	 * 
	 * @param parent
	 * @param child
	 */
	public NestedNamePair(String parent, String child) {
		this.parentName = parent;
		this.selfName = child;
	}
	
	/**
	 * 親要素の名前を取得します。
	 * @return 親要素の名前
	 */
	public String getParentName() {
		return parentName;
	}

	/**
	 * 親要素の名前を設定します。
	 * @param parentName 親要素の名前
	 */
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	/**
	 * 自身の名前を取得します。
	 * @return 自身の名前
	 */
	public String getSelfName() {
		return selfName;
	}

	/**
	 * 自身の名前を設定します。
	 * @param selfName 自身の名前
	 */
	public void setSelfName(String selfName) {
		this.selfName = selfName;
	}

	@Override
	public String toString() {
		if(StringUtils.isEmpty(getParentName())) {
			return getSelfName();
		} else {
			return getParentName()  + getElementSplit() + getSelfName();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final NestedNamePair other = (NestedNamePair) obj;
		if ((this.parentName == null) ? (other.parentName != null) : !this.parentName.equals(other.parentName)) {
			return false;
		}
		if ((this.selfName == null) ? (other.selfName != null) : !this.selfName.equals(other.selfName)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 79 * hash + (this.parentName != null ? this.parentName.hashCode() : 0);
		hash = 79 * hash + (this.selfName != null ? this.selfName.hashCode() : 0);
		return hash;
	}

	@Override
	public void load(String str) {
		if(StringUtils.isEmpty(str)) {
			return;
		}
		String[] split = str.split(getElementSplit());
		if(split.length > 1) {
			setParentName(split[0]);
			setSelfName(split[1]);
		} else {
			setParentName("");
			setSelfName(split[0]);
		}
	}

	private String getElementSplit() {
		return "\n";
	}
}
