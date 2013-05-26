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

import org.sitoolkit.core.infra.repository.EnumConverter;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author yuichi.kuwahara
 */
public class EnumConverterTest {

	@BeforeClass
	public static void setUpClass() {
		ConvertUtils.register(new EnumConverter(), TestEnum.class);
	}
	
	@Test
	public void testSomeMethod() throws Exception {
		TestBean bean = new TestBean();
		BeanUtils.setProperty(bean, "testEnum", "a");
		
		assertEquals(TestEnum.a, bean.getTestEnum());
	}
	
	public static enum TestEnum {
		a,
		;
	}
	
	public static class TestBean {
		private TestEnum testEnum;

		public TestEnum getTestEnum() {
			return testEnum;
		}

		public void setTestEnum(TestEnum testEnum) {
			this.testEnum = testEnum;
		}
	}
}
