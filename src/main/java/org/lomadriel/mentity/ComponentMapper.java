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
import java.util.BitSet;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class used to manage the components.
 *
 * @param <T> component's class.
 * @author Jérôme BOULMIER
 * @since 0.1
 */
public class ComponentMapper<T extends Component> implements Serializable {
	private static final long serialVersionUID = 494271719946187185L;

	private final Map<Integer, T> components = new TreeMap<>();
	private final transient BitSet componentsBitSet = new BitSet(); // No need to serialize this.
	private final transient BitSet removeQueue = new BitSet();

	ComponentMapper() {
	}

	/**
	 * Returns the component of the given {@code entity}.
	 *
	 * @param entity an entity
	 * @return the component if the entity has it, null otherwise.
	 */
	public T getComponent(int entity) {
		assert (entity >= 0);
		assert (hasComponent(entity)) : "Useless call to getComponent(int entity)";

		return this.components.get(new Integer(entity));
	}

	/**
	 * Returns true if the given entity have the component, false otherwise.
	 *
	 * @param entity an entity
	 * @return Returns true if the entity have the component, false otherwise.
	 */
	public boolean hasComponent(int entity) {
		assert (entity >= 0);

		return this.componentsBitSet.get(entity);
	}

	/**
	 * Adds the given component to the given entity.
	 *
	 * @param entity    an existing entity.
	 * @param component a component.
	 * @throws NullPointerException if the component is null.
	 */
	public void addComponent(int entity, T component) {
		// TODO: Throws exception if the entity doesn't exist.
		assert (entity >= 0);

		// throws NullPointerException since TreeMap doesn't allow null value.
		this.components.put(new Integer(entity), component);
		this.componentsBitSet.set(entity);
	}

	/**
	 * Removes the component T of the given entity.
	 * The component is removed at the end of the iteration.
	 *
	 * @param entity an existing entity.
	 */
	public void removeComponent(int entity) {
		// TODO: Throws exception if the entity doesn't exist.
		assert (entity >= 0);

		this.removeQueue.set(entity);
	}

	/**
	 * Returns all entities with this component.
	 *
	 * @return all entities with this component.
	 */
	public BitSet getEntitiesWithComponent() {
		return this.componentsBitSet;
	}

	void flush() {
		this.componentsBitSet.andNot(this.removeQueue);
		for (int i = this.removeQueue.nextSetBit(0); i != -1; i = this.removeQueue.nextSetBit(i + 1)) {
			this.components.remove(new Integer(i));
		}

		this.removeQueue.clear();
	}
}
