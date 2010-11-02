package org.richfaces.renderkit.html;

import java.util.Collection;

import org.ajax4jsf.javascript.ScriptString;
import org.richfaces.validator.LibraryResource;

public interface ComponentValidatorScript extends ScriptString {
    
    String getName();

    Collection<LibraryResource> getResources();

    /**
     * <p class="changed_added_4_0">Creates JavasCript that calls validator function.</p>
     * @param clientId
     * @param sourceId 
     * @return
     */
    String createCallScript(String clientId, String sourceId);

    
}
