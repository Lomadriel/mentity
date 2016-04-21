package org.lomadriel.mentity;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class EntityManagerTest {
	private final EntityManager entityManager = new EntityManager();

	@After
	public void clear() {
		this.entityManager.reset();
	}

	@Test
	public void addEntities() {
		for (int i = 0; i < 64; i++) {
			this.entityManager.createEntity();
		}

		long[] entities = this.entityManager.getEntities().toLongArray();

		Assert.assertEquals(1, entities.length);
		Assert.assertEquals(0xFFFFFFFF, entities[0]);
	}

	@Test
	public void cacheTest() {
		for (int i = 0; i < 64; i++) {
			this.entityManager.createEntity();
		}

		this.entityManager.destroyEntity(0);
		this.entityManager.destroyEntity(1);
		this.entityManager.flush();

		this.entityManager.createEntity();
		this.entityManager.createEntity();

		long[] entities = this.entityManager.getEntities().toLongArray();
		Assert.assertEquals(1, entities.length);
		Assert.assertEquals(0xFFFFFFFF, entities[0]);

		this.entityManager.destroyEntity(30);
		this.entityManager.destroyEntity(32);
		this.entityManager.flush();

		Assert.assertEquals(30, this.entityManager.createEntity());
		Assert.assertEquals(32, this.entityManager.createEntity());
	}
}
