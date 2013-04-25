package org.richfaces.demo.focus;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.richfaces.services.ServiceTracker;
import org.richfaces.ui.misc.focus.FocusManager;

@RequestScoped
@ManagedBean
public class FocusManagerBean {

    public void preRenderView() {
        FocusManager focusManager = ServiceTracker.getService(FocusManager.class);
        focusManager.focus("input2");
    }
}
