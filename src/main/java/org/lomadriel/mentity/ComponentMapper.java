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
import org.lomadriel.mentity.util.Bag;
import org.lomadriel.mentity.util.EventHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.BitSet;

/**
 * Class used to manage the components.
 *
 * @param <T> component's class.
 * @author Jérôme BOULMIER
 * @since 0.1
 */
public class ComponentMapper<T extends Component> implements Serializable {
	private static final long serialVersionUID = 494271719946187185L;

	private final Class<T> componentClass;
	private final Bag<T> components;
	private final transient BitSet componentsBitSet = new BitSet(); // No need to serialize this.
	private final transient BitSet removeQueue = new BitSet();

	private transient EventHandler<ComponentEvent> onComponentAdded;
	private transient EventHandler<ComponentEvent> onComponentRemoved;

	ComponentMapper(Class<T> componentClass) {
		this.componentClass = componentClass;
		this.components = new Bag<>();
	}

	private ComponentMapper(ComponentMapper copy) {
		this.componentClass = copy.componentClass;
		this.components = copy.components;

		for (int i = 0; i < this.components.size(); i++) {
			if (this.components.get(i) != null) {
				this.componentsBitSet.set(i);
			}
		}
	}

	void setOnComponentAdded(EventHandler<ComponentEvent> eventHandler) {
		this.onComponentAdded = eventHandler;
	}

	void setOnComponentRemoved(EventHandler<ComponentEvent> eventHandler) {
		this.onComponentRemoved = eventHandler;
	}

	/**
	 * Adds the given component to the given entity.
	 *
	 * @param entity    an existing entity.
	 * @param component a component.
	 * @throws NullPointerException if the component is null.
	 */
	public void addComponent(int entity, T component) {
		assert (entity >= 0);

		EventDispatcher.getInstance().fire(new InternalEvent(entity));

		if (component == null) {
			throw new NullPointerException("Component can't be null");
		}

		component.entity = entity;

		this.components.set(entity, component);
		this.componentsBitSet.set(entity);

		this.onComponentAdded.handleEvent(new ComponentEvent(this,
				ComponentEvent.Type.ADDED,
				this.componentClass,
				entity));
	}

	/**
	 * Returns true if the given entity has the component, false otherwise.
	 *
	 * @param entity an entity
	 * @return Returns true if the entity has the component, false otherwise.
	 */
	public boolean hasComponent(int entity) {
		assert (entity >= 0);

		return this.componentsBitSet.get(entity);
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

		return this.components.get(entity);
	}

	/**
	 * Removes the component T of the given entity.
	 * The component is removed at the end of the iteration.
	 *
	 * @param entity an existing entity.
	 */
	public void removeComponent(int entity) {
		assert (entity >= 0);

		EventDispatcher.getInstance().fire(new InternalEvent(entity));

		this.removeQueue.set(entity);

		this.onComponentRemoved.handleEvent(new ComponentEvent(this,
				ComponentEvent.Type.REMOVED,
				this.componentClass,
				entity));
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
			this.components.set(i, null);
		}

		this.removeQueue.clear();
	}

	private Object readResolve() {
		return new ComponentMapper<>(this);
	}

	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
	}
}
