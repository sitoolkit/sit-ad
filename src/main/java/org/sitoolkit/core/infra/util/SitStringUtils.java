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

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * このクラスは、Sitoolkit特有の文字列の操作を行うユーティリティです。
 * @author Yuichi Kuwahara
 *
 */
public class SitStringUtils {

	private static final char DELIMITER = '_';

	private static final String DELIMETER_STR = Character.toString(DELIMITER);

	private static final Pattern STARTS_WITH_UPPERCASE = Pattern.compile("^[A-Z]");

	/**
	 * 文字列をPascal形式(PascalStyle)に変換します。
	 * 
	 * @param str 文字列
	 * @return Pascal形式
	 */
	public static String toPascal(String str) {
		return toCamelOrPascal(str, true, DELIMITER);
	}
	
	/**
	 * 文字列をPascal形式(PascalStyle)またはCamel形式(camelStyle)に変換します。
	 * 
	 * @param str 文字列
	 * @param isPascal true:Camel形式、false:Pascal形式
	 * @param delimiter 区切り文字
	 * @return Pascal形式またはCamel形式
	 */
	public static String toCamelOrPascal(String str, boolean isPascal, char delimiter) {
		if(StringUtils.isEmpty(str)) {
			return "";
		}
		if(!str.contains(DELIMETER_STR)) {
			if(isPascal) {
				return StringUtils.capitalize(str);
			} else {
				return StringUtils.uncapitalize(str);
			}
		}
		
		StringBuilder sb = new StringBuilder();
		boolean toUpperCase = isPascal;
		for (char c : str.toCharArray()) {
			if(c == delimiter) {
				toUpperCase = true;
			} else {
				if (toUpperCase) {
					sb.append(Character.toUpperCase(c));
				} else {
					sb.append(Character.toLowerCase(c));
				}
				toUpperCase = false;
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * 文字列をCamel形式(camelStyle)に変換します。
	 * 
	 * @param str 文字列
	 * @return Camel形式
	 */
	public static String toCamel(String str) {
		return toCamelOrPascal(str, false, DELIMITER);
	}

	/**
	 * DBのテーブル物理名をJavaのエンティティ名に変換します。
	 * @param tableName
	 * @return
	 */
	public static String table2entity(String tableName) {
		return toPascal(table2id(tableName));
	}
	
	public static String table2camel(String tableName) {
		return toCamel(table2id(tableName));
	}
	
	/**
	 * テーブル名の識別子を取得します。
	 * 
	 * <pre>{@code 
	 * t_table -> table
	 * m_table -> table
	 * table -> table
	 * }</pre>
	 * 
	 * @param tableName テーブル名
	 * @return 
	 */
	public static String table2id(String tableName) {
		if(StringUtils.isEmpty(tableName)) {
			return "";
		}
		if(StringUtils.startsWithAny(tableName, "m_", "t_")) {
			return tableName.substring(2);
		}
		return tableName;
	}
	
	public static List<String> decodeList(String str) {
		return str == null ? new ArrayList<String>() :
			Arrays.asList(str.replaceAll(" |\\[|\\]", "").split(","));
	}
	
	public static String[] splitLine(String lineStr) {
		return StringUtils.isEmpty(lineStr) ? new String[0] : lineStr.split("[\n|\r\n]");
	}

	/**
	 * 文字列をMapにデコードします。
	 * 
	 * 
	 * @param str デコードする対象の文字列
	 * @param split1 要素の区切り文字
	 * @param split2 キーバリューの区切り文字
	 * @return 
	 */
	public static Map<String, String> decodeToMap(String str, String split1, String split2) {
		Map<String, String> map = new HashMap<String, String>();
		
		if(StringUtils.isEmpty(str) || StringUtils.isEmpty(split1) || StringUtils.isEmpty(split2)) {
			return map;
		}
		
		for(String entry : str.split(split1)) {
			String[] pair = entry.split(split2);
			if(pair.length > 1) {
				map.put(pair[0], pair[1]);
			}
		}
		
		return map;
	}

	/**
	 * 文字列からJava識別子として使用できない文字を除いたものを返します。
	 * @param str 文字列
	 * @return Java識別子として使用できない文字を除いた文字列
	 */
	public static String escapeForJavaIdentifer(String str) {
		if (StringUtils.isEmpty(str)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		char[] charArr = str.toCharArray();
		
		if (Character.isJavaIdentifierStart(charArr[0])) {
			sb.append(charArr[0]);
		}
		
		for (int i = 1; i < charArr.length; i++) {
			if (Character.isJavaIdentifierPart(charArr[i])) {
				sb.append(charArr[i]);
			}
		}
		return sb.toString();
	}
	
	/**
	 * オブジェクトがnullまたは空文字であるかを判定します。
	 * @param obj オブジェクト
	 * @return true:オブジェクトがnullまたは空文字である
	 */
	public static boolean isEmpty(Object obj) {
		return obj == null ? true : obj.toString().isEmpty();
	}

	/**
	 * 文字列から改行コード(\r、\n)と全角スペース、半角スペースを除いたものを返します。
	 * @param str 文字列
	 * @return クレンジング後文字列
	 */
	public static String cleansing(String str) {
		return StringUtils.isEmpty(str) ? "" : str.replaceAll("[\\r|\\n| |　]", "");
	}
	

	/**
	 * URLをファイルパスに変換します。
	 * 
	 * <pre>
	 * 例)
	 * 
	 * http://localhost:9090/a/b/c.html
	 * → /a/b/c.html
	 * 
	 * file:/C:/a/b/c.txt
	 * → /C:/a/b/c.txt
	 * 
	 * file:/C:/a/b/c.jar!/e/f/g.txt
	 * → /e/f/g.txt
	 * </pre>
	 * 
	 * @param url URL
	 * @return ファイルパス
	 */
	public static String url2filepath(URL url) {
		String filePath = url.getFile();
		if (filePath.contains("!")) {
			filePath = StringUtils.substringAfter(filePath, "!");
		}
		try {
			return URLDecoder.decode(filePath, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new SitException(e);
		}
	}

	/**
	 * 文字列が英字大文字で始まる場合にtrueを返します。
	 * @param str 文字列
	 * @return 文字列が英字大文字で始まる場合にtrue
	 */
	public static boolean startsWithUpperCase(String str) {
		return STARTS_WITH_UPPERCASE.matcher(str).matches();
	}

	public static String escapeReturn(Object obj) {
		if (obj == null) {
			return "";
		}
		return obj.toString().replaceAll("\\r\\n|\\n|\\r", "\\\\n");
	}
}
