/*
 * Copyright 2014 Monocrea Inc.
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
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yuichi.kuwahara
 */
public class ContinuousCopy {

	private static final Logger LOG  = LoggerFactory.getLogger(ContinuousCopy.class);
	private static final String SRC_DIR = "cc.srcdir";
	private static final String DST_DIR = "cc.dstdir";
	private static final String MONITOR_INTERVAL = "cc.interval";

	public static void main(String[] args) {
		System.exit(new ContinuousCopy().execute());
	}

	/**
	 * 繰り返しコピー処理を実行します。
	 * @return 0：正常終了、-1：コピー元ディレクトリの指定無し
	 *		-2：コピー先ディレクトリの指定無し
	 *		-3：予期しない例外発生
	 * @see #start(java.lang.String, java.lang.String)
	 */
	public int execute() {
		String srcDirStr = System.getProperty(SRC_DIR);
		if (StringUtils.isEmpty(srcDirStr)) {
			LOG.error("コピー元ディレクトリをVM引数{}で指定してください。", SRC_DIR);
			return -1;
		}
		String dstDirStr = System.getProperty(DST_DIR);
		if (StringUtils.isEmpty(dstDirStr)) {
			LOG.error("コピー先ディレクトリをVM引数{}で指定してください。", DST_DIR);
			return -2;
		}
		String srcDir = new File(srcDirStr).getAbsolutePath();
		String dstDir = new File(dstDirStr).getAbsolutePath();

		LOG.info("コピー元ディレクトリ:{}, コピー先ディレクトリ:{}",
				srcDir, dstDir);
		try {
			return start(srcDir, dstDir);
		} catch (Throwable e) {
			LOG.error("予期しない例外", e);
			return -3;
		}
	}

	/**
	 * コピー元ディレクトリの監視を開始します。
	 * コピー元ディレクトリ内のファイルが更新されたら、そのファイルをコピー先ディレクトリにコピーします。
	 *
	 * @param srcDir コピー元ディレクトリ
	 * @param dstDir コピー先ディレクトリ
	 * @return 0 (正常終了)
	 * @thｒrows Exception
	 */
	public int start(final String srcDir, final String dstDir) throws Exception {
		FileAlterationObserver observer = new FileAlterationObserver(srcDir);
		int interval = NumberUtils.toInt(System.getProperty(MONITOR_INTERVAL), 1000);
		final FileAlterationMonitor monitor
				= new FileAlterationMonitor(interval);

		FileAlterationListener listener = new FileAlterationListenerAdaptor() {
			@Override
			public void onFileChange(File file) {
				if (!file.isFile()) {
					return;
				}
				String relativePath = StringUtils.substringAfter(file.getAbsolutePath(), srcDir);
				File dstFile = new File(dstDir, relativePath);
				LOG.info("コピーします {} -> {}", file.getAbsolutePath(), dstFile.getAbsolutePath());
				try {
					FileUtils.copyFile(file, dstFile);
				} catch (IOException e) {
					LOG.error("ファイルコピーに失敗 ", e);
				}
			}
		};

		observer.addListener(listener);
		monitor.addObserver(observer);
		monitor.start();

		// TODO ファイル監視方式の統一
		String continuFilePath = System.getProperty("cc.cgfile", ".cg");
		ControlFile continueFile = new ControlFile(continuFilePath, new ControlFile.Listener() {
			@Override
			public void onDelete() throws Exception {
				monitor.stop();
			}
		});
		continueFile.watch();

		while(continueFile.exists()) {
			// 制御ファイルが存在し続ける間、処理を継続
		}

		return 0;
	}
}
