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

import org.lomadriel.lfc.event.Event;

/**
 * Event fired when an entity is created or deleted.
 *
 * @author Jérôme BOULMIER
 * @since 0.3
 */
public class EntityEvent implements Event<EntityListener> {
	public enum Type {
		CREATED,
		DESTROYED
	}

	private final Type type;
	private final int entity;

	EntityEvent(Type type, int entity) {
		this.type = type;
		this.entity = entity;
	}

	@Override
	public void notify(EntityListener listener) {
		switch (this.type) {
			case CREATED:
				listener.handleNewEntity(this.entity);
				break;
			case DESTROYED:
				listener.handleDeletedEntity(this.entity);
				break;
			default:
				break;
		}
	}
}
