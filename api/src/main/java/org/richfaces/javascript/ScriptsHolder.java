package org.richfaces.javascript;

import java.util.Collection;

public interface ScriptsHolder {
    Collection<Object> getScripts();

    /**
     * <p class="changed_added_4_0">
     * </p>
     *
     * @return the pageReadyScripts
     */
    Collection<Object> getPageReadyScripts();
}