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
import javax.annotation.Resource;
import org.sitoolkit.core.infra.repository.DocumentMapper;
import org.sitoolkit.core.infra.repository.RowData;
import org.sitoolkit.core.infra.repository.TableData;
import org.sitoolkit.core.infra.srccd.SourceCode;
import org.sitoolkit.core.infra.util.PropertyManager;

/**
 *
 * @author yuichi.kuwahara
 */
public class TreeDef extends SourceCode {

	/**
	 *
	 */
	private static final NodePathComparator PATH_COMP = new NodePathComparator();

	@Resource
	PropertyManager pm;

	@Resource
	DocumentMapper dm;

	private List<NodeDef> list = new ArrayList<NodeDef>();

	private List<NodeDef> nodeList = new ArrayList<NodeDef>();
	/**
	 * ソースコードとして出力する場合にtrue
	 */
	private boolean output;

	public void load(TableData pageListData) {
		for (RowData rowData : pageListData.getRows()) {
//			NodeDef node = new NodeDef();
//			node.load(rowData);
			NodeDef node = dm.map("nodeDef", rowData, NodeDef.class);
			nodeList.add(node);
		}

		list.addAll(buildTree(nodeList));
	}

	public List<NodeDef> buildTree(List<NodeDef> nodeList) {
		Collections.sort(nodeList, PATH_COMP);
		Map<String, NodeDef> nodeMap = new LinkedHashMap<String, NodeDef>();
		List<NodeDef> result = new ArrayList<NodeDef>();

		for (NodeDef node : nodeList) {
			NodeDef parentNode = nodeMap.get(node.getParentPath());
			if (parentNode == null) {
				result.add(node);
			} else {
				parentNode.addChild(node);
				node.setParent(parentNode);
			}
			nodeMap.put(node.getPath(), node);
		}

		return result;

	}

	/**
	 * ノードをパスで比較するComparator実装です。
	 */
	static class NodePathComparator implements Comparator<NodeDef> {

		@SuppressWarnings("unused")
		private static final long serialVersionUID = 1L;

		@Override
		public int compare(NodeDef o1, NodeDef o2) {
			return o1.getPath().compareTo(o2.getPath());
		}

	}

	public List<NodeDef> getNodeList() {
		return Collections.unmodifiableList(nodeList);
	}

	public List<NodeDef> getList() {
		return list;
	}

	public boolean isOutput() {
		return output;
	}

	public void setOutput(boolean output) {
		this.output = output;
	}

}
