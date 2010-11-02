package org.richfaces.renderkit.html;

import java.util.Collection;


public class ClientAndAjaxScript extends ClientOnlyScript{
    
    
    
    final String ajaxScript;
    
    public ClientAndAjaxScript(LibraryScriptString clientSideConverterScript,
        Collection<? extends LibraryScriptString> validatorScripts, String ajaxScript) {
        super(clientSideConverterScript,validatorScripts);
        this.ajaxScript = ajaxScript;
    }


    protected void finishValidation(StringBuffer body) {
        // AJAX callback
        body.append("if(!").append(DISABLE_AJAX).append("){\n");
        body.append(ajaxScript).append(EOL).append("}\n");
    }

    /* (non-Javadoc)
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
