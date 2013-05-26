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
package org.sitoolkit.core.app;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.sitoolkit.core.infra.util.SitFileUtils;
import org.sitoolkit.core.infra.util.SitStringUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * このクラスは、テンプレートファイルを展開するためのクラスです。
 * テンプレートライブラリ内からテンプレートファイルを取り出し、
 * 指定のディレクトリに配置します。
 * 
 * <dl>
 * <dt>テンプレートリストファイルの場所
 * <dd>${クラスパス}/template/conf/sit_javaee6/list.txt
 *
 * <dt>テンプレートリストファイルの内容
 * <dd>sit-conf.xml
 * <dd>mockup/css/style.css
 *
 * <dt>実行Javaコマンド
 * <dd>java org.sitoolkit.core.app.TemplateExtractor conf=/template/conf/sit_javaee6
 * 
 * <dt>実行後に展開されるファイル
 * <dd>${カレントディレクトリ}/conf/sit-conf.xml
 * <dd>${カレントディレクトリ}/conf/mockup/css/style.css
 *
 * @author yuichi.kuwahara
 * @since 1.0
 */
public class TemplateExtractor {

	private static final Logger LOG = LoggerFactory.getLogger(TemplateExtractor.class);
	
	public static void main(String[] args) {
		TemplateExtractor instance = new TemplateExtractor();
		for (String arg : args) {
			instance.extract(arg);
		}
	}

	/**
	 * 指定されたテンプレートライブラリを展開します。
	 * @param params 展開するテンプレートライブラリを表す式を指定します。
	 * 式は以下の書式に従う必要があります。
	 * <pre>
	 *	[テンプレートライブラリ略称]=[テンプレートライブラリへのリソースパス]
	 * </pre>
	 * @return 展開したテンプレートファイルのリスト
	 */
	public List<File> extract(String params) {
		String[] values = params.split("=");
		String libName = values[0];
		String basePath = values[1];
		String listPath = basePath + "/list.txt";
		return extract(listPath, basePath, libName);
	}

	/**
	 * テンプレートリストで指定されたテンプレートファイルを、
	 * 出力ディレクトリにコピーします。
	 * 
	 * @param listFile テンプレートリストのリソース名
	 * @param libPath テンプレートライブラリのパス
	 * @param dstDir 展開したファイルの出力先ディレクトリ
	 * @return 展開したテンプレートファイルのリスト
	 */
	public List<File> extract(String listFile, String libPath, String dstDir) {
		List<File> files = new ArrayList<File>();
		List<String> templateList = SitFileUtils.res2lines(TemplateExtractor.class, listFile);
		
		LOG.info("テンプレートライブラリ[{}]を[{}]に展開します。{}",
				libPath, dstDir, templateList);
		
		for (String line : templateList) {
			try {
				File file = extractResource(libPath, line, dstDir);
				if (file != null) {
					files.add(file);
				}
			} catch (Exception e) {
				LOG.warn("テンプレートファイルの展開中に例外が発生しました。", e);
			}
		}
		return files;
	}

	/**
	 * リソースを展開先のディレクトリにコピーします。
	 * @param basePath リソースを探す基底のパス
	 * @param resPath リソースのパス　つまりリソースは以下のパスで検索されます。
	 * <pre>
	 * ${クラスパス}/${basePath} + "/" + ${resPath}
	 * </pre>
	 * @param dstDir リソースの展開先ディレクトリのパス
	 * @return 展開したファイル　展開先のファイルが既存の場合はnull
	 * @throws IOException リソースをファイルにコピーする処理で異常
	 */
	File extractResource(String basePath, String resPath, String dstDir) throws IOException {
		String resource = resPath;
		if (!resPath.startsWith("/")) {
			resource = basePath + "/" + resPath;
		}

		URL url = SitFileUtils.res2url(TemplateExtractor.class, resource);
		File resFile = FileUtils.toFile(url);

		File dstFile = new File(dstDir, resPath);
		if (dstFile.exists()) {
			LOG.warn("{}は既に存在します。このテンプレートファイルは展開されません。", dstFile.getAbsolutePath());
			return null;
		} 
		LOG.info("リソースをコピーします。{}", dstFile.getAbsolutePath());
	
		// リソースがjarファイル等アーカイブされている場合
		if (resFile == null) {
			FileUtils.copyURLToFile(url, dstFile);

		// リソースが実ファイルである場合
		} else {
			FileUtils.copyFile(resFile, dstFile);
		}
		return dstFile;
	}

}
