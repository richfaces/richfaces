package org.richfaces.component.focus;

import static org.junit.Assert.assertTrue;

import javax.faces.context.FacesContext;

import org.jboss.arquillian.warp.extension.phaser.BeforePhase;
import org.jboss.arquillian.warp.extension.phaser.Phase;
import org.richfaces.application.ServiceTracker;
import org.richfaces.focus.FocusManager;
import org.richfaces.focus.FocusRendererUtils;

public class VerifyFocusEnforcing extends AbstractComponentAssertion {

    private static final long serialVersionUID = 1L;

    private String enforceFocusId;

    /**
     * @param enforceFocusId clientId of input component to be enforced to gain focus
     */
    public VerifyFocusEnforcing(String enforceFocusId) {
        this.enforceFocusId = enforceFocusId;
    }

    @BeforePhase(Phase.RENDER_RESPONSE)
    public void enforce_rendering_second_input() {
        FacesContext context = FacesContext.getCurrentInstance();

        FocusManager focusManager = ServiceTracker.getService(context, FocusManager.class);
        focusManager.focus(enforceFocusId);

        assertTrue(FocusRendererUtils.isFocusEnforced(context));
    }
}
