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

import java.io.File;
import java.net.URI;

/**
 *
 * @author yuichi.kuwahara
 */
public class TextFile extends File {

	private String text;
	
	public TextFile(URI uri) {
		super(uri);
	}

	public TextFile(File parent, String child) {
		super(parent, child);
	}

	public TextFile(String parent, String child) {
		super(parent, child);
	}

	public TextFile(String pathname) {
		super(pathname);
	}
	
	public TextFile(String parent, String child, String text) {
		this(parent, child);
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	

	
}
