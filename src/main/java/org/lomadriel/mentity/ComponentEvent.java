package org.lomadriel.mentity;

import org.lomadriel.lfc.event.Event;

public class ComponentEvent implements Event<ComponentListener> {
	public enum Type {
		ADDED,
		REMOVED
	}

	private final Type type;
	private final Class<? extends Component> componentClass;
	private final int entity;

	ComponentEvent(Type type, Class<? extends Component> componentClass, int entity) {
		this.type = type;
		this.componentClass = componentClass;
		this.entity = entity;
	}

	public Type getType() {
		return this.type;
	}

	public Class<? extends Component> getComponentClass() {
		return this.componentClass;
	}

	public int getEntity() {
		return this.entity;
	}

	@Override
	public void notify(ComponentListener listener) {
		if (this.type.equals(Type.ADDED)) {
			listener.handleComponentAdded(this);
		} else {
			listener.handleComponentRemoved(this);
		}
	}
}
