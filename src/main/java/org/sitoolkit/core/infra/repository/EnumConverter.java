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

import java.util.Arrays;
import org.sitoolkit.core.infra.util.LabeledEnum;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author yuichi.kuwahara
 */
public class EnumConverter implements Converter {

	@Override
	public Object convert(Class type, Object value) {
		Class[] interfaces = type.getInterfaces();
		if (ArrayUtils.contains(interfaces, LabeledEnum.class)) {
			for (Object enumConst : type.getEnumConstants()) {
				if (LabeledEnum.class.cast(enumConst).getLabel().equals(value)) {
					return enumConst;
				}
			}
			return null;
		} else {
			return Enum.valueOf(type, value.toString());
		}
	}

}
