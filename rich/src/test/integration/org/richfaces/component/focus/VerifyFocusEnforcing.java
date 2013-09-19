package org.richfaces.component.focus;

import static org.junit.Assert.assertTrue;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.PreRenderViewEvent;

import org.jboss.arquillian.warp.jsf.AfterPhase;
import org.jboss.arquillian.warp.jsf.BeforePhase;
import org.jboss.arquillian.warp.jsf.Phase;
import org.richfaces.services.ServiceTracker;
import org.richfaces.ui.misc.focus.FocusManager;
import org.richfaces.renderkit.focus.FocusRendererUtils;

public class VerifyFocusEnforcing extends AbstractComponentAssertion implements ComponentSystemEventListener {

    private static final long serialVersionUID = 1L;

    private String enforceFocusId;

    /**
     * @param enforceFocusId clientId of input component to be enforced to gain focus
     */
    public VerifyFocusEnforcing(String enforceFocusId) {
        this.enforceFocusId = enforceFocusId;
    }

    @BeforePhase(Phase.RENDER_RESPONSE)
    public void subscribe_to_preRenderViewEvent() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().subscribeToEvent(PreRenderViewEvent.class, this);
    }

    @AfterPhase(Phase.RENDER_RESPONSE)
    public void verify_focus_was_enforced() {
        FacesContext context = FacesContext.getCurrentInstance();
        assertTrue(FocusRendererUtils.isFocusEnforced(context));
    }

    @Override
    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
        if (event instanceof PreRenderViewEvent) {
            FacesContext context = FacesContext.getCurrentInstance();
            FocusManager focusManager = ServiceTracker.getService(context, FocusManager.class);
            focusManager.focus(enforceFocusId);
        }
    }
}
