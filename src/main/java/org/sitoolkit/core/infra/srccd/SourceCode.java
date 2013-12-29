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
package org.sitoolkit.core.infra.srccd;

import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;

import org.sitoolkit.core.infra.doc.DocumentElement;
import org.sitoolkit.core.infra.util.SitException;
import org.sitoolkit.core.infra.util.SitFileUtils;
import org.sitoolkit.core.infra.util.TextFile;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.sitoolkit.core.infra.util.PropertyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * このクラスは、ソースファイルに対応するクラスが継承すべき抽象クラスです。
 * @author Yuichi Kuwahara
 * @version 1.0
 * @since 1.0
 */
public abstract class SourceCode extends DocumentElement {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	@Resource
	PropertyManager pm;

	private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
	/**
	 * 出力するソースコードファイル名の拡張子
	 */
	private String fileExt;
	/**
	 * 出力ディレクトリのパス
	 */
	private String outDir;
	/**
	 * テンプレートのパス
	 */
	private String template;
	/**
	 * テンプレート内で参照する変数名
	 */
	private String var;
	/**
	 * Velocity設定ファイルのパス
	 */
	private static final String VELO_PROP_PATH = "/velocity.properties";
	/**
	 *
	 */
	private Map<String, Object> contextParam = new HashMap<String, Object>();
	/**
	 * 既存のソースファイルを上書きする場合にtrue
	 */
	private boolean overwrite = true;
	/**
	 * テキストファイルオブジェクトを構築して返します。
	 * テキストファイルの内容は、{@link #buildSrcCd()}の結果の文字列です。
	 * @return テキストファイルオブジェクト
	 */
	public TextFile toFile() {
		TextFile file = new TextFile(getOutDir(), getFileName());
		if (pm.isRebuild() || isOverwrite() || !file.exists()) {
			log.info("ソースコードを構築します。テンプレート：「{}」、出力ファイル名：「{}」"
					,getTemplate(), getFileName());
			file.setText(build());
		} else {
			log.info("ソースコードは既に存在します。{}",
					file.getAbsolutePath());
		}
		return file;
	}

	/**
	 * ソースコードの文字列を構築して返します。
	 * @return ソースコードの文字列
	 */
	protected String build() {
		try {

			Velocity.init(SitFileUtils.resource2prop(getClass(), VELO_PROP_PATH));
			Template tmpl = Velocity.getTemplate(getTemplate());
			VelocityContext context = new VelocityContext();
			StringWriter writer = new StringWriter();
			context.put(getVar(), this);
			for (Map.Entry<String, Object> param : contextParam.entrySet()) {
				context.put(param.getKey(), param.getValue());
			}
			tmpl.merge(context, writer);
			return writer.toString();
		} catch (Exception e) {
			throw new SitException(e);
		}
	}

	public Date getGeneratedDate() {
		return new Date();
	}

	/**
	 * 書式を適用したソースコード生成日時を取得します。
	 * @return 書式を適用したソースコード生成日時
	 */
	public String getGenDtStr() {
		return getDateFormat().format(getGeneratedDate());
	}

	/**
	 * dateFormatを取得します。
	 * @return dateFormat
	 */
	public DateFormat getDateFormat() {
		return dateFormat;
	}

	/**
	 * dateFormatを設定します。
	 * @param dateFormat dateFormat
	 */
	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getFileName() {
		return getPname() + "." + getFileExt();
	}

	/**
	 * 出力ディレクトリのパスを取得します。
	 * @return 出力ディレクトリのパス
	 */
	public String getOutDir() {
		return outDir;
	}

	/**
	 * 出力ディレクトリのパスを設定します。
	 * @param outDir 出力ディレクトリのパス
	 */
	public void setOutDir(String outDir) {
		this.outDir = outDir;
	}

	/**
	 * テンプレートのパスを取得します。
	 * @return テンプレートのパス
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * テンプレートのパスを設定します。
	 * @param template テンプレートのパス
	 */
	public void setTemplate(String template) {
		this.template = template;
	}

	/**
	 * テンプレート内で参照する変数名を取得します。
	 * @return テンプレート内で参照する変数名
	 */
	public String getVar() {
		return var;
	}

	/**
	 * テンプレート内で参照する変数名を設定します。
	 * @param var テンプレート内で参照する変数名
	 */
	public void setVar(String var) {
		this.var = var;
	}

	/**
	 * 出力するソースコードファイル名の拡張子を取得します。
	 * @return 出力するソースコードファイル名の拡張子
	 */
	public String getFileExt() {
		return fileExt;
	}

	/**
	 * 出力するソースコードファイル名の拡張子を設定します。
	 * @param fileExt 出力するソースコードファイル名の拡張子
	 */
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}

	public void addContextParam(String name, Object obj) {
		contextParam.put(name, obj);
	}

	public boolean isOverwrite() {
		return overwrite;
	}

	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}
}
