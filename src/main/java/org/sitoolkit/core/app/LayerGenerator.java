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

import javax.annotation.Resource;
import org.sitoolkit.core.domain.data.DBDefCatalog;
import org.sitoolkit.core.domain.data.TableDef;
import org.sitoolkit.core.domain.java.EntityDef;
import org.sitoolkit.core.infra.srccd.SourceCode;
import org.sitoolkit.core.infra.util.SitFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yuichi.kuwahara
 */
public class LayerGenerator extends SourceCodeGenerator {

	private static final Logger LOG = LoggerFactory.getLogger(LayerGenerator.class);

	@Resource
	DBDefCatalog dbDefCatalog;
	
	@Override
	public void generate(String... tableNames) {
		for (TableDef table : dbDefCatalog.getAll()) {
			LOG.info("テーブル：{}に対応する{}を生成します。", table.getName(), getName());

			EntityDef entity = appCtx().getBean("entityDef", EntityDef.class);
			entity.load(table);
			for (SourceCode sourceCode : entity.create()) {
				SitFileUtils.write(sourceCode.toFile());
			}
		}
	}
	
}
