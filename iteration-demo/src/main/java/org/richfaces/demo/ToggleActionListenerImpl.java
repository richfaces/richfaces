package org.richfaces.demo;

import org.richfaces.event.TreeToggleEvent;

/**
 * User: Gleb Galkin Date: 23.02.11
 */
public class ToggleActionListenerImpl {
    public void processToggleAction(TreeToggleEvent event) {
        System.out.println("Inside Toggle Action: " + (event.isCollapsed() ? "collapsed tree" : "expanded tree"));
    }
}
