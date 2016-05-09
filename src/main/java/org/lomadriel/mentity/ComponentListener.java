package org.lomadriel.mentity;

import java.util.EventListener;

public interface ComponentListener extends EventListener {
	void handleComponentAdded(ComponentEvent componentEvent);

	void handleComponentRemoved(ComponentEvent componentEvent);
}
