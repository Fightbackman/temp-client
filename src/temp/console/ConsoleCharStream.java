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

import temp.base.ModuleLinker;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.NoSuchElementException;

public class ConsoleCharStream implements Enumeration<Character>, Closeable {
	private InputStreamReader reader;
	private char c;
	private boolean lastRead = true;

	public ConsoleCharStream(InputStreamReader reader) {
		this.reader = reader;
	}

	@Override
	public boolean hasMoreElements() {
		try {
			if (!lastRead) {
				return true;
			}
			if (!reader.ready()) {
				return false;
			}
			c = (char)reader.read();
			if (Character.isWhitespace(c)) {
				c = ' ';
			}
			lastRead = false;
			return true;
		} catch (IOException e) {
			ModuleLinker.reportError(e.getLocalizedMessage());
			return false;
		}
	}

	@Override
	public Character nextElement() {
		if (!hasMoreElements()) {
			throw new NoSuchElementException();
		}
		lastRead = true;
		return c;
	}

	@Override
	public void close() {
		try {
			reader.close();
		} catch (IOException e) {
			ModuleLinker.reportError(e.getLocalizedMessage());
		}
	}
}
