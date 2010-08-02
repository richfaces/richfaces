package org.ajax4jsf.component;

import java.util.ArrayList;
import java.util.List;

import javax.el.MethodExpression;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.render.RenderKitFactory;

import org.ajax4jsf.context.AjaxContext;
import org.ajax4jsf.context.InvokerCallback;
import org.ajax4jsf.renderkit.AjaxContainerRenderer;
import org.ajax4jsf.tests.AbstractAjax4JsfTestCase;
import org.ajax4jsf.tests.MockUIComponent;

public class AjaxViewRootTest extends AbstractAjax4JsfTestCase {
    private AjaxViewRoot _ajaxRoot;
    private MockUIComponent _mockComponent;
    private MockUIComponent _panel;
    private PhaseListenerImplementation _phaseListener;
    private PhaseListenerImplementation _testListener;

    public AjaxViewRootTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        _ajaxRoot = new AjaxViewRoot();
        _ajaxRoot.setViewId("/viewId");
        _ajaxRoot.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
        _testListener = new PhaseListenerImplementation();
        externalContext.getRequestMap().put("listener", _testListener);
        facesContext.setViewRoot(_ajaxRoot);
    }

    public void tearDown() throws Exception {
        _ajaxRoot = null;
        _testListener = null;
        super.tearDown();
    }

    public final void testProcessPhaseListeners() {
        MethodExpression beforeExpression = application.getExpressionFactory().createMethodExpression(elContext,
                                                "#{listener.beforePhase}", null, new Class<?>[] {PhaseEvent.class});
        MethodExpression afterExpression = application.getExpressionFactory().createMethodExpression(elContext,
                                               "#{listener.afterPhase}", null, new Class<?>[] {PhaseEvent.class});

        _ajaxRoot.setBeforePhaseListener(beforeExpression);
        _ajaxRoot.setAfterPhaseListener(afterExpression);

        PhaseListenerImplementation phaseListener = new PhaseListenerImplementation();

        _ajaxRoot.addPhaseListener(phaseListener);
        _ajaxRoot.processPhaseListeners(facesContext, PhaseId.RESTORE_VIEW, true);
        assertEquals(1, this._testListener.runsBefore);
        assertEquals(0, this._testListener.runsAfter);
        assertEquals(1, phaseListener.runsBefore);
        assertEquals(0, phaseListener.runsAfter);
        phaseListener.setPhaseId(PhaseId.PROCESS_VALIDATIONS);
        _ajaxRoot.processPhaseListeners(facesContext, PhaseId.RESTORE_VIEW, false);
        assertEquals(1, this._testListener.runsBefore);
        assertEquals(1, this._testListener.runsAfter);
        assertEquals(1, phaseListener.runsBefore);
        assertEquals(0, phaseListener.runsAfter);
    }

    public final void testCreatePhaseEvent() {
        PhaseEvent phaseEvent = _ajaxRoot.createPhaseEvent(facesContext, PhaseId.APPLY_REQUEST_VALUES);

        assertSame(facesContext, phaseEvent.getFacesContext());
        assertSame(PhaseId.APPLY_REQUEST_VALUES, phaseEvent.getPhaseId());
        assertSame(lifecycle, phaseEvent.getSource());
    }

    public final void testProcessPhase() {
        prepareView(PhaseId.APPLY_REQUEST_VALUES);

        InvokerCallbackImpl callback = new InvokerCallbackImpl() {
            @Override
            public void invokeContextCallback(FacesContext context, UIComponent component) {
                super.invokeContextCallback(context, component);
                component.processDecodes(context);
            }
        };

        _ajaxRoot.processPhase(facesContext, PhaseId.APPLY_REQUEST_VALUES, callback);

        // Check phase listeners calls.
        assertEquals(1, this._testListener.runsBefore);
        assertEquals(1, this._testListener.runsAfter);
        assertEquals(1, _phaseListener.runsBefore);
        assertEquals(1, _phaseListener.runsAfter);

        // Check callbacks runs.
        assertEquals(1, callback.runsRoot);
        assertEquals(1, callback.callbackComponents.size());
        assertSame(_panel, callback.callbackComponents.get(0));
        assertEquals(1, _mockComponent.getRunsDecode());
    }

    public final void testProcessAjaxPhase() {
        externalContext.addRequestParameterMap(AjaxContext.AJAX_CONTEXT_KEY, _ajaxRoot.getClientId(facesContext));
        ajaxContext.decode(facesContext);
        prepareView(PhaseId.APPLY_REQUEST_VALUES);

        InvokerCallbackImpl callback = new InvokerCallbackImpl() {
            @Override
            public void invokeContextCallback(FacesContext context, UIComponent component) {
                super.invokeContextCallback(context, component);
                component.processDecodes(context);
            }
        };

        _ajaxRoot.processPhase(facesContext, PhaseId.APPLY_REQUEST_VALUES, callback);

        // Check phase listeners calls.
        assertEquals(1, this._testListener.runsBefore);
        assertEquals(1, this._testListener.runsAfter);
        assertEquals(1, _phaseListener.runsBefore);
        assertEquals(1, _phaseListener.runsAfter);

        // Check callbacks runs.
        assertEquals(1, callback.runsRoot);
        assertEquals(1, callback.callbackComponents.size());
        assertSame(_panel, callback.callbackComponents.get(0));
        assertEquals(1, _mockComponent.getRunsDecode());
    }

    public final void testProcessAjaxRegionPhase() {
        prepareView(PhaseId.APPLY_REQUEST_VALUES);
        externalContext.addRequestParameterMap(AjaxContainerRenderer.AJAX_PARAMETER_NAME,
                _mockComponent.getClientId(facesContext));
        ajaxContext.decode(facesContext);

        InvokerCallbackImpl callback = new InvokerCallbackImpl() {
            @Override
            public void invokeContextCallback(FacesContext context, UIComponent component) {
                super.invokeContextCallback(context, component);
                component.processDecodes(context);
            }
        };

        _ajaxRoot.processPhase(facesContext, PhaseId.APPLY_REQUEST_VALUES, callback);

        // Check phase listeners calls.
        assertEquals(1, this._testListener.runsBefore);
        assertEquals(1, this._testListener.runsAfter);
        assertEquals(1, _phaseListener.runsBefore);
        assertEquals(1, _phaseListener.runsAfter);

        // Check callbacks runs.
        assertEquals(0, callback.runsRoot);
        assertEquals(1, callback.callbackComponents.size());
        assertSame(_mockComponent, callback.callbackComponents.get(0));
        assertEquals(1, _mockComponent.getRunsDecode());
    }

    /**
     * @param phaseId TODO
     *
     */
    private void prepareView(PhaseId phaseId) {
        MethodExpression beforeExpression = application.getExpressionFactory().createMethodExpression(elContext,
                                                "#{listener.beforePhase}", null, new Class<?>[] {PhaseEvent.class});
        MethodExpression afterExpression = application.getExpressionFactory().createMethodExpression(elContext,
                                               "#{listener.afterPhase}", null, new Class<?>[] {PhaseEvent.class});

        _ajaxRoot.setBeforePhaseListener(beforeExpression);
        _ajaxRoot.setAfterPhaseListener(afterExpression);
        _phaseListener = new PhaseListenerImplementation();
        _phaseListener.setPhaseId(phaseId);
        _ajaxRoot.addPhaseListener(_phaseListener);
        _panel = new MockUIComponent();
        _panel.setId("panel");
        _mockComponent = new MockUIComponent();
        _mockComponent.setId("component");
        _panel.getChildren().add(_mockComponent);
        _ajaxRoot.getChildren().add(_panel);
    }

    public final void testProcessDecodes() {
        prepareView(PhaseId.APPLY_REQUEST_VALUES);
        externalContext.addRequestParameterMap(AjaxContainerRenderer.AJAX_PARAMETER_NAME,
                _mockComponent.getClientId(facesContext));
        ajaxContext.decode(facesContext);
        _ajaxRoot.processDecodes(facesContext);

        // Check phase listeners calls.
        assertEquals(1, this._testListener.runsBefore);
        assertEquals(1, this._testListener.runsAfter);
        assertEquals(1, _phaseListener.runsBefore);
        assertEquals(1, _phaseListener.runsAfter);
        assertEquals(0, _panel.getRunsDecode());
        assertEquals(1, _mockComponent.getRunsDecode());
    }

    public final void testQueueEventFacesEvent() {

        // fail("Not yet implemented");
    }

    public final void testBroadcastEvents() {

        // fail("Not yet implemented");
    }

    public final void testProcessEvents() {

        // fail("Not yet implemented");
    }

    public final void testBroadcastAjaxEvents() {

        // fail("Not yet implemented");
    }

    public final void testGetEventsQueue() {

        // fail("Not yet implemented");
    }

    public final void testGetEvents() {

        // fail("Not yet implemented");
    }

    public final void testGetAjaxEventsQueue() {

        // fail("Not yet implemented");
    }

    public final void testClearEvents() {

        // fail("Not yet implemented");
    }

    /**
     * @author asmirnov
     *
     */
    public static class InvokerCallbackImpl implements InvokerCallback {
        private int runsRoot = 0;
        private List<UIComponent> callbackComponents = new ArrayList<UIComponent>();

        /*
         *  (non-Javadoc)
         * @see org.ajax4jsf.context.InvokerCallback#invokeContextCallback(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
         */
        public void invokeContextCallback(FacesContext context, UIComponent component) {
            callbackComponents.add(component);
        }

        /*
         *  (non-Javadoc)
         * @see org.ajax4jsf.context.InvokerCallback#invokeRoot(javax.faces.context.FacesContext)
         */
        public void invokeRoot(FacesContext context) {
            runsRoot++;
        }
    }


    public static final class PhaseListenerImplementation implements PhaseListener {
        private int runsAfter = 0;
        private int runsBefore = 0;
        private PhaseId phase = PhaseId.ANY_PHASE;

        public void afterPhase(PhaseEvent event) {
            runsAfter++;
        }

        public void beforePhase(PhaseEvent event) {
            runsBefore++;
        }

        public PhaseId getPhaseId() {
            return phase;
        }

        /**
         * @return the runsBefore
         */
        int getRunsBefore() {
            return runsBefore;
        }

        /**
         * @return the runsAfter
         */
        int getRunsAfter() {
            return runsAfter;
        }

        /**
         * @param phase the phase to set
         */
        void setPhaseId(PhaseId phase) {
            this.phase = phase;
        }
    }
}
