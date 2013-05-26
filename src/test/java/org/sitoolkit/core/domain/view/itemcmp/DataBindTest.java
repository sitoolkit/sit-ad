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
package org.sitoolkit.core.domain.view.itemcmp;

import org.sitoolkit.core.domain.view.itemcmp.DataBind;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author kuwahara
 */
public class DataBindTest {
	
	@Test
	public void testDeserialize() {
		DataBind dataBind = DataBind.deserialize("t_table.col_1");
		assertEquals("t_table", dataBind.getTable());
		assertEquals("col_1", dataBind.getColumn());

		dataBind = DataBind.deserialize("eq t_table.col_1");
		assertEquals("eq", dataBind.getOperator());
		assertEquals("t_table", dataBind.getTable());
		assertEquals("col_1", dataBind.getColumn());

		dataBind = DataBind.deserialize("t_table-col_1");
		assertNull(dataBind);

		dataBind = DataBind.deserialize(null);
		assertNull(dataBind);

		dataBind = DataBind.deserialize("");
		assertNull(dataBind);
	}
}
