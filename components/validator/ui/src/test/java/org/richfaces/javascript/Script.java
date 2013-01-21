package org.richfaces.javascript;

import java.io.IOException;
import java.util.Collections;

import org.ajax4jsf.javascript.ScriptWithDependencies;
import org.richfaces.resource.ResourceKey;

final class Script implements ScriptWithDependencies {
    static final ResourceKey FOO_RESOURCE = ResourceKey.create("foo", "org.rf");
    private final String name;

    Script(String name) {
        this.name = name;
    }

    public String toScript() {
        return name;
    }

    public void appendScript(Appendable target) throws IOException {
        target.append(name);
    }

    public void appendScriptToStringBuilder(StringBuilder stringBuilder) {
        try {
            appendScript(stringBuilder);
        } catch (IOException e) {
            // ignore
        }
    }

    public Iterable<ResourceKey> getResources() {
        return Collections.singleton(FOO_RESOURCE);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
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
        Script other = (Script) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    public String getName() {
        return name;
    }
}