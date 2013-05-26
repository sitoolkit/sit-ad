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

import org.sitoolkit.core.infra.util.SitFileUtils;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import static org.junit.Assert.*;

import org.junit.Test;

public class SitFileUtilsTest {

	/**
	 * <dl>
	 * <dt>テスト対象
	 * <dd>{@link SitFileUtils#res2file(java.lang.Class, java.lang.String) }
	 * 
	 * <dt>ケース
	 * <dd>リソースが実ファイルである場合
	 * 
	 * </dl>
	 */
	@Test
	public void testRes2fileRealFile() {
		String actual = SitFileUtils.res2str(getClass(), "SitFileUtilsテスト用ファイル.txt");
		String expected = "テスト用のファイルです。";
		assertEquals(expected, actual);
	}

	/**
	 * <dl>
	 * <dt>テスト対象
	 * <dd>{@link SitFileUtils#res2file(java.lang.Class, java.lang.String) }
	 * 
	 * <dt>ケース
	 * <dd>リソースがjarファイル内のファイルである場合
	 *		(jarファイル内のパスは最上位)
	 * </dl>
	 */
	@Test
	public void testRes2fileArchivedFile() {
		String path = "overview.html";
		File expected = new File(path);
		File actual = SitFileUtils.res2file("/" + path);
		actual.deleteOnExit();
		
		assertEquals(expected.getAbsolutePath(), actual.getAbsolutePath());
	}
	
	/**
	 * <dl>
	 * <dt>テスト対象
	 * <dd>{@link SitFileUtils#res2file(java.lang.Class, java.lang.String) }
	 * 
	 * <dt>ケース
	 * <dd>リソースがjarファイル内のファイルである場合
	 *		(jarファイル内のパスが最上位ではない)
	 * </dl>
	 */
	@Test
	public void testRes2fileArchivedDeepFile() throws IOException {
		String path = "META-INF/maven/org.apache.commons/commons-lang3/pom.properties";
		File expected = new File(path);
		File actual = SitFileUtils.res2file("/" + path);
		FileUtils.forceDeleteOnExit(actual);
		
		assertEquals(expected.getAbsolutePath(), actual.getAbsolutePath());
	}
}
