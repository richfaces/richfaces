package org.richfaces.javascript;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.richfaces.resource.ResourceKey;

public final class DropScript extends DnDScript{

    private static final Set<ResourceKey> DRAG_SCRIPT = Collections.singleton(new ResourceKey("dnd-droppable.js", "org.richfaces"));
    
    public DropScript(String name) {
        super(name);
    }
    
    public Iterable<ResourceKey> getResources() {
        Set<ResourceKey> dragResourceKeys = new LinkedHashSet<ResourceKey>();
        dragResourceKeys.addAll(getBaseResources());
        dragResourceKeys.addAll(DRAG_SCRIPT);
        return dragResourceKeys;
    }
    
}
