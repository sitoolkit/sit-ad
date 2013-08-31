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

import org.sitoolkit.core.app.SourceCodeGenerator;
import java.io.File;
import java.io.IOException;
import org.sitoolkit.core.infra.util.SitFileUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Yuichi Kuwahara
 *
 */
public class GeneratorTestHelper {

	private static final Logger LOG = LoggerFactory.getLogger(GeneratorTestHelper.class);

	public static void execAndAssert(SourceCodeGenerator gen, String outDir, String[] fileNames) throws IOException {
		execAndAssert(gen, outDir, fileNames, "");
	}

	/**
	 * {@code SourceCodeGenerator}を実行し、その結果出力されるファイルについて検証を行います。
	 *
	 * @param gen
	 *            実行する{@code SourceCodeGenerator}
	 * @param outDir
	 *            出力ディレクトリ
	 * @param fileNames
	 *            ファイル名
	 * @throws IOException
	 * @see {@link #assertEqualsSrc(String, String[])}
	 */
	public static void execAndAssert(SourceCodeGenerator gen, String outDir, String[] fileNames, String includes) throws IOException {
		File outDirObj = new File(outDir);
		LOG.info("テスト前の準備として出力ディレクトリを削除します。{}", outDirObj.getAbsolutePath());
		FileUtils.deleteDirectory(outDirObj);

		gen.setIncludes(includes);

		assertEquals(0, gen.execute(new String[0]));

		assertEqualsSrc(outDir, fileNames);
	}

	/**
	 * ファイル名で指定されるリソースと、出力ディレクトリ以下の同じファイル名の文字列が等しいことを確認します。
	 *
	 * @param outDir
	 *            出力ディレクトリ
	 * @param fileNames
	 *            ファイル名
	 * @throws IOException
	 */
	public static void assertEqualsSrc(String outDir, String[] fileNames) throws IOException {
		int ngCnt = 0;
		for (String fileName : fileNames) {
			String resName = fileName + ".txt";
			String preparedStr = SitFileUtils.res2str(GeneratorTestHelper.class, resName);
			File srcCdFile = new File(outDir, fileName);
			String srcCdStrFromDoc = FileUtils.readFileToString(srcCdFile, "UTF-8");
			if (!StringUtils.equals(preparedStr, srcCdStrFromDoc)) {
				LOG.error("結果が期待と異なります\n\texpecte:{}\n\tactual:{}",
						SitFileUtils.res2filepath(GeneratorTestHelper.class, resName),
						srcCdFile.getAbsolutePath());
				ngCnt++;
			}
		}
		if (ngCnt > 0) {
			fail(ngCnt + "個のファイル比較が失敗しました");
		}
	}

}
