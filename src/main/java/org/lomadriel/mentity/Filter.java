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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Filter of entities.
 *
 * @author Jérôme BOULMIER
 * @since 0.2
 */
public class Filter implements Serializable {
	private static final long serialVersionUID = 847779566395542273L;

	private final Set<Class<? extends Component>> requiredComponents = new HashSet<>();
	private final Set<Class<? extends Component>> excludedComponents = new HashSet<>();

	public Filter(Class<? extends Component> requiredComponent) {
		this.requiredComponents.add(requiredComponent);
	}

	public Filter(Class<? extends Component> requiredComponent,
	              Class<? extends Component> excludedComponent) {
		this.requiredComponents.add(requiredComponent);
		this.excludedComponents.add(excludedComponent);
	}

	public Filter(Set<Class<? extends Component>> requiredComponents) {
		this.requiredComponents.addAll(requiredComponents);
	}

	public Filter(Set<Class<? extends Component>> requiredComponents,
	              Set<Class<? extends Component>> excludedComponents) {
		for (Class<? extends Component> requiredComponent : requiredComponents) {
			if (excludedComponents.contains(requiredComponent)) {
				throw new IllegalArgumentException();
			}
		}

		this.requiredComponents.addAll(requiredComponents);
		this.excludedComponents.addAll(excludedComponents);
	}

	Set<Class<? extends Component>> getRequiredComponents() {
		return this.requiredComponents;
	}

	Set<Class<? extends Component>> getExcludedComponents() {
		return this.excludedComponents;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Filter filter = (Filter) o;
		return this.requiredComponents.equals(filter.requiredComponents) &&
				this.excludedComponents.equals(filter.excludedComponents);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.requiredComponents, this.excludedComponents);
	}
}
