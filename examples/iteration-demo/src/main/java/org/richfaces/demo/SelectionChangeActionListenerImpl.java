package org.richfaces.demo;

import javax.faces.event.AbortProcessingException;

import org.richfaces.event.TreeSelectionChangeEvent;
import org.richfaces.event.TreeSelectionChangeListener;

/**
 * User: Gleb Galkin Date: 23.02.11
 */
public class SelectionChangeActionListenerImpl implements TreeSelectionChangeListener {
    public void processTreeSelectionChange(TreeSelectionChangeEvent event) throws AbortProcessingException {
        System.out.println("Inside Selection Change Action");
    }
}
