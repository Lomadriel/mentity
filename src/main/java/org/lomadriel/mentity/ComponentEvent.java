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

import org.lomadriel.mentity.util.EventObject;

/**
 * Event fired when a component is added or removed.
 *
 * @author Jérôme BOULMIER
 * @since 1.0
 */
public final class ComponentEvent extends EventObject {
	/**
	 * Type of a {@code ComponentEvent}.
	 */
	public enum Type {
		ADDED,
		REMOVED
	}

	private final Type type;
	private final Class<? extends Component> componentClass;
	private final int entity;

	ComponentEvent(Object source, Type type, Class<? extends Component> componentClass, int entity) {
		super(source);

		this.type = type;
		this.componentClass = componentClass;
		this.entity = entity;
	}

	/**
	 * Returns the type of the event.
	 *
	 * @return the type of the event.
	 * @see Type
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * Returns the class of the component.
	 *
	 * @return the class of the component.
	 */
	public Class<? extends Component> getComponentClass() {
		return this.componentClass;
	}

	/**
	 * Returns the entity affected.
	 *
	 * @return the entity affected
	 */
	public int getEntity() {
		return this.entity;
	}
}
