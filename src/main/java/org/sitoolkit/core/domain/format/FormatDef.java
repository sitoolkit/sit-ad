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
package org.sitoolkit.core.domain.format;

import org.sitoolkit.core.infra.doc.DocumentElement;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author yuichi.kuwahara
 */
public class FormatDef extends DocumentElement {
	
	/**
	 * アプリケーションを実装するプログラミング言語でのデータ型
	 */
	private String pgType;
	/**
	 * DBでのデータ型
	 */
	private String dbType;
	/**
	 * コンバータID
	 */ 
	private String converter;
	/**
	 * バリデータID
	 */
	private String validator;
	/**
	 * CSSのクラス
	 */
	private String cssClass;
	/**
	 * 入力書式
	 */
	private String inputFormat;
	/**
	 * 出力書式
	 */
	private String outputFormat;
	
	public String getConverter() {
		return converter;
	}

	public void setConverter(String converter) {
		this.converter = converter;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getPgType() {
		return pgType;
	}

	public void setPgType(String pgType) {
		this.pgType = pgType;
	}

	public String getValidator() {
		return validator;
	}

	public void setValidator(String validator) {
		this.validator = validator;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}
	
	/**
	 * 出力の際に書式を適用しない場合は{@code true}、そうでない場合は{@code false}を返します。
	 * 
	 * @return 
	 */
	public boolean isNoEdit() {
		return StringUtils.isEmpty(getOutputFormat()) || "－".equals(getOutputFormat());
	}

	public String getInputFormat() {
		return inputFormat;
	}

	public void setInputFormat(String inputFormat) {
		this.inputFormat = inputFormat;
	}

	public String getOutputFormat() {
		return outputFormat;
	}

	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}
}
