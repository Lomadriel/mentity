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

import org.lomadriel.mentity.util.EventHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.BitSet;

/**
 * Class used to manage entities.
 *
 * @author Jérôme BOULMIER
 * @author Benoît CORTIER
 * @since 0.1
 */
class EntityManager implements Serializable, Cloneable {
	private static final long serialVersionUID = 2007045073473283304L;

	private final BitSet entities;
	private final transient BitSet removeQueue = new BitSet();

	private transient int nextIndex;
	private transient int tempNextIndex = Integer.MAX_VALUE;

	private transient EventHandler<EntityEvent> onEntityCreated;
	private transient EventHandler<EntityEvent> onEntityRemoved;

	EntityManager() {
		this.entities = new BitSet();
	}

	private EntityManager(EntityManager copy) {
		this.entities = copy.entities;
	}

	void setOnEntityCreated(EventHandler<EntityEvent> eventHandler) {
		this.onEntityCreated = eventHandler;
	}

	void setOnEntityRemoved(EventHandler<EntityEvent> eventHandler) {
		this.onEntityRemoved = eventHandler;
	}

	/**
	 * Creates a new entity.
	 *
	 * @return the new entity.
	 */
	int createEntity() {
		int entity = this.entities.nextClearBit(this.nextIndex);
		this.entities.set(entity);

		if (this.onEntityCreated != null) {
			this.onEntityCreated.handleEvent(new EntityEvent(EntityEvent.Type.CREATED, entity));
		}

		this.nextIndex = entity + 1;

		return entity;
	}

	/**
	 * Checks the existence of an entity
	 *
	 * @param entity entity to check the existence
	 * @return {@code true} if the entity exists, {@code false} otherwise.
	 */
	boolean entityExists(int entity) {
		return this.entities.get(entity);
	}

	/**
	 * Destroys an entity.
	 *
	 * @param entity entity to destroy.
	 */
	void destroyEntity(int entity) {
		this.removeQueue.set(entity);

		if (this.onEntityRemoved != null) {
			this.onEntityRemoved.handleEvent(new EntityEvent(EntityEvent.Type.DESTROYED, entity));
		}

		if (entity < this.tempNextIndex) {
			this.tempNextIndex = entity;
		}
	}

	/**
	 * Returns entities.
	 *
	 * @return a {@code BitSet} containing the entities.
	 */
	BitSet getEntities() {
		return (BitSet) this.entities.clone();
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

	@Override
	public EntityManager clone() {
		EntityManager manager = null;

		try {
			manager = (EntityManager) super.clone();
		} catch (CloneNotSupportedException e) {
			// Unreachable
		}

		return manager;
	}

	private Object readResolve() {
		return new EntityManager(this);
	}

	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();

		this.tempNextIndex = Integer.MAX_VALUE;
	}
}
