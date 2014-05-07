package org.richfaces.application.push;

/**
 * A Push session that needs to be destroyed when ending
 */
public interface DestroyableSession extends Session {

    /**
     * Destroy the session and associated resources
     */
    void destroy();
}