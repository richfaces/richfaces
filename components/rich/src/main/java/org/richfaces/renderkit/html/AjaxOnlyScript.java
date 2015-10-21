package org.richfaces.renderkit.html;

import java.io.IOException;

import org.richfaces.resource.ResourceKey;

import com.google.common.collect.ImmutableSet;

public class AjaxOnlyScript extends ValidatorScriptBase {

    public static final Iterable<ResourceKey> AJAX_LIBRARIES = ImmutableSet.of(
            ResourceKey.create("javax.faces:jsf.js"),
            ResourceKey.create("org.richfaces:jquery.js"),
            ResourceKey.create("org.richfaces:richfaces.js"),
            ResourceKey.create("org.richfaces:richfaces-queue.reslib"),
            ClientOnlyScript.CSV_RESOURCE);

    private final String ajaxScript;

    public AjaxOnlyScript(String ajaxScript) {
        super();
        this.ajaxScript = ajaxScript;
    }

    public Iterable<ResourceKey> getResources() {
        return AJAX_LIBRARIES;
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
        result = prime * result + ((ajaxScript == null) ? 0 : ajaxScript.hashCode());
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
        AjaxOnlyScript other = (AjaxOnlyScript) obj;
        if (ajaxScript == null) {
            if (other.ajaxScript != null) {
                return false;
            }
        } else if (!ajaxScript.equals(other.ajaxScript)) {
            return false;
        }
        return true;
    }

    @Override
    protected void appendBody(Appendable target) throws IOException {
        target.append("if(!").append(DISABLE_AJAX).append("){(");
        appendAjaxFunction(target, ajaxScript);
        target.append(").call(").append(ELEMENT).append(",").append(EVENT).append(");");
        target.append(("}"));
    }
}
