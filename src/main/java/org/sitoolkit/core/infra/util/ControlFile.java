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
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author yuichi.kuwahara
 */
public class ControlFile {

	private static final Logger LOG = LoggerFactory.getLogger(ControlFile.class);
	private File controlFile;
	private long threadSleepInterval = 1000;
	private long monitorInterval = 1000;
	private Listener listener;

	/**
	 * コンストラクタ
	 * @param path 制御ファイルのパス
	 * @param listener 制御ファイルのイベントハンドラを実装するリスナー
	 */
	public ControlFile(String path, Listener listener) {
		this.controlFile = new File(path);
		this.listener = listener;
	}

	/**
	 * 制御ファイルの監視を開始します。
	 */
	public void watch() {
		LOG.info("制御ファイルを監視します {}", controlFile.getAbsolutePath());
		FileAlterationObserver observer = new FileAlterationObserver(
				controlFile.getAbsoluteFile().getParent(), new FileFileFilter() {
			@Override
			public boolean accept(File file) {
				return file.equals(controlFile);
			}
		});
		final FileAlterationMonitor monitor
				= new FileAlterationMonitor(monitorInterval);
		FileAlterationListener fileListener = new FileAlterationListenerAdaptor() {

			@Override
			public void onFileDelete(File file) {
				LOG.info("制御ファイルが削除されました {}", file.getAbsolutePath());
				try {
					listener.onDelete();
				} catch (Exception e) {
					throw new SitException(e);
				}
			}

		};
		observer.addListener(fileListener);
		monitor.addObserver(observer);

		try {
			monitor.start();
		} catch (Exception e) {
			throw new SitException(e);
		}
	}

	public boolean exists() {
		try {
			Thread.sleep(threadSleepInterval);
		} catch (InterruptedException e) {
			LOG.debug("スレッドの待機に失敗", e);
		}
		return controlFile.exists();
	}

	/**
	 * 制御ファイルのイベントハンドラを実装するためのリスナーです。
	 */
	public static interface Listener {
		/**
		 * 制御ファイルが削除された後に呼び出されるメソッドです。
		 * @throws Exception ハンドラ内で送出する例外
		 */
		void onDelete() throws Exception ;
	}
}
