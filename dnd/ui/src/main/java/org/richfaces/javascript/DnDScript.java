package org.richfaces.javascript;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.ajax4jsf.javascript.ScriptWithDependencies;
import org.richfaces.resource.ResourceKey;

public abstract class DnDScript implements ScriptWithDependencies {

    private static final Set<ResourceKey> BASE_RESOURCES = new LinkedHashSet<ResourceKey>();

    static {
        BASE_RESOURCES.add(new ResourceKey("jquery.js", null));
        BASE_RESOURCES.add(new ResourceKey("jquery.position.js", null));
        BASE_RESOURCES.add(new ResourceKey("richfaces.js", null));
        BASE_RESOURCES.add(new ResourceKey("jquery-ui-core.js", "org.richfaces"));
        BASE_RESOURCES.add(new ResourceKey("jquery-dnd.js", "org.richfaces"));
    }

    private final String name;

    public DnDScript(String name) {
        this.name = name;
    }

    public Set<ResourceKey> getBaseResources() {
        return BASE_RESOURCES;
    }

    public void appendScriptToStringBuilder(StringBuilder stringBuilder) {
        try {
            appendScript(stringBuilder);
        } catch (IOException e) {
            // ignore
        }
    }

    public String toScript() {
        return name;
    }

    public void appendScript(Appendable target) throws IOException {
        target.append(name);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        DnDScript other = (DnDScript) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }

        return true;
    }

}
