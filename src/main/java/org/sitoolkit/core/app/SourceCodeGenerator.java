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

import java.util.Collection;
import javax.annotation.Resource;
import org.sitoolkit.core.infra.srccd.SourceCode;
import org.sitoolkit.core.infra.srccd.SourceCodeCatalog;
import org.sitoolkit.core.infra.util.SitFileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.sitoolkit.core.infra.repository.ContinuousGeneratable;
import org.sitoolkit.core.infra.repository.InputSourceWatcher;
import org.sitoolkit.core.infra.util.TextFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author yuichi.kuwahara
 */
public class SourceCodeGenerator implements ContinuousGeneratable {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	public static final String APP_CTX_CONFIG_LOCATION = "sitoolkit-conf.xml";

	private static ApplicationContext appCtx = new ClassPathXmlApplicationContext(APP_CTX_CONFIG_LOCATION);

	private SourceCodeCatalog<? extends SourceCode> catalog;
	/**
	 * 成果物名
	 */
	private String name;

	/**
	 * 生成対象に含む成果物の論理名
	 */
	private String[] includes = new String[0];

	private InputSourceWatcher watcher;

	public SourceCodeGenerator() {
		super();
	}

	public static ApplicationContext appCtx() {
		return appCtx;
	}

	public void generate(String... args) {
		Collection<? extends SourceCode> sources = getCatalog().getAll();
		log.info("{}本の{}ソースコードを生成します。", sources.size(), getName());
		generate(sources);
		if (watcher != null) {
			watcher.start(this);
		}
	}

	@Override
	public void regenerate(String inputSource) {
		Collection<? extends SourceCode> sources = getCatalog().reload(inputSource);
		log.info("{}本の{}ソースコードを再生成します。", sources.size(), getName());
		generate(sources);
	}

	protected void generate(Collection<? extends SourceCode> sources) {
		for (SourceCode source : sources) {

			if (includes.length > 0 &&
					!ArrayUtils.contains(includes, source.getName())) {
				log.info("{}[{}]は生成対象から除外します。", getName(), source.getName());
				continue;
			}

			TextFile file = source.toFile();
			if (!file.isEmpty()) {
				log.info("{}[{}]を生成します。", getName(), source.getName());
				SitFileUtils.write(file);
			}
		}
	}
	/**
	 *
	 * @param args
	 * @return
	 */
	public int execute(String[] args) {
		log.info("{}の生成を開始します。", getName());
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		try {
			generate(args);
			log.info("{}の生成が正常に終了しました。処理時間[{}]", getName(), stopWatch);
			return 0;
		} catch (Exception e) {
			log.info("{}の生成が異常終了しました。処理時間[{}]", getName(), stopWatch);
			log.error(e.getMessage(), e);
			return 1;
		} finally {
			stopWatch.stop();
		}
	}

	public SourceCodeCatalog<? extends SourceCode> getCatalog() {
		return catalog;
	}

	public void setCatalog(SourceCodeCatalog<? extends SourceCode> catalog) {
		this.catalog = catalog;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setIncludes(String str) {
		includes = StringUtils.isEmpty(str) ? new String[0] : str.split(",");
	}

	public InputSourceWatcher getWatcher() {
		return watcher;
	}

	public void setWatcher(InputSourceWatcher watcher) {
		this.watcher = watcher;
	}
}
