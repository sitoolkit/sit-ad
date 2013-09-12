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
package org.sitoolkit.core.infra.util;

import java.net.URL;
import org.sitoolkit.core.infra.util.SitStringUtils;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kuwahara
 */
public class SitStringUtilsTest {

	public SitStringUtilsTest() {
	}

	@Test
	public void decodeToMap() {
		Map<String, String> map = SitStringUtils.decodeToMap("a:A,b:B", ",", ":");
		assertEquals("A", map.get("a"));
		assertEquals("B", map.get("b"));
	}

	@Test
	public void testToCamel() {
		assertEquals("camelStyle", SitStringUtils.toCamel("CAMEL_STYLE"));
		assertEquals("camelStyle", SitStringUtils.toCamel("camel_style"));
		assertEquals("camelStyle", SitStringUtils.toCamel("camelStyle"));
	}

	@Test
	public void testToPascal() {
		assertEquals("PascalStyle", SitStringUtils.toPascal("PASCAL_STYLE"));
		assertEquals("PascalStyle", SitStringUtils.toPascal("pascal_style"));
		assertEquals("PascalStyle", SitStringUtils.toPascal("pascalStyle"));
	}

	/**
	 * <dl>
	 * <dt>テスト対象
	 * <dd>@{@link SitStringUtils#url2filepath(java.net.URL) }
	 *
	 * <dt>テスト仕様
	 * <dd>
	 * <pre>
	 * http://localhost:9090/a/b/c.html
	 * → /a/b/c.html
	 *
	 * file:/C:/a/b/c.txt
	 * → /C:/a/b/c.txt
	 *
	 * file:/C:/a/b/c.jar!/e/f/g.txt
	 * → /e/f/g.txt
	 * </pre>
	 * </dl>
	 */
	@Test
	public void testUrl2filepath() throws Exception {
		assertEquals("/a/b/c.html",
			SitStringUtils.url2filepath(new URL("http://localhost:9090/a/b/c.html")));
		assertEquals("/C:/a/b/c.txt",
			SitStringUtils.url2filepath(new URL("file:/C:/a/b/c.txt")));
		assertEquals("/e/f/g.txt",
			SitStringUtils.url2filepath(new URL("file:/C:/a/b/c.jar!/e/f/g.txt")));
	}

	@Test
	public void testEscapeForJavaIdentifer() {
		assertEquals("ClassName", SitStringUtils.escapeForJavaIdentifer("9ClassName!"));
		assertEquals("はいいいえ", SitStringUtils.escapeForJavaIdentifer("はい・いいえ"));
	}
}
