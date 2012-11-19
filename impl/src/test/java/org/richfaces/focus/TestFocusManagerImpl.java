package org.richfaces.focus;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.ajax4jsf.javascript.JSLiteral;
import org.jboss.test.faces.mockito.runner.FacesMockitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.richfaces.application.ServicesFactory;
import org.richfaces.javascript.JavaScriptService;
import org.richfaces.test.AbstractServicesTest;

@RunWith(FacesMockitoRunner.class)
public class TestFocusManagerImpl extends AbstractServicesTest {

    @Inject
    FacesContext facesContext;

    @Mock
    JavaScriptService javaScriptService;

    @Test
    public void test() {
        // having
        FocusManager focusManager = new FocusManagerImpl();

        Map<Object, Object> attributes = mock(Map.class);
        when(facesContext.getAttributes()).thenReturn(attributes);

        // when
        focusManager.focus(null);

        // then
        verify(attributes).put(FocusManager.FOCUS_CONTEXT_ATTRIBUTE, null);
        assertFalse(FocusRendererUtils.isFocusEnforced(facesContext));

        verifyZeroInteractions(javaScriptService);
    }

    @Test
    public void test2() {
        // having
        FocusManager focusManager = new FocusManagerImpl();
        String clientId = "someClientId";

        Map<Object, Object> attributes = spy(new HashMap<Object, Object>());
        when(facesContext.getAttributes()).thenReturn(attributes);

        // when
        focusManager.focus(clientId);

        // then
        verify(attributes).put(FocusManager.FOCUS_CONTEXT_ATTRIBUTE, clientId);
        assertTrue(FocusRendererUtils.isFocusEnforced(facesContext));

        verify(javaScriptService).addPageReadyScript(Mockito.any(FacesContext.class), Mockito.any(JSLiteral.class));
    }

    @Override
    protected void configureServices(ServicesFactory injector) {
        injector.setInstance(JavaScriptService.class, javaScriptService);
    }
}
