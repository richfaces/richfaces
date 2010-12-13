package org.richfaces.renderkit.html;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.ajax4jsf.javascript.ScriptWithDependencies;
import org.richfaces.resource.ResourceKey;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

public class ClientOnlyScript extends ValidatorScriptBase {

    protected final ScriptWithDependencies converter;
    protected final Collection<? extends ScriptWithDependencies> validators;

    public ClientOnlyScript(ScriptWithDependencies clientSideConverterScript,
        Collection<? extends ScriptWithDependencies> validatorScripts) {
        super();
        if(null==clientSideConverterScript){
            this.converter = NULL_CONVERTER_SCRIPT;
        } else {
            this.converter = clientSideConverterScript;
        }
        this.validators = validatorScripts;
        
    }

    public Iterable<ResourceKey> getResources() {
        // TODO - make immutable.
        LinkedHashSet<ResourceKey> resources = Sets.newLinkedHashSet();
        Iterables.addAll(resources,converter.getResources());
        for (ScriptWithDependencies scriptString : validators) {
            Iterables.addAll(resources,scriptString.getResources());
        }
        return resources;
    }

    @Override
    protected Object buildBody() {
        StringBuilder body = new StringBuilder();
        // Get component value by clientId.
        body.append("var ").append(ClientValidatorRenderer.VALUE_VAR).append("=");
        GET_VALUE_FUNCTION.appendScriptToStringBuilder(body);
        body.append(EOL);
        // Try client-side validation
        body.append("try {\n");
        // convert value
        body.append("var ").append(ClientValidatorRenderer.CONVERTED_VALUE_VAR).append("=");
        converter.appendScriptToStringBuilder(body);
        body.append(EOL);
        // call validators
        for (ScriptWithDependencies validatorScript : validators) {
            validatorScript.appendScriptToStringBuilder(body);
            body.append(EOL);
        }
        finishValidation(body);
        body.append("return true;\n");
        // Catch errors
        body.append("} catch(e) {\n");
        SEND_ERROR_FUNCTION.appendScriptToStringBuilder(body);body.append(EOL);
        body.append("return false;\n}");
        return body;
    }

    protected void finishValidation(StringBuilder body) {
        // clear messages after successful validation
        body.append("if(!").append(DISABLE_AJAX).append("){\n");
        CLEAR_ERROR_FUNCTION.appendScriptToStringBuilder(body);
        body.append(EOL).append("}\n");
    }

    /* (non-Javadoc)
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

    /* (non-Javadoc)
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
        return true;
    }


}
