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

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
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

	private class Node implements Comparable<Node> {
		final System system;
		final Priority priority;

		Node(System system, Priority priority) {
			this.system = system;
			this.priority = priority;
		}

		@Override
		public int compareTo(Node o) {
			return this.priority.ordinal() - o.priority.ordinal();
		}
	}

	private final Set<Node> systems = new TreeSet<>();

	/**
	 * Adds a {@code system} in the {@code World} with the default priority.
	 * See also, {@link #addSystem(System, Priority)}
	 *
	 * @param system a system to add with the default priority.
	 * @return itself
	 * @throws NullPointerException     if the system or the priority is null.
	 * @throws IllegalArgumentException if the system is already in this builder.
	 */
	public WorldBuilder addSystem(System system) {
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
	public WorldBuilder addSystem(System system, Priority priority) {
		if (system == null) {
			throw new NullPointerException("System can't be null");
		}

		if (priority == null) {
			throw new NullPointerException("Priority can't be null");
		}

		Class<? extends System> systemClass = system.getClass();

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
		Set<System> systems = this.systems.stream()
				.map(node -> node.system)
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return new World(systems);
	}
}
