package org.richfaces.component.focus;

import static org.junit.Assert.assertFalse;

import javax.faces.context.FacesContext;

import org.jboss.arquillian.warp.jsf.AfterPhase;
import org.jboss.arquillian.warp.jsf.Phase;
import org.richfaces.component.AbstractFocus;
import org.richfaces.renderkit.FocusRendererBase;

public class VerifyFocusEnforcingOverridesFocusSettings extends VerifyFocusEnforcing {

    private static final long serialVersionUID = 1L;

    /**
     * @param enforceFocusId clientId of input component to be enforced to gain focus
     */
    public VerifyFocusEnforcingOverridesFocusSettings(String enforceFocusId) {
        super(enforceFocusId);
    }

    @AfterPhase(Phase.RENDER_RESPONSE)
    public void verify_focus_is_not_applied() {
        FacesContext context = FacesContext.getCurrentInstance();

        AbstractFocus component = bean.getComponent();
        FocusRendererBase renderer = bean.getRenderer();
        assertFalse(renderer.shouldApply(context, component));
    }
}
