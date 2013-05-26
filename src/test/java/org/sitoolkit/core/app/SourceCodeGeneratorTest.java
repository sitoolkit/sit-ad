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
import org.sitoolkit.core.app.PageSpecGenerator;
import java.io.IOException;
import javax.annotation.Resource;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * このクラスは、{@code SourceCodeGenerator}の受け入れテストクラスです。
 * @author Yuichi Kuwahara
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:" + SourceCodeGenerator.APP_CTX_CONFIG_LOCATION)
public class SourceCodeGeneratorTest {

	@Resource
	private SourceCodeGenerator codeGen;
	
	@Resource
	private SourceCodeGenerator ddlGen;

	@Resource
	private PageSpecGenerator pageSpecGen;
	
	@Resource
	ApplicationContext appCtx;
	
	/**
	 * <dl>
	 * <dt>コンディション
	 * <dd>サンプルのコード定義書.xlsxからコード定義クラスのソースコードを生成する。
	 * <dt>予想結果
	 * <dd>生成されたソースコードの文字列が、
	 * 		予め用意した「動作確認の取れているソースコード」の文字列と一致する。
	 * </dl>
	 */
	@Test
	public void codeDefSrcCdStrFromDocEqualsPreparedStr() throws IOException {
		String codeDefOutDir = "out/jp/monocrea/sitoolkit/archetype/domain/code";
		String[] fileNames = new String[]{"性別Vo.java", "権限Vo.java", "セキュリティ質問Vo.java", "興味ジャンルVo.java"};
		GeneratorTestHelper.execAndAssert(codeGen, codeDefOutDir, fileNames);
	}
	
	/**
	 * <dl>
	 * <dt>コンディション
	 * <dd>サンプルのテーブル定義書.xlsxからコード定義クラスのソースコードを生成する。
	 * <dt>予想結果
	 * <li>生成されたソースコードの文字列が、
	 * 		予め用意した「動作確認の取れているソースコード」の文字列と一致する。
	 * </dl>
	 */
	@Test
	public void ddlScriptStrFromDocEqualsPreparedStr() throws IOException {
		String ddlOutDir = "out/db/script";
		String[] fileNames = new String[]{"tbm01_create_m_user.sql", "tbm02_create_m_group.sql", "tbm03_create_m_user_group.sql"};
		GeneratorTestHelper.execAndAssert(ddlGen, ddlOutDir, fileNames);
	}

	/**
	 * <dl>
	 * <dt>コンディション
	 * <dd>サンプルのテーブル定義書.xlsxから画面仕様書_ユーザー一覧.xlsx、ユーザー登録.xlsxを生成する。
	 * <dd>サンプルの画面一覧.xlsx、生成した画面仕様書からビューのソースコードを生成する。
	 * <dt>予想結果
	 * <dd>生成されたソースコードの文字列が、
	 * 		予め用意した「動作確認の取れているソースコード」の文字列と一致する。
	 * </dl>
	 */
	@Test
	public void viewStrFromDocEqualsPreparedStr() throws IOException {
		assertEquals(0, pageSpecGen.execute(new String[0]));
		
		String outDir = "out/management";
		String[] fileNames = new String[]{"userInput.xhtml", "userList.xhtml"};
		
		SourceCodeGenerator viewGen = (SourceCodeGenerator) appCtx.getBean("viewGen");
		GeneratorTestHelper.execAndAssert(viewGen, outDir , fileNames);
	}
}
