package org.richfaces.renderkit.html;

import org.ajax4jsf.javascript.FunctionDefWithDependencies;

public interface ComponentValidatorScript extends FunctionDefWithDependencies {
    /**
     * <p class="changed_added_4_0">
     * Creates JavasCript that calls validator function.
     * </p>
     *
     * @param clientId
     * @param sourceId
     */
    String createCallScript(String clientId, String sourceId);
}
