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

import org.lomadriel.lfc.event.EventDispatcher;

import java.util.EventListener;

/**
 * Implements this interface to listen {@code EntityEvent}.
 * Then you have to register the listener.
 *
 * @author Jérôme BOULMIER
 * @see EntityEvent
 * @see EventDispatcher
 * @since 0.3
 */
public interface EntityListener extends EventListener {
	/**
	 * This method is called when an entity is created
	 *
	 * @param entity the new entity.
	 */
	void handleNewEntity(int entity);

	/**
	 * This method is called when an entity is added to the remove queue.
	 *
	 * @param entity the entity which will be destroyed.
	 */
	void handleDeletedEntity(int entity);
}
