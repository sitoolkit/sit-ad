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
import org.sitoolkit.core.app.MockupGenerator;
import org.sitoolkit.core.app.PageSpecGenerator;
import java.io.File;
import java.io.IOException;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * このクラスは、{@code MockupGenerator}の受け入れテストクラスです。
 * @author Yuichi Kuwahara
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:" + SourceCodeGenerator.APP_CTX_CONFIG_LOCATION)
public class MockupGeneratorTest {

	@Resource
	PageSpecGenerator pageSpecGen;

	@Autowired
	ApplicationContext appCtx;
	
	/**
	 * <dl>
	 * <dt>コンディション
	 * <dd>サンプルの画面一覧.xlsx、画面項目定義書_モックアップデモ.xlsxからモックアップのソースコードを生成する。
	 * <dt>予想結果
	 * <dd>生成されたソースコードの文字列が、
	 * 		予め用意した「動作確認の取れているソースコード」の文字列と一致する。
	 * </dl>
	 */
	@Test
	public void mockupSrcCdStrFromDocEqualsPreparedStr() throws IOException {
		File doc = new File("doc/画面仕様書_ユーザー一覧.xlsx");
		if (!doc.exists()) {
			pageSpecGen.execute(new String[0]);
		}

		MockupGenerator mockupGen = appCtx.getBean(MockupGenerator.class);
		String[] fileNames = new String[]{"モックアップデモ.html"};
		String outDir = "out/mockup/デモ";
		GeneratorTestHelper.execAndAssert(mockupGen, outDir , fileNames, "モックアップデモ");
	}

}
