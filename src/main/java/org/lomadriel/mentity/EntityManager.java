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

import java.io.Serializable;
import java.util.BitSet;

/**
 * Class used to manage entities.
 *
 * @author Jérôme BOULMIER
 * @author Benoît CORTIER
 * @since 0.1
 */
class EntityManager implements Serializable {
	private static final long serialVersionUID = 2007045073473283304L;

	private final BitSet entities = new BitSet();
	private transient final BitSet removeQueue = new BitSet();

	private transient int nextIndex = 0;
	private transient int tempNextIndex = Integer.MAX_VALUE;

	EntityManager() {

	}

	/**
	 * Creates a new entity
	 *
	 * @return the new entity.
	 */
	int createEntity() {
		int entity = this.entities.nextClearBit(this.nextIndex);
		this.entities.set(entity);
		EventDispatcher.getInstance().fire(new EntityEvent(EntityEvent.Type.CREATED, entity));

		this.nextIndex = entity + 1;

		return entity;
	}

	void destroyEntity(int entity) {
		this.removeQueue.set(entity);
		EventDispatcher.getInstance().fire(new EntityEvent(EntityEvent.Type.DESTROYED, entity));

		if (entity < this.tempNextIndex) {
			this.tempNextIndex = entity;
		}
	}

	BitSet getEntities() {
		return this.entities;
	}

	/**
	 * Deletes all entities.
	 */
	void reset() {
		this.entities.clear();
		this.removeQueue.clear();
	}

	void flush() {
		this.entities.andNot(this.removeQueue);
		this.removeQueue.clear();

		if (this.nextIndex > this.tempNextIndex) {
			this.nextIndex = this.tempNextIndex;
			this.tempNextIndex = Integer.MAX_VALUE;
		}
	}
}
