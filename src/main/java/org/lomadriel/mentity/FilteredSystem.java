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

import java.util.BitSet;

/**
 * System used to perform operations on specific entities using a filter.
 *
 * @author Jérôme BOULMIER
 * @see Filter
 * @since 0.2
 */
public abstract class FilteredSystem extends System {
	private static final long serialVersionUID = 424333379186904007L;

	private final Filter filter;
	transient BitSet entities;

	/**
	 * Constructs a new instance of {@code FilteredSystem}.
	 *
	 * @param filter entities' filter.
	 */
	public FilteredSystem(Filter filter) {
		this.filter = filter;
	}

	@Override
	protected final void setup() {
		super.setup();
		this.world.get().registerFilteredEntitySystem(this);
	}

	@Override
	protected final void update() {
		int entity = this.entities.nextSetBit(0);
		while (entity != -1) {
			update(entity);

			entity = this.entities.nextSetBit(entity + 1);
		}
	}

	/**
	 * Called for each entity which corresponds to the given {@code filter}.
	 *
	 * @param entity an entity.
	 */
	protected abstract void update(int entity);

	/**
	 * Returns the filter of this system.
	 *
	 * @return the filter.
	 */
	public Filter getFilter() {
		return this.filter;
	}
}
