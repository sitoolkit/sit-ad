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

import java.text.MessageFormat;

/**
 * 仕様書内の項目の記載書式が不正であることを表す例外クラスです。
 * 
 * @author Yuichi Kuwahara
 */
public class FormatException extends RuntimeException {

	/**
	 * Creates a new instance of <code>FormatException</code> without detail message.
	 */
	public FormatException() {
	}

	/**
	 * Constructs an instance of <code>FormatException</code> with the specified detail message.
	 * @param msg the detail message.
	 */
	public FormatException(String msg) {
		super(msg);
	}
	
	public FormatException(String pattern, Object...arguments) {
		this(MessageFormat.format(pattern, arguments));
	}
}
