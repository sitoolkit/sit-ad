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
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author yuichi.kuwahara
 */
public class TemplateExtractorTest {
	
	/**
	 * <dl>
	 * <dt>テスト対象
	 * <dd>{@link TemplateExtractor#extract(java.lang.String)}
	 * 
	 * <dt>テストケース
	 * <dd>ディレクトリにテンプレートファイルが配置されている場合
	 * 
	 * <dt>予想結果
	 * <dd>
	 * <ul>
	 * <li>テンプレートファイルが出力ディレクトリに存在する。
	 * <li>テンプレートファイルの出力ディレクトリ内でのパスが、
	 *		テンプレートライブラリ内のディレクトリに一致する。
	 * <li>リストファイルが出力ディレクトリに存在しない。
	 * </ul>
	 * </dl>
	 */
	@Test
	public void testExtractDir() throws IOException {
		TemplateExtractor instance = new TemplateExtractor();
		File testOutDir = new File("templatetest");
		testOutDir.mkdir();
		List<File> files = instance.extract("templatetest=/templatelib");

		FileUtils.forceDeleteOnExit(testOutDir);

		assertEquals(2, files.size());
		assertEquals(
				new File(testOutDir, "TemplateFile1.txt").getAbsolutePath(),
				files.get(0).getAbsolutePath());
		assertEquals(
				new File(testOutDir, "templatedir/TemplateFile2.txt").getAbsolutePath(),
				files.get(1).getAbsolutePath());
		assertFalse(new File(testOutDir, "list.txt").exists());
	}

		/**
	 * <dl>
	 * <dt>テスト対象
	 * <dd>{@link TemplateExtractor#extract(java.lang.String)}
	 * 
	 * <dt>テストケース
	 * <dd>ディレクトリにテンプレートファイルが配置されている場合
	 * 
	 * <dt>予想結果
	 * <dd>
	 * <ul>
	 * <li>テンプレートファイルが出力ディレクトリに存在する。
	 * <li>テンプレートファイルの出力ディレクトリ内でのパスが、
	 *		テンプレートライブラリ内のディレクトリに一致する。
	 * <li>リストファイルが出力ディレクトリに存在しない。
	 * </ul>
	 * </dl>
	 */
	@Test
	public void testExtractJar() throws IOException {
		TemplateExtractor instance = new TemplateExtractor();
		File testOutDir = new File("templatejartest");
		testOutDir.mkdir();
		List<File> files = instance.extract("templatejartest=/org/springframework/context");

		FileUtils.forceDeleteOnExit(testOutDir);

		assertEquals(2, files.size());
		assertEquals(
				new File(testOutDir, "ApplicationContext.class").getAbsolutePath(),
				files.get(0).getAbsolutePath());
		assertEquals(
				new File(testOutDir, "config/spring-context-3.1.xsd").getAbsolutePath(),
				files.get(1).getAbsolutePath());
		assertFalse(new File(testOutDir, "list.txt").exists());
	}

}
