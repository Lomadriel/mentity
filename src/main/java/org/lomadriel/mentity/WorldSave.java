package org.lomadriel.mentity;

import java.io.Serializable;

/**
 * Image of the world at the save time.
 *
 * @author Jérôme BOULMIER
 * @since 1.0
 */
public class WorldSave implements Serializable {
	private static final long serialVersionUID = 7615221804208638510L;

	private final EntityManager entityManager;
	private final ComponentManager componentManager;

	/**
	 * Creates the world save.
	 *
	 * @param entityManager    world's entity manager.
	 * @param componentManager world's component manager.
	 */
	WorldSave(EntityManager entityManager, ComponentManager componentManager) {
		this.entityManager = entityManager;
		this.componentManager = componentManager;
	}

	EntityManager getEntityManager() {
		return this.entityManager;
	}

	ComponentManager getComponentManager() {
		return this.componentManager;
	}
}
