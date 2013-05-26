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

import org.sitoolkit.core.domain.view.nav.NodeDef;
import org.sitoolkit.core.domain.view.nav.TreeDef;
import java.util.ArrayList;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author yuichi.kuwahara
 */
public class TreeDefTest {
	
	private TreeDef tree = new TreeDef();
	
	@Test
	public void testBuildTree() {
		List<NodeDef> nodeList = new ArrayList<NodeDef>();
		nodeList.add(new NodeDef("a/b/1.xhtml"));
		nodeList.add(new NodeDef("a/b/2.xhtml"));
		nodeList.add(new NodeDef("a/b"));
		nodeList.add(new NodeDef("a"));
		List<NodeDef> actual = tree.buildTree(nodeList);
		
		NodeDef root = actual.get(0);
		assertEquals("a", root.getPath());
		assertEquals("a/b", root.getChild(0).getPath());
		assertEquals("a/b/1.xhtml", root.getChild(0, 0).getPath());
		assertEquals("a/b/2.xhtml", root.getChild(0, 1).getPath());
	}
}
