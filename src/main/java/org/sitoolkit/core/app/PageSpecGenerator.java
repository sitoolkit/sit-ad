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

import java.util.List;
import org.sitoolkit.core.domain.data.DBDefCatalog;
import org.sitoolkit.core.domain.data.TableDef;
import org.sitoolkit.core.domain.view.PageDef;
import org.sitoolkit.core.domain.view.PageDefCatalog;
import org.sitoolkit.core.domain.view.pagecnv.PageDefConverter;

/**
 *
 * @author yuichi.kuwahara
 */
public class PageSpecGenerator extends SourceCodeGenerator {

	private PageDefCatalog pageDefCatalog;

	private DBDefCatalog dbDefCatalog;

	private List<PageDefConverter> converterList;

	@Override
	public void generate(String... tableNames) {

		for (TableDef table : getDbDefCatalog().getAll()) {
			if (table.isRelation()) {
				log.info("テーブル[{}]は関連テーブルのため変換対象から除外します。", table.getName());
				continue;
			}
			for (PageDefConverter converter : getConverterList()) {
				log.info("テーブル[{}]を画面に変換します。", table.getName());
				PageDef page = converter.convert(table);
				log.info("テーブル[{}]を画面[{}](項目数[{}])に変換しました。",
						new Object[]{table.getName(), page.getName(), page.getItemCount()});
				getPageDefCatalog().add(page);
			}
		}
		getPageDefCatalog().write();
	}

	public PageDefCatalog getPageDefCatalog() {
		return pageDefCatalog;
	}

	public void setPageDefCatalog(PageDefCatalog pageDefCatalog) {
		this.pageDefCatalog = pageDefCatalog;
	}

	public static void main(String args[]) {
		PageSpecGenerator generator = appCtx().getBean(PageSpecGenerator.class);
		System.exit(generator.execute(args));
	}

	public List<PageDefConverter> getConverterList() {
		return converterList;
	}

	public void setConverterList(List<PageDefConverter> converterList) {
		this.converterList = converterList;
	}

	public DBDefCatalog getDbDefCatalog() {
		return dbDefCatalog;
	}

	public void setDbDefCatalog(DBDefCatalog dbDefCatalog) {
		this.dbDefCatalog = dbDefCatalog;
	}
}
