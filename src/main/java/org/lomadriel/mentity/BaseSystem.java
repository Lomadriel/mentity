/*
 * mentity
 *
 * Copyright (c) 2016 Jérôme BOULMIER
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */

package org.lomadriel.mentity;

import java.lang.ref.WeakReference;

/**
 * Basic system used to perform operations.
 *
 * @author Jérôme BOULMIER
 * @since 0.1
 */
public abstract class BaseSystem {
	private transient WeakReference<World> world;

	void setWorld(World world) {
		this.world = new WeakReference<>(world);
	}

	protected World getWorld() {
		return this.world.get();
	}

	/**
	 * Setups the world.
	 * Called when the world is constructed.
	 */
	protected void setup() {
	}

	/**
	 * Initializes the system.
	 * Called by the world, once after setup.
	 */
	protected void initialize() {
	}

	/**
	 * Updates the system.
	 * Called by the world, at each update.
	 */
	protected abstract void update();
}
