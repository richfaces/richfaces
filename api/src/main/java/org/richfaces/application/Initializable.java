package org.richfaces.application;


/**
 * <p class="changed_added_4_0">Classes that require initialization and release methods should implement this interface</p>
 * @author asmirnov@exadel.com
 *
 */
public interface Initializable {

    public void init();
    
    public void release();

}