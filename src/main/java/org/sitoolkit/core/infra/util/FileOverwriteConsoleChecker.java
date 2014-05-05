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

import java.io.Console;
import java.io.File;
import java.io.IOException;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * このクラスは、ファイルを上書き可否をユーザーに確認するためのクラスです。
 * @author yuichi.kuwahara
 * @deprecated Mavenプロセス内で実行中はSystem.cnsoleが使用できないため、このクラスは使用しないで下さい。
 */
public class FileOverwriteConsoleChecker {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	private Writable allWritable = Writable.NA;

	private static final String QUESTION;

	private static final String CHOICE;

	@Resource
	PropertyManager pm;

	static {
		StringBuilder qsb = new StringBuilder();
		StringBuilder csb = new StringBuilder();
		for (Answer a : Answer.values()) {
			qsb.append(a.name());
			qsb.append(":");
			qsb.append(a.getDescription());
			qsb.append(" ");

			if (csb.length() > 0) {
				csb.append("、");
			}
			csb.append(a.name());
		}
		qsb.append(">");
		QUESTION = qsb.toString();

		CHOICE = csb.toString();
	}

	public boolean isWritable(File targetFile) {
		if (!targetFile.exists()) {
			return true;
		}

		if (pm.isRebuild()) {
			return true;
		}

		if (Writable.No.equals(allWritable)) {
			return false;
		} else if (Writable.Yes.equals(allWritable)) {
			return true;
		}

		Console console = System.console();
		if (console == null) {
			allWritable = Writable.No;
			log.warn("コンソールが使用できません。ファイルは上書きされません。{}",
					targetFile.getAbsolutePath());
			return false;
		}

		Writable writable = Writable.NA;
		System.out.println("書込み先にファイルが存在します。" + targetFile.getAbsolutePath());
		System.out.println("上書きしますか？");
		while(writable == Writable.NA) {
			console.printf(QUESTION);
			Answer answer = Answer.parse(console.readLine());
			switch (answer) {
				case y:
					writable = Writable.Yes;
					break;
				case a:
					writable = Writable.Yes;
					allWritable = Writable.Yes;
					break;
				case n:
					writable = Writable.No;
					break;
				case q:
					writable = Writable.No;
					allWritable = Writable.No;
					break;
				default:
					System.out.println(CHOICE + "のいずれかを入力してください。");
			}
		}
		return Writable.Yes.equals(writable);
	}

	enum Writable {
		Yes,
		No,
		NA
	}

	enum Answer {
		y("上書き"),
		a("以降全て上書き"),
		n("上書きしない"),
		q("以降全て上書きしない"),
		;
		private final String description;

		private Answer(String description) {
			this.description = description;
		}

		public String getDescription() {
			return description;
		}

		public static Answer parse(String s) {
			try {
				return valueOf(s.toLowerCase());
			} catch (IllegalArgumentException e) {
				return null;
			} catch (NullPointerException e) {
				return null;
			}
		}

	}

	public static void main(String[] args) throws IOException {
		File file1 = File.createTempFile("sit", FileOverwriteConsoleChecker.class.getSimpleName());
		file1.deleteOnExit();

		FileOverwriteConsoleChecker checker = new FileOverwriteConsoleChecker();
		checker.pm = new PropertyManager();

		for (int i = 0; i < 3; i++) {
			System.out.println("writable:" + checker.isWritable(file1));;
		}
	}
}
