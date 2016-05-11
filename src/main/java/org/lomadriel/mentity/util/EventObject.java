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

package org.lomadriel.mentity.util;

/**
 * Abstract event.
 *
 * @author Jérôme BOULMIER
 */
public abstract class EventObject {
	private final Object source;

	/**
	 * Constructs an event with no source.
	 */
	public EventObject() {
		this(null);
	}

	/**
	 * Constructs an event with a source.
	 *
	 * @param source - source of the event.
	 */
	public EventObject(Object source) {
		this.source = source;
	}

	/**
	 * Returns the source of the event. If the source is unknown, the source is null.
	 *
	 * @return the source of the event.
	 */
	public Object getSource() {
		return this.source;
	}
}
