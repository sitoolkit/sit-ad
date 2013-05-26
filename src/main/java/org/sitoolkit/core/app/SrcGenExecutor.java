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
package org.sitoolkit.core.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yuichi.kuwahara
 */
public class SrcGenExecutor {

	private static final Logger LOG = LoggerFactory.getLogger(SrcGenExecutor.class);
	
	public int generate(String...genTypes) {
		for (String genType : genTypes) {
			try {
				SourceCodeGenerator srcGen = SourceCodeGenerator.appCtx().getBean(genType, SourceCodeGenerator.class);
				srcGen.generate();
			} catch (Throwable e) {
				LOG.error("ソースコード生成の処理で例外が発生しました。", e);
				return 1;
			}
		}
		
		return 0;
	}
	
	public static void main(String[] args) {
		SrcGenExecutor generator = new SrcGenExecutor();
		System.exit(generator.generate(args));
	}
}
