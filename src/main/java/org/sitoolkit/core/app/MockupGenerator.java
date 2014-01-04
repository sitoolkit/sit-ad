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
import javax.annotation.Resource;
import org.sitoolkit.core.infra.util.PropertyManager;
import org.apache.commons.io.FileUtils;

/**
 * このクラスは、モックアップを出力する{@link SourceCodeGenerator}の拡張です。
 *
 * @author Yuichi Kuwahara
 * @since 1.0
 * @version 1.0
 */
public class MockupGenerator extends SourceCodeGenerator {

	/**
	 *
	 */
	@Resource
	PropertyManager pm;

	@Override
	public void generate(String... names) {
		String outFolderPath = pm.getMockupResOutputDir();

        setupResource(outFolderPath);

		super.generate(names);
	}

	private void setupResource(String outFolderPath) {
        File outDir = new File(outFolderPath);

        File resDir = new File(pm.getProperty("resdir.mockup"));
        try {
			log.info("モックアップ用資源を出力フォルダにコピーします。資源フォルダ：{}、出力フォルダ:{}",
					resDir.getAbsolutePath(), outDir.getAbsolutePath());
            FileUtils.copyDirectory(resDir, outDir);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }

	/**
	 *
	 * @param args コマンドライン引数
	 */
	public static void main(final String[] args) {
		MockupGenerator generator = appCtx().getBean(MockupGenerator.class);
		System.exit(generator.execute(args));
	}
}
