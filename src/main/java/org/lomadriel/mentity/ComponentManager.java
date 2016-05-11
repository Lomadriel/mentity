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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Class used to manage the component mappers.
 *
 * @author Jérôme BOULMIER
 * @since 0.1
 */
public class ComponentManager implements Serializable, Cloneable {
	private static final long serialVersionUID = 1491726414158764138L;

	private final Map<Class<? extends Component>, ComponentMapper<? extends Component>> mappers = new HashMap<>();

	private transient EventHandler<ComponentEvent> onComponentAdded;
	private transient EventHandler<ComponentEvent> onComponentRemoved;

	ComponentManager() {
	}

	void setOnComponentAdded(EventHandler<ComponentEvent> eventHandler) {
		this.onComponentAdded = eventHandler;

		for (ComponentMapper<? extends Component> componentMapper : this.mappers.values()) {
			componentMapper.setOnComponentAdded(eventHandler);
		}
	}

	void setOnComponentRemoved(EventHandler<ComponentEvent> eventHandler) {
		this.onComponentRemoved = eventHandler;

		for (ComponentMapper<? extends Component> componentMapper : this.mappers.values()) {
			componentMapper.setOnComponentRemoved(eventHandler);
		}
	}

	/**
	 * Returns the mapper associated with the components.
	 *
	 * @param componentClass class of the component.
	 * @param <T>            class of the component.
	 * @return a mapper
	 */
	public <T extends Component> ComponentMapper<T> getMapper(Class<T> componentClass) {
		@SuppressWarnings("unchecked")
		ComponentMapper<T> mapper = (ComponentMapper<T>) this.mappers.get(componentClass);
		if (mapper == null) {
			mapper = new ComponentMapper<>(componentClass);

			mapper.setOnComponentAdded(this.onComponentAdded);
			mapper.setOnComponentRemoved(this.onComponentRemoved);

			this.mappers.put(componentClass, mapper);
		}

		return mapper;
	}

	/**
	 * Adds the given {@code component} to the given {@code entity}.
	 *
	 * @param entity         an entity
	 * @param componentClass component's class
	 * @param component      component
	 * @param <T>            type of the component.
	 * @throws NullPointerException if the component is null.
	 */
	public <T extends Component> void addComponent(int entity, Class<T> componentClass, T component) {
		getMapper(componentClass).addComponent(entity, component);
	}

	/**
	 * Returns {@code true} if the given {@code entity} has the given component.
	 *
	 * @param entity         an entity
	 * @param componentClass component's class
	 * @param <T>            component's class
	 * @return {@code true} if the given {@code entity} has the given component.
	 */
	public <T extends Component> boolean hasComponent(int entity, Class<T> componentClass) {
		return getMapper(componentClass).hasComponent(entity);
	}

	/**
	 * Removes the given {@code component} of the given {@code entity}.
	 * The component is removed at the end of the iteration.
	 *
	 * @param entity         an entity
	 * @param componentClass component's class
	 * @param <T>            component's class
	 */
	public <T extends Component> void removeComponent(int entity, Class<T> componentClass) {
		getMapper(componentClass).removeComponent(entity);
	}

	/**
	 * Removes all the components of the given {@code entity}.
	 * Components are removed at the end of the iteration.
	 *
	 * @param entity an existing entity
	 */
	void removeComponents(int entity) {
		for (Class<? extends Component> componentClass : this.mappers.keySet()) {
			getMapper(componentClass).removeComponent(entity);
		}
	}

	/**
	 * Removes all the components.
	 */
	void reset() {
		this.mappers.clear();
	}

	void flush() {
		this.mappers.values().forEach(ComponentMapper::flush);
	}

	@Override
	public ComponentManager clone() {
		ComponentManager manager = null;

		try {
			manager = (ComponentManager) super.clone();
		} catch (CloneNotSupportedException e) {
			// Unreachable
		}

		return manager;
	}
}
