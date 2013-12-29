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

import java.io.File;
import java.io.IOException;
import javax.annotation.Resource;
import org.sitoolkit.core.infra.util.PropertyManager;
import org.sitoolkit.core.infra.util.SitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 入力ソースの変更を監視するクラスです。
 * @author yuichi.kuwahara
 */
public abstract class InputSourceWatcher {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	@Resource
	protected PropertyManager pm;
	/**
	 * 監視中ファイル
	 * このファイルが存在する間は監視が継続していることを意味します。
	 */
	protected final File continueFile = new File(".cg");

	/**
	 * 入力ソースを監視対象に追加します。
	 * 実際の処理はサブクラスに委譲します。
	 * また、プロセス開始後の初回実行時には監視中ファイルを作成します。
	 * このメソッドは繰り返し生成モードでない場合、何も行いません。
	 *
	 * @param inputSource 入力ソース
	 * @see #watchInputSource(java.lang.String)
	 * @see PropertyManager#isCGMode()
	 */
	public void watch(String inputSource) {
		if (!pm.isCGMode()) {
			return;
		}

		watchInputSource(inputSource);
		try {
			if (!continueFile.exists()) {
				continueFile.createNewFile();
			}
		} catch (IOException e) {
			throw new SitException(e);
		}
	}

	/**
	 * 入力ソースの監視を開始します。
	 * 実際の処理はサブクラスに委譲します。
	 * このメソッドは繰り返し生成モードでない場合、何も行いません。
	 *
	 * @param cg 繰り返し生成インターフェース
	 * @see #watchStart(org.sitoolkit.core.infra.repository.ContinuousGeneratable)
	 */
	public void start(ContinuousGeneratable cg) {
		if (!pm.isCGMode()) {
			return;
		}

		watchStart(cg);
	}

	/**
	 * 入力ソースを監視対象に追加する実際の処理を実装します。
	 * @param inputSource 入力ソース
	 */
	protected abstract void watchInputSource(String inputSource);

	/**
	 * 入力ソースの監視を開始します。
	 * サブクラスの実装責務は以下の通りです。
	 * <ul>
	 * <li>入力ソースの変更の監視
	 * <li>変更を検知した入力ソースで繰り返しインターフェースの再生成メソッドを実行
	 * <li>プロセスの常駐(監視中ファイルが無くなったら終了)
	 * </ul>
	 *
	 * @param cg 繰り返し生成インターフェース
	 * @see ContinuousGeneratable#regenerate(java.lang.String)
	 */
	protected abstract void watchStart(ContinuousGeneratable cg);
}
