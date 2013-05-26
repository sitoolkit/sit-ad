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
package org.sitoolkit.core.domain.view.nav;

import java.util.*;
import org.sitoolkit.core.domain.view.PageDef;
import org.sitoolkit.core.infra.doc.DocumentElement;
import org.sitoolkit.core.infra.repository.RowData;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * このクラスは、画面メニュー階層の1階層を表します。
 * @author yuichi.kuwahara
 */
public class NodeDef extends DocumentElement {

	/**
	 * ノードタイプ
	 */
	private NodeType type;
	/**
	 * 画面定義
	 */
	private PageDef page;
	/**
	 * 画面ID
	 */
	private String id;
	/**
	 * ドメイン
	 */
	private String domain;
	/**
	 * 当該ノードのパス(論理名)
	 * 階層1/階層2/・・・[/論理名]
	 */
	private String path;
	/**
	 * 親ノード
	 */
	private NodeDef parent;

	/**
	 * 階層_1～Nの各名称
	 */
	private String[] dirs = new String[0];
	/**
	 * 子ノードのリスト
	 */
	private List<NodeDef> children = new ArrayList<NodeDef>();
	/**
	 * デフォルトコンストラクタです。
	 * 特に何も処理しません。
	 */
	public NodeDef() {
		super();
	}
	
	/**
	 * パスでインスタンスを初期化します。
	 * @param path 内部保持するパス
	 */
	public NodeDef(String path) {
		this();
		this.path = path;
	}

	/**
	 * 画面定義を取得します。
	 * @return 画面定義
	 */
	public PageDef getPage() {
		return page;
	}

	/**
	 * 画面定義を設定します。
	 * @param page 画面定義
	 */
	public void setPage(PageDef page) {
		this.page = page;
	}

	/**
	 * ドメインを取得します。
	 * @return ドメイン
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * ドメインを設定します。
	 * @param domain ドメイン
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * 画面IDを取得します。
	 * @return 画面ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * 画面IDを設定します。
	 * @param id 画面ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 当該ノードのパス(論理名)を取得します。
	 * @return 当該ノードのパス(論理名)
	 */
	public String getPath() {
		if (path == null) {
			if (isPage()) {
				path = StringUtils.join(
						ArrayUtils.add(getDirs(), getName()), "/");
			} else {
				path = StringUtils.join(getDirs(), "/");
			}
		}
		return path;
	}

	/**
	 * 当該ノードのパス(論理名)を設定します。
	 * @param path 当該ノードのパス(論理名)
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * 親ノードのパス(論理名)を返します。
	 * @return 親ノードのパス(論理名)
	 */
	public String getParentPath() {
		return getPath().contains("/") ? getPath().substring(0, getPath().lastIndexOf("/")) : "";
	}
	
	public boolean addChild(NodeDef node) {
		return children.add(node);
	}

	/**
	 * 子ノードのリストを取得します。
	 * @return 子ノードのリスト
	 */
	public List<NodeDef> getChildren() {
		return Collections.unmodifiableList(children);
	}

	public NodeDef getChild(int... indexes) {
		if (indexes.length == 0) {
			return this;
		}
		return getChildren().get(indexes[0]).getChild(Arrays.copyOfRange(indexes, 1, indexes.length));
	}

	/**
	 * ノードタイプを取得します。
	 * @return ノードタイプ
	 */
	public NodeType getType() {
		return type;
	}

	/**
	 * ノードタイプを設定します。
	 * @param type ノードタイプ
	 */
	public void setType(NodeType type) {
		this.type = type;
	}

	/**
	 * ノードタイプがページである場合にtrueを返します。
	 * @return ノードタイプがページである場合はtrue
	 */
	public boolean isPage() {
		return NodeType.Page.equals(getType());
	}

	/**
	 * 当該ノードのパス(物理名)を返します。
	 * @return 当該ノードのパス(物理名)
	 */
	public String getPpath() {
		StringBuilder sb = new StringBuilder();
		if (getParent() != null) {
			sb.append(getParent().getPpath());
			sb.append("/");
		}
		sb.append(getPname());
		return sb.toString();
	}
	
	/**
	 * 親ノードを取得します。
	 * @return 親ノード
	 */
	public NodeDef getParent() {
		return parent;
	}

	/**
	 * 親ノードを設定します。
	 * @param parent 親ノード
	 */
	public void setParent(NodeDef parent) {
		this.parent = parent;
	}
	
	/**
	 * 階層_1～Nの各名称を取得します。
	 * @return 階層_1～Nの各名称
	 */
	public String[] getDirs() {
		return dirs;
	}

	/**
	 * 階層_1～Nの各名称を設定します。
	 * @param dirs 階層_1～Nの各名称
	 */
	public void setDirs(String[] dirs) {
		this.dirs = dirs;
	}
	
}
