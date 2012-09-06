package org.richfaces.renderkit.html;

import java.io.IOException;
import java.util.Collection;

import org.richfaces.resource.ResourceKey;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

public class ClientAndAjaxScript extends ClientOnlyScript {
    private final String ajaxScript;
    private final Iterable<ResourceKey> resources;

    public ClientAndAjaxScript(LibraryScriptFunction clientSideConverterScript,
            Collection<? extends LibraryScriptFunction> validatorScripts, String ajaxScript, String onvalid, String oninvalid) {
        super(clientSideConverterScript, validatorScripts, onvalid, oninvalid);
        this.ajaxScript = ajaxScript;
        Builder<ResourceKey> builder = ImmutableSet.<ResourceKey>builder();
        builder.add(AjaxOnlyScript.AJAX_RESOURCE);
        builder.addAll(super.getResources());
        resources = builder.build();
    }

    @Override
    public Iterable<ResourceKey> getResources() {
        return resources;
    }

    @Override
    protected void appendAjaxParameter(Appendable target) throws IOException {
        target.append(',');
        appendAjaxParameter(target, ajaxScript);
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
        result = prime * result + ((converter == null) ? 0 : converter.hashCode());
        result = prime * result + ((validators == null) ? 0 : validators.hashCode());
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
        ClientAndAjaxScript other = (ClientAndAjaxScript) obj;
        if (ajaxScript == null) {
            if (other.ajaxScript != null) {
                return false;
            }
        } else if (!ajaxScript.equals(other.ajaxScript)) {
            return false;
        }
        if (converter == null) {
            if (other.converter != null) {
                return false;
            }
        } else if (!converter.equals(other.converter)) {
            return false;
        }
        if (validators == null) {
            if (other.validators != null) {
                return false;
            }
        } else if (!validators.equals(other.validators)) {
            return false;
        }
        return true;
    }
}
