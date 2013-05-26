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
package org.sitoolkit.core.domain.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.sitoolkit.core.domain.code.CodeDefCatalog;
import org.sitoolkit.core.domain.view.nav.NodeDef;
import org.sitoolkit.core.domain.view.nav.TreeDef;
import org.sitoolkit.core.infra.repository.DocumentRepository;
import org.sitoolkit.core.infra.repository.TableData;
import org.sitoolkit.core.infra.repository.TableDataCatalog;
import org.sitoolkit.core.infra.srccd.SourceCode;
import org.sitoolkit.core.infra.srccd.SourceCodeCatalog;
import org.sitoolkit.core.infra.util.PropertyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * このクラスは、画面定義書の内容を保持するカタログです。
 * @author yuichi.kuwahara
 */
public class PageDefCatalog implements ApplicationContextAware, SourceCodeCatalog<SourceCode> {

	private static final Logger LOG = LoggerFactory.getLogger(PageDefCatalog.class);
	
	/**
	 * キー：ページ名、値：画面定義エンティティ
	 */
	private Map<String, PageDef> pageMap = new HashMap<String, PageDef>();
	private List<SourceCode> srcCdList = new ArrayList<SourceCode>();
	private String pageDefId;
	/**
	 * 画面のパスを物理名で出力する場合はtrue
	 */
	private boolean isPnamePath;
	@Resource
	DocumentRepository repo;
	@Resource
	CodeDefCatalog codeDefCatalog;
	@Resource
	PropertyManager pm;
	
	private ApplicationContext appCtx;
	private TreeDef tree;

	/**
	 * 画面一覧と画面仕様書を読み込み、
	 * ツリー定義エンティティ、ツリー定義エンティティに変換して内部に保持します。
	 */
	@PostConstruct
	public void load() {
		loadPageList();
		loadPage();
	}
	
	private void loadPage() {
		for (NodeDef node : getTree().getNodeList()) {
			if (!node.isPage()) {
				continue;
			}
			String pageSpecFilePath = pm.getPageSpec(node.getName());
			
			TableData itemDefList = null;
			try {
				LOG.info("画面定義書を読み込みます。{}", node.getName());
				itemDefList = repo.read(pageSpecFilePath, pm.getProperty("pagespec.sheet"));
			} catch (Exception e) {
				LOG.error("画面定義書の読み込みに失敗しました。{}", e.getMessage());
				continue;
			}
			PageDef page = appCtx.getBean(getPageDefId(), PageDef.class);
			page.setDomain(node.getDomain());
			page.setName(node.getName());
			if (isPnamePath()) {
				page.setPname(node.getPname());
				page.setParentPath(node.getParent() == null ? "" : node.getParent().getPname());
			} else {
				page.setPname(node.getName());
				page.setParentPath(node.getParentPath());
			}
			page.addContextParam(tree.getVar(), tree);
			node.setPage(page);
			add(page);
			page.load(itemDefList);
		}
	}
	
	private void loadPageList() {
		LOG.info("画面一覧を読み込みます。");
		TableData pageListData = repo.read(pm.getPageCatalog(), pm.getProperty("pagelist.sheet"));
		tree.load(pageListData);
		if (tree.isOutput()) {
			srcCdList.add(tree);
		}
		LOG.info("画面一覧から{}件のノードを読み込みました。", tree.getNodeList().size());
	}

	public void add(PageDef page) {
		srcCdList.add(page);
		pageMap.put(page.getName(), page);
	}

	public Collection<PageDef> getPages() {
		return pageMap.values();
	}

	public void write() {
		for (PageDef page : getPages()) {
			LOG.info("画面定義書を書き込みます。{}", page.getName());
			String outFilePath = pm.getPageSpec(page.getName());
			TableDataCatalog catalog = new TableDataCatalog();
			
			TableData tableData = page.toTableData();
			tableData.setName(pm.getProperty("pagespec.sheet"));
			catalog.add(tableData);
			repo.write(pm.getPageSpecTemplate(), outFilePath, catalog);
		}

	}

	@Override
	public void setApplicationContext(ApplicationContext appCtx) throws BeansException {
		this.appCtx = appCtx;
	}

	@Override
	public Collection<SourceCode> getAll() {
		return srcCdList;
	}

	/**
	 * 画面のパスを物理名で出力する場合はtrueを返します。
	 * @return 画面のパスを物理名で出力する場合はtrue
	 */
	public boolean isPnamePath() {
	    return isPnamePath;
	}

	/**
	 * 画面のパスを物理名で出力する場合はtrueを設定します。
	 * @param isPnamePath 画面のパスを物理名で出力する場合はtrue
	 */
	public void setPnamePath(boolean isPnamePath) {
	    this.isPnamePath = isPnamePath;
	}

	public TreeDef getTree() {
		return tree;
	}

	public void setTree(TreeDef tree) {
		this.tree = tree;
	}
	
	public String getPageDefId() {
		return pageDefId;
	}

	public void setPageDefId(String pageDefId) {
		this.pageDefId = pageDefId;
	}
	
}
