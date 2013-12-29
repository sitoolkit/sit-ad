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

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yuichi.kuwahara
 */
public class WatchServiceTrial {

	public static void main(String[] args) throws InterruptedException {
		File file = new File(".");

		Path path = file.toPath();
		final WatchService watcher;
		try {
			watcher = FileSystems.getDefault().newWatchService();
			path.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		WatchKey watchKey = watcher.take();
		while(true) {
			for (WatchEvent<?> event : watchKey.pollEvents()) {
				System.out.println(event + ", context:" + event.context() + ", kind:" + event.kind() + ", count:" + event.count());
			}
			watchKey.reset();
		}
	}
}
