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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class used to manage systems which are filtered.
 *
 * @author Jérôme BOULMIER
 * @since 0.2
 */
class FilteredSystemManager {
	private final WeakReference<World> world;
	private final Map<Filter, List<FilteredSystem>> filteredEntitySystem = new HashMap<>();

	FilteredSystemManager(World world) {
		this.world = new WeakReference<>(world);
	}

	void updateAll() {
		for (Map.Entry<Filter, List<FilteredSystem>> filterListEntry : this.filteredEntitySystem.entrySet()) {
			BitSet entities = this.world.get().getEntities();

			for (Class<? extends Component> requiredComponent : filterListEntry.getKey().getRequiredComponents()) {
				entities.and(this.world.get().getMapper(requiredComponent).getEntitiesWithComponent());
			}

			for (Class<? extends Component> excludedComponent : filterListEntry.getKey().getExcludedComponents()) {
				entities.andNot(this.world.get().getMapper(excludedComponent).getEntitiesWithComponent());
			}

			for (FilteredSystem filteredSystem : filterListEntry.getValue()) {
				filteredSystem.entities = entities;
			}
		}

	}

	void register(FilteredSystem filteredEntitySystem) {
		Filter filter = filteredEntitySystem.getFilter();
		List<FilteredSystem> filteredSystems = this.filteredEntitySystem.get(filter);
		if (filteredSystems == null) {
			filteredSystems = new ArrayList<>();
		}

		filteredSystems.add(filteredEntitySystem);

		this.filteredEntitySystem.put(filter, filteredSystems);
	}
}
