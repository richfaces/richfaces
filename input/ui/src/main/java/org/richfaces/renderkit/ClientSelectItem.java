package org.richfaces.renderkit;

import org.ajax4jsf.javascript.ScriptString;
import org.ajax4jsf.javascript.ScriptUtils;

public final class ClientSelectItem implements ScriptString {

    private String clientId;
    private String label;
    private String convertedValue;

    public ClientSelectItem(String convertedValue, String label) {
        this(convertedValue, label, null);
    }

    public ClientSelectItem(String convertedValue, String label,
            String clientId) {
        super();
        this.convertedValue = convertedValue;
        this.label = label;
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getLabel() {
        return label;
    }

    public String getConvertedValue() {
        return convertedValue;
    }

    public void appendScript(StringBuffer functionString) {
        functionString.append(this.toScript());
    }

    public String toScript() {
        return "{ 'id' : " + ScriptUtils.toScript(clientId)
                + " , 'label' : " + ScriptUtils.toScript(label)
                + ", 'value' : " + ScriptUtils.toScript(convertedValue)
                + "}";
    }
}
