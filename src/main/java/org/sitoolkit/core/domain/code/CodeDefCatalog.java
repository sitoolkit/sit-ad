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
package org.sitoolkit.core.domain.code;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.sitoolkit.core.infra.repository.DocumentMapper;
import org.sitoolkit.core.infra.repository.DocumentRepository;
import org.sitoolkit.core.infra.repository.RowData;
import org.sitoolkit.core.infra.repository.TableData;
import org.sitoolkit.core.infra.srccd.SourceCodeCatalog;
import org.sitoolkit.core.infra.util.PropertyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yuichi.kuwahara
 */
public class CodeDefCatalog implements SourceCodeCatalog<CodeDef> {

	private static final Logger LOG = LoggerFactory.getLogger(CodeDefCatalog.class);
	private String codeDefId;
	@Resource
	DocumentRepository repo;
	@Resource
	PropertyManager pm;
	@Resource
	DocumentMapper dm;

	private Map<String, CodeDef> data = new HashMap<String, CodeDef>();

	@Override
	public Collection<CodeDef> getAll() {
		return data.values();
	}

	public void load(TableData tableData) {
		for (RowData rowData : tableData.getRows()) {
			CodeDef codeDef = dm.map(getCodeDefId(), rowData, CodeDef.class);
			add(codeDef);
		}
		LOG.info("{}件のコード定義を読み込みました。", data.size());
	}

	private void add(CodeDef codeDef) {
		data.put(codeDef.getName(), codeDef);
	}

	public CodeDef get(String codeName) {
		return data.get(codeName);
	}

	@PostConstruct
	public void load() {
		load(repo.read(pm.getCodeSpec(), "コード定義"));
	}

	public String getCodeDefId() {
		return codeDefId;
	}

	public void setCodeDefId(String codeDefId) {
		this.codeDefId = codeDefId;
	}

	@Override
	public Collection<CodeDef> reload(String inputSource) {
		// TODO 実装
		return Collections.emptyList();
	}

}
