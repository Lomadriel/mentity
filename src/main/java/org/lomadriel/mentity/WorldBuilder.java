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

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class used to build the world.
 * Each systems should be inserted into the world builder.
 * Theses systems are sorted by priority and should be unique.
 *
 * @author Jérôme BOULMIER
 * @see Priority
 * @since 0.1
 */
public class WorldBuilder {
	/**
	 * Priority of a system.
	 */
	public enum Priority {
		LOWEST,
		LOWER,
		LOW,
		DEFAULT,
		HIGH,
		HIGHER,
		HIGHEST
	}


	private class Node {
		final BaseSystem system;
		final Priority priority;

		Node(BaseSystem system, Priority priority) {
			this.system = system;
			this.priority = priority;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Node node = (Node) o;
			return this.system.equals(node.system) &&
					this.priority == node.priority;
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.system, this.priority);
		}
	}

	private static final Comparator<Node> NODE_COMPARATOR
			= ((o1, o2) -> o2.priority.ordinal() - o1.priority.ordinal());

	private final Queue<Node> systems = new PriorityQueue<>(NODE_COMPARATOR);

	/**
	 * Adds a {@code system} in the {@code World} with the default priority.
	 * See also, {@link #addSystem(BaseSystem, Priority)}
	 *
	 * @param system a system to add with the default priority.
	 * @return itself
	 * @throws NullPointerException     if the system or the priority is null.
	 * @throws IllegalArgumentException if the system is already in this builder.
	 */
	public WorldBuilder addSystem(BaseSystem system) {
		return addSystem(system, Priority.DEFAULT);
	}

	/**
	 * Adds a {@code system} with the given {@code priority}.
	 * System are launched in priority order.
	 *
	 * @param system   a system
	 * @param priority system's priority
	 * @return itself
	 * @throws NullPointerException     if the system or the priority is null.
	 * @throws IllegalArgumentException if the system is already in this builder.
	 */
	public WorldBuilder addSystem(BaseSystem system, Priority priority) {
		if (system == null) {
			throw new NullPointerException("system can't be null");
		}

		if (priority == null) {
			throw new NullPointerException("priority can't be null");
		}

		Class<? extends BaseSystem> systemClass = system.getClass();

		for (Node node : this.systems) {
			if (node.system.getClass() == systemClass) {
				throw new IllegalArgumentException(systemClass.getName() + " is already in this builder.");
			}
		}

		this.systems.add(new Node(system, priority));

		return this;
	}

	/**
	 * Builds the world.
	 *
	 * @return the world.
	 */
	public World toWorld() {
		Set<BaseSystem> systems = this.systems.stream()
				.map(node -> node.system)
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return new World(systems);
	}

	/**
	 * Builds the world from a save.
	 *
	 * @return the world.
	 */
	public World toWorld(WorldSave save) {
		Set<BaseSystem> systems = this.systems.stream()
				.map(node -> node.system)
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return new World(systems, save);
	}
}
