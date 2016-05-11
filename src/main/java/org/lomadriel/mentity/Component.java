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

import java.io.Serializable;

/**
 * Component are data class.
 * Extends this class to define a new component.
 * Logic should be in a {@code BaseSystem} or a {@code FilteredSystem}.
 *
 * @author Jérôme BOULMIER
 * @see FilteredSystem
 * @see BaseSystem
 * @since 0.1
 */
public abstract class Component implements Serializable {
	private static final long serialVersionUID = 7657325065672262186L;

	int entity;

	/**
	 * Returns the id of the entity which owns the component.
	 *
	 * @return the id of the entity which owns the component.
	 */
	public final int getEntity() {
		return this.entity;
	}
}
