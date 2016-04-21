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
import java.util.HashSet;
import java.util.Set;

/**
 * This class manages the systems. This class can't be construct.
 * Uses {@link WorldBuilder} to construct the world.
 *
 * @author Jérôme BOULMIER
 * @since 0.1
 */
public class World implements EntityListener, Serializable {
	private static final long serialVersionUID = 3335361230408407617L;

	private final EntityManager entityManager = new EntityManager();
	private final ComponentManager componentManager = new ComponentManager();
	private transient final FilteredSystemManager filteredSystemManager = new FilteredSystemManager(this);
	private final Set<System> systems = new HashSet<>();
	private transient boolean hasToBeFlushed = true;

	World(Set<System> systems) {
		this.systems.addAll(systems);

		for (System system : systems) {
			system.setWorld(this);
			system.setup();
		}

		systems.forEach(System::initialize);

		flush();
	}

	/**
	 * Updates the systems using the priority order.
	 */
	public void update() {
		for (System system : this.systems) {
			system.update();
			flush();
		}
	}

	/**
	 * Clears all the entities.
	 */
	public void reset() {
		this.entityManager.reset();
		this.componentManager.reset();
		this.filteredSystemManager.reset();
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
	 * Destroys the given {@code entity}.
	 *
	 * @param entity an entity
	 */
	public void destroyEntity(int entity) {
		this.entityManager.destroyEntity(entity);
		this.componentManager.removeComponents(entity);
	}

	/**
	 * Adds the given {@code component} to the given {@code entity}.
	 *
	 * @param entity         an entity
	 * @param componentClass component's class
	 * @param component      component to add.
	 * @param <T>            component's class
	 */
	public <T extends Component> void addComponent(int entity, Class<T> componentClass, T component) {
		this.componentManager.addComponent(entity, componentClass, component);
	}

	/**
	 * Return {@code true} if the given {@code entity} has the given component.
	 *
	 * @param entity         an entity
	 * @param componentClass component's class
	 * @param <T>            component's class
	 * @return Return {@code true} if the given {@code entity} has the given component.
	 */
	public <T extends Component> boolean hasComponent(int entity, Class<T> componentClass) {
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
	 */
	public <T extends Component> void removeComponent(int entity, Class<T> componentClass) {
		this.componentManager.removeComponent(entity, componentClass);
	}

	/**
	 * Gets the component mapper.
	 *
	 * @param positionComponentClass component's class
	 * @param <T>                    component's class
	 * @return Returns the component mapper of the given {@code component}.
	 */
	public <T extends Component> ComponentMapper<T> getMapper(Class<T> positionComponentClass) {
		return this.componentManager.getMapper(positionComponentClass);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleNewEntity(int entity) {
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
		return (BitSet) this.entityManager.getEntities().clone();
	}

	void registerFilteredEntitySystem(FilteredSystem filteredEntitySystem) {
		this.filteredSystemManager.register(filteredEntitySystem);
	}

	private void flush() {
		if (this.hasToBeFlushed) {
			this.entityManager.flush();
			this.componentManager.flush();
			this.filteredSystemManager.updateAll(); // FIXME: updateAll when (an entity)/(a component) is added.
			this.hasToBeFlushed = false;
		}
	}

	// TODO: 4/21/16 Implement writeObject & readObject 
}
