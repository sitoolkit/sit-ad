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
package org.sitoolkit.core.infra.repository.excel;

import java.io.File;
import org.sitoolkit.core.infra.repository.TableData;
import org.sitoolkit.core.infra.repository.TableDataCatalog;
import org.sitoolkit.core.infra.util.SitFileUtils;
import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.sitoolkit.core.infra.repository.FileInputSourceWatcher;
import org.sitoolkit.core.infra.util.FileOverwriteChecker;

/**
 *
 * @author yuichi.kuwahara
 */
public class ExcelRepositoryTest {

	ExcelRepository repo = new ExcelRepository();

	@Before
	public void setUp() {
		repo.watcher = mock(FileInputSourceWatcher.class);
		repo.fileOverwriteChecker = mock(FileOverwriteChecker.class);
		when(repo.fileOverwriteChecker.isWritable(any(File.class))).thenReturn(true);
	}

	/**
     * <dl>
     * <dt>ケース
     * <dd>xlsx形式のファイルの読み込み
     * </dl>
	 */
	@Test
	public void testReadXlsx() {
		TableDataCatalog result = repo.readAll(
				SitFileUtils.res2filepath(ExcelRepositoryTest.class, "ExcelRepositoryReadTestData.xlsx"));

		assertEquals(1, result.size());

		TableData sheet1 = result.get("Sheet1");
		assertEquals("1,a,200,a200\r\n2,b,300,b300\r\n3,c,400,c400", sheet1.toString());
	}

	/**
     * <dl>
     * <dt>ケース
     * <dd>xls形式のファイルの読み込み
     * </dl>
	 */
	@Test
	public void testReadXls() {
		TableDataCatalog result = repo.readAll(
				SitFileUtils.res2filepath(ExcelRepositoryTest.class, "ExcelRepositoryReadTestData.xls"));

		assertEquals(1, result.size());

		TableData sheet1 = result.get("Sheet1");
		assertEquals("1,a,200\r\n2,b,300\r\n3,c,400", sheet1.toString());
	}

	/**
     * <dl>
     * <dt>ケース
     * <dd>conerCellPatternの初期値の確認
     * </dl>
	 */
	@Test
	public void testGetCornerCellPattern() {
		assertTrue("項番".matches(repo.getCornerCellPattern()));
		assertTrue("No.".matches(repo.getCornerCellPattern()));
		assertTrue("#".matches(repo.getCornerCellPattern()));
		assertFalse("No".matches(repo.getCornerCellPattern()));
	}

	/**
     * <dl>
     * <dt>ケース
     * <dd>xlsx形式のファイルの書き込み
     * </dl>
	 */
	@Test
	public void testWriteXlsx() {
		TableDataCatalog expectedCatalog = repo.readAll(
				SitFileUtils.res2filepath(ExcelRepositoryTest.class, "ExcelRepositoryReadTestData.xlsx"));

		File actualFile = new File("target/test-classes/ExcelRepositoryWirteTestResult.xlsx");
		repo.write(
				SitFileUtils.res2filepath(ExcelRepositoryTest.class, "ExcelRepositoryWriteTestTemplate.xlsx"),
				actualFile, expectedCatalog);
		TableDataCatalog actualCatalog = repo.readAll(actualFile.getAbsolutePath());

		assertEquals(expectedCatalog, actualCatalog);

        actualFile.deleteOnExit();
	}
}
