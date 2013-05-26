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

import org.sitoolkit.core.infra.repository.excel.ExcelRepository;
import java.io.File;
import java.io.IOException;
import org.sitoolkit.core.infra.repository.TableData;
import org.sitoolkit.core.infra.repository.TableDataCatalog;
import org.sitoolkit.core.infra.util.SitFileUtils;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author yuichi.kuwahara
 */
public class ExcelRepositoryTest {

	/**
	 * xlsxファイルを読み取ることができることを確認
	 */
	@Test
	public void testRead_xlsx() throws Exception {
		ExcelRepository instance = new ExcelRepository();
		TableDataCatalog result = instance.readAll(
				SitFileUtils.res2filepath(ExcelRepositoryTest.class, "ExcelRepositoryReadTestData.xlsx"));
		
		assertEquals(1, result.size());

		TableData sheet1 = result.get("Sheet1");
		assertEquals("1,a,200\r\n2,b,300\r\n3,c,400", sheet1.toString());
	}

	/**
	 * xlsファイルを読み取ることができることを確認
	 */
	@Test
	public void testRead_xls() throws Exception {
		ExcelRepository instance = new ExcelRepository();
		TableDataCatalog result = instance.readAll(
				SitFileUtils.res2filepath(ExcelRepositoryTest.class, "ExcelRepositoryReadTestData.xls"));
		
		assertEquals(1, result.size());

		TableData sheet1 = result.get("Sheet1");
		assertEquals("1,a,200\r\n2,b,300\r\n3,c,400", sheet1.toString());
	}

	@Test
	public void testGetCornerCellPattern() {
		ExcelRepository instance = new ExcelRepository();
		assertTrue("項番".matches(instance.getCornerCellPattern()));
		assertTrue("No.".matches(instance.getCornerCellPattern()));
		assertTrue("#".matches(instance.getCornerCellPattern()));
		assertFalse("No".matches(instance.getCornerCellPattern()));
	}
	
	@Test
	public void testWrite_xlsx() throws IOException {
		ExcelRepository instance = new ExcelRepository();
		TableDataCatalog expectedCatalog = instance.readAll(
				SitFileUtils.res2filepath(ExcelRepositoryTest.class, "ExcelRepositoryReadTestData.xlsx"));

		File actualFile = new File("target/test-classes/ExcelRepositoryWirteTestResult.xlsx");
		actualFile.deleteOnExit();
		instance.write(
				SitFileUtils.res2filepath(ExcelRepositoryTest.class, "ExcelRepositoryWriteTestTemplate.xlsx"),
				actualFile, expectedCatalog);
		TableDataCatalog actualCatalog= instance.readAll(actualFile.getAbsolutePath());

		assertEquals(expectedCatalog, actualCatalog);
	}	
}
