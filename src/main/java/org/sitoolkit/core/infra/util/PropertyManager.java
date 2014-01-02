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
package org.sitoolkit.core.infra.util;

import java.util.MissingFormatArgumentException;
import java.util.Properties;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ツール共通プロパティファイル：sitoolkit.xmlへのアクセスを提供するクラス
 *
 * 当該クラスはシングルトンであり、初回のインスタンスアクセス時にプロパティファイルを読み込む。
 * 読み込むプロパティファイルのパスは次の順に決まる。
 *
 * <ol>
 * <li>システムプロパティ：jp.monocrea.sitoolkit.config.fileで指定されるパスのプロパティファイル</li>
 * <li>上記のシステムプロパティが指定されていない場合は、
 * 		VMの実行ディレクトリからの相対パス：conf/sitoolkit.xmlのプロパティファイル</li>
 * </ol>
 *
 * @author Yuichi Kuwahara
 *
 */
public class PropertyManager {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final String LIBSRC = "outdir.libsrc";
	private static final String WEBPAGE = "outdir.webpage";
	private static final String WEBSRC = "outdir.websrc";
	private static final String WEBCMP = "outdir.webcmp";
	private static final Logger LOG = LoggerFactory.getLogger(PropertyManager.class);

	private Properties prop = new Properties();

	public Properties getProp() {
		return prop;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}

	public String getProperty(String key) {
		return getProp().getProperty(key);
	}

	public String getProperty(String key, String defaultValue) {
		return getProp().getProperty(key, defaultValue);
	}

	/**
	 * keyで指定されるプロパティのパスから、dirsで指定される階層を辿ったパスを取得します。
	 * @param key 基準となるパスを持つプロパティのキー
	 * @param dirs　ディレクトリ階層
	 * @return パス文字列
	 */
	public String getPath(String key, Object...dirs) {
		String basePath = getSysProp(key);
		return basePath + "/" + StringUtils.join(dirs, "/");
	}

	/**
	 * DB項目定義書のファイルパスを取得します。
	 * @return
	 */
	public String getDbDef() {
		return getSysProp("docdir.dbdef") + "/" + getSysProp("dbdef.file");
	}

	/**
	 * 画面仕様書のファイルパスを取得します。
	 * @param pageName 画面名
	 * @return 画面仕様書のファイルパス
	 */
	public String getPageSpec(String pageName) {
		return getPath("docdir.pagespec", fmt("pagespec.file", pageName));
	}

	/**
	 * 書式文字列に変数を設定した文字列を返します。
	 * 書式文字列はプロパティファイルに定義します。
	 *
	 * @param key プロパティのキー
	 * @param args 書式に設定する変数
	 * @return 書式設定された文字列
	 */
	public String fmt(String key, Object...args) {
		try {
			String value = getProperty(key);

			if(value == null) {
				LOG.warn("キー「{}」のプロパティは未定義です。", key);
				return "";
			}

			return String.format(value, args);
		} catch (MissingFormatArgumentException e) {
			LOG.warn("プロパティ「{}」の書式設定が失敗しました。", key);
			return "";
		}
	}

    /**
     * 書式定義書のパスを取得します。
     * @return 書式定義書のパス
     */
	public String getFormatDefPath() {
		return getSysProp("docdir.pagespec") + "/" + getSysProp("ioformat.file");
	}

	public String getPageCatalog() {
		return getPageSpecDir() + "/" + getSysProp("pagelist.file");
	}

	public String getPageSpecTemplate() {
		return getPageSpecDir() + "/" + getSysProp("pagespec.template");
	}

	/**
	 * 画面仕様書が配置されているフォルダのパスを取得します。
	 *
	 * @return 画面仕様書配置フォルダのパス
	 */
	public String getPageSpecDir() {
		return getPath("docdir.pagespec");
	}

	/**
	 * Javaソースファイルの出力先ルートを取得します。
	 * @return
	 */
	public String getLibSrcDir() {
		return getSysProp(LIBSRC, "out");
	}

	/**
	 * Javaソースファイルの出力先ルートを取得します。
	 * @return
	 */
	public String getWebSrcDir() {
		return getSysProp(WEBSRC, "out");
	}

	/**
	 * Webコンポーネントファイルの出力ディレクトリを取得します。
	 * @return Webコンポーネントファイルの出力ディレクトリ
	 */
	public String getWebCmpDir() {
		return getSysProp(WEBCMP, "out");
	}

	/**
	 * @return 要素の区切り文字
	 */
	public String getElementSplit() {
		String elementSplit = getProperty("elementSplit");
		return elementSplit == null ? "\n" : StringEscapeUtils.unescapeJava(elementSplit);
	}

	/**
	 * @return キーと値の区切り文字
	 */
	public String getKeyValueSplit() {
		return getSysProp("keyValueSplit", "：");
	}

	public String getWebPageDir() {
		return getSysProp(WEBPAGE, "out");
	}

	/**
	 * コード仕様書のファイルパスを取得します。
	 * @return
	 */
	public String getCodeSpec() {
		return getPath("docdir.dbdef", getSysProp("codespec.file"));
	}

	public String getMockupOutputDir() {
		return getSysProp("outdir.mockup");
	}

	/**
	 * プロパティを取得します。
	 * プロパティの取得先の優先度は以下の通りです。
	 * <ol>
	 * <li>システムプロパティ
	 * <li>SIToolkitプロパティファイル
	 * </ol>
	 * プロパティが何れにも設定されていない場合は空文字を返却します。
	 * @param key プロパティ名
	 * @return プロパティ
	 */
	public String getSysProp(String key) {
		return  getSysProp(key, "");
	}

	/**
	 * プロパティを取得します。
	 * プロパティの取得先の優先度は以下の通りです。
	 * <ol>
	 * <li>システムプロパティ
	 * <li>SIToolkitプロパティファイル
	 * <li>初期値
	 * </ol>
	 * @param key プロパティ名
	 * @param def プロパティ名に対応するプロパティが無い場合に返される初期値
	 * @return プロパティ
	 */
	public String getSysProp(String key, String def) {
		return  System.getProperty(key, getProperty(key, def));
	}

	/**
	 * @return アプリケーションルートパッケージ
	 */
	public String getAppRootPkg() {
		return getSysProp("package", "jp.monocrea.sitoolkit.archetype");
	}

	/**
	 * 再構築モードである場合にtrueを返します。
	 * キー：rebuild
	 * @return 再構築モードである場合にtrue
	 */
	public boolean isRebuild() {
		return "true".equalsIgnoreCase(getSysProp("rebuild", "false"));
	}

	/**
	 * 繰り返し生成モードである場合にtrueを返します。
	 * @return 繰り返し生成モードである場合にtrue
	 */
	public boolean isCGMode() {
		return "true".equalsIgnoreCase(System.getProperty("sit.cg"));
	}
}
