package org.lomadriel.mentity;

import java.io.Serializable;

/**
 * Image of the world at the save time.
 *
 * @author Jérôme BOULMIER
 */
public class WorldSave implements Serializable {
	private static final long serialVersionUID = 7615221804208638510L;

	final EntityManager entityManager;
	final ComponentManager componentManager;

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
}
