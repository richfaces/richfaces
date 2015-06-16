package org.richfaces.component.focus;

import java.text.MessageFormat;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.render.Renderer;
import javax.inject.Named;

import org.richfaces.application.ServiceTracker;
import org.richfaces.component.AbstractFocus;
import org.richfaces.focus.FocusManager;
import org.richfaces.renderkit.FocusRendererBase;

@Named
@RequestScoped
public class ComponentBean {

    public static final String NOT_RUN = "did not run";
    public static final String PASSED = "passed";

    private AbstractFocus component;
    private String createGlobalMessageAndCheckFocusResult = NOT_RUN;
    private String invalidateBothInputsAndCheckFocusResult = NOT_RUN;

    // used in ITFocusValidationAware#testGlobalMessageIsIgnored
    public void createGlobalMessageAndCheckFocus() {
        FacesContext context = FacesContext.getCurrentInstance();
        AbstractFocus comp = getComponent();
        FocusRendererBase renderer = getRenderer();

        // form should be focused
        final String expectedFocusCandidates = "form";
        String actualFocusCandidates = renderer.getFocusCandidatesAsString(context, comp);
        if (!expectedFocusCandidates.equals(actualFocusCandidates)) {
            createGlobalMessageAndCheckFocusResult = MessageFormat.format("Only form should be focused. Expected focus candidates <{0}>, but have: <{1}>.", expectedFocusCandidates, actualFocusCandidates);
            return;
        }

        // create global message
        context.addMessage(null, new FacesMessage("global message"));

        // form should be still focused
        actualFocusCandidates = renderer.getFocusCandidatesAsString(context, comp);
        if (!expectedFocusCandidates.equals(actualFocusCandidates)) {
            createGlobalMessageAndCheckFocusResult = MessageFormat.format("Only form should be focused. Expected focus candidates <{0}>, but have: <{1}>.", expectedFocusCandidates, actualFocusCandidates);
            return;
        }
        createGlobalMessageAndCheckFocusResult = PASSED;
    }

    @SuppressWarnings("unchecked")
    public <T extends AbstractFocus> T getComponent() {
        return (T) component;
    }

    public String getCreateGlobalMessageAndCheckFocusResult() {
        return createGlobalMessageAndCheckFocusResult;
    }

    public String getFocusCandidates() {
        FacesContext context = FacesContext.getCurrentInstance();
        FocusRendererBase renderer = getRenderer();
        return renderer.getFocusCandidatesAsString(context, component);
    }

    public String getInvalidateBothInputsAndCheckFocusResult() {
        return invalidateBothInputsAndCheckFocusResult;
    }

    @SuppressWarnings("unchecked")
    public <T extends Renderer> T getRenderer() {
        FacesContext context = FacesContext.getCurrentInstance();
        String componentFamily = component.getFamily();
        String rendererType = component.getRendererType();
        return (T) context.getRenderKit().getRenderer(componentFamily, rendererType);
    }

    // used in ITFocusValidationAware#testValidateMultipleInputsDuringFormSubmission and ITFocusValidationAware#testValidateMultipleInputsDuringAjax
    public void invalidateBothInputsAndCheckFocus() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        AbstractFocus comp = getComponent();
        FocusRendererBase renderer = getRenderer();

        // assert intial focus on form
        String expectedFocusCandidates = "form";
        String actualFocusCandidates = renderer.getFocusCandidatesAsString(facesContext, comp);
        if (!expectedFocusCandidates.equals(actualFocusCandidates)) {
            invalidateBothInputsAndCheckFocusResult = MessageFormat.format("Only form should be focused. Expected focus candidates <{0}>, but have: <{1}>.", expectedFocusCandidates, actualFocusCandidates);
            return;
        }

        // invalidate first two inputs
        for (String invalidate : new String[] { "form:input1", "form:input2" }) {
            facesContext.addMessage(invalidate, new FacesMessage("invalidated " + invalidate));
        }

        if (!facesContext.getClientIdsWithMessages().hasNext()) {
            invalidateBothInputsAndCheckFocusResult = "Messages should be generated.";
            return;
        }

        // assert focus on first input
        expectedFocusCandidates = "form:input1 form:input2";
        actualFocusCandidates = renderer.getFocusCandidatesAsString(facesContext, comp);
        if (!expectedFocusCandidates.equals(actualFocusCandidates)) {
            invalidateBothInputsAndCheckFocusResult = MessageFormat.format("First input should be focused. Expected focus candidates <{0}>, but have: <{1}>.", expectedFocusCandidates, actualFocusCandidates);
            return;
        }
        invalidateBothInputsAndCheckFocusResult = PASSED;
    }

    public void setComponent(AbstractFocus component) {
        this.component = component;
    }

    public void setFocusToSecondInput(ComponentSystemEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        FocusManager focusManager = ServiceTracker.getService(context, FocusManager.class);
        focusManager.focus("input2");
    }
}
