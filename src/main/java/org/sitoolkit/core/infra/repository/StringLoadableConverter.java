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

import org.apache.commons.beanutils.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringLoadableConverter implements Converter {

	private static final Logger LOG = LoggerFactory.getLogger(StringLoadableConverter.class);
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object convert(Class type, Object value) {
		try {
			StringLoadable ret = (StringLoadable) type.newInstance();
			ret.load(value.toString());
			return ret;
		} catch (InstantiationException e) {
			LOG.error("", e);
		} catch (IllegalAccessException e) {
			LOG.error("", e);
		}
		return null;
	}

}
