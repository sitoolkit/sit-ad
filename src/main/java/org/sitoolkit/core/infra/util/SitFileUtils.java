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

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ファイル操作を提供するユーティリティクラス。
 * @author Yuichi Kuwahara
 *
 */
public class SitFileUtils {

	private static final Logger LOG = LoggerFactory.getLogger(SitFileUtils.class);

	/**
	 *
	 * @param filePath
	 * @return
	 */
	public static String read(String filePath) {
		try {
			return FileUtils.readFileToString(new File(filePath), getEncoding());
		} catch (IOException e) {
			throw new SitException(e);
		}
	}

	/**
	 * テキストファイルを読み込み、各行を要素とするString配列として取得します。
	 * ファイルが見つからないなど、読み込み時に例外が発生した場合は長さ0の配列が返却されます。
	 *
	 * @param folderPath テキストファイルが配置されたフォルダのパス
	 * @param fileName テキストファイルのファイル名
	 * @return 各行を要素とする配列
	 */
	public static String[] readLines(String folderPath, String fileName) {
		File file = new File(folderPath, fileName);

		if(!file.exists()) {
			return new String[0];
		}

		LOG.info("テキストファイルを読み込みます。{}", file.getAbsolutePath());

		try {
			List<String> files = FileUtils.readLines(file, getEncoding());
			return files.toArray(new String[files.size()]);
		} catch (IOException e) {
			LOG.warn("テキストファイルの読み込みで例外が発生しました。{}", e);
			return new String[0];
		}
	}

	public static String res2filepath(Class<?> type, String resource) {
		return res2file(type, resource).getAbsolutePath();
	}

	/**
	 * リソースをファイルオブジェクトとして取得します。
	 * @param resource リソース名
	 * @return リソースのファイルオブジェクト
	 * @see #res2file(java.lang.Class, java.lang.String)
	 */
	public static File res2file(String resource) {
		return res2file(SitFileUtils.class, resource);
	}

	/**
	 * リソースをファイルオブジェクトとして取得します。
	 * <ul>
	 * <li>リソースがjarなどアーカイブファイル中にある場合、
	 *		ファイルオブジェクトはアーカイブファイル外にコピーした実ファイルとして返します。
	 * <li>リソースが実ファイルである場合、
	 *		ファイルオブジェクトはその実ファイルを指します。
	 * </ul>
	 * @param type リソースの場所を特定するためのクラス
	 * @param resource リソース名
	 * @return リソースのファイルオブジェクト
	 */
	public static File res2file(Class<?> type, String resource) {
		URL url = res2url(type, resource);
		File realFile = FileUtils.toFile(url);
		// FileUtils.toFile()はURLのプロトコルがfileでない場合(アーカイブファイルなど)は、
		// nullを返す仕様である。
		if (realFile != null) {
			return realFile;
		}
		File currentDir = new File(".").getAbsoluteFile().getParentFile();
		realFile = new File(currentDir, SitStringUtils.url2filepath(url));

		try {
			FileUtils.copyURLToFile(url, realFile);
		} catch (IOException e) {
			throw new SitException(e);
		}
		return realFile;
	}

	public static URL res2url(Class<?> type, String resourceName) {
		return res2url(type, "リソース", resourceName);
	}

	public static URL res2url(Class<?> type, String resourceType, String resourceName) {
		URL resUrl = type.getResource(resourceName);
		if (resUrl == null) {
			throw new SitException("指定された" + resourceType + "「" + resourceName + "」は見つかりません。");
		}
		try {
			LOG.info(resourceType + "を読み込みます。{}", URLDecoder.decode(resUrl.toString(), getEncoding()));
			return resUrl;
		} catch (UnsupportedEncodingException e) {
			throw new SitException(e);
		}
	}

	/**
	 * リソースの内容を1行ずつ格納したリストとして返します。
	 * @param type リソースを取得する基準となるクラス
	 * @param resName リソース名
	 * @return
	 * @see FileUtils#readLines(java.io.File, java.lang.String)
	 */
	public static List<String> res2lines(Class<?> type, String resName) {
		try {
			return FileUtils.readLines(res2file(type, resName), getEncoding());
		} catch (IOException e) {
			throw new SitException(e);
		}
	}
	/**
	 * リソースをUTF-8の文字列として読み込んだものを返します。
	 * @param type リソースはこのオブジェクトのクラスのリソースとして探します。
	 * @param resName リソースの位置を表す文字列
	 * @return リソースを読み込んだ文字列
	 */
	public static String res2str(Class<?> type, String resName) {
		try {
			return FileUtils.readFileToString(res2file(type, resName), getEncoding());
		} catch (IOException e) {
			throw new SitException(e);
		}
	}

	public static Properties resource2prop(Class<?> type, String resource) {
		Properties prop = new Properties();
		URL url = type.getResource(resource);

		if (url == null) {
			throw new SitException("型:" + type.getClass() + "に対するリソース:" + resource
					+ "が見つかりません。");
		}
		try {
			prop.load(url.openStream());
		} catch (IOException e) {
			throw new SitException(e);
		}

		return prop;
	}

	/**
	 *
	 * @param filePath
	 * @param data
	 */
	public static void write(final String filePath, final Object data) {

		File file = new File(filePath);
		File dir = file.getParentFile();
		if (!dir.exists()) {
			if (dir.mkdirs()) {
				LOG.info("ディレクトリを作成しました。{}", file.getParentFile().getAbsolutePath());
			} else {
				throw new SitException("ディレクトリの作成に失敗しました。" + dir.getAbsolutePath());
			}
		}

		try {
			FileUtils.writeStringToFile(file, data.toString(), getEncoding());

			LOG.info("ファイルに書き込みました。{}", file.getAbsolutePath());
		} catch (IOException e) {
			throw new SitException(e);
		}
	}

	public static void write(TextFile file) {
		write(file.getAbsolutePath(), file.getText());
	}

	private static String getEncoding() {
		return "UTF-8";
	}

}
