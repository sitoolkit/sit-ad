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

/**
 *
 * @author kuwahara
 */
public class CharUtils {

	public static void main(String[] args) throws UnsupportedEncodingException {
		for(byte i = 2; i < 7; i++) {
			for(byte j = 0; j < 16; j++) {
				String hex = Integer.toHexString(i) + Integer.toHexString(j);
				System.out.println(i + "," + j + " " + hex + " " + hex2str(hex));
			}
		}
		
//		System.out.println("a".getBytes(getDefaultCharsetName()).length);
	}

	/**
	 * 処理対象文字列を、指定文字コードセットでのバイト配列の16進数表記に変換します。
	 * 
	 * @param str 処理対象文字列
	 * @param charsetName 文字コードセット
	 * @param withPrefix 返却する文字列の先頭に"0x"を加えるか否か
	 * @return 処理対象文字列の16進数表記
	 * @throws UnhandledException 指定の文字コードセットが不正な場合に送出します。
	 */
	public static String str2hex(String str, String charsetName, boolean withPrefix) throws SitException {
		StringBuilder sb = new StringBuilder();
		if(withPrefix) {
			sb.append("0x");
		}
		try {
			for(byte b : str.getBytes(charsetName)) {
				sb.append(String.format("%02X", b));
			}
		} catch (UnsupportedEncodingException e) {
			throw new SitException(e);
		}
		return sb.toString();
	}
	
	public static String str2hex(String str) {
		return str2hex(str, getDefaultCharsetName(), true);
	}
	
	/**
	 * 
	 * @param str
	 * @return 
	 * @see Integer#parseInt(java.lang.String, int) 
	 */
	public static byte[] hex2bytes(String str) throws NumberFormatException {
		if(str == null) {
			return new byte[0];
		}
		final int loop = str.length() / 2;
		byte[] bytes = new byte[loop];
		for(int i = 0; i < loop; i++) {
			String s = str.substring(i * 2, (i + 1) * 2);
			bytes[i] = (byte) Integer.parseInt(s, 16);
		}
		return bytes;
	}
	
	public static String hex2str(String hex) {
		try {
			return new String(hex2bytes(hex), getDefaultCharsetName());
		} catch (UnsupportedEncodingException e) {
			throw new SitException(e);
		}
	}

	public static String decs2hex(byte... decs) {
		StringBuilder sb = new StringBuilder();
		for(int dec : decs) {
			sb.append(String.format("%02X", dec));
		}
		return sb.toString();
	}
	
	private static String getDefaultCharsetName() {
		return "UTF-8";
	}
	
	
}
