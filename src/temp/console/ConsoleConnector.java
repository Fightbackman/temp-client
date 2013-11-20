/*
 * TEMP
 * Copyright (C) 2012-2013 Simon Roosen, Lukas Glitt, Olaf Matticzk
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package temp.console;

import temp.base.ModuleConnector;
import temp.base.ModuleLinker;

import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Module for Console Access. This Module is for development purposes only.
 */
public class ConsoleConnector extends ModuleConnector {
	public boolean load;
	public String name;

	boolean stop = false;
	Thread running = new Thread(new Runnable() {
		@Override
		public void run() {
			r();
		}
	});

	private void r() {
		try (ConsoleCharStream charStream = new ConsoleCharStream(new InputStreamReader(System.in))) {
			StringBuilder builder = new StringBuilder();
			while (!stop) {
				while (charStream.hasMoreElements()) {
					char c = charStream.nextElement();
					if (!(Character.getType(c) == Character.LINE_SEPARATOR)) {
						builder.append(c);
					}
				}
				if (builder.length() > 0) {
					String[] result = builder.toString().split(" +");
					if (result.length > 0 && result[0].equals("exit")) {
						break;
					}
					if (result.length == 0) {
						continue;
					}
					ModuleLinker.push(result[0], Arrays.copyOfRange(result, 1, result.length));
					builder = new StringBuilder();
				}
				try {
					Thread.sleep(150);
				} catch (InterruptedException e) {
				}
			}
		}
		System.out.println("ConsoleConnector Thread stopped");
	}

	@Override
	public void init() {
		ModuleLinker.registerPush(this, "ALL");
		running.setDaemon(false);
		running.setName("ConsoleIO");
	}

	@Override
	protected void onInitEnd() {
		running.start();
		System.out.println("ConsoleConnector ready:");
	}

	@Override
	protected void onStop() {
		stop = true;
		running.interrupt();
	}

	@Override
	protected void onPush(String type, Object[] args) {
		System.out.println(type);
		for (int i = 0; i < args.length; i++) {
			System.out.println(args[i].toString());
		}
	}

	@Override
	protected Object onPull(String key) {
		System.out.println("Pull: " + key);
		return null;
	}
}
