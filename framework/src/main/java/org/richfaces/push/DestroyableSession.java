package org.richfaces.push;

/**
 * A Push session that needs to be destroyed when ending
 */
interface DestroyableSession extends Session {

    /**
     * Destroy the session and associated resources
     */
    void destroy();
}