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

import java.util.BitSet;
import java.util.Set;

/**
 * This class manages the systems. This class can't be construct.
 * Uses {@link WorldBuilder} to construct the world.
 *
 * @author Jérôme BOULMIER
 * @since 0.1
 */
public class World implements EntityListener {
	private static final String ENTITY_DOES_NOT_EXIST_MSG = "This entity doesn't exist";

	private final EntityManager entityManager;
	private final ComponentManager componentManager;
	private final FilteredSystemManager filteredSystemManager = new FilteredSystemManager(this);
	private final BaseSystem[] systems;
	private boolean hasToBeFlushed = true;

	World(Set<BaseSystem> systems) {
		this.entityManager = new EntityManager();
		this.componentManager = new ComponentManager();
		this.systems = systems.toArray(new BaseSystem[systems.size()]);

		init();
	}

	World(Set<BaseSystem> systems, WorldSave save) {
		this.entityManager = save.entityManager;
		this.componentManager = save.componentManager;
		this.systems = systems.toArray(new BaseSystem[systems.size()]);

		init();
	}

	/**
	 * Updates the systems using the order of the builder.
	 *
	 * @see WorldBuilder
	 */
	public void update() {
		for (BaseSystem system : this.systems) {
			system.update();
			flush();
		}
	}

	/**
	 * Clears all the entities and deletes all components.
	 */
	public void reset() {
		this.entityManager.reset();
		this.componentManager.reset();
		this.hasToBeFlushed = true;
	}

	/**
	 * Creates a new entity.
	 *
	 * @return the new entity.
	 */
	public int createEntity() {
		return this.entityManager.createEntity();
	}

	/**
	 * Creates a new entity using the given {@code prefabs}
	 *
	 * @param prefabs prefabs used to build the new entity.
	 * @return the new entity.
	 */
	public int createEntity(Prefabs prefabs) {
		int entity = createEntity();

		prefabs.initialize(this, entity);

		return entity;
	}

	/**
	 * Destroys the given {@code entity}.
	 *
	 * @param entity an entity
	 */
	public void destroyEntity(int entity) {
		if (!this.entityManager.entityExists(entity)) {
			return;
		}

		this.entityManager.destroyEntity(entity);
		this.componentManager.removeComponents(entity);
		this.hasToBeFlushed = true;
	}

	/**
	 * Adds the given {@code component} to the given {@code entity}.
	 *
	 * @param entity         an entity
	 * @param componentClass component's class
	 * @param component      component to add.
	 * @param <T>            component's class
	 * @throws IllegalArgumentException if the entity doesn't exist.
	 * @throws NullPointerException     if the component is null.
	 */
	public <T extends Component> void addComponent(int entity, Class<T> componentClass, T component) {
		if (!this.entityManager.entityExists(entity)) {
			throw new IllegalArgumentException(ENTITY_DOES_NOT_EXIST_MSG);
		}

		this.componentManager.addComponent(entity, componentClass, component);
		this.hasToBeFlushed = true;
	}

	/**
	 * Returns {@code true} if the given {@code entity} has the given component.
	 *
	 * @param entity         an entity
	 * @param componentClass component's class
	 * @param <T>            component's class
	 * @return {@code true} if the given {@code entity} has the given component, false otherwise.
	 * @throws IllegalArgumentException if the entity doesn't exist.
	 */
	public <T extends Component> boolean hasComponent(int entity, Class<T> componentClass) {
		if (!this.entityManager.entityExists(entity)) {
			throw new IllegalArgumentException(ENTITY_DOES_NOT_EXIST_MSG);
		}

		return this.componentManager.hasComponent(entity, componentClass);
	}

	/**
	 * Removes the given {@code component} of the given {@code entity}.
	 * <p>
	 * The component is removed at the end of the iteration.
	 *
	 * @param entity         an entity
	 * @param componentClass component's class
	 * @param <T>            component's class
	 * @throws IllegalArgumentException if the entity doesn't exist.
	 */
	public <T extends Component> void removeComponent(int entity, Class<T> componentClass) {
		if (!this.entityManager.entityExists(entity)) {
			throw new IllegalArgumentException(ENTITY_DOES_NOT_EXIST_MSG);
		}

		this.componentManager.removeComponent(entity, componentClass);
		this.hasToBeFlushed = true;
	}

	/**
	 * Gets the component mapper.
	 *
	 * @param componentClass component's class
	 * @param <T>            component's class
	 * @return the component mapper of the given {@code component}.
	 */
	public <T extends Component> ComponentMapper<T> getMapper(Class<T> componentClass) {
		return this.componentManager.getMapper(componentClass);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleNewEntity(int entity) {
		this.hasToBeFlushed = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleDeletedEntity(int entity) {
		this.hasToBeFlushed = true;
	}

	/**
	 * Returns a clone of the entities.
	 *
	 * @return the entites.
	 */
	public BitSet getEntities() {
		return this.entityManager.getEntities();
	}

	/**
	 * Creates an image of the world at time t.
	 *
	 * @return an image of the world at time t.
	 */
	public WorldSave save() {
		return new WorldSave(this.entityManager.clone(), this.componentManager.clone());
	}

	void registerFilteredEntitySystem(FilteredSystem filteredEntitySystem) {
		this.filteredSystemManager.register(filteredEntitySystem);
	}

	private void init() {
		for (BaseSystem system : this.systems) {
			system.setWorld(this);
			system.setup();
		}

		for (BaseSystem system : this.systems) {
			system.initialize();
		}

		flush();
	}

	private void flush() {
		if (this.hasToBeFlushed) {
			this.entityManager.flush();
			this.componentManager.flush();
			this.filteredSystemManager.updateAll();
			this.hasToBeFlushed = false;
		}
	}
}
