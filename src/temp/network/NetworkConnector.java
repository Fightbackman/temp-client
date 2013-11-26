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

package temp.network;

import temp.base.ModuleConnector;
import temp.base.ModuleLinker;

public class NetworkConnector extends ModuleConnector {

	private NetworkConnection connection;

	@Override
	public void init() {
		connection = new NetworkConnection();
		ModuleLinker.registerPush(this, "connect");
		ModuleLinker.registerPush(this, "disconnect");
	}

	@Override
	protected void onInitEnd() {
	}

	@Override
	protected void onStop() {
	}

	@Override
	protected void onPush(String type, Object[] args) {
		switch (type) {
			case "connect":
				connection.connect(args);
				break;
			case "disconnect":
				connection.disconnect(args);
				break;
			default:
				break;
		}
	}

	@Override
	protected Object onPull(String key) {
		return null;
	}
}