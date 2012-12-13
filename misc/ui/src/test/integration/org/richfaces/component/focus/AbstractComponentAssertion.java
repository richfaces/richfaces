package org.richfaces.component.focus;

import javax.inject.Inject;

import org.jboss.arquillian.warp.Inspection;

public abstract class AbstractComponentAssertion extends Inspection {

    private static final long serialVersionUID = 1L;

    @Inject
    protected ComponentBean bean;
}
