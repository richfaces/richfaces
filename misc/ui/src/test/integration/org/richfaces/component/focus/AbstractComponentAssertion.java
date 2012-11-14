package org.richfaces.component.focus;

import javax.inject.Inject;

import org.jboss.arquillian.warp.ServerAssertion;

public abstract class AbstractComponentAssertion extends ServerAssertion {

    private static final long serialVersionUID = 1L;

    @Inject
    protected ComponentBean bean;
}
