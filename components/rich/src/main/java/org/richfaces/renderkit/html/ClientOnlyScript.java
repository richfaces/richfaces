package org.richfaces.renderkit.html;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashSet;

import org.ajax4jsf.javascript.JSFunctionDefinition;
import org.ajax4jsf.javascript.ScriptUtils;
import org.richfaces.resource.ResourceKey;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;

public class ClientOnlyScript extends ValidatorScriptBase {
    public static final ResourceKey CSV_RESOURCE = ResourceKey.create("csv.reslib", "org.richfaces");
    protected final LibraryScriptFunction converter;
    protected final ImmutableList<? extends LibraryScriptFunction> validators;
    private final ImmutableSet<ResourceKey> resources;
    protected final String onvalid;
    protected final String oninvalid;

    public ClientOnlyScript(LibraryScriptFunction clientSideConverterScript,
            Collection<? extends LibraryScriptFunction> validatorScripts, String onvalid, String oninvalid) {
        super();
        this.converter = clientSideConverterScript;
        this.validators = ImmutableList.copyOf(validatorScripts);
        LinkedHashSet<ResourceKey> resources = Sets.newLinkedHashSet();
        resources.add(CSV_RESOURCE);
        if (null != converter) {
            Iterables.addAll(resources, converter.getResources());
        }
        for (LibraryScriptFunction scriptString : validators) {
            Iterables.addAll(resources, scriptString.getResources());
        }
        this.resources = ImmutableSet.copyOf(resources);
        this.onvalid = onvalid;
        this.oninvalid = oninvalid;
    }

    public Iterable<ResourceKey> getResources() {
        return resources;
    }

    @Override
    public void appendFunctionName(Appendable target) throws IOException {
        target.append("window").append(DOT).append(super.getName()).append(EQUALS).append(FUNCTION);
    }

    @Override
    protected void appendParameters(Appendable target) throws IOException {
        if (null != converter) {
            target.append(CONVERTER).append(COLON);
            appendConverter(target, converter);
            target.append(COMMA);
        }
        target.append(VALIDATORS).append(COLON + LEFT_SQUARE_BRACKET);

        UnmodifiableIterator<? extends LibraryScriptFunction> iterator = validators.iterator();
        while (iterator.hasNext()) {
            LibraryScriptFunction validatorScript = iterator.next();
            appendValidator(target, validatorScript);
            if (iterator.hasNext()) {
                target.append(COMMA);
            }
        }
        target.append(RIGHT_SQUARE_BRACKET);
        appendAjaxParameter(target);
        if (oninvalid != null && oninvalid.trim().length() > 0) {
            target.append(COMMA);
            target.append("oninvalid:");
            target.append(new JSFunctionDefinition("messages").addToBody(oninvalid).toScript());
        }
        if (onvalid != null && onvalid.trim().length() > 0) {
            target.append(COMMA);
            target.append("onvalid:");
            target.append(new JSFunctionDefinition().addToBody(onvalid).toScript());
        }
    }

    protected void appendValidator(Appendable target, LibraryScriptFunction validatorScript) throws IOException {
        appendConverter(target, validatorScript);
    }

    protected void appendConverter(Appendable target, LibraryScriptFunction converter) throws IOException {
        target.append(LEFT_CURLY_BRACKET).append("f").append(COLON).append(converter.getName()).append(COMMA);
        target.append(PARAMS).append(COLON);
        ScriptUtils.appendScript(target, converter.getParameters());
        target.append(COMMA);
        target.append(MESSAGE).append(COLON);
        ScriptUtils.appendScript(target, converter.getMessage());
        target.append(RIGHT_CURLY_BRACKET);
    }

    protected void appendAjaxParameter(Appendable target) throws IOException {
        // This is client-only validation script.
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
        ClientOnlyScript other = (ClientOnlyScript) obj;
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
        if (onvalid == null) {
            if (other.onvalid != null) {
                return false;
            }
        } else if (!onvalid.equals(other.onvalid)) {
            return false;
        }
        if (oninvalid == null) {
            if (other.oninvalid != null) {
                return false;
            }
        } else if (!oninvalid.equals(other.oninvalid)) {
            return false;
        }
        return true;
    }
}
