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
package org.sitoolkit.core.infra.repository;

import org.sitoolkit.core.infra.repository.StringLoadableConverter;
import org.sitoolkit.core.infra.repository.StringLoadable;
import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.junit.BeforeClass;
import org.junit.Test;

public class StringLoadableConverterTest {

	@BeforeClass
	public static void setUpClass() {
		StringLoadableConverter converter = new StringLoadableConverter();
		ConvertUtils.register(converter, TestProperty.class);
	}
	
	@Test
	public void testSetProperty_101() throws IllegalAccessException, InvocationTargetException {
		TestBean bean = new TestBean();
		BeanUtils.setProperty(bean, "prop", "value");
		
		assertEquals("value", bean.getProp().val);
	}

	public class TestBean {
		private TestProperty prop;

		public TestProperty getProp() {
			return prop;
		}

		public void setProp(TestProperty prop) {
			this.prop = prop;
		}
	}

	public static class TestProperty implements StringLoadable {

		String val;
		
		@Override
		public void load(String str) {
			val = str;
		}
		
	}
}
