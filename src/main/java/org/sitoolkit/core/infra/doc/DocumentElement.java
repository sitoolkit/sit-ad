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
package org.sitoolkit.core.infra.doc;

/**
 *
 * @author Yuichi Kuwahara
 */
public abstract class DocumentElement {

	/**
	 * 要素番号
	 */
	private int no;
	/**
	 * 要素名
	 */
	private String name;
	/**
	 * 要素物理名
	 */
	private String pname;

	/**
	 * 要素名を返します。
	 * @return 要素名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 要素名を設定します。
	 * @param name 要素名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 連番を返します。
	 * @return 連番
	 */
	public int getNo() {
		return no;
	}

	/**
	 * 連番を設定します。
	 * @param no 連番
	 */
	public void setNo(int no) {
		this.no = no;
	}

	/**
	 * 物理名を返します。
	 * @return 物理名
	 */
	public String getPname() {
		return pname;
	}

	/**
	 * 物理名を設定します。
	 * @param pname 物理名
	 */
	public void setPname(String pname) {
		this.pname = pname;
	}
}
