package org.richfaces.component.focus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.jboss.arquillian.warp.jsf.AfterPhase;
import org.jboss.arquillian.warp.jsf.BeforePhase;
import org.jboss.arquillian.warp.jsf.Phase;
import org.richfaces.component.AbstractFocus;
import org.richfaces.renderkit.FocusRendererBase;

import com.google.common.base.Splitter;

public class VerifyFocusCandidates extends AbstractComponentAssertion {

    private static final long serialVersionUID = 1L;

    private String invalidatedComponents;
    private String expectedFocusCandidates;
    private String message;

    /**
     *
     * @param message message to be thrown during focus candidates verification
     * @param invalidatedComponents space separated list of components to invalidate
     * @param focusCandidates candidates for gaining focus
     */
    public VerifyFocusCandidates(String message, String invalidatedComponents, String focusCandidates) {
        this.invalidatedComponents = invalidatedComponents;
        this.expectedFocusCandidates = focusCandidates;
        this.message = message;
    }

    @BeforePhase(Phase.RENDER_RESPONSE)
    public void invalidate_first_input() {
        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (invalidatedComponents != null) {
            for (String invalidate : Splitter.on(" ").split(invalidatedComponents)) {
                facesContext.addMessage(invalidate, new FacesMessage("invalidated " + invalidate));
            }

            assertTrue(facesContext.getClientIdsWithMessages().hasNext());
        }
    }

    @AfterPhase(Phase.RENDER_RESPONSE)
    public void verify_focus_candidates() {

        FacesContext context = FacesContext.getCurrentInstance();

        AbstractFocus component = bean.getComponent();
        FocusRendererBase renderer = bean.getRenderer();

        String actualFocusCandidates = renderer.getFocusCandidatesAsString(context, component);

        assertEquals(message, expectedFocusCandidates, actualFocusCandidates);
    }
}
