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

import org.sitoolkit.core.app.LayerGenerator;
import org.sitoolkit.core.app.SourceCodeGenerator;
import java.io.File;
import java.io.IOException;
import javax.annotation.Resource;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * このクラスは、{@code LayerGenerator}の受け入れテストクラスです。
 * @author Yuichi Kuwahara
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:" + SourceCodeGenerator.APP_CTX_CONFIG_LOCATION)
public class LayerGeneratorTest {

	@Resource
	private LayerGenerator layerGen;

	/**
	 * <dl>
	 * <dt>コンディション
	 * <dd>サンプルのテーブル定義書.xlsxからレイヤを構成するJavaクラスのソースコードを生成する。
	 * <dt>予想結果
	 * <dd>生成されたソースコードの文字列が、
	 * 		予め用意した「動作確認の取れているソースコード」の文字列と一致する。
	 * </dl>
	 */
	@Test
	public void layerJavaSrcFromDocEqualsPreparedStr() throws IOException {
		String userOutDir = "out/jp/monocrea/sitoolkit/archetype/domain/user";
		String[] fileNamesInUser = new String[]{
			"UserEntity.java", "UserGroupEntity.java", "UserGroupEntityPK.java",
			"UserService.java", "UserDao.java"};
		GeneratorTestHelper.execAndAssert(layerGen, userOutDir, fileNamesInUser);
		
		String groupOutDir = "out/jp/monocrea/sitoolkit/archetype/domain/group";
		String[] fileNamesInGroup = new String[]{"GroupEntity.java", "GroupService.java",
			"GroupDao.java"};
		GeneratorTestHelper.assertEqualsSrc(groupOutDir, fileNamesInGroup);

		String modelOutDir = "out/jp/monocrea/sitoolkit/archetype/web/model";
		String[] fileNamesInModel = new String[]{
			"UserInputModel.java", "UserListModel.java",
			"GroupInputModel.java", "GroupListModel.java"
		};

		GeneratorTestHelper.assertEqualsSrc(modelOutDir, fileNamesInModel);
	}

}
